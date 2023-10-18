plugins {
    id("BotWithUs") version "1.0-SNAPSHOT"
}

group = "net.botwithus.debug"
version = "1.0-SNAPSHOT"

repositories {
    mavenLocal()
    mavenCentral()
}

script {
    this.useMavenLocal.set(true)
    this.apiVersion.set("1.0.0-SNAPSHOT")
    this.scriptClass.set("net.botwithus.debug.DebugScript")
    this.scriptVersion.set("1.0")
    this.scriptName.set("Debug Script")
    this.scriptDescription.set("An Example Script")
    this.author.set("BotWithUs")
}

tasks.withType<JavaCompile> {
    sourceCompatibility = "20"
    targetCompatibility = "20"
    options.compilerArgs.add("--enable-preview")
}

dependencies {
    implementation("com.google.code.gson:gson:2.10.1")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.2")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}