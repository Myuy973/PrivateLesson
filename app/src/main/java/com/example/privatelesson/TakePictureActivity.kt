package com.example.privatelesson

import android.Manifest
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.privatelesson.databinding.ActivityTakePictureBinding
import java.text.SimpleDateFormat
import java.util.*

class TakePictureActivity : AppCompatActivity() {

    val REQUEST_PREVIEW = 1
    val REQUEST_PICTURE = 2
    val REQUEST_EXTERNAL_STORAGE = 3

    lateinit var currentPhotoUri: Uri

    private lateinit var binding: ActivityTakePictureBinding
    private lateinit var title: PageTitle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTakePictureBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 戻るボタン
        binding.takePictureRreturnButton.setOnClickListener { finish() }

        // カメラタイプ
        binding.radioGroup2.setOnCheckedChangeListener { group, checkedId ->
            when( checkedId ) {
                R.id.preview ->
                    binding.upCamera.text = binding.preview.text
                R.id.fullPicture ->
                    binding.upCamera.text = binding.fullPicture.text
            }
        }

        // カメラ起動ボタン
        binding.upCamera.setOnClickListener {
            val cameraType = binding.radioGroup2.checkedRadioButtonId
            Log.d("value", "$cameraType")
            when (cameraType) {
                R.id.preview, -1 -> preview()
                R.id.fullPicture -> fullPicture()
            }
        }

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
            storagePermission()
        }

        // タイトル処理
        title = PageTitle()
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.takePictureTitle, title)
            commit()
        }

    }


    private fun storagePermission() {
        val permission = ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    REQUEST_EXTERNAL_STORAGE
            )
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            REQUEST_EXTERNAL_STORAGE -> {
                binding.upCamera.isEnabled = grantResults.isNotEmpty() &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED
            }
        }
    }


    // プレビュー
    private fun preview() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { intent ->
            intent.resolveActivity(packageManager).also {
                startActivityForResult(intent, REQUEST_PREVIEW)
            }
        }
    }

    // フルサイズ撮影
    private fun fullPicture() {

        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { intent ->
            intent.resolveActivity(packageManager).also {
                val time: String = SimpleDateFormat("yyyyMMdd_HHmmss")
                    .format(Date())
                val value = ContentValues().apply{
                    put(MediaStore.Images.Media.DISPLAY_NAME, "${time}_.jpg")
                    put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
                }
                val collection = MediaStore.Images.Media
                        .getContentUri("external")
                val photoUri = contentResolver.insert(collection, value)
                photoUri?.let {
                    currentPhotoUri = it
                }
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
                startActivityForResult(intent, REQUEST_PICTURE)
            }
        }

    }


    // カメラ撮影後処理
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_PREVIEW && resultCode == RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            binding.previewPicture.setImageBitmap(imageBitmap)
        } else if (requestCode == REQUEST_PICTURE) {
            when(resultCode) {
                RESULT_OK -> {

                    // 画像表示
                    binding.previewPicture.setImageURI(currentPhotoUri)

                    // 共有
                    Intent(Intent.ACTION_SEND).also { share ->
                        share.type = "image/*"
                        share.putExtra(Intent.EXTRA_STREAM, currentPhotoUri)
                        startActivity(Intent.createChooser(share, "Share to"))
                    }
                }
                else ->
                    contentResolver.delete(currentPhotoUri, null, null)
            }
        }
    }



    override fun onResume() {
        super.onResume()
        title.setTitle("CAMERA")
    }

}