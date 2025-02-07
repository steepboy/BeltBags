pluginManagement {
  val kotlinVersion = providers.gradleProperty("kotlinVersion")

  plugins {
    id("org.jetbrains.kotlin.jvm") version kotlinVersion
  }
}

plugins {
  id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}

rootProject.name = "BeltBags"
