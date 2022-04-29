package com.wlq.willymodule.system.pkg.ui.fragment.square

import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.loadmore.SimpleLoadMoreView
import com.wlq.willymodule.base.mvi.observeState
import com.wlq.willymodule.base.util.LogUtils
import com.wlq.willymodule.common.base.viewpager2.BaseBusiness2Fragment
import com.wlq.willymodule.common.utils.livedata.IsRefresh
import com.wlq.willymodule.common.utils.livedata.ListStatus
import com.wlq.willymodule.common.view.SpaceItemDecoration
import com.wlq.willymodule.system.pkg.databinding.FragmentRecyclerViewBinding
import com.wlq.willymodule.system.pkg.ui.ArticleUiState
import com.wlq.willymodule.system.pkg.ui.SystemViewModel
import kotlinx.android.synthetic.main.fragment_recycler_view.*

class SquareFragment : BaseBusiness2Fragment<FragmentRecyclerViewBinding, SystemViewModel>(FragmentRecyclerViewBinding::inflate) {

    override val viewModel: SystemViewModel by viewModels()
    private val squareAdapter by lazy { SquareAdapter() }
    private val linearLayoutManager: LinearLayoutManager by lazy {
        LinearLayoutManager(activity)
    }

    override fun initView(view: View) {
        initRecyclerView()
    }

    override fun firstVisibleToUser() {
        LogUtils.i(TAG_FRAGMENT, "firstVisibleToUser")
        refreshListData(true)
    }

    private fun refreshListData(isRefresh: Boolean) {
        viewModel.getSquareArticleList(isRefresh)
    }

    private fun initRecyclerView() {
        swipeRefreshLayout.run {
            setOnRefreshListener {
                refreshListData(true)
            }
        }
        recyclerView.run {
            layoutManager = linearLayoutManager
            adapter = squareAdapter
            itemAnimator = DefaultItemAnimator()
            recyclerViewItemDecoration?.let { addItemDecoration(it) }
        }
        squareAdapter.run {
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
                refreshListData(false)
            }
        }
    }

    override fun startObserve() {
        super.startObserve()
        viewModel.apply {

            listUiStates.run {
                observeState(this@SquareFragment, ArticleUiState::listStatus) {
                    when (it) {
                        is ListStatus.Loading -> swipeRefreshLayout.isRefreshing = true
                        is ListStatus.Success -> swipeRefreshLayout.isRefreshing = false
                        is ListStatus.Error -> swipeRefreshLayout.isRefreshing = false
                        is ListStatus.LoadMoreEnd -> {
                            swipeRefreshLayout.isRefreshing = false
                            squareAdapter.loadMoreModule.loadMoreEnd()
                        }
                    }
                }

                observeState(this@SquareFragment, ArticleUiState::articleList) {
                    squareAdapter.run {
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

    companion object {
        const val TAG_FRAGMENT = "SquareFragment"
        fun getInstance(): SquareFragment = SquareFragment()
    }
}