package com.raaveinm.myapplication.ui.layout


import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Update
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.edit
import com.raaveinm.myapplication.R


@Composable
fun SharedPreferencesUI (
    modifier: Modifier = Modifier,
    context: Context
){
    var localName by rememberSaveable { mutableStateOf(getData(context)) }
    var isNameExist by rememberSaveable { mutableStateOf(getData(context) != "world") }

    Column (
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        if (localName == "Heisenberg"){
            Image(
                painter = painterResource(id = R.drawable.heisenberg),
                contentDescription = "Heisenberg",
                modifier = Modifier.clip(CircleShape).size(300.dp),
                contentScale = ContentScale.Crop
            )

            Text(
                text = "you are goddamn right",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier,
            )
        } else {
            HelloWorld(
                modifier = Modifier.clip(CircleShape),
                isNameExist = isNameExist,
                onValueChange = { localName = it; isNameExist = true },
                localName = localName,
                context = context
            )
        }
        ButtonRow(
            isExistChangeFalse = { isNameExist = false },
            isExistChange = { isNameExist = getData(context) != "world"; localName = getData(context)},
            localName = localName,
            context = context
        )
    }
}

@Composable
private fun HelloWorld(
    modifier: Modifier,
    isNameExist: Boolean,
    onValueChange: (String) -> Unit,
    localName: String,
    context: Context
) {
    if (isNameExist) {
        Text(
            text = "Hello $localName!",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.headlineMedium,
            modifier = modifier
                .heightIn(min = 50.dp)
                .widthIn(min = 200.dp)
                .padding(0.dp,15.dp,0.dp,15.dp),
        )
    } else {
        var text by rememberSaveable { mutableStateOf("") }
        TextField(
            value = text,
            onValueChange = { text = it; onValueChange },
            modifier = modifier.heightIn(min = 50.dp),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.Edit,
                    contentDescription = "textField"
                )
            },
            trailingIcon = {
                IconButton(
                    onClick = { if (text.isNotEmpty()) { editData(text, context); onValueChange(text) } }
                ){
                    if (text.isEmpty()) {
                        Icon(
                            imageVector = Icons.Filled.Block,
                            contentDescription = "textFieldBlock"
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Filled.Done,
                            contentDescription = "textFieldDone"
                        )
                    }
                }
            },
            placeholder = {
                Text(
                    text = "say my name",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        )
    }
}

@Composable
fun ButtonRow (
    isExistChangeFalse: () -> Unit,
    isExistChange: () -> Unit,
    localName: String,
    context: Context
){
    var modifier = Modifier
    Row (
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly,
    ) {
        IconButton(
            modifier = modifier,
            onClick = { editData(localName,context); isExistChangeFalse() }
        ) {
            Icon(
                imageVector = Icons.Filled.Edit,
                contentDescription = "editButton",
                modifier = Modifier.size(30.dp)
            )
        }
        IconButton(
            modifier = modifier,
            onClick = { deleteData(context) }
        ) {
            Icon(
                imageVector = Icons.Filled.Delete,
                contentDescription = "deleteButton",
                modifier = Modifier.size(30.dp)
            )
        }
        IconButton(
            modifier = modifier,
            onClick = { isExistChange() }
        ) {
            Icon(
                imageVector = Icons.Filled.Update,
                contentDescription = "updateButton",
                modifier = Modifier.size(30.dp)
            )
        }
    }
}

private fun getData(context: Context): String {
    var data: String
    val sharedPreferences: SharedPreferences = context.getSharedPreferences("name", MODE_PRIVATE)
    data = sharedPreferences.getString("name", "world").toString()
    Log.d("RRR", "getData: $data")
    return data
}

private fun editData(localName: String, context: Context){
    val sharedPreferences: SharedPreferences = context.getSharedPreferences("name", MODE_PRIVATE)
    sharedPreferences.edit {
        putString("name", localName)
        apply()
    }
    Log.d("RRR", "editData: $localName")
}

private fun deleteData(context: Context){
    val sharedPreferences: SharedPreferences = context.getSharedPreferences("name", MODE_PRIVATE)
    sharedPreferences.edit {
        remove("name")
        apply()
    }
    Log.d("RRR", "deleteData")
}
