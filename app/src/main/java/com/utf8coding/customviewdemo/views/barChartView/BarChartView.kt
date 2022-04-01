package com.utf8coding.customviewdemo.views.barChartView

import android.animation.Animator
import android.animation.ObjectAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.animation.AnticipateOvershootInterpolator
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.constraintlayout.widget.ConstraintLayout
import com.utf8coding.customviewdemo.R
import org.w3c.dom.Text


class BarChartView(context: Context, attrs: AttributeSet): ConstraintLayout(context, attrs) {
    private val drawingView: BarChartDrawingView
    get() {
        return findViewById(R.id.coordinate)
    }
    private val titleTextView: TextView
        get(){
            return findViewById(R.id.titleTextView) as TextView
        }

    //标题
    var title = ""
    set(value) {
        if (value == ""){
            titleTextView.visibility = GONE
        } else {
            titleTextView.visibility = VISIBLE
            titleTextView.text = value
        }
        field = title
    }
    //标题字体
    var titleTextSize: Float = 50f
    set(value) {
        field = value
        titleTextView.textSize = value
    }
    init {
        LayoutInflater.from(context).inflate(R.layout.view_bar_chart, this)
        titleTextView.text = title
    }

    //有无坐标轴
    var isShowCoordinate: Boolean = drawingView.isShowCoordinate
    set(value) {
        field = value
        drawingView.isShowCoordinate = value
    }

    //坐标轴线粗
    var coordinateLineThickness: Float = 2f
    get() {
        return drawingView.lineWidthInDp
    }
    set(value) {
        field = value
        drawingView.lineWidthInDp = value
    }

    //设置Bar
    //最高条距离View顶部宽度
    var topSpacing: Float = drawingView.topSpacing
        set(value){
            field = value
            val animator = ObjectAnimator.ofFloat(drawingView,"topSpacing" , value)
            animator.duration = 200
            animator.start()
        }
    //条宽度（条之间空格宽度自动计算）
    var barWidth: Float = drawingView.barWidth
        set(value) {
            field= value
            val animator = ObjectAnimator.ofFloat(drawingView,"barWidth" , value)
            animator.duration = 200
            animator.start()
        }
    //在所有条画完之后的空格宽度
    var endSpacing: Float = drawingView.endSpacing
        set(value) {
            field = value
            val animator = ObjectAnimator.ofFloat(drawingView,"endSpacing" , value)
            animator.duration = 200
            animator.start()
        }
    //条的名称、高度和颜色属性
    data class BarData(val amount: Float, @ColorInt val color: Int)
    var barDataList: ArrayList<BarData> = ArrayList()
        set(value) {
            field = value
            val animator = ObjectAnimator.ofFloat(drawingView, "scale", 1f, 0f)
            animator.duration = 350
            animator.addListener(object: Animator.AnimatorListener{
                override fun onAnimationEnd(p0: Animator?) {
                    drawingView.dataList = value
                    val animator2 = ObjectAnimator.ofFloat(drawingView, "scale", 0f, 1f)
                    animator2.interpolator = AnticipateOvershootInterpolator()
                    animator2.duration = 350
                    animator2.start()
                }

                override fun onAnimationStart(p0: Animator?) {
                }
                override fun onAnimationCancel(p0: Animator?) {
                }
                override fun onAnimationRepeat(p0: Animator?) {
                }
            })
            animator.start()
        }
}