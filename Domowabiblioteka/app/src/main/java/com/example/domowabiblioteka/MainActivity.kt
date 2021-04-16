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
class MainActivity:AppCompatActivity() {
    // creating variables for our request queue,
    // array list, progressbar, edittext,
    // image button and our recycler view.
     lateinit var mRequestQueue:RequestQueue
     lateinit var bookInfoArrayList:ArrayList<BookInfo>
     lateinit var progressBar:ProgressBar
     lateinit var searchEdt:EditText
     lateinit var searchBtn: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // initializing our views.
        progressBar = findViewById(R.id.idLoadingPB)
        searchEdt = findViewById(R.id.idEdtSearchBooks)
        searchBtn = findViewById(R.id.idBtnSearch)
        searchBtn.setOnClickListener { view ->
            func()
        }

    }

    fun func(){
        progressBar.setVisibility(View.VISIBLE)
        // checking if our edittext field is empty or not.
        if (searchEdt.getText().toString().isEmpty())
        {
            searchEdt.setError("Please enter search query")
            return
        }

        getBooksInfo(searchEdt.getText().toString())

    }

    private fun getBooksInfo(query:String) {
        bookInfoArrayList = ArrayList<BookInfo>()

        mRequestQueue = Volley.newRequestQueue(this@MainActivity)

        mRequestQueue.getCache().clear()
        val url = "https://www.googleapis.com/books/v1/volumes?q=" + query
        val queue = Volley.newRequestQueue(this@MainActivity)

        val booksObjrequest = JsonObjectRequest(Request.Method.GET, url, null, object:Response.Listener<JSONObject> {
            override fun onResponse(response:JSONObject) {
                progressBar.setVisibility(View.GONE)
                // inside on response method we are extracting all our json data.
                try
                {
                    val itemsArray = response.getJSONArray("items")
                    for (i in 0 until itemsArray.length())
                    {
                        val itemsObj = itemsArray.getJSONObject(i)
                        val volumeObj = itemsObj.getJSONObject("volumeInfo")
                        val title = volumeObj.optString("title")
                        val subtitle = volumeObj.optString("subtitle")
                        val authorsArray = volumeObj.getJSONArray("authors")
                        val publisher = volumeObj.optString("publisher")
                        val publishedDate = volumeObj.optString("publishedDate")
                        val description = volumeObj.optString("description")
                        val pageCount = volumeObj.optInt("pageCount")
                        val imageLinks = volumeObj.optJSONObject("imageLinks")
                        val thumbnail = imageLinks.optString("thumbnail")
                        val previewLink = volumeObj.optString("previewLink")
                        val infoLink = volumeObj.optString("infoLink")
                        val saleInfoObj = itemsObj.optJSONObject("saleInfo")
                        val buyLink = saleInfoObj.optString("buyLink")
                        val authorsArrayList = ArrayList<String>()
                        if (authorsArray.length() !== 0)
                        {
                            for (j in 0 until authorsArray.length())
                            {
                                authorsArrayList.add(authorsArray.optString(i))
                            }
                        }

                        // saving this data in our modal class.
                        val bookInfo = BookInfo(title, subtitle, authorsArrayList, publisher, publishedDate, description, pageCount, thumbnail, previewLink, infoLink, buyLink)

                        bookInfoArrayList.add(bookInfo)

                        val adapter = BookAdapter(bookInfoArrayList, this@MainActivity)

                        val linearLayoutManager = LinearLayoutManager(this@MainActivity, RecyclerView.VERTICAL, false)
                        val mRecyclerView = findViewById(R.id.idRVBooks) as RecyclerView

                        mRecyclerView.setLayoutManager(linearLayoutManager)
                        mRecyclerView.setAdapter(adapter)
                    }
                }
                catch (e:JSONException) {
                    e.printStackTrace()
                    // displaying a toast message when we get any error from API
                    Toast.makeText(this@MainActivity, "No Data Found" + e, Toast.LENGTH_SHORT).show()
                }
            }
        }, object:Response.ErrorListener {
            override fun onErrorResponse(error:VolleyError) {
                // also displaying error message in toast.
                Toast.makeText(this@MainActivity, "Error found is " + error, Toast.LENGTH_SHORT).show()
            }
        })
        // at last we are adding our json object
        // request in our request queue.
        queue.add(booksObjrequest)
    }
}

     