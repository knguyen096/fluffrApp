package com.example.fluffrapp.ui.search

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fluffrapp.*
import com.example.fluffrapp.adapters.SearchAdapter
import com.example.fluffrapp.firestore.firestore
import com.example.fluffrapp.models.pet
import com.example.fluffrapp.models.user
import kotlinx.android.synthetic.main.fragment_search.*


class SearchFragment : Fragment() {

    private lateinit var homeViewModel: SearchViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    fun successSearchFirestore(searchList: ArrayList<pet>) {
        if(searchList.size > 0){
            rv_search_posts.visibility = View.VISIBLE
            tv_no_search_found.visibility = View.GONE

            rv_search_posts.layoutManager = LinearLayoutManager(activity)
            rv_search_posts.setHasFixedSize(true)

            val adapterPosts = SearchAdapter(
                requireActivity(),
                searchList
            )
            rv_search_posts.adapter = adapterPosts

        } else {
            rv_search_posts.visibility = View.GONE
            tv_no_search_found.visibility = View.VISIBLE
        }
    }

    private fun getSearchFirestore() {
        firestore().getSearchList(this)
    }

    override fun onResume() {
        super.onResume()
        getSearchFirestore()
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProvider(this).get(SearchViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_search, container, false)

        return root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.dashboard_menu_chat, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        when(id) {
            R.id.action_chat -> {
                startActivity(Intent(activity, newChat::class.java))
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}