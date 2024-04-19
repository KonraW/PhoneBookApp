package com.example.phonebookapp.ui.item

//import androidx.compose.foundation.layout.FlowRowScopeInstance.weight
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.phonebookapp.PhoneBookTopAppBar
import com.example.phonebookapp.data.Category
import com.example.phonebookapp.ui.AppViewModelProvider
import com.example.phonebookapp.ui.home.HomePersonIcon
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
                title = "Contact Details",
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
//        DetailsPhoto(
//            itemDetails = itemDetails
//        )
        HomePersonIcon(
            item = itemDetails.toItem(),
            size = 192.dp
        )
        DetailsName(
            text = itemDetails.name
        )
        if (itemDetails.surname.isNotBlank()) {
            DetailsName(text = itemDetails.surname)
        }
        DetailsCategory(
            text = itemDetails.category,
            icon = Icons.Default.Person,
            color = Category.valueOf(itemDetails.category).color
        )
        DetailsButtons(itemDetails = itemDetails)
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
        // for number, numberType in itemDetails.number.zip(itemDetails.numberTypes) {
        val backgroundColor = MaterialTheme.colorScheme.secondaryContainer
        Card(
            colors = CardDefaults.cardColors(
                containerColor = backgroundColor
            ),
            modifier = Modifier
                .padding(16.dp)
                .clip(MaterialTheme.shapes.medium)
                .fillMaxWidth()
        ) {
            Text(
                text = "Contact Information",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(8.dp)
            )

            for (i in itemDetails.number.indices) {
                DetailsListItem(
                    icon = Icons.Default.Phone,
                    firstString = itemDetails.number[i],
                    secondString = itemDetails.numberTypes[i],
                    backgroundColor = backgroundColor
                )
            }
            if (itemDetails.email.isNotBlank()) {
                DetailsListItem(
                    icon = Icons.Default.MailOutline,
                    firstString = itemDetails.email,
                    secondString = "E-mail",
                    backgroundColor = backgroundColor
                )
            }
            if (itemDetails.notes.isNotBlank()) {
                DetailsListItem(
                    icon = Icons.Default.MailOutline,
                    firstString = itemDetails.notes,
                    secondString = "Notes",
                    backgroundColor = backgroundColor
                )
            }
        }

//        for (items in itemDetails.number) {
//            Row {
//Text(text = itemDetails.numberTypes)
//                Text(text = items)
//            }
//        }
    }
}


@Composable
fun DetailsListItem(
    icon: ImageVector,
    firstString: String,
    secondString: String,
    backgroundColor: Color,
    modifier: Modifier = Modifier
) {

    Row(
        modifier = Modifier
            .padding(start = 8.dp, bottom = 8.dp),
//            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        val dialerLauncher =
            rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                // Obsługa wyniku dzwonienia, na przykład wyświetlenie komunikatu o sukcesie lub porażce
            }
//        Box (
//
//            modifier = Modifier
//                .background(color = backgroundColor)
//                .weight(1f)
//                .padding(end = 8.dp)
//        ){

//        FilledTonalButton(
//            onClick = { detailsCall(firstString, dialerLauncher) },
////                colors = ButtonDefaults.buttonColors(
////                    containerColor = backgroundColor,
//////                contentColor = MaterialTheme.colors.onSurface
////                ),
//
//            shape = MaterialTheme.shapes.small,
//            modifier = Modifier
////                .fillMaxWidth()
//                .weight(1f)
//
//        ) {
        Row(

            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier
//                    .padding(8.dp)
                .clip(MaterialTheme.shapes.small)
//                    .fillMaxWidth()
                .clickable(
                    onClick = { detailsCall(firstString, dialerLauncher) }
                )
                .weight(1f)
        ) {

            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.padding(8.dp)
            )
            Column(modifier = Modifier.padding(start = 8.dp)) {
                Text(
                    text = firstString,
                    style = MaterialTheme.typography.headlineMedium,
                    textAlign = TextAlign.Start
                )
                Text(
                    text = secondString,
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Start
                )
//                }
            }

        }
//        Spacer(modifier = Modifier.weight(1f))
        IconButton(
            onClick = { /*TODO*/ },
            modifier = Modifier.padding(8.dp)
        )
        {
            Icon(
                imageVector = Icons.Default.Send,
                contentDescription = "Send"
            )
        }
    }
}

@Composable
fun DetailsPhoto(
    itemDetails: ItemDetails
) {
    Card(
        modifier = Modifier
            .padding(16.dp)
            .clip(CircleShape)
//            .clip(RoundedCornerShape(96.dp))
//            .size(192.dp),
    ) {
        if (itemDetails.photo.toString().isNotEmpty()) {
            val painter: Painter = rememberAsyncImagePainter(model = itemDetails.photo)
            Image(
                painter = painter,
                contentDescription = "Photo",
                modifier = Modifier
                    .size(192.dp),
                contentScale = androidx.compose.ui.layout.ContentScale.Crop

            )
        }
    }
}

@Composable
fun DetailsButtons(
    itemDetails: ItemDetails
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(64.dp, 8.dp, 64.dp, 8.dp),
    ) {

        DetailsCallButton(itemDetails = itemDetails)
        Spacer(modifier = Modifier.weight(1f))
        DetailsButtonAndName(
            icon = Icons.Default.MailOutline,
            name = "SMS",
            onClick = { /*TODO*/ }
        )

    }
}

@Composable
fun DetailsCallButton(
    itemDetails: ItemDetails
) {
//    val context = LocalContext.current
//    val dialerLauncher =
//        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
//            // Obsługa wyniku dzwonienia, na przykład wyświetlenie komunikatu o sukcesie lub porażce
//        }
    val dialerLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            // Obsługa wyniku dzwonienia, na przykład wyświetlenie komunikatu o sukcesie lub porażce
        }
    DetailsButtonAndName(
        icon = Icons.Default.Phone,
        name = "Call",
        onClick = {
            detailsCall(itemDetails.number[0], dialerLauncher = dialerLauncher)
//            val intent = Intent(Intent.ACTION_DIAL).apply {
//                data = Uri.parse("tel:" + itemDetails.number[0]) // Tutaj wpisz numer telefonu
//            }
//            dialerLauncher.launch(intent)

        }
    )
}

fun detailsCall(
    number: String,
    dialerLauncher: ManagedActivityResultLauncher<Intent, ActivityResult>
) {

    val intent = Intent(Intent.ACTION_DIAL).apply {
        data = Uri.parse("tel:$number") // Tutaj wpisz numer telefonu
    }
    dialerLauncher.launch(intent)
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
        DetailsListItem(
            icon = Icons.Default.Phone,
            firstString = "123456789",
            secondString = "HOME",
            backgroundColor = MaterialTheme.colorScheme.secondaryContainer
        )
    }
}