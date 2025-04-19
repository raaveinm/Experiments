package com.raaveinm.myapplication


import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Album
import androidx.compose.material.icons.filled.Brush
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.DashboardCustomize
import androidx.compose.material.icons.filled.DataArray
import androidx.compose.material.icons.filled.DataSaverOff
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Lan
import androidx.compose.material.icons.filled.MoreTime
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SwapVerticalCircle
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.raaveinm.myapplication.service.Companion.ACTION_PAUSE
import com.raaveinm.myapplication.service.Companion.ACTION_PLAY
import com.raaveinm.myapplication.service.PlayerService
import com.raaveinm.myapplication.ui.layout.ButtonRow
import com.raaveinm.myapplication.ui.layout.CatScreen
import com.raaveinm.myapplication.ui.layout.DBMain
import com.raaveinm.myapplication.ui.layout.DataLayerLayout
import com.raaveinm.myapplication.ui.layout.SharedPreferencesUI
import com.raaveinm.myapplication.ui.layout.TimePickerScreen
import com.raaveinm.myapplication.ui.layout.WebViewScreenMain
import com.raaveinm.myapplication.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {

    val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()){}

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            if (ContextCompat.checkSelfPermission(this, READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                requestPermissionLauncher.launch(READ_EXTERNAL_STORAGE)
            }
            if (ContextCompat.checkSelfPermission(this, WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                requestPermissionLauncher.launch(WRITE_EXTERNAL_STORAGE)
            }
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.MANAGE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                requestPermissionLauncher.launch(android.Manifest.permission.MANAGE_EXTERNAL_STORAGE)
            }
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_MEDIA_AUDIO) != PackageManager.PERMISSION_GRANTED){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    requestPermissionLauncher.launch(android.Manifest.permission.READ_MEDIA_AUDIO)
                }
            }
            MyApplicationTheme {
                MainScreen()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        val intent = Intent(this, PlayerService::class.java)
        startService(intent)
    }
}


/**
 * How I wish, how I wish you were here
 *
 * We're just two lost souls swimming in a fishbowl,
 *
 * year after year Running over the same old ground,
 *
 * what have we found?
 *
 * The same old fears, wish you were here
 */

@Composable
fun MainScreen() {
    var localPressed by rememberSaveable { mutableIntStateOf(0) }
    var swapped by rememberSaveable { mutableIntStateOf(0) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = false,
                    onClick = { swapped = (swapped + 1) % 3 },
                    icon = { Icon(Icons.Filled.SwapVerticalCircle, contentDescription = "swap")},
                    label = { Text(text = "Swap") }
                )
                if (swapped == 0) {
                    NavigationBarItem(
                        selected = localPressed == 4,
                        onClick = { localPressed = 4 },
                        icon = { Icon(
                            (Icons.Filled.DataSaverOff),
                            contentDescription = stringResource(R.string.datalayer)
                        )},
                        label = { Text(text = stringResource(R.string.datalayer)) }
                    )
                    NavigationBarItem(
                        selected = localPressed == 5,
                        onClick = { localPressed = 5 },
                        icon = { Icon(
                            (Icons.Filled.AccountCircle),
                            contentDescription = stringResource(R.string.Preferences)
                        )},
                        label = { Text(text = stringResource(R.string.Preferences)) }
                    )
                    NavigationBarItem(
                        selected = localPressed == 6,
                        onClick = { localPressed = 6 },
                        icon = { Icon(
                            (Icons.Filled.DataArray),
                            contentDescription = stringResource(R.string.db)
                        )}
                    )
                } else if (swapped == 1) {
                    NavigationBarItem(
                        selected = localPressed == 0,
                        onClick = { localPressed = 0 },
                        icon = { Icon(
                            (Icons.Filled.AccountCircle),
                            contentDescription = stringResource(R.string.home)
                        )},
                        label = { Text(text = stringResource(R.string.home)) }
                    )
                    NavigationBarItem(
                        selected = localPressed == 2,
                        onClick = { localPressed = 2 },
                        icon = { Icon(
                            (Icons.Filled.Album),
                            contentDescription = stringResource(R.string.player)
                        )},
                        label = { Text(text = stringResource(R.string.player)) }
                    )
                    NavigationBarItem(
                        selected = localPressed == 1,
                        onClick = { localPressed = 1 },
                        icon = { Icon(
                            (Icons.Filled.Brush),
                            contentDescription = stringResource(R.string.settings)
                        )},
                        label = { Text(text = stringResource(R.string.settings)) }
                    )
                    NavigationBarItem(
                        selected = localPressed == 3,
                        onClick = { localPressed = 3 },
                        icon = {
                            Icon(
                                (Icons.Filled.Image),
                                contentDescription = stringResource(R.string.cats)
                            )
                        },
                        label = { Text(text = stringResource(R.string.cats)) }
                    )
                } else if (swapped == 2) {
                    NavigationBarItem(
                        selected = localPressed == 7,
                        onClick = { localPressed = 7 },
                        icon = { Icon(
                            (Icons.Filled.Lan),
                            contentDescription = stringResource(R.string.WebView)
                        )},
                        label = { Text(text = stringResource(R.string.WebView)) }
                    )
                    NavigationBarItem(
                        selected = localPressed == 8,
                        onClick = { localPressed = 8 },
                        icon = { Icon(
                            (Icons.Filled.NotificationsActive),
                            contentDescription = stringResource(R.string.Notifications)
                        )},
                        label = { Text(text = stringResource(R.string.Notifications)) }
                    )
                }
            }
        }
    ) { innerPadding ->
        when (localPressed) {
            0 -> { Greeting(name = "Android", modifier = Modifier.padding(innerPadding))}
            1 -> { TimePickerScreen(modifier = Modifier.padding(innerPadding)) }
            2 -> { ButtonRow(modifier = Modifier.padding(innerPadding)) }
            3 -> { CatScreen(modifier = Modifier.padding(innerPadding)) }
            4 -> { DataLayerLayout(modifier = Modifier.padding(innerPadding)) }
            5 -> { SharedPreferencesUI(modifier = Modifier.padding(innerPadding), context = LocalContext.current) }
            6 -> { DBMain(modifier = Modifier.padding(innerPadding)) }
            7 -> { WebViewScreenMain(modifier = Modifier.padding(innerPadding)) }
            8 -> {  }
            else -> {  }
        }
    }
}


