# GS CodeNarc Gradle Plugin

The plugin that runs [GS CodeNarc Extension](https://github.com/rcw3bb/gs-codenarc-ext) on your gosu source codes.

## Pre-requisites

* Java 8 (Minimum)
* Gosu plugin

## Plugging in the gs-codenarc

In your **build.gradle** file add the following plugins:

```groovy
plugins {
  id "org.gosu-lang.gosu" version "0.3.10"
  id "xyz.ronella.gs-codenarc" version "1.0.0"
}
```

> If you wanted to use **gosu 1.15**, check the https://github.com/rcw3bb/template-gosu-library and select **gosu-15** branch.

This will add the following tasks:

| Task Name      | Description                              |
| -------------- | ---------------------------------------- |
| gscodenarcMain | Run GSCodeNarc analysis for main classes |
| gscodenarcTest | Run GSCodeNarc analysis for test classes |

## Plugin Properties

| Property                          | Description                                                  | Type   | Default                        |
| --------------------------------- | ------------------------------------------------------------ | ------ | ------------------------------ |
| gscodenarc.config                 | The CodeNarc configuration to use.                           | File   | config/codenarc/gscodenarc.xml |
| gscodenarc.extensionVersion       | This is the [GS CodeNarc Extension](https://github.com/rcw3bb/gs-codenarc-ext) to use. | String | 1.0.0                          |
| gscodenarc.maxPriority1Violations | The maximum P1 rule failures before the build fail.          | int    | 0                              |
| gscodenarc.maxPriority2Violations | The maximum P2 rule failures before the build fail.          | int    | 0                              |
| gscodenarc.maxPriority3Violations | The maximum P3 rule failures before the build fail.          | int    | 0                              |
| gscodenarc.reportFormat           | The supported report format are: **html**, **xml** and **text** *(i.e. also check the [CodeNarc](https://codenarc.github.io/CodeNarc/) documentation)* | String | html                           |
> The report location will be in the **build/reports/codenarc** directory.

## The gscodenarc.xml File

The **gscodenarc.xml** holds the ruleset configuration for codenarc. The ruleset for gosu is as follows:

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

## Sample build.gradle File

```groovy
plugins {
  id "org.gosu-lang.gosu" version "0.3.10"
  id "xyz.ronella.gs-codenarc" version "1.0.0"
}

repositories {
  mavenCentral()
}

dependencies {
  compile group: 'org.gosu-lang.gosu', name: 'gosu-core', version: "1.14.9"
  testImplementation group: 'org.gosu-lang.gosu', name: 'gosu-test', version: "1.14.9"
}
```

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details

## [Build](BUILD.md)

## [Changelog](CHANGELOG.md)

## Author

* Ronaldo Webb
