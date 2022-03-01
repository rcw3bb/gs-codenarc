package xyz.ronella.gradle.plugin.gosu.codenarc

import org.gradle.api.plugins.quality.CodeNarcPlugin

class GSCodeNarcExtension {

    private int maxPriority1Violations
    private int maxPriority2Violations
    private int maxPriority3Violations
    private String extensionVersion
    private File config
    private String reportFormat

    int getMaxPriority1Violations() {
        return maxPriority1Violations
    }

    void setMaxPriority1Violations(int maxPriority1Violations) {
        this.maxPriority1Violations = maxPriority1Violations
    }

    int getMaxPriority2Violations() {
        return maxPriority2Violations
    }

    void setMaxPriority2Violations(int maxPriority2Violations) {
        this.maxPriority2Violations = maxPriority2Violations
    }

    int getMaxPriority3Violations() {
        return maxPriority3Violations
    }

    void setMaxPriority3Violations(int maxPriority3Violations) {
        this.maxPriority3Violations = maxPriority3Violations
    }

    String getExtensionVersion() {
        return extensionVersion==null ? GSCodeNarcPlugin.DEFAULT_GSCODENARC_VERSION : extensionVersion
    }

    void setExtensionVersion(String extensionVersion) {
        this.extensionVersion = extensionVersion
    }

    File getConfig() {
        return config
    }

    void setConfig(File config) {
        this.config = config
    }

    String getReportFormat() {
        return reportFormat==null ? 'html' : reportFormat
    }

    void setReportFormat(String reportFormat) {
        this.reportFormat = reportFormat
    }
}
