package com.example.domowabiblioteka

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteException

//creating the database logic, extending the SQLiteOpenHelper base class
class DatabaseHandler(context: Context): SQLiteOpenHelper(context,DATABASE_NAME,null,DATABASE_VERSION) {
    companion object {
        private val DATABASE_VERSION = 1
        private val DATABASE_NAME = "BookDatabase"
        private val TABLE_BOOKS = "BookTable"
        private val KEY_TITLE = "title"
        private val KEY_SUBTITLE = "subtitle"
        private val KEY_PUBLISHER = "publisher"
        private val KEY_PUBLISHED_DATE = "publishedDate"
        private val KEY_DESCRIPTION = "description"
        private val KEY_PAGECOUNT = "pagecount"
        private val KEY_THUMBNAIL = "thumbnail"
        private val KEY_PREVIEWLINK = "previewlink"
        private val KEY_INFOLINK = "infolink"
        private val KEY_BUYLINK = "buylink"
    }
    override fun onCreate(db: SQLiteDatabase?) {
        //creating table with fields
        val CREATE_BOOKS_TABLE = ("CREATE TABLE " + TABLE_BOOKS + "("
                + KEY_TITLE + " TEXT," + KEY_SUBTITLE + " TEXT,"
                + KEY_PUBLISHER + " TEXT," + KEY_PUBLISHED_DATE + " TEXT,"
                + KEY_DESCRIPTION + " TEXT," + KEY_PAGECOUNT + " INTEGER,"
                + KEY_THUMBNAIL + " TEXT," + KEY_PREVIEWLINK + " TEXT,"
                + KEY_INFOLINK + " TEXT," + KEY_BUYLINK + " TEXT" + ")")
        db?.execSQL(CREATE_BOOKS_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS " + TABLE_BOOKS)
        onCreate(db)
    }

    //method to insert data
    fun addBook(book: BookInfo){
        val db = this.writableDatabase
        val contentValues = ContentValues()

        contentValues.put(KEY_TITLE, book.title)
        contentValues.put(KEY_SUBTITLE, book.subtitle)
        contentValues.put(KEY_PUBLISHER, book.publisher)
        contentValues.put(KEY_PUBLISHED_DATE, book.publishedDate)
        contentValues.put(KEY_DESCRIPTION, book.description)
        contentValues.put(KEY_PAGECOUNT, book.pages)
        contentValues.put(KEY_THUMBNAIL, book.thumbnail)
        contentValues.put(KEY_PREVIEWLINK, book.previewLink)
        contentValues.put(KEY_INFOLINK, book.infoLink)
        contentValues.put(KEY_BUYLINK, book.buyLink)
        // Inserting Row
        db.insert(TABLE_BOOKS, null, contentValues)
        //2nd argument is String containing nullColumnHack
        db.close() // Closing database connection
    }

    //method to read data
    fun viewBooks():ArrayList<BookInfo>{
        val bookList:ArrayList<BookInfo> = ArrayList<BookInfo>()
        val selectQuery = "SELECT  * FROM $TABLE_BOOKS"
        val db = this.readableDatabase
        var cursor: Cursor? = null
        try{
            cursor = db.rawQuery(selectQuery, null)
        }catch (e: SQLiteException) {
            db.execSQL(selectQuery)
            return ArrayList()
        }
        var bookTitle: String
        var bookSubtitle: String
        var bookPublisher: String
        var bookPublishedDate: String
        var bookDescription: String
        var bookPages: Int
        var bookThumbnail: String
        var bookPreviewLink: String
        var bookInfoLink: String
        var bookBuyLink: String

        if (cursor.moveToFirst()) {
            do {
                bookTitle = cursor.getString(cursor.getColumnIndex(KEY_TITLE))
                bookSubtitle = cursor.getString(cursor.getColumnIndex(KEY_SUBTITLE))
                bookPublisher = cursor.getString(cursor.getColumnIndex(KEY_PUBLISHER))
                bookPublishedDate = cursor.getString(cursor.getColumnIndex(KEY_PUBLISHED_DATE))
                bookDescription = cursor.getString(cursor.getColumnIndex(KEY_DESCRIPTION))
                bookThumbnail = cursor.getString(cursor.getColumnIndex(KEY_THUMBNAIL))
                bookPreviewLink = cursor.getString(cursor.getColumnIndex(KEY_PREVIEWLINK))
                bookInfoLink = cursor.getString(cursor.getColumnIndex(KEY_INFOLINK))
                bookBuyLink = cursor.getString(cursor.getColumnIndex(KEY_BUYLINK))
                bookPages = cursor.getInt(cursor.getColumnIndex(KEY_PAGECOUNT))

                val bookInfo= BookInfo(bookTitle, bookSubtitle, ArrayList(), bookPublisher, bookPublishedDate,
                        bookDescription, bookPages, bookThumbnail, bookPreviewLink, bookInfoLink, bookBuyLink)

                bookList.add(bookInfo)
            } while (cursor.moveToNext())
        }
        return bookList
    }

    //method to delete data
    fun deleteBook(book: BookInfo):Int{
        val db = this.writableDatabase
        val contentValues = ContentValues()

        contentValues.put(KEY_TITLE, book.title)
        contentValues.put(KEY_SUBTITLE, book.subtitle)
        contentValues.put(KEY_PUBLISHER, book.publisher)
        contentValues.put(KEY_PUBLISHED_DATE, book.publishedDate)
        contentValues.put(KEY_DESCRIPTION, book.description)
        contentValues.put(KEY_PAGECOUNT, book.pages)
        contentValues.put(KEY_THUMBNAIL, book.thumbnail)
        contentValues.put(KEY_PREVIEWLINK, book.previewLink)
        contentValues.put(KEY_INFOLINK, book.infoLink)
        contentValues.put(KEY_BUYLINK, book.buyLink)
        // Deleting Row
        val success = db.delete(TABLE_BOOKS, "$KEY_TITLE='${e(book.title)}' AND $KEY_SUBTITLE='${e(book.subtitle)}' AND" +
                " $KEY_PUBLISHER='${e(book.publisher)}' AND $KEY_PUBLISHED_DATE='${e(book.publishedDate)}' AND $KEY_DESCRIPTION='${e(book.description)}' AND" +
                " $KEY_PAGECOUNT='${book.pages}' AND $KEY_THUMBNAIL='${e(book.thumbnail)}'",null) //AND $KEY_PREVIEWLINK='${e(book.previewLink)}' AND " +
                //"$KEY_INFOLINK='${e(book.infoLink)}' AND $KEY_BUYLINK='${e(book.buyLink)}'",null)
        //2nd argument is String containing nullColumnHack
        db.close() // Closing database connection
        return success
    }

    // escapes '' characters
    fun e(str: String):String{
        return str.replace("'","''")
    }

    //method to check if book exists in database
    fun checkIfBookExists(book: BookInfo):Boolean{
        val db = this.readableDatabase
        var cursor: Cursor? = null

        // everything must be equal except links, they are different depending on query used to search them
        val selectQuery = "SELECT * FROM $TABLE_BOOKS WHERE $KEY_TITLE='${e(book.title)}' AND $KEY_SUBTITLE='${e(book.subtitle)}' AND" +
                " $KEY_PUBLISHER='${e(book.publisher)}' AND $KEY_PUBLISHED_DATE='${e(book.publishedDate)}' AND $KEY_DESCRIPTION='${e(book.description)}' AND" +
                " $KEY_PAGECOUNT='${book.pages}' AND $KEY_THUMBNAIL='${e(book.thumbnail)}'" //AND $KEY_PREVIEWLINK='${e(book.previewLink)}' AND " +
                //"$KEY_INFOLINK='${e(book.infoLink)}' AND $KEY_BUYLINK='${e(book.buyLink)}'"

        cursor = db.rawQuery(selectQuery, null)

        return cursor.moveToFirst()
    }
}