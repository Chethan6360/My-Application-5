package com.example.myapplication3

import android.app.Activity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun InAppPurchaseScreen(billingViewModel: BillingViewModel = viewModel()) {
    val products by billingViewModel.products.collectAsState()
    val purchaseComplete by billingViewModel.purchaseComplete.collectAsState()
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (purchaseComplete) {
            Text("Purchase complete! You now have access to the premium feature.")
        } else {
            products.forEach { product ->
                Button(onClick = { billingViewModel.launchPurchaseFlow(context as Activity, product) }) {
                    Text("Buy ${product.name}")
                }
            }
        }
    }
}