package com.example.phonebookapp.ui.item

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.phonebookapp.PhoneBookTopAppBar
import com.example.phonebookapp.data.Category
import com.example.phonebookapp.data.NumberTypes
import com.example.phonebookapp.ui.AppViewModelProvider
import com.example.phonebookapp.ui.navigation.NavigationDestination
import kotlinx.coroutines.launch


object EntryDestination : NavigationDestination {
    override val route = "item_entry"
    override val titleRes = "Add New Contact"
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EntryScreen(
    onNavigateUp: () -> Unit,
    navigateBack: () -> Unit,
    navigateToItemDetails: (Int) -> Unit,
    viewModel: EntryViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val coroutineScope = rememberCoroutineScope()

//    val uiState = viewModel.uiState.collectAsState()

    Scaffold(topBar = {
//        EntryTopBar(onSaveClick = { coroutineScope.launch { viewModel.saveItem() } })

        PhoneBookTopAppBar(
            title = "New Contact",
            canNavigateBack = true,
            navigateUp = onNavigateUp,
            canClickButton = true,
            onClickButton = {
                coroutineScope.launch {
                    viewModel.saveItem()
                    if (viewModel.validateInput()) {
                        navigateToItemDetails(viewModel.itemUiState.itemDetails.id)
                    }
                }
//            navigateToItemDetails(viewModel.itemUiState.itemDetails.id)
            },
            buttonIcon = Icons.Default.Done
        )
    }) { innerPadding ->
        EntryBody(
            viewModel = viewModel,
            itemUiState = viewModel.itemUiState,
            onItemValueChange = viewModel::updateUiState,
            onSaveClick = {
                coroutineScope.launch {
                    viewModel.saveItem()
                    if (viewModel.validateInput()) {
                        navigateToItemDetails(viewModel.itemUiState.itemDetails.id)
                    }
                }
            },
//            coroutineScope = coroutineScope,
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
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
    viewModel: EntryViewModel = viewModel(factory = AppViewModelProvider.Factory),
    itemUiState: ItemUiState,
    onItemValueChange: (ItemDetails) -> Unit = viewModel::updateUiState,
    onSaveClick: () -> Unit,
//    coroutineScope: CoroutineScope,
    modifier: Modifier = Modifier
) {
    val itemDetails = itemUiState.itemDetails
    val enabledUsed = itemUiState.enabledUsed

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        EntryPhoto(
            itemDetails = itemDetails,
            onItemValueChange = onItemValueChange
        )
        EntryIconAndText(
            image = Icons.Default.Person,
            value = itemDetails.name,
            onValueChange = { newValue -> onItemValueChange(itemDetails.copy(name = newValue)) },
            label = "Name",
            index = -1,
            isError = itemUiState.nameError
        )
        EntryText(
            value = itemDetails.surname,
            onValueChange = { newValue -> onItemValueChange(itemDetails.copy(surname = newValue)) },
            label = "Surname",
            isError = ""
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
                index = i, itemDetails = itemDetails, onItemValueChange = onItemValueChange
            )
        }
        EntryIconAndText(
            image = Icons.Default.MailOutline,
            value = itemDetails.email,
            onValueChange = { newValue -> onItemValueChange(itemDetails.copy(email = newValue)) },
            label = "Email",
            index = -1,
            isError = itemUiState.emailError
        )
        TextField(
            value = itemDetails.notes,
            onValueChange = { newValue -> onItemValueChange(itemDetails.copy(notes = newValue)) },
            label = { Text("Notes") },
            modifier = Modifier.padding(8.dp),
            supportingText = {
                Text(
                    text = "${itemDetails.notes.length} / 20",
                    textAlign = TextAlign.End,
                )
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text, imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(onDone = {
                onSaveClick()
            })

        )
    }
}

@Composable
fun EntryPhoto(
    itemDetails: ItemDetails, onItemValueChange: (ItemDetails) -> Unit
) {

    val context = androidx.compose.ui.platform.LocalContext.current


    val pickMediaLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
    ) { uri: Uri? ->
        if (uri != null) {
            context.contentResolver.takePersistableUriPermission(
                uri,
                Intent.FLAG_GRANT_READ_URI_PERMISSION
            )
            onItemValueChange(itemDetails.copy(photo = uri))
        }
    }


    val painter: Painter = rememberAsyncImagePainter(model = itemDetails.photo.toString())

    Card(
        modifier = Modifier
            .padding(16.dp)
            .clip(RoundedCornerShape(96.dp))
            .size(192.dp)
    ) {
        if (itemDetails.photo.toString().isNotBlank()) {
            Image(
                painter = painter,
                contentDescription = "Photo",
//                modifier = Modifier.size(192.dp),
                contentScale = androidx.compose.ui.layout.ContentScale.Crop
            )
        } else {
            val initial = if (itemDetails.name.isNotEmpty()) {
                itemDetails.name.first().uppercaseChar().toString()
            } else {
                ""
            }
            Box(
                modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
            ) {
                Text(
                    text = initial, style = MaterialTheme.typography.headlineLarge
                )
            }
        }
    }
    Button(onClick = {
        pickMediaLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }) {
        Text("Add Photo")
    }

}

@Composable
fun EntryNumberAndType(
    index: Int,
    itemDetails: ItemDetails,
    onItemValueChange: (ItemDetails) -> Unit,
    viewModel: EntryViewModel = viewModel(factory = AppViewModelProvider.Factory)
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
        isError = if (index == 0) {
            viewModel.itemUiState.numberError
        } else {
            ""
        },
    )
    EntryDrop(
        types = NumberTypes.values().map { it.name },
        value = itemDetails.numberTypes[index],
        onValueChange = { newValue ->
            val updatedNumberList = itemDetails.numberTypes.toMutableList()
            updatedNumberList[index] = newValue
            onItemValueChange(itemDetails.copy(numberTypes = updatedNumberList))
        },
        label = "Type"
    )
}

