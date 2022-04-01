package com.utf8coding.customviewdemo.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.utf8coding.customviewdemo.R
import com.utf8coding.customviewdemo.recyclerViewTestAdapter.QzoneRecyclerViewTestAdapter

class QZoneFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_qzone, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerView = view.findViewById<RecyclerView>(R.id.qzoneRecyclerView)
        val layoutManager = LinearLayoutManager(activity)
        //这个展示用的RecyclerView随便写的，可能会有点BUG
        //没放五张图的是因为和六张图的的布局一样，7、8张同理。
        //数据的初始化放在adapter里面了因为比较方便（有好好写注释请不要生气qaq）
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = QzoneRecyclerViewTestAdapter()
    }
}