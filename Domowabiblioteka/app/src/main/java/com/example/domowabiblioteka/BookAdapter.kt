package com.example.domowabiblioteka

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

import com.squareup.picasso.Picasso
import java.util.ArrayList
class BookAdapter(var bookInfoArrayList:ArrayList<BookInfo>,val mcontext:Context):RecyclerView.Adapter<BookAdapter.BookViewHolder>() {


    override fun onCreateViewHolder( parent:ViewGroup, viewType:Int):BookViewHolder {
        // inflating our layout for item of recycler view item.
        val view = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_rv_item, parent, false)
        return BookViewHolder(view)
    }
    override fun onBindViewHolder( holder:BookViewHolder, position:Int) {
        // inside on bind view holder method we are
        // setting ou data to each UI component.
        val bookInfo = bookInfoArrayList.get(position)
        holder.nameTV.setText(bookInfo.title)
        holder.publisherTV.setText(bookInfo.publisher)
        holder.pageCountTV.setText("No of Pages : " + bookInfo.pages)
        holder.dateTV.setText(bookInfo.publishedDate)

        val existsInCollection = Singleton.getDatabaseHandler().checkIfBookExists(bookInfo)
        holder.inCollectionTV.text = (if (existsInCollection) "\u2713" else "\u2717")
        holder.inCollectionTV.setTextColor(if (existsInCollection) Color.GREEN else Color.RED)

        // below line is use to set image from URL in our image view.
        if (bookInfo.thumbnail.isNotEmpty())
            Picasso.get().load(bookInfo.thumbnail).into(holder.bookIV)
        // below line is use to add on click listener for our item of recycler view.
        holder.itemView.setOnClickListener(object: View.OnClickListener {
            override fun onClick(v:View) {
                // inside on click listener method we are calling a new activity
                // and passing all the data of that item in next intent.
                val i = Intent(mcontext, BookDetails::class.java)
                i.putExtra("title", bookInfo.title)
                i.putExtra("subtitle", bookInfo.subtitle)
                //i.putExtra("authors", bookInfo.authors)
                i.putExtra("publisher", bookInfo.publisher)
                i.putExtra("publishedDate", bookInfo.publishedDate)
                i.putExtra("description", bookInfo.description)
                i.putExtra("pageCount", bookInfo.pages)
                i.putExtra("thumbnail", bookInfo.thumbnail)
                i.putExtra("previewLink", bookInfo.previewLink)
                i.putExtra("infoLink", bookInfo.infoLink)
                i.putExtra("buyLink", bookInfo.buyLink)
                // after passing that data we are
                // starting our new intent.
                mcontext.startActivity(i)
            }
        })
    }
    inner class BookViewHolder(itemView:View):RecyclerView.ViewHolder(itemView) {
        // below line is use to initialize
        // our text view and image views.
        internal var nameTV:TextView
        internal var publisherTV:TextView
        internal var pageCountTV:TextView
        internal var dateTV:TextView
        internal var inCollectionTV:TextView
        internal var bookIV:ImageView
        init{
            nameTV = itemView.findViewById(R.id.idTVBookTitle)
            publisherTV = itemView.findViewById(R.id.idTVpublisher)
            pageCountTV = itemView.findViewById(R.id.idTVPageCount)
            dateTV = itemView.findViewById(R.id.idTVDate)
            bookIV = itemView.findViewById(R.id.idIVbook)
            inCollectionTV = itemView.findViewById(R.id.idTVinCollection)
        }
    }

    override fun getItemCount(): Int {
        return bookInfoArrayList.size
    }
}