package xyz.ronella.gradle.plugin.gosu.codenarc

import org.gradle.testkit.runner.GradleRunner

import java.nio.file.Paths

import static org.gradle.testkit.runner.TaskOutcome.*
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Specification

class GSCodeNarcIntegrationWithGosuPluginTest extends Specification {

    @Rule TemporaryFolder testProjectDir = new TemporaryFolder()
    File settingsFile
    File buildFile
    File confDir
    File confFile

    def setup() {
        settingsFile = testProjectDir.newFile('settings.gradle')
        buildFile = testProjectDir.newFile('build.gradle')
        confDir = testProjectDir.newFolder("config", "codenarc")
        confFile = Paths.get(confDir.absolutePath, "gscodenarc.xml").toFile()
        settingsFile << "rootProject.name = 'gs-codenarc-testing'"

        writeBuildFile()
        writeConfigFile()
    }

    def "sanity check"() {
        given:
        buildFile << """
            task helloWorld {
                doLast {
                    println 'Hello world!'
                }
            }
        """

        when:
        def result = GradleRunner.create()
                .withProjectDir(testProjectDir.root)
                .withArguments('helloWorld')
                .build()

        then:
        result.output.contains('Hello world!')
        result.task(":helloWorld").outcome == SUCCESS
    }

    def "defined external gosu file by extension property"() {
        given:

        def mainDir = testProjectDir.newFolder("src", "main", "gosu")
        def gosuFile = Paths.get(mainDir.absolutePath, "HelloWorld.gs").toFile()

        def mainDirStr =  mainDir.toPath().toFile().absolutePath.replaceAll('\\\\', '\\\\\\\\')

        buildFile << """
            gscodenarc.maxPriority2Violations=1
        """

        confFile.write("""
            <ruleset xmlns="http://codenarc.org/ruleset/1.0"
                    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                    xsi:schemaLocation="http://codenarc.org/ruleset/1.0 http://codenarc.org/ruleset-schema.xsd"
                    xsi:noNamespaceSchemaLocation="http://codenarc.org/ruleset-schema.xsd">
                <ruleset-ref path="rulesets/gosu.xml">
                    <rule-config name='GosuFunctionSize'>
                        <property name='maxLines' value='1'/>
                    </rule-config>                
                </ruleset-ref>
            </ruleset>
        """.stripIndent())

        gosuFile << """
        class HelloWorld {
        
          public function hello(name : String) {
            print("Hello " + name)
            print("Hello " + name)
          }
        
        }      
        """

        buildFile << """
            task testGosuFile(type: GSCodeNarcExt) {
                sourceFiles.from("${mainDirStr}")
            }
        """

        when:
        def result = GradleRunner.create()
                .withProjectDir(testProjectDir.root)
                .withArguments('testGosuFile')
                .build()

        then:
        println "Result: ${result.output}"

        def report = Paths.get(testProjectDir.getRoot().absolutePath, "build", "reports", "codenarc",
                "testGosuFile.html").toFile()
        report.exists()

        result.task(":testGosuFile").outcome == SUCCESS
    }

    def "defined external gosu file by local property"() {
        given:

        def mainDir = testProjectDir.newFolder("src", "main", "gosu")
        def gosuFile = Paths.get(mainDir.absolutePath, "HelloWorld.gs").toFile()

        def mainDirStr =  mainDir.toPath().toFile().absolutePath.replaceAll('\\\\', '\\\\\\\\')

        buildFile << """
        """

        confFile.write("""
            <ruleset xmlns="http://codenarc.org/ruleset/1.0"
                    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                    xsi:schemaLocation="http://codenarc.org/ruleset/1.0 http://codenarc.org/ruleset-schema.xsd"
                    xsi:noNamespaceSchemaLocation="http://codenarc.org/ruleset-schema.xsd">
                <ruleset-ref path="rulesets/gosu.xml">
                    <rule-config name='GosuFunctionSize'>
                        <property name='maxLines' value='1'/>
                    </rule-config>                
                </ruleset-ref>
            </ruleset>
        """.stripIndent())

        gosuFile << """
        class HelloWorld {
        
          public function hello(name : String) {
            print("Hello " + name)
            print("Hello " + name)
          }
        
        }      
        """

        buildFile << """
            task testGosuFile(type: GSCodeNarcExt) {
                sourceFiles.from("${mainDirStr}")
                maxPriority2Violations=1
            }
        """

        when:
        def result = GradleRunner.create()
                .withProjectDir(testProjectDir.root)
                .withArguments('testGosuFile')
                .build()

        then:
        println "Result: ${result.output}"

        def report = Paths.get(testProjectDir.getRoot().absolutePath, "build", "reports", "codenarc",
                "testGosuFile.html").toFile()
        report.exists()

        result.task(":testGosuFile").outcome == SUCCESS
    }

