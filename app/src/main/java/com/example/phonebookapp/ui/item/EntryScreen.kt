@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.phonebookapp.ui.item

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.phonebookapp.data.NumberTypes
import com.example.phonebookapp.ui.AppViewModelProvider
import com.example.phonebookapp.ui.item.EntryViewModel
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

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.padding(8.dp)
    ) {
        EntryIconAndText(
            image = Icons.Default.Person,
            value = itemDetails.name,
            onValueChange = { newValue -> onItemValueChange(itemDetails.copy(name = newValue)) },
            label = "Name"
        )
        EntryText(
            value = itemDetails.surname,
            onValueChange = { newValue -> onItemValueChange(itemDetails.copy(surname = newValue)) },
            label = "Surname"
        )
        EntryIconAndText(
            image = Icons.Default.Phone,
            value = itemDetails.number,
            onValueChange = { newValue -> onItemValueChange(itemDetails.copy(number = newValue)) },
            label = "Phone"
        )
        EntryDrop(
//            value=itemDetails.numberTypes,
            itemDetails = itemDetails,
            onValueChange = onItemValueChange //= {newValue -> onItemValueChange(itemDetails.copy(numberTypes = newValue))}
        )

    }
}

@Composable
fun EntryDrop(
//    value: NumberTypes,
    itemDetails: ItemDetails,
    onValueChange: (ItemDetails) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var value by remember { mutableStateOf(NumberTypes.OTHER) }

    ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = it }) {
        OutlinedTextField(
            value = itemDetails.numberTypes.toString(),//selectedValue.toString(),
            label = { Text("Select") },
            readOnly = true,
            onValueChange = {},
            modifier = Modifier
//                .clickable(onClick = { expanded = !expanded })
                .menuAnchor()
                .padding(8.dp)
        )
        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {

//            DropdownMenuItem(
//                text = { Text(text = "Home") },
//                onClick = { onValueChange(itemDetails.copy(numberTypes = NumberTypes.HOME)); expanded = false })
//            DropdownMenuItem(
//                text = { Text(text = "Mobile") },
//                onClick = { onValueChange(itemDetails.copy(numberTypes = NumberTypes.MOBILE)); expanded = false })
            for (type in NumberTypes.values()) {
                DropdownMenuItem(
                    text = { Text(text = type.name) },
                    onClick = {
                        onValueChange(itemDetails.copy(numberTypes = type))
                        expanded = false
                    }
                )
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
            onValueChange = { newValue ->
                onValueChange(newValue)
            },
            //{onValueChange(itemDetails.copy(number=it))},
            label = { Text(label) }
        )
        Spacer(modifier = Modifier.padding(20.dp))
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
        modifier = Modifier.padding(8.dp)
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewEntryScreen() {
    EntryScreen()
}