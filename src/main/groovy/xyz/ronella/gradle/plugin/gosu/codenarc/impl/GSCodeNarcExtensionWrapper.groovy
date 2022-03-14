package xyz.ronella.gradle.plugin.gosu.codenarc.impl

import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property
import xyz.ronella.gradle.plugin.gosu.codenarc.GSCodeNarcExtension
import xyz.ronella.gradle.plugin.gosu.codenarc.GSCodeNarcPlugin

/**
 * An implementation that provides default values to some of the properties.
 *
 * @author Ron Webb
 * @since 2.0.0
 */
class GSCodeNarcExtensionWrapper implements GSCodeNarcExtension {

    private GSCodeNarcExtension ext

    GSCodeNarcExtensionWrapper(GSCodeNarcExtension ext) {
        this.ext=ext
    }

    @Override
    Property<Integer> getMaxPriority1Violations() {
        return ext.maxPriority1Violations
    }

    @Override
    Property<Integer> getMaxPriority2Violations() {
        return ext.maxPriority2Violations
    }

    @Override
    Property<Integer> getMaxPriority3Violations() {
        return ext.maxPriority3Violations
    }

    @Override
    Property<String> getExtensionVersion() {
        if (!ext.extensionVersion.isPresent()) {
            ext.extensionVersion = GSCodeNarcPlugin.DEFAULT_GSCODENARC_VERSION
        }
        return ext.extensionVersion
    }

    @Override
    RegularFileProperty getConfig() {
        return ext.config
    }

    @Override
    Property<String> getReportFormat() {
        if (!ext.reportFormat.isPresent()) {
            ext.reportFormat = GSCodeNarcPlugin.DEFAULT_REPORT_FORMAT
        }
        return ext.reportFormat
    }
}