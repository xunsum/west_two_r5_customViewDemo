package com.utf8coding.customviewdemo.views.materialSearchView

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.utf8coding.customviewdemo.R

//就是一个展示文字列表的Adapter
open class SuggestionRecyclerViewAdapter(private val suggestList: List<String>,
                                         private val view: MaterialSearchView,
                                         private val onSuggestionClickListener: OnSuggestionClickListener
): RecyclerView.Adapter<SuggestionRecyclerViewAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val suggestionTextView: TextView = view.findViewById(R.id.suggestionText)
        val wholeView: View = view.findViewById(R.id.suggestionRecyclerViewItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_md_search_suggestion, parent, false)
        return ViewHolder(view)
    }

    interface OnSuggestionClickListener{
        fun onClick(suggestion: String, position: Int)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val suggestionText = suggestList[position]
        holder.suggestionTextView.text = suggestionText
        holder.wholeView.setOnClickListener {
            view.fillInSuggestion(suggestionText)
            onSuggestionClickListener.onClick(suggestionText, position)
        }
    }

    override fun getItemCount() = suggestList.size
}