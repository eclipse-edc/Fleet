# EDC Fleet

This repository contains components implementing an EDC Registry:

- A registry server and tooling based on [xRegsitry](https://xregistry.io/).
- A reconciler for synchronizing EDC management domains with resource states specified in a hierarchy of registry servers. 

> :warning: This codebase is under active development and is not yet ready for production use.
 
Build and run the xRegistry server:

```
./gradlew clean shadowJar
java -Dedc.participant.id="test" -jar registry/launcher/build/libs/registry-server.jar
```
