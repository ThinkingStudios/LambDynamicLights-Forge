import com.modrinth.minotaur.dependencies.ModDependency
import lambdynamiclights.Constants
import lambdynamiclights.Utils
import net.darkhax.curseforgegradle.TaskPublishCurseForge

plugins {
	id("lambdynamiclights")
	`maven-publish`
	id("com.github.johnrengelman.shadow").version("8.1.1")
	id("com.modrinth.minotaur").version("2.+")
	id("net.darkhax.curseforgegradle").version("1.1.+")
}

base.archivesName.set(Constants.NAME)

if (!(System.getenv("CURSEFORGE_TOKEN") != null || System.getenv("MODRINTH_TOKEN") != null || System.getenv("LDL_MAVEN") != null)) {
	version = (version as String) + "-local"
}
logger.lifecycle("Preparing version ${version}...")

repositories {
	mavenLocal()
	mavenCentral()
	maven {
		name = "Terraformers"
		url = uri("https://maven.terraformersmc.com/releases/")
	}
	maven {
		name = "Gegy"
		url = uri("https://maven.gegy.dev")
	}
	maven {
		name = "grondag"
		url = uri("https://maven.dblsaiko.net/")
	}
	maven {
		name = "ParchmentMC"
		url = uri("https://maven.parchmentmc.org")
	}
	exclusiveContent {
		forRepository {
			maven {
				name = "Modrinth"
				url = uri("https://api.modrinth.com/maven")
			}
		}
		filter {
			includeGroup("maven.modrinth")
		}
	}
}

loom {
	accessWidenerPath = file("src/main/resources/lambdynlights.accesswidener")
}

repositories {
	maven { url = uri("https://maven.neoforged.net/releases/") }
}

dependencies {
	implementation(project(":api", configuration = "namedElements"))
	implementation(libs.yumi.commons.core)

	implementation(libs.nightconfig.core)
	implementation(libs.nightconfig.toml)
	modImplementation(libs.obsidianui)
	include(libs.obsidianui)

	shadow(project(":api", configuration = "namedElements"))
	shadow(libs.yumi.commons.core)
	shadow(libs.nightconfig.core)
	shadow(libs.nightconfig.toml)
}

tasks.processResources {
	inputs.property("version", project.version)

	filesMatching("fabric.mod.json") {
		expand("version" to project.version)
	}
}

tasks.shadowJar {
	dependsOn(tasks.jar)
	configurations = listOf(project.configurations["shadow"])
	destinationDirectory.set(file("${project.layout.buildDirectory.get()}/devlibs"))
	archiveClassifier.set("dev")

	relocate("com.electronwill.nightconfig", "dev.lambdaurora.lambdynlights.shadow.nightconfig")
}

tasks.remapJar {
	dependsOn(tasks.shadowJar)
}

modrinth {
	projectId = project.property("modrinth_id") as String
	versionName = "LambDynamicLights ${Constants.VERSION} (${Constants.mcVersion()})"
	uploadFile.set(tasks.remapJar.get())
	loaders.set(listOf("fabric", "quilt"))
	gameVersions.set(listOf(Constants.mcVersion()))
	versionType.set(Constants.getVersionType())
	syncBodyFrom.set(Utils.parseReadme(project))
	dependencies.set(
		listOf(
			ModDependency("P7dR8mSH", "required")
		)
	)

	// Changelog fetching
	val changelogContent = Utils.fetchChangelog(project)

	if (changelogContent != null) {
		changelog.set(changelogContent)
	} else {
		afterEvaluate {
			tasks.modrinth.get().setEnabled(false)
		}
	}
}

tasks.modrinth {
	dependsOn(tasks.modrinthSyncBody)
}

tasks.register("curseforge", TaskPublishCurseForge::class) {
	this.setGroup("publishing")

	val token = System.getenv("CURSEFORGE_TOKEN")
	if (token != null) {
		this.apiToken = token
	} else {
		this.isEnabled = false
		return@register
	}

	// Changelog fetching
	var changelogContent = Utils.fetchChangelog(project)

	if (changelogContent != null) {
		changelogContent = "Changelog:\n\n${changelogContent}"
	} else {
		this.isEnabled = false
		return@register
	}

	val mainFile = upload(project.property("curseforge_id"), tasks.remapJar.get())
	mainFile.releaseType = Constants.getVersionType()
	mainFile.addGameVersion(Constants.mcVersion())
	mainFile.addModLoader("Fabric", "Quilt")
	mainFile.addJavaVersion("Java 21", "Java 22")
	mainFile.addEnvironment("Client")

	mainFile.displayName = "LambDynamicLights ${Constants.VERSION} (${Constants.mcVersion()})"
	mainFile.addRequirement("fabric-api")
	mainFile.addOptional("modmenu")
	mainFile.addIncompatibility("optifabric")

	mainFile.changelogType = "markdown"
	mainFile.changelog = changelogContent
}

// Configure the maven publication.
publishing {
	publications {
		create("mavenJava", MavenPublication::class) {
			from(components["java"])

			pom {
				name.set("LambDynamicLights")
				description.set("Adds dynamic lights to Minecraft.")
			}
		}
	}

	repositories {
		mavenLocal()
		maven {
			name = "BuildDirLocal"
			url = uri("${project.layout.buildDirectory.get()}/repo")
		}

		val ldlMaven = System.getenv("LDL_MAVEN")
		if (ldlMaven != null) {
			maven {
				name = "LambDynamicLightsMaven"
				url = uri(ldlMaven)
				credentials {
					username = (project.findProperty("gpr.user") as? String) ?: System.getenv("MAVEN_USERNAME")
					password = (project.findProperty("gpr.key") as? String) ?: System.getenv("MAVEN_PASSWORD")
				}
			}
		}
	}
}
