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
    antlr("org.antlr:antlr4:4.13.1")
    implementation("org.antlr:antlr4-runtime:4.13.1")

    testImplementation(platform("org.junit:junit-bom:5.11.0"))
    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testImplementation("org.junit.jupiter:junit-jupiter-params")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}

// Configuration ANTLR
tasks.named<AntlrTask>("generateGrammarSource") {
    arguments = arguments + listOf("-visitor", "-listener")
    source = fileTree("src/main/antlr") { include("*.g4") }
    outputDirectory = file("${buildDir}/generated-src/antlr/main/simulation/antlr4") // <- pas de simulation/antlr4 ici
}


sourceSets {
    named("main") {
        antlr {
            srcDir("src/main/antlr")
        }
        java {
            srcDir("${buildDir}/generated-src/antlr/main")
        }
    }
}

tasks.test {
    useJUnitPlatform()
}

