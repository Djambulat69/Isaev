package com.djambulat69.developerslife

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import java.lang.IllegalArgumentException

class TabFragmentStateAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle)
    : FragmentStateAdapter(fragmentManager, lifecycle){
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return TabFragment.newInstance(when (position) {
            0 -> "latest"
            1 -> "top"
            2 -> "hot"
            else -> throw IllegalArgumentException()
        })
    }

}