@OptIn(ExperimentalMaterial3Api::class)
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
    Row {
        ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = it }) {
            OutlinedTextField(
                value = value,
                label = { Text(label) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = color,
                    unfocusedTextColor = color,
                ),
                readOnly = true,
                onValueChange = {},
                modifier = Modifier
                    .menuAnchor()
                    .padding(8.dp)
            )
            ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                typesAndColor.forEach { (type, color) ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(4.dp)
                    ) {
                        Image(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            colorFilter = ColorFilter.tint(color)
                        )
                        DropdownMenuItem(text = { Text(text = type, color = color) }, onClick = {
                            onValueChange(type)
                            expanded = false
                        })
                    }
                }
                for (type in types) {
                    DropdownMenuItem(text = { Text(text = type) }, onClick = {
                        onValueChange(type)
                        expanded = false
                    })
                }
            }
        }
    }
}

@Composable
fun EntryIconAndText(
    image: ImageVector,
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    index: Int = 0,
    isError: String,
    viewModel: EntryViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            imageVector = image,
            contentDescription = null,
            modifier = Modifier
                .padding(start = 4.dp, end = 8.dp)
                .size(24.dp)
        )
        EntryText(value = value, onValueChange = onValueChange, label = label, isError = isError)

        if (index > 0) {
            FilledIconButton(
                onClick = { viewModel.deleteNumber(index) }, modifier = Modifier.size(36.dp)
            ) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = null)
            }
        } else if (index == 0 && viewModel.itemUiState.isEnabledMore) {
            FilledIconButton(
                onClick = { viewModel.addMoreNumbers() }, modifier = Modifier.size(36.dp)
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = null)
            }
        } else {
            Spacer(modifier = Modifier.size(36.dp))
        }
    }
}

@Composable
fun EntryText(
    value: String, onValueChange: (String) -> Unit, label: String, isError: String
) {
    OutlinedTextField(
        value = value,
        onValueChange = { newValue ->
            onValueChange(newValue)
        },
        label = { Text(label) },
        modifier = Modifier.padding(8.dp, 8.dp, 8.dp, 0.dp),
        maxLines = 1,
        isError = isError.isNotBlank(),
        trailingIcon = {
            if (isError.isNotBlank()) {
                Icon(
                    imageVector = Icons.Default.Warning, contentDescription = null, tint = Color.Red
                )
            }
        },
        supportingText = {
            if (isError.isNotBlank()) {
                Text(
                    text = isError, color = Color.Red
                )
            }
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = when (label) {
                "Phone" -> KeyboardType.Phone
                "Email" -> KeyboardType.Email
                else -> KeyboardType.Text
            }, imeAction = ImeAction.Next
        )
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewEntryScreen() {
//    EntryScreen()
    Column {
//        EntryIconAndTextAndDrop(itemDetails = ItemDetails(), onItemValueChange = {})

    }
}