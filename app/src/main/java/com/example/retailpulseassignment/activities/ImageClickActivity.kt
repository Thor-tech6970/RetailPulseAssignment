package com.example.retailpulseassignment.activities

import android.app.Activity
import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.retailpulseassignment.R
import com.example.retailpulseassignment.databinding.ActivityImageClickBinding
import androidx.core.app.ActivityCompat.startActivityForResult

import android.content.Intent

import android.provider.MediaStore

import android.content.DialogInterface
import androidx.appcompat.app.AlertDialog
import java.io.IOException

import android.net.Uri

import android.graphics.Bitmap
import android.widget.Toast
import com.example.retailpulseassignment.interfaces.ImageClickActivityInterface
import com.example.retailpulseassignment.presenters.ImageClickActivityPresenter

class ImageClickActivity : AppCompatActivity() , ImageClickActivityInterface.View {

    private lateinit var binding : ActivityImageClickBinding
    private lateinit var presenter : ImageClickActivityPresenter
    private var photoBitmap : Bitmap? = null
    private var photoUri : Uri? = null
    private lateinit var key : String
    private lateinit var dialog : ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityImageClickBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        key = intent.getStringExtra("Key").toString()
        presenter = ImageClickActivityPresenter(this , this)
        dialog = ProgressDialog(this)

        binding.btnImageClick.setOnClickListener(View.OnClickListener {

            val options = arrayOf<CharSequence>("Take Photo", "Choose from Gallery", "Cancel")
            val builder: AlertDialog.Builder = AlertDialog.Builder(this)
            builder.setTitle("Add Photo")
            builder.setItems(options,
                DialogInterface.OnClickListener { dialog, item ->
                    if (options[item] == "Take Photo") {
                        val takePicture = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                        startActivityForResult(takePicture, 4)
                    } else if (options[item] == "Choose from Gallery") {
                        val pickPhoto = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                        startActivityForResult(pickPhoto, 5)
                    } else if (options[item] == "Cancel") {
                        dialog.dismiss()
                    }
                })
            builder.show()

        })

        binding.btnImageUpload.setOnClickListener(View.OnClickListener {

            if(photoBitmap==null){
                showEmptyImageToast()
            }else {
                presenter.uploadImageOnFirebase(key, photoBitmap)
            }
        })

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, imageReturnedIntent: Intent?) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent)
        if (resultCode != RESULT_CANCELED) {
            when (requestCode) {
                4 -> if (resultCode == RESULT_OK && imageReturnedIntent != null) {
                    val extras = imageReturnedIntent.extras
                    photoBitmap = extras!!["data"] as Bitmap?
                    binding.iv.setImageURI(photoUri)
                    binding.iv.setImageBitmap(photoBitmap)
                }
                5 -> if (resultCode == RESULT_OK && imageReturnedIntent != null) {
                    val selectedImage = imageReturnedIntent.data
                    try {
                        photoBitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, selectedImage)
                        binding.iv.setImageBitmap(photoBitmap)
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }
        }
    }

    override fun showDialog() {
        dialog.setTitle("Please wait")
        dialog.setMessage("Uploading image...")
        dialog.show()
    }

    override fun dismissDialog() {
        dialog.dismiss()
    }

    override fun showEmptyImageToast() {
        Toast.makeText(this , "Please add an image first",Toast.LENGTH_SHORT).show()
    }


}