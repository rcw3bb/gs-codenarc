package xyz.ronella.gradle.plugin.gosu.codenarc.task

import org.gradle.api.DefaultTask
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.Optional
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

    @Optional
    @Input
    abstract Property<Integer> getMaxPriority1Violations()

    @Optional
    @Input
    abstract Property<Integer> getMaxPriority2Violations()

    @Optional
    @Input
    abstract Property<Integer> getMaxPriority3Violations()

    @Optional
    @InputFile
    abstract RegularFileProperty getConfig()

    @TaskAction
    def executeCommand() {
        GSCodeNarcExtension extension = project.extensions.gscodenarc
        if (sourceFiles!=null && sourceFiles.size()>0) {
            project.ant.codenarcGosu(ruleSetFiles: config.isPresent() ? config.get().asFile.toURI().toString() : extension.config.toURI().toString(),
                    maxPriority1Violations: maxPriority1Violations.getOrElse(extension.maxPriority1Violations),
                    maxPriority2Violations: maxPriority2Violations.getOrElse(extension.maxPriority2Violations),
                    maxPriority3Violations: maxPriority3Violations.getOrElse(extension.maxPriority3Violations)) {
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
