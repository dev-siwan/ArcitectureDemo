package com.ksw.presentation.ui.feeds


import android.os.Bundle
import android.view.MenuItem
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.paging.PagedListAdapter
import com.google.android.material.snackbar.Snackbar
import com.ksw.base.BaseFragment
import com.ksw.base.utils.showConfirmDialog
import com.ksw.base.utils.showListDialog
import com.ksw.domain.common.NetworkState
import com.ksw.domain.model.Feed
import com.ksw.presentation.BR
import com.ksw.presentation.R
import kotlinx.android.synthetic.main.fragment_feeds.*

/**
 * 피드리스트 프래그먼트 입니다.
 * 아이템[Feed]리스트를 뿌려주고 리스트를 통해 삭제, 수정이 가능합니다.
 */
class FeedsFragment :
    BaseFragment<ViewDataBinding, FeedsViewModel>(
        R.layout.fragment_feeds,
        FeedsViewModel::class,
        BR.viewModel,
        menuId = R.menu.feeds_fragment_menu
    ) {

    private val listAdapter: PagedListAdapter<Feed, FeedListAdapter.ViewHolder> by lazy {
        FeedListAdapter(viewModel = viewModel!!)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setHasOptionsMenu(true)

        binding {

            viewModel {
                recycler_view.adapter = listAdapter

                //리프레쉬
                refreshItem(this)

                //Feed PagedList 옵저버
                feeds.observe(viewLifecycleOwner, Observer { list ->
                    listAdapter.submitList(list)
                })

                //FeedList 가 empty 일때 피드만들기 버튼 이벤트
                addFeedClickEvent.observe(viewLifecycleOwner, Observer {
                    navigationToFeedEdit(null)
                })

                //FeedItem 옵션버튼 누를때 listDialog show
                feedOptionClickEvent.observe(viewLifecycleOwner, Observer {
                    pickFeedOptionMenu(this,it)
                })

                //Feed Delete 옵저버
                deleteResult.observe(viewLifecycleOwner, Observer {
                    if(it){
                        setUpSnackBar(getString(R.string.delete_feed_success))
                    }else{
                        setUpSnackBar(getString(R.string.delete_feed_error))
                    }
                })
            }
        }
    }

    //상단바 아이템
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.feed_add -> {
                navigationToFeedEdit(null)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    //옵션메뉴 다이아로그 리스트
    private fun pickFeedOptionMenu(viewModel: FeedsViewModel,feed: Feed) {
        val menuList = resources.getStringArray(R.array.feedMenu)

        requireActivity().showListDialog(menuList, null) { position ->
            when (position) {
                0 -> {
                    navigationToFeedEdit(feed.fid)
                }
                1 -> {
                    viewModel.deleteFeed(feed)
                }
            }
        }
    }

    //Navigation Component addFragment 로 이동 [args = fid]
    private fun navigationToFeedEdit(fid: String?) {
        val action = FeedsFragmentDirections.actionHomeFragmentToAddFeedFragment(fid)
        findNavController().navigate(action)
    }

    private fun setUpSnackBar(text: String) {
        Snackbar.make(requireView(), text, Snackbar.LENGTH_LONG).show()
    }

    //refresh 로직
    private fun refreshItem(viewModel: FeedsViewModel){
        refresh_layout.setOnRefreshListener {

            viewModel.refresh()

            viewModel.networkState.observe(viewLifecycleOwner, Observer {
                refresh_layout.isRefreshing = it == NetworkState.LOADING
            })
        }
    }

    //backPress 핸들링
    override fun onBackPressed(): Boolean {
        if (isVisible) {
            requireContext().showConfirmDialog(null,getString(R.string.app_finish_message),getString(R.string.positive_text),getString(R.string.negative_text)){
                requireActivity().finish()
            }
        }
        return super.onBackPressed()
    }

}
