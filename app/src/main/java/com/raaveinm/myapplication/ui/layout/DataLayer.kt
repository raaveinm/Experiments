package com.raaveinm.myapplication.ui.layout

import android.os.Environment
import android.os.Environment.getExternalStoragePublicDirectory
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.FileOpen
import androidx.compose.material.icons.filled.Recycling
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.WavingHand
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import java.io.File
import java.io.FileWriter

@Composable
fun DataLayerLayout(
    modifier: Modifier = Modifier,
){

    var fileName by rememberSaveable { mutableStateOf("") }
    var fileContent by rememberSaveable { mutableStateOf("") }

    Column {
        LazyColumn(
            modifier = modifier,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            item {
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(57.dp, 42.dp, 57.dp, 0.dp),
                    singleLine = true,
                    value = fileName,
                    onValueChange = { fileName = it },
                    label = { Text("file name") }
                )
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(57.dp, 5.dp, 57.dp, 0.dp),
                    singleLine = false,
                    value = fileContent,
                    onValueChange = { fileContent = it },
                    label = { Text("file content") }
                )
            }
            item {
                ButtonRow(
                    modifier = Modifier.fillMaxWidth(),
                    fileName = fileName,
                    fileContent = fileContent
                )
            }
        }

    }
}

@Preview(device = "id:pixel_9_pro_xl")
@Composable
fun DataLayerLayoutPreview(){
    DataLayerLayout(Modifier.fillMaxSize())
}

@Composable
fun ButtonRow(
    modifier: Modifier = Modifier,
    fileName: String = "",
    fileContent: String = ""
){
    var fileOutput by rememberSaveable { mutableStateOf("") }
    var showAlertDialogue by rememberSaveable { mutableStateOf(false) }
    var result by remember { mutableStateOf("false") }

    Column {
        Row(
            modifier = modifier,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            IconButton(
                modifier = Modifier.padding(10.dp),
                onClick = { result = saveFile(fileName, fileContent) }
            ) {
                Icon(
                    modifier = Modifier.size(80.dp),
                    imageVector = Icons.Default.Save,
                    contentDescription = "save"
                )
            }
            IconButton(
                modifier = Modifier.padding(10.dp),
                onClick = { fileOutput = openFile(fileName) }
            ) {
                Icon(
                    modifier = Modifier.size(80.dp),
                    imageVector = Icons.Default.FileOpen,
                    contentDescription = "open"
                )
            }
            IconButton(
                modifier = Modifier.padding(10.dp),
                onClick = { showAlertDialogue = true }
            ) {
                Icon(
                    modifier = Modifier.size(80.dp),
                    imageVector = Icons.Default.Recycling,
                    contentDescription = "delete"
                )
            }
            IconButton(
                modifier = Modifier.padding(10.dp),
                onClick = { result = addData(fileName, fileContent) }
            ) {
                Icon(
                    modifier = Modifier.size(80.dp),
                    imageVector = Icons.Default.Add,
                    contentDescription = "add"
                )
            }
        }
        Text(
            modifier = Modifier.padding(10.dp),
            text = fileOutput
        )
    }

    if (showAlertDialogue) {
        AlertDialog(
            onDismissRequest = {  },
            confirmButton = {
                Button ( onClick = { result = (deleteFile(fileName)); showAlertDialogue = false })
                { Text("Da") }
            },
            dismissButton = { Button (onClick = { showAlertDialogue = false }) {Text ("No")} },
            icon = { Icon(Icons.Filled.WavingHand, contentDescription = null) },
            title = { Text("alert") },
            text = { Text("are you sure you want to delete $fileName?") }
        )
    }

    when (result) {
        "CREATE_SUCCESS"->{
            Toast.makeText(LocalContext.current, "file $fileName created",
                Toast.LENGTH_SHORT).show(); result = "false"
        }
        "CREATE_EXIST"->{
            Toast.makeText(LocalContext.current, "file $fileName already exists",
                Toast.LENGTH_SHORT).show(); result = "false"
        }
        "CREATE_FAILED"->{
            Toast.makeText(LocalContext.current, "file $fileName creation failed",
                Toast.LENGTH_SHORT).show(); result = "false"
        }
        "DELETE_SUCCESS"->{
            Toast.makeText(LocalContext.current, "file $fileName deleted",
                Toast.LENGTH_SHORT).show(); result = "false"
        }
        "DELETE_FAILED"->{
            Toast.makeText(LocalContext.current, "file $fileName deletion failed",
                Toast.LENGTH_SHORT).show(); result = "false"
        }
        "ADD_SUCCESS"->{
            Toast.makeText(LocalContext.current, "file $fileName added",
                Toast.LENGTH_SHORT).show(); result = "false"
        }
        "ADD_FAILED"->{
            Toast.makeText(LocalContext.current, "file $fileName addition failed",
                Toast.LENGTH_SHORT).show(); result = "false"
        }
        "ERR_FILE_NOT_FOUND"->{
            Toast.makeText(LocalContext.current, "file $fileName not found",
                Toast.LENGTH_SHORT).show(); result = "false"
        }
        else -> {}
    }
}

val storageDir: File = getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)

private fun saveFile(fileName: String, fileContent: String) : String {

    if (!storageDir.exists()) storageDir.mkdirs()

    val file = File(storageDir, fileName)
    try {
        if (!file.exists()) {
            val writer = FileWriter(file)
            writer.append(fileContent)
            writer.flush()
            writer.close()
            return "CREATE_SUCCESS"
        } else { return "CREATE_EXIST" }
    } catch (e: Exception) {
        e.printStackTrace()
        return "CREATE_FAILED"
    }
}

private fun openFile(fileName: String) : String{
    val file = File(storageDir, fileName)
    if (file.exists()) {
        val output: String = file.readText()
        return output
    } else return "ERR_FILE_NOT_FOUND"
}

private fun deleteFile(
    fileName: String
) : String {
    val file = File(storageDir, fileName)

    if (file.exists()) file.delete(); return "DELETE_SUCCESS"
    //Toast.makeText(LocalContext.current, "file $fileName deleted", Toast.LENGTH_SHORT).show()
    return "DELETE_FAILED"
}

private fun addData(fileName: String, fileContent: String) : String {
    val file = File(storageDir, fileName)
    if (file.exists()) {
        val writer = FileWriter(file,true)
        writer.append(fileContent)
        writer.close()
        return "ADD_SUCCESS"
    }
    return "ADD_FAILED"
}

/**
 *     AlertDialog(
 *         onDismissRequest = { confirmed = false },
 *         confirmButton = { Button(onClick = { confirmed = true }) { Text("") } },
 *         dismissButton = { Button(onClick = { confirmed = false }) { Text("") } },
 *         icon = { Icon(Icons.Filled.WavingHand, contentDescription = null) },
 *         title = { Text("Alert Sample") },
 *         text = {"are you sure you want to delete $fileName?"}
 *     )
 */
