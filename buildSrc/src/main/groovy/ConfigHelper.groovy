import org.gradle.api.Project
import org.gradle.api.ProjectEvaluationListener
import org.gradle.api.ProjectState
import org.gradle.api.invocation.Gradle

class ConfigHelper {

    static init(Gradle gradle) {
        generateDep(gradle)
        addCommonGradle(gradle)
        TaskDurationUtils.init(gradle)
    }

    /**
     * 根据config生成dep
     * @param gradle
     */
    private static void generateDep(Gradle gradle) {
        def configs = [:]
        for(Map.Entry<String, ModuleConfig> entry : Config.modules.entrySet()) {
            def (name, config) = [entry.key, entry.value]
            if (config.useLocal) {
                config.dep = gradle.rootProject.findProject(name)
            } else {
                config.dep = config.remotePath
            }
            configs.put(name, config)
        }
    }

    private static void addCommonGradle(Gradle gradle) {
        gradle.addProjectEvaluationListener(new ProjectEvaluationListener() {

            @Override
            void beforeEvaluate(Project project) {
                //在Project的build.gradle前运行以下逻辑
                if (project.subprojects.isEmpty()) {
                    if (project.path.contains("plugin")) {
                        return
                    }
                    if (project.name.endsWith("_app")) {
                        GLog.l(project.toString() + " applies buildApp.gradle")
                        project.apply {
                            from "${project.rootDir.path}/buildApp.gradle"
                        }
                    } else {
                        GLog.l(project.toString() + " applies buildLib.gradle")
                        project.apply {
                            from "${project.rootDir.path}/buildLib.gradle"
                        }
                    }
                }
            }

            @Override
            void afterEvaluate(Project project, ProjectState state) {
                //在Project的build.gradle后运行以下逻辑
            }
        })
    }

    static getApplyPkgs() {
        def pkgs = [:]
        for (Map.Entry<String, ModuleConfig> entry : Config.modules.entrySet()) {
            if (entry.value.isApply && entry.key.endsWith("_pkg")) {
                pkgs.put(entry.key, entry.value)
            }
        }
        GLog.d("getApplyPkgs = ${GLog.object2String(pkgs)}")
        return pkgs
    }

    static getApplyExports() {
        def exports = [:]
        for (Map.Entry<String, ModuleConfig> entry : Config.modules.entrySet()) {
            if (entry.value.isApply && entry.key.endsWith("_export")) {
                exports.put(entry.key, entry.value)
            }
        }
        GLog.d("getApplyExports = ${GLog.object2String(exports)}")
        return exports
    }

    static getApplyPlugins() {
        def plugins = [:]
        for(Map.Entry<String, PluginConfig> entry : Config.plugins.entrySet()) {
            if (entry.value.isApply) {
                plugins.put(entry.key, entry.value)
            }
        }
        GLog.d("getApplyPlugins = ${GLog.object2String(plugins)}")
        return plugins
    }
}