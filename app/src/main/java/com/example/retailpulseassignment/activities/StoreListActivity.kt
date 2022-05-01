package com.example.retailpulseassignment.activities

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextWatcher
import com.example.retailpulseassignment.sharedPref.SharedPref
import com.example.retailpulseassignment.databinding.ActivityStoreListBinding
import com.example.retailpulseassignment.interfaces.StoreListActivityInterface
import com.example.retailpulseassignment.models.Store
import com.example.retailpulseassignment.models.StoreListResponse
import com.example.retailpulseassignment.presenters.StoreListActivityPresenter
import com.google.gson.Gson
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.retailpulseassignment.adapters.UserStoreListRVAdapter
import android.text.Editable

class StoreListActivity : AppCompatActivity() , StoreListActivityInterface.View {

    private lateinit var binding : ActivityStoreListBinding ;
    private lateinit var presenter : StoreListActivityPresenter
    private lateinit var sharedPref: SharedPref
    private lateinit var dialog : ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoreListBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        sharedPref = SharedPref(this)
        dialog = ProgressDialog(this)
        presenter = StoreListActivityPresenter(this , this)
        presenter.fetchAllStoreList()

        binding.searchEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {

                val filteredList : ArrayList<Store> = arrayListOf()
                filteredList.clear();
                if (s.toString().isEmpty()) {
                    filteredList.addAll(presenter.userStoreList);
                } else {
                    for (store in presenter.userStoreList) {
                        if (store.name.toLowerCase().contains(s.toString().toLowerCase())) {
                            filteredList.add(store);
                        }
                    }
                }

                initialiseRecyclerView(filteredList)

            }
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }
        })

    }

    override fun showDialog() {
        dialog.setTitle("Please wait")
        dialog.setMessage("Fetching list...")
        dialog.show()
    }

    override fun dismissDialog() {
        dialog.dismiss()
    }

    override fun initialiseRecyclerView(userStoresList : ArrayList<Store>) {

        val adapter = UserStoreListRVAdapter(this , userStoresList)
        val linearLayoutManager = LinearLayoutManager(this)
        binding.rv.setLayoutManager(linearLayoutManager)
        binding.rv.setAdapter(adapter)
        dismissDialog()

    }
}