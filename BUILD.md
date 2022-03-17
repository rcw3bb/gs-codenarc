# Build

## Pre-requisite

* Java 11

* Create or update **<USER_HOME>\\.gradle\\gradle.properties** to have the following properties:

    ```properties
    artifactoryUsername=<VALID_ARTIFACTORY_USERNAME>
    artifactoryPassword=<VALID_ARTIFACTORY_PASSWORD>
    archivaUsername=<VALID_ARCHIVA_USERNAME>
    archivaPassword=<VALID_ARCHIVA_PASSWORD>
    ```
    
    > If you don't have access to my **artifactory** and **archiva** repositories, update all the **repositories section** in the **build.gradle** file **after cloning**, from:
    >
    > ```groovy
    > repositories {
    >     maven {
    >         url 'https://repo.ronella.xyz/artifactory/java-central'
    >         credentials {
    >             username "${artifactoryUsername}"
    >             password "${artifactoryPassword}"
    >         }
    >         mavenContent {
    >             releasesOnly()
    >         }
    >     }
    >     maven {
    >         url 'https://repo.ronella.xyz/archiva/repository/snapshots/'
    >         credentials {
    >             username "${archivaUsername}"
    >             password "${archivaPassword}"
    >         }
    >         mavenContent {
    >             snapshotsOnly()
    >         }
    >     }
    > }
    > ```
    >
    > to
    >
    > ```groovy
    > repositories {
    > 	mavenCentral()
    > }
    > ```

## Running Unit Test

Run the following command to where you've cloned this repository:

```
gradlew clean check
```

## Package

Run the following command to where you've cloned this repository:

```
gradlew jar
```

> The **generated jar** file will be in the **libs directory** inside the **build directory** from where you've cloned the this repository.

