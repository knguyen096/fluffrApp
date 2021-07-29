package com.example.fluffrapp


import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.bumptech.glide.Glide

import com.example.fluffrapp.base.baseActivity
import com.example.fluffrapp.models.user
import com.example.fluffrapp.utils.constants

import com.google.firebase.firestore.FirebaseFirestore

import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_new_chat.*
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.user_row_newchat.view.*

class newChat : baseActivity() {
    private val mFirestore = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_chat)


        supportActionBar?.title = "Chat"
       fetchUsers()
    }

    private fun fetchUsers() {
        val adapter = GroupAdapter<ViewHolder>()

        mFirestore.collection(constants.USERS)
           .get().addOnSuccessListener { document ->
                val user = document.toObjects(user::class.java)
                user.forEach {
                    adapter.add(UserItem(it))
                }

                adapter.setOnItemClickListener{ item, view ->
                    val useritem = item as UserItem
                    val intent = Intent(this, chatLog::class.java)
                    intent.putExtra(constants.EXTRA_USER_DETAILS, "${useritem.user.ownerFirstName} ${useritem.user.ownerLastName}")
                    intent.putExtra(constants.EXTRA_USER_ID, useritem.user.user_id)
                    intent.putExtra(constants.EXTRA_USER_IMAGE, useritem.user.ownerImage)
                    startActivity(intent)
                    finish()
                }


                rv_newChat.adapter =adapter

            }
            .addOnFailureListener { e ->

            }


    }


}

class UserItem(val user: user): Item<ViewHolder>() {

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.tv_username.text = "${user.ownerFirstName} ${user.ownerLastName}"
        Glide.with(viewHolder.itemView.getContext())
            .load(user.ownerImage)
            .centerCrop()
            .into(viewHolder.itemView.iv_image)
    }

    override fun getLayout(): Int {
        return R.layout.user_row_newchat
    }

}
