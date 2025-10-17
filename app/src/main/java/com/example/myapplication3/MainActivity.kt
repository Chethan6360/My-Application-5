package com.example.myapplication3

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.myapplication3.ui.theme.MyApplication3Theme

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplication3Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val posts by viewModel.posts.collectAsState()
                    val isLoading by viewModel.isLoading.collectAsState()
                    val error by viewModel.error.collectAsState()

                    Box(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
                        when {
                            isLoading -> CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                            error != null -> Text(text = "Error: $error", modifier = Modifier.align(Alignment.Center))
                            else -> PostList(posts = posts)
                        }
                    }
                }
            }
        }
        viewModel.fetchPosts()
    }
}

@Composable
fun PostList(posts: List<Post>, modifier: Modifier = Modifier) {
    LazyColumn(modifier = modifier) {
        items(posts) { post ->
            Text(text = post.title, modifier = Modifier.padding())
        }
    }
}