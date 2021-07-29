package com.example.fluffrapp

import android.content.Context
import android.os.Bundle
import android.widget.Toast

import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.fluffrapp.firestore.firestore
import com.example.fluffrapp.models.chat
import com.example.fluffrapp.models.user
import com.example.fluffrapp.utils.constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_chat_log.*
import kotlinx.android.synthetic.main.chat_from.view.*
import kotlinx.android.synthetic.main.chat_to.view.*
import kotlinx.android.synthetic.main.fragment_profile.*


class chatLog : AppCompatActivity() {

    private val mFirestore = FirebaseFirestore.getInstance()
    val adapter = GroupAdapter<ViewHolder>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_log)

        rv_chatLog.adapter = adapter
        val username = intent.getStringExtra(constants.EXTRA_USER_DETAILS)
        supportActionBar?.title = username

        btn_sendMessage.setOnClickListener {
            uploadMessage()
        }

        listenForMessages()

    }

    private fun listenForMessages() {
        val receivedUserid = intent.getStringExtra(constants.EXTRA_USER_ID)

        val sendUserid = firestore().getCurrentUserID()
        mFirestore.collection("messages/${receivedUserid}/${sendUserid}")
            .orderBy("timestamp", Query.Direction.ASCENDING)
            .get()
            .addOnSuccessListener { document ->
                //  val chatMessage = document.toObjects(chatMessage::class.java)!!
                for (i in document.documents) {
                    val chatMessage = i.toObject(chat::class.java)
                    chatMessage!!.message_id = i.id
                    if (chatMessage.receivedUserid == FirebaseAuth.getInstance().uid) {
                        adapter.add(chatItemFrom(chatMessage.message))
                    } else {
                        adapter.add((chatItemTo(chatMessage.message)))
                    }

                }
            }
    }

    fun messageUploadSuccess() {
        Toast.makeText(
            this@chatLog,
            resources.getString(R.string.message_upload_success),
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun uploadMessage() {

        val sendUserImage =
            this.getSharedPreferences(constants.FLUFPREFERENCES, Context.MODE_PRIVATE)
                .getString(constants.USER_PROF_IMAGE, "")!!

        val receivedUserImage = intent.getStringExtra(constants.EXTRA_USER_IMAGE)
        val receivedUserId = intent.getStringExtra(constants.EXTRA_USER_ID)

        val message = chat(
            chat().message_id,
            firestore().getCurrentUserID(),
            sendUserImage,
            et_sendMessage.text.toString().trim { it <= ' ' },
            receivedUserId!!,
            receivedUserImage!!,
            System.currentTimeMillis() / 1000
        )
        firestore().uploadChatMessages(this, message)
        et_sendMessage.text.clear()
        rv_chatLog.scrollToPosition(adapter.itemCount - 1)
    }


}

class chatItemFrom(val text: String) : Item<ViewHolder>() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.tv_chatfrom.text = text

    }

    override fun getLayout(): Int {
        return R.layout.chat_from

    }

}

class chatItemTo(
    val text: String) : Item<ViewHolder>() {

    override fun bind(viewHolder: ViewHolder, position: Int) {
        val context: Context
        viewHolder.itemView.tv_chatto.text = text

    }

    override fun getLayout(): Int {
        return R.layout.chat_to

    }

}
