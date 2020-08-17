package com.luna.movieapp.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.luna.movieapp.R
import com.luna.movieapp.ui.single_movie_details.SingleMovie
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn.setOnClickListener {
            val intent = Intent(this, SingleMovie::class.java)
            intent.putExtra("id",299534 )
            this.startActivity(intent)
        }
    }
}