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