    def "defined external gosu file by local property with lib"() {
        given:

        def mainDir = testProjectDir.newFolder("src", "main", "gosu")
        def gosuFile = Paths.get(mainDir.absolutePath, "HelloWorld.gs").toFile()
        def libs = testProjectDir.newFolder("config", "codenarc", "libs")

        def mainDirStr =  mainDir.toPath().toFile().absolutePath.replaceAll('\\\\', '\\\\\\\\')

        buildFile << """
        """

        confFile.write("""
            <ruleset xmlns="http://codenarc.org/ruleset/1.0"
                    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                    xsi:schemaLocation="http://codenarc.org/ruleset/1.0 http://codenarc.org/ruleset-schema.xsd"
                    xsi:noNamespaceSchemaLocation="http://codenarc.org/ruleset-schema.xsd">
                <ruleset-ref path="rulesets/gosu.xml">
                    <rule-config name='GosuFunctionSize'>
                        <property name='maxLines' value='1'/>
                    </rule-config>                
                </ruleset-ref>
            </ruleset>
        """.stripIndent())

        gosuFile << """
        class HelloWorld {
        
          public function hello(name : String) {
            print("Hello " + name)
            print("Hello " + name)
          }
        
        }      
        """

        buildFile << """
            task testGosuFile(type: GSCodeNarcExt) {
                sourceFiles.from("${mainDirStr}")
                maxPriority2Violations=1
            }
        """

        when:
        def result = GradleRunner.create()
                .withProjectDir(testProjectDir.root)
                .withArguments('testGosuFile')
                .build()

        then:
        println "Result: ${result.output}"

        def report = Paths.get(testProjectDir.getRoot().absolutePath, "build", "reports", "codenarc",
                "testGosuFile.html").toFile()
        report.exists()

        result.task(":testGosuFile").outcome == SUCCESS
    }


    def "no gosu main to check"() {
        when:
        def result = GradleRunner.create()
                .withProjectDir(testProjectDir.root)
                .withArguments('gscodenarcMain')
                .build()

        then:
        result.output.contains('Nothing to check.')
        result.task(":gscodenarcMain").outcome == SUCCESS
    }

    def "no gosu test to check"() {
        when:
        def result = GradleRunner.create()
                .withProjectDir(testProjectDir.root)
                .withArguments('gscodenarcTest')
                .build()

        then:
        result.output.contains('Nothing to check.')
        result.task(":gscodenarcTest").outcome == SUCCESS
    }

    def "with gosu main source to check"() {
        given:

        def mainDir = testProjectDir.newFolder("src", "main", "gosu")
        def gosuFile = Paths.get(mainDir.absolutePath, "HelloWorld.gs").toFile()

        gosuFile << """
        class HelloWorld {
        
          public function hello(name : String) {
            print("Hello " + name)
          }
        
        }      
        """

        when:
        def result = GradleRunner.create()
                .withProjectDir(testProjectDir.root)
                .withArguments('gscodenarcMain')
                .build()

        then:
        def report = Paths.get(testProjectDir.getRoot().absolutePath, "build", "reports", "codenarc",
                "gscodenarcMain.html").toFile()
        report.exists()
        result.task(":gscodenarcMain").outcome == SUCCESS
    }

    def "with gosu test source to check"() {
        given:

        def mainDir = testProjectDir.newFolder("src", "test", "gosu")
        def gosuFile = Paths.get(mainDir.absolutePath, "HelloWorld.gs").toFile()

        gosuFile << """
        class HelloWorld {
        
          public function hello(name : String) {
            print("Hello " + name)
          }
        
        }      
        """

        when:
        def result = GradleRunner.create()
                .withProjectDir(testProjectDir.root)
                .withArguments('gscodenarcTest')
                .build()

        then:
        def report = Paths.get(testProjectDir.getRoot().absolutePath, "build", "reports", "codenarc",
                "gscodenarcTest.html").toFile()
        report.exists()
        result.task(":gscodenarcTest").outcome == SUCCESS
    }

    def "has gscodenarcMain task"() {
        when:
        def result = GradleRunner.create()
                .withProjectDir(testProjectDir.root)
                .withArguments('tasks', '--all')
                .build()

        then:
        result.output.contains('gscodenarcMain')
    }

    def "has gscodenarcTest task"() {
        when:
        def result = GradleRunner.create()
                .withProjectDir(testProjectDir.root)
                .withArguments('tasks', '--all')
                .build()

        then:
        result.output.contains('gscodenarcTest')
    }

    private void writeBuildFile() {
        buildFile << """
            import xyz.ronella.gradle.plugin.gosu.codenarc.task.GSCodeNarcExt 

            buildscript {
              dependencies {
                classpath fileTree(dir: \"${Paths.get("build", "libs").toAbsolutePath().toString()
                .replace("\\", "\\\\")}\", include: ['*.jar'])
              }
            }
            
            plugins {
              id "org.gosu-lang.gosu" version "6.1.0"
            }            
            
            apply plugin: "xyz.ronella.gs-codenarc"
            
            repositories {
                mavenCentral()
            }
            
            dependencies {
                compile group: 'org.gosu-lang.gosu', name: 'gosu-core', version: "1.14.3"
            }
            
        """.stripIndent()
    }

    private void writeConfigFile() {
        confFile << """
            <ruleset xmlns="http://codenarc.org/ruleset/1.0"
                    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                    xsi:schemaLocation="http://codenarc.org/ruleset/1.0 http://codenarc.org/ruleset-schema.xsd"
                    xsi:noNamespaceSchemaLocation="http://codenarc.org/ruleset-schema.xsd">
                <ruleset-ref path="rulesets/gosu.xml"/>
            </ruleset>
        """.stripIndent()
    }

}