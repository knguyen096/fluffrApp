package com.example.fluffrapp.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fluffrapp.*
import com.example.fluffrapp.adapters.PostAdapter
import com.example.fluffrapp.firestore.firestore
import com.example.fluffrapp.models.post
import com.example.fluffrapp.utils.constants
import kotlinx.android.synthetic.main.fragment_home.*


class PostsFragment : Fragment() {

    private lateinit var homeViewModel: PostsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    fun successPostHomeListFirestore(postList: ArrayList<post>) {
       if(postList.size > 0){
           rv_posts_items.visibility = View.VISIBLE
           tv_no_posts.visibility = View.GONE

           rv_posts_items.layoutManager = LinearLayoutManager(activity)
           rv_posts_items.setHasFixedSize(true)

           val adapterPosts = PostAdapter(
               requireActivity(),
               postList)
           rv_posts_items.adapter = adapterPosts

           adapterPosts.setOnClickListener(object :
               PostAdapter.OnClickListener {
               override fun onClick(position: Int, posts: post) {

                   val intent = Intent(context, UserProfile::class.java)
                   intent.putExtra(constants.EXTRA_USER_POST_ID, posts.post_id)
                   intent.putExtra(constants.EXTRA_USER_ID, posts.user_id)
                   startActivity(intent)
               }
           })
       } else {
           rv_posts_items.visibility = View.GONE
           tv_no_posts.visibility = View.VISIBLE
       }
    }

    private fun getPostListFirestore() {
        firestore().getPostHomeList(this)
    }

    override fun onResume() {
        super.onResume()
        getPostListFirestore()
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProvider(this).get(PostsViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)

        return root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.dashboard_menu_add, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        when(id) {
            R.id.action_add -> {
                startActivity(Intent(activity, createPost::class.java))
                return true
            }

            R.id.action_chat -> {
                startActivity(Intent(activity, newChat::class.java))
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}