package com.example.retailpulseassignment.presenters

import com.example.retailpulseassignment.interfaces.MainActivityInterface

class MainActivityPresenter : MainActivityInterface.Presenter {

    var View : MainActivityInterface.View

    constructor(view: MainActivityInterface.View){
        View = view
    }

    override fun doLogin(password: String) {

        if(password.equals("retailpulse" , ignoreCase = true)){
            View.onSuccess("Login successful")
        }else{
            View.onFailure("Incorrect password")
        }
    }
}