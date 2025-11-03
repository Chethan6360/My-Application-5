package com.example.myapplication3

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication3.ui.theme.MyApplication3Theme
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

@Composable
fun App() {
    val auth = Firebase.auth
    val authViewModel: AuthViewModel = viewModel(factory = AuthViewModelFactory(auth))
    val mainViewModel: MainViewModel = viewModel()
    val user by authViewModel.user.collectAsState()
    var showInAppPurchaseScreen by remember { mutableStateOf(false) }

    if (user == null) {
        AuthScreen(authViewModel)
    } else {
        if (showInAppPurchaseScreen) {
            InAppPurchaseScreen()
        } else {
            Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                val posts by mainViewModel.posts.collectAsState()
                val isLoading by mainViewModel.isLoading.collectAsState()
                val error by mainViewModel.error.collectAsState()

                Column(modifier = Modifier.padding(innerPadding)) {
                    Button(onClick = { authViewModel.logout() }) {
                        Text("Logout")
                    }
                    Button(onClick = { showInAppPurchaseScreen = true }) {
                        Text("Premium Feature")
                    }
                    Box(modifier = Modifier.fillMaxSize()) {
                        when {
                            isLoading -> CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                            error != null -> Text(text = "Error: $error", modifier = Modifier.align(Alignment.Center))
                            else -> PostList(posts = posts)
                        }
                    }
                }
                mainViewModel.fetchPosts()
            }
        }
    }
}
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplication3Theme {
                App()
            }
        }
    }
}

@Composable
fun PostList(posts: List<Post>, modifier: Modifier = Modifier) {
    LazyColumn(modifier = modifier) {
        items(posts) { post ->
            Text(text = post.title, modifier = Modifier.padding(8.dp))
        }
    }
}