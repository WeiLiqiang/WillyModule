package com.wlq.willymodule.system.pkg.ui.fragment.knowledge

import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.loadmore.SimpleLoadMoreView
import com.wlq.willymodule.base.mvi.observeState
import com.wlq.willymodule.base.util.LogUtils
import com.wlq.willymodule.common.base.viewpager2.BaseBusiness2Fragment
import com.wlq.willymodule.common.utils.ext.startKtxActivity
import com.wlq.willymodule.common.utils.livedata.IsRefresh
import com.wlq.willymodule.common.utils.livedata.ListStatus
import com.wlq.willymodule.common.view.SpaceItemDecoration
import com.wlq.willymodule.system.pkg.databinding.FragmentRecyclerViewBinding
import com.wlq.willymodule.system.pkg.ui.activity.SystemTypeNormalActivity
import kotlinx.android.synthetic.main.fragment_recycler_view.*

class KnowledgeFragment : BaseBusiness2Fragment<FragmentRecyclerViewBinding, KnowledgeViewModel>(FragmentRecyclerViewBinding::inflate) {

    override val viewModel: KnowledgeViewModel by viewModels()
    private val knowledgeAdapter by lazy { KnowledgeAdapter() }
    private val linearLayoutManager: LinearLayoutManager by lazy {
        LinearLayoutManager(activity)
    }

    override fun initView(view: View) {
        initRecyclerView()
    }

    override fun firstVisibleToUser() {
        LogUtils.i(TAG_FRAGMENT, "firstVisibleToUser")
        refreshListData()
    }

    private fun initRecyclerView() {
        swipeRefreshLayout.run {
            setOnRefreshListener {
                refreshListData()
            }
        }
        recyclerView.run {
            layoutManager = linearLayoutManager
            adapter = knowledgeAdapter
            itemAnimator = DefaultItemAnimator()
            recyclerViewItemDecoration?.let { addItemDecoration(it) }
        }
        knowledgeAdapter.run {
            setOnItemClickListener { _, _, position ->
                startKtxActivity<SystemTypeNormalActivity>(value = SystemTypeNormalActivity.ARTICLE_LIST to knowledgeAdapter.data[position])
            }
            if (headerLayoutCount > 0) removeAllHeaderView()
            loadMoreModule.loadMoreView = SimpleLoadMoreView()
        }
    }

    private fun refreshListData() {
        viewModel.getSystemTypes(true)
    }

    override fun startObserve() {
        super.startObserve()
        viewModel.apply {

            listUiStates.run {
                observeState(this@KnowledgeFragment, KnowledgeUiState::listStatus) {
                    when (it) {
                        is ListStatus.Loading -> swipeRefreshLayout.isRefreshing = true
                        is ListStatus.Success -> swipeRefreshLayout.isRefreshing = false
                        is ListStatus.Error -> swipeRefreshLayout.isRefreshing = false
                        is ListStatus.LoadMoreEnd -> {
                            swipeRefreshLayout.isRefreshing = false
                            knowledgeAdapter.loadMoreModule.loadMoreEnd()
                        }
                    }
                }

                observeState(this@KnowledgeFragment, KnowledgeUiState::dataList) {
                    knowledgeAdapter.run {
                        loadMoreModule.isEnableLoadMore = false
                        if (it?.first is IsRefresh.TRUE) {
                            setList(it.second)
                        } else {
                            it?.second?.let { list ->
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
        const val TAG_FRAGMENT = "KnowledgeFragment"
        fun getInstance(): KnowledgeFragment = KnowledgeFragment()
    }
}