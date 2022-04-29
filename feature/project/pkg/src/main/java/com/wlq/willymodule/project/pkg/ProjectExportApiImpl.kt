package com.wlq.willymodule.project.pkg

import androidx.fragment.app.FragmentTransaction
import com.wlq.willymodule.project.pkg.ui.ProjectFragment.Companion.getInstance
import com.wlq.willymodule.base.util.ApiUtils
import com.wlq.willymodule.project.export.ProjectExportApi
import com.wlq.willymodule.project.pkg.ui.ProjectFragment

@ApiUtils.Api
class ProjectExportApiImpl : ProjectExportApi() {
    private var projectFragment: ProjectFragment? = null
    override fun showFragment(transaction: FragmentTransaction?, containerViewId: Int) {
        if (projectFragment == null) {
            projectFragment = getInstance()
            transaction?.add(containerViewId, projectFragment!!, ProjectFragment.TAG_FRAGMENT)
        } else {
            transaction?.show(projectFragment!!)
        }
    }

    override fun hideFragment(transaction: FragmentTransaction?) {
        if (projectFragment == null) {
            return
        }
        transaction?.hide(projectFragment!!)
    }

    override fun removeFragment(transaction: FragmentTransaction?) {
        if (projectFragment == null) {
            return
        }
        transaction?.remove(projectFragment!!)
    }

    override fun destroy() {
        projectFragment = null
    }
}