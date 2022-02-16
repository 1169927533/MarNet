package com.example.marnetsearch

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.SearchView
import android.widget.Toast
import com.example.marnetsearch.databinding.ActivityMainBinding
import com.example.matnet_model.BaseViewBingActivity
import com.example.matnet_model.MainNetResultActivity

class MainActivity : BaseViewBingActivity<ActivityMainBinding>() {
    override fun initView() {
        binding.searchView.setIconifiedByDefault(false)
        binding.searchView.setOnQueryTextListener(object :
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                var i = Intent(this@MainActivity,MainNetResultActivity::class.java)
                i.putExtra("searchcontent",query)
                startActivity(i)
                return false
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                return false;
            }
        })
    }
}