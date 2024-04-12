package com.example.phonebookapp.ui.item

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.phonebookapp.PhoneBookTopAppBar
import com.example.phonebookapp.ui.AppViewModelProvider
import com.example.phonebookapp.ui.navigation.NavigationDestination
import kotlinx.coroutines.launch

object EditDestination : NavigationDestination {
    override val route = "item_edit"
    override val titleRes = "Edit Item"
    const val itemIdArg = "itemId"
    val routeWithArgs = "$route/{$itemIdArg}"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditScreen(
    onNavigateUp: () -> Unit,
    navigateBack: () -> Unit,
    navigateToItemDetails: (Int) -> Unit,
    viewModel: EntryViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val coroutineScope = rememberCoroutineScope()

    Scaffold(topBar = {
//        EntryTopBar(onSaveClick = { coroutineScope.launch { viewModel.updateItem() } })
        PhoneBookTopAppBar(
            title = "edit",
            canNavigateBack = true,
            navigateUp = onNavigateUp,
            canClickButton = true,
            onClickButton = {
                coroutineScope.launch {
                    viewModel.updateItem()
                    if (viewModel.validateInput()) {
                        navigateToItemDetails(viewModel.itemUiState.itemDetails.id)
                    }
                }
            },
            buttonIcon = Icons.Default.Done
        )
    }) { innerPadding ->
        EntryBody(
//            viewModel = viewModel,
            itemUiState = viewModel.itemUiState,
            onItemValueChange = viewModel::updateUiState,
            onSaveClick = {
                coroutineScope.launch {
                    viewModel.updateItem()
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