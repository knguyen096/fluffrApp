package com.example.fluffrapp.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.fluffrapp.R
import com.example.fluffrapp.UserProfile
import com.example.fluffrapp.models.pet
import com.example.fluffrapp.petProfile

import com.example.fluffrapp.utils.constants
import com.example.fluffrapp.utils.glideLoader
import kotlinx.android.synthetic.main.profile_pet_row.view.*


open class petAdapter(
    private val context: Context,
    private var list: ArrayList<pet>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.profile_pet_row,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]

        if (holder is MyViewHolder) {
            glideLoader(context).loadUserPicture(model.petImage, holder.itemView.iv_post_image)

        }

        holder.itemView.setOnClickListener{
            val intent = Intent(context, petProfile::class.java)
            intent.putExtra(constants.EXTRA_PET_ID, model.pet_id)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)


}