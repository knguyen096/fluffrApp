package com.example.fluffrapp.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.fluffrapp.R
import com.example.fluffrapp.UserProfile
import com.example.fluffrapp.firestore.firestore
import com.example.fluffrapp.models.pet
import com.example.fluffrapp.models.post

import com.example.fluffrapp.utils.constants
import com.example.fluffrapp.utils.glideLoader
import kotlinx.android.synthetic.main.search_list_layout.view.*


open class SearchAdapter(
    private val context: Context,
    private var list: ArrayList<pet>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    // A global variable for OnClickListener interface.
    private var onClickListener: OnClickListener? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.search_list_layout,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]

        if (holder is MyViewHolder) {

            glideLoader(context).loadUserPicture(model.petImage, holder.itemView.iv_search_image)
            holder.itemView.tv_search_username.text = model.petName
            holder.itemView.tv_search_user_bio.text = model.petBio
        }

        holder.itemView.setOnClickListener {
            if (onClickListener != null) {
                onClickListener!!.onClick(position, model)
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }

    interface OnClickListener {

        fun onClick(position: Int, pets: pet)
    }

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)


}