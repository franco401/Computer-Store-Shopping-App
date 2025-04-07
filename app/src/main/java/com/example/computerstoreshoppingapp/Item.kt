package com.example.computerstoreshoppingapp

import androidx.annotation.DrawableRes

class Item {
    var ID: String = ""
    var Name: String = ""
    var Price: Double = 0.0

    //amount in user's cart
    var Qty: Int = 0

    //amount available to buy
    var Stock: Int = 0

    //image resource to render
    @DrawableRes
    var Image: Int = 0

    var Condition: String = "New"
    var Description: String = ""
    var Rating: Double = 0.0

    //empty constructor with default values initialized above
    constructor()

    constructor(ID: String, Name: String, Price: Double, Stock: Int, @DrawableRes Image: Int) {
        this.ID = ID
        this.Name = Name
        this.Price = Price
        this.Stock = Stock
        this.Image = Image
    }
}