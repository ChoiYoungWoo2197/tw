import com.moowork.gradle.node.npm.NpmTask

plugins {
    java
    id("org.springframework.boot") version "2.2.5.RELEASE"
    id("com.moowork.node") version "1.3.1"
}

version = "1.0-SNAPSHOT"

val dependencyVersions = project.ext.get("dependencyVersions") as Map<String, String>

dependencies {
    implementation(project(":seal-core"))
    implementation(project(":seal-domain-member"))
    implementation(project(":seal-domain-environmentvariable"))
    implementation(project(":seal-domain-operatingcycle"))
    implementation(project(":seal-domain-calendar"))
    implementation(project(":seal-domain-article"))

    implementation("org.springframework.boot", "spring-boot-starter-web", dependencyVersions["spring-boot"])
    implementation("org.springframework.boot", "spring-boot-starter-security", dependencyVersions["spring-boot"])
    implementation("org.springframework.boot", "spring-boot-starter-thymeleaf", dependencyVersions["spring-boot"])
    implementation("org.springframework.boot", "spring-boot-starter-data-jpa", dependencyVersions["spring-boot"])
    implementation("org.springframework.boot", "spring-boot-starter-validation", dependencyVersions["spring-boot"])
    implementation("org.springframework.boot", "spring-boot-devtools", dependencyVersions["spring-boot"])

    implementation("org.thymeleaf.extras", "thymeleaf-extras-springsecurity5", dependencyVersions["thymeleaf-extras-springsecurity5"])
}

sourceSets {
    main {
        resources {
            exclude("ui-src")
        }
    }
}

springBoot {
    mainClassName = "kr.co.cmtinfo.seal.WebSiteApplication"
}

node {
    version = "12.16.3"
    npmVersion = "6.14.4"
    download = true
}

val jsBuildDir = project.projectDir.absolutePath + "/src/main/resources/static"

tasks {

    register<NpmTask>("npmInstallAll") {
        inputs.file("package.json")
        setArgs(listOf("install"))
    }

    register<NpmTask>("npmBuild") {
        dependsOn("npmInstallAll")
        inputs.file("package.json")
        setArgs(listOf("run", "build"))
    }

    jar {
//        from(jsBuildDir) {
//            eachFile {
//                path = if( path.startsWith("META-INF") ) path else "static/" + path
//            }
//        }
//        includeEmptyDirs = false
//        dependsOn("npmBuild")
    }

    processResources {
        dependsOn("npmBuild")
    }
}