package com.example.matnet_model.util

import android.util.Log
import com.example.matnet_model.bean.MarNetLinkBean
import org.jsoup.Connection
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import java.lang.Exception
import java.util.regex.Pattern

object HtmlUtil {
    suspend fun getHtmlContent(url: String): Document {
        var connection =
            Jsoup.connect(url)
        var headMap = HashMap<String, String>()
        headMap["accept-language"] = "zh-CN,zh;q=0.9"
        headMap["sec-ch-ua-platform"] = "Linux"
        headMap["accept"] =
            "text/html;application/xhtml+xml;application/xml;q=0.9;image/avif;image/webp;image/apng;*/*;q=0.8;application/signed-exchange;v=b3;q=0.9"
        headMap["accept-encoding"] = "gzip;deflate;br"
        headMap["sec-ch-ua-platform"] = "Android"
        connection.headers(headMap)
        connection.userAgent("Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/98.0.4758.80 Mobile Safari/537.36")
        connection.method(Connection.Method.GET)
        return connection.get()
    }

    // 获取页数
    fun getTotalNum(elements: Elements): String {
        var content = ""
        var totalNumber = elements.select("div.summary")
        for (value in totalNumber) {
            content += value.text()
        }
        return content
    }

    // 获取页数
    fun getEveryItem(elements: Elements): ArrayList<MarNetLinkBean> {
        var linkType = ""
        var linkTitle = ""
        var linkAddress = ""

        var HTMLTAGPATTERN = Pattern.compile("<[a-zA-Z]+.*?>([\\s\\S]*?)</[a-zA-Z]*?>");
        var listItem = ArrayList<MarNetLinkBean>()
        for (value in elements) {
            linkType = ""
            linkTitle = ""
            linkAddress = ""
            val c1 = value.select("a[href]")
            val temp = c1.select("h4").html().split(";") // 分割字符串
            var results = ArrayList<String>();
            if (temp.size > 0) {
                var imageTagMatcher = HTMLTAGPATTERN.matcher(temp[0])
                while (imageTagMatcher.find()) {
                    var result = "";
                    result = imageTagMatcher.group(1).trim();
                    result = replaceStartTag(result)!!
                    results.add(result)
                }
                for (avaa in results) {
                    linkType += avaa
                }
            }
            if (temp.size > 1) {
                linkTitle = temp[1]
            }
            linkAddress = c1.attr("href")
            if (!linkType.contains("在线播放")) {
                var marNetLinkBean = MarNetLinkBean(linkType, linkTitle, linkAddress)
                listItem.add(marNetLinkBean)
            }
        }
        return listItem
    }

    /**
     * 针对多个标签嵌套的情况进行处理
     * 比如
     *
     *<span style="white-space: normal;">王者荣耀</span>
     * 预处理并且正则匹配完之后结果是 <span>王者荣耀
     * 需要手工移除掉前面的起始标签
     * @param content
     * @return
    </span> */
    fun replaceStartTag(content: String?): String? {
        var content = content
        if (content == null || content.length == 0) {
            return content
        }
        val regEx = "<[a-zA-Z]*?>([\\s\\S]*?)"
        val p = Pattern.compile(regEx)
        val m = p.matcher(content)
        if (m.find()) {
            content = m.replaceAll("")
        }
        return content
    }
    /**
     * 获取磁力链接
     */
    suspend fun getNetLinkAddress(url: String): ArrayList<String> {
        var docParent = HtmlUtil.getHtmlContent(url)
        var ll = ArrayList<String>()
        var docmenets = docParent.select("div.hash-view article.col-md-8 div.panel-body ul li")
        for (docItem in docmenets) {
            var docTitle = docItem.select("div.media-body h4.media-heading")
            var doclink = docItem.select("div.media-body a[href]")
            if (docTitle.text().equals("磁力连接：") || docTitle.text().equals("迅雷下载：")){
                ll.add("${docTitle.text()}  ${doclink.text()}")
            }
        }
        return ll
    }
}