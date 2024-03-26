@file:OptIn(ExperimentalMaterial3Api::class)

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
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
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
import com.example.phonebookapp.ui.theme.PhoneBookAppTheme

@Composable
fun ReadScreen() {
    Scaffold(
        topBar = {
            ReadTopBar()
        }
    ) {innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding)
        ) {
            ReadBody()
        }
    }
}

@Composable
fun ReadTopBar(){
    TopAppBar(title = {
        Text(text = "Back")
    })

}

@Composable
fun ReadBody() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start=16.dp, end=16.dp, top=8.dp, bottom=8.dp)
    ) {
        ReadPhoto()
        ReadName(
            text = "Name"
        )
        ReadCategory(
            text = "Family",
            icon = Icons.Default.Person,
            color = Color.Red
        )
        ReadButtons()
        ReadList()
    }

}

@Composable
fun ReadCategory(text: String, icon: ImageVector, color: Color) {
    Row(
        modifier = Modifier
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            imageVector = icon,
            contentDescription = "Category",
            modifier = Modifier.size(24.dp),
            colorFilter= ColorFilter.tint(color)
        )
        Spacer(modifier = Modifier.padding(4.dp))
        Text(text = text,color = color)
    }
}

@Composable
fun ReadList() {
    LazyColumn(content = {
        items(3) {
            Text(text = "Item $it")
        }
    })
}

@Composable
fun ReadPhoto() {
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
fun ReadButtons() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(64.dp, 8.dp, 64.dp, 8.dp),
    ) {

        ReadButton(
            icon = Icons.Default.Phone,
            name = "Call",
            onClick = { /*TODO*/ }
        )
        Spacer(modifier = Modifier.weight(1f))
        ReadButton(
            icon = Icons.Default.MailOutline,
            name = "SMS",
            onClick = { /*TODO*/ }
        )

    }
}


@Composable
fun ReadButton(
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
fun ReadName(
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
fun ReadScreenPreview() {
    PhoneBookAppTheme {
        ReadScreen()
    }
}