rootProject.name = "fleet-coordinator"

// this is needed to have access to snapshot builds of plugins
pluginManagement {
    repositories {
        mavenLocal()
        maven {
            url = uri("https://oss.sonatype.org/content/repositories/snapshots/")
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        maven {
            url = uri("https://oss.sonatype.org/content/repositories/snapshots/")
        }
        mavenCentral()
        mavenLocal()
    }
}

include(":common:xregistry:xregistry-model")
include(":common:xregistry:xregistry-lib")
include(":common:xregistry:xregistry-policy")
include(":common:xregistry:xregistry-schema")

include(":reconciler:reconciler-spi")
include(":reconciler:reconciler-core")
include(":reconciler:reconciler-policy")