@Composable
fun Greeting(
    name: String,
    modifier: Modifier = Modifier
)  {
    var pressed: Boolean = remember { false }
    Column (modifier = modifier
        .fillMaxSize()
        .padding(16.dp),
        //.background(Color.DarkGray),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Hello $name!",
            modifier = modifier
        )
        Image(
            painter = painterResource(id = R.drawable.nix_in_star_wars_outlaws),
            contentDescription = "nix_in_star_wars_outlaws",
            modifier = modifier.clip(CircleShape)
        )
        Button(
            onClick = { pressed = true },
            modifier = modifier.padding(21.dp)
        ) {
            Text(text = "NIX")
        }
    }

    Row(
        modifier = Modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.Bottom
    ) {/*row elements*/}
}

@Composable
fun Settings(
    modifier: Modifier = Modifier
){
    Row(
        modifier = modifier.fillMaxSize(),
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.SpaceEvenly,
    ) {
        Button(
            modifier = modifier
                .shadow(
                    10.dp, CircleShape, true,
                    Color.Yellow, Color.Cyan
                ),
            onClick = {  }
        ) {
            Image(
                imageVector = Icons.Filled.MoreTime,
                contentDescription = "timePicker",
                modifier = modifier.clip(CircleShape)
            )
        }
        Button(
            modifier = modifier
                .shadow(
                    10.dp, CircleShape, true,
                    Color.Yellow, Color.Cyan
                ),
            onClick = {  }
        ) {
            Image(
                imageVector = Icons.Filled.CalendarMonth,
                contentDescription = "datePicker",
                modifier = modifier.clip(CircleShape)
            )
        }
        Button(
            modifier = modifier
                .shadow(
                    10.dp, CircleShape, true,
                    Color.Yellow, Color.Cyan
                ),
            onClick = {  }
        ) {
            Image(
                imageVector = Icons.Filled.Warning,
                contentDescription = "Alert",
                modifier = modifier.clip(CircleShape)
            )
        }
        Button(
            modifier = modifier
                .shadow(
                    10.dp, CircleShape, true,
                    Color.Yellow, Color.Cyan
                ),
            onClick = {  }
        ) {
            Image(
                imageVector = Icons.Filled.DashboardCustomize,
                contentDescription = "Custom",
                modifier = modifier.clip(CircleShape)
            )
        }
    }
}


@Preview
@Composable
fun SettingsPreview() {
    Settings()
}

@Preview
@Composable
fun Main() {
    MainScreen()
}


@Preview(showBackground = true, backgroundColor = 0xfaf8ff)
@Composable
fun GreetingPreview() {
    MyApplicationTheme {
        Greeting("Android")
    }

}


@Composable
fun rainbowBackground(): Brush {

    val infiniteTransition = rememberInfiniteTransition(label = "")
    val offset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(6000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ), label = ""
    )

    val rainbowBrush = Brush.horizontalGradient(
        colors = listOf(
            Color(0x00000000),
            Color(0xFFf90101),
            Color(0xFFf9a101),
            Color(0xFFf9f903),
            Color(0xFF017d01),
            Color(0xFF1314e0),
            Color(0xFF7e027e),
            // Color(0xFFFF00FF)
        ),
        startX = offset * 2000f,
        endX = offset * 0f
    )

    return rainbowBrush
}
