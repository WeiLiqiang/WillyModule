package com.wlq.willymodule.main.pkg.ui.activity

import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import com.wlq.willymodule.base.util.ActivityUtils
import com.wlq.willymodule.base.util.LogUtils
import com.wlq.willymodule.common.base.BaseBusinessActivity
import com.wlq.willymodule.main.pkg.databinding.ActivityLoginBinding
import com.wlq.willymodule.main.pkg.ui.intent.LoginEnable
import com.wlq.willymodule.main.pkg.ui.intent.LoginEvent
import com.wlq.willymodule.main.pkg.ui.intent.LoginStatus
import com.wlq.willymodule.main.pkg.ui.viewmodel.LoginViewModel
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.coroutines.flow.collect

class LoginActivity :
    BaseBusinessActivity<ActivityLoginBinding, LoginViewModel>(ActivityLoginBinding::inflate) {

    override val viewModel: LoginViewModel by viewModels()

    override fun initView() {
        et_user_name.addTextChangedListener {
            viewModel.sendEvent(LoginEvent.UpdateUserOrPass(it.toString(), et_password.text.toString(), true))
        }
        et_password.addTextChangedListener {
            viewModel.sendEvent(LoginEvent.UpdateUserOrPass(et_user_name.text.toString(), it.toString(), false))
        }
        btn_login.setOnClickListener {
            viewModel.sendEvent(LoginEvent.Login(binding.etUserName.text.toString(), binding.etPassword.text.toString()))
        }
        btn_register.setOnClickListener {
            viewModel.sendEvent(LoginEvent.Register)
        }
        mToolbar.setNavigationOnClickListener {
            ActivityUtils.finishActivity(this@LoginActivity)
        }
    }

    override fun initData(savedInstanceState: Bundle?) {
        super.initData(savedInstanceState)
        lifecycleScope.launchWhenStarted {
            viewModel.uiState.collect {
                when (it.loginStatus) {
                    is LoginStatus.SUCCESS -> {
                        ActivityUtils.finishActivity(this@LoginActivity)
                    }
                    is LoginStatus.FAILURE -> {
                        LogUtils.i("LoginStatus.FAILURE")
                    }
                    else -> {}
                }
                when (it.loginEnable) {
                    is LoginEnable.Enable -> {
                        btn_login.isEnabled = true
                    }
                    is LoginEnable.Disable -> {
                        btn_login.isEnabled = false
                    }
                    else -> {}
                }
            }
        }
    }
}