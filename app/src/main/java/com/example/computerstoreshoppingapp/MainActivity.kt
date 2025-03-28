package com.example.computerstoreshoppingapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBarColors

//need for screen navigation
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

//needed for mutableStateOf() function
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

//used for very basic authentication
var loggedIn: Boolean = false

//to be displayed in the home screen
var globalUsername: String = ""

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

                IconButton(onClick = { /**/ }) {
                    Icon(Icons.Filled.AccountBox, contentDescription = "My Account")
                }

                IconButton(onClick = { /**/ }) {
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
                Column {
                    Row {
                        OutlinedTextField(value = searchBarValue, onValueChange = { searchBarValue = it }, label = {Text(text = "Search")}, modifier = Modifier.requiredWidth(325.dp))
                        IconButton(onClick = { /**/ }) {
                            Icon(Icons.Filled.Search, contentDescription = "Item Search")
                        }
                    }
                    Text("Display items here...")
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