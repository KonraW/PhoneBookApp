@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.phonebookapp.ui.item

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun EditScreen() {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        EditIconAndText(image = Icons.Default.Person, "", {}, "Name")
        EditText(value = "", onValueChange = {}, label = "Surname")
        EditIconAndText(image = Icons.Default.Phone, value = "", onValueChange = {}, label = "Phone")
        EditDrop()

    }
}

@Composable
fun EditDrop() {
    var expanded by remember { mutableStateOf(false) }
    var value by remember { mutableStateOf("Home") }

    ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = it }) {
        OutlinedTextField(
            value = value,
            label = { Text("Select") },
            readOnly = true,
            onValueChange = { value = it },
            modifier = Modifier
//                .clickable(onClick = { expanded = !expanded })
                .menuAnchor()
                .padding(8.dp)
        )
        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {

            DropdownMenuItem(
                text = { Text(text = "kto") },
                onClick = { value = "kto"; expanded = false })
            DropdownMenuItem(
                text = { Text(text = "fasf") },
                onClick = { value = "fasf"; expanded = false })

        }
    }
}

@Composable
fun EditTopBar() {

}

@Composable
fun EditTexts() {
    Column {

    }
}

@Composable
fun EditIconAndText(
    image: ImageVector,
    value: String,
    onValueChange: (String) -> Unit,
    label: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.padding(8.dp)
    ) {
        Image(
            imageVector = image,
            contentDescription = null,
            modifier = Modifier
                .padding(start = 4.dp, end = 12.dp)
                .size(24.dp)
        )
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(label) }
        )
        Spacer(modifier = Modifier.padding(20.dp))
    }
}

@Composable
fun EditText(
    value: String,
    onValueChange: (String) -> Unit,
    label: String
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = Modifier.padding(8.dp)
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewEditScreen() {
    EditScreen()
}