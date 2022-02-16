package com.example.matnet_model.fragment

import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.example.matnet_model.BuildConfig
import com.example.matnet_model.R
import com.example.matnet_model.adapter.SearchViewAdapter
import com.example.matnet_model.bean.MarNetLinkBean
import com.example.matnet_model.util.HtmlUtil
import com.example.smart.smartpack.base.fragme.BaseRecycleViewFragment
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import kotlinx.coroutines.*
import org.jsoup.Connection
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.lang.Exception

class BaseNetFragment(var searchContent: String) : BaseRecycleViewFragment<MarNetLinkBean>(),
    CoroutineScope by MainScope() {
    var searchPage = 1
    var netHeadUrl = "https://www.sokankan80.cc"

    override val baseAdapter: BaseQuickAdapter<MarNetLinkBean, BaseViewHolder> by lazy {
        SearchViewAdapter().apply {
            setOnItemChildLongClickListener { adapter, view, position ->
                launch(Dispatchers.IO) {
                    var urlLink =
                        HtmlUtil.getNetLinkAddress("${netHeadUrl}${baseAdapter.data[position].link}")
                    withContext(Dispatchers.Main) {
                        for (value in urlLink) {
                            Log.i("zjc", "${value}")
                        }
                    }
                }
                true
            }
        }
    }

    override val layoutManager: RecyclerView.LayoutManager by lazy {
        LinearLayoutManager(requireContext())
    }
    override val onFetchListener: (page: Int) -> Unit = {
        searchPage = it
        getData()
    }

    override fun initViewData() {
        super.initViewData()
        view?.findViewById<SmartRefreshLayout>(R.id.smartRefreshLayout_fans)?.autoRefresh()
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_empty
    }

    override fun observeLiveData() {
    }

    private fun getData() {
        launch(Dispatchers.IO) {
            var url = "${netHeadUrl}/search/${searchContent}/page-${searchPage}.html"
            try {
                var doc = HtmlUtil.getHtmlContent(url)
                var elements = doc.select("div#w0.list-view article.item div")
                withContext(Dispatchers.Main) {
                    var list = HtmlUtil.getEveryItem(elements)
                    finishGetDataSuccess()
                    smartRefreshUtil.onFetchFinish(list, false)
                }
            } catch (e: Exception) {
                print(e.printStackTrace())
                if (BuildConfig.DEBUG) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(requireContext(), "服务器异常502", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }


    fun refreshSearch(content: String) {
        searchContent = content
        searchPage = 1
        smartRefreshUtil.startRefresh()//开启第一个加载数据
    }
}