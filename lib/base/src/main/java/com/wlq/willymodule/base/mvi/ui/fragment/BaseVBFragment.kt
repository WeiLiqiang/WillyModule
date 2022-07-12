package com.wlq.willymodule.base.mvi.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

abstract class BaseVBFragment<VB : ViewBinding>(
    private val inflate: (LayoutInflater, ViewGroup?, Boolean) -> VB
) : Fragment() {

    protected var mViewCreated = false
    private var _binding: VB? = null
    private val binding: VB get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = inflate(inflater, container, false)
        mViewCreated = true
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        startObserve()
        super.onViewCreated(view, savedInstanceState)
        initView(view)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        mViewCreated = false
    }

    abstract fun initView(view: View)

    abstract fun startObserve()
}