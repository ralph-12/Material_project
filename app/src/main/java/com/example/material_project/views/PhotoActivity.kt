package com.example.material_project.views

import android.app.SearchManager
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.text.InputFilter
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.CompoundButton
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.material_project.R
import com.example.material_project.model.PhotoData
import com.example.material_project.model.SearchData
import com.example.material_project.recyclerview.PhotoGridRecyclerViewAdapter
import com.example.material_project.recyclerview.SearchHistRecyclerViewAdapter
import com.example.material_project.utils.Constants
import com.example.material_project.utils.Constants.TAG
import com.example.material_project.utils.SharedPref_Manager
import com.example.material_project.utils.toSimpleString
import kotlinx.android.synthetic.main.activity_photo.*
import java.util.*
import kotlin.collections.ArrayList

class PhotoActivity: AppCompatActivity(), SearchView.OnQueryTextListener, CompoundButton.OnCheckedChangeListener, View.OnClickListener {
    private var photoList = ArrayList<PhotoData>()
    private var searchHistoryList = ArrayList<SearchData>()
    private lateinit var photoGridRecyclerViewAdapter: PhotoGridRecyclerViewAdapter
    private lateinit var searchHistRecyclerViewAdapter: SearchHistRecyclerViewAdapter
    private lateinit var mSearchView: SearchView
    private lateinit var mSearchEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo)
        Log.d(Constants.TAG, "PhotoActivity onCreate")

        val bundle = intent.getBundleExtra("array_bundle")
        val searchTerm = intent.getStringExtra("search_term")
        topAppBar.title = searchTerm
        setSupportActionBar(topAppBar)

        photoList = bundle?.getSerializable("photo_array_list") as ArrayList<PhotoData>
        this.setPhotohRecyclerView(this.photoList)

        this.searchHistoryList = SharedPref_Manager.getSearchHistoryList() as ArrayList<SearchData>
        this.searchHistoryList.forEach{
            Log.d(TAG, "저장된 검색 기록 : ${it.term}, ${it.timestamp}")
        }
        this.setSearchRecyclerView(this.searchHistoryList)
    }

    private fun setPhotohRecyclerView(PhotoList: ArrayList<PhotoData>) {
        this.photoGridRecyclerViewAdapter = PhotoGridRecyclerViewAdapter()
        this.photoGridRecyclerViewAdapter.submitList(PhotoList)
        //역순 정렬
        val photoGridLayoutManager = GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false)

        rc_view.apply {
            layoutManager = photoGridLayoutManager
            adapter = photoGridRecyclerViewAdapter
        }
    }

    private fun setSearchRecyclerView(searchHistoryList: ArrayList<SearchData>) {
        this.searchHistRecyclerViewAdapter = SearchHistRecyclerViewAdapter()
        this.searchHistRecyclerViewAdapter.submitList(searchHistoryList)
        //역순 정렬
        val seachLinearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,true)
        seachLinearLayoutManager.stackFromEnd = true
        rc_search_view.apply {
            layoutManager = seachLinearLayoutManager
            this.scrollToPosition(searchHistRecyclerViewAdapter.itemCount - 1)
            adapter = searchHistRecyclerViewAdapter
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        Log.d(Constants.TAG, "onCreateOptionsMenu called")

        val inflater = menuInflater
        inflater.inflate(R.menu.top_app_bar_menu, menu)
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        this.mSearchView = menu?.findItem(R.id.search_menu)?.actionView as SearchView
        this.mSearchView.apply{
            this.queryHint = resources.getString(R.string.hint_helper)
            this.setOnQueryTextListener(this@PhotoActivity)
            this.setOnQueryTextFocusChangeListener{ _, hasExpanded ->
                when(hasExpanded) {
                    true -> {
                        Log.d(TAG, "onCreateOptionsMenu: $hasExpanded")
                        li_view.visibility = View.VISIBLE
                    }
                    false -> {
                        Log.d(TAG, "onCreateOptionsMenu: $hasExpanded")
                        li_view.visibility = View.INVISIBLE
                    }
                }
            }
            mSearchEditText = this.findViewById(androidx.appcompat.R.id.search_src_text)
        }
        this.mSearchEditText.apply {
            this.filters = arrayOf(InputFilter.LengthFilter(12))
            this.setTextColor(Color.parseColor("#ffffff"))
            this.setHintTextColor(Color.parseColor("#ffffff"))
        }
        this.sw_search.setOnCheckedChangeListener(this)
        this.btn_delete.setOnClickListener(this)

        return true
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        Log.d(TAG, "onQueryTextSubmit: query : ${query}")
        if(!query.isNullOrEmpty()) {
            this.topAppBar.title = query
            val newSearchData = SearchData(term = query, timestamp = Date().toSimpleString())
            this.searchHistoryList.add(newSearchData)

            SharedPref_Manager.storeSearchHistoryList(this.searchHistoryList)
        }

        this.topAppBar.collapseActionView()
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        Log.d(TAG, "onQueryTextChange: newText :${newText}")
        val userInputText = newText ?: ""
        if (userInputText.count() == 12){
            Toast.makeText(this, R.string.search_view_edittext_msg,Toast.LENGTH_SHORT).show()
        }

        return true
    }

    override fun onCheckedChanged(switch: CompoundButton?, isChecked: Boolean) {
        when(switch) {
            sw_search -> {
                if(isChecked) {
                    Log.d(TAG, "onCheckedChanged: $isChecked")
                } else {
                    Log.d(TAG, "onCheckedChanged: $isChecked")
                }
            }
        }

    }

    override fun onClick(view: View?) {
        when(view) {
            btn_delete -> {
                Log.d(TAG, "onClick: btn_delete")
            }
        }
    }

}