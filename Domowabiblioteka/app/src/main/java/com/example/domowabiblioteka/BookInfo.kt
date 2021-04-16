package com.example.domowabiblioteka

class BookInfo(
    title: String,
    subtitle: String,
    authors: ArrayList<String>,
    publisher: String,
    publishedDate: String,
    description: String,
    pages: Int,
    thumbnail: String,
    previewLink: String,
    infoLink: String,
    buyLink: String
) {
    lateinit var title:String
    lateinit var subtitle:String
    lateinit var authors: ArrayList<String>
    lateinit var publisher: String
    lateinit var publishedDate:String
    lateinit var descprition:String
    lateinit var thumbnail: String
    var pages=0

}