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
    val enabledUsed: Int = 0,
    val nameError: String = "",
    val numberError: String = "",
    val emailError: String = "",
)

class EntryViewModel(private val itemsRepository: ItemsRepository) : ViewModel() {
    var itemUiState by mutableStateOf(ItemUiState())
        private set

    fun updateUiState(itemDetails: ItemDetails) {
//        itemUiState = ItemUiState(
//            itemDetails = itemDetails,
//            isEntryValid = validateInput(itemDetails)
//        )
//        if (itemDetails.notes.length<=10){
//            itemDetails.notes=itemDetails.notes.substring(0,10)
//        }
//        val newNotes = if (itemDetails.notes.length > 20) {
//            itemDetails.notes.substring(0, 20)
//        } else {
//            itemDetails.notes
//        }

        if (itemUiState.itemDetails.name != itemDetails.name) {
            validateName(itemDetails.name)
        }
        if (itemUiState.itemDetails.number[0] != itemDetails.number[0]) {
            validateNumber(itemDetails.number[0])
        }
        if (itemUiState.itemDetails.email != itemDetails.email) {
            isValidEmail(itemDetails.email, itemUiState)
        }
        if (limitText(itemDetails)) {
            itemUiState = itemUiState.copy(
                itemDetails = itemDetails,//.copy(notes = newNotes),
                isEntryValid = validateInput(itemDetails)
            )
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
                isEnabledMore = itemUiState.enabledUsed + 2 < NumberTypes.values().size
            )
        }
    }

    fun deleteNumber(index: Int) {
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
        if (validateInput()) {
            itemsRepository.insertItem(itemUiState.itemDetails.toItem())
        }
    }

    private fun limitText(uiState: ItemDetails): Boolean {
        val textRegex = "^[A-Za-z0-9+_.@-]{0,20}$".toRegex()

        return with(uiState) {
            limitPhoneText(uiState) && name.matches(textRegex) && surname.matches(textRegex) && category.matches(
                textRegex
            ) && email.matches(textRegex) && notes.matches(textRegex)
        }
    }

    private fun limitPhoneText(uiState: ItemDetails): Boolean {
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
            //textFieldsNotEmpty(uiState) && isValidEmail(email)
            validateName(name) and validateNumber(number[0]) and isValidEmail(email, itemUiState)
        }
    }


    //    private fun isValidPhone(phones: List<String>): Boolean {
//        for (phone in phones) {
//            if (phone.trim().length !in 9..13 || !Patterns.PHONE.matcher(phone).matches()) {
//                return false
//            }
//        }
//        return true
////        return phone.trim().length in 9..13 && Patterns.PHONE.matcher(phone).matches()
//    }
//    private fun textFieldsNotEmpty(uiState: ItemDetails): Boolean {
//        with(uiState) {
//            if (name.isBlank() && number[0].isBlank()) {
//                return false
//            }
//        }
//        return true
//    }

    private fun isValidEmail(email: String, uiState: ItemUiState): Boolean {
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

data class ItemDetails(
    val id: Int = 0,
    val name: String = "",
    val surname: String = "",
    val category: String = "FAMILY",
    val number: List<String> = listOf(""),
    val numberTypes: List<String> = listOf("HOME"),//NumberTypes= NumberTypes.HOME,
    val email: String = "",
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