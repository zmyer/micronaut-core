plugins {
    id 'groovy'
    id "application"
}

repositories {
    mavenLocal()
    mavenCentral()
}

ext {
    micronautVersion = "@version@"
}

dependencies {
    compile "org.particleframework:inject-groovy:\${micronautVersion}"
    compile "org.particleframework:runtime-groovy:\${micronautVersion}"

    // use one of these servers, not both...
    compile "org.particleframework:http-server-netty:\${micronautVersion}"
    compile "org.particleframework.configuration:hibernate-gorm:\${micronautVersion}"

    runtime 'org.apache.tomcat:tomcat-jdbc:8.0.44'
    runtime 'com.h2database:h2:1.4.196'

    runtime "ch.qos.logback:logback-classic:1.2.3"
}

group = "${groupId}"
version = "${version}"

mainClassName = "${pkg}.Application"