package com.example.phonebookapp.ui.home

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import coil.request.ImageRequest
import com.example.phonebookapp.PhoneBookTopAppBar
import com.example.phonebookapp.data.Category
import com.example.phonebookapp.data.Item
import com.example.phonebookapp.ui.AppViewModelProvider
import com.example.phonebookapp.ui.navigation.NavigationDestination
import com.example.phonebookapp.ui.theme.PhoneBookAppTheme
import kotlinx.coroutines.launch

object HomeDestination : NavigationDestination {
    override val route = "home"
    override val titleRes = "Phone Book App"
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navigateToItemEntry: () -> Unit,
    navigateToItemDetails: (Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
//    val homeUiState by viewModel.stateFlow.collectAsState()
    val homeUiState = viewModel.homeUiState

    val coroutineScope = rememberCoroutineScope()
    //Add a new item to the list
//    coroutineScope.launch {
//        viewModel.addItem(Item(4, "New Person", 123456789))
//    }
//    coroutineScope.launch {
//        viewModel.addItem(Item(name="New Person 123", number=9893219))
//    }
//    coroutineScope.launch {
//        viewModel.addItem(Item(4, "New Person", 123456789))
//    }

//    coroutineScope.launch {
//        viewModel.deleteAllItems(homeUiState.itemList)
//    }
    Scaffold(
        topBar = {

            PhoneBookTopAppBar(
                title = "home",
                canNavigateBack = false,
                canClickButton = true,
                onClickButton = {
                    navigateToItemEntry()
                },
                buttonIcon = Icons.Default.Add
            )
        }
    ) { innerPadding ->
        PeopleList(
            homeUiState.itemList,
            homeUiState.alphabetItemLists,
            onClick = {
                coroutineScope.launch {
                    viewModel.deleteAllItems(homeUiState.itemList)
                }
            },
            navigateToItemUpdate = navigateToItemDetails,
//        onItemClick = {
//        },
            modifier = Modifier.padding(innerPadding)
        )
    }


}




//@Composable
//fun PickFileButton() {
//    val context = LocalContext.current
//    val pickFileLauncher = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
//        // Handle the returned Uri
//        uri?.let {
//            // Here you can handle the Uri, for example, by updating your ViewModel
//            // or by using the Uri directly in your Composable
//            // For example, to persist access to the Uri across app restarts:
//            val takeFlags = Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
//            context.contentResolver.takePersistableUriPermission(it, takeFlags)
//        }
//    }
//
//    Button(onClick = { pickFileLauncher.launch("image/*") }) {
//        Text("Pick a File")
//    }
//}


@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun PeopleList(
    itemList: List<Item>,
    alphabetItemLists: List<List<Item>>,
    onClick: () -> Unit = {},
    navigateToItemUpdate: (Int) -> Unit,
//    onItemClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Column {
//        Button(onClick = onClick) {
//
//        }

        val state: LazyListState = rememberLazyListState()

        val sections = alphabetItemLists.map { it.first().name.uppercase().first().toString() }

        LazyColumn(
            state = state,
            modifier = modifier,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            sections.forEach { section ->
                stickyHeader {
                    Row(
                    ) {
                        Text(
                            section,
                            style = MaterialTheme.typography.headlineLarge,
                            modifier = Modifier.padding(start = 16.dp, top = 4.dp, bottom = 4.dp)
                        )
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }

//                stickyHeader { Text("Header") }
                items(items = itemList, key = { section + it.id }) {
//                    Box(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(16.dp)
//                    ) {
//                        Text(text = "Item ${it.id}", Modifier.size(0.dp))
//                    }
                    if (it.name.uppercase().first() == section[0]) {
                        PersonRow(item = it, onItemClick = { navigateToItemUpdate(it.id) })
                    }
                }
            }
        }
//        }


    }
}

@Composable
private fun PersonRow(item: Item, onItemClick: () -> Unit) {
    Card(
        modifier = Modifier
//            .fillMaxWidth()
            .padding(start = 48.dp, end = 8.dp, top = 8.dp, bottom = 8.dp)
            .clickable(onClick = onItemClick),
//            .border(1.dp, Color.Gray, MaterialTheme.shapes.medium),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            MaterialTheme.colorScheme.secondaryContainer,
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
//                .clickable(onClick = onItemClick),

//        horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            PersonIcon(item = item)
            val nameAndSurname = if (item.surname.isNotEmpty()) {
                "${item.name} ${item.surname}"
            } else {
                item.name
            }
            Text(
                text = if (nameAndSurname.length > 16) nameAndSurname.substring(0, 13)
                    .plus("...") else nameAndSurname,
            )//, modifier = Modifier.weight(1f))
            Spacer(modifier = Modifier.weight(1f))
            if (item.category != "NONE") {
                PersonCategory(
                    text = item.category,
                    icon = Icons.Default.Person,
                    color = Category.valueOf(item.category).color
                )
            }
//        Text(text = item.number.toString())
//        Spacer(modifier = Modifier.weight(1f))
//        Icon(
//            imageVector = Icons.Default.Person,
//            contentDescription = null,
//
//        )
        }
    }

}

