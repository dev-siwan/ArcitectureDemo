package com.ksw.presentation.ui.addfeeds

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.ksw.base.BaseFragment
import com.ksw.base.utils.showListDialog
import com.ksw.domain.model.Feed
import com.ksw.presentation.BR
import com.ksw.presentation.R
import com.ksw.presentation.common.PickImageUtil
import com.ksw.presentation.databinding.FragmentAddFeedBinding
import com.theartofdev.edmodo.cropper.CropImage
import kotlinx.android.synthetic.main.fragment_add_feed.*
/**
 * 피드추가 프래그먼트 입니다.
 * 아이템[Feed]의 내용을 작성, 수정이 가능합니다.
 */

class AddFeedFragment : BaseFragment<FragmentAddFeedBinding, AddFeedViewModel>(
    R.layout.fragment_add_feed, AddFeedViewModel::class,
    BR.viewModel,
    R.menu.add_feeds_fragment_menu
) {

    //args 값
    private val args : AddFeedFragmentArgs by navArgs()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setHasOptionsMenu(true)

        binding {

            viewModel {

                getFeed(args.fid)

                //Floating Button 이벤트
                doGetPhotoEvent.observe(viewLifecycleOwner, Observer {
                    //이미지 로직
                    pickPhoto(this)
                })
                //피드 성공 이벤트
                successEvent.observe(viewLifecycleOwner, Observer {successType->
                    when(successType){
                        AddFeedViewModel.SuccessType.Create->{
                            setUpSnackBar(getString(R.string.make_feed_success))
                            onBackPressed()
                        }
                        AddFeedViewModel.SuccessType.Update->{
                            setUpSnackBar(getString(R.string.update_feed_success))
                            onBackPressed()
                        }
                    }
                })
                //에러 이벤트
                errorEvent.observe(viewLifecycleOwner, Observer { errorType ->
                    when (errorType) {
                        AddFeedViewModel.ErrorType.CreateFeed -> {
                            setUpSnackBar(getString(R.string.make_feed_error))
                        }
                        AddFeedViewModel.ErrorType.Image -> {
                            setUpSnackBar(getString(R.string.make_feed_image_error))
                        }
                        AddFeedViewModel.ErrorType.GetFeed->{
                            setUpSnackBar(getString(R.string.make_get_feed_error))
                        }
                        AddFeedViewModel.ErrorType.UpdateFeed->{
                            setUpSnackBar(getString(R.string.make_update_feed_error))
                        }
                    }
                })

                //필드 empty 이벤트
                emptyEvent.observe(viewLifecycleOwner, Observer { emptyType ->
                    when (emptyType) {
                        AddFeedViewModel.EmptyType.Title -> {
                            setUpSnackBar(getString(R.string.make_feed_empty_title))
                        }
                        AddFeedViewModel.EmptyType.Content -> {
                            setUpSnackBar(getString(R.string.make_feed_empty_content))
                        }
                    }
                })

                //피드 수정 할때 imgUrl 이 있을 경우 값을 들고와서 feedPhotoImageView 에 이미지를 나타냄
                imgUrl.observe(viewLifecycleOwner, Observer {
                    Glide.with(this@AddFeedFragment).load(it).centerCrop().into(feedPhotoImageView)
                })
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        //이미지 Result 로직
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                //앨범에서 들고와서 crop 으로 데이터 전달
                PickImageUtil.PICK_FROM_ALBUM -> PickImageUtil.getImageFromGallery(data!!)

                //카메라에서 들고와서 crop 으로 데이터 전달
                PickImageUtil.PICK_FROM_CAMERA -> PickImageUtil.getImageFromCamera()

                //크롭에서 로직 처리 후 result value 처리
                CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE -> {

                    val result = CropImage.getActivityResult(data)

                    PickImageUtil.setImage(result.uri) { imageFile, bitmap ->
                        viewModel?.apply {
                            feedImgFile = imageFile
                            isPhoto = true
                            feedPhotoImageView.setImageBitmap(bitmap)
                        }
                    }
                }

                //크롭 에러
                CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE -> {

                    /* showAlertDialog(getString(R.string.error_crop_message))*/
                    viewModel?.apply {
                        isPhoto = false
                    }
                }
                else -> PickImageUtil.cancelPick()
            }
        }
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.feed_create_done -> {
                viewModel?.saveFeed()
            }
            android.R.id.home ->{
                onBackPressed()
            }
        }
        return super.onOptionsItemSelected(item)
    }


    //이미지 로직
    private fun pickPhoto(viewModel: AddFeedViewModel) {
        //이미지가 있을때 없을때 구분하여 리스트 Dialog 제공
        val menuList = if (viewModel.isPhoto) {
            resources.getStringArray(R.array.afterPhotoMenu)
        } else {
            resources.getStringArray(R.array.beforePhotoMenu)
        }

        requireActivity().showListDialog(menuList, null) { position ->
            when (position) {
                0 -> {
                    PickImageUtil.pickFromGallery(
                        requireActivity(),
                        PickImageUtil.CROP_1_1
                    )
                }
                1 -> {
                    PickImageUtil.pickFromCamera(
                        requireActivity(),
                        PickImageUtil.CROP_1_1
                    )
                }
                2 -> {
                    viewModel.run {
                        isPhoto = false
                        feedImgFile = null
                    }
                    feedPhotoImageView.setImageBitmap(null)
                }
            }
        }
    }


    //BackPress 핸들링
    override fun onBackPressed(): Boolean {
        if (isVisible) {
            val action =
                AddFeedFragmentDirections.actionAddFeedFragmentDestToFeedFragmentDest()
            findNavController().navigate(action)
        }
        return super.onBackPressed()
    }


    //SnackBar
    private fun setUpSnackBar(text: String) {
        hideSoftKeyboard()
        Snackbar.make(requireView(), text, Snackbar.LENGTH_LONG).show()

    }

    //키보드 Hide 이벤트
    private fun hideSoftKeyboard() {
            val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(requireView().windowToken, 0)
    }
}
