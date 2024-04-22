package com.example.phonebookapp.ui.item

import android.net.Uri
import android.util.Log
import android.util.Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.phonebookapp.data.Item
import com.example.phonebookapp.data.ItemsRepository
import com.example.phonebookapp.data.NumberTypes
import com.example.phonebookapp.ui.home.HomeUiState
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class ItemUiState(
    val itemDetails: ItemDetails = ItemDetails(),
    val isEntryValid: Boolean = false,
    val isEnabledMore: Boolean = true,
    val enabledUsed: Int = 0,
    val nameError: String = "",
    val numberError: String = "",
    val emailError: String = "",
)

class EntryViewModel( private val itemsRepository: ItemsRepository) : ViewModel() {
    var itemUiState by mutableStateOf(ItemUiState())
        private set

//    val uiState: StateFlow<ItemUiState> =
//        itemsRepository.getAllItemsStream().map { ItemUiState() }
//            .stateIn(
//                scope=viewModelScope,
//                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
//                initialValue = ItemUiState()
//            )
//    val uiState: StateFlow<ItemDetailsUiState> =
//        itemsRepository.getItemStream(itemId)
//            .filterNotNull()
//            .map { ItemDetailsUiState(itemDetails = it.toItemDetails()) }
//            .stateIn(
//                scope = viewModelScope,
//                started = SharingStarted.WhileSubscribed(DetailsViewModel.TIMEOUT_MILLIS),
//                initialValue = ItemDetailsUiState()
//            )

//    companion object {
//        private const val TIMEOUT_MILLIS = 5_000L
//    }


    constructor(
        savedStateHandle: SavedStateHandle?,
        itemsRepository: ItemsRepository
    ) : this(itemsRepository) {
        savedStateHandle?.let { handle ->
            val itemId: Int? = handle[EditDestination.itemIdArg]
            if (itemId != null) {
                viewModelScope.launch {
                    itemUiState = itemsRepository.getItemStream(itemId)
                        .filterNotNull()
                        .first()
                        .toItemUiState(true)

                    itemUiState = itemUiState.copy(
                        enabledUsed = itemUiState.itemDetails.number.size-1,
                        isEnabledMore = itemUiState.itemDetails.number.size < NumberTypes.values().size
                    )
                }

            }
//            viewModelScope.launch {
//                itemUiState = itemsRepository.getItemStream(itemId)
//                    .filterNotNull()
//                    .first()
//                    .toItemUiState(true)
//            }
        }
    }

    suspend fun saveItem() {
        if (validateInput()) {
            deleteEmptyNumbers()
            itemsRepository.insertItem(itemUiState.itemDetails.toItem())
//            val itemId = itemsRepository.insertItem(itemUiState.itemDetails.toItem())
            // Update the itemDetails with the new ID
            itemUiState = itemUiState.copy(
                itemDetails = itemUiState.itemDetails.copy(id = itemsRepository.getLastItemId().first()!!)
            )
            //sort the list
//            itemsRepository.sortItems()
        }
    }

    suspend fun deleteItem() {
        itemsRepository.deleteItem(itemUiState.itemDetails.toItem())
    }

    suspend fun updateItem() {
        if (validateInput()) {
            deleteEmptyNumbers()
            itemsRepository.updateItem(itemUiState.itemDetails.toItem())
//            itemsRepository.sortItems()
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

    fun addMoreNumbers() {
        if (itemUiState.isEnabledMore) {
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
            itemUiState = itemUiState.copy(
                itemDetails = itemUiState.itemDetails.copy(
                    number = numberList, numberTypes = numberTypesList
                ),
                enabledUsed = itemUiState.enabledUsed + 1,
                isEnabledMore = itemUiState.enabledUsed + 2 < NumberTypes.values().size
            )
        }
    }

    fun deleteNumber(index: Int) {
        if (itemUiState.enabledUsed > 0) {
            val numberTypesList = itemUiState.itemDetails.numberTypes.toMutableList()
            val numberList = itemUiState.itemDetails.number.toMutableList()
            numberTypesList.removeAt(index)
            numberList.removeAt(index)
            itemUiState = itemUiState.copy(
                itemDetails = itemUiState.itemDetails.copy(
                    number = numberList, numberTypes = numberTypesList
                ),
                enabledUsed = itemUiState.enabledUsed - 1,
                isEnabledMore = itemUiState.enabledUsed < NumberTypes.values().size
            )
        }
    }


    private fun deleteEmptyNumbers() {
        val numberList = itemUiState.itemDetails.number.toMutableList()
        val numberTypesList = itemUiState.itemDetails.numberTypes.toMutableList()
        var j=0
        for (i in 0 until itemUiState.itemDetails.number.size) {
            if (itemUiState.itemDetails.number[j].isBlank()) {
                numberList.removeAt(j)
                numberTypesList.removeAt(j)
                j-=1
            } else{
                numberList[j] = numberList[j].replace("\\s".toRegex(), "")
            }
            j+=1
        }

        itemUiState = itemUiState.copy(
            itemDetails = itemUiState.itemDetails.copy(
                number = numberList,
                numberTypes = numberTypesList
            )
        )
        itemUiState = itemUiState.copy(
            enabledUsed = itemUiState.itemDetails.number.size-1,
            isEnabledMore = itemUiState.itemDetails.number.size < NumberTypes.values().size
        )
    }

    private fun validateTexts(uiState: ItemDetails): Boolean {
        val textRegex = "^[A-Za-z0-9+_.@\\- ]{0,25}$".toRegex()

        return with(uiState) {
            validatePhone(uiState) && name.matches(textRegex) && surname.matches(textRegex) && category.matches(
                textRegex
            ) && email.matches(textRegex) && notes.matches(textRegex)
        }
    }

    private fun validatePhone(uiState: ItemDetails): Boolean {
        val phoneRegex = "^\\+?[0-9\\- ]{0,25}$".toRegex()

        for (phone in uiState.number) {
            if (!phone.matches(phoneRegex)) {
                return false
            }
        }
        return true
    }

    fun validateInput(uiState: ItemDetails = itemUiState.itemDetails): Boolean {
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

data class ItemDetails(
    val id: Int = 0,
    val photo: Uri = Uri.EMPTY,
    val name: String = "",
    val surname: String = "",
    val category: String = "FAMILY",
    val number: List<String> = listOf(""),
    val numberTypes: List<String> = listOf("HOME"),
    val email: String = "",
    val notes: String = ""
) {
    fun toItem(): Item = Item(
        id = id,
        photo = photo,
        name = name,
        surname = surname,
        category = category,
        number = number,
        numberType = numberTypes,
        email = email,
        notes = notes
    )

}

fun Item.toItemDetails(): ItemDetails = ItemDetails(
    id = id,
    photo = photo,
    name = name,
    surname = surname,
    category = category,
    number = number,
    numberTypes = numberType,
    email = email,
    notes = notes
)

fun Item.toItemUiState(isEntryValid: Boolean = false): ItemUiState = ItemUiState(
    itemDetails = this.toItemDetails(),
    isEntryValid = isEntryValid
)