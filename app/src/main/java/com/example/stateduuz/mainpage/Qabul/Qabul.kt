package com.example.stateduuz.mainpage.Qabul

import android.content.Context
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.example.stateduuz.model.qabul.StatisticsQabul
import com.example.stateduuz.model.qabul.TypeData
import com.example.stateduuz.ui.theme.QabulColor
import com.example.stateduuz.ui.theme.QabulColor2
import com.example.stateduuz.utils.AnimatedSelection
import com.example.stateduuz.utils.formatNumber
import com.google.gson.Gson
import kotlinx.coroutines.delay
import java.io.InputStreamReader

@Composable
fun QabulScreen() {
    val jsonFileName = "qabul" // JSON fayl nomi
    val context = LocalContext.current

    var selectedyear by remember { mutableStateOf(0) }

    // JSON yuklash uchun State
    var statistics by remember { mutableStateOf<StatisticsQabul?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    // JSON ma'lumotlarini yuklash
    LaunchedEffect(Unit) {
        delay(1000) // 2 soniya kutish
        statistics = loadStatistics(context, jsonFileName)
        isLoading = false
    }

    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color.Transparent)
        .padding(16.dp)
        ) {
        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier
                .size(40.dp)
                .align(Alignment.Center))
        } else {
            statistics?.let { data ->
                Column(modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.onSecondary)
                    .zIndex(1f)
                    .padding(16.dp)
                ) {
                    val myList = listOf("2021-2022", "2022-2023", "2023-2024", "2024-2025")
                    var selectedCategory by remember { mutableStateOf(myList[0]) }
                    AnimatedSelection(list = myList) { selected ->
                        selectedCategory = selected
                        Log.d("TAG", "Tanlangan: $selected")
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Divider(modifier = Modifier.background(Color.Black))
                    Spacer(modifier = Modifier.height(10.dp))
                    key(selectedCategory) {
                        selectedyear=myList.indexOf(selectedCategory)
                        Log.e("TAG", "QabulScreen Selected: $selectedyear and $selectedCategory", )
                        LazyColumn(modifier = Modifier.fillMaxWidth()) {
                            items(data.categories[selectedyear].data) { it ->
                                Spacer(modifier = Modifier.padding(3.dp))
                                QabulMiniItem(
                                    data = it,
                                    foiz = normalizeApplications(
                                        data = data.categories[selectedyear].data,
                                        num = it.applications
                                    )
                                )
                            }
                        }
                        Log.e("TAG", "QabulScreen: $selectedyear", )

                    }
                }
            } ?: run {
                Text(
                    "Ma'lumot yuklanmadi",
                    modifier = Modifier.padding(16.dp),
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

// JSON yuklash funksiyasi (Composabledan tashqarida bo‘lishi kerak)
fun loadStatistics(context: Context, fileName: String): StatisticsQabul? {
    return try {
        val resId = context.resources.getIdentifier(fileName, "raw", context.packageName)
        if (resId == 0) {
            Log.e("Statistics", "File not found: $fileName")
            return null
        }
        val inputStream = context.resources.openRawResource(resId)
        val reader = InputStreamReader(inputStream)
        Gson().fromJson(reader, StatisticsQabul::class.java)
    } catch (e: Exception) {
        Log.e("Statistics", "Error reading JSON file: $fileName", e)
        null
    }
}


@Composable
fun QabulMiniItem(data:TypeData, foiz:Float){
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(6.dp)
                .clip(CircleShape)
                .background(QabulColor)
        )
        Spacer(modifier = Modifier.width(5.dp))
        Text(text = data.type, color = MaterialTheme.colorScheme.secondary, fontSize = 12.sp)
    }
    Spacer(modifier = Modifier.height(5.dp))
    Row (modifier = Modifier
        .fillMaxWidth()
        .height(20.dp)) {
        Row(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(0.7f)
                .clip(RoundedCornerShape(12.dp))
                .background(QabulColor2),
        )
        {
            val colorWhidth = if (foiz > 0.05f) foiz else 0.05f

            Box(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .fillMaxHeight()
            ) {
                // 70% qismi bo‘yalgan Box
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(colorWhidth) // 70% qismi ko‘k bo‘ladi
                        .clip(RoundedCornerShape(12.dp))
                        .background(QabulColor)
                        .padding(10.dp),
                    contentAlignment = Alignment.CenterStart
                )
                {}
            }
        }
        Spacer(modifier = Modifier.weight(1f))
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .clip(RoundedCornerShape(6.dp)) // Birinchi clip
                .background(QabulColor2) // Keyin background
                .border(
                    1.dp,
                    QabulColor,
                    RoundedCornerShape(6.dp)
                ) // Border ham clipdan keyin bo‘lishi kerak
                .padding(horizontal = 5.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(text = formatNumber(data.applications), color = QabulColor, fontSize = 12.sp)
        }

    }
}

