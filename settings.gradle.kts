rootProject.name = "ryoamiclights"

pluginManagement {
	repositories {
		maven { url = uri("https://maven.fabricmc.net/") }
		maven { url = uri("https://maven.architectury.dev/") }
		maven { url = uri("https://maven.neoforged.net/releases/") }
		maven { url = uri("https://maven.firstdark.dev/releases") }
		gradlePluginPortal()
	}
}

includeBuild("build_logic")
include("api")
