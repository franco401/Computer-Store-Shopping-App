package com.example.computerstoreshoppingapp

class User {
    var username: String = "guest"
    var password: String = ""

    constructor()

    constructor(username: String, password: String) {
        this.username = username
        this.password = password
    }
}