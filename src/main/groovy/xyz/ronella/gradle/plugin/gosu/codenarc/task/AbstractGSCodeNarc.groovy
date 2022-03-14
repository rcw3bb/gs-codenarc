package xyz.ronella.gradle.plugin.gosu.codenarc.task

import org.gradle.api.DefaultTask
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property
import xyz.ronella.gradle.plugin.gosu.codenarc.GSCodeNarcExtension

abstract class AbstractGSCodeNarc extends DefaultTask {

    def runGSCodenarc(ConfigurableFileCollection sourceFiles,
                    GSCodeNarcExtension extension,
                    RegularFileProperty config,
                    Property<Integer> maxPriority1Violations,
                    Property<Integer> maxPriority2Violations,
                    Property<Integer> maxPriority3Violations,
                    String name) {
        if (sourceFiles!=null && sourceFiles.size()>0) {
            project.ant.codenarcGosu(ruleSetFiles: config.orElse(extension.config.get()).get().asFile.toURI().toString(),
                    maxPriority1Violations: maxPriority1Violations.orElse(extension.maxPriority1Violations.getOrElse(0)).get(),
                    maxPriority2Violations: maxPriority2Violations.orElse(extension.maxPriority2Violations.getOrElse(0)).get(),
                    maxPriority3Violations: maxPriority3Violations.orElse(extension.maxPriority3Violations.getOrElse(0)).get()) {
                report(type: extension.reportFormat.get()) {
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
