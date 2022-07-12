package com.wlq.willymodule.wx.pkg.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.loadmore.SimpleLoadMoreView
import com.wlq.willymodule.base.mvi.observeState
import com.wlq.willymodule.common.base.viewpager2.BaseBusiness2Fragment
import com.wlq.willymodule.common.utils.livedata.IsRefresh
import com.wlq.willymodule.common.utils.livedata.ListStatus
import com.wlq.willymodule.common.view.SpaceItemDecoration
import com.wlq.willymodule.wx.pkg.databinding.FragmentRecyclerViewBinding
import com.wlq.willymodule.wx.pkg.ui.action.ListUiStates
import com.wlq.willymodule.wx.pkg.ui.action.WxIntent
import com.wlq.willymodule.wx.pkg.ui.viewmodel.WxChildViewModel
import kotlinx.android.synthetic.main.fragment_recycler_view.*

class WxChildFragment :
    BaseBusiness2Fragment<FragmentRecyclerViewBinding, WxChildViewModel>(FragmentRecyclerViewBinding::inflate) {

    companion object {
        private const val CID = "cid"
        fun newInstance(cid: Int): WxChildFragment {
            val fragment = WxChildFragment()
            val bundle = Bundle()
            bundle.putInt(CID, cid)
            fragment.arguments = bundle
            return fragment
        }
    }

    private val cid by lazy { arguments?.getInt(CID) }

    override val viewModel: WxChildViewModel by viewModels()
    private val wxChildAdapter by lazy { WxChildAdapter() }
    private val linearLayoutManager: LinearLayoutManager by lazy {
        LinearLayoutManager(activity)
    }

    override fun firstVisibleToUser() {
        viewModel.dispatchViewIntent(WxIntent.RefreshListData(true, cid!!))
    }

    override fun initView(view: View) {
        initRecyclerView()
    }

    private fun initRecyclerView() {
        swipeRefreshLayout.run {
            setOnRefreshListener {
                viewModel.dispatchViewIntent(WxIntent.RefreshListData(true, cid!!))
            }
        }
        recyclerView.run {
            layoutManager = linearLayoutManager
            adapter = wxChildAdapter
            itemAnimator = DefaultItemAnimator()
            recyclerViewItemDecoration?.let { addItemDecoration(it) }
        }
        wxChildAdapter.run {
            setOnItemClickListener { _, _, position ->
                //TODO 跳转到详细页面（webview activity）
            }
            setOnItemChildClickListener { _, view, position ->
                when (view.id) {

                }
            }
            if (headerLayoutCount > 0) removeAllHeaderView()
            loadMoreModule.loadMoreView = SimpleLoadMoreView()
            loadMoreModule.setOnLoadMoreListener {
                viewModel.dispatchViewIntent(WxIntent.RefreshListData(false, cid!!))
            }
        }
    }

    override fun startObserve() {
        super.startObserve()
        viewModel.apply {

            listUiStates.run {
                observeState(this@WxChildFragment, ListUiStates::listStatus) {
                    when (it) {
                        is ListStatus.Loading -> swipeRefreshLayout.isRefreshing = true
                        is ListStatus.Success -> swipeRefreshLayout.isRefreshing = false
                        is ListStatus.Error -> swipeRefreshLayout.isRefreshing = false
                        is ListStatus.LoadMoreEnd -> {
                            swipeRefreshLayout.isRefreshing = false
                            wxChildAdapter.loadMoreModule.loadMoreEnd()
                        }
                    }
                }

                observeState(this@WxChildFragment, ListUiStates::articleList) {
                    wxChildAdapter.run {
                        loadMoreModule.isEnableLoadMore = false
                        if (it?.first is IsRefresh.TRUE) {
                            setList(it.second?.datas)
                        } else {
                            it?.second?.datas?.let { list ->
                                addData(list)
                            }
                        }
                        loadMoreModule.isEnableLoadMore = true
                        loadMoreModule.loadMoreComplete()
                    }
                }
            }
        }
    }

    private val recyclerViewItemDecoration by lazy {
        activity?.let {
            SpaceItemDecoration(it)
        }
    }
}