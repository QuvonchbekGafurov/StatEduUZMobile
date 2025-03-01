package com.example.stateduuz.mainpage

import android.content.Context
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
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
import com.example.stateduuz.data.ApplicationData
import com.example.stateduuz.data.Category
import com.example.stateduuz.data.StatisticsQabul
import com.google.gson.Gson
import java.io.InputStreamReader
import java.text.NumberFormat
import java.util.Locale

@Composable
fun QabulScreen() {
    val jsonFileName = "qabul" // JSON fayl nomi
    val context = LocalContext.current

    // JSON yuklash uchun State
    var statistics by remember { mutableStateOf<StatisticsQabul?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    // JSON ma'lumotlarini yuklash
    LaunchedEffect(Unit) {
        statistics = loadStatistics(context, jsonFileName)
        isLoading = false
    }

    Column(modifier = Modifier
        .fillMaxSize()
        .background(Color.Transparent)
        .padding(16.dp)) {
        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.padding(16.dp))
        } else {
            statistics?.let { data ->
                Column {
                    Text(text = data.title, fontSize = 25.sp, fontWeight = FontWeight.Bold)
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(data.categories) { category ->
                            QabulItem(category = category)
                        }
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

// QabulItem composable (hozircha bo‘sh)
@Composable
fun QabulItem(category: Category) {
    Column (modifier = Modifier
        .padding(top = 10.dp)
        .fillMaxWidth()
        .height(310.dp)
        .clip(RoundedCornerShape(16.dp))
        .background(Color.White)
        .padding(16.dp)

    ){

        Text(text = category.name, modifier = Modifier, fontWeight = FontWeight.Bold, fontSize = 20.sp)
        Row(modifier = Modifier.padding(top = 10.dp)) {
            Text(text = "Yillar kesimida", color = Color.LightGray)
            Spacer(modifier = Modifier.weight(1f))
            Text(text = "Arizalar Soni",color = Color.LightGray )
        }

        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            items(category.data) { it->
                Spacer(modifier = Modifier.padding(6.dp))
                QabulMiniItem(data = it, color = getCategoryColor(category.name), foiz = normalizeApplications(data = category.data, num = it.applications))
            }
        }
    }
}



@Composable
fun QabulMiniItem(data: ApplicationData,color: Color,foiz:Float){
    Row (modifier = Modifier
        .height(45.dp)
        .fillMaxWidth()
        .clip(RoundedCornerShape(8.dp)),
        verticalAlignment = Alignment.CenterVertically
    )
    {
        val colorWhidth=if(foiz>0.45f) foiz else 0.45f

        Box(
            modifier = Modifier
                .weight(1f)
                .height(45.dp)
        ) {
            // 70% qismi bo‘yalgan Box
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(colorWhidth) // 70% qismi ko‘k bo‘ladi
                    .background(color)
                    .padding(10.dp)
                ,
                contentAlignment = Alignment.CenterStart
            )
            {
                Text(text = data.year)
            }
        }
        // O‘ng tomondagi Box (o‘zi uchun joy egallaydi)
        Text(text = NumberFormat
            .getNumberInstance(Locale.US)
            .format(data.applications)
            .replace(",", " "),
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(end = 10.dp).width(80.dp)
        )
    }
}
fun normalizeApplications(data:List <ApplicationData>,num:Int): Float {
    val maxApplications = data.maxOfOrNull { it.applications } ?: return 0f // Eng katta qiymatni topish
    val scaleFactor = 0.9f / maxApplications // 0.9f ga normalizatsiya koeffitsiyenti
    return scaleFactor*num
}

fun getCategoryColor(categoryName: String): Color {
    return when (categoryName) {
        "Bakalavr" -> Color(0xFF4DA2F1) // Ko'k
        "Magistratura" -> Color(0xFFFDD835) // Sariq
        "Akademik litsey" -> Color(0xFFFFAB91) // Apelsin
        "Kasb-hunar maktabi" -> Color(0xFF80CBC4) // Yashil
        "Kollej" -> Color(0xFFA5D6A7) // Yashil
        "Texnikum" -> Color(0xFFFFF59D) // Och sariq
        else -> Color.Gray
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun showQabul(){
    QabulScreen()
}