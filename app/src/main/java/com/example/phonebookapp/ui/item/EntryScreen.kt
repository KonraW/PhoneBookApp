@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.phonebookapp.ui.item

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.phonebookapp.data.Category
import com.example.phonebookapp.data.NumberTypes
import com.example.phonebookapp.ui.AppViewModelProvider
import kotlinx.coroutines.launch

@Composable
fun EntryScreen(
    viewModel: EntryViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val corutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            EntryTopBar(
                onSaveClick = { corutineScope.launch { viewModel.saveItem() } }
            )
        }
    ) { innerPadding ->
        EntryBody(
            itemUiState = viewModel.itemUiState,
            onItemValueChange = viewModel::updateUiState,
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        )
    }
}

@Composable
fun EntryTopBar(
    onSaveClick: () -> Unit
) {
    TopAppBar(title = {
        Text("Entry")
        Button(onClick = onSaveClick) {
            Text("Save")
        }
    })

}

@Composable
fun EntryBody(
    itemUiState: ItemUiState,
    onItemValueChange: (ItemDetails) -> Unit,
    modifier: Modifier = Modifier
) {
    val itemDetails = itemUiState.itemDetails
    val enabledUsed = itemUiState.enabledUsed

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.padding(8.dp)
    ) {
        EntryIconAndText(
            image = Icons.Default.Person,
            value = itemDetails.name,
            onValueChange = { newValue -> onItemValueChange(itemDetails.copy(name = newValue)) },
            label = "Name",
            index = -1
        )
        EntryText(
            value = itemDetails.surname,
            onValueChange = { newValue -> onItemValueChange(itemDetails.copy(surname = newValue)) },
            label = "Surname"
        )
        EntryDrop(
            typesAndColor = Category.values().map { it.name to it.color },
            value = itemDetails.category,
            color = Category.valueOf(itemDetails.category).color,
            onValueChange = { newValue -> onItemValueChange(itemDetails.copy(category = newValue)) },
            label = "Category"
        )
        for (i in 0..enabledUsed) {
            EntryNumberAndType(
                index = i,
                itemDetails = itemDetails,
                onItemValueChange = onItemValueChange
            )
        }
        EntryIconAndText(
            image = Icons.Default.MailOutline,
            value = itemDetails.email,
            onValueChange = { newValue -> onItemValueChange(itemDetails.copy(email = newValue)) },
            label = "Email",
            index = -1
        )
        TextField(
            value = itemDetails.notes,
            //if (newValue.length<=10)
            onValueChange = { newValue -> onItemValueChange(itemDetails.copy(notes = newValue)) },
            label = { Text("Notes") },
            modifier = Modifier
                .padding(8.dp),
//                .fillMaxWidth(),
//            maxLines = 10
            supportingText = {
                Text(
                    text = "${itemDetails.notes.length} / 10",
//                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.End,
                )
            }
        )
//        val maxChar = 5

//        TextField(
//            value = text,
//            onValueChange = {
//                if (it.length <= maxChar) text = it
//            },
//            modifier = Modifier.fillMaxWidth(),
//            supportingText = {
//                Text(
//                    text = "${text.length} / $maxChar",
//                    modifier = Modifier.fillMaxWidth(),
//                    textAlign = TextAlign.End,
//                )
//            },
//        )


    }
}

@Composable
fun EntryNumberAndType(
    index: Int,
    itemDetails: ItemDetails,
    onItemValueChange: (ItemDetails) -> Unit
) {
    EntryIconAndText(
        image = Icons.Default.Phone,
        value = itemDetails.number[index],
        onValueChange = { newValue ->
            val updatedNumberList = itemDetails.number.toMutableList()
            updatedNumberList[index] = newValue
            onItemValueChange(itemDetails.copy(number = updatedNumberList))
        },
        label = "Phone",
        index = index,
    )
    EntryDrop(
        types = NumberTypes.values()
//            .filter { !itemDetails.numberTypes.contains(it.name) }
            .map { it.name },
        value = itemDetails.numberTypes[index],
//        onValueChange = { newValue -> onItemValueChange(itemDetails.copy(numberTypes = newValue)) }

        onValueChange = { newValue ->
            val updatedNumberList = itemDetails.numberTypes.toMutableList()
//            updatedNumberList.add(newValue)
            updatedNumberList[index] = newValue
            onItemValueChange(itemDetails.copy(numberTypes = updatedNumberList))
        },
        label = "Type"
    )
}

