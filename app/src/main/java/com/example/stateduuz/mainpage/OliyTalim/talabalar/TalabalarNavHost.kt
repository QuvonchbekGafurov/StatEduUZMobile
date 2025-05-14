package com.example.stateduuz.mainpage.OliyTalim.talabalar

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.stateduuz.mainpage.OliyTalim.talabalar.screens.FuqaroligiScreen
import com.example.stateduuz.mainpage.OliyTalim.talabalar.screens.HududlarKesimidaScreen
import com.example.stateduuz.mainpage.OliyTalim.talabalar.screens.KurslarScreen
import com.example.stateduuz.mainpage.OliyTalim.talabalar.screens.TalimShakliScreen
import com.example.stateduuz.mainpage.OliyTalim.talabalar.screens.TalimTuriScreen
import com.example.stateduuz.mainpage.OliyTalim.talabalar.screens.TolovShakliScreen
import com.example.stateduuz.mainpage.OliyTalim.talabalar.screens.YashashJoyiScreen
import com.example.stateduuz.mainpage.OliyTalim.talabalar.screens.YoshiScreen

@Composable

fun TalabalarNavHost(navController: NavHostController) {
    NavHost(navController, startDestination = "talim_turi_screen") {
        // Ekranlar va marshrutlarni qo'shish
        composable("talim_turi_screen") {
            TalimTuriScreen() // Ta'lim turi ekranini chaqirish
        }
        composable("talim_shakli_screen") {
            TalimShakliScreen() // Ta'lim shakli ekranini chaqirish
        }
        composable("tolov_shakli_screen") {
            TolovShakliScreen() // To'lov shakli ekranini chaqirish
        }
        composable("fuqaroligi_screen") {
            FuqaroligiScreen() // Fuqaroligi ekranini chaqirish
        }
        composable("kurslar_screen") {
            KurslarScreen() // Kurslar ekranini chaqirish
        }
        composable("yoshi_screen") {
            YoshiScreen() // Yoshi ekranini chaqirish
        }
        composable("yashash_joyi_screen") {
            YashashJoyiScreen() // Yashash joyi ekranini chaqirish
        }
        composable("hududlar_kesimida_screen") {
            HududlarKesimidaScreen() // Hududlar kesimida ekranini chaqirish
        }
    }
}
