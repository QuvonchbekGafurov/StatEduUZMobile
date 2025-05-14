package com.example.stateduuz.mainpage.ProfessionalTalim

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import com.example.stateduuz.R
import com.example.stateduuz.SearchView
import com.example.stateduuz.ui.theme.ColorGreenMain
import com.example.stateduuz.ui.theme.ColorWhite

@Composable
fun ProfessionalTalimScreen(navController: NavController) {
    val tabs = listOf("O'quvchilar", "Muassasalar")
    val textState = remember {
        mutableStateOf(TextFieldValue(""))
    }
    Column(modifier = Modifier.fillMaxSize()) {
        // LazyRow orqali horizontal tablar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(bottomStart = 25.dp, bottomEnd = 25.dp))
                .background(
                    MaterialTheme.colorScheme.primary
                )
                .height(90.dp)
                .clip(RoundedCornerShape(25.dp))

        )
        {
            Box(modifier = Modifier
                .padding(horizontal = 15.dp)
                .fillMaxWidth()
                .height(40.dp)
                .align(Alignment.TopCenter)
                .zIndex(1f)
            ){

                Row {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(if (KasbiyTalimManager.selectedTabIndex == 0) 0.dp else 40.dp)
                            .weight(0.8f),
                    ) {

                        SearchView(
                            state = textState,
                            placeHolder = "Texnikumlarni qidirish",
                            modifier = Modifier
                                .fillMaxSize()
                        )

                    }
                    Spacer(modifier = Modifier.weight(0.02f))
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .border(
                                1.dp,
                                ColorGreenMain,
                                RoundedCornerShape(14.dp)
                            ) // ✅ Avval border
                            .clip(RoundedCornerShape(14.dp)) // ✅ Keyin clip
                            .background(Color.White)
                            .weight(0.15f)
                            .clickable {
                                navController.navigate("filter_screen")
                            }
                    )
                    {
                        Icon(
                            painter = painterResource(id = R.drawable.filter),
                            contentDescription = "",
                            modifier = Modifier
                                .align(Alignment.Center)
                                .padding(8.dp)
                        )
                    }
                }
                val searchedText = textState.value.text
            }
            Box(modifier = Modifier
                .align(Alignment.BottomCenter)
                .height(70.dp)
                .clip(RoundedCornerShape(25.dp))
                .background(MaterialTheme.colorScheme.outline)
            )
            {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                        .padding(horizontal = 10.dp)
                        .padding(bottom = 10.dp)
                        .clip(RoundedCornerShape(25.dp))
                        .background(ColorWhite)

                ) {
                    tabs.forEach { tab ->
                        val isSelected = tabs[KasbiyTalimManager.selectedTabIndex] == tab
                        Box(
                            modifier = Modifier
                                .weight(1f) // <-- FAOL, har bir element teng joy egallaydi
                                .clip(RoundedCornerShape(25.dp))
                                .background(if (isSelected) ColorGreenMain else Color.White)
                                .clickable(
                                    indication = null,
                                    interactionSource = remember { MutableInteractionSource() }
                                ) {
                                    KasbiyTalimManager.selectedTabIndex = tabs.indexOf(tab)

                                }
                                .padding(vertical = 6.dp, horizontal = 1.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = tab,
                                color = if (isSelected) Color.White else ColorGreenMain,
                                style = MaterialTheme.typography.bodyLarge,
                                fontSize = 12.sp,
                            )
                        }
                    }
                }
            }
        }
        when (KasbiyTalimManager.selectedTabIndex) {
            0 -> OquvchilarScreen()
            1 -> Muassasalar(textSearch = textState.value.text)
        }

    }
}
object KasbiyTalimManager {
    var selectedTabIndex by mutableStateOf(0)
}