@Composable
fun PersonCategory(text: String, icon: ImageVector, color: Color) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(end = 16.dp),
    ) {
        Image(
            contentDescription = "Category",
            imageVector = icon,
            modifier = Modifier.size(24.dp),
            colorFilter = ColorFilter.tint(color)
        )
        Spacer(modifier = Modifier.padding(4.dp))
        Text(text = text.lowercase(), color = color)
    }
}


@Composable
private fun PersonIcon(item: Item) {
    val (initial, color) = if (item.name.isNotEmpty() and (item.photo.toString().isEmpty())){
        item.name.first().uppercaseChar().toString() to generateUniqueColor(
            item.id,
            item.name.first(),
            item.number.first().toString()
        )
    } else {
        "" to Color.Gray
    }
    Box(modifier = Modifier.padding(16.dp)) {
        Card(
            colors = CardDefaults.cardColors(
                color
            ),
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape),


            ) {
            if (item.photo.toString().isNotEmpty()) {
                val image = item.photo

//                val painter: Painter = rememberAsyncImagePainter(
//                    model = ImageRequest.Builder(LocalContext.current)
//                        .data(image)
//                        .size(coil.size.Size.ORIGINAL) // Set the target size to load the image at.
//                        .build()
//                        .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
//                )
                val painter: Painter = rememberAsyncImagePainter(
                    model = image,
//                    size = Size.ORIGINAL // Set the target size to load the image at.
                )
                val painter2: Painter= rememberImagePainter(data = item.photo,builder= {
                    crossfade(true)
                })

                Image(
                    painter = painter2,
                    contentDescription = null,
                    modifier = Modifier.size(100.dp),
                    contentScale = androidx.compose.ui.layout.ContentScale.Crop
                )
            } else {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = initial,
                        style = MaterialTheme.typography.headlineLarge
                    )
                }
//            Box(
//                modifier = Modifier.fillMaxSize(),
//                contentAlignment = Alignment.Center
//            ) {
//                Text(
//                    text = initial,
//                    style = MaterialTheme.typography.headlineLarge
//                )
//            }
            }
        }
    }
}

fun generateUniqueColor(id: Int, initial: Char, number: String): Color {
    val red = (id.hashCode() * 17) % 128 + 128
    val green = (initial.hashCode() * 31) % 128 + 128
    val blue = (number.hashCode() * 47) % 128 + 128

    return Color(red = red / 255f, green = green / 255f, blue = blue / 255f)
}

@Preview()
@Composable
fun GreetingPreview() {
    PhoneBookAppTheme {
//        PersonIcon(
//            item = Item(
//                1,
//                "Xyz",
//                "cos",
//                "999888333",
//                "asf",
//                listOf("asdfa"),
//                listOf("asdfa"),
//                "asdfa",
//                "asdfa"
//            )
//        )
//        PeopleList(
//            listOf(
//                Item(1, "Xyz", "cos","999888333","asf","asdfa","asdfa"),
//            )
//        )
    }
}
