package xyz.ronella.gradle.plugin.gosu.codenarc

import static org.junit.Assert.*

import org.gradle.api.Project
import org.gradle.api.plugins.quality.CodeNarcPlugin
import org.gradle.testfixtures.ProjectBuilder
import org.junit.Before
import org.junit.Test

class GSCodeNarcExtensionTest {
    private Project project;

    @Before
    public void initProject() {
        project = ProjectBuilder.builder().build()
        project.pluginManager.apply 'xyz.ronella.gs-codenarc'
    }

    @Test
    public void testDefaultToolVersion() {
        assertEquals(CodeNarcPlugin.DEFAULT_CODENARC_VERSION, project.extensions.gscodenarc.toolVersion)
    }

    @Test
    public void testUpdateToolVersion() {
        project.extensions.gscodenarc.toolVersion = "1.5"
        assertEquals("1.5", project.extensions.gscodenarc.toolVersion)
    }

    @Test
    public void testDefaultExtensionVersion() {
        assertEquals(GSCodeNarcPlugin.DEFAULT_GSCODENARC_VERSION, project.extensions.gscodenarc.extensionVersion)
    }

    @Test
    public void testUpdateExtensionVersion() {
        project.extensions.gscodenarc.extensionVersion = "1.1.0"
        assertEquals("1.1.0", project.extensions.gscodenarc.extensionVersion)
    }

    @Test
    public void testDefaultConfig() {
        def suffix="config\\codenarc\\gscodenarc.xml"
        assertTrue(project.extensions.gscodenarc.config.absolutePath.endsWith(suffix))
    }

}