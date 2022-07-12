package com.wlq.willymodule.base.mvi.ui.activity

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.wlq.willymodule.base.mvi.ui.IUiView

abstract class BaseVBActivity<VB : ViewBinding>(
    private val inflate: (LayoutInflater) -> VB
) : AppCompatActivity(), IUiView {

    lateinit var binding: VB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = inflate(layoutInflater)
        setContentView(binding.root)
        initView()
        initData(savedInstanceState)
    }

    abstract fun initView()

    abstract fun initData(savedInstanceState: Bundle?)
}