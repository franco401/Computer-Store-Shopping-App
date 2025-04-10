package com.example.computerstoreshoppingapp

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.computerstoreshoppingapp.ui.theme.ComputerStoreShoppingAppTheme

import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton

//need for screen navigation
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

//needed for mutableStateOf() function
import androidx.compose.runtime.setValue


import androidx.compose.ui.Alignment
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

//used for very basic authentication
var loggedIn: Boolean = false

//to be displayed in the home screen
var globalUsername: String = ""

//currently selected item when item clicks on an item in the home screen to view
var currentItem: Item = Item()

//arrays of items for show in home screen (more will be added later)
var laptops = arrayOf(
    Item("AN5", "Acer Nitro 5", 700.00, 100, R.drawable.acer_nitro_5_laptop),
    Item("AN5", "Acer Nitro 5", 700.00, 100, R.drawable.acer_nitro_5_laptop),
)

var smartPhones = arrayOf(
    Item("SGS25", "Samsung Galaxy S25", 860.00, 50, R.drawable.samsung_galaxy_s25),
    Item("SGS25", "Samsung Galaxy S25", 860.00, 50, R.drawable.samsung_galaxy_s25)
)

//user's shopping cart
var cartItems = ArrayList<Item>()

/*
* used to keep track of what items are in the user's cart
* to prevent duplicate Item objects in cartItems
* */
var itemsAddedToCart = ArrayList<String>()

//list of user's purchased items
var purchasedItems = ArrayList<Item>()

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComputerStoreShoppingAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    //import NavController and remember nav controller to navigate between screens
                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination = "homeScreen") {
                        //pass in navController so that each screen can go to other screens
                        composable("loginScreen") {
                            loginScreen(navController)
                        }
                        composable("signUpScreen") {
                            signUpScreen(navController)
                        }
                        composable("homeScreen") {
                            homeScreen(navController)
                        }
                        composable("accountScreen") {
                            accountScreen(navController)
                        }
                        composable("viewItemScreen") {
                            //also pass in MainActivity's Context to access toast messages
                            viewItemScreen(navController, super.getBaseContext())
                        }
                        composable("shoppingCartScreen") {
                            //also pass in MainActivity's Context to access toast messages
                            shoppingCartScreen(navController, super.getBaseContext())
                        }
                        composable("checkOutScreen") {
                            //also pass in MainActivity's Context to access toast messages
                            checkOutScreen(navController, super.getBaseContext())
                        }
                        composable("purchasesScreen") {
                            //also pass in MainActivity's Context to access toast messages
                            purchasesScreen(navController, super.getBaseContext())
                        }
                    }
                }
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun signUpScreen(navController: NavController) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column {
        Text(text = "Create your account")

        OutlinedTextField(value = username, onValueChange = {username = it}, label = {Text(text = "Username")})
        OutlinedTextField(value = password, onValueChange = {password = it}, label = {Text(text = "Password")})

        //store username and password to local db later using Room library
        Button(onClick = { navController.navigate("loginScreen") }) {
            Text(text = "Sign Up")
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun loginScreen(navController: NavController) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column {
        Text(text = "Login to your account")

        OutlinedTextField(value = username, onValueChange = {username = it}, label = {Text(text = "Username")})
        OutlinedTextField(value = password, onValueChange = {password = it}, label = {Text(text = "Password")})

        //clicking this button will store currently typed username to globalUsername variable and set loggedIn to true
        Button(onClick = { globalUsername = username; loggedIn = true; navController.navigate("homeScreen") }) {
            Text(text = "Login")
        }

        Button(onClick = { navController.navigate("signUpScreen") }) {
            Text(text = "Make an account")
        }

        //goes to home screen with globalUser as empty string and loggedIn set to false
        Button(onClick = { globalUsername = ""; loggedIn = false; navController.navigate("homeScreen") }) {
            Text(text = "Continue as guest")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun homeScreen(navController: NavController) {
    //displays username if logged in
    var welcomeMessage: String = ""

    var searchBarValue by remember { mutableStateOf("") }

    //displays username entered from login screen if logged in
    if (loggedIn) {
        welcomeMessage = "Welcome, $globalUsername!"
    } else {
        welcomeMessage = "Welcome, guest!"
    }

    //builds UI with the top and bottom bars and the screen's content in between
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(welcomeMessage) }
            )
        },
        bottomBar = {BottomAppBar(
            actions = {
                IconButton(onClick = { /**/ }) {
                    Icon(Icons.Filled.Home, contentDescription = "Home")
                }

                IconButton(onClick = { navController.navigate("accountScreen") }) {
                    Icon(Icons.Filled.AccountBox, contentDescription = "My Account")
                }

                IconButton(onClick = { navController.navigate("shoppingCartScreen") }) {
                    Icon(Icons.Filled.ShoppingCart, contentDescription = "My Cart")
                }

                IconButton(onClick = { navController.navigate("purchasesScreen") }) {
                    Icon(Icons.Filled.Favorite, contentDescription = "My Purchases")
                }
            }
        )},

        //the content in the middle of the top and bottom bars
        content = { innerPadding ->
            Box(
                modifier = Modifier
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Column {
                    Row {
                        OutlinedTextField(value = searchBarValue, onValueChange = { searchBarValue = it }, label = {Text(text = "Search")}, modifier = Modifier.requiredWidth(325.dp))
                        IconButton(onClick = { /**/ }) {
                            Icon(Icons.Filled.Search, contentDescription = "Item Search")
                        }
                    }
                    //a column that stacks rows of item types
                    Column {
                        Text("Laptops")

                        //row of laptops
                        Row (
                            Modifier.horizontalScroll(rememberScrollState(0))
                            ) {
                            for (item in laptops) {
                                //clickable image
                                Image(painter = painterResource(item.Image), contentDescription = null, modifier = Modifier
                                    .width(100.dp)
                                    .then(Modifier.height(100.dp))
                                    .then(Modifier.clickable {
                                        currentItem = item; navController.navigate("viewItemScreen")
                                    }))
                            }
                        }
                        Text("Smartphones")

                        //row of smartphones
                        Row (
                            Modifier.horizontalScroll(rememberScrollState(0))
                        ) {
                            for (item in smartPhones) {
                                //clickable image
                                Image(painter = painterResource(item.Image), contentDescription = null, modifier = Modifier
                                    .width(100.dp)
                                    .then(Modifier.height(100.dp))
                                    .then(Modifier.clickable {
                                        currentItem = item; navController.navigate("viewItemScreen")
                                    }))
                            }
                        }
                    }
                }
            }
        }
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun accountScreen(navController: NavController) {
    var username by remember { mutableStateOf("") }

    //builds UI with the top and bottom bars and the screen's content in between
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Your Account") }
            )
        },
        bottomBar = {BottomAppBar(
            actions = {
                IconButton(onClick = { navController.navigate("homeScreen") }) {
                    Icon(Icons.Filled.Home, contentDescription = "Home")
                }

                IconButton(onClick = { /**/ }) {
                    Icon(Icons.Filled.AccountBox, contentDescription = "My Account")
                }

                IconButton(onClick = { navController.navigate("shoppingCartScreen") }) {
                    Icon(Icons.Filled.ShoppingCart, contentDescription = "My Cart")
                }

                IconButton(onClick = { navController.navigate("purchasesScreen") }) {
                    Icon(Icons.Filled.Favorite, contentDescription = "My Purchases")
                }
            }
        )},

        //the content in the middle of the top and bottom bars
        content = { innerPadding ->
            Box(
                modifier = Modifier
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Column {
                    Button(onClick = { globalUsername = ""; loggedIn = false; navController.navigate("loginScreen") }) {
                        Text(text = "Logout")
                    }
                    OutlinedTextField(value = username, onValueChange = {username = it}, label = {Text(text = "Username")})
                    Button(onClick = { globalUsername = username }) {
                        Text(text = "Update Username")
                    }
                    Button(onClick = { globalUsername = ""; loggedIn = false; navController.navigate("signUpScreen") }) {
                        Text("Delete Account")
                    }
                }
            }
        }
    )
}

