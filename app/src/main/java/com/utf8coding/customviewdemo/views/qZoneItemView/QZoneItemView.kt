package com.utf8coding.customviewdemo.views.qZoneItemView

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewStub
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.utf8coding.customviewdemo.R
import com.utf8coding.customviewdemo.views.barChartView.DensityUtils
import jp.wasabeef.glide.transformations.RoundedCornersTransformation
import java.net.URI
import java.net.URL
/*
使用ViewStub实现，最大的问题是不能实现加载图片之后再次加载。
 */

class QZoneItemView(context: Context, attrs: AttributeSet): LinearLayout(context, attrs) {
    private var mwidth = 0
    private var mheight = 0
    private val contentText: TextView
        get() {
            return findViewById(R.id.contentText) as TextView
        }
    private val sendTimeText: TextView
        get() {
            return findViewById(R.id.timeOfSending) as TextView
        }
    private val userAvatar: ImageView
        get() {
            return findViewById(R.id.userHeadImageView) as ImageView
        }
    private val userName: TextView
        get() {
            return findViewById(R.id.userName) as TextView
        }
    private val pictureViewStub: ViewStub
        get() {
            return findViewById(R.id.qzoneViewStub) as ViewStub
        }
    private val menuButton: ImageButton
        get() {
            return findViewById(R.id.optionsButton) as ImageButton
        }
    private var urlList = ArrayList<URL>()
    private var uriList = ArrayList<URI>()
    private var drawableList = ArrayList<Drawable>()
    private var idList = ArrayList<Int>()
    private val URI_MODE = 0
    private val URL_MODE = 1
    private val DRAWABLE_MODE = 2
    private val ID_MODE = 3
    private val NO_PIC_MODE= 4
    private var mode: Int = NO_PIC_MODE
    private lateinit var imagePartView: ConstraintLayout
    private var scale = 1f
    private var onImagesClickListener: OnImagesClickListener? = null
    private var isImageLoaded = false
    private val widthForSetImageHeight: Int
        get() {
            return DensityUtils.dp2px(context, 400f).toInt()
        }

    init {
        LayoutInflater.from(context).inflate(R.layout.view_qzone_item, this)
    }

    private fun setImageHeight(imagePartView: ConstraintLayout, width: Int ,scale: Float) {
        imagePartView.layoutParams.height = (width/scale).toInt()
    }

    //测试-暂无：

    //设置说说内容
    fun setContentText(text: String){
        contentText.text = text
    }

    //设置用户头像
    fun setUserAvatar(url: URL){
        Glide.with(context)
            .load(url)
            .apply(RequestOptions
                .bitmapTransform(RoundedCornersTransformation(150, 0,
                    RoundedCornersTransformation.CornerType.BOTTOM)
                )
            )
            .skipMemoryCache(true)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .into(userAvatar)
    }
    fun setUserAvatar(uri: URI){
        Glide.with(context)
            .load(uri)
            .apply(RequestOptions
                .bitmapTransform(RoundedCornersTransformation(150, 0,
                    RoundedCornersTransformation.CornerType.BOTTOM)
                )
            )
            .skipMemoryCache(true)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .into(userAvatar)
    }
    fun setUserAvatar(drawable: Drawable){
        Glide.with(context)
            .load(drawable)
            .apply(RequestOptions
                .bitmapTransform(RoundedCornersTransformation(150, 0,
                    RoundedCornersTransformation.CornerType.BOTTOM)
                )
            )
            .skipMemoryCache(true)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .into(userAvatar)
    }
    fun setUserAvatar(id: Int){
        Glide.with(context)
            .load(id)
            .apply(RequestOptions
                .bitmapTransform(RoundedCornersTransformation(150, 0,
                    RoundedCornersTransformation.CornerType.BOTTOM)
                )
            )
            .skipMemoryCache(true)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .into(userAvatar)
    }

    //设置用户名：
    fun setUserName(name: String){
        userName.text = name
    }

    //设置上次发送的时间文本
    fun setSendTimeText(text: String){
        sendTimeText.text = text
    }

    //设置目录按钮：
    fun setMenuButtonVisibility(visibility: Int){
        menuButton.visibility = visibility
    }

