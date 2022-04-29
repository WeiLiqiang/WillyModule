package com.wlq.willymodule.index.pkg.ui

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.loadmore.SimpleLoadMoreView
import com.wlq.willymodule.base.mvi.observeState
import com.wlq.willymodule.base.util.LogUtils
import com.wlq.willymodule.base.util.SizeUtils
import com.wlq.willymodule.common.base.BaseBusinessFragment
import com.wlq.willymodule.common.utils.livedata.IsRefresh
import com.wlq.willymodule.common.utils.livedata.ListStatus
import com.wlq.willymodule.common.view.SpaceItemDecoration
import com.wlq.willymodule.index.pkg.R
import com.wlq.willymodule.index.pkg.data.bean.Banner
import com.wlq.willymodule.index.pkg.databinding.FragmentIndexBinding
import com.wlq.willymodule.index.pkg.util.BannerImageLoader
import com.youth.banner.BannerConfig
import kotlinx.android.synthetic.main.fragment_index.*

class IndexFragment : BaseBusinessFragment<FragmentIndexBinding, IndexViewModel>(FragmentIndexBinding::inflate) {

    override val viewModel: IndexViewModel by viewModels()

    companion object {
        const val TAG_FRAGMENT = "IndexFragment"
        fun getInstance(): IndexFragment = IndexFragment()
    }

    private val indexArticleAdapter by lazy { HomeAdapter() }
    private val linearLayoutManager: LinearLayoutManager by lazy {
        LinearLayoutManager(activity)
    }
    private val banner by lazy { com.youth.banner.Banner(activity) }

    override fun initView(view: View) {
        initRecycleView()
        initBannerView()
    }

    override fun onVisible(isFirstVisible: Boolean) {
        LogUtils.i(TAG_FRAGMENT, "onVisible isFirstVisible:$isFirstVisible")
        if (isFirstVisible) {
            viewModel.getBannerList()
            viewModel.getArticleList(true)
        }
        banner.startAutoPlay()
    }

    override fun onInvisible() {
        LogUtils.i(TAG_FRAGMENT, "onInvisible")
        banner.stopAutoPlay()
    }

    private fun initRecycleView() {
        swipeRefreshLayout.run {
            setOnRefreshListener {
                viewModel.getBannerList()
                viewModel.getArticleList(true)
            }
        }
        recyclerView.run {
            layoutManager = linearLayoutManager
            adapter = indexArticleAdapter
            itemAnimator = DefaultItemAnimator()
            recyclerViewItemDecoration?.let { addItemDecoration(it) }
        }
        indexArticleAdapter.run {
            setOnItemClickListener { _, _, position ->
                //TODO 跳转到详细页面（webview activity）
            }
            setOnItemChildClickListener { _, view, position ->
                when (view.id) {
                    R.id.iv_like -> collectArticle(position)
                }
            }
            if (headerLayoutCount > 0) removeAllHeaderView()
            addHeaderView(banner)
            loadMoreModule.loadMoreView = SimpleLoadMoreView()
            loadMoreModule.setOnLoadMoreListener {
                viewModel.getArticleList(false)
            }
        }
    }

    private fun initBannerView() {
        banner.run {
            layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                SizeUtils.dp2px(240F)
            )
            setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE)
            setImageLoader(BannerImageLoader())
            setOnBannerListener { position ->
                run {
                    //TODO 点击Banner跳转
                }
            }
        }
    }

    override fun startObserve() {
        super.startObserve()

        viewModel.apply {
            uiBannerState.observe(viewLifecycleOwner, Observer {
                it.showSuccess?.let { list ->
                    setBanner(list)
                }
            })

            collectStates.run {
                observeState(this@IndexFragment, IndexCollectViewState::collectArticleList) {

                }
            }

            listStates.run {
                observeState(this@IndexFragment, IndexListViewState::listStatus) {
                    when (it) {
                        is ListStatus.Loading -> swipeRefreshLayout.isRefreshing = true
                        is ListStatus.Success -> swipeRefreshLayout.isRefreshing = false
                        is ListStatus.Error -> swipeRefreshLayout.isRefreshing = false
                        is ListStatus.LoadMoreEnd -> {
                            swipeRefreshLayout.isRefreshing = false
                            indexArticleAdapter.loadMoreModule.loadMoreEnd()
                        }
                    }
                }

                observeState(this@IndexFragment, IndexListViewState::articleList) {
                    indexArticleAdapter.run {
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

    private fun setBanner(bannerList: List<Banner>) {
        val bannerImages = mutableListOf<String>()
        val bannerTitles = mutableListOf<String>()
        val bannerUrls = mutableListOf<String>()
        for (bannerItem in bannerList) {
            bannerImages.add(bannerItem.imagePath)
            bannerTitles.add(bannerItem.title)
            bannerUrls.add(bannerItem.url)
        }
        banner.run {
            update(bannerImages, bannerTitles)
            setBannerStyle(BannerConfig.NUM_INDICATOR_TITLE)
            setDelayTime(3000)
        }
        banner.start()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun collectArticle(position: Int) {
        indexArticleAdapter.run {
            data[position].run {
                collect = !collect
                viewModel.collectArticle(id, collect)
            }
            notifyDataSetChanged()
        }
    }

    fun scrollToTop() {
        recyclerView.run {
            if (linearLayoutManager.findFirstVisibleItemPosition() > 20) {
                scrollToPosition(0)
            } else {
                smoothScrollToPosition(0)
            }
        }
    }

    private val recyclerViewItemDecoration by lazy {
        activity?.let {
            SpaceItemDecoration(it)
        }
    }
}