package com.example.fluffrapp.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.fluffrapp.R
import com.example.fluffrapp.UserProfile
import com.example.fluffrapp.models.post
import com.example.fluffrapp.newChat
import com.example.fluffrapp.ui.profile.ProfileFragment
import com.example.fluffrapp.utils.constants
import com.example.fluffrapp.utils.glideLoader
import kotlinx.android.synthetic.main.post_list_layout.view.*


open class PostAdapter(
    private val context: Context,
    private var list: ArrayList<post>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var onClickListener: OnClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.post_list_layout,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]
        if (holder is MyViewHolder) {

            glideLoader(context).loadUserPicture(
                model.ownerImage,
                holder.itemView.iv_userPhoto
            )
            glideLoader(context).loadPostPicture(
                model.postImage,
                holder.itemView.iv_post_image
            )
            holder.itemView.tv_post_user.text = model.user_name
            holder.itemView.tv_post_location.text = "location: ${model.location}"
            holder.itemView.tv_post_content.text = model.content

            holder.itemView.setOnClickListener {

            /*    context.getSharedPreferences(constants.FLUFPREFERENCES, Context.MODE_PRIVATE)
                    .edit().putString(constants.EXTRA_USER_ID, model.user_id).apply()

                (context as AppCompatActivity).supportFragmentManager.beginTransaction()
                    .replace(R.id.container1, ProfileFragment()).commit()*/
                val intent = Intent(context, UserProfile::class.java)
                intent.putExtra(constants.EXTRA_USER_ID, model.user_id)
                context.startActivity(intent)
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

        fun onClick(position: Int, posts: post)
    }

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)
}
