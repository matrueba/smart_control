package com.example.smartcontrol.Adapter


import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.smartcontrol.Fragments.ControlFragment
import com.example.smartcontrol.Fragments.IndicatorsFragment

class MyAdapter(fragmentActivity: FragmentActivity): FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> ControlFragment()
            else ->  IndicatorsFragment()
        }
    }
}