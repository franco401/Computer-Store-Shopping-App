package com.example.computerstoreshoppingapp

class Review {
    //item id this review belongs to
    var itemID: Int = 0

    //the review itself
    var text: String = ""

    //1 to 5
    var rating: Int = 0

    constructor(itemID: Int, text: String, rating: Int) {
        this.itemID = itemID
        this.text = text
        this.rating = rating
    }
}