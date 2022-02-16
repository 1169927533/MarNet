package com.example.matnet_model

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentStatePagerAdapter
import com.example.matnet_model.databinding.ActivitySearchresultBinding
import com.example.matnet_model.fragment.BaseNetFragment
import com.google.android.material.tabs.TabLayoutMediator

class MainNetResultActivity : BaseViewBingActivity<ActivitySearchresultBinding>() {
    var searchContent = ""
    var titles: ArrayList<String> = ArrayList()
    var fragments: ArrayList<Fragment> = ArrayList()
    override fun initView() {
        binding.searchView.setIconifiedByDefault(false)
        searchContent = intent.getStringExtra("searchcontent").toString()
        initTabAndViewPager()

        binding.searchView.setOnQueryTextListener(object :
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                (fragments[binding.viewPager.currentItem] as BaseNetFragment).refreshSearch(
                    query ?: ""
                )
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false;
            }
        })
        binding.searchView.setQuery(searchContent, false)
    }

    private fun initTabAndViewPager() {
        titles.add("soKanKan")
        titles.add("Btso")
        fragments.add(BaseNetFragment(searchContent))
        fragments.add(BaseNetFragment(searchContent))
        binding.viewPager.adapter = object : FragmentStatePagerAdapter(
            supportFragmentManager,
            FragmentStatePagerAdapter.BEHAVIOR_SET_USER_VISIBLE_HINT
        ) {
            override fun getCount(): Int {
                return titles.size
            }

            override fun getItem(position: Int): Fragment {
                return fragments[position]
            }
        }
        binding.tabLayout.setupWithViewPager(binding.viewPager)
        for ((index, value) in titles.withIndex()) {
            binding.tabLayout.getTabAt(index)?.text = titles[index]
        }
    }
}