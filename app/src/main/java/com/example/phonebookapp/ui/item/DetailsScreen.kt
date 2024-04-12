package com.example.phonebookapp.ui.item

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.phonebookapp.PhoneBookTopAppBar
import com.example.phonebookapp.data.Category
import com.example.phonebookapp.ui.AppViewModelProvider
import com.example.phonebookapp.ui.navigation.NavigationDestination
import com.example.phonebookapp.ui.theme.PhoneBookAppTheme

object DetailsDestination : NavigationDestination {
    override val route = "item_details"
    override val titleRes = "Details"
    const val itemIdArg = "itemId"
    val routeWithArgs = "$route/{$itemIdArg}"
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsScreen(
    navigateToEditItem: (Int) -> Unit,
    navigateBack: () -> Unit,
    navigateHome: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: DetailsViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val uiState = viewModel.uiState.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
//            DetailsTopBar()

            PhoneBookTopAppBar(
                title = "edit",
                canNavigateBack = true,
                navigateUp = navigateHome,//navigateBack,
                canClickButton = true,
                onClickButton = {
                    navigateToEditItem(viewModel.uiState.value.itemDetails.id)//uiState.value.itemDetails.id)
                },
                buttonIcon = Icons.Default.Edit
            )
        },
//        contentColor = MaterialTheme.colorScheme.onPrimary,
//        containerColor = MaterialTheme.colorScheme.primaryContainer
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
        ) {
            item {
                DetailsBody(
                    itemDetails = uiState.value.itemDetails
                )
            }
        }
    }
}

//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun DetailsTopBar() {
//    TopAppBar(
//        navigationIcon = {
//            DetailsButton(image = Icons.Default.ArrowBack) {
//            }
//        },
//        title = {
//            Row(
//            ) {
//                Spacer(modifier = Modifier.weight(1f))
//                DetailsButton(
//                    image = Icons.Default.Edit,
//                    onClick = {}
//                )
//            }
//        },
//        colors = TopAppBarDefaults.topAppBarColors(
//            containerColor = MaterialTheme.colorScheme.primaryContainer
//        )
//    )
//}
//
//@Composable
//fun DetailsButton(
//    image: ImageVector,
//    onClick: () -> Unit
//) {
//    FloatingActionButton(
//        onClick = onClick,
//        elevation = FloatingActionButtonDefaults.elevation(0.dp)
//    ) {
//        Image(
//            imageVector = image,
//            contentDescription = null
//        )
//    }
//}


@Composable
fun DetailsBody(
    itemDetails: ItemDetails
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 8.dp)
    ) {
        DetailsPhoto()
        DetailsName(
            text = itemDetails.name
        )
        if(itemDetails.surname.isNotBlank()) {
            DetailsName(text = itemDetails.surname)
        }
        DetailsCategory(
            text = itemDetails.category,
            icon = Icons.Default.Person,
            color = Category.valueOf(itemDetails.category).color
        )
        DetailsButtons()
        DetailsList(itemDetails = itemDetails)
    }

}

@Composable
fun DetailsCategory(text: String, icon: ImageVector, color: Color) {
    Row(
        modifier = Modifier
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            imageVector = icon,
            contentDescription = "Category",
            modifier = Modifier.size(24.dp),
            colorFilter = ColorFilter.tint(color)
        )
        Spacer(modifier = Modifier.padding(4.dp))
        Text(text = text, color = color)
    }
}

@Composable
fun DetailsList(
    itemDetails: ItemDetails
) {

//    val uiState = viewModel.uiState.collectAsState()
    Column {
        for (items in itemDetails.number) {
            Text(text = items)
        }
    }
}

@Composable
fun DetailsPhoto() {
    val painter: Painter =
        painterResource(id = com.example.phonebookapp.R.drawable.ic_launcher_background)
    Card(
        modifier = Modifier
            .padding(16.dp)
            .clip(RoundedCornerShape(96.dp))
    ) {
        Image(
            painter = painter,
            contentDescription = "Photo",
            modifier = Modifier
                .size(192.dp)
        )
    }
}

@Composable
fun DetailsButtons() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(64.dp, 8.dp, 64.dp, 8.dp),
    ) {

        DetailsButtonAndName(
            icon = Icons.Default.Phone,
            name = "Call",
            onClick = { /*TODO*/ }
        )
        Spacer(modifier = Modifier.weight(1f))
        DetailsButtonAndName(
            icon = Icons.Default.MailOutline,
            name = "SMS",
            onClick = { /*TODO*/ }
        )

    }
}


@Composable
fun DetailsButtonAndName(
    icon: ImageVector,
    name: String,
    onClick: () -> Unit
) {
    Button(onClick = onClick, shape = MaterialTheme.shapes.medium) {
        Card(
            shape = MaterialTheme.shapes.extraSmall,
            colors = CardDefaults.cardColors(
                MaterialTheme.colorScheme.primary
            )
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(imageVector = icon, contentDescription = name)
                Text(name)
            }
        }
    }
}

@Composable
fun DetailsName(
    text: String
) {
    Text(
        text = text,
        style = MaterialTheme.typography.displaySmall,
        modifier = Modifier.padding(8.dp)

    )
}

@Preview(showBackground = true)
@Composable
fun DetailsScreenPreview() {
    PhoneBookAppTheme {
//        DetailsScreen()
    }
}