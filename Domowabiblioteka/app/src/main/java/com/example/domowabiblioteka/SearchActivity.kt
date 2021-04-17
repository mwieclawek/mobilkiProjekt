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
class SearchActivity:AppCompatActivity() {
    // creating variables for our request queue,
    // array list, progressbar, edittext,
    // image button and our recycler view.
     lateinit var mRequestQueue:RequestQueue
     lateinit var bookInfoArrayList:ArrayList<BookInfo>
     lateinit var progressBar:ProgressBar
     lateinit var searchEdt:EditText
     lateinit var mRecyclerView:RecyclerView
     lateinit var mAdapter: BookAdapter
     lateinit var searchBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        // initializing our views.
        progressBar = findViewById(R.id.idLoadingPB)
        searchEdt = findViewById(R.id.idEdtSearchBooks)
        searchBtn = findViewById(R.id.idBtnSearch)
        mRecyclerView = findViewById(R.id.idRVBooks)
        mAdapter = BookAdapter(ArrayList<BookInfo>(), this)

        mRecyclerView.setLayoutManager(LinearLayoutManager(this@SearchActivity, RecyclerView.VERTICAL, false))
        mRecyclerView.setAdapter(mAdapter)

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
        else
        getBooksInfo(searchEdt.getText().toString())

    }

    private fun getBooksInfo(query:String) {
        bookInfoArrayList = ArrayList<BookInfo>()

        mRequestQueue = Volley.newRequestQueue(this@SearchActivity)

        mRequestQueue.getCache().clear()
        var url = "https://www.googleapis.com/books/v1/volumes?q=" + query
        url = url.replace(" ", "%20"); // encode spaces

        val queue = Volley.newRequestQueue(this@SearchActivity)

        val booksObjrequest = JsonObjectRequest(Request.Method.GET, url, null, object:Response.Listener<JSONObject> {
            override fun onResponse(response:JSONObject) {
                progressBar.setVisibility(View.GONE)
                try
                {
                    val itemsArray = response.getJSONArray("items")
                    for (i in 0 until itemsArray.length())
                    {
                        val itemsObj = itemsArray.getJSONObject(i)
                        val volumeObj = itemsObj.getJSONObject("volumeInfo")
                        val title = volumeObj.optString("title")
                        val subtitle = volumeObj.optString("subtitle")
                        val authorsArray = volumeObj.optJSONArray("authors")
                        val publisher = volumeObj.optString("publisher")
                        val publishedDate = volumeObj.optString("publishedDate")
                        val description = volumeObj.optString("description")
                        val pageCount = volumeObj.optInt("pageCount")
                        val imageLinks = volumeObj.optJSONObject("imageLinks")
                        val thumbnail = imageLinks?.optString("thumbnail") ?: ""
                        val previewLink = volumeObj.optString("previewLink")
                        val infoLink = volumeObj.optString("infoLink")
                        val saleInfoObj = itemsObj.optJSONObject("saleInfo")
                        val buyLink = saleInfoObj?.optString("buyLink") ?: ""
                        val authorsArrayList = ArrayList<String>()
                        if (authorsArray != null)
                        {
                            for (j in 0 until authorsArray.length())
                            {
                                authorsArrayList.add(authorsArray.optString(i))
                            }
                        }

                        val bookInfo = BookInfo(title, subtitle, authorsArrayList, publisher, publishedDate, description, pageCount, thumbnail, previewLink, infoLink, buyLink)

                        bookInfoArrayList.add(bookInfo)


                    }
                    mAdapter.bookInfoArrayList = bookInfoArrayList
                    mAdapter.notifyDataSetChanged()
                }
                catch (e:JSONException) {
                    e.printStackTrace()
                    // displaying a toast message when we get any error from API
                    Toast.makeText(this@SearchActivity, "No Data Found" + e, Toast.LENGTH_SHORT).show()
                }
            }
        }, object:Response.ErrorListener {
            override fun onErrorResponse(error:VolleyError) {
                // also displaying error message in toast.
                Toast.makeText(this@SearchActivity, "Error found is " + error, Toast.LENGTH_SHORT).show()
            }
        })
        // at last we are adding our json object
        // request in our request queue.
        queue.add(booksObjrequest)
    }
}

