package com.example.domowabiblioteka

import android.content.Context
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley.newRequestQueue

object Singleton {

    private lateinit var queue: RequestQueue

    fun prepareSingleton(context: Context) {
        if (::queue.isInitialized)
            return

        queue = newRequestQueue(context)
    }

    fun <T> enqueue(request: Request<T>){
        queue.add(request)
    }

}