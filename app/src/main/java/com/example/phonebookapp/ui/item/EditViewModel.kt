package com.example.phonebookapp.ui.item

import android.util.Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.phonebookapp.data.ItemsRepository
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class EditViewModel(
    savedStateHandle: SavedStateHandle, private val itemsRepository: ItemsRepository) : ViewModel() {
    var itemUiState by mutableStateOf(ItemUiState())
        private set

    private val itemId: Int = checkNotNull(savedStateHandle[EditDestination.itemIdArg])

    init {
        viewModelScope.launch {
            itemUiState = itemsRepository.getItemStream(itemId)
                .filterNotNull()
                .first()
                .toItemUiState(true)
        }
    }

    suspend fun updateItem() {
        if (validateInput()) {
            itemsRepository.updateItem(itemUiState.itemDetails.toItem())
        }
    }
    fun updateUiState(itemDetails: ItemDetails) {
        if (itemUiState.itemDetails.name != itemDetails.name) {
            validateName(itemDetails.name)
        }
        if (itemUiState.itemDetails.number[0] != itemDetails.number[0]) {
            validateNumber(itemDetails.number[0])
        }
        if (itemUiState.itemDetails.email != itemDetails.email) {
            isValidEmail(itemDetails.email)
        }
        if (validateTexts(itemDetails)) {
            itemUiState = itemUiState.copy(
                itemDetails = itemDetails, isEntryValid = validateInput(itemDetails)
            )
        }
    }

    private fun validateTexts(uiState: ItemDetails): Boolean {
        val textRegex = "^[A-Za-z0-9+_.@-]{0,20}$".toRegex()

        return with(uiState) {
            validatePhone(uiState) && name.matches(textRegex) && surname.matches(textRegex) && category.matches(
                textRegex
            ) && email.matches(textRegex) && notes.matches(textRegex)
        }
    }
    private fun validatePhone(uiState: ItemDetails): Boolean {
        val phoneRegex = "^\\+?[0-9]{0,20}$".toRegex()

        for (phone in uiState.number) {
            if (!phone.matches(phoneRegex)) {
                return false
            }
        }
        return true
    }
    private fun validateInput(uiState: ItemDetails = itemUiState.itemDetails): Boolean {
        return with(uiState) {
            validateName(name) and validateNumber(number[0]) and isValidEmail(email)
        }
    }

    private fun validateName(name: String): Boolean {
        var nameError = ""
        if (name.isBlank()) {
            nameError = "Name is required"
        }
        itemUiState = itemUiState.copy(
            nameError = nameError
        )
        return nameError.isBlank()
    }

    private fun validateNumber(number: String): Boolean {
        var numberError = ""
        if (number.isBlank()) {
            numberError = "Number is required"
        }
        itemUiState = itemUiState.copy(
            numberError = numberError
        )
        return numberError.isBlank()
    }

    private fun isValidEmail(email: String): Boolean {
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches() && email.isNotBlank()) {
            itemUiState = itemUiState.copy(
                emailError = "Invalid email"
            )
            return false
        }
        itemUiState = itemUiState.copy(
            emailError = ""
        )
        return true
    }
}