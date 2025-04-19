package com.example.computerstoreshoppingapp

import androidx.annotation.DrawableRes

class Item {
    var id: Int = 0
    var short_name: String = ""
    var full_name: String = ""
    var category: String = ""
    var price: Int = 0

    //amount in user's cart
    var qty: Int = 0

    //amount available to buy
    var stock: Int = 0

    var condition: String = ""

    @DrawableRes
    var image: Int = 0

    constructor()

    constructor(id: Int, short_name: String, full_name: String, category: String, price: Int, qty: Int, stock: Int, condition: String, @DrawableRes image: Int) {
        this.id = id
        this.short_name = short_name
        this.full_name = full_name
        this.category = category
        this.price = price
        this.qty = qty
        this.stock = stock
        this.condition = condition
        this.image = image
    }
}