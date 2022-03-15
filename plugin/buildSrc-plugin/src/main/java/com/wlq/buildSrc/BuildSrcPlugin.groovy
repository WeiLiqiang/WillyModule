package com.wlq.buildSrc

import org.gradle.api.Plugin
import org.gradle.api.Project

class BuildSrcPlugin implements Plugin<Project> {

    @Override
    void apply(Project target) {
        println 'apply BuildSrcPlugin'
        SettingsConfig.main()
    }
}