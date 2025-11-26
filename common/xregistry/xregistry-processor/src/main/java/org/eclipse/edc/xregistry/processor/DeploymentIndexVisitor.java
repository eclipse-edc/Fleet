package org.eclipse.edc.xregistry.processor;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import static java.util.Collections.emptyList;

/**
 * Provides a {@link DeploymentIndex} by visiting the xRegistry.
 */
public class DeploymentIndexVisitor implements XRegistryVisitor {
    private Map<Artifact, Supplier<InputStream>> artifacts = new HashMap<>();
    private Map<Artifact, Supplier<InputStream>> policies = new HashMap<>();
    private Map<Artifact, Supplier<InputStream>> schemas = new HashMap<>();
    private Map<Artifact, Supplier<InputStream>> rules = new HashMap<>();
    private List<String> problems = new ArrayList<>();

    @Override
    public void onPolicy(Artifact artifact, Supplier<InputStream> ref) {
        artifacts.put(artifact, ref);
        policies.put(artifact, ref);
    }

    @Override
    public void onSchema(Artifact artifact, Supplier<InputStream> ref) {
        artifacts.put(artifact, ref);
        schemas.put(artifact, ref);
    }

    @Override
    public void onRule(Artifact artifact, Supplier<InputStream> ref) {
        artifacts.put(artifact, ref);
        rules.put(artifact, ref);
    }

    @Override
    public void onError(String problem) {
        problems.add(problem);
    }

    public ValidationResult validate() {
        if (!problems.isEmpty()) {
            return new ValidationResult(false, problems);
        }
        // TODO: scan that all references are complete
        return new ValidationResult(true, emptyList());
    }

    public DeploymentIndex getIndex() {
        return new DeploymentIndex(artifacts, policies, schemas, rules);
    }
}
