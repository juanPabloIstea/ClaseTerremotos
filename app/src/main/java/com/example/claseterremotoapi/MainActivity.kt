package com.example.claseterremotoapi

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private lateinit var tvTitle: TextView
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: Adapter
    private var listQuakes = mutableListOf<Feature>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerQuakes)
        tvTitle = findViewById(R.id.textViewTitle)

        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = Adapter(listQuakes)
        recyclerView.adapter = adapter

        adapter.onItemClickListener = { feature ->
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra("feature", feature)
            startActivity(intent)
        }

        getQuakes()
    }

    private fun getQuakes() {
        CoroutineScope(Dispatchers.IO).launch {
            val call = getRetrofit().create(ApiService::class.java).getQuakesSigMonth(SIGNIFICANT_QUAKES_MONTH)
            val response = call.body()

            runOnUiThread {
                if (call.isSuccessful) {
                    val quakes = response?.features
                    quakes?.forEach {
                        listQuakes.add(it)
                    }
                    val metadata = response?.metadata?.title
                    tvTitle.text = metadata

                    adapter.notifyDataSetChanged()
                }
            }
        }
    }

    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    companion object {
        const val BASE_URL = "https://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/"
        const val ALL_QUAKES_LAST_DAY = "all_day.geojson"
        const val SIGNIFICANT_QUAKES_MONTH = "significant_month.geojson"
    }
}