//a wrapper function to make a toast message appear with any given string
fun buildToastMessage(message: String, context: Context) {
    //message duration
    val duration: Int = Toast.LENGTH_SHORT

    val toast = Toast.makeText(context, message, duration)
    toast.show()
}

fun addToCartToastMessage(selectedQty: Int, context: Context) {
    //toast message to display when adding to cart
    var message: String = ""
    if (selectedQty > 1) {
        message = "Added $selectedQty items to your cart"
    } else {
        message = "Added 1 item to your cart"
    }

    //message duration
    val duration: Int = Toast.LENGTH_SHORT

    val toast = Toast.makeText(context, message, duration)
    toast.show()
}

//adds item to cart and also does checks to prevent duplicate items in cart
fun addItemToCart(itemToAdd: Item, qtyToAdd: Int) {
    var itemExists: Boolean = false

    if (itemsAddedToCart.size > 0) {
        for (item in itemsAddedToCart) {
            /*
            * checks if an item was already added
            * such as in a scenario i.e. where a user
            * adds 3 of item1 to their cart and then
            * decides to add 2 more of the same item to
            * their cart
            * */
            if (item.equals(itemToAdd.Name)) {
                itemExists = true
                break
            }
        }
    }

    if (itemExists) {
        for (item in cartItems) {
            /*
            * if the user decides to add more of an
            * item they already added i.e. 3 of item1
            * to the cart and then add 2 more of item1
            * just increment item1's qty by 2 instead of
            * adding a new item1 object to cartItems list
            * */
            if (item.Name.equals(itemToAdd.Name)) {
                item.Qty += qtyToAdd
            }
        }
    } else {
        //adding new item to cart
        itemToAdd.Qty = qtyToAdd
        cartItems.add(itemToAdd)
        itemsAddedToCart.add(itemToAdd.Name)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun viewItemScreen(navController: NavController, context: Context) {
    //used for dropdown menu
    var expanded by remember { mutableStateOf(false) }

    /*
    * list of integers from 1 to current item's max quantity
    * this will be used for the dropdown menu
    * */
    var options = List(currentItem.Stock) { "${it + 1}" }

    //the qty of an item a user wants to add to their cart
    var selectedQty by remember { mutableStateOf(1) }

    //builds UI with the top and bottom bars and the screen's content in between
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("${currentItem.Name}") }
            )
        },
        bottomBar = {BottomAppBar(
            actions = {
                IconButton(onClick = { navController.navigate("homeScreen") }) {
                    Icon(Icons.Filled.Home, contentDescription = "Home")
                }

                IconButton(onClick = { navController.navigate("accountScreen") }) {
                    Icon(Icons.Filled.AccountBox, contentDescription = "My Account")
                }

                IconButton(onClick = { navController.navigate("shoppingCartScreen") }) {
                    Icon(Icons.Filled.ShoppingCart, contentDescription = "My Cart")
                }

                IconButton(onClick = { navController.navigate("purchasesScreen") }) {
                    Icon(Icons.Filled.Favorite, contentDescription = "My Purchases")
                }
            }
        )},

        //the content in the middle of the top and bottom bars
        content = { innerPadding ->
            Box(
                modifier = Modifier
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Column {
                    Image(painter = painterResource(currentItem.Image), contentDescription = null, modifier = Modifier
                        .width(400.dp)
                        .then(Modifier.height(100.dp)))
                    Text(text = "$${currentItem.Price}")
                    Text(text = "Stock: ${currentItem.Stock}")
                    Text(text = "Rating ${currentItem.Rating}")
                    Text(text = "Condition: ${currentItem.Condition}")

                    Row {
                        IconButton(onClick = {
                            addItemToCart(currentItem, selectedQty);
                            addToCartToastMessage(selectedQty, context)
                        }) {
                            Icon(Icons.Filled.ShoppingCart, contentDescription = "Add To Cart")
                        }

                        //icon button used to expand dropdown menu
                        IconButton(onClick = { expanded = !expanded }) {
                            Icon(Icons.Default.ArrowDropDown, contentDescription = "More options")
                        }

                        //dropdown menu to pick a quantity to add to cart
                        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                            options.forEach { option ->
                                DropdownMenuItem(text = { Text(option) }, onClick = { selectedQty = option.toInt(); expanded = false })
                            }
                        }
                        Text(text = "Selected Qty: $selectedQty")
                    }
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun shoppingCartScreen(navController: NavController, context: Context) {
    if (cartItems.size == 0) {
        navController.navigate("homeScreen");
        buildToastMessage("You need to add items in your cart first", context)
    }

    //builds UI with the top and bottom bars and the screen's content in between
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Your Cart") }
            )
        },
        bottomBar = {BottomAppBar(
            actions = {
                IconButton(onClick = { navController.navigate("homeScreen") }) {
                    Icon(Icons.Filled.Home, contentDescription = "Home")
                }

                IconButton(onClick = { navController.navigate("accountScreen") }) {
                    Icon(Icons.Filled.AccountBox, contentDescription = "My Account")
                }

                IconButton(onClick = { /**/ }) {
                    Icon(Icons.Filled.ShoppingCart, contentDescription = "My Cart")
                }

                IconButton(onClick = { navController.navigate("purchasesScreen") }) {
                    Icon(Icons.Filled.Favorite, contentDescription = "My Purchases")
                }
            }
        )},

        //the content in the middle of the top and bottom bars
        content = { innerPadding ->
            Box(
                modifier = Modifier
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Column {
                    for (item in cartItems) {
                        Row {
                            Image(painter = painterResource(item.Image), contentDescription = null, modifier = Modifier
                                .width(100.dp)
                                .then(Modifier.height(100.dp)))
                            Column {
                                Text(text = "${item.Name}")
                                Text(text = "$${item.Price}")
                            }
                            Column {
                                Text(text = "Qty")
                                Text(text = "${item.Qty}")
                            }
                            IconButton(onClick = {
                                //removing item from cart
                                cartItems.remove(item);
                                itemsAddedToCart.remove(item.Name);
                                buildToastMessage("Removed ${item.Name}", context);
                                if (cartItems.size == 0) navController.navigate("homeScreen");
                            }) {
                                Icon(Icons.Filled.Clear, contentDescription = "Remove")
                            }
                        }
                    }
                    Button(onClick = { navController.navigate("checkOutScreen") }) {
                        Text(text = "Go To Checkout")
                    }
                }
            }
        }
    )
}

