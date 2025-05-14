package com.example.stateduuz.mainpage.OliyTalim.talabalar

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownNavigator(navController: NavController) {
    var expanded by remember { mutableStateOf(false) }
    val options = listOf("Ta'lim turi", "Ta'lim shakli", "To'lov shakli", "Fuqaroligi", "Kurslar")
    var selectedOption by remember { mutableStateOf(options[0]) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it }
    ) {
        OutlinedTextField(
            value = selectedOption,
            onValueChange = {},
            readOnly = true,
            label = { Text("Bo'lim tanlang") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
            modifier = Modifier
                .menuAnchor()
                .clickable { expanded = true },
                    shape = RoundedCornerShape(25.dp), // Burchaklarni 25.dp qilish
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.LightGray, // Fokustagi border rangi
                unfocusedBorderColor = Color.LightGray // Oddiy border rangi
            )
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        selectedOption = option
                        expanded = false
                        navController.navigate(optionToRoute(option)) // Navigatsiya
                    }
                )
            }
        }
    }
}
// Funksiya: Variantga qarab marshrutga yo'naltirish
fun optionToRoute(option: String): String {
    return when (option) {
        "Ta'lim turi" -> "talim_turi_screen"
        "Ta'lim shakli" -> "talim_shakli_screen"
        "To'lov shakli" -> "tolov_shakli_screen"
        "Fuqaroligi" -> "fuqaroligi_screen"
        "Kurslar" -> "kurslar_screen"
        "Yoshi" -> "yoshi_screen"
        "Yashash joyi" -> "yashash_joyi_screen"
        "Hududlar kesimida" -> "hududlar_kesimida_screen"
        else -> "default_screen"
    }
}