@Composable
fun EntryDrop(
    types: List<String> = emptyList(),
    typesAndColor: List<Pair<String, Color>> = emptyList(),
    value: String,
    color: Color = Color.Black,
    onValueChange: (String) -> Unit,
    label: String
) {
    var expanded by remember { mutableStateOf(false) }
    Row(
//        horizontalArrangement = Arrangement.Center,
//        modifier = Modifier.fillMaxWidth()
    ) {
        ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = it }) {
//            Row {
//                Spacer(modifier = Modifier.width(36.dp))
            OutlinedTextField(
                value = value,//selectedValue.toString(),
                label = { Text(label) },
                colors = TextFieldDefaults.outlinedTextFieldColors(color, color),
                readOnly = true,
                onValueChange = {},
                modifier = Modifier
                    .menuAnchor()
                    .padding(8.dp)
            )
//            }
            ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                typesAndColor.forEach { (type, color) ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(8.dp)
                    ) {
                        Image(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            colorFilter = ColorFilter.tint(color)
                        )
                        DropdownMenuItem(
                            text = { Text(text = type, color = color) },
                            onClick = {
                                onValueChange(type)
                                expanded = false
                            }
                        )
                    }
                }
                for (type in types) {
                    DropdownMenuItem(
                        text = { Text(text = type) },
                        onClick = {
                            onValueChange(type)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}


@Composable
fun EntryTexts() {
    Column {

    }
}

@Composable
fun EntryIconAndText(
    image: ImageVector,
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    index: Int = 0,
    onAddClick: () -> Unit = {},
    viewModel: EntryViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {

    Row(
        verticalAlignment = Alignment.CenterVertically,
//        horizontalArrangement = Arrangement.Center,
//        modifier = Modifier.fillMaxWidth()
    ) {
        Image(
            imageVector = image,
            contentDescription = null,
            modifier = Modifier
                .padding(start = 4.dp, end = 8.dp)
                .size(24.dp)
        )
        OutlinedTextField(
            value = value,
            onValueChange = { newValue ->
                onValueChange(newValue)
            },
            //{onValueChange(itemDetails.copy(number=it))},
            label = { Text(label) },
            modifier = Modifier.padding(8.dp),
            maxLines = 1
        )
//        if (enabledMore) {
        if (index > 0) {
            FilledIconButton(
                onClick = { viewModel.deleteNumber(index) },
                modifier = Modifier.size(36.dp)
            ) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = null)
            }
        } else if (index == 0 && viewModel.itemUiState.isEnabledMore) {
            FilledIconButton(
                onClick = { viewModel.addMoreNumbers() },
                modifier = Modifier.size(36.dp)
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = null)
            }
        } else {
            Spacer(modifier = Modifier.size(36.dp))
        }
//        }
    }
}

@Composable
fun EntryText(
    value: String,
    onValueChange: (String) -> Unit,
    label: String
) {
    OutlinedTextField(
        value = value,
        onValueChange = { onValueChange(it) },
        label = { Text(label) },
        modifier = Modifier.padding(8.dp),
        maxLines = 1
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewEntryScreen() {
//    EntryScreen()
    Column(
//        horizontalAlignment = Alignment.CenterHorizontally
    ) {
//        EntryIconAndTextAndDrop(itemDetails = ItemDetails(), onItemValueChange = {})

    }
}