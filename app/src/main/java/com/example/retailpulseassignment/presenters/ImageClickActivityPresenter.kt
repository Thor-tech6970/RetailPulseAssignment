package com.example.retailpulseassignment.presenters

import android.content.Context
import android.graphics.Bitmap
import android.widget.Toast
import com.example.retailpulseassignment.sharedPref.SharedPref
import com.example.retailpulseassignment.interfaces.ImageClickActivityInterface
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class ImageClickActivityPresenter : ImageClickActivityInterface.Presenter {

    private var View : ImageClickActivityInterface.View
    private var Context : Context
    private var firebaseStorage: FirebaseStorage
    private var firebaseDatabase: FirebaseDatabase
    private var sharedPref: SharedPref

    constructor(view : ImageClickActivityInterface.View , context: Context){
        View = view
        Context = context
        sharedPref = SharedPref(Context)
        firebaseDatabase = FirebaseDatabase.getInstance()
        firebaseStorage = FirebaseStorage.getInstance()
    }

    override fun uploadImageOnFirebase(key : String, photoBitmap : Bitmap?) {

        View.showDialog()

        GlobalScope.launch {
            val user : String? = sharedPref.getUser()
            val byteArrayOutputStream : ByteArrayOutputStream =  ByteArrayOutputStream()
            photoBitmap?.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)

            if(user!=null){

                firebaseStorage.getReference().child(user).child(key).putBytes(byteArrayOutputStream.toByteArray()).addOnSuccessListener(
                    OnSuccessListener {

                        View.dismissDialog()

                        Toast.makeText(Context , "Image uploaded successfully" , Toast.LENGTH_SHORT).show()

                        val localTime : String = getCurrentTime()
                        val url = it.storage.downloadUrl.toString()
                        val map : MutableMap<String, String> = mutableMapOf()
                        map["URL"] = url
                        map["Time"] = localTime
                        firebaseDatabase.getReference().child("store_visits").child(user).child(key).push().setValue(map)


                    }).addOnFailureListener(OnFailureListener {

                    Toast.makeText(Context , it.message.toString() , Toast.LENGTH_SHORT).show()
                    View.dismissDialog()
                })

            }

        }

    }

    fun getCurrentTime() : String{
        val cal: Calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+5:30"))
        val currentLocalTime: Date = cal.getTime()
        val date: DateFormat = SimpleDateFormat("HH:mm a")
        date.setTimeZone(TimeZone.getTimeZone("GMT+5:30"))
        val localTime: String = date.format(currentLocalTime)
        return localTime
    }

    }
