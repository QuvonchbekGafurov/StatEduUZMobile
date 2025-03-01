package com.example.stateduuz
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.stateduuz.ui.theme.blue

@Composable
fun AppDrawer(
    route: String,
    modifier: Modifier = Modifier,
    navigateToOliyTalim: () -> Unit = {},
    navigateToProfessionalTalim: () -> Unit = {},
    navigateToQabul: () -> Unit = {},
    navigateToDoktorantura: () -> Unit = {},
    closeDrawer: () -> Unit = {}
) {
    ModalDrawerSheet(modifier = Modifier) {
        DrawerHeader(modifier)
        Spacer(modifier = Modifier.padding(vertical = 5.dp))
        NavigationDrawerItem(
            label = {
                Text(
                    text = "Oliy Talim",
                    style = MaterialTheme.typography.labelSmall
                )
            },
            selected = route == AllDestinations.OliyTalim,
            onClick = {
                navigateToOliyTalim()
                closeDrawer()
            },

            icon = { Image(
                modifier = Modifier.size(24.dp),
                painter = painterResource(id = R.drawable.university),
                contentDescription = null
            ) },
            shape = MaterialTheme.shapes.small,
            colors = NavigationDrawerItemDefaults.colors(
                selectedContainerColor = Color.Transparent,  // Orqa fon o‘zgarmasin
                unselectedContainerColor = Color.Transparent,
                selectedTextColor = blue,  // Matn rangi o‘zgarmasin
                unselectedTextColor = Color.Black,
                selectedIconColor = blue,  // Icon rangi o‘zgarmasin
                unselectedIconColor = Color.Black
            )
        )
        Spacer(modifier = Modifier.padding(top = 5.dp))
        NavigationDrawerItem(
            label = { Text(text = "Professionaltalim", style = MaterialTheme.typography.labelSmall) },
            selected = route == AllDestinations.ProfessionalTalim,
            onClick = {
                navigateToProfessionalTalim()
                closeDrawer()
            },

            icon = { Image(
                modifier = Modifier.size(24.dp),
                painter = painterResource(id = R.drawable.school),
                contentDescription = null
            ) },
            shape = MaterialTheme.shapes.small,
            colors = NavigationDrawerItemDefaults.colors(
                selectedContainerColor = Color.Transparent,  // Orqa fon o‘zgarmasin
                unselectedContainerColor = Color.Transparent,
                selectedTextColor = blue,  // Matn rangi o‘zgarmasin
                unselectedTextColor = Color.Black,
                selectedIconColor = blue,  // Icon rangi o‘zgarmasin
                unselectedIconColor = Color.Black
            )
        )
        Spacer(modifier = Modifier.padding(top = 5.dp))
        NavigationDrawerItem(
            label = { Text(text = "Qabul", style = MaterialTheme.typography.labelSmall) },
            selected = route == AllDestinations.Qabul,
            onClick = {
                navigateToQabul()
                closeDrawer()
            },
            icon = { Image(
                modifier = Modifier.size(24.dp),
                painter = painterResource(id = R.drawable.famale_univer),
                contentDescription = null
            ) },
            shape = MaterialTheme.shapes.small,
            colors = NavigationDrawerItemDefaults.colors(
                selectedContainerColor = Color.Transparent,  // Orqa fon o‘zgarmasin
                unselectedContainerColor = Color.Transparent,
                selectedTextColor = blue,  // Matn rangi o‘zgarmasin
                unselectedTextColor = Color.Black,
                selectedIconColor = blue,  // Icon rangi o‘zgarmasin
                unselectedIconColor = Color.Black
            )

        )
        Spacer(modifier = Modifier.padding(top = 5.dp))
        NavigationDrawerItem(
            label = { Text(text = "Doktorantura", style = MaterialTheme.typography.labelSmall) },
            selected = route == AllDestinations.Doctorantura,
            onClick = {
                navigateToDoktorantura()
                closeDrawer()
            },
            icon = { Image(
                modifier = Modifier.size(24.dp),
                painter = painterResource(id = R.drawable.graduation),
                contentDescription = null
            ) },
            shape = MaterialTheme.shapes.small,
            colors = NavigationDrawerItemDefaults.colors(
                selectedContainerColor = Color.Transparent,  // Orqa fon o‘zgarmasin
                unselectedContainerColor = Color.Transparent,
                selectedTextColor = blue,  // Matn rangi o‘zgarmasin
                unselectedTextColor = Color.Black,
                selectedIconColor =blue,  // Icon rangi o‘zgarmasin
                unselectedIconColor = Color.Black
            )
        )
    }
}


@Composable
fun DrawerHeader(modifier: Modifier) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start,
        modifier = modifier
            .background(Color.White)
            .padding(10.dp)
            .fillMaxWidth()
    ) {

        Image(
            painterResource(id = R.drawable.logo_light),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = modifier
                .size(100.dp)
                .clip(CircleShape),

            )
        Spacer(modifier = Modifier.padding(10.dp))
        Text(
            text = "Talim vazirligi",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.padding(10.dp))
        HorizontalDivider()
        Spacer(modifier = Modifier.padding(10.dp))
    }
}

@Preview
@Composable
fun DrawerHeaderPreview() {
    AppDrawer(modifier = Modifier, route = AllDestinations.OliyTalim)
}