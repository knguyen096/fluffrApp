package com.example.fluffrapp.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.fluffrapp.*
import com.example.fluffrapp.adapters.ProfilePostAdapter
import com.example.fluffrapp.adapters.petAdapter
import com.example.fluffrapp.databinding.FragmentProfileBinding
import com.example.fluffrapp.firestore.firestore
import com.example.fluffrapp.models.pet
import com.example.fluffrapp.models.user
import com.example.fluffrapp.models.post
import com.example.fluffrapp.utils.constants
import com.example.fluffrapp.utils.glideLoader
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_user_profile_edit.*
import kotlinx.android.synthetic.main.fragment_profile.*


class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val mFirestore = FirebaseFirestore.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

    }

    fun successPetFirestore(petList: ArrayList<pet>) {
        if (petList.size > 0) {
            rv_petlist.visibility = View.VISIBLE
            tv_no_posts.visibility = View.GONE
            rv_petlist.layoutManager = GridLayoutManager(activity,3)
            rv_petlist.setHasFixedSize(true)

            val adapterPets = petAdapter(
                requireActivity(),
                petList
            )
            rv_petlist.adapter = adapterPets

        } else {
            rv_petlist.visibility = View.GONE
            tv_no_posts.visibility = View.VISIBLE
        }
    }

    private fun getPetFirestore() {
        firestore().getPetList(this)
    }

    fun successPostProfileListFirestore(postList: ArrayList<post>) {
        if (postList.size > 0) {
            rv_posts_items.visibility = View.VISIBLE
            tv_no_posts.visibility = View.GONE

            rv_posts_items.layoutManager = LinearLayoutManager(activity)
            rv_posts_items.setHasFixedSize(true)
            val adapterPosts = ProfilePostAdapter(
                requireActivity(),
                postList
            )

            rv_posts_items.adapter = adapterPosts
        } else {
            rv_posts_items.visibility = View.GONE
            tv_no_posts.visibility = View.VISIBLE
        }
    }

    override fun onResume() {
        super.onResume()
        getPostProfileListFirestore()
        getPetFirestore()
    }

    private fun getPostProfileListFirestore() {
        firestore().getPostProfileList(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)

        mFirestore.collection(constants.USERS)
            .document(firestore().getCurrentUserID()).get().addOnSuccessListener { document ->
                val user = document.toObject(user::class.java)

                if (user != null) {
                    binding.tvName.text = "${user.ownerFirstName} ${user.ownerLastName}"
                    binding.tvLocation.text = user.location
                    binding.tvBio.text = user.ownerBio

                    Glide.with(this@ProfileFragment)
                        .load(user.ownerImage)
                        .centerCrop()
                        .into(iv_user_photo)

                }


            }
            .addOnFailureListener { e ->

            }



        return binding.root


    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.dashboard_menu_settings, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        when (id) {
            R.id.action_add_pet -> {
                startActivity(Intent(activity, createPet::class.java))

                return true
            }
            R.id.action_owner -> {
                startActivity(Intent(activity, userProfileEdit::class.java))

                return true
            }

            R.id.action_logout -> {
                FirebaseAuth.getInstance().signOut()
                activity?.let {
                    val intent = Intent(it, login::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                }
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

}