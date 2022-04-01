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

    //生命周期：保存标题、不然会消失
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

    //应用内监听字符改变，动态显示 RecyclerView
    private fun onTextChanged() {
        if (searchEditText.text.toString() != ""){
            //不为空，清理按钮可见，recyclerView 可见
            if (suggestRecyclerView.visibility != VISIBLE){
                clearButton.visibility = VISIBLE
                val mAlphaAnimation = fadeInAnimation
                suggestRecyclerView.animation = mAlphaAnimation
                mAlphaAnimation.startNow()
                suggestRecyclerView.visibility = VISIBLE
            }
        } else {
            //为空，清理按钮不可见，recyclerView 不可见
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

    //显示搜索栏
    private fun transmitToSearch(){
        //聚焦搜索栏：
        searchEditText.isFocusable = true
        searchEditText.isFocusableInTouchMode = true

        //隐藏标题栏：
        val mAlphaAnimation = fadeOutAnimation
        appBar.animation = mAlphaAnimation
        mAlphaAnimation.startNow()
        appBar.visibility = View.GONE
    }

    //从搜索回到标题显示：
    private fun transmitToTitle(){
        //清空推荐词：
        setSearchSuggestion(ArrayList())

        //清空、取消聚焦搜索栏，收起软键盘：
        searchEditText.setText("")
        searchEditText.isFocusable = false
        searchEditText.isFocusableInTouchMode = false
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(searchEditText.windowToken, 0)

        //显示标题栏：
        val mAlphaAnimation = fadeInAnimation
        appBar.animation = mAlphaAnimation
        mAlphaAnimation.startNow()
        appBar.visibility = View.VISIBLE

        //使没有消失的推荐列表消失：
        if (suggestRecyclerView.visibility != INVISIBLE){
            suggestRecyclerView.visibility = INVISIBLE
        }
    }

    //点击搜索按钮，或者键盘上的搜索按钮后触发的监听
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

    //设置搜索框的标题
    fun setTitle(title: String){
        Log.i("MaterialSearchView:", "setTitle")
        mdToolBar.title = title
        this.title = title
    }

    //设置 navigation 的图标 -> 没什么用
    fun setNavigationIcon(drawable: Drawable){
        mdToolBar.navigationIcon = drawable
    }

    //设置 navigation 的图标，用ID的重载方法 -> 没什么用
    fun setNavigationIcon(resourceId: Int){
        mdToolBar.setNavigationIcon(resourceId)
    }

    //navigation 按钮点击监听传递
    fun setNavigationOnClickListener(onClickListener: OnClickListener){
        mdToolBar.setNavigationOnClickListener(onClickListener)
    }

    //传进一个字符串列表，将在搜索框下面显示，提供搜索建议
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
    这个方法执行填充的功能，点击推荐项目后将推荐的内容填充到 edittext
     */
    fun fillInSuggestion(suggestion: String){
        searchEditText.setText(suggestion)
        searchEditText.setSelection(suggestion.length)
    }

    /*
    这里的带 listener 的回调方法是为了方便获取推荐List的点击事件，并且将点击的内容，内容苏所在 list 的位置进行回传，
    方便设置推荐内容后，进行下一步搜索操作
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
    这里是搜索框内容一旦改变就激活的 listener 用于实时提供搜索建议
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