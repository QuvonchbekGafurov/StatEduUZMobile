package com.example.stateduuz.mainpage.OliyTalim

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Scaffold
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import com.example.stateduuz.FilterScreenManager
import com.example.stateduuz.R
import com.example.stateduuz.SearchView
import com.example.stateduuz.mainpage.OliyTalim.Teachers.OqtuvchilarScreen
import com.example.stateduuz.mainpage.OliyTalim.talabalar.TalabalarScreen
import com.example.stateduuz.mainpage.OliyTalim.umumiy.UmumiyScreen
import com.example.stateduuz.mainpage.OliyTalim.umumiy.UmumiyScreen1
import com.example.stateduuz.mainpage.ProfessionalTalim.OquvchilarScreen
import com.example.stateduuz.mainpage.Qabul.loadStatistics
import com.example.stateduuz.ui.theme.ColorBgSecondary
import com.example.stateduuz.ui.theme.ColorGreen
import com.example.stateduuz.ui.theme.ColorGreenMain
import com.example.stateduuz.ui.theme.ColorNyanza

@SuppressLint("SuspiciousIndentation")
@Composable
fun OliyTalimScreen(navControllerfilter: NavController) {
    val backgroundColor = MaterialTheme.colorScheme.primary

    val tabs = listOf("Umumiy", "Talabalar", "O‘qituvchilar", "OTMlar")
    val textState = remember { mutableStateOf(TextFieldValue("")) }
    val selectedTabIndex = rememberSaveable { mutableStateOf(0) }

    val showSearch = selectedTabIndex.value == 3 ||selectedTabIndex.value == 1
    val topHeight = if (showSearch) 90.dp else 80.dp
    val searchBarHeight = if (showSearch) 40.dp else 0.dp
    val bottomBoxHeight = if (showSearch || selectedTabIndex.value == 1) 60.dp else 50.dp

        Column(modifier = Modifier.fillMaxSize()) {
            // Ekran almashuvi
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(bottomStart = 25.dp, bottomEnd = 25.dp))
                    .background(backgroundColor)
                    .height(topHeight)
            ) {
                if (showSearch) {
                    Row(
                        modifier = Modifier
                            .padding(horizontal = 15.dp)
                            .fillMaxWidth()
                            .height(searchBarHeight)
                            .align(Alignment.TopCenter)
                            .zIndex(1f)
                    ) {
                        Box(
                            modifier = Modifier
                                .weight(0.8f)
                                .fillMaxHeight()
                        ) {
                            SearchView(
                                state = textState,
                                placeHolder = "Oliy ta’lim massasasini qidirish",
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                        Spacer(modifier = Modifier.weight(0.02f))
                        Box(
                            modifier = Modifier
                                .weight(0.15f)
                                .fillMaxHeight()
                                .border(1.dp, ColorGreenMain, RoundedCornerShape(14.dp))
                                .clip(RoundedCornerShape(14.dp))
                                .background(Color.White)
                                .clickable {
                                    navControllerfilter.navigate("filter_screen")
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.filter),
                                contentDescription = null,
                                modifier = Modifier.padding(8.dp)
                            )
                        }
                    }
                }
                // Tabs
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .height(bottomBoxHeight)
                        .clip(RoundedCornerShape(25.dp))
                        .background(MaterialTheme.colorScheme.outline)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.BottomCenter)
                            .padding(10.dp)
                    ) {
                        tabs.forEachIndexed { index, tab ->
                            val isSelected = selectedTabIndex.value == index
                            Spacer(modifier = Modifier.width(4.dp))
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(11.dp))
                                    .background(if (isSelected) MaterialTheme.colorScheme.primary else Color.White)
                                    .clickable(
                                        indication = null,
                                        interactionSource = remember { MutableInteractionSource() }
                                    ) {
                                        selectedTabIndex.value = index
                                        Log.e("TAG", "OliyTalimManager: $index")
                                    }
                                    .padding(vertical = 3.dp, horizontal = 1.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = tab,
                                    color = if (isSelected) Color.White else ColorGreenMain,
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontSize = 12.sp
                                )
                            }
                            Spacer(modifier = Modifier.width(4.dp))
                        }
                    }
                }
            }
            Crossfade(targetState = selectedTabIndex.value) { index ->
                when (index) {
                    0 -> UmumiyScreen()
                    1 -> TalabalarScreen()
                    2 -> OqtuvchilarScreen()
                    3 -> OtmlarRoyxatiScreen(textSearch = textState.value.text)
                }
            }
        }
}


