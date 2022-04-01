package com.utf8coding.customviewdemo.recyclerViewTestAdapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.utf8coding.customviewdemo.MyApplication
import com.utf8coding.customviewdemo.R
import com.utf8coding.customviewdemo.views.qZoneItemView.QZoneItemView

class QzoneRecyclerViewTestAdapter(): RecyclerView.Adapter<QzoneRecyclerViewTestAdapter.ViewHolder>() {
    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val qzoneView: QZoneItemView = view.findViewById(R.id.qzoneView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_qzone, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val positionNow = position
        val imageArray = when(positionNow){
            0 -> arrayListOf(R.drawable.test_user_image_1)
            1 -> arrayListOf(R.drawable.test_user_image_1, R.drawable.test_user_image_2)
            2 -> arrayListOf(R.drawable.test_user_image_1, R.drawable.test_user_image_2,
                R.drawable.test_user_image_3, R.drawable.test_user_image_4
            )
            //测试五张图的效果可以把这里的ID列表里面删掉一张人
            3 -> arrayListOf(R.drawable.test_user_image_1, R.drawable.test_user_image_2,
                R.drawable.test_user_image_3, R.drawable.test_user_image_4,
                R.drawable.test_user_image_5, R.drawable.test_user_image_6
            )
            //测试八张图、七张图的效果可以把这里的ID列表里面删掉一张、两张
            4 -> arrayListOf(R.drawable.test_user_image_1, R.drawable.test_user_image_2,
                R.drawable.test_user_image_3, R.drawable.test_user_image_4,
                R.drawable.test_user_image_5, R.drawable.test_user_image_6,
                R.drawable.test_user_image_7, R.drawable.test_user_image_8,
                R.drawable.test_user_image_9
            )
            else -> {ArrayList()}
        }
        val qzoneView = holder.qzoneView
        qzoneView.setContentText("本条说说序号: ${positionNow + 1}\n从残佬那边薅了一个棉花糖0v0。" +
                "\n总之随便投点烦恼商谈啦美食推荐啦绘画问题啦啥都行！没整过这个，先不定主题什么的 " +
                "\n下周一的肝稿无图直播会捞一些出来聊聊！"
        )
        //头像自动处理成圆形
        qzoneView.setUserAvatar(R.drawable.test_user_avatar_square)
        //用户名部分
        qzoneView.setSendTimeText("发布于 4小时前")
        qzoneView.setUserName("Bison仓鼠仓")
        Log.i("QZoneFragment:", "setting image: $imageArray")

        //设置进准备好的图片
        qzoneView.setImageByIdList(imageArray)

        //监听：头像、每张图片、文字、用户名、还有右上角的目录按钮
        qzoneView.setOnImagesClickListener(object: QZoneItemView.OnImagesClickListener{
            override fun onImageClick(index: Int) {
                Toast.makeText(MyApplication.context, "onclick image index： $index， 说说是第 ${positionNow + 1} 条", Toast.LENGTH_SHORT).show()
            }
        })
        qzoneView.setOnUserAvatarClickListener(object: QZoneItemView.OnUserAvatarClickListener{
            override fun onClick() {
                toast("on user avatar click. 说说是第 ${positionNow + 1} 条")
            }
        })
        qzoneView.setOnUserNameClickListener(object: QZoneItemView.OnUserNameClickListener{
            override fun onClick() {
                toast("on user name click.  说说是第 ${positionNow + 1} 条")
            }
        })
        qzoneView.setOnMenuButtonClickListener(object: QZoneItemView.OnMenuButtonClickListener{
            override fun onClick() {
                toast("on menu click. 说说是第 ${positionNow + 1} 条")
            }
        })
        qzoneView.setOnContentTextClickListener(object: QZoneItemView.OnContentTextClickListener{
            override fun onClick() {
                toast("on user content text click. 说说是第 ${positionNow + 1} 条")
            }
        })
    }

    override fun getItemCount(): Int {
        return 5 //只是展示用的Adapter，展示用是5个View，所以直接返回
    }

    //方便发toast
    private fun toast(text: String){
        Toast.makeText(MyApplication.context, text, Toast.LENGTH_SHORT).show()
    }
}