package com.wlq.willymodule.main.pkg.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.wlq.willymodule.base.mvi.view.activity.BaseVBActivity
import com.wlq.willymodule.main.pkg.databinding.ActivitySplashBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : BaseVBActivity<ActivitySplashBinding>(ActivitySplashBinding::inflate) {

    override fun initView() {
        lifecycleScope.launch {
            delay(2500)
            startActivity(Intent(this@SplashActivity, MainActivity::class.java))
        }
    }

    override fun initData(savedInstanceState: Bundle?) {

    }

    override fun showLoading() {
    }

    override fun dismissLoading() {
    }
}