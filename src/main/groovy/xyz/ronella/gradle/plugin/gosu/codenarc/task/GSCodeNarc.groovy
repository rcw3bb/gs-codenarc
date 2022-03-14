package xyz.ronella.gradle.plugin.gosu.codenarc.task

import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.SourceSet
import org.gradle.api.tasks.TaskAction
import xyz.ronella.gradle.plugin.gosu.codenarc.GSCodeNarcExtension
import xyz.ronella.gradle.plugin.gosu.codenarc.impl.GSCodeNarcExtensionWrapper

abstract class GSCodeNarc extends AbstractGSCodeNarc {

    GSCodeNarc() {
        group = 'Gosu CodeNarc'
    }

    @Internal
    SourceSet sourceSet

    @TaskAction
    def executeCommand() {

        GSCodeNarcExtension extension = new GSCodeNarcExtensionWrapper(project.extensions.gscodenarc)
        List<File> dirs = new ArrayList<File>()

        sourceSet.allGosu.srcDirs.each { ___file ->
            if (___file.exists()) {
                dirs.add(___file)
            }
        }

        def sourceFiles = project.files(dirs.toArray())
        def name = "gscodenarc${sourceSet.name.capitalize()}"
        def factory = project.getObjects()
        def propInt = factory.property(Integer.class)

        runGSCodenarc(sourceFiles, extension, factory.fileProperty(), propInt, propInt, propInt, name)
    }
}
