import com.google.protobuf.gradle.*
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

group = "com.vmiforall"
version = "1.0-SNAPSHOT"

plugins {
    kotlin("jvm") version "1.5.0"
    id("com.google.protobuf") version "0.8.16"
    id("java")
}

buildscript {
    repositories {
        google()
        jcenter()
        mavenCentral()
    }
    dependencies {
        classpath("com.google.protobuf:protobuf-gradle-plugin:0.8.16")
    }
}

allprojects {
    repositories {
        google()
        jcenter()
        mavenCentral()
    }
}

sourceSets {
    main {
        java {
            srcDir("build/generated/source/proto/main/java")
            srcDir("build/generated/source/proto/main/grpckt")
            srcDir("build/extracted-include-protos/main")
        }
        proto {
            // In addition to the default 'src/main/proto'
            srcDir("src/proto")
        }
    }
}

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:3.12.0"
    }
    plugins {
        id("grpc") {
            artifact = "io.grpc:protoc-gen-grpc-java:1.36.0"
        }
        id("grpckt") {
            artifact = "io.grpc:protoc-gen-grpc-kotlin:1.0.0:jdk7@jar"
        }
    }
    generateProtoTasks {
        ofSourceSet("main").forEach {
            it.plugins {
                id("grpc")
                id("grpckt")
            }
        }
    }
}

dependencies {
    val kotlinVersion by extra { "1.5.0-RC" }
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$kotlinVersion")

    val grpcVersion by extra { "1.36.1" }
    implementation("io.grpc:grpc-netty-shaded:$grpcVersion")
    implementation("io.grpc:grpc-protobuf:$grpcVersion")
    implementation("io.grpc:grpc-kotlin-stub:1.0.0")
    implementation("com.google.guava:guava:30.1.1-jre")
    compileOnly("org.apache.tomcat:annotations-api:6.0.53") // necessary for Java 9+

    val jjwtVersion by extra { "0.11.2" }
    implementation("io.jsonwebtoken:jjwt-api:$jjwtVersion")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:$jjwtVersion")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:$jjwtVersion")

    val bcVersion by extra { "1.68" }
    implementation("org.bouncycastle:bcprov-jdk15on:$bcVersion")

    val koinVersion by extra { "3.0.1" }
    implementation("io.insert-koin:koin-core:$koinVersion")

    val scyllaDriverVersion by extra { "3.10.2-scylla-1" }
    implementation("com.scylladb:scylla-driver-core:$scyllaDriverVersion")
    implementation("com.scylladb:scylla-driver-mapping:$scyllaDriverVersion")
    implementation("com.scylladb:scylla-driver-extras:$scyllaDriverVersion")

    val apacheCommonsValidatorVersion by extra { "1.7" }
    implementation("commons-validator:commons-validator:$apacheCommonsValidatorVersion")

    val config4kVersion by extra { "0.4.2" }
    implementation("io.github.config4k:config4k:$config4kVersion")

    testImplementation(kotlin("test-junit"))
}

tasks.test {
    useJUnit()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}
