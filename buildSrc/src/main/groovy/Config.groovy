
class Config {

    static applicationId = 'com.wlq.willymodule'
    static appName = 'WillyModule'

    static compileSdkVersion = 31
    static minSdkVersion = 21
    static targetSdkVersion = 31
    static versionCode = 1_000_000
    static versionName = '1.0.0'

    static gradlePluginVersion = '4.1.3'
    static kotlinVersion = '1.4.20'
    static ktxVersion = '1.2.0'
    static lifecycleVersion = '2.4.0'

    static modules = [
            /*Don't delete this line*/
            /*Generated by "module_config.json"*/
            plugin_api_gradle_plugin   : new ModuleConfig(isApply: true , useLocal: true , localPath: "./plugin/api-gradle-plugin"),
            plugin_bus_gradle_plugin   : new ModuleConfig(isApply: true , useLocal: true , localPath: "./plugin/bus-gradle-plugin"),
            plugin_lib_base_transform  : new ModuleConfig(isApply: true , useLocal: true , localPath: "./plugin/lib/base-transform", remotePath: "io.github.weiliqiang:base-transform:1.0.6"),
            feature_mock               : new ModuleConfig(isApply: false, useLocal: true , localPath: "./feature/mock"),
            feature_main_app           : new ModuleConfig(isApply: true , useLocal: true , localPath: "./feature/main/app"),
            feature_main_pkg           : new ModuleConfig(isApply: true , useLocal: true , localPath: "./feature/main/pkg"),
            feature_index_app          : new ModuleConfig(isApply: true , useLocal: true , localPath: "./feature/index/app"),
            feature_index_export       : new ModuleConfig(isApply: true , useLocal: true , localPath: "./feature/index/export"),
            feature_index_pkg          : new ModuleConfig(isApply: true , useLocal: true , localPath: "./feature/index/pkg"),
            lib_base                   : new ModuleConfig(isApply: true , useLocal: true , localPath: "./lib/base", remotePath: "io.github.weiliqiang:lib_base:1.0.7"),
            lib_common                 : new ModuleConfig(isApply: true , useLocal: true , localPath: "./lib/common", remotePath: "io.github.weiliqiang:lib_common:1.0.1"),
            /*Don't delete this line*/
    ]

    static plugins = [
            plugin_gradle               : new PluginConfig(path: "com.android.tools.build:gradle:$gradlePluginVersion"),
            plugin_kotlin               : new PluginConfig(path: "org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion"),

            plugin_api          : new PluginConfig(isApply: true, useLocal: false, path: "io.github.weiliqiang:api-gradle-plugin:1.0.10", id: "io.github.weiliqiang.api"),
            plugin_bus          : new PluginConfig(isApply: true, useLocal: false, path: "io.github.weiliqiang:bus-gradle-plugin:1.0.2", id: "io.github.weiliqiang.bus"),
            plugin_mehtodTrace  : new PluginConfig(isApply: false, useLocal: false, path: "io.github.weiliqiang:methodTrace-plugin:1.0.3", id: "io.github.weiliqiang.methodTrace"),
    ]

    static libs = [
            androidx_multidex           : new LibConfig(path: "androidx.multidex:multidex:2.0.1"),
            androidx_constraint         : new LibConfig(path: "androidx.constraintlayout:constraintlayout:2.1.3"),
            appcompat                   : new LibConfig(path: "androidx.appcompat:appcompat:1.2.0"),
            recyclerview                : new LibConfig(path: "androidx.recyclerview:recyclerview:1.2.0"),
            core_ktx                    : new LibConfig(path: "androidx.core:core-ktx:1.6.0"),
            fragment_ktx                : new LibConfig(path: "androidx.fragment:fragment-ktx:$ktxVersion"),
            activity_ktx                : new LibConfig(path: "androidx.activity:activity-ktx:$ktxVersion"),
            viewmodel_ktx               : new LibConfig(path: "androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycleVersion"),
            livedata_ktx                : new LibConfig(path: "androidx.lifecycle:lifecycle-livedata-ktx:$lifecycleVersion"),
            lifecycle_ktx               : new LibConfig(path: "androidx.lifecycle:lifecycle-runtime-ktx:$lifecycleVersion"),
            view_pager2                 : new LibConfig(path: "androidx.viewpager2:viewpager2:1.0.0"),
            swiperefreshlayout          : new LibConfig(path: "androidx.swiperefreshlayout:swiperefreshlayout:1.1.0"),
            material                    : new LibConfig(path: "com.google.android.material:material:1.2.0"),

            kotlin                     : new LibConfig(path: "org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlinVersion"),
            kotlin_coroutines           : new LibConfig(path: "org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.9"),
            kotlin_coroutines_Android   : new LibConfig(path: "org.jetbrains.kotlinx:kotlinx-coroutines-android:1.3.9"),

            retrofit                    : new LibConfig(path: "com.squareup.retrofit2:retrofit:2.9.0"),
            okhttp                      : new LibConfig(path: "com.squareup.okhttp3:okhttp:4.9.3"),
            converter_gson              : new LibConfig(path: "com.squareup.retrofit2:converter-gson:2.9.0"),
            converter_moshi             : new LibConfig(path: "com.squareup.retrofit2:converter-moshi:2.9.0"),
            logging_interceptor         : new LibConfig(path: "com.squareup.okhttp3:logging-interceptor:4.9.0"),

            moshi_kotlin                : new LibConfig(path: "com.squareup.moshi:moshi-kotlin:1.11.0"),
            moshi                       : new LibConfig(path: "com.squareup.moshi:moshi:1.11.0"),
            gson                        : new LibConfig(path: "com.google.code.gson:gson:2.8.5"),
            glide                       : new LibConfig(path: "com.github.bumptech.glide:glide:4.13.0"),
            glide_processor             : new LibConfig(path: "com.github.bumptech.glide:compiler:4.13.0"),
            glide_okhttp3               : new LibConfig(path: "com.github.bumptech.glide:okhttp3-integration:4.11.0"),

            brvh                        : new LibConfig(path: "com.github.CymChad:BaseRecyclerViewAdapterHelper:3.0.7"),
            banner                      : new LibConfig(path: "com.youth.banner:banner:1.4.10"),

            commons_io                  : new LibConfig(path: "commons-io:commons-io:2.6"),

            auto_size                   : new LibConfig(path: "com.github.JessYanCoding:AndroidAutoSize:v1.2.1")
    ]
}