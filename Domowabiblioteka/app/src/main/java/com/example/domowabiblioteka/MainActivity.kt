package com.example.domowabiblioteka

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Singleton.prepareSingleton(this.applicationContext)
    }

    fun openSearch(view: View){
        val intent = Intent(this, SearchActivity::class.java);
        startActivity(intent)
    }

    fun openCollection(view: View){
        val intent = Intent(this, CollectionActivity::class.java);
        startActivity(intent)
    }

    fun openScan(view: View){
        val intent = Intent(this, BarcodeActivity::class.java);
        startActivity(intent)
    }
}