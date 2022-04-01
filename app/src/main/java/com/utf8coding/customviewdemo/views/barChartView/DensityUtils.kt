package com.utf8coding.customviewdemo.views.barChartView

import android.content.Context
import android.util.TypedValue
import java.lang.UnsupportedOperationException

//网上找来改的，这个类在绘制的时候感觉可以减少失误导致的 BUG
object DensityUtils  {
    fun dp2px(context: Context, dpVal: Float): Float {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dpVal, context.resources.displayMetrics
        )
    }

    fun sp2px(context: Context, spVal: Float): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP,
            spVal, context.resources.displayMetrics
        ).toInt()
    }

    fun px2dp(context: Context, pxVal: Int): Float {
        val scale = context.resources.displayMetrics.density
        return pxVal / scale
    }

    fun px2sp(context: Context, pxVal: Float): Float {
        return pxVal / context.resources.displayMetrics.scaledDensity
    }

    fun dps2pxs(context:Context, array: ArrayList<Float>): ArrayList<Float>{
        val tempArray = ArrayList<Float>()
        for (f in array){
            tempArray.add(DensityUtils.dp2px(context, f).toFloat())
        }
        return tempArray
    }
}