package com.example.claseterremotoapi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class DetailActivity : AppCompatActivity() {

    private lateinit var tvTitle: TextView
    private lateinit var tvLat: TextView
    private lateinit var tvLong: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val quake = intent.getParcelableExtra<Feature>("feature")

        tvTitle = findViewById(R.id.textViewDescription)
        tvLat = findViewById(R.id.textViewLat)
        tvLong = findViewById(R.id.textViewLong)

        tvTitle.text = quake?.properties?.place
        tvLat.text = "latitud: " + quake!!.geometry.coordinates[0].toString()
        tvLong.text = "longitud: " + quake.geometry.coordinates[1].toString()
    }
}