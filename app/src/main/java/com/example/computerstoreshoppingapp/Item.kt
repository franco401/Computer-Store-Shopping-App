package com.example.computerstoreshoppingapp

import androidx.annotation.DrawableRes

class Item {
    var ID: String = ""
    var Name: String = ""
    var Price: Double = 0.0
    var Qty: Int = 0

    //image resource to render
    @DrawableRes
    var Image: Int = 0

    var Condition: String = "New"
    var Description: String = ""
    var Rating: Double = 0.0

    //empty constructor with default values initialized above
    constructor()

    constructor(ID: String, Name: String, Price: Double, Qty: Int, @DrawableRes Image: Int) {
        this.ID = ID
        this.Name = Name
        this.Price = Price
        this.Qty = Qty
        this.Image = Image
    }
}