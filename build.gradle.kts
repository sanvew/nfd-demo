val kotlinVersion = "1.9.22"
val ktorVersion = "2.3.7"
val guiceVersion = "7.0.0"
val logbackVersion = "1.4.14"

val jupiterVersion = "5.10.2"
val mockitoVersion = "5.9.0"
val mockitoKotlinVersion = "5.2.1"

val jvmToolChainVersion = 11

plugins {
    kotlin("jvm") version "1.9.22"
    id("io.ktor.plugin") version "2.3.7"
}

group = "xyz.sanvew"
version = "0.0.1"

application {
    mainClass.set("xyz.sanvew.MainKt")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

kotlin {
    jvmToolchain(jvmToolChainVersion)
}

repositories {
    mavenCentral()
}


dependencies {
//    Ktor server dependencies
    implementation("io.ktor:ktor-server-core-jvm:$ktorVersion")
    implementation("io.ktor:ktor-server-netty-jvm:$ktorVersion")
    implementation("io.ktor:ktor-server-content-negotiation:$ktorVersion")
    implementation("io.ktor:ktor-serialization-jackson:$ktorVersion")
    implementation("io.ktor:ktor-server-status-pages:$ktorVersion")

//    Ktor client dependencies
    implementation("io.ktor:ktor-client-core:$ktorVersion")
    implementation("io.ktor:ktor-client-cio:$ktorVersion")
    implementation("io.ktor:ktor-client-logging:$ktorVersion")

//    Other dependencies
    implementation("com.google.inject:guice:$guiceVersion")
    implementation("ch.qos.logback:logback-classic:$logbackVersion")

//    Testing dependencies
    testImplementation("io.ktor:ktor-server-tests:$ktorVersion")
    testImplementation("io.ktor:ktor-client-mock:$ktorVersion")
    testImplementation("org.junit.jupiter:junit-jupiter-api:$jupiterVersion")
    testImplementation("org.mockito:mockito-core:$mockitoVersion")
    testImplementation("org.mockito.kotlin:mockito-kotlin:$mockitoKotlinVersion")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$jupiterVersion")
}

tasks.withType<Test> {
    useJUnitPlatform {}
}
