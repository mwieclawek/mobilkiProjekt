package com.example.domowabiblioteka

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso
import java.util.ArrayList
class BookDetails:AppCompatActivity() {
    // creating variables for strings,text view, image views and button.
    internal lateinit var title:String
    internal lateinit var subtitle:String
    internal lateinit var publisher:String
    internal lateinit var publishedDate:String
    internal lateinit var description:String
    internal lateinit var thumbnail:String
    internal lateinit var previewLink:String
    internal lateinit var infoLink:String
    internal lateinit var buyLink:String
    internal var pageCount:Int = 0
    private lateinit var authors:ArrayList<String>
    internal lateinit var titleTV:TextView
    internal lateinit var subtitleTV:TextView
    internal lateinit var publisherTV:TextView
    internal lateinit var descTV:TextView
    internal lateinit var pageTV:TextView
    internal lateinit var publishDateTV:TextView
    internal lateinit var previewBtn:Button
    internal lateinit var buyBtn:Button
    private lateinit var bookIV:ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_details)
        // initializing our views..
        titleTV = findViewById(R.id.idTVTitle)
        subtitleTV = findViewById(R.id.idTVSubTitle)
        publisherTV = findViewById(R.id.idTVpublisher)
        descTV = findViewById(R.id.idTVDescription)
        pageTV = findViewById(R.id.idTVNoOfPages)
        publishDateTV = findViewById(R.id.idTVPublishDate)
        previewBtn = findViewById(R.id.idBtnPreview)
        buyBtn = findViewById(R.id.idBtnBuy)
        bookIV = findViewById(R.id.idIVbook)
        // getting the data which we have passed from our adapter class.
        title = getIntent().getStringExtra("title").toString()
        subtitle = getIntent().getStringExtra("subtitle").toString()
        publisher = getIntent().getStringExtra("publisher").toString()
        publishedDate = getIntent().getStringExtra("publishedDate").toString()
        description = getIntent().getStringExtra("description").toString()
        pageCount = getIntent().getIntExtra("pageCount", 0)
        thumbnail = getIntent().getStringExtra("thumbnail").toString()
        previewLink = getIntent().getStringExtra("previewLink").toString()
        infoLink = getIntent().getStringExtra("infoLink").toString()
        buyLink = getIntent().getStringExtra("buyLink").toString()
        // after getting the data we are setting
        // that data to our text views and image view.
        titleTV.setText(title)
        subtitleTV.setText(subtitle)
        publisherTV.setText(publisher)
        publishDateTV.setText("Published On : " + publishedDate)
        descTV.setText(description)
        pageTV.setText("No Of Pages : " + pageCount)
        Picasso.get().load(thumbnail).into(bookIV)
        // adding on click listener for our preview button.
        previewBtn.setOnClickListener(object: View.OnClickListener {
            override fun onClick(v:View) {
                if (previewLink.isEmpty())
                {
                    // below toast message is displayed when preview link is not present.
                    Toast.makeText(this@BookDetails, "No preview Link present", Toast.LENGTH_SHORT).show()
                    return
                }
                // if the link is present we are opening
                // that link via an intent.
                val uri = Uri.parse(previewLink)
                val i = Intent(Intent.ACTION_VIEW, uri)
                startActivity(i)
            }
        })
        // initializing on click listener for buy button.
        buyBtn.setOnClickListener(object: View.OnClickListener {
            override fun onClick(v:View) {
                if (buyLink.isEmpty())
                {
                    // below toast message is displaying when buy link is empty.
                    Toast.makeText(this@BookDetails, "No buy page present for this book", Toast.LENGTH_SHORT).show()
                    return
                }
                // if the link is present we are opening
                // the link via an intent.
                val uri = Uri.parse(buyLink)
                val i = Intent(Intent.ACTION_VIEW, uri)
                startActivity(i)
            }
        })
    }
}