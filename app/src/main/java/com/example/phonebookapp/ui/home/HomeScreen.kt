package com.example.phonebookapp.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.phonebookapp.PhoneBookTopAppBar
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
    Scaffold (
        topBar={

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
    ){innerPadding ->
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

        LazyColumn(modifier = modifier) {
            items(items = itemList, key = { it.id }) {
                PersonRow(item = it, onItemClick = { navigateToItemUpdate(it.id) })
            }
        }
    }
}

@Composable
private fun PersonRow(item: Item, onItemClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable(onClick = onItemClick),
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