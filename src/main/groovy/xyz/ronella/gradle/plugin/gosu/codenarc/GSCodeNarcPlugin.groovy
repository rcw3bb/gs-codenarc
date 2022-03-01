package xyz.ronella.gradle.plugin.gosu.codenarc

import org.gradle.api.Action
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.DependencySet
import org.gradle.api.logging.Logger
import org.gradle.api.logging.Logging
import org.gradle.api.plugins.JavaPluginConvention
import xyz.ronella.gradle.plugin.gosu.codenarc.task.GSCodeNarc

class GSCodeNarcPlugin implements Plugin<Project> {

    private final Logger LOG = Logging.getLogger(GSCodeNarc.class)

    public static final String DEFAULT_GSCODENARC_VERSION = "1.1.1"

    protected def createAntTask(Project project) {
        withBasePlugin(project) {
            project.ant.taskdef(name: 'codenarcGosu', classname: 'org.codenarc.ant.CodeNarcTask') {
                classpath {
                    def configuration = project.configurations.getByName(getConfigurationName())
                    configuration.collect { ___file ->
                        fileset(file: ___file)
                    }
                }
            }
        }
    }

    protected String getToolName() {
        return "GSCodeNarc"
    }

    protected String getTaskBaseName() {
        return this.getToolName().toLowerCase();
    }

    protected String getConfigurationName() {
        return this.getToolName().toLowerCase();
    }

    protected def initDepedencies(Project project) {
        final def configuration = project.configurations.create(getConfigurationName())
        configuration.setVisible(false)
        configuration.setTransitive(true)

        configuration.defaultDependencies(new Action<DependencySet>() {
            public void execute(DependencySet dependencies) {
                dependencies.add(project.getDependencies().create("xyz.ronella.gosu:gs-codenarc-ext:${getGSCodeNarcExtVersion(project)}"))
            }
        })
    }

    protected String getGSCodeNarcExtVersion(Project project) {
        def versionFile = project.rootProject.file("config/codenarc/gs-codenarc-ext.version")
        def actualVersion = project.extensions.gscodenarc.getExtensionVersion()
        if (versionFile.exists()) {
            def extVersion = versionFile.text.trim()
            if (extVersion!='') {
                actualVersion=extVersion
            }
        }

        return actualVersion
    }

    protected def createExtension(Project project) {
        def extension = project.extensions.create(getTaskBaseName(), GSCodeNarcExtension.class);
        def config = extension.getConfig()
        if (null==config) {
            extension.setConfig(project.rootProject.file("config/codenarc/gscodenarc.xml"))
        }
    }

    private def customRepository(Project project, Properties propRepository) {
        def propUrl = propRepository.url
        def propUsername = propRepository.username
        def propPassword = propRepository.password

        if (propUrl) {
            println("Using repository ${propUrl}")
            project.repositories {
                maven {
                    url "${propUrl}"
                    if (propUsername && propPassword) {
                        credentials {
                            username "${propUsername}"
                            password "${propPassword}"
                        }
                    }
                }
            }
        }
    }

    protected def initRepositories(Project project) {
        def fileRepository = project.rootProject.file("config/codenarc/repository.properties")
        if (fileRepository.exists()) {
            def propRepository = new Properties()
            propRepository.load(fileRepository.newReader())
            customRepository(project, propRepository)
        }
        else {
            project.repositories {
                mavenCentral()
            }
        }
    }

    protected void withBasePlugin(Project project, Action<Plugin> action) {
        Class<?> clazz
        try {
            clazz = Class.forName("org.gosulang.gradle.GosuBasePlugin")
            project.getPlugins().withType(clazz, action)
        }
        catch (ClassNotFoundException exp) {
            LOG.warn('Gosu plugin not detected.')
        }
    }

    protected JavaPluginConvention getJavaPluginConvention(Project project) {
        return (JavaPluginConvention) project.getConvention().getPlugin(JavaPluginConvention.class)
    }

    protected createTasks(Project project) {
        withBasePlugin(project) {
            def convention = getJavaPluginConvention(project)
            convention.sourceSets.each { ___sourceSet ->
                String name = ___sourceSet.name
                def provider = project.tasks.register("${getTaskBaseName()}${name.capitalize()}", GSCodeNarc.class)
                def gsCodeNarcTask = provider.get()
                gsCodeNarcTask.sourceSet = ___sourceSet
                gsCodeNarcTask.setDescription("Run GSCodeNarc analysis for ${name} classes");
            }
        }
    }

    @Override
    void apply(Project project) {
        createExtension(project)
        initRepositories(project)
        initDepedencies(project)
        createAntTask(project)
        createTasks(project)
    }
}
