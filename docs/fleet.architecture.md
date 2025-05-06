# EDC Fleet Coordination Architecture

This document outlines an architecture and implementation plan for the coordination of multiple Connector
deployments, termed _**Fleet Coordination**_. Note that the term `EDC` is used to refer to an EDC-based implementation
of the [Dataspace Protocol Specification (DSP)](https://github.com/eclipse-dataspace-protocol-base/DataspaceProtocol),
while `Connector` is used to refer to a generic implementation that may or may not be based on EDC.

## Problem Statement
                            
Organizations may control multiple Connector deployments. For example, individual divisions may run their own EDC
instances
using [Management Domains](https://eclipse-edc.github.io/documentation/for-adopters/distributions-deployment-operations/#management-domains).
Other examples of multiple deployments include applications with embedded Connectors and
Connector service providers. The upshot of these heterogeneous environments is that organizations are faced with the
challenge of ensuring consistency and transparency across diverse deployments. For example, an organization may
want to enforce a compliance requirement that all data-sharing must adhere to a particular policy, regardless of the
Connector that enacts a particular data-sharing agreement. Similarly, an organization may want to track data-sharing
activities such as signed agreements across all Connector deployments.

We term all deployments under the control of a single organization a _**fleet**_. The concept of a fleet implies that a
particular deployment (ship) is operated autonomously (its crew) but in coordination (a fleet commander or admiral). The
problems of ensuring artifact consistency and event observability across deployments in a fleet is termed
_**fleet coordination**_.

### Example Scenarios

We will use the following example to explain concepts introduced in this document:

> Acme Corporation deploys a set of connectors in its own datacenters and also subscribes to a hosted service for
> sharing parts manufacturing data. The hosted service uses a highly customized EDC implementation.

### Fleet Coordination vs. Connector as a Service vs. Management Domains

Fleet Coordination differs from Connector as Service in that it is only concerned with maintaining consistency of
artifacts and the ability to receive events across Connector deployments belonging to a single organization. It is not
concerned with provisioning or operating those deployments. In contrast, Connector as a Service involves building
provisioning and operations infrastructure for Connectos that belong to multiple organizations.

Management domains enable an organization to delegate operational responsibility of EDC components throughout an
organization. For example, organizational subdivisions may elect to manage data sharing completely independently or
delegate some responsibilities such as operating data planes to other units. A deployment is therefore a management
domain, and we will use the two terms interchangeably.

Fleet coordination is a corollary to management domains. The latter allows an organization to present a unified
data catalog to external partners even though the contents may be served by independent Connectors. Fleet coordination
completes the picture by enabling unified control of key artifacts such as policies and schemas across management
domains.

## Problem 1: Consistency and Artifact Deployment

The problem of artifact deployment is summarized as follows:

> How are data-sharing resources coordinated across disparate applications and Connector runtimes in an organization?

These resources include:

- Access control policies
- Usage control policies
- Contract definitions
- Verifiable credential schemas
- Trust anchors

In the context of our example scenario, Acme Corporation wants to deploy policies and policy definitions across
connectors in their datacenter and to the hosted service.

One potential approach is to implement a push architecture based on a standardized API implemented by all
Connector distributions. Defining a common API is not practical given the diverse nature of Connector deployments. In
addition, it is overly complex. If each deployment adopted a common API, a coordinator would be needed to reliably push
changes to all Connector runtimes. This coordinator would have to be aware of all deployments (to push changes),
maintain state about those deployments (to know what to push), and support reliable delivery over a substantial period
of time(to handle the case where a deployment is unavailable or offline).

Rather, a loosely coupled approach has been chosen. Instead of a push model, fleet coordination will operate on a
declarative pull architecture similar to Kubernetes. Artifacts will be committed to a registry that can then be used to
calculate the desired deployment "state". It will be the responsibility of each deployment, or management domain, to
_**reconcile**_ the current state of its runtimes with the registry. This reconciliation process is depicted below:

![](/docs/reconciliation.png)

This architecture has a number of benefits:

- There is no need for a standard management API.
- There is no need for central coordinator that has knowledge of all deployments. Each deployment is responsible for
  maintaining its own state calculated from the registry.
- There is no need for reliable delivery. If a deployment is unavailable or a new one comes online, it simply
  synchronizes its last known state against the current state of the registry.
- It integrates well with continuous delivery (CD) pipelines. Artifacts can be developed in repositories under version
  control and committed to the registry as part of a CD process.
- It scales up and down. As will be detailed, the registry can be a simple file-based implementation or a server
  allowing it to be used with single-instance or complex multi-cloud setups.

This architecture relies on the concept of eventual consistency. After changes have been pushed to the registry, there
will be an interval where changes are not applied uniformly to all deployments. The length of this interval depends on
the reconciliation cycle. Note, however, that this is no different from a push approach, which would also be subject to
a similar interval while all deployments are updated.

### xRegistry: A Standard Approach to Extensibility

The registry will implement the CNCF [xRegistry](https://github.com/xregistry/spec) specification. As its name implies,
xRegistry defines an extensible data model and API for storing and retrieving resources. These resources can be schemas,
message definitions, endpoint definitions, or other artifacts such as policies. Fleet coordination will define xRegistry
extensions for all managed artifacts as well as provide an xRegistry implementation.

Since xRegistry defines a model for storing schemas and other definitions, it can be used to manage additional
resources such as Verifiable Credential schemas, message schemas, and trust lists.

### Artifact Deployment Implementation

The xRegistry implementation and Reconciler will be maintained in the EDC project since it is generally applicable
to all dataspaces.

### xRegistry Implementation

The xRegistry implementation will consist of a set of composable modules. The core modules will implement:

- Parsers for serializing and deserializing xRegistry types
- An extensible, type system for manipulating xRegistry data and artifacts
- An data validation system
- Support for dataspace artifact types such as policies
- Java-based build plugins for validating artifacts such as policies
- An xRegistry runtime built on the EDC modularity framework

### Reconciler Implementation

The Reconciler will be implemented as a sey of option EDC extensions that are deployed as a runtime to a management
domain. In a Kubernetes-based deployment, the Reconciler runtime can be configured as a `ReplicaSet` containing one pod.
Multiple pods are not needed since the Reconciler does not need to support high availability as it does not serve client
requests. If a Reconciler pod becomes inoperable, Kubernetes will redeploy the reconciler to another pod and operations
will continue as normal.

The Reconciler will periodically scan a set of configured xRegistry targets, calculate changes since its last scan, and
update the management domain. The Reconciler will record the update for its next run. Note that not all resources may be
updated transactionally. In this case, the reconciler will need to implement compensation logic to rollback committed
changes.

Returning to our example, both Acme Corporation and the hosting company will need to do two things:

- Configure connectors to use Acme's xRegistry server. The hosting company can do this by providing the option to enter
  a URL and API Key (or OAuth 2 credentials) for the xRegsitry.
- Deploy reconcilers that watch the xRegistry for changes. The hosting company may use the open-source reconcile or
  develop their own implementation.

## Problem 2: Observability and Activity Events

The problem of fleet transparency is defined as the following:

> How are data-sharing activity events observed across a fleet of Connectors?

There are two of key points packed into how this problem is framed.

### Activity Events vs. Telemetry

The first point is that events are defined as "data-sharing activity" and are distinct from telemetry. This is inline
with a core EDC design tenant that distinguishes between monitoring (information logging), software operations metrics (
telemetry), and events related to activities performed by the Connector. These three realms should not be mixed.

To understand why these distinctions were made, consider telemetry in two scenarios. If an organization has contracted
with a provider to run a Connector on its behalf (Connector as a Service), it should not be directly concerned with
specific telemetry. The organization may be interested in the overall quality of service of its Connector operations,
but specific data about particular runtime operations will not be relevant. This is particularly true in setups where
Connectors are virtualized by a service provider and run in a shared runtime instance. In fact, telemetry data will be
of little use to an organization not familiar with the implementation of the connector service.

Another example is where two Connectors implementations or different. Note this may be the case even when both
Connectors are implemented using EDC, as the latter supports a wide variety of deployment architectures. In these
situations, there will be limited similarity between the telemetry emitted by the two runtimes.

In contrast, data-sharing activity can be abstracted across divergent Connector implementations since these activities
are defined by DSP state machines. For example, a contract agreement being finalized or a data transfer being started or
paused.

### Activity Events as a Basis for Automation

The second point is that we are concerned with the observability of events, not querying data-sharing metrics. Fleet
coordination will provide facilities for receiving events, but it will not provide a way to querying metrics such
as the number of contract agreements signed over a period of time. The focus is on providing a basis for automation.
Events can be observed and compiled into metrics that are exposed as query endpoints by separate applications.
Event automation can be used to create novel metrics and solve problems posed by a diverse range of use cases as opposed
to a fixed set of indicators.

### CloudEvents: A Protocol Agnostic Message Standard

Activity events are designed to be protocol agnostic. In other words, it must be possible to transport these events over
multiple messaging providers such as an MQTT broker, Kafka, or a cloud messaging provider. Fleet coordination will adopt
the CNCF [CloudEvents specification](https://www.cncf.io/projects/cloudevents/) as it defines a protocol-independent
message format for events. An example cloud event is as follows:

```json
{
  "specversion": "1.0",
  "type": "EdcSampleEvent",
  "source": "….",
  "subject": "123",
  "id": "4ec892f6-9b58-4ead-862e-41c1ec5eaf11",
  "time": "2025-02-05T17:31:00Z",
  "datacontenttype": "applicationjson",
  "data": " {…}"
}
```

Cloud events will enable Connector deployments to send events and route events over a diverse range of messaging
systems. These events can then be received and acted upon by applications and other systems.

### Activity Events Implementation

An EDC implementation will be developed that builds on the existing low-level `EventRouter` to transform data passed
through it into Cloud Events. A set of events and schemas will also be defined for those transformations. Output sinks
to specific messaging systems such as Kafka will also be developed. The specific target message systems still needs to
be defined.

Returning to the example, Acme Corporation may choose to use EDC extensions to propagate events to a specific messaging
provider such as Kafka or AWS EventBridge. The hosting provider could offer the option for Acme to chose their message
destination of choice, for example, EventBridge, or another solution. Acme could then listen for events and forward them
to internal applications for processing.

## Fleet Coordination Architecture

Fleet Coordination capabilities will be developed as a set of EDC extensions consisting of the following:

- An xRegistry implementation that can be deployed as a server
- xRegistry tooling for integrating artifact publishing in automated CI pipelines
- A Reconciler runtime that synchronizes changes to a management domain
- A CloudEvent Router and transport bindings for publishing activity events

The above components can be incorporated into downstream projects as part of an EDC distribution. These components are
represented in the following architecture diagram:

![](/docs/edc.connector.png)



