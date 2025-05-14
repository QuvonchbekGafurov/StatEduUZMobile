package com.example.stateduuz

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.stateduuz.mainpage.OliyTalim.OtmlarManager
import com.example.stateduuz.mainpage.OliyTalim.talabalar.TalabalarManager
import com.example.stateduuz.mainpage.ProfessionalTalim.MuassasalarManager
import com.example.stateduuz.mainpage.ProfessionalTalim.OquvchilarManager
import com.example.stateduuz.ui.theme.ColorGreen
import com.example.stateduuz.ui.theme.ColorGreenMain
import javax.annotation.meta.When

@Composable
fun FilterScreen(
    navController: NavController,
) {
    val filters =
        if (FilterScreenManager.selectedTabScreen=="Otmlar" || FilterScreenManager.selectedTabScreen=="Muassasalar") {
            listOf("Davlat","Nodavlat","Xorijiy","Umumiy")

        } else {
            listOf(
                "Ta’lim turi", "Ta’lim shakli", "To‘lov shakli",
                "Kurslar", "Fuqaroligi", "Yoshi", "Yashash hududi", "Hududlar kesimida"
            )
        }

    var selectedFilter by remember {
        mutableStateOf<String?>(
            when (FilterScreenManager.selectedTabScreen) {
                "Oliy Talim" -> {
                    TalabalarManager.selectedTabScreen
                }

                "Prof Screen" -> {
                    OquvchilarManager.selectedTabScreen
                }

                "Otmlar" -> {
                    OtmlarManager.selected1
                }

                else -> {
                    TalabalarManager.selectedTabScreen
                }
            }
        )
    }
    var expanded by remember { mutableStateOf(true) }

    Column {
        Box(modifier = Modifier
            .statusBarsPadding() // <<< MUHIM QO'SHILADI
            .fillMaxWidth()
            .background(ColorGreenMain)
            .padding(vertical = 10.dp)
            .padding(start = 16.dp)
            ){
            Text(text = "Filter Screen", fontSize = 20.sp, color = Color.White)
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {

            Column(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.White)
                    .padding(16.dp)
            ) {
                Row {
                    Text(text = "$selectedFilter", color = Color.Black, fontSize = 20.sp)
                    Spacer(modifier = Modifier.weight(1f))
                    Icon(
                        modifier = Modifier.clickable {
                            expanded = !expanded
                        },
                        imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = "Toggle",
                        tint = Color.Black
                    )
                }
                if (expanded) {
                    LazyColumn()
                    {
                        items(filters) { filter ->
                            FilterItem(
                                filters = filters,
                                title = filter,
                                isSelected = filter == selectedFilter,
                                onSelect = { selectedFilter = filter }
                            )
                        }
                    }
                }
            }

            Button(
                onClick = {
                    navController.popBackStack()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 15.dp)
                    .align(Alignment.BottomCenter),
                colors = ButtonDefaults.buttonColors(containerColor = ColorGreenMain)
            ) {
                Text(text = "Saqlash", color = Color.White, fontSize = 18.sp, modifier = Modifier.padding(vertical = 5.dp))
            }
        }
    }
    when (FilterScreenManager.selectedTabScreen) {
        "Oliy Talim" -> {
            TalabalarManager.selectedTabScreen = selectedFilter.toString()
        }

        "Prof Screen" -> {
            OquvchilarManager.selectedTabScreen = selectedFilter.toString()
        }
        "Otmlar"->{
            OtmlarManager.selected1=selectedFilter.toString()
        }
        "Muassasalar"->{
            MuassasalarManager.selected1=selectedFilter.toString()
        }
    }
}

object FilterScreenManager {
    var selectedTabScreen by mutableStateOf("Oliy Talim")
}

@Composable
fun FilterItem(
    filters: List<String>,
    title: String,
    isSelected: Boolean,
    onSelect: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                indication = null, // Soya yo'qoladi
                interactionSource = remember { MutableInteractionSource() } // Vizual effektni o'chirish
            ) {
                onSelect()
            }
            .padding(vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            fontSize = 16.sp,
            color = Color.Black,
            modifier = Modifier.weight(1f)
        )

        if (isSelected) {
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .clip(CircleShape)
                    .background(ColorGreen)
            )
        }
        Spacer(modifier = Modifier.width(5.dp))
    }

    if (filters.indexOf(title) != filters.size - 1) Divider(
        color = Color.LightGray,
        thickness = 1.dp
    )
}
