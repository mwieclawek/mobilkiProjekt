import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.example.domowabiblioteka.BookInfo
import com.example.domowabiblioteka.R
import com.squareup.picasso.Picasso
import java.util.ArrayList
class BookAdapter(bookInfoArrayList:ArrayList<BookInfo>, mcontext:Context):RecyclerView.Adapter<BookAdapter.BookViewHolder>() {
    private val bookInfoArrayList:ArrayList<BookInfo>
    private val mcontext:Context

    val getItemCount:Int
        get() {
            return bookInfoArrayList.size
        }
    init{
        this.bookInfoArrayList = bookInfoArrayList
        this.mcontext = mcontext
    }
    @NonNull
    override fun onCreateViewHolder(@NonNull parent:ViewGroup, viewType:Int):BookViewHolder {
        // inflating our layout for item of recycler view item.
        val view = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_rv_item, parent, false)
        return BookViewHolder(view)
    }
    override fun onBindViewHolder(@NonNull holder:BookViewHolder, position:Int) {
        // inside on bind view holder method we are
        // setting ou data to each UI component.
        val bookInfo = bookInfoArrayList.get(position)
        holder.nameTV.setText(bookInfo.title)
        holder.publisherTV.setText(bookInfo.publisher)
        holder.pageCountTV.setText("No of Pages : " + bookInfo.pages)
        holder.dateTV.setText(bookInfo.publishedDate)
        // below line is use to set image from URL in our image view.
        Picasso.get().load(bookInfo.thumbnail).into(holder.bookIV)
        // below line is use to add on click listener for our item of recycler view.
        holder.itemView.setOnClickListener(object: View.OnClickListener {
            override fun onClick(v:View) {
                // inside on click listener method we are calling a new activity
                // and passing all the data of that item in next intent.
                val i = Intent(mcontext, BookDetails::class.java)
                i.putExtra("title", bookInfo.title)
                i.putExtra("subtitle", bookInfo.subtitle)
                i.putExtra("authors", bookInfo.authors)
                i.putExtra("publisher", bookInfo.publisher)
                i.putExtra("publishedDate", bookInfo.publishedDate)
                i.putExtra("description", bookInfo.descprition)
                i.putExtra("pageCount", bookInfo.pages)
                i.putExtra("thumbnail", bookInfo.thumbnail)
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
        internal var bookIV:ImageView
        init{
            nameTV = itemView.findViewById(R.id.idTVBookTitle)
            publisherTV = itemView.findViewById(R.id.idTVpublisher)
            pageCountTV = itemView.findViewById(R.id.idTVPageCount)
            dateTV = itemView.findViewById(R.id.idTVDate)
            bookIV = itemView.findViewById(R.id.idIVbook)
        }
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }
}