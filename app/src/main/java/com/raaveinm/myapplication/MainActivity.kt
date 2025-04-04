package com.raaveinm.myapplication

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Album
import androidx.compose.material.icons.filled.Brush
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
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
import com.raaveinm.myapplication.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
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
    var pressed by remember { mutableIntStateOf(0) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = true,
                    onClick = { pressed = 0 },
                    icon = { Icon(
                        (Icons.Filled.AccountCircle),
                        contentDescription = stringResource(R.string.home)
                    )},
                    label = { Text(text = stringResource(R.string.home)) }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = { pressed = 2 },
                    icon = { Icon(
                        (Icons.Filled.Album),
                        contentDescription = stringResource(R.string.player)
                    )},
                    label = { Text(text = stringResource(R.string.player)) }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = { pressed = 1 },
                    icon = { Icon(
                        (Icons.Filled.Brush),
                        contentDescription = stringResource(R.string.settings)
                    )},
                    label = { Text(text = stringResource(R.string.settings)) }
                )
            }
        }
    ) { innerPadding ->
        when (pressed) {
            0 -> {
                Greeting(
                    name = "Android",
                    modifier = Modifier.padding(innerPadding)
                )
            }
            1 -> {}
            2 -> {
                ButtonRow(
                    modifier = Modifier.padding(innerPadding)
                )
            }
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
fun ButtonRow(
    modifier: Modifier = Modifier
) {
    var playPauseState by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Button(
            modifier = modifier
                .padding(21.dp)
                .shadow(
                    7.dp, CircleShape, true,
                    Color.Yellow, Color.Cyan
                ),
            //.border(3.dp,rainbowBackground(), CircleShape),
            onClick = {
                playPauseState = !playPauseState
                context.playPause(playPauseState)

            }
        ) {
            Image(
                imageVector = if (!playPauseState)(Icons.Filled.Pause)else(Icons.Filled.PlayArrow),
                contentDescription = "dd",
                modifier = modifier.clip(CircleShape)
            )
        }
        Button(
            modifier = modifier
                .padding(21.dp)
                .shadow(7.dp, CircleShape, true,)
                .clip(CircleShape),
            onClick = {  }
        ) {
            Image(
                imageVector = Icons.Filled.Brush,
                contentDescription = "dd",
                modifier = modifier.clip(CircleShape)
            )
        }
    }
}

fun Context.playPause(isPlaying: Boolean) {
    val intent = Intent(this, PlayerService::class.java).apply {
        action = if (isPlaying) ACTION_PAUSE else ACTION_PLAY
    }
    ContextCompat.startForegroundService(this, intent)
}

@Preview
@Composable
fun ButtonRowPreview() {
    ButtonRow()
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
fun rainbowBackground()  {//return brush
    val rainbowColorsBrush = remember {
        Brush.horizontalGradient(
            listOf(
                Color(0xFFf90101),
                Color(0xFFf9a101),
                Color(0xFFf9f903),
                Color(0xFF017d01),
                Color(0xFF1314e0),
                Color(0xFF7e027e),
                //Color(0xFFFF00FF)
            ))
    }

    val infiniteTransition = rememberInfiniteTransition(label = "")
    val offset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(6000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ), label = ""
    )

    //return rainbowColorsBrush.(start = offset * 1000f)
}