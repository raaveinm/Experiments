package com.raaveinm.myapplication.ui.layout

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.raaveinm.myapplication.dbsystem.Track
import com.raaveinm.myapplication.ui.viewmodel.TrackInputState
import com.raaveinm.myapplication.ui.viewmodel.TrackViewModel
import java.util.concurrent.TimeUnit


enum class TrackScreens {
    AllTracks,
    TracksByName,
    AddTrack
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DBMain(
    modifier: Modifier = Modifier,
    viewModel: TrackViewModel = viewModel(factory = TrackViewModel.factory)
) {
    val navController = rememberNavController()
    val allTracksTitle = "All Tracks"
    var topAppBarTitle by remember { mutableStateOf(allTracksTitle) }
    val allTracks by viewModel.getAllTracks().collectAsState(emptyList())

    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = TrackScreens.valueOf(
        backStackEntry?.destination?.route?.substringBefore('/') ?: TrackScreens.AllTracks.name
    )

    val onBackHandler: () -> Unit = {
        if (navController.previousBackStackEntry?.destination?.route == TrackScreens.AllTracks.name) {
            topAppBarTitle = allTracksTitle
        } else if (navController.previousBackStackEntry?.destination?.route?.startsWith(TrackScreens.TracksByName.name) == true) {
            val prevRoute = navController.previousBackStackEntry?.destination?.route
            val trackName = prevRoute?.substringAfter('/')
            if (trackName != null) {
                topAppBarTitle = trackName
            } else {
                navController.previousBackStackEntry?.destination?.route?.let { topAppBarTitle = it }
            }
        }
        navController.navigateUp()
    }

    val snackbarHostState = remember { SnackbarHostState() }
    Scaffold(
        modifier = modifier,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TrackTopAppBar(
                title = topAppBarTitle,
                canNavigateBack = navController.previousBackStackEntry != null,
                onBackClick = onBackHandler
            )
        },
        floatingActionButton = {
            if (currentScreen == TrackScreens.AllTracks) {
                FloatingActionButton(
                    onClick = {
                        viewModel.clearInputFields()
                        navController.navigate(TrackScreens.AddTrack.name)
                        topAppBarTitle = "Add New Track"
                    },
                    modifier = Modifier.navigationBarsPadding()
                ) {
                    Icon(Icons.Filled.Add, contentDescription = "Add Track")
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = TrackScreens.AllTracks.name,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(TrackScreens.AllTracks.name) {
                AllTracksScreen(
                    tracks = allTracks,
                    onTrackClick = { trackName ->
                        navController.navigate("${TrackScreens.TracksByName.name}/$trackName")
                        topAppBarTitle = trackName
                    },
                    onDeleteTrack = { track -> viewModel.deleteTrack(track) }
                )
            }

            val trackNameArgument = "trackName"
            composable(
                route = "${TrackScreens.TracksByName.name}/{$trackNameArgument}",
                arguments = listOf(navArgument(trackNameArgument) { type = NavType.StringType })
            ) { backStackEntry ->
                val name = backStackEntry.arguments?.getString(trackNameArgument)
                    ?: error("trackNameArgument cannot be null")
                val tracksByName by viewModel.getTracksByName(name).collectAsState(emptyList())
                TracksByNameScreen(
                    trackName = name,
                    tracks = tracksByName,
                    onBack = onBackHandler,
                    onDeleteTrack = { track -> viewModel.deleteTrack(track) }
                )
            }

            composable(TrackScreens.AddTrack.name) {
                AddTrackScreen(
                    inputState = viewModel.trackInputState,
                    onInputChange = { newState -> viewModel.updateInputState(newState) },
                    onSaveClick = {
                        viewModel.insertTrack()
                        navController.popBackStack()
                        topAppBarTitle = allTracksTitle
                    },
                    onCancelClick = {
                        viewModel.clearInputFields()
                        onBackHandler()
                    }
                )
            }
        }
    }
}

@Composable
fun AllTracksScreen(
    tracks: List<Track>,
    onTrackClick: (String) -> Unit,
    onDeleteTrack: (Track) -> Unit,
    modifier: Modifier = Modifier,
) {
    TrackListScreen(
        tracks = tracks,
        onTrackClick = onTrackClick,
        onDeleteTrack = onDeleteTrack,
        modifier = modifier
    )
}

@Composable
fun TracksByNameScreen(
    trackName: String,
    tracks: List<Track>,
    onDeleteTrack: (Track) -> Unit,
    modifier: Modifier = Modifier,
    onBack: () -> Unit = {}
) {
    BackHandler { onBack() }
    TrackListScreen(
        tracks = tracks,
        modifier = modifier,
        listHeaderText = "$trackName Tracks",
        onDeleteTrack = onDeleteTrack
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTrackScreen(
    inputState: TrackInputState,
    onInputChange: (TrackInputState) -> Unit,
    onSaveClick: () -> Unit,
    onCancelClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .imePadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Enter Track Details", style = MaterialTheme.typography.headlineSmall)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = inputState.trackName,
            onValueChange = { onInputChange(inputState.copy(trackName = it)) },
            label = { Text("Track Name") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        OutlinedTextField(
            value = inputState.artist,
            onValueChange = { onInputChange(inputState.copy(artist = it)) },
            label = { Text("Artist") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        OutlinedTextField(
            value = inputState.album,
            onValueChange = { onInputChange(inputState.copy(album = it)) },
            label = { Text("Album") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        OutlinedTextField(
            value = inputState.durationSeconds,
            onValueChange = { newValue ->
                if (newValue.all { it.isDigit() }) {
                    onInputChange(inputState.copy(durationSeconds = newValue))
                }
            },
            label = { Text("Duration (seconds)") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true
        )

        Spacer(modifier = Modifier.weight(1f))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedButton(
                onClick = onCancelClick,
                modifier = Modifier.weight(1f)
            ) {
                Text("Cancel")
            }
            Button(
                onClick = onSaveClick,
                modifier = Modifier.weight(1f),
                enabled = inputState.trackName.isNotBlank() &&
                        inputState.artist.isNotBlank() &&
                        inputState.album.isNotBlank() &&
                        inputState.durationSeconds.isNotBlank() &&
                        inputState.durationSeconds.toLongOrNull() != null
            ) {
                Text("Save Track")
            }
        }
    }
}


@Composable
fun TrackListScreen(
    tracks: List<Track>,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    listHeaderText: String? = null,
    onTrackClick: ((String) -> Unit)? = null,
    onDeleteTrack: ((Track) -> Unit)? = null,
) {
    val headerText = listHeaderText ?: "Track Name"
    val layoutDirection = LocalLayoutDirection.current

    Column(
        modifier = modifier
            .padding(
                start = contentPadding.calculateStartPadding(layoutDirection),
                end = contentPadding.calculateEndPadding(layoutDirection),
                top = contentPadding.calculateTopPadding()
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = headerText,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = "Duration",
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.End,
                modifier = Modifier.padding(end = 40.dp)
            )
        }
        HorizontalDivider()

        TrackListDetails(
            tracks = tracks,
            onTrackClick = onTrackClick,
            onDeleteTrack = onDeleteTrack,
            contentPadding = PaddingValues(bottom = contentPadding.calculateBottomPadding())
        )
    }
}

@Composable
fun TrackListDetails(
    tracks: List<Track>,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    onTrackClick: ((String) -> Unit)? = null,
    onDeleteTrack: ((Track) -> Unit)? = null
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = contentPadding,
    ) {
        items(
            items = tracks,
            key = { track -> track.id }
        ) { track ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(enabled = onTrackClick != null) {
                        onTrackClick?.invoke(track.track)
                    }
                    .padding(start = 16.dp, top = 12.dp, bottom = 12.dp, end = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = track.track,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = "${track.artist} - ${track.album}",
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = formatDuration(track.duration),
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.End,
                    modifier = Modifier.padding(end = 8.dp)
                )

                if (onDeleteTrack != null) {
                    IconButton(
                        onClick = { onDeleteTrack(track) },
                        modifier = Modifier.size(40.dp).padding(0.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Delete,
                            contentDescription = "Delete Track",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                } else {
                    Spacer(modifier = Modifier.width(40.dp))
                }
            }
            Divider(modifier = Modifier.padding(start = 16.dp, end = 16.dp))
        }
    }
}


fun formatDuration(millis: Long): String {
    val hours = TimeUnit.MILLISECONDS.toHours(millis)
    val minutes = TimeUnit.MILLISECONDS.toMinutes(millis) % 60
    val seconds = TimeUnit.MILLISECONDS.toSeconds(millis) % 60

    return if (hours > 0) {
        String.format("%02d:%02d:%02d", hours, minutes, seconds)
    } else {
        String.format("%02d:%02d", minutes, seconds)
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrackTopAppBar(
    title: String,
    canNavigateBack: Boolean,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = { Text(title, maxLines = 1, overflow = TextOverflow.Ellipsis) },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        modifier = modifier,
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            }
        }
    )
}

@Preview(showBackground = true, widthDp = 360, heightDp = 640)
@Composable
fun AllTracksScreenWithDeletePreview() {
    val previewTracks = List(5) { index ->
        Track(
            id = index,
            track = "Track Title $index",
            artist = "Artist Name $index",
            album = "Album $index",
            duration = (180 + index * 15) * 1000L
        )
    }
    MaterialTheme {
        AllTracksScreen(
            tracks = previewTracks,
            onTrackClick = {},
            onDeleteTrack = {}
        )
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 640)
@Composable
fun AddTrackScreenPreview() {
    MaterialTheme {
        AddTrackScreen(
            inputState = TrackInputState(trackName = "Preview Track", artist = "Preview Artist"),
            onInputChange = {},
            onSaveClick = {},
            onCancelClick = {}
        )
    }
}
