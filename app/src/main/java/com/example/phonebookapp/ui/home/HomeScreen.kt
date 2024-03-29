package com.example.phonebookapp.ui.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.phonebookapp.data.Item
import com.example.phonebookapp.ui.AppViewModelProvider
import com.example.phonebookapp.ui.theme.PhoneBookAppTheme
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    name: String,
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

    PeopleList(
        homeUiState.itemList,
        onClick = {
            coroutineScope.launch {
                viewModel.deleteAllItems(homeUiState.itemList)
            }
        },
        modifier = modifier
    )
}

@Composable
private fun PeopleList(
    itemList: List<Item>,
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Column {
        Button(onClick = onClick) {

        }

        LazyColumn(modifier = modifier) {
            items(items = itemList, key = { it.id }) {
                PersonRow(item = it)
            }
        }
    }
}

@Composable
private fun PersonRow(item: Item) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
//        horizontalArrangement = Arrangement.SpaceBetween,
//        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = item.id.toString(), modifier = Modifier.padding(end = 16.dp))
        Text(text = item.name, modifier = Modifier.weight(1f))
        Text(text = item.number.toString())
        Spacer(modifier = Modifier.weight(1f))
        Icon(
            imageVector = Icons.Default.Person,
            contentDescription = null
        )
    }

}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    PhoneBookAppTheme {
//        PeopleList(
//            listOf(
//                Item(1, "Xyz", "cos","999888333","asf","asdfa","asdfa"),
//            )
//        )
    }
}