package com.djambulat69.developerslife

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.djambulat69.developerslife.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.viewPager.adapter =
            TabFragmentStateAdapter(supportFragmentManager, lifecycle)

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when(position) {
                0 -> tab.text = getString(R.string.latest_tab_name)
                1 -> tab.text = getString(R.string.top_tab_name)
                2 -> tab.text = getString(R.string.hot_tab_name)
            }
        }.attach()
    }
}
