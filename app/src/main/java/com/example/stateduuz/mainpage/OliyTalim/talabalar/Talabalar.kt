package com.example.stateduuz.mainpage.OliyTalim.talabalar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.stateduuz.FilterScreenManager
import com.example.stateduuz.mainpage.OliyTalim.talabalar.screens.FuqaroligiScreen
import com.example.stateduuz.mainpage.OliyTalim.talabalar.screens.HududlarKesimidaScreen
import com.example.stateduuz.mainpage.OliyTalim.talabalar.screens.KurslarScreen
import com.example.stateduuz.mainpage.OliyTalim.talabalar.screens.TalimShakliScreen
import com.example.stateduuz.mainpage.OliyTalim.talabalar.screens.TalimTuriScreen
import com.example.stateduuz.mainpage.OliyTalim.talabalar.screens.TolovShakliScreen
import com.example.stateduuz.mainpage.OliyTalim.talabalar.screens.YashashJoyiScreen
import com.example.stateduuz.mainpage.OliyTalim.talabalar.screens.YoshiScreen
import javax.annotation.meta.When

@Composable
fun TalabalarScreen() {

    FilterScreenManager.selectedTabScreen = "Oliy Talim"
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top, // Yuqoridan pastga joylashtirish
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f) // Bo‘sh joyni egallash uchun
        ) {
            when(TalabalarManager.selectedTabScreen){
                "Ta’lim turi" -> {
                    TalimTuriScreen()
                }
                "Ta’lim shakli" -> {
                    TalimShakliScreen()
                }
                "To‘lov shakli" -> {
                    TolovShakliScreen()
                }
                "Fuqaroligi" -> {
                    FuqaroligiScreen()
                }
                "Kurslar" -> {
                    KurslarScreen()
                }
                "Yoshi" -> {
                    YoshiScreen()
                }
                "Yashash hududi" -> {
                    YashashJoyiScreen()
                }
                "Hududlar kesimida" -> {
                    HududlarKesimidaScreen()
                }
            }
        }
    }
}
object TalabalarManager {
    var selectedTabScreen by mutableStateOf("Ta’lim turi")
}
