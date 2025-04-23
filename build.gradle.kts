plugins {
    kotlin("jvm") version "2.1.10"
}

group = "org.beijing"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("io.insert-koin:koin-core:4.0.3")
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.6.2")

    testImplementation("com.google.truth:truth:1.4.4")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.12.0")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.12.0")
    testImplementation("io.mockk:mockk:1.14.0")


}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}