package com.example.retailpulseassignment.interfaces

import android.graphics.Bitmap

interface ImageClickActivityInterface {

    interface Presenter{
        fun uploadImageOnFirebase(key: String, photoBitmap : Bitmap?)
    }

    interface View{
        fun showDialog()
        fun dismissDialog()
        fun showEmptyImageToast()
    }

}