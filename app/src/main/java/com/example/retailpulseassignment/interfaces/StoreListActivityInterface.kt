package com.example.retailpulseassignment.interfaces

import com.example.retailpulseassignment.models.Store

interface StoreListActivityInterface {

    interface Presenter{
        fun fetchAllStoreList()
    }

    interface View{
        fun showDialog()
        fun dismissDialog()
        fun initialiseRecyclerView(userStoresList : ArrayList<Store>)
    }

}

