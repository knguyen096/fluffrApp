package com.example.fluffrapp.adapters

import android.content.Context
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.fluffrapp.R
import com.example.fluffrapp.models.post
import com.example.fluffrapp.utils.glideLoader
import kotlinx.android.synthetic.main.post_list_layout.view.*

open class ProfilePostAdapter(
    private val context: Context,
    private var list: ArrayList<post>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.post_list_layout_user_p,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]

        if (holder is MyViewHolder) {
            glideLoader(context).loadPostPicture(model.postImage, holder.itemView.iv_post_image)
            holder.itemView.tv_post_location.text = "location: ${model.location}"
            holder.itemView.tv_post_content.text = model.content
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)
}