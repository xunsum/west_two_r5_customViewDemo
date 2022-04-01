package com.utf8coding.customviewdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.utf8coding.customviewdemo.fragments.BarChartFragment
import com.utf8coding.customviewdemo.fragments.MaterialSearchViewFragment
import com.utf8coding.customviewdemo.fragments.QZoneFragment

class MainActivity : AppCompatActivity() {
    private val tabLayout: TabLayout
        get() {
            return findViewById(R.id.mainActivityTabLayout)
        }
    private val viewPager: ViewPager2
        get() {
            return findViewById(R.id.mainActivityViewPager)
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
    }

    private fun initView() {
        //remove title bar -> what's the proper way to deal with it?
        supportActionBar?.hide()
        //init viewPager2:
        viewPager.adapter = object : FragmentStateAdapter(this) {
            override fun getItemCount() = 3

            override fun createFragment(position: Int): Fragment =
                when (position){
                    0 -> MaterialSearchViewFragment()
                    1 -> QZoneFragment()
                    2 -> BarChartFragment()
                    else -> {
                        MaterialSearchViewFragment()
                    }
                }
        }
        //init tabView:
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = getString(R.string.materialSearchView)
                }
                1 -> {
                    tab.text = getString(R.string.qZoneView)
                }
                2 -> {
                    tab.text = getString(R.string.barChartView)
                }
            }
        }.attach()
    }
}