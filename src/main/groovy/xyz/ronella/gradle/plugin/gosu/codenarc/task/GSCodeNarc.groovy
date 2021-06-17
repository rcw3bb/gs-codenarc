package xyz.ronella.gradle.plugin.gosu.codenarc.task

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.SourceSet
import org.gradle.api.tasks.TaskAction
import xyz.ronella.gradle.plugin.gosu.codenarc.GSCodeNarcExtension

class GSCodeNarc extends DefaultTask {

    public GSCodeNarc() {
        group = 'Gosu CodeNarc'
    }

    @Internal
    SourceSet sourceSet

    @TaskAction
    def executeCommand() {
        GSCodeNarcExtension extension = project.extensions.gscodenarc
        List<File> dirs = new ArrayList<File>()

        sourceSet.allGosu.srcDirs.each { ___file ->
            if (___file.exists()) {
                dirs.add(___file)
            }
        }

        def name = sourceSet.name.capitalize()

        if (dirs.size()>0) {
            project.ant.codenarcGosu(ruleSetFiles: extension.config.toURI().toString(),
                    maxPriority1Violations: extension.maxPriority1Violations,
                    maxPriority2Violations: extension.maxPriority2Violations,
                    maxPriority3Violations: extension.maxPriority3Violations) {
                report(type: extension.reportFormat) {
                    option(name: "outputFile", value: "${project.rootProject.buildDir}/reports/codenarc/gscodenarc${name}.html")
                    option(name: "title", value: "Gosu Library Report for ${name}")
                }
                dirs.each { ___file ->
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
