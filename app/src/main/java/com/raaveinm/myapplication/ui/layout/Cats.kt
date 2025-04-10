package com.raaveinm.myapplication.ui.layout

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.raaveinm.myapplication.restapi.RetrofitInstance
import kotlinx.coroutines.launch


@Composable
fun CatScreen(
    modifier: Modifier = Modifier
) {
    var imageUrl by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val coroutineScope = rememberCoroutineScope()

    fun loadCatImage() {
        isLoading = true
        errorMessage = null
        coroutineScope.launch {
            try {
                val response = RetrofitInstance.api.getRandomCat()
                if (response.isSuccessful && response.body() != null) {
                    imageUrl = response.body()!!.relativeUrl
                } else {
                    errorMessage = "Ошибка: ${response.code()} ${response.message()}"
                    Log.e("CatApi", "Ошибка ответа: ${response.code()} ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                errorMessage = "Ошибка сети: ${e.message}"
                Log.e("CatApi", "Исключение: ", e)
            } finally {
                isLoading = false
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (imageUrl != null) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(imageUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = "randomCat",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .padding(bottom = 16.dp),
                contentScale = ContentScale.Fit
            )
        } else {
            Box(modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .padding(bottom = 16.dp)) {
                Text("press for kitty", modifier = Modifier.align(Alignment.Center))
            }
        }

        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.padding(bottom = 16.dp))
        }

        if (errorMessage != null) {
            Text(
                text = errorMessage!!,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }

        Button(onClick = { loadCatImage() }, enabled = !isLoading) {
            Text(if (isLoading) "Loading, please wait" else "I need furry kitty")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MaterialTheme {
        CatScreen()
    }
}