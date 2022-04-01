package com.utf8coding.customviewdemo.views.barChartView

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.View

class BarChartDrawingView(context: Context, attrs: AttributeSet): View(context, attrs){
    //是否显示坐标轴
    var isShowCoordinate: Boolean = true

    //所有的操作以dp为单位，只有在设置时函数转化为像素
    //动画用，每个 bar 在绘制时的高度乘以这个数字：
    var scale: Float = 1f
    set(value) {
        field = value
        invalidate()
    }
    //bar 的宽度，默认20dp？
    var barWidth: Float = 20f
    //提供画 bar 时候的数据
    var dataList: ArrayList<BarChartView.BarData> = ArrayList()
        set(value) {
            field = value
            invalidate()
        }
    var topSpacing: Float = 20f
    set(value) {
        field = value
        invalidate()
    }
    var endSpacing = 0f
    set(value){
        field = value
        invalidate()
    }
    //坐标轴的线粗
    var lineWidthInDp = 5f
        set(dp: Float) {
            field = dp
            invalidate()
        }

    //绘图用的 paint
    private var paint = Paint()
    //dp为单位的长和宽：
    private var dpWidth: Float = 0f
    private var dpHeight: Float = 0f


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        dpWidth = MeasureSpec.getSize(widthMeasureSpec).toDp()
        dpHeight = MeasureSpec.getSize(heightMeasureSpec).toDp()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (isShowCoordinate){
            paint.strokeWidth = DensityUtils.dp2px(context, lineWidthInDp)
        } else {
            paint.style = Paint.Style.FILL
            paint.isAntiAlias = true
            paint.strokeWidth = 0f
        }
        paint.setShadowLayer(5f, 10f, 10f, Color.parseColor("#858585"))

        //画条形
        drawBars(canvas)

        //画坐标轴
        if (isShowCoordinate){
            drawCoordinate(canvas)
        }
    }
    //画条形
    private fun drawBars(canvas: Canvas?){
        if (endSpacing > dpWidth){
            endSpacing = dpWidth - 50
        }
        val availableBarWidth = (dpWidth - endSpacing) / dataList.size
        val availableBarHeight = dpHeight - topSpacing
        //每块后面一个空格：
        val space: Float
        //找一个最数据高值出来
        var highestAmount = 0f
        for (i in dataList){
            if (i.amount > highestAmount) highestAmount = i.amount
        }
        //每1单位需要画的高度：
        val heightUnit = availableBarHeight / highestAmount
        //判断期望宽度是否能够放下，不能放下取最大值
        if (barWidth > availableBarWidth - 2){
            barWidth = availableBarWidth
            space = 0f
        } else {
            space = availableBarWidth - barWidth
        }
        //用循环开始画条：
        var pxLeft: Float = paint.strokeWidth*1.5f
        var pxTop: Float
        var pxRight: Float
        val pxBottom: Float = height - paint.strokeWidth*1.5f
        for (i in 0 until dataList.size){
            val data = dataList[i]
            pxLeft = if (i != 0){
                pxLeft + barWidth.toPx() + space.toPx()
            } else{
                pxLeft
            }
            scale = if (scale >= 0){
                scale
            } else{
                0f
            }
            pxTop = height - paint.strokeWidth * 1.5f - ((heightUnit.toPx() * data.amount) * scale)
            if (pxTop < 0){
                pxTop = 0f
            }
            pxRight = pxLeft + barWidth.toPx()
            paint.color = data.color
            canvas?.drawRect(pxLeft, pxTop, pxRight, pxBottom, paint)
        }
    }
    //画坐标轴
    private fun drawCoordinate(canvas: Canvas?){
        paint.style = Paint.Style.FILL
        paint.color = Color.parseColor("#696969")
        paint.strokeJoin = Paint.Join.ROUND
        paint.isAntiAlias = true
        //画坐标轴，此处与整个View等宽高，所以直接使用像素计算：
        val lineStrokeWidth = paint.strokeWidth
        val dp20 = DensityUtils.dp2px(context, DensityUtils.px2dp(context, lineStrokeWidth.toInt()) * 4)
        val dp10 = DensityUtils.dp2px(context, DensityUtils.px2dp(context, lineStrokeWidth.toInt()) * 2)
        val lines = floatArrayOf(
            dp20 - lineStrokeWidth, dp20 - lineStrokeWidth, lineStrokeWidth, lineStrokeWidth,

            lineStrokeWidth, lineStrokeWidth, lineStrokeWidth, height - lineStrokeWidth,

            lineStrokeWidth, height - lineStrokeWidth,
            width - lineStrokeWidth, height - lineStrokeWidth,

            width - lineStrokeWidth, height - lineStrokeWidth,
            width - lineStrokeWidth - dp10, height - lineStrokeWidth - dp10
        )
        canvas?.drawLines(lines, paint)
        //坐标轴画完
    }

    //发Log：
    private fun makeLogInfo(msg: String){
        Log.i("BarChartDrawingView:", msg)
    }

    //Float px 转 dp
    private fun Int.toDp():Float{
        return DensityUtils.px2dp(context,this)
    }
    private fun Float.toPx(): Float{
        return DensityUtils.dp2px(context,this)
    }
}