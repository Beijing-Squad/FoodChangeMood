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


}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}