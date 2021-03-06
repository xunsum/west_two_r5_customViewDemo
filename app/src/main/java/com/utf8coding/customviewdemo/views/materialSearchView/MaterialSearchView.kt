package com.utf8coding.customviewdemo.views.materialSearchView

import android.animation.Animator
import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Parcelable
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.MaterialToolbar
import com.utf8coding.customviewdemo.R


class MaterialSearchView (context: Context, attrs: AttributeSet) : ConstraintLayout(context, attrs) {
    private val fadeInAnimation: Animation
        get(){
            val mAlphaAnimation = AlphaAnimation(0f, 1f)
            mAlphaAnimation.duration = 200
            return mAlphaAnimation
        }
    private val fadeOutAnimation: Animation
        get(){
            val mAlphaAnimation = AlphaAnimation(1f, 0f)
            mAlphaAnimation.duration = 200
            return mAlphaAnimation
        }
    private val doSearchButton: ImageButton
        get() {
            val button = findViewById<ImageButton>(R.id.doSearchButton)
            return button
        }
    private val searchEditText: EditText
        get() {
            val editText = findViewById<EditText>(R.id.searchEditText)
            return editText
        }
    private val appBar: AppBarLayout
        get() {
            return findViewById(R.id.appBar)
        }
    private val mdToolBar: MaterialToolbar
        get() {
            return findViewById(R.id.mdToolBar)
        }
    private val closeSearchButton: ImageButton
        get() {
            val button = findViewById<ImageButton>(R.id.closeSearchButton)
            return button
        }
    private val suggestRecyclerView: RecyclerView
        get() {
            return findViewById(R.id.suggestionRecyclerView)
        }
    private val clearButton: ImageButton
        get() {
            val button = findViewById<ImageButton>(R.id.clearButton)
            return button
        }
    private val suggestionRecyclerViewAdapter: SuggestionRecyclerViewAdapter
        get(){
            return SuggestionRecyclerViewAdapter(suggestList, this, object:
                SuggestionRecyclerViewAdapter.OnSuggestionClickListener {
                override fun onClick(suggestion: String, position: Int) {
                    onSuggestionClick(suggestion, position)
                }
            })
        }
    private lateinit var suggestList: ArrayList<String>
    private var mOnSuggestItemClickListener: OnSuggestItemClickListener = object:
        OnSuggestItemClickListener {
        override fun onClick(suggestion: String, position: Int) {
        }
    }
    private lateinit var title: String


    init{
        LayoutInflater.from(context).inflate(R.layout.view_md_search, this)

        mdToolBar.setOnMenuItemClickListener{
            when(it.itemId) {
                R.id.action_search -> {
                    transmitToSearch()
                    true
                }
                else -> false
            }
        }

        closeSearchButton.setOnClickListener{
            transmitToTitle()
        }

        clearButton.setOnClickListener{
            searchEditText.setText("")
        }

        searchEditText.addTextChangedListener {
            onTextChanged()
        }

        clearButton.visibility = INVISIBLE
    }

    //?????????????????????????????????????????????
    override fun onSaveInstanceState(): Parcelable {
        super.onSaveInstanceState()
        val bundle = Bundle()
        bundle.putParcelable("instance", super.onSaveInstanceState())
        bundle.putString("title", title)
        return bundle
    }
    override fun onRestoreInstanceState(state: Parcelable?) {
        if (state is Bundle) {
            val bundle = state
            super.onRestoreInstanceState(state.getParcelable("instance"))
            setTitle(bundle.getString("title", ""))
        } else {
            super.onRestoreInstanceState(state)
        }
    }

    //?????????????????????????????????????????? RecyclerView
    private fun onTextChanged() {
        if (searchEditText.text.toString() != ""){
            //?????????????????????????????????recyclerView ??????
            if (suggestRecyclerView.visibility != VISIBLE){
                clearButton.visibility = VISIBLE
                val mAlphaAnimation = fadeInAnimation
                suggestRecyclerView.animation = mAlphaAnimation
                mAlphaAnimation.startNow()
                suggestRecyclerView.visibility = VISIBLE
            }
        } else {
            //?????????????????????????????????recyclerView ?????????
            clearButton.visibility = INVISIBLE
            suggestRecyclerView.animate().alpha(0f)
                .setListener(object: Animator.AnimatorListener{
                    override fun onAnimationStart(p0: Animator?) {
                    }
                    override fun onAnimationEnd(p0: Animator?) {
                        suggestRecyclerView.visibility = INVISIBLE
                    }
                    override fun onAnimationCancel(p0: Animator?) {
                    }
                    override fun onAnimationRepeat(p0: Animator?) {
                    }
                })
                .start()
        }
    }

