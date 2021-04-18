package com.example.domowabiblioteka

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
import org.json.JSONObject
import java.util.ArrayList
class CollectionActivity:AppCompatActivity() {
    // creating variables for our request queue,
    // array list, progressbar, edittext,
    // image button and our recycler view.

    lateinit var mRecyclerView:RecyclerView
    lateinit var mAdapter: BookAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_collection)
        // initializing our views.
        mRecyclerView = findViewById(R.id.idRVBooks)
        mAdapter = BookAdapter(ArrayList<BookInfo>(), this)

        mRecyclerView.setLayoutManager(LinearLayoutManager(this@CollectionActivity, RecyclerView.VERTICAL, false))
        mRecyclerView.setAdapter(mAdapter)

        getBooksInfo()

    }

    private fun getBooksInfo() {
        val bookInfoArrayList = Singleton.getDatabaseHandler().viewBooks()
        mAdapter.bookInfoArrayList = bookInfoArrayList
        mAdapter.notifyDataSetChanged()

//        for (book in bookInfoArrayList){
//            println(book.buyLink + " " + book.title + " " + book.description + " " + book.infoLink + " " + book.pages + " " +
//                    book.previewLink + " " + book.publishedDate + " " + book.publisher + " " + book.subtitle + " " + book.thumbnail)
//        }
    }

    override fun onResume() {
        super.onResume()
        getBooksInfo()
    }
}

