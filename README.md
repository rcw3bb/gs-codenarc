# GS CodeNarc Gradle Plugin

The plugin that runs [GS CodeNarc Extension](https://github.com/rcw3bb/gs-codenarc-ext) on your gosu source codes.

## Pre-requisites

* Java 11
* Gosu plugin

## Plugging in the gs-codenarc

In your **build.gradle** file add the following plugins:

```groovy
plugins {
  id "org.gosu-lang.gosu" version "6.1.0"
  id "xyz.ronella.gs-codenarc" version "2.0.0"
}
```

> If you wanted to create a **gosu-library** you can **clone the template** from https://github.com/rcw3bb/template-gosu-library and use this plugin.

This will add the **following tasks** within the group **Gosu CodeNarc**:

| Task Name      | Description                              |
| -------------- | ---------------------------------------- |
| gscodenarcMain | Run GSCodeNarc analysis for main classes |
| gscodenarcTest | Run GSCodeNarc analysis for test classes |

## Plugin Properties

| Property                          | Description                                                  | Type   | Default                                      |
| --------------------------------- | ------------------------------------------------------------ | ------ | -------------------------------------------- |
| gscodenarc.config                 | The CodeNarc configuration to use.                           | File   | <PROJECT_DIR>/config/codenarc/gscodenarc.xml |
| gscodenarc.maxPriority1Violations | The maximum P1 rule failures before the build fail.          | int    | 0                                            |
| gscodenarc.maxPriority2Violations | The maximum P2 rule failures before the build fail.          | int    | 0                                            |
| gscodenarc.maxPriority3Violations | The maximum P3 rule failures before the build fail.          | int    | 0                                            |
| gscodenarc.reportFormat           | The supported report format are: **html**, **xml** and **text** *(i.e. also check the [CodeNarc](https://codenarc.github.io/CodeNarc/) documentation)* | String | html                                         |
> The report location will be in the **<PROJECT_DIR>/build/reports/codenarc** directory.

## Configuration Directory

The location of the configuration files of the plugin is in the following directory:

```
<PROJECT_DIR>/config/codenarc
```

## The gscodenarc.xml File

The **gscodenarc.xml** holds the ruleset configuration for codenarc *(i.e. this is controlled by the gscodenarc.config property)*. The ruleset for gosu is as follows:

```xml
<ruleset xmlns="http://codenarc.org/ruleset/1.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://codenarc.org/ruleset/1.0 http://codenarc.org/ruleset-schema.xsd"
	xsi:noNamespaceSchemaLocation="http://codenarc.org/ruleset-schema.xsd">
	
	<ruleset-ref path='rulesets/gosu.xml'>
  
		<rule-config name='GosuClassSize'>
			<property name='maxLines' value='1200' />
		</rule-config>
		
		<rule-config name='GosuFunctionSize'>
			<property name='maxLines' value='25' />
		</rule-config>
		
		<rule-config name='GosuReturnCount'>
			<property name='maxReturnCount' value='4' />
		</rule-config>
		
		<rule-config name='GosuCyclomaticComplexity'>
			<property name='maxMethodComplexity' value='7' />
			<property name='priority' value='1' />
		</rule-config>
		
		<rule-config name='GosuNestedIf'>
			<property name='maxNestedDepth' value='2' />
		</rule-config>
		
		<rule-config name='GosuFunctionParameterLength'>
			<property name='maxParameters' value='4' />
		</rule-config>
		
		<!--You can exclude rules from being executed as follows-->
		<!--<exclude name='GosuFunctionSize' />-->
	
	</ruleset-ref>

</ruleset>
```

## The GSCodeNarcExt Task

The task that can **target source directories** other that the one provided by the **org.gosu-lang.gosu plugin** *(e.g. <PROJECT_DIR>/src/main/gosu)*.

### Properties

| Property    | Description                                           | Type                       | Default |
| ----------- | ----------------------------------------------------- | -------------------------- | ------- |
| config                 | The configuration to use.                           | File   | ${gscodenarc.config}                 |
| maxPriority1Violations | The maximum P1 rule failures before the build fail.          | int    | ${gscodenarc.maxPriority1Violations}  |
| maxPriority2Violations | The maximum P2 rule failures before the build fail.          | int    | ${gscodenarc.maxPriority2Violations}  |
| maxPriority3Violations | The maximum P3 rule failures before the build fail.          | int    | ${gscodenarc.maxPriority3Violations}  |
| sourceFiles | Specifies the directory to scan by gscodenarc plugin. | FileCollection |         |

### Usage

1. **Import** on your build script the following:

   ```groovy
   import xyz.ronella.gradle.plugin.gosu.codenarc.task.GSCodeNarcExt
   ```

2. **Create a task** for your **new target source directory**. 

   See the following example:

   ```groovy
   task testGosuFile(type: GSCodeNarcExt) {
       sourceFiles.from(file("ext/gosu"))
       maxPriority2Violations=1
   }
   ```

   > The example task above will scan the following directory:
   >
   > ```
   > <PROJECT_DIR>/ext/gosu
   > ```

   > The sample **testGosuFile task** was added to the group **Gosu CodeNarc**.

3. **Run** the **custom task**.

## The gs-codenarc-ext.version file

The file that can **override the default gs-codenarc-ext version** to use. Create the file **gs-codenarc-ext.version** in the **configuration directory** with the target gs-codenarc-ext version as follows:

```
1.1.2
```

## The repository.properties file

The properties file that can **override the default repository *(i.e. maven central)*** where to download the **gs-codenarc plugin dependency** *(e.g. gs-codenarc-ext)*. Create the file **repository.properties** in the **configuration directory** with the following properties:

* **url**

  The address of desired repository.

* **username** *(Optional)*

  The username to access the repository.

* **password** *(Required if username is provided)*

  The password associated to the username.

## Custom Libraries for GSCodeNarc plugin

Create a **libs directory** under the **configuration directory** like the following:

```
<PROJECT_DIR>/config/codenarc/libs
```

The files inside the libs directory will be considered when running the tasks for the GSCodeNarc plugin. However, to use the custom library you need to add or update the configuration file *(e.g. gscodenarc.xml)* to use a new ruleset configuration.

## Sample build.gradle File

```groovy
plugins {
  id "org.gosu-lang.gosu" version "6.1.0"
  id "xyz.ronella.gs-codenarc" version "2.0.0"
}

repositories {
  mavenCentral()
}

dependencies {
  compile group: 'org.gosu-lang.gosu', name: 'gosu-core', version: "1.15.8"
  testImplementation group: 'org.gosu-lang.gosu', name: 'gosu-test', version: "1.15.8"
}
```

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details

## [Build](BUILD.md)

## [Changelog](CHANGELOG.md)

## Author

* Ronaldo Webb
