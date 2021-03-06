import org.jetbrains.kotlin.gradle.dsl.KotlinJvmOptions

plugins {
    kotlin("jvm") version "1.5.20"
    //id("kr.entree.spigradle") version "2.2.3"
}

group = "org.example"
version = "1.0.0"

repositories {
    mavenCentral()
    maven("https://papermc.io/repo/repository/maven-public/")
    maven("https://jitpack.io")
    //maven("https://repo.dmulloy2.net/repository/public/")
}

dependencies {
    compileOnly(kotlin("stdlib"))
    compileOnly("io.papermc.paper:paper-api:1.17-R0.1-SNAPSHOT")
    //compileOnly("com.github.spigradle.spigradle:kr.entree.spigradle.base.gradle.plugin:v2.2.3")
    compileOnly("io.github.monun:kommand:1.2.1")
    compileOnly("io.github.monun:tap:4.0.0-RC")
    //compileOnly(paper("1.16.5"))
    //compileOnly("com.comphenix.protocol:ProtocolLib:4.6.0")
}

/*
spigot {
    authors = listOf("NamuTree0345")
    apiVersion = project.property("apiVersion").toString()
    //depends = listOf("ProtocolLib")
    commands {
        //create("hello")
    }
}
 */

val shade = configurations.create("shade")
shade.extendsFrom(configurations.implementation.get())

tasks {

    javadoc {
        options.encoding = "UTF-8"
    }

    compileJava {
        options.encoding = "UTF-8"
    }

    compileKotlin {
        (kotlinOptions as KotlinJvmOptions).apply {
            jvmTarget = JavaVersion.VERSION_1_8.toString()
        }
    }
    compileTestKotlin {
        (kotlinOptions as KotlinJvmOptions).apply {
            jvmTarget = JavaVersion.VERSION_1_8.toString()
        }
    }

    create<Jar>("sourceJar") {
        archiveClassifier.set("source")
        from(sourceSets["main"].allSource)
    }

    jar {
        from (shade.map { if (it.isDirectory) it else zipTree(it) })
    }

    // From monun/tap-sample-plugin
    create<Copy>("copyToServer") {
        from(jar)
        val plugins = File(rootDir, ".server/plugins")
        if (File(shade.artifacts.files.asPath).exists()) {
            into(File(plugins, "update"))
        } else {
            into(plugins)
        }
    }
}
