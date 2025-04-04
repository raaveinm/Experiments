package com.raaveinm.myapplication.ui.layout

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerScreen() {
    // State variables to hold selected values and comparison results
    var selectedTime by remember { mutableStateOf<String?>(null) }
    var isTimeCorrect by remember { mutableStateOf<Boolean?>(null) }
    var selectedDate by remember { mutableStateOf<String?>(null) }
    var isDateCorrect by remember { mutableStateOf<Boolean?>(null) }

    // State for controlling M3 Picker dialog visibility
    var showTimePickerDialog by remember { mutableStateOf(false) }
    var showDatePickerDialog by remember { mutableStateOf(false) }

    // M3 Picker States
    val timePickerState = rememberTimePickerState(is24Hour = true)
    val datePickerState = rememberDatePickerState()

    // State for controlling other dialog visibility
    var showAlertDialog by remember { mutableStateOf(false) }
    var showCustomDialog by remember { mutableStateOf(false) }

    // State for Snackbar
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    // --- Picker Launching Logic ---
    val launchTimePicker = { showTimePickerDialog = true }
    val launchDatePicker = { showDatePickerDialog = true }

    // --- UI Structure ---
    Scaffold(
        modifier = Modifier.windowInsetsPadding(WindowInsets.systemBars),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(32.dp))
                TimeDisplay(time = selectedTime, isCorrect = isTimeCorrect)

                Spacer(modifier = Modifier.height(32.dp))
                DateDisplay(date = selectedDate, isCorrect = isDateCorrect)

                Spacer(modifier = Modifier.weight(1f))

                Settings(
                    modifier = Modifier.fillMaxWidth(),
                    onTimeClick = launchTimePicker,
                    onDateClick = launchDatePicker,
                    onAlertClick = { showAlertDialog = true },
                    onCustomClick = { showCustomDialog = true }
                )
            }

            // --- M3 Time Picker Dialog ---
            if (showTimePickerDialog) {
                TimePickerDialog(
                    state = timePickerState,
                    onDismissRequest = { showTimePickerDialog = false },
                    onConfirm = {
                        val cal = Calendar.getInstance()
                        cal.set(Calendar.HOUR_OF_DAY, timePickerState.hour)
                        cal.set(Calendar.MINUTE, timePickerState.minute)
                        cal.isLenient = false
                        cal.set(Calendar.SECOND, 0)
                        cal.set(Calendar.MILLISECOND, 0)

                        val timeFormat = SimpleDateFormat("H:mm", Locale.getDefault())
                        val formattedTime = timeFormat.format(cal.time)
                        selectedTime = formattedTime

                        // Perform comparison
                        val actualTimeFormatted = timeFormat.format(Calendar.getInstance().time)
                        isTimeCorrect = (actualTimeFormatted == formattedTime)

                        showTimePickerDialog = false
                    },
                    onDismiss = {
                        showTimePickerDialog = false
                    }
                )
            }


            // --- M3 Date Picker Dialog ---
            if (showDatePickerDialog) {
                DatePickerDialog(
                    state = datePickerState,
                    onDismissRequest = { showDatePickerDialog = false },
                    onConfirm = {
                        val selectedMillis = datePickerState.selectedDateMillis
                        if (selectedMillis != null) {
                            val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
                            calendar.timeInMillis = selectedMillis
                            val dateFormat = SimpleDateFormat("M/d/yyyy", Locale.getDefault())
                            val formattedDate = dateFormat.format(calendar.time)
                            selectedDate = formattedDate

                            val todayCalendar = Calendar.getInstance()
                            val actualDateFormatted = dateFormat.format(todayCalendar.time)
                            isDateCorrect = (actualDateFormatted == formattedDate)

                        } else {
                            selectedDate = null
                            isDateCorrect = null
                        }
                        showDatePickerDialog = false
                    },
                    onDismiss = {
                        showDatePickerDialog = false
                    }
                )
            }

            // --- Other Dialogs (unchanged) ---
            if (showAlertDialog) {
                AlertDialog(
                    onDismissRequest = { showAlertDialog = false },
                    confirmButton = {
                        Button(onClick = { showAlertDialog = false }) { Text("Understood") }
                    },
                    icon = { Icon(Icons.Filled.CheckCircle, contentDescription = null) },
                    title = { Text("Alert Sample") },
                    text = {
                        Text("You told that it's ${selectedTime ?: "no time selected"}. " +
                                "And you think that today is ${selectedDate ?: "no date selected"}")
                    }
                )
            }

            if (showCustomDialog) {
                Dialog(onDismissRequest = { showCustomDialog = false }) {
                    Surface(
                        shape = MaterialTheme.shapes.medium,
                        color = MaterialTheme.colorScheme.surface,
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text("Custom Dialog Title", style = MaterialTheme
                                .typography.headlineSmall)
                            Spacer(modifier = Modifier.height(16.dp))
                            Text("This is custom dialog content.")
                            Spacer(modifier = Modifier.height(24.dp))
                            Button(
                                onClick = {
                                    showCustomDialog = false
                                    coroutineScope.launch {
                                        snackbarHostState.showSnackbar(
                                            "Custom dialog button clicked!")
                                    }
                                }
                            ) {
                                Text("OK")
                            }
                        }
                    }
                }
            }
        }
    )
}

