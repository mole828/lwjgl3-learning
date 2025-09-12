plugins {
    // Apply the shared build logic from a convention plugin.
    // The shared code is located in `buildSrc/src/main/kotlin/kotlin-jvm.gradle.kts`.
    id("buildsrc.convention.kotlin-jvm")
    id("org.lwjgl.plugin") version "0.0.35"
    // Apply the Application plugin to add support for building an executable JVM application.
    application
}

val lwjglVersion = "3.3.6"

val osName: String = System.getProperty("os.name")
val osArch: String = System.getProperty("os.arch")
val lwjglNatives = when {
    osName.startsWith("Windows") -> "natives-windows"
    osName.startsWith("Linux") -> "natives-linux"
    osName.startsWith("Mac") -> if (osArch == "aarch64") "natives-macos-arm64" else "natives-macos"

    else -> throw GradleException("Unsupported operating system: $osName")
}
println("lwjglNatives: $lwjglNatives")

dependencies {
    implementation(kotlin("stdlib"))
    // Project "app" depends on project "utils". (Project paths are separated with ":", so ":utils" refers to the top-level "utils" project.)
    implementation(project(":utils"))


    implementation(platform("org.lwjgl:lwjgl-bom:$lwjglVersion"))

    implementation("org.lwjgl", "lwjgl")
    runtimeOnly("org.lwjgl", "lwjgl", classifier = lwjglNatives)
    implementation("org.lwjgl", "lwjgl-glfw")
    runtimeOnly("org.lwjgl", "lwjgl-glfw", classifier = lwjglNatives)
    implementation("org.lwjgl", "lwjgl-opengl")
    runtimeOnly("org.lwjgl", "lwjgl-opengl", classifier = lwjglNatives)
    implementation("org.lwjgl", "lwjgl-stb")
    runtimeOnly("org.lwjgl", "lwjgl-stb", classifier = lwjglNatives)
}

application {
    // Define the Fully Qualified Name for the application main class
    // (Note that Kotlin compiles `App.kt` to a class with FQN `com.example.app.AppKt`.)
    mainClass = "learning.HelloWorldKt"
}