@Composable
fun QabulMiniItem1(data:TypeData, foiz:Float){
    val colorWhidth = if (foiz > 0.05f) foiz else 0.05f

    Box(modifier = Modifier
        .fillMaxWidth()
        .height(40.dp)
        .clip(RoundedCornerShape(12.dp))
        .background(getBackgroundCategoryColor(data.type))){
        Text(text = data.type,
            color = Color.DarkGray,
            fontSize = 14.sp,
            modifier = Modifier
                .padding(start = 20.dp)
                .zIndex(1f)
                .align(Alignment.CenterStart),
            fontWeight = FontWeight.Bold
        )
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(colorWhidth) // 70% qismi ko‘k bo‘ladi
                .clip(RoundedCornerShape(12.dp))
                .background(getCategoryColor(data.type))
                .padding(10.dp),
        )
        Text(text = formatNumber(data.applications),
            color = getappCategoryColor(data.type),
            fontSize = 16.sp,
            modifier = Modifier
                .padding(end = 20.dp)
                .zIndex(1f)
                .align(Alignment.CenterEnd),
            fontWeight = FontWeight.Bold
        )
    }
}

fun normalizeApplications(data:List<TypeData>, num:Int): Float {
    val maxApplications = data.maxOfOrNull { it.applications } ?: return 0f
    val scaleFactor = 0.9f / maxApplications
    return scaleFactor * num
}


fun getCategoryColor(categoryName: String): Color {
    return when (categoryName) {
        "Bakalavr" -> Color(0xFF71BEEF) // Ko'k
        "Magistratura" -> Color(0xFFF3F8D1) // Sariq
        "Akademik litsey" -> Color(0xFFFFEEDD) // Apelsin
        "Kasb-hunar maktabi" -> Color(0xFFFDEBF0) // Yashil
        "Kollej" -> Color(0xFFF1F3FD) // Yashil
        "Texnikum" -> Color(0xFFFDEBF0) // Yashil
        else -> Color.Gray
    }
}
fun getappCategoryColor(categoryName: String): Color {
    return when (categoryName) {
        "Bakalavr" -> Color(0xFF003658) // Ko'k
        "Magistratura" -> Color(0xFFA6B63D) // Sariq
        "Akademik litsey" -> Color(0xFFAA7743) // Apelsin
        "Kasb-hunar maktabi" -> Color(0xFFBB929D) // Yashil
        "Kollej" -> Color(0xFF31418F) // Yashil
        "Texnikum" -> Color(0xFF744F59) // Yashil
        else -> Color.Gray
    }
}
fun getBackgroundCategoryColor(categoryName: String): Color {
    return when (categoryName) {
        "Bakalavr" -> Color(0x6671BEEF) // Ko'k
        "Magistratura" -> Color(0x66F3F8D1) // Sariq
        "Akademik litsey" -> Color(0x66FFEEDD) // Apelsin
        "Kasb-hunar maktabi" -> Color(0x66FDEBF0) // Yashil
        "Kollej" -> Color(0x66F1F3FD) // Yashil
        "Texnikum" -> Color(0x66FDEBF0) // Yashil
        else -> Color.Gray
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun showQabul(){
    QabulScreen()
}