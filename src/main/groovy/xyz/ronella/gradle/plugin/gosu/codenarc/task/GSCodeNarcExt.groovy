package xyz.ronella.gradle.plugin.gosu.codenarc.task

import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.TaskAction
import xyz.ronella.gradle.plugin.gosu.codenarc.GSCodeNarcExtension
import xyz.ronella.gradle.plugin.gosu.codenarc.impl.GSCodeNarcExtensionWrapper

/**
 * The tasks to run gscodenarc to a non-default gosu source directories.
 *
 * @author Ronaldo Webb
 * @since 1.1.0
 */
abstract class GSCodeNarcExt extends AbstractGSCodeNarc {

    GSCodeNarcExt() {
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
        GSCodeNarcExtension extension = new GSCodeNarcExtensionWrapper(project.extensions.gscodenarc)
        runGSCodenarc(sourceFiles, extension, config, maxPriority1Violations, maxPriority2Violations,
                maxPriority3Violations, name)

    }
}
