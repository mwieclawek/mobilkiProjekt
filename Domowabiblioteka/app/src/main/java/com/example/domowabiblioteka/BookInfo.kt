package com.example.domowabiblioteka

class BookInfo() {
    var buyLink: String = ""
    lateinit var infoLink: String
    lateinit var previewLink: String
    lateinit var description: String
    var title:String = ""
    lateinit var subtitle:String
    lateinit var authors: ArrayList<String>
    lateinit var publisher: String
    lateinit var publishedDate:String
    var descprition:String=""
    lateinit var thumbnail: String
    var pages=0
constructor(title: String,
            subtitle: String,
            authors: ArrayList<String>,
            publisher: String,
            publishedDate: String,
            description: String,
            pages: Int,
            thumbnail: String,
            previewLink: String,
            infoLink: String,
            buyLink: String) : this() {
    this.title=title
    this.subtitle=subtitle
    this.authors=authors
    this.publisher=publisher
    this.publishedDate=publishedDate
    this.description=description
    this.pages=pages
    this.thumbnail=thumbnail
    this.previewLink=previewLink
    this.infoLink=infoLink
    this.buyLink=buyLink

}
}