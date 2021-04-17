package com.example.domowabiblioteka

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.zxing.Result
import me.dm7.barcodescanner.zxing.ZXingScannerView
import org.json.JSONException
import org.json.JSONObject
import java.util.ArrayList

class BarcodeActivity : Activity(), ZXingScannerView.ResultHandler {
    private var mScannerView: ZXingScannerView? = null

    public override fun onCreate(state: Bundle?) {
        super.onCreate(state)

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            == PackageManager.PERMISSION_DENIED)
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), 0);

        mScannerView = ZXingScannerView(this) // Programmatically initialize the scanner view
        setContentView(mScannerView) // Set the scanner view as the content view
    }

    public override fun onResume() {
        super.onResume()
        mScannerView!!.setResultHandler(this) // Register ourselves as a handler for scan results.
        mScannerView!!.startCamera() // Start camera on resume
    }

    public override fun onPause() {
        super.onPause()
        mScannerView!!.stopCamera() // Stop camera on pause
    }

    override fun handleResult(rawResult: Result?) {

        if (rawResult != null) {
            println(rawResult.getText()) // Prints scan results
            Toast.makeText(this@BarcodeActivity, "Fetching data from internet...", Toast.LENGTH_SHORT).show()
            getBookInfo(rawResult.getText())
            //println(rawResult.getBarcodeFormat().toString()) // Prints the scan format (qrcode, pdf417 etc.)
        }

        // If you would like to resume scanning, call this method below:
        // mScannerView!!.resumeCameraPreview(this)
    }

    private fun getBookInfo(query:String) {

        var url = "https://www.googleapis.com/books/v1/volumes?q=isbn:" + query

        val booksObjrequest = JsonObjectRequest(Request.Method.GET, url, null, object:
            Response.Listener<JSONObject> {
            override fun onResponse(response: JSONObject) {

                try
                {
                    val itemsArray = response.getJSONArray("items")

                    val itemsObj = itemsArray.getJSONObject(0)
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
                            authorsArrayList.add(authorsArray.optString(j))
                        }
                    }

                    val bookInfo = BookInfo(title, subtitle, authorsArrayList, publisher, publishedDate, description, pageCount, thumbnail, previewLink, infoLink, buyLink)

                    goToDetails(bookInfo)
                }
                catch (e: JSONException) {
                    e.printStackTrace()
                    // displaying a toast message when we get any error from API
                    Toast.makeText(this@BarcodeActivity, "No Data Found " + e, Toast.LENGTH_SHORT).show()
                }
            }
        }, object: Response.ErrorListener {
            override fun onErrorResponse(error: VolleyError) {
                // also displaying error message in toast.
                Toast.makeText(this@BarcodeActivity, "Error found is " + error, Toast.LENGTH_SHORT).show()
            }
        })
        // at last we are adding our json object
        // request in our request queue.
        Singleton.enqueue(booksObjrequest)
    }

    private fun goToDetails(bookInfo: BookInfo) {
        val i = Intent(this, BookDetails::class.java)

        i.putExtra("title", bookInfo.title)
        i.putExtra("subtitle", bookInfo.subtitle)
        //i.putExtra("authors", bookInfo.authors)
        i.putExtra("publisher", bookInfo.publisher)
        i.putExtra("publishedDate", bookInfo.publishedDate)
        i.putExtra("description", bookInfo.descprition)
        i.putExtra("pageCount", bookInfo.pages)
        i.putExtra("thumbnail", bookInfo.thumbnail)
        i.putExtra("previewLink", bookInfo.previewLink)
        i.putExtra("infoLink", bookInfo.infoLink)
        i.putExtra("buyLink", bookInfo.buyLink)
        // after passing that data we are
        // starting our new intent.

        this.startActivity(i)
    }
}