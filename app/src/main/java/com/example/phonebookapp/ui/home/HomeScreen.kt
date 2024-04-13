package com.example.phonebookapp.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
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
    val homeUiState by viewModel.homeUiState.collectAsState()

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

@Composable
private fun PeopleList(
    itemList: List<Item>,
    onClick: () -> Unit = {},
    navigateToItemUpdate: (Int) -> Unit,
//    onItemClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Column {
//        Button(onClick = onClick) {
//
//        }

        LazyColumn(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
            items(items = itemList, key = { it.id }) {
                PersonRow(item = it, onItemClick = { navigateToItemUpdate(it.id) })
            }
        }
    }
}

@Composable
private fun PersonRow(item: Item, onItemClick: () -> Unit) {
    Card (
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 64.dp, end = 8.dp, top = 8.dp, bottom = 8.dp)
            .clickable(onClick = onItemClick),
//            .border(1.dp, Color.Gray, MaterialTheme.shapes.medium),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            MaterialTheme.colorScheme.secondaryContainer,
        )
    ){
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
            .padding(end=16.dp),
    ) {
        Image(
            imageVector = icon,
            contentDescription = "Category",
            modifier = Modifier.size(24.dp),
            colorFilter = ColorFilter.tint(color)
        )
        Spacer(modifier = Modifier.padding(4.dp))
        Text(text = text.lowercase(), color = color)
    }
}


@Composable
private fun PersonIcon(item: Item) {
    val (initial, color) = if (item.name.isNotEmpty()) {
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
            modifier = Modifier.size(48.dp),
            shape = CircleShape


        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = initial,
                    style = MaterialTheme.typography.headlineLarge
                )
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