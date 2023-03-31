package com.app.mydemo

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import androidx.core.widget.NestedScrollView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.androidquery.AQuery
import com.androidquery.callback.AjaxCallback
import com.androidquery.callback.AjaxStatus
import com.app.mydemo.databinding.ActivityMainBinding
import com.google.gson.Gson
import org.json.JSONObject

@SuppressLint("NotifyDataSetChanged")
class MainActivity : AppCompatActivity() {

    val TAG = "MainActivity"
    private lateinit var biding: ActivityMainBinding
    private var rvAdapter: PassengerHistoryAdapter?=null
    private lateinit var pastHistoryList : ArrayList<PassengerHistoryData>
    private var page = 1
    private var isLoadData = false
    private lateinit var aQuery: AQuery

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        biding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        aQuery = AQuery(this)

        pastHistoryList = arrayListOf()

        init()
    }

    private fun init()
    {
        Log.e(TAG, "init()")

        biding.rvPastHistory.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rvAdapter = PassengerHistoryAdapter(this@MainActivity, pastHistoryList)
        biding.rvPastHistory.adapter = rvAdapter
        biding.rvPastHistory.isNestedScrollingEnabled = false


        biding.svPast.setOnScrollChangeListener { v: NestedScrollView, scrollX, scrollY, oldScrollX, oldScrollY ->

            if (scrollY > 0 && scrollY == v[0].measuredHeight - v.measuredHeight)
            {
                Log.e(TAG, "init() scrollY:- $scrollY")
                Log.e(TAG, "init() equal:- ${v[0].measuredHeight - v.measuredHeight}")
                if (isLoadData)
                {
                    page++
                    biding.pbHistory.visibility = View.VISIBLE
                    isLoadData = false
                    callPastHistoryApi()
                }
            }
        }

        callPastHistoryApi()
    }

    private fun callPastHistoryApi() {
        Log.e(TAG, "callPastHistoryApi()")

        val url = "https://api.instantwebtools.net/v1/passenger?page=$page&size=10"

        Log.e(TAG, "callPastHistoryApi() url:- $url")

        aQuery.ajax(url, null, JSONObject::class.java, object : AjaxCallback<JSONObject>()
        {
            override fun callback(url: String?, json: JSONObject?, status: AjaxStatus?)
            {
                try {
                    Log.e(TAG, "callback() responseCode:- ${status?.code}")
                    Log.e(TAG, "callback() response:- $json")
                    val passengerHistoryBeen: PassengerHistoryBeen = Gson().fromJson(json.toString(), PassengerHistoryBeen::class.java)
                    if (passengerHistoryBeen.data.isNotEmpty())
                    {
                        isLoadData = true
                        pastHistoryList.addAll(passengerHistoryBeen.data)
                    }
                    Log.e(TAG, "callback() pastHistoryList.size:- ${pastHistoryList.size}")
                }
                catch (e: Exception)
                {
                    Log.e(TAG, "callback() exception:- ${e.message}")
                }
             finally {
                    biding.pbHistory.visibility = View.GONE
                    rvAdapter!!.notifyDataSetChanged()
                }
            }
        }.method(AQuery.METHOD_GET))
    }
}
