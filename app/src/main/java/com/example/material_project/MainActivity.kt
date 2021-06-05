package com.example.material_project

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.material_project.utils.Constants
import com.example.material_project.utils.SEARCH_TYPE
import com.example.material_project.utils.onMyTextChanged
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_button_search.*

class MainActivity : AppCompatActivity() {
    private var current_Search: SEARCH_TYPE = SEARCH_TYPE.PHOTO

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.d(Constants.TAG, "MainActivity onCreate")

        rg_search.setOnCheckedChangeListener{ _, checkId ->
            when(checkId){
                R.id.rb_search1 -> {
                    Log.d(Constants.TAG, "photo search")
                    textField.hint = "사진 검색"
                    textField.startIconDrawable = resources.getDrawable(R.drawable.ic_baseline_insert_photo_24, resources.newTheme())
                    this.current_Search = SEARCH_TYPE.PHOTO
                }
                R.id.rb_search2 -> {
                    Log.d(Constants.TAG, "person search")
                    textField.hint = "사용자 검색"
                    textField.startIconDrawable = resources.getDrawable(R.drawable.ic_baseline_person_24, resources.newTheme())
                    this.current_Search = SEARCH_TYPE.USER
                }
            }
            Log.d(Constants.TAG, "MainActivity - setOnCheckedChangeListener, current_Search = $current_Search")
        }

        et_search.onMyTextChanged {
            if(it.toString().count() > 0) {
                fl_search.visibility = View.VISIBLE
                sc_view.scrollTo(0, 200)
                textField.helperText = " "

            } else {
                fl_search.visibility = View.INVISIBLE
                textField.helperText = resources.getString(R.string.hint_helper)
            }
            if (it.toString().count() == 12) {
                Log.e(Constants.TAG, "edit text error")
                Toast.makeText(this, "검색어는 12자 까지만 입력 가능합니다.", Toast.LENGTH_SHORT).show()
            }
        }

        btn_search.setOnClickListener {
            Log.d(Constants.TAG, "btn_search clicked current_Search :  $current_Search")

        }

    }

    private fun handleSearchButton() {
        btn_progress.visibility = View.VISIBLE
        btn_search.text = ""
        Handler().postDelayed({
            btn_progress.visibility = View.INVISIBLE
            btn_search.text = "검색"
        }, 1500)
    }


}