package com.example.retailpulseassignment.sharedPref

import android.content.Context
import android.content.SharedPreferences
import com.example.retailpulseassignment.models.StoreListResponse
import com.google.gson.Gson

open class SharedPref {

    var context: Context? = null

    constructor(Context: Context?)  {
        context = Context
    }

     private fun getSharedPrefrence(): SharedPreferences? {
        return context?.getSharedPreferences(
            "com.example.retailpulseassignment", Context.MODE_PRIVATE
        )
    }

    fun getUser(): String? {
        return getSharedPrefrence()?.getString("User", "")
    }

    fun setUser(user: String?) {
        getSharedPrefrence()?.edit()?.putString("User", user)?.apply()
    }

    fun setAllStoresList(allStoreListResponse: StoreListResponse){
        getSharedPrefrence()?.edit()?.putString("ALL_STORES_LIST_RESPONSE", Gson().toJson(allStoreListResponse))?.apply()
    }

    fun getAllStoresList() : String?{
        return getSharedPrefrence()?.getString("ALL_STORES_LIST_RESPONSE" , "{}")
    }

    fun setUserStoresList(userStoreListResponse: StoreListResponse){
        getSharedPrefrence()?.edit()?.putString("USER_STORES_LIST_RESPONSE", Gson().toJson(userStoreListResponse))?.apply()
    }

    fun getUserStoresList() : String?{
        return getSharedPrefrence()?.getString("USER_STORES_LIST_RESPONSE" , "{}")
    }

}