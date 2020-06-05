package xyz.ronella.gradle.plugin.gosu.codenarc

import org.gradle.testkit.runner.GradleRunner
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Specification

import java.nio.file.Paths

import static org.gradle.testkit.runner.TaskOutcome.SUCCESS

class GSCodeNarcIntegrationWithoutGosuPluginTest  extends Specification {

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

    def "no gscodenarcMain task"() {
        when:
        def result = GradleRunner.create()
                .withProjectDir(testProjectDir.root)
                .withArguments('tasks', '--all')
                .build()

        then:
        !result.output.contains('gscodenarcMain')
    }

    def "no gscodenarcTest task"() {
        when:
        def result = GradleRunner.create()
                .withProjectDir(testProjectDir.root)
                .withArguments('tasks', '--all')
                .build()

        then:
        !result.output.contains('gscodenarcTest')
    }

    private void writeBuildFile() {
        buildFile << """
            buildscript {
              dependencies {
                classpath fileTree(dir: \"${Paths.get("build", "libs").toAbsolutePath().toString()
                    .replace("\\", "\\\\")}\", include: ['*.jar'])
              }
            }
            
            plugins {
              id "java"
            }              
            
            apply plugin: "xyz.ronella.gs-codenarc"
            
            repositories {
                mavenCentral()
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