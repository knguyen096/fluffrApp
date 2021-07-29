package com.example.fluffrapp.firestore

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.util.Log

import com.example.fluffrapp.*
import com.example.fluffrapp.register.register
import com.example.fluffrapp.UserProfile
import com.example.fluffrapp.models.chat
import com.example.fluffrapp.models.pet
import com.example.fluffrapp.models.user
import com.example.fluffrapp.models.post
import com.example.fluffrapp.ui.profile.ProfileFragment
import com.example.fluffrapp.ui.home.PostsFragment
import com.example.fluffrapp.ui.search.SearchFragment
import com.example.fluffrapp.utils.constants

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class firestore {
    private val mFireStore = FirebaseFirestore.getInstance()

    fun registerUser(activity: register, userInfo: user) {
        mFireStore.collection(constants.USERS)
            .document(userInfo.user_id)
            .set(userInfo, SetOptions.merge())
            .addOnSuccessListener {
                activity.userRegistrationSuccess()
            }
            .addOnFailureListener { e ->
                Log.e(
                    activity.javaClass.simpleName, "Error while registering user", e
                )
            }
    }

    fun getCurrentUserID(): String {
        val currentUser = FirebaseAuth.getInstance().currentUser

        var currentUserID = ""
        if (currentUser != null) {
            currentUserID = currentUser.uid
        }
        return currentUserID
    }


    fun getCurrentUserDetails(activity: Activity) {
        mFireStore.collection(constants.USERS)
            .document(getCurrentUserID())
            .get()
            .addOnSuccessListener { document ->
                Log.i(activity.javaClass.simpleName, document.toString())

                val user = document.toObject(user::class.java)!!

                val sharedPreferences = activity.getSharedPreferences(
                    constants.FLUFPREFERENCES,
                    Context.MODE_PRIVATE
                )

                val editor: SharedPreferences.Editor = sharedPreferences.edit()
                editor.putString(
                    constants.LOGGED_IN_USERNAME,
                    "${user.ownerFirstName} ${user.ownerLastName}"
                )
                editor.apply()

                val editor2: SharedPreferences.Editor = sharedPreferences.edit()
                editor2.putString(
                    constants.USER_PROF_IMAGE, user.ownerImage
                )
                editor2.apply()

                when (activity) {
                    is login -> {
                        activity.userLoggedInSuccess(user)
                    }
                }
            }
            .addOnFailureListener { e ->
                Log.e(activity.javaClass.simpleName, "Error while registering user", e)

            }
    }

    fun getUserDetails(activity: UserProfile, userID: String) {
        mFireStore.collection(constants.USERS)
            .document(userID).get().addOnSuccessListener { document ->
                Log.e(activity.javaClass.simpleName, document.toString())
                val user = document.toObject(user::class.java)

                if (user != null) {
                    activity.userDetailSuccess(user)
                }

            }
            .addOnFailureListener { e ->
                Log.e(activity.javaClass.simpleName, "Error while getting profile")
            }
    }


    fun getPetDetails(activity: petProfile, userID: String) {
        mFireStore.collection(constants.PET)
            .document(userID).get().addOnSuccessListener { document ->
                Log.e(activity.javaClass.simpleName, document.toString())
                val user = document.toObject(pet::class.java)

                if (user != null) {
                    activity.petDetailSuccess(user)
                }

            }
            .addOnFailureListener { e ->
                Log.e(activity.javaClass.simpleName, "Error while getting profile")
            }
    }

    fun updateUserProfile(activity: Activity, userHashMap: HashMap<String, Any>) {
        mFireStore.collection(constants.USERS).document(getCurrentUserID())
            .update(userHashMap)
            .addOnSuccessListener {
                when (activity) {
                    is userProfileEdit -> {
                        activity.userProfileUpdateSuccess()
                    }
                }
            }
            .addOnFailureListener { e ->
                Log.e(activity.javaClass.simpleName, "Error while updating user profile", e)
            }
    }

    fun uploadImageToCloud(activity: Activity, imagefileURI: Uri?, imagetype: String) {
        val sRef: StorageReference = FirebaseStorage.getInstance().reference.child(
            imagetype + System.currentTimeMillis() + "." +
                    constants.getFileExt(activity, imagefileURI)
        )
        sRef.putFile(imagefileURI!!).addOnSuccessListener { taskSnapshot ->
            Log.e("Firebase Image URL", taskSnapshot.metadata!!.reference!!.downloadUrl.toString())
            taskSnapshot.metadata!!.reference!!.downloadUrl.addOnSuccessListener { uri ->
                Log.e(
                    "Downloadable image URL",
                    uri.toString()
                )

                when (activity) {
                    is userProfileEdit -> {
                        activity.imageUploadSuccess(uri.toString())
                    }
                    is createPet -> {
                        activity.imageUploadSuccess(uri.toString())
                    }
                    is createPost -> {
                        activity.imageUploadSuccess(uri.toString())
                    }

                }
            }
        }
            .addOnFailureListener { exception ->
                Log.e(
                    activity.javaClass.simpleName,
                    exception.message,
                    exception
                )
            }
    }

    fun uploadPostDetails(activity: createPost, postInfo: post) {
        mFireStore.collection(constants.POST)
            .document()
            .set(postInfo, SetOptions.merge())
            .addOnSuccessListener {
                activity.postUploadSuccess()
            }
            .addOnFailureListener { e ->
                Log.e(activity.javaClass.simpleName, "Error uploading post", e)

            }
    }

    fun uploadPetDetails(activity: createPet, petInfo: pet) {
        mFireStore.collection(constants.PET)
            .document()
            .set(petInfo, SetOptions.merge())
            .addOnSuccessListener {
                activity.petUploadSuccess()
            }
            .addOnFailureListener { e ->
                Log.e(activity.javaClass.simpleName, "Error saving pet profile", e)

            }
    }

    fun uploadChatMessages(activity: chatLog, messageInfo: chat) {
        mFireStore.collection("messages/${messageInfo.receivedUserid}/${messageInfo.sendUserid}")
            .document()
            .set(messageInfo, SetOptions.merge())
            .addOnSuccessListener {
                activity.messageUploadSuccess()
            }
            .addOnFailureListener { e ->
                Log.e(activity.javaClass.simpleName, "Error uploading messages", e)

            }

        mFireStore.collection("messages/${messageInfo.sendUserid}/${messageInfo.receivedUserid}")
            .document()
            .set(messageInfo, SetOptions.merge())
            .addOnSuccessListener {
                activity.messageUploadSuccess()
            }
            .addOnFailureListener { e ->
                Log.e(activity.javaClass.simpleName, "Error uploading messages", e)

            }
    }

    fun getPostHomeList(fragment: PostsFragment) {
        mFireStore.collection(constants.POST)
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { document ->
                Log.e("Posts", document.documents.toString())
                val postLists: ArrayList<post> = ArrayList()
                for (i in document.documents) {
                    val post = i.toObject(post::class.java)
                    post!!.post_id = i.id
                    postLists.add(post)
                }

                fragment.successPostHomeListFirestore(postLists)
            }
    }

    fun getPostProfileList(fragment: ProfileFragment) {

        mFireStore.collection(constants.POST).whereEqualTo(constants.USER_ID, getCurrentUserID())
            .get()
            .addOnSuccessListener { document ->
                Log.e("Posts", document.documents.toString())
                val postLists: ArrayList<post> = ArrayList()
                for (i in document.documents) {
                    val post = i.toObject(post::class.java)
                    post!!.post_id = i.id
                    postLists.add(post)
                }
                fragment.successPostProfileListFirestore(postLists)

            }
    }

    fun getPostProfileList2(activity: UserProfile) {

        mFireStore.collection(constants.POST).whereEqualTo(constants.EXTRA_USER_ID, FirebaseAuth.getInstance().uid)
            .get()
            .addOnSuccessListener { document ->
                Log.e("Posts", document.documents.toString())
                val postLists: ArrayList<post> = ArrayList()
                for (i in document.documents) {
                    val post = i.toObject(post::class.java)
                    post!!.post_id = i.id
                    postLists.add(post)
                }
                activity.successPostProfileListFirestore2(postLists)

            }
    }

    fun getPetList(fragment: ProfileFragment) {

        mFireStore.collection(constants.PET).whereEqualTo(constants.USER_ID, getCurrentUserID())
          //  .orderBy(constants.PET_NAME, Query.Direction.ASCENDING)
            .get()
            .addOnSuccessListener { document ->
                Log.e("Pets", document.documents.toString())
                val petList: ArrayList<pet> = ArrayList()
                for (i in document.documents) {
                    val post = i.toObject(pet::class.java)
                    post!!.pet_id = i.id
                    petList.add(post)
                }
                fragment.successPetFirestore(petList)

            }
    } fun getPetList2(activity: UserProfile) {
        mFireStore.collection(constants.PET).whereEqualTo(constants.EXTRA_USER_ID, FirebaseAuth.getInstance().uid)
            //  .orderBy(constants.PET_NAME, Query.Direction.ASCENDING)
            .get()
            .addOnSuccessListener { document ->
                Log.e("Pets", document.documents.toString())
                val petList: ArrayList<pet> = ArrayList()
                for (i in document.documents) {
                    val post = i.toObject(pet::class.java)
                    post!!.pet_id = i.id
                    petList.add(post)
                }
                activity.successPetFirestore(petList)

            }
    }


    fun getSearchList(fragment: SearchFragment) {
        mFireStore.collection(constants.PET)
            .get()
            .addOnSuccessListener { document ->
                Log.e("Search Users", document.documents.toString())
                val userLists: ArrayList<pet> = ArrayList()
                for (i in document.documents) {
                    val post = i.toObject(pet::class.java)
                    post!!.user_id = i.id
                    userLists.add(post)
                }
                fragment.successSearchFirestore(userLists)
            }
    }

}