    //???????????????
    private fun transmitToSearch(){
        //??????????????????
        searchEditText.isFocusable = true
        searchEditText.isFocusableInTouchMode = true

        //??????????????????
        val mAlphaAnimation = fadeOutAnimation
        appBar.animation = mAlphaAnimation
        mAlphaAnimation.startNow()
        appBar.visibility = View.GONE
    }

    //??????????????????????????????
    private fun transmitToTitle(){
        //??????????????????
        setSearchSuggestion(ArrayList())

        //???????????????????????????????????????????????????
        searchEditText.setText("")
        searchEditText.isFocusable = false
        searchEditText.isFocusableInTouchMode = false
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(searchEditText.windowToken, 0)

        //??????????????????
        val mAlphaAnimation = fadeInAnimation
        appBar.animation = mAlphaAnimation
        mAlphaAnimation.startNow()
        appBar.visibility = View.VISIBLE

        //???????????????????????????????????????
        if (suggestRecyclerView.visibility != INVISIBLE){
            suggestRecyclerView.visibility = INVISIBLE
        }
    }

    //?????????????????????????????????????????????????????????????????????
    interface OnSearchListener{
        fun onKeyboardSearch(searchText: String)
        fun onSearch(searchText: String)

    }
    fun setOnSearchListener(onSearchListener: OnSearchListener){
        searchEditText.setOnEditorActionListener { textView, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                onSearchListener.onKeyboardSearch(textView.text.toString())
                true
            } else false
        }
        doSearchButton.setOnClickListener{
            onSearchListener.onSearch(searchEditText.text.toString())
        }
    }

    //????????????????????????
    fun setTitle(title: String){
        Log.i("MaterialSearchView:", "setTitle")
        mdToolBar.title = title
        this.title = title
    }

    //?????? navigation ????????? -> ????????????
    fun setNavigationIcon(drawable: Drawable){
        mdToolBar.navigationIcon = drawable
    }

    //?????? navigation ???????????????ID??????????????? -> ????????????
    fun setNavigationIcon(resourceId: Int){
        mdToolBar.setNavigationIcon(resourceId)
    }

    //navigation ????????????????????????
    fun setNavigationOnClickListener(onClickListener: OnClickListener){
        mdToolBar.setNavigationOnClickListener(onClickListener)
    }

    //??????????????????????????????????????????????????????????????????????????????
    fun setSearchSuggestion(inputList: ArrayList<String>) {
        suggestList = inputList
        if (suggestList.size != 0){
            Log.v("MaterialSearchView", "showing list: $suggestList")
            suggestRecyclerView.layoutManager = LinearLayoutManager(context)
            suggestRecyclerView.adapter = suggestionRecyclerViewAdapter
            suggestRecyclerView.animate().alpha(1f)
                .setListener(object: Animator.AnimatorListener{
                    override fun onAnimationStart(p0: Animator?) {
                        suggestRecyclerView.visibility = VISIBLE
                    }
                    override fun onAnimationEnd(p0: Animator?) {
                    }
                    override fun onAnimationCancel(p0: Animator?) {
                    }
                    override fun onAnimationRepeat(p0: Animator?) {
                    }

                })
                .start()
        } else {
            suggestList = ArrayList()
            suggestRecyclerView.animate().alpha(0f)
                .setListener(object: Animator.AnimatorListener{
                    override fun onAnimationStart(p0: Animator?) {
                    }
                    override fun onAnimationEnd(p0: Animator?) {
                        suggestRecyclerView.visibility = INVISIBLE
                    }
                    override fun onAnimationCancel(p0: Animator?) {
                    }
                        override fun onAnimationRepeat(p0: Animator?) {
                    }
                })
                .start()
        }
    }

    /*
    ???????????????????????????????????????????????????????????????????????????????????? edittext
     */
    fun fillInSuggestion(suggestion: String){
        searchEditText.setText(suggestion)
        searchEditText.setSelection(suggestion.length)
    }

    /*
    ???????????? listener ??????????????????????????????????????????List???????????????????????????????????????????????????????????? list ????????????????????????
    ?????????????????????????????????????????????????????????
     */
    private fun onSuggestionClick(suggestion: String, position: Int){
        mOnSuggestItemClickListener.onClick(suggestion, position)
    }
    interface OnSuggestItemClickListener{
        fun onClick(suggestion: String, position: Int)
    }
    fun setOnSuggestionItemClickListener(listener: OnSuggestItemClickListener){
        mOnSuggestItemClickListener = listener
    }

    /*
    ???????????????????????????????????????????????? listener ??????????????????????????????
     */
    interface OnSearchContentChangedListener{
        fun onChange(textNow: String)
    }
    fun setOnSearchContentChangedListener(listener: OnSearchContentChangedListener){
        searchEditText.addTextChangedListener {
            listener.onChange(searchEditText.text.toString())
        }
    }
}