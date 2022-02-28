package xyz.ronella.gradle.plugin.gosu.codenarc.task

import org.gradle.api.DefaultTask
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.TaskAction
import xyz.ronella.gradle.plugin.gosu.codenarc.GSCodeNarcExtension

/**
 * The tasks to run gscodenarc to a non-default gosu source directories.
 *
 * @author Ronaldo Webb
 * @since 1.1.0
 */
abstract class GSCodeNarcExt extends DefaultTask {

    public GSCodeNarcExt() {
        group = 'Gosu CodeNarc'
    }

    @InputFiles
    abstract ConfigurableFileCollection getSourceFiles()

    @TaskAction
    def executeCommand() {
        GSCodeNarcExtension extension = project.extensions.gscodenarc
        if (sourceFiles!=null && sourceFiles.size()>0) {
            project.ant.codenarcGosu(ruleSetFiles: extension.config.toURI().toString(),
                    maxPriority1Violations: extension.maxPriority1Violations,
                    maxPriority2Violations: extension.maxPriority2Violations,
                    maxPriority3Violations: extension.maxPriority3Violations) {
                report(type: extension.reportFormat) {
                    option(name: "outputFile", value: "${project.rootProject.buildDir}/reports/codenarc/${name}.html")
                    option(name: "title", value: "Gosu Library Report for ${name}")
                }
                sourceFiles.each { ___file ->
                    if (___file.exists()) {
                        fileset(dir: ___file)
                    }
                }
            }
        }
        else {
            print "Nothing to check."
        }
    }
}