// ------------- UI Components (Unchanged except imports if any) -------------

@Composable
fun TimeDisplay(time: String?, isCorrect: Boolean?) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
    ) {
        Icon(
            imageVector = Icons.Default.AccessTime,
            contentDescription = "Time Icon",
            modifier = Modifier.size(40.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text("Selected Time", style = MaterialTheme.typography.titleMedium)
            Text(
                text = time ?: "Not Selected",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = when (isCorrect) {
                    true -> Color.Green
                    false -> Color.Red
                    null -> LocalContentColor.current
                }
            )
        }

        val iconToShow = when (isCorrect) {
            true -> Icons.Filled.Check
            else -> Icons.Filled.Add
        }

        Icon(
            imageVector = iconToShow,
            contentDescription = if (isCorrect == true) "Correct Time" else "Add/Incorrect Time",
            modifier = Modifier.size(30.dp),
            tint = when (isCorrect) {
                true -> Color.Green
                false -> Color.Red
                null -> LocalContentColor.current
            }
        )
    }
}

@Composable
fun DateDisplay(date: String?, isCorrect: Boolean?) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
    ) {
        Icon(
            imageVector = Icons.Default.CalendarMonth,
            contentDescription = "Date Icon",
            modifier = Modifier.size(40.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text("Selected Date", style = MaterialTheme.typography.titleMedium)
            Text(
                text = date ?: "Not Selected",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = when (isCorrect) {
                    true -> Color.Green
                    false -> Color.Red
                    null -> LocalContentColor.current
                }
            )
        }
        val iconToShow = when (isCorrect) {
            true -> Icons.Filled.Check
            else -> Icons.Filled.Add
        }

        Icon(
            imageVector = iconToShow,
            contentDescription = if (isCorrect == true) "Correct Date" else "Add/Incorrect Date",
            modifier = Modifier.size(30.dp),
            tint = when (isCorrect) {
                true -> Color.Green
                false -> Color.Red
                null -> LocalContentColor.current
            }
        )
    }
}


// ------------- Settings Composable (Unchanged) -------------

@Composable
fun Settings(
    modifier: Modifier = Modifier,
    onTimeClick: () -> Unit,
    onDateClick: () -> Unit,
    onAlertClick: () -> Unit,
    onCustomClick: () -> Unit
){
    Row(
        modifier = modifier.padding(vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly,
    ) {
        SettingsButton(
            icon = Icons.Filled.MoreTime,
            contentDescription = "timePicker",
            onClick = onTimeClick
        )
        SettingsButton(
            icon = Icons.Filled.CalendarMonth,
            contentDescription = "datePicker",
            onClick = onDateClick
        )
        SettingsButton(
            icon = Icons.Filled.Warning,
            contentDescription = "Alert",
            onClick = onAlertClick
        )
        SettingsButton(
            icon = Icons.Filled.DashboardCustomize,
            contentDescription = "Custom",
            onClick = onCustomClick
        )
    }
}

@Composable
fun SettingsButton(
    icon: ImageVector,
    contentDescription: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        shape = CircleShape,
        contentPadding = PaddingValues(0.dp),
        modifier = modifier
            .size(56.dp)
            .shadow(
                elevation = 10.dp,
                shape = CircleShape,
                clip = true,
            )

    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            modifier = Modifier.size(24.dp)
        )
    }
}

// -------------  M3 Picker Dialog Composable Wrappers -------------

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerDialog(
    state: TimePickerState,
    onDismissRequest: () -> Unit,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    title: String = "Select Time"
) {
    BasicAlertDialog(
        onDismissRequest = onDismissRequest,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Surface(
            shape = MaterialTheme.shapes.large,
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = AlertDialogDefaults.TonalElevation
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier.padding(bottom = 20.dp)
                )
                TimePicker(state = state)
                Row(
                    modifier = Modifier
                        .height(40.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    // Dismiss Button
                    TextButton(onClick = onDismiss) {
                        Text("Dismiss")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    // Confirm Button
                    TextButton(onClick = onConfirm) {
                        Text("OK")
                    }
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerDialog(
    state: DatePickerState,
    onDismissRequest: () -> Unit,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    title: @Composable (() -> Unit)? = { Text("Select date") },
    headline: @Composable (() -> Unit)? = null
) {
    DatePickerDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    ) {
        DatePicker(
            state = state,
            title = title,
            headline = headline
        )
    }
}