package com.utf8coding.customviewdemo.fragments

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.button.MaterialButton
import com.utf8coding.customviewdemo.R
import com.utf8coding.customviewdemo.views.barChartView.BarChartView
import java.lang.Thread.sleep
import kotlin.concurrent.thread

class BarChartFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_bar_chart, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val barChartView = view.findViewById<BarChartView>(R.id.barChart)
        val startAnimationButton = view.findViewById<MaterialButton>(R.id.showAnimation)
        val recoverAnimationButton = view.findViewById<MaterialButton>(R.id.recoverAnimation)
        //初始化一个数据怕，barData 类是专门用来存储条条的数据的，颜色和量的数据定义
        barChartView.coordinateLineThickness = 5f
        barChartView.barWidth = 50f
        barChartView.topSpacing = 100f
        barChartView.endSpacing = 100f
        barChartView.barDataList = arrayListOf(BarChartView.BarData(2f, Color.parseColor("#4285f4")),
            BarChartView.BarData(1f, Color.parseColor("#34a853")),
            BarChartView.BarData(8f, Color.parseColor("#fbbc05")),
            BarChartView.BarData(19f, Color.parseColor("#ea4335"))
        )
        barChartView.title = "TEST TITLE"
        barChartView.titleTextSize = 35f

        //换成另一个数据
        startAnimationButton.setOnClickListener {
            thread {
                activity?.runOnUiThread {
                    barChartView.barDataList = arrayListOf(
                        BarChartView.BarData(10f, Color.parseColor("#000000")),
                        BarChartView.BarData(13f, Color.parseColor("#505050")),
                        BarChartView.BarData(5f, Color.parseColor("#737373")),
                        BarChartView.BarData(1f, Color.parseColor("#eeeeee")),
                        BarChartView.BarData(15f, Color.parseColor("#0078d7"))
                    )
                }
                //这里是分开展示动画，合在一起同时开始也没有 BUG（点下恢复按钮的时候就是合在一起的），只能说 ObjectAnimator 超神奇
                sleep(1000)
                activity?.runOnUiThread {
                    barChartView.topSpacing = 50f
                }
                sleep(1000)
                activity?.runOnUiThread {
                    barChartView.endSpacing = 50f
                }
                sleep(1000)
                activity?.runOnUiThread {
                    barChartView.barWidth = 60f
                }
                sleep(500)
                activity?.runOnUiThread {
                    barChartView.isShowCoordinate = false
                }
            }
        }

        //恢复成一开始的数据
        recoverAnimationButton.setOnClickListener {
            thread {
                activity?.runOnUiThread{
                    barChartView.coordinateLineThickness = 5f
                    barChartView.barWidth = 50f
                    barChartView.topSpacing = 100f
                    barChartView.endSpacing = 100f
                    barChartView.barDataList = arrayListOf(
                        BarChartView.BarData(2f, Color.parseColor("#4285f4")),
                        BarChartView.BarData(1f, Color.parseColor("#34a853")),
                        BarChartView.BarData(8f, Color.parseColor("#fbbc05")),
                        BarChartView.BarData(19f, Color.parseColor("#ea4335"))
                    )
                    barChartView.isShowCoordinate = true
                }
            }
        }
    }



}