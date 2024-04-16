package com.example.phonebookapp.ui.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.phonebookapp.data.Item
import com.example.phonebookapp.data.ItemsRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HomeViewModel(private val itemsRepository: ItemsRepository) : ViewModel() {
    var homeUiState by mutableStateOf(HomeUiState())
        private set

    val stateFlow: StateFlow<HomeUiState> =
        itemsRepository.getAllItemsStream().map { HomeUiState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = HomeUiState()
            )

    init {
        // Obserwuj zmiany w stateFlow i aktualizuj homeUiState
        viewModelScope.launch {
            stateFlow.collect { newState ->
                homeUiState = newState
                fillAlphabetItemLists()
            }
        }
    }

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }

    fun fillAlphabetItemLists() {
        val alphabetItemLists = mutableListOf<List<Item>>()
        val alphabet = ('A'..'Z').toList()
        for (letter in alphabet) {
            val items = homeUiState.itemList.filter { it.name.uppercase().first() == letter }
            if (items.isNotEmpty()) {
                alphabetItemLists.add(items)
            }
        }
        homeUiState=homeUiState.copy(alphabetItemLists = alphabetItemLists)
    }

    suspend fun addItem(item: Item) {
        itemsRepository.insertItem(item)
    }

    suspend fun deleteAllItems(items: List<Item>) {
        if (items.isNotEmpty()) {
            itemsRepository.deleteAllItems(items)
        }
    }
}

data class HomeUiState(
    val itemList: List<Item> = listOf(),
    val alphabetItemLists: List<List<Item>> = listOf()
)