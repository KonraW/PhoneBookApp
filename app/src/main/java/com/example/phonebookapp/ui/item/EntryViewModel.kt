package com.example.phonebookapp.ui.item

import android.util.Log
import android.util.Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.phonebookapp.data.Item
import com.example.phonebookapp.data.ItemsRepository
import com.example.phonebookapp.data.NumberTypes

data class ItemUiState(
    val itemDetails: ItemDetails = ItemDetails(),
    val isEntryValid: Boolean = false,
    val isEnabledMore: Boolean = true,
//    val enabledMore: Int = NumberTypes.values().size,
    val enabledUsed: Int = 0
)

class EntryViewModel(private val itemsRepository: ItemsRepository) : ViewModel() {
    var itemUiState by mutableStateOf(ItemUiState())
        private set

    fun updateUiState(itemDetails: ItemDetails) {
//        itemUiState = ItemUiState(
//            itemDetails = itemDetails,
//            isEntryValid = validateInput(itemDetails)
//        )
        itemUiState = itemUiState.copy(
            itemDetails = itemDetails,
            isEntryValid = validateInput(itemDetails)
        )
    }

    fun addMoreNumbers() {
        if (itemUiState.isEnabledMore) {
            Log.d("EntryViewModel", "addMore: ${itemUiState.enabledUsed}")
//            itemUiState = ItemUiState(
//                itemDetails = itemUiState.itemDetails,
//                isEntryValid = itemUiState.isEntryValid,
//                isEnabledMore = itemUiState.enabledUsed < NumberTypes.values().size,
//                enabledUsed = itemUiState.enabledUsed + 1
//            )
            val numberTypesList = itemUiState.itemDetails.numberTypes.toMutableList()
            val numberList = itemUiState.itemDetails.number.toMutableList()
            for (i in 0 until NumberTypes.values().size) {
                if (numberTypesList.contains(NumberTypes.values()[i].name)) {
                    continue
                }
                numberTypesList.add(NumberTypes.values()[i].name)
                numberList.add("")
                break
            }
//            numberTypesList.add("HOME")
//            numberList.add("")
//            itemDetails = itemDetails.copy(numberTypes = numberTypesList)

            itemUiState = itemUiState.copy(
                itemDetails = itemUiState.itemDetails.copy(
                    number = numberList,
                    numberTypes = numberTypesList
                ),
                enabledUsed = itemUiState.enabledUsed + 1,
                isEnabledMore = itemUiState.enabledUsed+2 < NumberTypes.values().size
            )
        }
    }

    fun deleteNumber(index: Int){
        if (itemUiState.enabledUsed > 0) {
            Log.d("EntryViewModel", "deleteNumber: ${itemUiState.enabledUsed}")
            val numberTypesList = itemUiState.itemDetails.numberTypes.toMutableList()
            val numberList = itemUiState.itemDetails.number.toMutableList()
            numberTypesList.removeAt(index)
            numberList.removeAt(index)
            itemUiState = itemUiState.copy(
                itemDetails = itemUiState.itemDetails.copy(
                    number = numberList,
                    numberTypes = numberTypesList
                ),
                enabledUsed = itemUiState.enabledUsed - 1,
                isEnabledMore = itemUiState.enabledUsed < NumberTypes.values().size
            )
        }
    }

    suspend fun saveItem() {
        // if (validateInput()) {
        itemsRepository.insertItem(itemUiState.itemDetails.toItem())
        // }
    }

    private fun validateInput(uiState: ItemDetails = itemUiState.itemDetails): Boolean {
        return with(uiState) {
            name.isNotBlank() && number.isNotEmpty() && email.isNotBlank() && address.isNotBlank()
                    && isValidPhone(number) //&& isValidEmail(email)
        }
    }


    private fun isValidPhone(phones: List<String>): Boolean {
        for (phone in phones) {
            if (phone.trim().length !in 9..13 || !Patterns.PHONE.matcher(phone).matches()) {
                return false
            }
        }
        return true
//        return phone.trim().length in 9..13 && Patterns.PHONE.matcher(phone).matches()
    }

//    private fun isValidEmail(email: String): Boolean {
//        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"
//        return email.matches(emailRegex.toRegex())
//    }
}

data class ItemDetails(
    val id: Int = 0,
    val name: String = "",
    val surname: String = "",
    val category: String = "FAMILY",
    val number: List<String> = listOf(""),
    val numberTypes: List<String> = listOf("HOME"),//NumberTypes= NumberTypes.HOME,
    val email: String = "",
    val address: String = "",
    val notes: String = ""
) {
    fun toItem(): Item = Item(
        id = id,
        name = name,
        surname = surname,
        category = category,
        number = number,
        numberType = numberTypes,
        email = email,
        notes = notes
    )
}