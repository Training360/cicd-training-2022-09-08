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