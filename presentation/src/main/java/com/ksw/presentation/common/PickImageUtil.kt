package com.ksw.presentation.common

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import com.wct.sportsmate.util.ImageResizeUtil
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


object PickImageUtil {

    private var tempFile: File? = null
    private var isCamera = false
    private var cropCode = 0

    private lateinit var activity: Activity


    fun pickFromGallery(activity: Activity, cropCode:Int) {

        this.activity = activity
        this.cropCode = cropCode

        TedPermission.with(activity)
            .setPermissionListener(object : PermissionListener {
                override fun onPermissionGranted() {
                    setImageFromGallery()
                }

                override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {

                }
            }).setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE)
            .check()
    }

    private fun setImageFromGallery() {
        isCamera = false
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        activity.startActivityForResult(intent, PICK_FROM_ALBUM)
    }


    fun getImageFromGallery(data: Intent) {
        val photoUri = data.data
        cropImage(photoUri!!)
    }

    fun pickFromCamera(activity: Activity,cropCode:Int) {

        PickImageUtil.activity = activity
        PickImageUtil.cropCode = cropCode

        TedPermission.with(activity)
            .setPermissionListener(object : PermissionListener {
                override fun onPermissionGranted() {
                    setImageFromCamera()
                }

                override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {

                }
            })
            .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
            .check()
    }

    private fun setImageFromCamera() {

        isCamera = true

        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        try {
            tempFile = createImageFile()

        } catch (e: IOException) {
            e.printStackTrace()
        }

        tempFile?.let {file->
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                FileProvider.getUriForFile(
                    activity,
                    "${activity.applicationContext.packageName}.fileprovider",
                    file
                )
            } else {
                Uri.fromFile(file)
            }.also {uri->
                intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
                activity.startActivityForResult(intent, PICK_FROM_CAMERA)
            }
        }

    }

    fun getImageFromCamera() {
        val photoUri = Uri.fromFile(tempFile)

        cropImage(photoUri)
    }

    fun cancelPick() {
        tempFile?.apply {
            if (exists()) {
                delete()
            }
        }
    }

    fun setImage(resultUri: Uri, action: (File, Bitmap) -> Unit) {

        val file = File(resultUri.path!!)

        ImageResizeUtil.resizeFile(file, file, 1280, isCamera)


        val options = BitmapFactory.Options()
        val originalBm = BitmapFactory.decodeFile(file.absolutePath, options)

        action(file, originalBm)

        tempFile = null

    }

    private fun cropImage(photoUri: Uri) {

        if (tempFile == null) {
            try {
                tempFile = createImageFile()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

        // 크롭 후 이미지 비율
        CropImage.activity(photoUri).apply {
            when (cropCode) {
                CROP_1_1 -> {
                    setAspectRatio(1, 1)
                    setCropShape(CropImageView.CropShape.RECTANGLE)
                    start(activity)
                }
            }
        }
    }


    private fun createImageFile(): File {

        // 이미지 파일 이름
        val cal = Calendar.getInstance().time
        val timeStamp = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.getDefault())
        val imageFileName = "sportsMate" + timeStamp.format(cal) + "_"

        // 이미지가 저장될 폴더 이름
        val storageDir = File(activity.getExternalFilesDir(null)!!.absolutePath + "/img/")
        if (!storageDir.exists()) storageDir.mkdirs()

        // 빈 파일 생성
        return File.createTempFile(imageFileName, ".jpg", storageDir)
    }
    
    //image pick code
    const val PICK_FROM_ALBUM = 1000
    const val PICK_FROM_CAMERA = 1001

    //crop code
    const val CROP_1_1 = 2000
}
