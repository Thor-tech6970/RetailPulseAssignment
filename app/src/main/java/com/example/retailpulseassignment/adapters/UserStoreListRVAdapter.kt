package com.example.retailpulseassignment.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.retailpulseassignment.R
import com.example.retailpulseassignment.activities.ImageClickActivity
import com.example.retailpulseassignment.models.Store


class UserStoreListRVAdapter(context: Context , userStoreList: ArrayList<Store>) : RecyclerView.Adapter<UserStoreListRVAdapter.ViewHolder>() {

    private var userStoreList:ArrayList<Store> = userStoreList
    private var context : Context = context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.store_lists_item, parent, false)
        val holder: ViewHolder = ViewHolder(view)
        return holder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val store: Store = userStoreList[position]
        holder.tvAddress.text = "Address: "+ store.address
        holder.tvArea.text = "Area: " + store.area
        holder.tvName.text = "Name: " +  store.name
        holder.tvRoute.text = "Route: " + store.route
        holder.tvType.text =  "Type: " + store.type
        holder.itemView.setOnClickListener(View.OnClickListener {
            val intent : Intent = Intent(context , ImageClickActivity::class.java)
            intent.putExtra("Key" , store.storeID)
            context.startActivity(intent)
        })

    }

    override fun getItemCount(): Int {
        return userStoreList.size
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvAddress: TextView
        var tvArea: TextView
        var tvName :  TextView
        var tvRoute: TextView
        var tvType: TextView


        init {
            tvAddress = itemView.findViewById(R.id.tvAddress)
            tvArea = itemView.findViewById(R.id.tvArea)
            tvName = itemView.findViewById(R.id.tvName)
            tvRoute = itemView.findViewById(R.id.tvRoute)
            tvType = itemView.findViewById(R.id.tvType)
        }
    }

}