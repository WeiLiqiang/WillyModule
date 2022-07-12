package com.wlq.willymodule.main.pkg.ui.activity

import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.widget.addTextChangedListener
import com.wlq.willymodule.base.mvi.observeState
import com.wlq.willymodule.base.util.ActivityUtils
import com.wlq.willymodule.common.base.BaseBusinessActivity
import com.wlq.willymodule.main.pkg.databinding.ActivityLoginBinding
import com.wlq.willymodule.main.pkg.ui.action.LoginIntent
import com.wlq.willymodule.main.pkg.ui.action.LoginViewState
import com.wlq.willymodule.main.pkg.ui.viewmodel.LoginViewModel
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.mToolbar

class LoginActivity :
    BaseBusinessActivity<ActivityLoginBinding, LoginViewModel>(ActivityLoginBinding::inflate) {

    override val viewModel: LoginViewModel by viewModels()

    override fun initView() {
        et_user_name.addTextChangedListener {
            viewModel.dispatchViewIntent(LoginIntent.UpdateUserName(it.toString()))
        }
        et_password.addTextChangedListener {
            viewModel.dispatchViewIntent(LoginIntent.UpdatePassword(it.toString()))
        }
        btn_login.setOnClickListener {
            viewModel.dispatchViewIntent(LoginIntent.Login)
        }
        btn_register.setOnClickListener {
            viewModel.dispatchViewIntent(LoginIntent.Register)
        }
        mToolbar.setNavigationOnClickListener {
            ActivityUtils.finishActivity(this@LoginActivity)
        }
    }

    override fun initData(savedInstanceState: Bundle?) {
        super.initData(savedInstanceState)
        viewModel.apply {
            viewStates.run {
                observeState(this@LoginActivity, LoginViewState::isLoginEnable) {
                    btn_login.isEnabled = it
                }
                observeState(this@LoginActivity, LoginViewState::passwordTipVisible) {

                }
                observeState(this@LoginActivity, LoginViewState::loginSuccess) {
                    if (it) {
                        ActivityUtils.finishActivity(this@LoginActivity)
                    }
                }
            }
        }
    }
}