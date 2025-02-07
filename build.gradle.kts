import net.minecrell.pluginyml.bukkit.BukkitPluginDescription

plugins {
  id("java")
  id("net.minecrell.plugin-yml.bukkit") version "0.6.0"
  id("com.gradleup.shadow") version "9.0.0-beta4"
  id("io.papermc.paperweight.userdev") version "2.0.0-beta.14"
  kotlin("jvm")
}

allprojects {
  group = "space.subkek"
  version = project.properties["pluginVersion"]!!
}

java {
  java.targetCompatibility = JavaVersion.VERSION_21
  java.sourceCompatibility = JavaVersion.VERSION_21
  java.disableAutoTargetJvm()
}

repositories {
  mavenLocal()
  maven("https://repo.subkek.space/maven-public/")
  maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
  paperweight.devBundle(group = "space.subkek.etheria", version = "1.21.4-R0.1-SNAPSHOT")

  compileOnly(kotlin("stdlib"))

  shadow("space.subkek:sklib:1.0.0")
  shadow("com.h2database:h2:2.3.232")

  compileOnly("dev.jorel:commandapi-bukkit-core:9.7.0")
}

bukkit {
  name = rootProject.name
  version = rootProject.version as String
  main = "space.subkek.beltbags.BeltBags"

  authors = listOf("subkek")

  website = "https://github.com/sub-kek/"

  load = BukkitPluginDescription.PluginLoadOrder.STARTUP

  apiVersion = "1.21"

  foliaSupported = true

  depend = listOf(
    "MCKotlin",
    "CommandAPI"
  )

  permissions {
    register("beltbags.admin") {
      default = BukkitPluginDescription.Permission.Default.OP
    }
  }
}

tasks.build {
  dependsOn(tasks.shadowJar)
}

tasks.shadowJar {
  archiveFileName.set("${rootProject.name}-$version.jar")

  configurations = listOf(project.configurations.shadow.get())
  mergeServiceFiles()

  fun relocate(pkg: String) = relocate(pkg, "space.subkek.beltbags.libs.$pkg")

  relocate("space.subkek.sklib")
  relocate("org.h2")
}
