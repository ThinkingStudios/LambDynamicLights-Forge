import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
	`java-gradle-plugin`
	`kotlin-dsl`
}

val javaVersion = 21

repositories {
	gradlePluginPortal()
	maven { url = uri("https://maven.fabricmc.net/") }
	maven { url = uri("https://maven.architectury.dev/") }
	maven { url = uri("https://maven.neoforged.net/releases/") }
	maven { url = uri("https://maven.firstdark.dev/releases") }
}

dependencies {
	implementation(libs.gradle.licenser)
	implementation(libs.gradle.loom)
	implementation(libs.mappingio)

	// A bit of a hack you definitely should not worry about.
	// https://github.com/gradle/gradle/issues/15383#issuecomment-779893192
	implementation(files(libs.javaClass.superclass.protectionDomain.codeSource.location))
}

java {
	sourceCompatibility = JavaVersion.toVersion(javaVersion)
	targetCompatibility = JavaVersion.toVersion(javaVersion)
}

kotlin {
	compilerOptions {
		jvmTarget = JvmTarget.fromTarget(javaVersion.toString())
	}
}
