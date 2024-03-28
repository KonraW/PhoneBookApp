package com.example.phonebookapp.ui.item

import android.util.Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.phonebookapp.data.Item
import com.example.phonebookapp.data.ItemsRepository

data class ItemUiState(
    val itemDetails: ItemDetails = ItemDetails(),
    val isEntryValid: Boolean = false
)

class EntryViewModel(private val itemsRepository: ItemsRepository) : ViewModel(){
    var itemUiState by mutableStateOf(ItemUiState())
        private set

    fun updateUiState(itemDetails: ItemDetails) {
        itemUiState = ItemUiState(
            itemDetails = itemDetails,
            isEntryValid = validateInput(itemDetails)
        )
    }

    private fun validateInput(uiState: ItemDetails = itemUiState.itemDetails): Boolean {
        return with(uiState) {
            name.isNotBlank() && number.isNotBlank() && email.isNotBlank() && address.isNotBlank()
                    && isValidPhone(number) && isValidEmail(email)
        }
    }

    suspend fun saveItem() {
        if (validateInput()) {
            itemsRepository.insertItem(itemUiState.itemDetails.toItem())
        }
    }

    private fun isValidPhone(phone: String): Boolean {
        return phone.trim().length in 9..13 && Patterns.PHONE.matcher(phone).matches()
    }

    private fun isValidEmail(email: String): Boolean {
        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"
        return email.matches(emailRegex.toRegex())
    }
}

data class ItemDetails(
    val id: Int = 0,
    val name: String = "",
    val number: String = "",
    val email: String = "",
    val address: String = "",
    val notes: String = ""
) {
    fun toItem(): Item = Item(
        id = id,
        name = name,
        number = number,
        email = email,
        address = address,
        notes = notes
    )
}