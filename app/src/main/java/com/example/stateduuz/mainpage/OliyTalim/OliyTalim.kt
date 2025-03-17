package com.example.stateduuz.mainpage.OliyTalim

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.stateduuz.mainpage.OliyTalim.talabalar.TalabalarScreen
import com.example.stateduuz.mainpage.OliyTalim.umumiy.UmumiyScreen
import com.example.stateduuz.ui.theme.ColorBgSecondary

@Composable
fun OliyTalimScreen() {
    val tabs = listOf("Umumiy", "Talabalar", "O‘qituvchilar", "OTMlar Ro‘yxati", "Jadvallar")
    var selectedTabIndex by remember { mutableStateOf(0) }

    Column(modifier = Modifier.fillMaxSize()){
        // LazyRow orqali horizontal tablar
        Row (modifier = Modifier.padding(20.dp).clip(RoundedCornerShape(15.dp)).background(
            ColorBgSecondary))
        {
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp),
                horizontalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                items(tabs) { tab ->
                    val isSelected = tabs[selectedTabIndex] == tab
                    Text(
                        text = tab,
                        color = if (isSelected) Color.Black else Color.DarkGray,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier
                            .clickable(
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() }
                            ) {
                                selectedTabIndex = tabs.indexOf(tab) }
                            .clip(RoundedCornerShape(11.dp))
                            .background(
                                if (isSelected) Color.White else ColorBgSecondary
                            )
                            .padding(8.dp)
                    )
                }
            }
        }
        // Tanlangan tab bo'yicha ekran almashtirish
        when (selectedTabIndex) {
            0 -> UmumiyScreen()
            1 -> TalabalarScreen()
            2 -> OqtuvchilarScreen()
            3 -> OtmlarRoyxatiScreen()
            4 -> JadvallarScreen()
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HomeScreenPreview() {
    OliyTalimScreen()
}