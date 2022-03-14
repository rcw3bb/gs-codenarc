package xyz.ronella.gradle.plugin.gosu.codenarc

import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property

interface GSCodeNarcExtension {
    Property<Integer> getMaxPriority1Violations()

    Property<Integer> getMaxPriority2Violations()

    Property<Integer> getMaxPriority3Violations()

    Property<String> getExtensionVersion()

    RegularFileProperty getConfig()

    Property<String> getReportFormat()
}
