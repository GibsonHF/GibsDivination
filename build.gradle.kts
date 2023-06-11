import org.gradle.internal.impldep.org.eclipse.jgit.lib.ObjectChecker.type

plugins {
    id("java")
}

group = "net.botwithus.debug"
version = "1.0-SNAPSHOT"

repositories {
    mavenLocal()
    mavenCentral()
}

tasks.withType<JavaCompile> {
    options.compilerArgs.add("--enable-preview")
}

dependencies {
    implementation("net.botwithus.rs3:api:1.0-SNAPSHOT")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.2")
}

val copyJar by tasks.register<Copy>("copyJar") {
    from("build/libs/")
    into("${System.getProperty("user.home")}\\Abyss\\plugins\\local\\")
    include("*.jar")
}

tasks.named<Jar>("jar") {
    finalizedBy(copyJar)
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}
