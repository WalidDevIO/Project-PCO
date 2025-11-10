plugins {
    java
    antlr
}

group = "com.ubo.paco"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

dependencies {
    // ANTLR
    antlr("org.antlr:antlr4:4.13.1")
    implementation("org.antlr:antlr4-runtime:4.13.1")

    // JUnit Jupiter (tests)
    testImplementation(platform("org.junit:junit-bom:5.11.0"))
    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testImplementation("org.junit.jupiter:junit-jupiter-params")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}

// --- Configuration ANTLR ---
tasks.generateGrammarSource {
    arguments = arguments + listOf("-visitor")
    outputDirectory = layout.buildDirectory.dir("generated-src/antlr/main").get().asFile
}

sourceSets {
    named("main") {
        antlr {
            srcDir("src/main/antlr4")
        }
        java {
            srcDir(layout.buildDirectory.dir("generated-src/antlr/antlr4"))
        }
    }
}

tasks.test {
    useJUnitPlatform()
}

tasks.register<JavaExec>("runMain") {
    group = "application"
    description = "Exécute la classe Main"
    classpath = sourceSets["main"].runtimeClasspath
    mainClass.set("com.ubo.paco.Main")
}
