package com.wlq.willymodule.system.pkg.ui.fragment.systemtype

import android.annotation.SuppressLint
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
import com.wlq.willymodule.system.pkg.R
import com.wlq.willymodule.system.pkg.databinding.FragmentRecyclerViewBinding
import com.wlq.willymodule.system.pkg.ui.ArticleUiState
import com.wlq.willymodule.system.pkg.ui.SystemViewModel
import kotlinx.android.synthetic.main.fragment_recycler_view.*

class SystemTypeFragment :
    BaseBusiness2Fragment<FragmentRecyclerViewBinding, SystemViewModel>(FragmentRecyclerViewBinding::inflate) {

    override val viewModel: SystemViewModel by viewModels()
    private val isLogin = true
    private val cid by lazy { arguments?.getInt(CID) }
    private val isBlog by lazy { arguments?.getBoolean(BLOG) ?: false } // 区分是体系下的文章列表还是公众号下的文章列表
    private val systemArticleAdapter by lazy { SystemArticleAdapter() }

    override fun initView(view: View) {
        initRecyclerView()
    }

    override fun firstVisibleToUser() {
        refreshListData(true)
    }

    private fun initRecyclerView() {
        swipeRefreshLayout.run {
            setOnRefreshListener {
                refreshListData(true)
            }
        }
        recyclerView.run {
            layoutManager = linearLayoutManager
            adapter = systemArticleAdapter
            itemAnimator = DefaultItemAnimator()
            recyclerViewItemDecoration?.let { addItemDecoration(it) }
        }
        systemArticleAdapter.run {
            setOnItemClickListener { _, _, position ->
                //TODO 跳转到详细页面（webview activity）
            }
            setOnItemChildClickListener { _, view, position ->
                when (view.id) {
                    R.id.articleStar -> collectArticle(position)
                }
            }
            if (headerLayoutCount > 0) removeAllHeaderView()
            loadMoreModule.loadMoreView = SimpleLoadMoreView()
            loadMoreModule.setOnLoadMoreListener {
                refreshListData(false)
            }
        }
    }

    private fun refreshListData(isRefresh: Boolean) {
        cid?.let {
            if (this.isBlog)
            else
                viewModel.getKnowledgeArticleList(isRefresh, it)
        }
    }

    override fun startObserve() {
        super.startObserve()

        viewModel.apply {

            listUiStates.run {
                observeState(this@SystemTypeFragment, ArticleUiState::listStatus) {
                    when (it) {
                        is ListStatus.Loading -> swipeRefreshLayout.isRefreshing = true
                        is ListStatus.Success -> swipeRefreshLayout.isRefreshing = false
                        is ListStatus.Error -> swipeRefreshLayout.isRefreshing = false
                        is ListStatus.LoadMoreEnd -> {
                            swipeRefreshLayout.isRefreshing = false
                            systemArticleAdapter.loadMoreModule.loadMoreEnd()
                        }
                    }
                }

                observeState(this@SystemTypeFragment, ArticleUiState::articleList) {
                    systemArticleAdapter.run {
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

    @SuppressLint("NotifyDataSetChanged")
    private fun collectArticle(position: Int) {
        if (isLogin) {
            systemArticleAdapter.run {
                data[position].run {
                    collect = !collect
                    viewModel.collectArticle(id, collect)
                }
                notifyDataSetChanged()
            }
        } else {
            //TODO 跳转到登录页面
        }
    }

    private val linearLayoutManager: LinearLayoutManager by lazy {
        LinearLayoutManager(activity)
    }

    private val recyclerViewItemDecoration by lazy {
        activity?.let {
            SpaceItemDecoration(it)
        }
    }

    companion object {
        const val TAG_FRAGMENT = "SystemTypeFragment"
        private const val CID = "cid"
        private const val BLOG = "blog"
        fun newInstance(cid: Int, isBlog: Boolean): SystemTypeFragment {
            val fragment = SystemTypeFragment()
            val bundle = Bundle()
            bundle.putBoolean(BLOG, isBlog)
            bundle.putInt(CID, cid)
            fragment.arguments = bundle
            return fragment
        }
    }
}