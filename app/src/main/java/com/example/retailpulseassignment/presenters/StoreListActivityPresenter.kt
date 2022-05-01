package com.example.retailpulseassignment.presenters

import android.content.Context
import com.example.retailpulseassignment.sharedPref.SharedPref
import com.example.retailpulseassignment.interfaces.StoreListActivityInterface
import com.example.retailpulseassignment.models.StoreListResponse
import com.example.retailpulseassignment.models.Store
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.gson.Gson

class StoreListActivityPresenter : StoreListActivityInterface.Presenter {

    private var firebaseDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()
    private var allStoreList : ArrayList<Store> = arrayListOf()
    var userStoreList : ArrayList<Store> = arrayListOf()
    private var View : StoreListActivityInterface.View
    private var Context: Context
    private var sharedPref : SharedPref
    private val map : MutableMap<String, Store> = mutableMapOf()

    constructor(view: StoreListActivityInterface.View , context: Context){
        View = view
        Context = context
        sharedPref = SharedPref(Context)
    }

    override fun fetchAllStoreList() {

        View.showDialog()

        firebaseDatabase.getReference().child("stores").addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

                for(childSnapshot in snapshot.children){

                    val storeID = childSnapshot.key.toString()
                    val address = childSnapshot.child("address").value.toString()
                    val area = childSnapshot.child("area").value.toString()
                    val name = childSnapshot.child("name").value.toString()
                    val route = childSnapshot.child("route").value.toString()
                    val type = childSnapshot.child("type").value.toString()

                    val store : Store = Store(storeID, address , area , name , route , type)
                    allStoreList.add(store)
                }

                val finalList : ArrayList<Store> = allStoreList
                val allStoreListResponse : StoreListResponse = StoreListResponse(finalList)
                sharedPref.setAllStoresList(allStoreListResponse)
                makeMapOfUserStoreList()

            }

            override fun onCancelled(error: DatabaseError) {
                View.dismissDialog()
            }

        })

    }



    fun fetchUserStoreList() {

        View.showDialog()

        firebaseDatabase.getReference().child("users").addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

                for(childSnapshot in snapshot.children){

                    val name:String = childSnapshot.child("name").value.toString()

                    if(name.equals(sharedPref.getUser() , ignoreCase = true)){

                        val storeIDArrayList: ArrayList<String> = childSnapshot.child("stores").value as ArrayList<String>

                        for(storeID in storeIDArrayList){
                            val store: Store? = map[storeID]
                            if (store != null) {
                                userStoreList.add(store)
                            }
                        }


                        val finalList: ArrayList<Store> = userStoreList
                        val userStoreListResponse: StoreListResponse = StoreListResponse(finalList)
                        sharedPref.setUserStoresList(userStoreListResponse)

                        val userStoresList: ArrayList<Store> = userStoreListResponse.allStoreList
                        if(!userStoreList.isEmpty()) {
                            View.initialiseRecyclerView(userStoresList)
                        }
                    }

                }

            }

            override fun onCancelled(error: DatabaseError) {
                View.dismissDialog()
            }
        })
    }

    fun makeMapOfUserStoreList() {

        val AllStoreList : StoreListResponse = Gson().fromJson(sharedPref.getAllStoresList() , StoreListResponse::class.java)
        for(store in AllStoreList.allStoreList){
            map[store.storeID] = store
        }

        fetchUserStoreList()

    }
}