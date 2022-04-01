package com.utf8coding.customviewdemo.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import com.utf8coding.customviewdemo.MyApplication
import com.utf8coding.customviewdemo.R
import com.utf8coding.customviewdemo.views.materialSearchView.MaterialSearchView
import java.util.ArrayList

class MaterialSearchViewFragment : Fragment() {
    private val mdSearchBar: MaterialSearchView?
        get() {
            return requireActivity().findViewById(R.id.mdSearchBar)
        }
    private val testList = ArrayList<String>()
    private val testList2 = ArrayList<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_material_search_view, container, false)
    }

    override fun onStart() {
        initTestList()
        //method to set title:
        mdSearchBar?.setTitle("mdSearchViewDemo")
        /*
        没有用到的 function：setMenuItemIcon，setOnSearchContentChangedListener:支持实时显示搜索结果，
        但需要考虑后端性能。（这里好像做不出来什么东西而且也没什么技术含量就没放到 Demo 里面）
         */
        //listeners:
        mdSearchBar?.setNavigationOnClickListener {
            Toast.makeText(MyApplication.context, "menuButton pressed.", Toast.LENGTH_SHORT).show()
        }
        mdSearchBar?.setOnSearchListener(object: MaterialSearchView.OnSearchListener{
            override fun onKeyboardSearch(searchText: String) {
                Log.i("MaterialSearchViewFragment:", "onSearch. SearchContent: $searchText")
                Toast.makeText(MyApplication.context, "onKeyBoardSearch. SearchContent: $searchText", Toast.LENGTH_SHORT).show()
                if (searchText != ""){
                    if(searchText == "114514"){
                        Log.i("MaterialSearchViewFragment:", "suggesting homo content.")
                        mdSearchBar?.setSearchSuggestion(testList2)
                    } else {
                        mdSearchBar?.setSearchSuggestion(testList)
                    }
                }
            }

            override fun onSearch(searchText: String) {
                Log.i("MaterialSearchViewFragment:", "onSearch")
                Toast.makeText(MyApplication.context, "onSearch. SearchContent: $searchText", Toast.LENGTH_SHORT).show()
                if (searchText != ""){
                    mdSearchBar?.setSearchSuggestion(testList)
                }
            }
        })
        mdSearchBar?.setOnSuggestionItemClickListener(object: MaterialSearchView.OnSuggestItemClickListener{
            override fun onClick(suggestion: String, position: Int) {
                Toast.makeText(activity as Context, "clicked on: $suggestion, position: ${position + 1}", Toast.LENGTH_SHORT).show()
            }

        })
        super.onStart()
    }

    private fun initTestList() {
        for (i in listOf("hello", "this", "is", "test", "content")){
            testList.add(i)
        }
        for (i in listOf("hello", "this", "is", "恶臭test", "content")){
            testList2.add(i)
        }
    }

}