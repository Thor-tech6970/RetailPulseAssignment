package com.example.retailpulseassignment.interfaces

interface MainActivityInterface {

    interface View {
        fun onSuccess(message : String)
        fun onFailure(message : String)
        fun blank(message: String)
    }

    interface Presenter{
        fun doLogin(password : String)
    }


}