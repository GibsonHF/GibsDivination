plugins {
    id("java")
    `maven-publish`
}

group = "net.botwithus.debug"
version = "1.0-SNAPSHOT"

repositories {
    mavenLocal()
    mavenCentral()
<<<<<<< HEAD
    maven { setUrl("https://jitpack.io") }
=======
    maven {
        setUrl("https://nexus.botwithus.net/repository/maven-snapshots/")
    }
>>>>>>> 9cbe48e566ba875ade55d9af66e5d5cc97a22a8c
}

tasks.withType<JavaCompile> {
    sourceCompatibility = "20"
    targetCompatibility = "20"
    options.compilerArgs.add("--enable-preview")
}

val copyJar by tasks.register<Copy>("copyJar") {
    from("build/libs/")
    into("${System.getProperty("user.home")}\\BotWithUs\\scripts\\local\\")
    include("*.jar")
}

configurations {
    create("includeInJar") {
        this.isTransitive = false
    }
}

tasks.named<Jar>("jar") {
    from({
        configurations["includeInJar"].map { zipTree(it) }
    })
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    finalizedBy(copyJar)
}

dependencies {
<<<<<<< HEAD
    implementation ("com.github.BotWithUs:BotWithUsAPI:master-SNAPSHOT")
    implementation ("com.github.BotWithUs:BwuExtendedPublicAPI:master-SNAPSHOT")
=======
    implementation ("net.botwithus.rs3:botwithus-api:1.0.0-20231112.012705-1")
    implementation("net.botwithus.xapi.public:botwithusx-api:1.0.0-20231113.032104-1")
    "includeInJar"("net.botwithus.xapi.public:botwithusx-api:1.0.0-20231113.032104-1")
>>>>>>> 9cbe48e566ba875ade55d9af66e5d5cc97a22a8c
    implementation("com.google.code.gson:gson:2.10.1")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.2")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}