/*
* add each item in cartItems to purchasedItems
* then empty out the cartItems list
* */
fun addToPurchasedItems(cartItems: ArrayList<Item>) {
    for (item in cartItems) {
        purchasedItems.add(item)
    }
    cartItems.clear()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun checkOutScreen(navController: NavController, context: Context) {
    var total: Double = 0.0
    var tax: Double = 0.0

    //price change after logged in user applies coupon code
    var totalAfterCoupon by remember { mutableStateOf(0.0) }

    var couponCode by remember { mutableStateOf("") }

    //builds UI with the top and bottom bars and the screen's content in between
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Checkout") }
            )
        },
        bottomBar = {BottomAppBar(
            actions = {
                IconButton(onClick = { navController.navigate("homeScreen") }) {
                    Icon(Icons.Filled.Home, contentDescription = "Home")
                }

                IconButton(onClick = { navController.navigate("accountScreen") }) {
                    Icon(Icons.Filled.AccountBox, contentDescription = "My Account")
                }

                IconButton(onClick = { navController.navigate("shoppingCartScreen") }) {
                    Icon(Icons.Filled.ShoppingCart, contentDescription = "My Cart")
                }

                IconButton(onClick = { navController.navigate("purchasesScreen") }) {
                    Icon(Icons.Filled.Favorite, contentDescription = "My Purchases")
                }
            }
        )},

        //the content in the middle of the top and bottom bars
        content = { innerPadding ->
            Box(
                modifier = Modifier
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Column {
                    for (item in cartItems) {
                        total += item.Price * item.Qty
                        Row {
                            Image(painter = painterResource(item.Image), contentDescription = null, modifier = Modifier
                                .width(100.dp)
                                .then(Modifier.height(100.dp)))
                            Column {
                                Text(text = "${item.Name}")
                                Text(text = "$${item.Price}")
                            }
                            Column {
                                Text(text = "Qty")
                                Text(text = "${item.Qty}")
                            }
                            IconButton(onClick = {
                                //removing item from cart
                                cartItems.remove(item);
                                itemsAddedToCart.remove(item.Name);
                                buildToastMessage("Removed ${item.Name}", context);
                                if (cartItems.size == 0) navController.navigate("homeScreen");
                            }) {
                                Icon(Icons.Filled.Clear, contentDescription = "Remove")
                            }
                        }
                    }
                    tax = total * 0.07
                    total += tax

                    Text(text = "Tax: $${tax}")
                    Text(text = "Total: $${total}")

                    Button(onClick = {
                        buildToastMessage("Thank you for your purchase!", context);
                        addToPurchasedItems(cartItems)
                        navController.navigate("homeScreen");
                    }) {
                        Text(text = "Purchase")
                    }
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun purchasesScreen(navController: NavController, context: Context) {
    //builds UI with the top and bottom bars and the screen's content in between
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Your Purchases") }
            )
        },
        bottomBar = {BottomAppBar(
            actions = {
                IconButton(onClick = { navController.navigate("homeScreen") }) {
                    Icon(Icons.Filled.Home, contentDescription = "Home")
                }

                IconButton(onClick = { navController.navigate("accountScreen") }) {
                    Icon(Icons.Filled.AccountBox, contentDescription = "My Account")
                }

                IconButton(onClick = { navController.navigate("shoppingCartScreen") }) {
                    Icon(Icons.Filled.ShoppingCart, contentDescription = "My Cart")
                }

                IconButton(onClick = { /**/ }) {
                    Icon(Icons.Filled.Favorite, contentDescription = "My Purchases")
                }
            }
        )},

        //the content in the middle of the top and bottom bars
        content = { innerPadding ->
            Box(
                modifier = Modifier
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                if (!loggedIn) {
                    Text(text = "You need to be logged in to view your purchases")
                } else if (purchasedItems.size == 0) {
                    Text(text = "Make purchases to view them here")
                } else {
                    Column {
                        for (item in purchasedItems) {
                            Row {
                                Image(painter = painterResource(item.Image), contentDescription = null, modifier = Modifier
                                    .width(100.dp)
                                    .then(Modifier.height(100.dp)))
                                Column {
                                    Text(text = "${item.Name}")
                                    Text(text = "$${item.Price}")
                                }
                                Column {
                                    Text(text = "Qty")
                                    Text(text = "${item.Qty}")
                                }
                                Text(text = "Purchased on 4/10/2025 at 3:12 PM")
                            }
                        }
                    }
                }
            }
        }
    )
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ComputerStoreShoppingAppTheme {
        Greeting("Android")
    }
}