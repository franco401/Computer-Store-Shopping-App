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
import androidx.compose.foundation.verticalScroll
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
import java.math.RoundingMode
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException
import java.sql.Statement
import java.sql.Timestamp

//used for very basic authentication
var loggedIn: Boolean = false

//to be displayed in the home screen
var globalUsername: String = ""

//currently selected item when item clicks on an item in the home screen to view
var currentItem: Item = Item()

var cartItems: ArrayList<Item> = ArrayList<Item>()
var itemsAddedToCart: ArrayList<String> = ArrayList<String>()
var purchasedItems: ArrayList<Item> = ArrayList<Item>()

var allItems = arrayOf(
    Item(1, "Acer Nitro V", "Acer - Nitro V ANV15-41-R2Y3 Gaming Laptop– 15.6\" Full HD 144Hz – AMD Ryzen 5 7535HS – GeForce RTX 4050 - 16GB DDR5 – 512GB SSD - Obsidian Black", "Laptop", 949.99, 0, 100, "New", R.drawable.acer_nitro_v_laptop),
    Item(2, "ASUS TUF Gaming A15", "ASUS - TUF Gaming A15 15.6\" 144Hz FHD Gaming Laptop - AMD Ryzen 5 7535HS with 8GB Memory - NVIDIA GeForce RTX 2050 - 512GB SSD - Graphite Black", "Laptop", 699.99, 0, 100, "New", R.drawable.asus_tuf_gaming_a15_laptop),
    Item(3, "GIGABYTE G6", "GIGABYTE - G6 KF 16\" 165Hz Gaming Laptop IPS - Intel i7-13620H with 32GB RAM - NVIDIA GeForce RTX 4060 - 1TB SSD - Black," ,"Laptop", 999.99, 0, 100, "New", R.drawable.gigabyte_g6_gaming_laptop),
    Item(4, "HP OMEN 16.1", "HP OMEN - 16.1\" 165Hz Full HD Gaming Laptop - Intel Core i7 - 16GB DDR5 Memory - NVIDIA GeForce RTX 4060 - 1TB SSD - Shadow Black", "Laptop", 1149.99, 0, 100, "New", R.drawable.hp_omen_gaming_laptop),
    Item(5, "Macbook Pro M4", "Apple - MacBook Pro 14-inch Apple M4 chip Built for Apple Intelligence - 16GB Memory - 512GB SSD - Space Black", "Laptop", 1449.00, 0, 100, "New", R.drawable.macbook_pro_m4_laptop_space_black),
    Item(6, "Macbook Air M4", "Apple - MacBook Air 13-inch Apple M4 chip Built for Apple Intelligence - 16GB Memory - 256GB SSD - Midnight", "Laptop", 999.00, 0, 100, "New", R.drawable.macbook_air_m4_laptop_midnight),
    Item(7, "Macbook Air M3", "Apple - MacBook Air 13-inch Laptop - M3 chip Built for Apple Intelligence - 16GB Memory - 512GB SSD - Starlight", "Laptop", 1099.99, 0, 100, "New", R.drawable.macbook_air_m3_laptop_starlight),
    Item(8, "Macbook Air M2", "Apple - MacBook Air 13.6\" Laptop - M2 chip Built for Apple Intelligence - 8GB Memory - 512GB SSD - Space Gray", "Laptop", 799.99, 0, 100, "New", R.drawable.macbook_air_m2_laptop_space_gray),
    Item(9, "Macbook Air M1", "Apple - MacBook Air 13.3\" Laptop - Apple M1 chip - 8GB Memory - 256GB SSD - Gold", "Laptop", 479.99, 0, 100, "Refurbished", R.drawable.macbook_air_m1_laptop_gold),
    Item(10, "Samsung Galaxy S25", "Samsung - Galaxy S25 256GB (Unlocked) - Navy", "Smartphone", 779.99, 0, 100, "New", R.drawable.samsung_galaxy_s25_navy),
    //Item(11, "Samsung Galaxy S25 Ultra", "Samsung - Galaxy S25 Ultra 512GB (Unlocked) - Titanium Silverblue", "Smartphone", 1219.99, 0, 100, "New", R.drawable.samsung_galaxy_s25_ultra_titanium_silver_blue),
    Item(12, "Samsung Galaxy S24 FE", "Samsung - Galaxy S24 FE 128GB (Unlocked) - Graphite", "Smartphone", 499.99, 0, 100, "New", R.drawable.samsung_galaxy_s24_fe_graphite),
    //Item(13, "Samsung Galaxy S24 Ultra", "Samsung - Galaxy S24 Ultra 128GB (Unlocked) - Titanium Violet", "Smartphone", 1299.99, 0, 100, "New", R.drawable.samsung_galaxy_s24_ultra_titanium_violet),
    Item(14, "Google Pixel 9", "Google - Pixel 9 128GB (Unlocked) - Obsidian", "Smartphone", 649.00, 0, 100, "New", R.drawable.google_pixel_9_obsidian_black),
    Item(15, "iPhone 16", "Apple - iPhone 16 128GB - Apple Intelligence - Ultramarine (AT&T)", "Smartphone", 829.99, 0, 100, "New", R.drawable.iphone_16_ultramarine),
    //Item(16, "iPhone 16 Pro", "Apple - iPhone 16 Pro 256GB - Apple Intelligence - Black Titanium (AT&T)", "Smartphone", 1099.99, 0, 100, "New", R.drawable.iphone_16_pro_black_titanium),
    Item(17, "iPhone 15", "Apple - iPhone 15 128GB (Unlocked) - Black", "Smartphone", 729.99, 0, 100, "New", R.drawable.iphone_15_black),
    //Item(18, "iPhone 15 Pro", "Apple - iPhone 15 Pro 128GB - Apple Intelligence - Natural Titanium (AT&T)", "Smartphone", 899.99, 0, 100, "New", R.drawable.iphone_15_pro_natural_titanium),
    Item(19, "iPhone 16 Pro Max", "Apple - iPhone 16 Pro Max 256GB - Apple Intelligence - Desert Titanium (AT&T)", "Smartphone", 1199.99, 0, 100, "New", R.drawable.iphone_16_pro_max_desert_titanium),
    Item(20, "Samsung Galaxy S25 Case", "Samsung - Galaxy S25 Silicone Case - Black", "Accessory", 16.49, 0, 100, "New", R.drawable.samsung_galaxy_s25_silicone_case_black),
    //Item(21, "Samsung Galaxy S25 Ultra Case", "Samsung - Galaxy S25 Ultra Clear Case - Transparent", "Accessory", 14.99, 0, 100, "New", R.drawable.samsung_galaxy_s25_ultra_clear_case_transparent),
    Item(22, "Samsung Galaxy S24 Case", "Samsung - Galaxy S24 Standing Grip Case - Dark Violet", "Accessory", 59.99, 0, 100, "New", R.drawable.samsung_galaxy_s24_standing_grip_case_dark_violet),
    //Item(23, "Samsung Galaxy S24 Ultra Case", "Samsung - Galaxy S24 Ultra Silicone Case - Dark Violet", "Accessory", 34.99, 0, 100, "New", R.drawable.samsung_galaxy_s24_ultra_silicone_case_dark_violet),
    Item(24, "Google Pixel 9 Case", "Google - Pixel 9 / 9 Pro Case - Obsidian", "Accessory", 34.99, 0, 100, "New", R.drawable.google_pixel_9_case_obsidian),
    Item(25, "iPhone 15 Case", "OtterBox - Commuter Series Hard Shell for MagSafe for Apple iPhone 16e, Apple iPhone 15, Apple iPhone 14, and Apple iPhone 13 - Black", "Accessory", 44.99, 0, 100, "New", R.drawable.iphone_15_pro_silicone_case_black),
    //Item(26, "iPhone 15 Pro Case", "Apple - iPhone 15 Pro Silicone Case with MagSafe - Black", "Accessory", 49.99, 0, 100, "New", R.drawable.iphone_15_pro_silicone_case_black),
    //Item(27, "iPhone 16 Pro Case", "OtterBox - Symmetry Series Hard Shell for MagSafe for Apple iPhone 16 Pro - Black", "Accessory", 49.99, 0, 100, "New", R.drawable.iphone_16_pro_otterbox_symmetry_series_hard_shell_case_black),
    Item(28, "iPhone 16 Pro Max Case", "OtterBox - Defender Series Pro Hard Shell for MagSafe for Apple iPhone 16 Pro Max - Black", "Accessory", 64.99, 0, 100, "New", R.drawable.iphone_16_pro_max_otterbox_defender_series_pro_hard_shell_case),
    Item(29, "Samsung USB C Charger", "Samsung - 25W 6' USB Type C-to-USB Type C Device Cable - Black", "Accessory", 19.99, 0, 100, "New", R.drawable.samsung_usb_type_c_cable_black),
    Item(30, "Samsung USB C Wall Charger", "Samsung - Fast Charging 15W USB Type-C Wall Charger - Black", "Accessory", 10.99, 0, 100, "New", R.drawable.samsung_fast_charging_usb_type_c_wall_charger_black),
    Item(31, "Apple USB C Charger", "Apple - 240W USB-C Charge Cable (2 m) - White", "Accessory", 29.99, 0, 100, "New", R.drawable.apple_usb_type_c_cable_white),
    Item(32, "Apple USB C Power Adapter", "Apple - 20W USB-C Power Adapter - White", "Accessory", 14.99, 0, 100, "New", R.drawable.apple_usb_type_c_power_adapterwhite)
)

