package com.wlq.willymodule.system.pkg.ui.fragment.project

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

class ProjectFragment :
    BaseBusiness2Fragment<FragmentRecyclerViewBinding, SystemViewModel>(FragmentRecyclerViewBinding::inflate) {

    override val viewModel: SystemViewModel by viewModels()
    private val isLogin = true
    private val cid by lazy { arguments?.getInt(CID) }
    private val isLasted by lazy { arguments?.getBoolean(LASTED) }
    private val projectAdapter by lazy { ProjectAdapter() }
    private val linearLayoutManager: LinearLayoutManager by lazy {
        LinearLayoutManager(activity)
    }

    override fun initView(view: View) {
        initRecyclerView()
    }

    private fun refreshListData(isRefresh: Boolean) {
        isLasted?.run {
            if (this) {
                viewModel.getNewProjectList(isRefresh)
            } else {
                cid?.let {

                }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initRecyclerView() {
        swipeRefreshLayout.run {
            setOnRefreshListener {
                refreshListData(true)
            }
        }
        recyclerView.run {
            layoutManager = linearLayoutManager
            adapter = projectAdapter
            itemAnimator = DefaultItemAnimator()
            recyclerViewItemDecoration?.let { addItemDecoration(it) }
        }
        projectAdapter.run {
            setOnItemClickListener { _, _, position ->
                //TODO 跳转到详细页面 webView activity
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

    @SuppressLint("NotifyDataSetChanged")
    private fun collectArticle(position: Int) {
        if (isLogin) {
            projectAdapter.run {
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

    override fun firstVisibleToUser() {
        refreshListData(true)
    }

    override fun startObserve() {
        super.startObserve()

        viewModel.apply {

            listUiStates.run {
                observeState(this@ProjectFragment, ArticleUiState::listStatus) {

                    when (it) {
                        is ListStatus.Loading -> swipeRefreshLayout.isRefreshing = true
                        is ListStatus.Success -> swipeRefreshLayout.isRefreshing = false
                        is ListStatus.Error -> swipeRefreshLayout.isRefreshing = false
                        is ListStatus.LoadMoreEnd -> {
                            swipeRefreshLayout.isRefreshing = false
                            projectAdapter.loadMoreModule.loadMoreEnd()
                        }
                    }
                }

                observeState(this@ProjectFragment, ArticleUiState::articleList) {
                    projectAdapter.run {
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
        const val TAG_FRAGMENT = "ProjectFragment"
        private const val CID = "projectCid"
        private const val LASTED = "lasted"
        fun getInstance(cid: Int, isLasted: Boolean): ProjectFragment {
            val fragment = ProjectFragment()
            val bundle = Bundle()
            bundle.putInt(CID, cid)
            bundle.putBoolean(LASTED, isLasted)
            fragment.arguments = bundle
            return fragment
        }
    }
}