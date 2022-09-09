# CI/CD training

## A projekt clone-ozás magatokhoz

```
git clone https://github.com/Training360/cicd-training-2022-09-08
```

Az `employees` könyvtárban ki kell adni a következő parancsokat:

```
git init
git add .
git commit -m "A szállító által adott első verzió"
```

## Build

```
set JAVA_HOME=C:\Program Files\Java\jdk-17.0.4.1
mvnw package
```

## Alkalmazás indítása

```
java -jar target\employees-1.0-SNAPSHOT.jar
```

Az alkalmazás elérhető a `http://localhost:8080/` címen.

## Függőségek

```
versions:display-dependency-updates 
```

## Docker konfiguráció

```
net localgroup docker-users %USERDOMAIN%\%USERNAME% /add
```

Kipróbálása:

```
docker run hello-world
```

## Nexus

```
docker run --name nexus --detach --publish 8091:8081 --publish 8092:8082 sonatype/nexus3
docker logs -f nexus
```

Jelszó megszerzése:

```
docker exec -it nexus cat /nexus-data/admin.password
```

A `%HOME%` könyvtár `.m2/settings.xml` fájlt kell létrehozni a következő
tartalommal:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0"
     xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
     xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0 http://maven.apache.org/xsd/settings-1.0.0.xsd">
   <mirrors>
    <mirror>
      <id>central</id>
      <name>central</name>
      <url>http://localhost:8091/repository/maven-public/</url>
      <mirrorOf>*</mirrorOf>
    </mirror>
  </mirrors>
</settings>
```

Kipróbálható `org\springframework` könyvtár letörlésével.

# Nexus deploy

`pom.xml`-t kell kiegészíteni:


```xml
<distributionManagement>
    <snapshotRepository>
        <id>nexus-snapshots</id>
        <url>http://localhost:8091/repository/maven-snapshots/</url>
    </snapshotRepository>
</distributionManagement>
```

A `settings.xml` legyen ez:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0"
     xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
     xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0 http://maven.apache.org/xsd/settings-1.0.0.xsd">
	 
	 <servers>
     <server>
       <id>nexus-snapshots</id>
       <username>admin</username>
       <password>admin</password>
     </server>
   </servers>
	 
   <mirrors>
    <mirror>
      <id>central</id>
      <name>central</name>
      <url>http://localhost:8091/repository/maven-public/</url>
      <mirrorOf>*</mirrorOf>
    </mirror>
  </mirrors>
</settings>
```

Deploy a Nexusba: `mvnw deploy`

## Teszt lefedettség

A `pom.xml`-be:

```xml
<plugin>
  <groupId>org.jacoco</groupId>
  <artifactId>jacoco-maven-plugin</artifactId>
  <version>0.8.7</version>
  <executions>
    <execution>
      <id>jacoco-initialize</id>
      <goals>
        <goal>prepare-agent</goal>
      </goals>
    </execution>
    <execution>
      <id>jacoco-site</id>
      <phase>package</phase>
      <goals>
        <goal>report</goal>
      </goals>
    </execution>
  </executions>
</plugin>
```

```
mvnw package
```

Elérhető lesz a `target/site/jacoco/index.html` fájlban.

## Integrációs tesztelés

Fájlok átmásolása:

```
\src\test\java\employees\EmployeesControllerRestAssuredIT.java
\src\test\resources\application.properties
\src\test\resources\employee-dto.json
```

A `pom.xml` kiegészítése:

```xml
<plugin>
			<groupId>org.apache.maven.plugins</groupId>
			<artifactId>maven-failsafe-plugin</artifactId>
			<version>2.22.2</version>
			<executions>
				<execution>
					<goals>
						<goal>integration-test</goal>
					</goals>
				</execution>
			</executions>
		</plugin>
```

Parancs:

```
mvnw verify
```

## Integrációs tesztelés valós adatbázissal

```
docker run -d -e MYSQL_DATABASE=employees -e MYSQL_USER=employees -e MYSQL_PASSWORD=employees -e MYSQL_ALLOW_EMPTY_PASSWORD=yes   -p 3306:3306 --name it-mariadb mariadb
```

A `\src\test\resources\application.properties` állományban át kell írni a következőket:

```
spring.datasource.url=jdbc:mariadb://localhost/employees
spring.datasource.username=employees
spring.datasource.password=employees
```

```
docker exec -it it-mariadb mysql employees -e "select * from employees"  
```

## Docker image készítése

```
docker build -t employees .
docker run -p 8080:8080 -d --name my-employees employees
docker logs -f my-employees
```

# Build is konténerben

```
docker build -t employees -f Dockerfile.build .
docker stop my-employees
docker rm my-employees
docker run -p 8080:8080 -d --name my-employees employees
```

# E2E tesztek futtatása

```
docker compose up --abort-on-container-exit
```

# SonarQube

```
docker run --name sonarqube -d -p 9000:9000 sonarqube:lts
`

My account/Security/Token

```
mvnw -Dsonar.login=[ide jön a token szögletesek nélkül] sonar:sonar
```

# Docker push Nexusba

```
docker login localhost:8092
docker tag employees localhost:8092/employees
docker push localhost:8092/employees
```

# GitLab indítása

```
cd gitlab
docker compose up -d
docker exec -it gitlab-gitlab-1 grep "Password" /etc/gitlab/initial_root_password
```

# Push a GitLabra

```
git remote add origin http://localhost/gitlab-instance-4e231e23/employees.git
git config --global user.name "FIRST_NAME LAST_NAME"
git config --global user.email "MY_NAME@example.com"
git push origin master
```

# Runner regisztrálása

```
docker exec -it gitlab-gitlab-runner-1 gitlab-runner register --non-interactive --url http://gitlab-gitlab-1 --registration-token 1QzzH9QrgGFgyARnvvVE --executor docker --docker-image docker:latest --docker-network-mode gitlab_default --clone-url http://gitlab-gitlab-1 --docker-volumes /var/run/docker.sock:/var/run/docker.sock
```

# Fájl futtathatóvá tétele

```
git update-index --chmod=+x mvnw
```