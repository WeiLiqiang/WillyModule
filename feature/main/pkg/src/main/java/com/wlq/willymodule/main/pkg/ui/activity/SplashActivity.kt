package com.wlq.willymodule.main.pkg.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.wlq.willymodule.base.mvi.ui.activity.BaseVBActivity
import com.wlq.willymodule.main.pkg.databinding.ActivitySplashBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : BaseVBActivity<ActivitySplashBinding>(ActivitySplashBinding::inflate) {

    override fun initView() {
        binding.particleview.setOnParticleAnimListener {
            startActivity(Intent(this@SplashActivity, MainActivity::class.java))
            finish()
        }
        binding.particleview.startAnim()
    }

    override fun initData(savedInstanceState: Bundle?) {

    }

    override fun showLoading() {
    }

    override fun dismissLoading() {
    }
}