    //加载图片的部分
    //设置九宫格内图片
    fun setImageByURLList(list: ArrayList<URL>){
        mode = URL_MODE
        urlList = list
        checkPicturesTypeAndLoad()
    }
    fun setImageByURIList(list: ArrayList<URI>){
        mode = URI_MODE
        uriList = list
        checkPicturesTypeAndLoad()
    }
    fun setImageByDrawableList(list: ArrayList<Drawable>){
        mode = DRAWABLE_MODE
        drawableList = list
        checkPicturesTypeAndLoad()
    }
    fun setImageByIdList(list: ArrayList<Int>){
        mode = ID_MODE
        idList = list
        checkPicturesTypeAndLoad()
    }
    private fun checkPicturesTypeAndLoad(){
        if (!isImageLoaded){
            isImageLoaded = true
            when (mode) {
                URL_MODE -> {
                    makeLogInfo("load: URL_MODE")
                    loadPic(urlList)
                }
                URI_MODE -> {
                    makeLogInfo("load: URI_MODE")
                    loadPic(uriList)
                }
                DRAWABLE_MODE -> {
                    makeLogInfo("load: DRAWABLE_MODE")
                    loadPic(drawableList)
                }
                ID_MODE -> {
                    makeLogInfo("load: ID_MODE")
                    loadPic(idList)
                }
                else -> ArrayList<URI>()
            }
        } else {
            Log.w("QZoneItemView:", "Loading picture twice without redrawing the whole " +
                    "view is not available")
        }
    }
    //根据 ArrayList 里图片数量不同在 ViewStub 中显示不同的 View，最后实现自适应的动态显示
    private fun<T> loadPic(imageSourceList: ArrayList<T>){
        fun<T> loadPic1(imageContextList: ArrayList<T>){
            pictureViewStub.layoutResource = R.layout.view_qzone_item_view1
            pictureViewStub.inflate()
            val image1 = findViewById<ImageView>(R.id.view1Image)
            val imageArray = arrayListOf(image1)
            setOnImageViewClickListener(imageArray)
            glideLoad30Radius(imageContextList, imageArray)
        }
        fun<T> loadPic2(imageContextList: ArrayList<T>){
            pictureViewStub.layoutResource = R.layout.view_qzone_item_view2
            pictureViewStub.inflate()
            val image1 = findViewById<ImageView>(R.id.view2Image1)
            val image2 = findViewById<ImageView>(R.id.view2Image2)
            imagePartView = findViewById(R.id.view2)
            scale = 2f
            val imageArray = arrayListOf(image1, image2)
            setOnImageViewClickListener(imageArray)
            Log.e("qzone onsetpic:", "$mwidth")
            setImageHeight(imagePartView, widthForSetImageHeight, scale)
            glideLoad30Radius(imageContextList, imageArray)
        }
        fun<T> loadPic3(imageContextList: ArrayList<T>){
            pictureViewStub.layoutResource = R.layout.view_qzone_item_view4
            pictureViewStub.inflate()
            val image1 = findViewById<ImageView>(R.id.view4_1Image1)
            val image2 = findViewById<ImageView>(R.id.view4_1Image2)
            val image3 = findViewById<ImageView>(R.id.view4_2Image1)
            imagePartView = findViewById(R.id.view4)
            val imageArray = arrayListOf(image1, image2, image3)
            setOnImageViewClickListener(imageArray)
            setImageHeight(imagePartView, widthForSetImageHeight, scale)
            glideLoad30Radius(imageContextList, imageArray)
        }
        fun<T> loadPic4(imageContextList: ArrayList<T>){
            pictureViewStub.layoutResource = R.layout.view_qzone_item_view4
            pictureViewStub.inflate()
            val image1 = findViewById<ImageView>(R.id.view4_1Image1)
            val image2 = findViewById<ImageView>(R.id.view4_1Image2)
            val image3 = findViewById<ImageView>(R.id.view4_2Image1)
            val image4 = findViewById<ImageView>(R.id.view4_2Image2)
            imagePartView = findViewById(R.id.view4)
            val imageArray =
                arrayListOf(image1, image2, image3, image4)
            glideLoad30Radius(imageContextList, imageArray)
            setImageHeight(imagePartView, widthForSetImageHeight, scale)
            setOnImageViewClickListener(imageArray)
        }
        fun<T> loadPic6(imageContextList: ArrayList<T>){
            pictureViewStub.layoutResource = R.layout.view_qzone_item_view6
            pictureViewStub.inflate()
            val image1 = findViewById<ImageView>(R.id.view6_1Image1)
            val image2 = findViewById<ImageView>(R.id.view6_1Image2)
            val image3 = findViewById<ImageView>(R.id.view6_1Image3)
            val image4 = findViewById<ImageView>(R.id.view6_2Image1)
            val image5 = findViewById<ImageView>(R.id.view6_2Image2)
            val image6 = findViewById<ImageView>(R.id.view6_2Image3)
            imagePartView = findViewById(R.id.view6)
            scale = 1.5f
            val imageArray =
                arrayListOf(image1, image2, image3, image4, image5, image6)
            setOnImageViewClickListener(imageArray)
            setImageHeight(imagePartView, widthForSetImageHeight, scale)
            glideLoad30Radius(imageContextList,imageArray)
        }
        fun<T> loadPic9(imageContextList: ArrayList<T>){
            pictureViewStub.layoutResource = R.layout.view_qzone_item_view9
            pictureViewStub.inflate()
            imagePartView = findViewById(R.id.view9)
            val image1 = findViewById<ImageView>(R.id.view9_1Image1)
            val image2 = findViewById<ImageView>(R.id.view9_1Image2)
            val image3 = findViewById<ImageView>(R.id.view9_1Image3)
            val image4 = findViewById<ImageView>(R.id.view9_2Image1)
            val image5 = findViewById<ImageView>(R.id.view9_2Image2)
            val image6 = findViewById<ImageView>(R.id.view9_2Image3)
            val image7 = findViewById<ImageView>(R.id.view9_3Image1)
            val image8 = findViewById<ImageView>(R.id.view9_3Image2)
            val image9 = findViewById<ImageView>(R.id.view9_3Image3)
            val imageArray =
                arrayListOf(image1, image2, image3, image4, image5, image6, image7, image8, image9)
            setOnImageViewClickListener(imageArray)
            setImageHeight(imagePartView, widthForSetImageHeight, scale)
            glideLoad30Radius(imageContextList,imageArray)
        }
        Log.i("QZoneView:", "loading picture: source list: $imageSourceList, id list: $idList")
        when(imageSourceList.size){
            1 -> {
                makeLogInfo("loading view: 1pic_horizontal")
                loadPic1(imageSourceList)
            }
            2 -> {
                makeLogInfo("loading view: 2pic")
                loadPic2(imageSourceList)
            }
            3 -> {
                makeLogInfo("loading view: 3pic")
                loadPic3(imageSourceList)
            }
            4 -> {
                makeLogInfo("loading view: 4pic")
                loadPic4(imageSourceList)
            }
            in 5..6 -> {
                loadPic6(imageSourceList)
            }
            in 7..9 -> {
                loadPic9(imageSourceList)
            }
        }
    }
    //用来设置Glide的工具类，不安全，泛型范围应限定为 URL, URI, Drawable, Int
    private fun<T> glideLoad30Radius(pictureContextList: ArrayList<T>, targetViewList: ArrayList<ImageView>){
        //遍历两个列表，进行加载
        for (i in 0 until pictureContextList.size){
            makeLogInfo("Glide loading: ${pictureContextList[i]} to ${targetViewList[i]}")
            Glide.with(context).load(pictureContextList[i])
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(targetViewList[i])
        }
        if (targetViewList.size > pictureContextList.size){
            for (i in (targetViewList.size - (targetViewList.size - pictureContextList.size)) until targetViewList.size){
                makeLogInfo("hiding: $i")
                targetViewList[i].visibility = INVISIBLE
            }
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        mwidth = MeasureSpec.getSize(widthMeasureSpec)
        mheight = MeasureSpec.getSize(heightMeasureSpec)
    }

    //onClickListeners:
    //图片点击返回图片在传入图片列表中的index
    private fun setOnImageViewClickListener(viewList: ArrayList<ImageView>){
        for (i in 0 until viewList.size){
            val view = viewList[i]
            view.setOnClickListener{
                onImageViewClick(i)
            }
        }
    }
    interface OnImagesClickListener{
         fun onImageClick(index: Int)
    }
    fun setOnImagesClickListener(onImagesClickListener: OnImagesClickListener){
        this.onImagesClickListener = onImagesClickListener
    }
    private fun onImageViewClick(index: Int){
        onImagesClickListener?.onImageClick(index)
    }
    //点击TextView:
    interface OnContentTextClickListener{
        fun onClick()
    }
    fun setOnContentTextClickListener(listener: OnContentTextClickListener){
        contentText.setOnClickListener {
            listener.onClick()
        }
    }
    //点击用户名：
    interface OnUserNameClickListener{
        fun onClick()
    }
    fun setOnUserNameClickListener(listener: OnUserNameClickListener){
        userName.setOnClickListener{
            listener.onClick()
        }
    }
    //点击头像：
    interface OnUserAvatarClickListener{
        fun onClick()
    }
    fun setOnUserAvatarClickListener(listener: OnUserAvatarClickListener){
        userAvatar.setOnClickListener{
            listener.onClick()
        }
    }
    //点击菜单按钮：
    interface OnMenuButtonClickListener{
        fun onClick()
    }
    fun setOnMenuButtonClickListener(listener: OnMenuButtonClickListener){
        menuButton.setOnClickListener{
            listener.onClick()
        }
    }

    //发Log：
    private fun makeLogInfo(msg: String){
        Log.i("QZoneView:", msg)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        Log.e("qzone", "size changed: $w")
        mwidth = w
        mheight = h
    }
}