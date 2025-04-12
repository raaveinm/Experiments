package com.raaveinm.myapplication.ui.layout

import android.os.Environment
import android.os.Environment.getExternalStoragePublicDirectory
import android.widget.ImageButton
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.FileOpen
import androidx.compose.material.icons.filled.Recycling
import androidx.compose.material.icons.filled.Save
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

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(57.dp, 42.dp, 57.dp, 0.dp),
            singleLine = true,
            value = "",
            onValueChange = {fileName = it},
            label = { Text("file name") }
        )
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(57.dp, 5.dp, 57.dp, 0.dp),
            singleLine = true,
            value = "",
            onValueChange = {fileContent = it},
            label = { Text("file content") }
        )
        ButtonRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(57.dp, 30.dp, 57.dp, 0.dp),
            fileName = fileName,
            fileContent = fileContent
        )
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
    Column {
        Row(
            modifier = modifier,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            IconButton(
                modifier = Modifier.padding(10.dp),
                onClick = { saveFile(fileName, fileContent) }
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
                onClick = { deleteFile(fileName)  }
            ) {
                Icon(
                    modifier = Modifier.size(80.dp),
                    imageVector = Icons.Default.Recycling,
                    contentDescription = "delete"
                )
            }
            IconButton(
                modifier = Modifier.padding(10.dp),
                onClick = { addFile(fileName, fileContent) }
            ) {
                Icon(
                    modifier = Modifier.size(80.dp),
                    imageVector = Icons.Default.Add,
                    contentDescription = "add"
                )
            }
            Text(text = fileOutput)
        }
    }
}

val storageDir: File = getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)

private fun saveFile(fileName: String, fileContent: String) {

    if (!storageDir.exists()) storageDir.mkdirs()

    val file = File(storageDir, fileName)
    try {
        if (!file.exists()) {
            val writer = FileWriter(file)
            writer.append(fileContent)
            writer.flush()
            writer.close()
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

private fun openFile(fileName: String) : String{
    val file: File = File(storageDir, fileName)
    if (file.exists()) {
        val output: String = file.readText()
        return output
    } else return "ERR_FILE_NOT_FOUND"
}

private fun deleteFile(fileName: String) {}

private fun addFile(fileName: String, fileContent: String) {}