var reviews: ArrayList<Review> = ArrayList<Review>()
fun submitReview(navController: NavController, context: Context, itemID: Int, reviewText: String, selectedRating: Int) {
    reviews.add(Review(currentItem.id, reviewText, selectedRating))

    var sum: Double = 0.0
    var currentItemReviewCount: Int = 0

    for (review in reviews) {
        if (itemID == currentItem.id) {
            sum += review.rating
            currentItemReviewCount++
        }
    }
    currentItem.rating = (sum / currentItemReviewCount).toBigDecimal().setScale(2, RoundingMode.UP).toDouble()
    buildToastMessage("Successfully posted your item review", context)
    navController.navigate("viewItemScreen")

}


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
                            //pass in MainActivity's Context to access toast messages
                            homeScreen(navController, super.getBaseContext())
                        }
                        composable("accountScreen") {
                            accountScreen(navController, super.getBaseContext())
                        }
                        composable("viewItemScreen") {
                            viewItemScreen(navController, super.getBaseContext())
                        }
                        composable("shoppingCartScreen") {
                            shoppingCartScreen(navController, super.getBaseContext())
                        }
                        composable("checkOutScreen") {
                            checkOutScreen(navController, super.getBaseContext())
                        }
                        composable("purchasesScreen") {
                            purchasesScreen(navController, super.getBaseContext())
                        }
                        composable("writeReviewScreen") {
                            writeReviewScreen(navController, super.getBaseContext())
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
fun homeScreen(navController: NavController, context: Context) {
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

                IconButton(onClick = {
                    if (cartItems.size == 0) {
                        buildToastMessage("You need to add items in your cart first", context)
                    } else {
                        navController.navigate("shoppingCartScreen")
                    }
                }) {
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
                            for (item in allItems) {
                                if (item.category == "Laptop") {
                                    Image(painter = painterResource(item.image), contentDescription = null, modifier = Modifier
                                        .width(100.dp)
                                        .then(Modifier.height(100.dp))
                                        .then(Modifier.clickable {
                                            currentItem = item; navController.navigate("viewItemScreen")
                                        }))
                                }
                            }
                        }

                        Text("Smartphones")

                        //row of smartphones
                        Row (
                            Modifier.horizontalScroll(rememberScrollState(0))
                        ) {
                            for (item in allItems) {
                                if (item.category == "Smartphone") {
                                    Image(painter = painterResource(item.image), contentDescription = null, modifier = Modifier
                                        .width(100.dp)
                                        .then(Modifier.height(100.dp))
                                        .then(Modifier.clickable {
                                            currentItem = item; navController.navigate("viewItemScreen")
                                        }))
                                }
                            }
                        }

                        Text("Accessories")

                        //row of accessories
                        Row (
                            Modifier.horizontalScroll(rememberScrollState(0))
                        ) {
                            for (item in allItems) {
                                if (item.category == "Accessory") {
                                    Image(painter = painterResource(item.image), contentDescription = null, modifier = Modifier
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
        }
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun accountScreen(navController: NavController, context: Context) {
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

                IconButton(onClick = {
                    if (cartItems.size == 0) {
                        buildToastMessage("You need to add items in your cart first", context)
                    } else {
                        navController.navigate("shoppingCartScreen")
                    }
                }) {
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
            if (item.equals(itemToAdd.full_name)) {
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
            if (item.full_name.equals(itemToAdd.full_name)) {
                item.qty += qtyToAdd
            }
        }
    } else {
        //adding new item to cart
        itemToAdd.qty = qtyToAdd
        cartItems.add(itemToAdd)
        itemsAddedToCart.add(itemToAdd.full_name)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun writeReviewScreen(navController: NavController, context: Context) {
    //used for dropdown menu
    var expanded by remember { mutableStateOf(false) }

    var reviewText by remember { mutableStateOf("") }
    var reviewRatingOptions = List(5) { "${it + 1}" }

    var selectedRating by remember { mutableStateOf(1) }

    //builds UI with the top and bottom bars and the screen's content in between
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("${currentItem.short_name}") }
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

                IconButton(onClick = {
                    if (cartItems.size == 0) {
                        buildToastMessage("You need to add items in your cart first", context)
                    } else {
                        navController.navigate("shoppingCartScreen")
                    }
                }) {
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
                    Image(painter = painterResource(currentItem.image), contentDescription = null, modifier = Modifier
                        .width(400.dp)
                        .then(Modifier.height(100.dp)))

                    Column {
                        Row {
                            Text(text = "Select a rating: ")
                            //icon button used to expand dropdown menu
                            IconButton(onClick = { expanded = !expanded }) {
                                Icon(Icons.Default.ArrowDropDown, contentDescription = "More options")
                            }
                        }

                        //dropdown menu to pick a rating between 1-5
                        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                            reviewRatingOptions.forEach { option ->
                                DropdownMenuItem(text = { Text(option) }, onClick = { selectedRating = option.toInt(); expanded = false })
                            }
                        }

                        Text(text = "Selected Qty: $selectedRating")

                        OutlinedTextField(value = reviewText, onValueChange = {reviewText = it})
                        Button(onClick = {
                            submitReview(navController, context, currentItem.id, reviewText, selectedRating)
                        }) {
                            Text(text = "Submit Review")
                        }
                    }
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun viewItemScreen(navController: NavController, context: Context) {
    //used for dropdown menu
    var expanded by remember { mutableStateOf(false) }

    var purchaseQtyOptions = List(currentItem.stock) { "${it + 1}" }

    var selectedQty by remember { mutableStateOf(1) }

    //builds UI with the top and bottom bars and the screen's content in between
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("${currentItem.short_name}") }
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

                IconButton(onClick = {
                    if (cartItems.size == 0) {
                        buildToastMessage("You need to add items in your cart first", context)
                    } else {
                        navController.navigate("shoppingCartScreen")
                    }
                }) {
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
                    Image(painter = painterResource(currentItem.image), contentDescription = null, modifier = Modifier
                        .width(400.dp)
                        .then(Modifier.height(100.dp)))
                    Text(text = "$${currentItem.price}")
                    Text(text = "Stock: ${currentItem.stock}")
                    Text(text = "Condition: ${currentItem.condition}")
                    
                    if (currentItem.rating > 0) {
                        Text(text = "Rating: ${currentItem.rating}")
                    }

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
                            purchaseQtyOptions.forEach { option ->
                                DropdownMenuItem(text = { Text(option) }, onClick = { selectedQty = option.toInt(); expanded = false })
                            }
                        }
                        Text(text = "Selected Qty: $selectedQty")

                        Button(onClick = {
                            if (!loggedIn) {
                                buildToastMessage("You need to be logged in to write a review", context)
                            } else {
                                navController.navigate("writeReviewScreen")
                            }
                        }) {
                            Text(text = "Write a review")
                        }
                    }

                    Column(Modifier.verticalScroll(rememberScrollState(0))) {
                        for (review in reviews) {
                            if (review.itemID == currentItem.id) {
                                Text(text = "Rating: ${review.rating}")
                                Text(text = "${review.text}")
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
fun shoppingCartScreen(navController: NavController, context: Context) {
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
                            Image(painter = painterResource(item.image), contentDescription = null, modifier = Modifier
                                .width(100.dp)
                                .then(Modifier.height(100.dp)))
                            Column {
                                Text(text = "${item.short_name}")
                                Text(text = "$${item.price}")
                            }
                            Column {
                                Text(text = "Qty")
                                Text(text = "${item.qty}")
                            }
                            IconButton(onClick = {
                                //removing item from cart
                                cartItems.remove(item);
                                itemsAddedToCart.remove(item.full_name);
                                buildToastMessage("Removed ${item.full_name}", context);
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
    for (i in cartItems.indices) {
        purchasedItems.add(cartItems[i])
        purchasedItems.get(i).purchased = System.currentTimeMillis()
    }
    cartItems.clear()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun checkOutScreen(navController: NavController, context: Context) {
    var total: Double = 0.0
    var tax: Double = 0.0

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

                IconButton(onClick = {
                    if (cartItems.size == 0) {
                        buildToastMessage("You need to add items in your cart first", context)
                    } else {
                        navController.navigate("shoppingCartScreen")
                    }
                }) {
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
                        total += item.price * item.qty
                        Row {
                            Image(painter = painterResource(item.image), contentDescription = null, modifier = Modifier
                                .width(100.dp)
                                .then(Modifier.height(100.dp)))
                            Column {
                                Text(text = "${item.short_name}")
                                Text(text = "$${item.price}")
                            }
                            Column {
                                Text(text = "Qty")
                                Text(text = "${item.qty}")
                            }
                            IconButton(onClick = {
                                //removing item from cart
                                cartItems.remove(item);
                                itemsAddedToCart.remove(item.full_name);
                                buildToastMessage("Removed ${item.full_name}", context);
                                if (cartItems.size == 0) navController.navigate("homeScreen");
                            }) {
                                Icon(Icons.Filled.Clear, contentDescription = "Remove")
                            }
                        }
                    }
                    tax = total * 0.07
                    total += tax

                    tax = tax.toBigDecimal().setScale(2, RoundingMode.UP).toDouble()
                    total = total.toBigDecimal().setScale(2, RoundingMode.UP).toDouble()

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

                IconButton(onClick = {
                    if (cartItems.size == 0) {
                        buildToastMessage("You need to add items in your cart first", context)
                    } else {
                        navController.navigate("shoppingCartScreen")
                    }
                }) {
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
                                Image(painter = painterResource(item.image), contentDescription = null, modifier = Modifier
                                    .width(100.dp)
                                    .then(Modifier.height(100.dp)))
                                Column {
                                    Text(text = "${item.short_name}")
                                    Text(text = "$${item.price}")
                                    Text(text = "Qty: ${item.qty}")
                                }
                            }
                            Column {
                                //find a way to format this in the format: Month/Day/Year Hour:Minutes
                                Text(text = "Purchased on ${Timestamp(item.purchased)}")
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