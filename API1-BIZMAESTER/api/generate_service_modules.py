import os
import textwrap

root = r'd:\MON BUREAU\PPE302\PPE-302\ppe-302-2\TEST\API1-BIZMAESTER\api'
modules = [
    ('service-commerce', 18101, 'commerce_db'),
    ('service-construction', 18102, 'construction_db'),
    ('service-ferme', 18103, 'ferme_db'),
    ('service-education', 18104, 'education_db'),
    ('service-hotellerie', 18105, 'hotellerie_db'),
    ('service-logistique', 18106, 'logistique_db'),
    ('service-restauration', 18107, 'restauration_db'),
    ('service-sante', 18108, 'sante_db'),
    ('service-transport', 18109, 'transport_db'),
]

for module, port, db in modules:
    base = os.path.join(root, module)
    package_name = module.replace('service-', '')
    package_folder = os.path.join(base, 'src', 'main', 'java', 'com', 'bizmaster', 'service', package_name)
    os.makedirs(package_folder, exist_ok=True)
    os.makedirs(os.path.join(base, 'src', 'main', 'resources'), exist_ok=True)

    pom = textwrap.dedent(f'''
        <?xml version="1.0" encoding="UTF-8"?>
        <project xmlns="http://maven.apache.org/POM/4.0.0"
                 xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                 xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
            <modelVersion>4.0.0</modelVersion>
            <parent>
                <groupId>com.bizmaster</groupId>
                <artifactId>api</artifactId>
                <version>0.0.1-SNAPSHOT</version>
            </parent>
            <artifactId>{module}</artifactId>
            <dependencies>
                <dependency>
                    <groupId>com.bizmaster</groupId>
                    <artifactId>service-template</artifactId>
                </dependency>
            </dependencies>
            <build>
                <plugins>
                    <plugin>
                        <groupId>org.springframework.boot</groupId>
                        <artifactId>spring-boot-maven-plugin</artifactId>
                    </plugin>
                </plugins>
            </build>
        </project>
    ''').strip() + '\n'
    with open(os.path.join(base, 'pom.xml'), 'w', encoding='utf-8') as f:
        f.write(pom)

    app_name = ''.join(part.capitalize() for part in package_name.split('-')) + 'ServiceApplication'
    java = textwrap.dedent(f'''
        package com.bizmaster.service.{package_name};

        import org.springframework.boot.SpringApplication;
        import org.springframework.boot.autoconfigure.SpringBootApplication;
        import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
        import org.springframework.context.annotation.ComponentScan;

        @SpringBootApplication
        @EnableEurekaClient
        @ComponentScan(basePackages = {{"com.bizmaster.service.template", "com.bizmaster.service.{package_name}"}})
        public class {app_name} {{
            public static void main(String[] args) {{
                SpringApplication.run({app_name}.class, args);
            }}
        }}
    ''').strip() + '\n'
    with open(os.path.join(package_folder, f'{app_name}.java'), 'w', encoding='utf-8') as f:
        f.write(java)

    yml = textwrap.dedent(f'''
        server:
          port: {port}

        spring:
          application:
            name: {module}
          datasource:
            url: jdbc:postgresql://localhost:5432/{db}
            username: postgres
            password: 1234
            driver-class-name: org.postgresql.Driver
          jpa:
            hibernate:
              ddl-auto: update
            properties:
              hibernate:
                format_sql: true
                jdbc:
                  lob:
                    non_contextual_creation: true

        service:
          base-path: /api/{package_name}
          domain: {package_name}

        jwt:
          secret: changeme1234567890changeme1234567890
          expiration: 3600000

        eureka:
          client:
            service-url:
              defaultZone: http://localhost:8761/eureka
            fetch-registry: true
            register-with-eureka: true

        springdoc:
          api-docs:
            path: /api-docs
          swagger-ui:
            path: /swagger-ui.html
    ''').strip() + '\n'
    with open(os.path.join(base, 'src', 'main', 'resources', 'application.yml'), 'w', encoding='utf-8') as f:
        f.write(yml)

print('created modules', [m for m,_,_ in modules])
