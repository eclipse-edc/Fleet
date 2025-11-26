plugins {
    base
    `java-gradle-plugin`
}

repositories {
    gradlePluginPortal()
}

// Configure the functional test source set
val functionalTestSourceSet = sourceSets.create("functionalTest")
configurations.getByName("functionalTestImplementation").extendsFrom(configurations.implementation.get())
configurations.getByName("functionalTestRuntimeOnly").extendsFrom(configurations.runtimeOnly.get())


val functionalTestImplementation: Configuration by configurations.getting {
    extendsFrom(configurations.implementation.get())
}

dependencies {
    implementation(gradleApi())
    implementation(libs.oras.java.sdk)

    testImplementation(platform(libs.testcontainers.bom))
    testImplementation(libs.testcontainers)
    testImplementation(libs.testcontainers.junit.jupiter)

    functionalTestImplementation(gradleTestKit())
    functionalTestImplementation(gradleApi())
    functionalTestImplementation(libs.oras.java.sdk)
    functionalTestImplementation(libs.bundles.testing)

    functionalTestImplementation(platform(libs.testcontainers.bom))
    functionalTestImplementation(libs.bundles.testcontainers)
}

gradlePlugin {
    plugins {
        register("xRegistryOCIPublisher") {
            id = "org.eclipse.edc.xregistry-oci-publisher"
            displayName = "xregistry-oci-publisher"
            description = "Publishes xRegistry OCI artifacts"
            implementationClass = "org.eclipse.edc.xregistry.oci.publisher.XRegistryOciPublisherPlugin"
        }
    }

    // Add the functional test source set
    testSourceSets(functionalTestSourceSet)
}

// Configure the functional test task
val functionalTest = tasks.register<Test>("functionalTest") {
    description = "Runs functional tests."
    group = "verification"

    testClassesDirs = functionalTestSourceSet.output.classesDirs
    classpath = functionalTestSourceSet.runtimeClasspath
    useJUnitPlatform()

    // Ensure plugin is built before running functional tests
    dependsOn(tasks.pluginUnderTestMetadata)
}

// Configure test task
tasks.test {
    useJUnitPlatform()
}

// Make check depend on functionalTest
tasks.check {
    dependsOn(functionalTest)
}


project.defaultTasks("buildXRegistryOciPublish")

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
        }
    }
}