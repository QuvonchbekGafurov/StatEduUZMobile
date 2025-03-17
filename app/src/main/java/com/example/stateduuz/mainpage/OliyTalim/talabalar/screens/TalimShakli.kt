package com.example.stateduuz.mainpage.OliyTalim.talabalar.sort

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.stateduuz.AnimatedCounter
import com.example.stateduuz.chart.stackedbar.StackedBarGraph
import com.example.stateduuz.mainpage.OliyTalim.talabalar.viewmodel.EduFormViewModel
import com.example.stateduuz.mainpage.OliyTalim.talabalar.viewmodel.EduTypeViewModel
import com.example.stateduuz.model.educationFormAndAccommodation.educationFormAndAccommodationItem
import com.example.stateduuz.model.educationFormAndCitizenship.educationFormAndCitizenship
import com.example.stateduuz.ui.theme.ColorDeepPurple
import com.example.stateduuz.ui.theme.ColorGreen
import com.example.stateduuz.ui.theme.ColorLightBlue
import com.example.stateduuz.ui.theme.ColorLightOrange
import com.example.stateduuz.ui.theme.ColorLightYellow

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TalimShakliScreen(viewModel: EduFormViewModel = hiltViewModel()) {
    val educationFormAndGender by viewModel.educationFormAndGender.observeAsState()
    val educationFormAndAge by viewModel.educationFormAndAge.observeAsState()
    val educationFormAndPaymentForm by viewModel.educationFormAndPaymentForm.observeAsState()
    val educationFormAndCourse by viewModel.educationFormAndCourse.observeAsState()
    val educationFormAndAccommodation by viewModel.educationFormAndAccommodation.observeAsState()
    val educationFormAndCitizenship by viewModel.educatiobFormAndCitizenship.observeAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchEducationFormAndGender()
        viewModel.fetchEducationFormAndAge()
        viewModel.fetchEducationFormAndPaymentForm()
        viewModel.fetchEducationFormAndCourse()
        viewModel.fetchEducationFormAndAccommodation()
        viewModel.fetchEducationFormAndCitizenship()
    }
    Column(
        modifier = Modifier
            .padding(20.dp)
            .fillMaxSize()
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            item {
                Column {
                    Spacer(modifier = Modifier.height(20.dp))
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(Color.White)
                            .padding(10.dp)
                    )
                    {
                        Text(text = "Jins bo'yicha", fontWeight = FontWeight.Bold, fontSize = 20.sp)

                        Spacer(modifier = Modifier.height(5.dp))
                        var xAxisScaleData by remember { mutableStateOf<List<String>>(emptyList()) }
                        var allDataLists by remember { mutableStateOf<List<List<Int>>>(emptyList()) }
                        var yAxisScaleData by remember { mutableStateOf<List<String>>(emptyList()) }
                        var topValue by remember { mutableStateOf<List<Int>>(emptyList()) }

                        LaunchedEffect(educationFormAndGender) {
                            educationFormAndGender?.let { data ->
                                xAxisScaleData = data.map { it.eduForm }.distinct()
                                yAxisScaleData = data.map { it.name }.distinct() // ["1-kurs", "2-kurs", ..., "6-kurs"]
                                val groupedData = data.groupBy { it.name }
                                    .mapValues { (_, values) -> values.associateBy { it.eduForm } }
                                allDataLists = yAxisScaleData.map { course ->
                                    xAxisScaleData.map { eduType ->
                                        groupedData[course]?.get(eduType)?.count ?: 0
                                    }
                                }
                                // To'g'ri count olish uchun groupedData ichidan qiymat bo'lmaganlar uchun nol qo'ymaymiz
                                topValue = yAxisScaleData.map { course ->
                                    val courseData = groupedData[course]
                                    if (courseData != null) {
                                        courseData.values.sumOf { it.count } // Kurs bo'yicha barcha countlarni yig'ish
                                    } else {
                                        0
                                    }
                                }                            }
                        }
                        LazyRow(modifier = Modifier.fillMaxWidth(),
                        ) {
                            items(yAxisScaleData.size) {index->
                                Column(horizontalAlignment = Alignment.Start) {
                                    Text(
                                        text = "${yAxisScaleData[index]}",
                                        color = Color.LightGray,
                                        modifier = Modifier.padding(start = 5.dp)
                                    )
                                    Spacer(modifier = Modifier.height(5.dp))
                                    if (topValue.isNotEmpty()) {
                                        AnimatedCounter(
                                            targetNumber = topValue[index] ?: 0,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 25,
                                            modifier = Modifier
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.width(20.dp))


                            }
                        }
                        val colors = listOf(
                            ColorGreen, ColorLightBlue, ColorLightOrange, ColorDeepPurple, ColorLightYellow,
                            Color.Red, Color.Red) // Ranglarni o'zgartirish mumkin
                        Spacer(modifier = Modifier.height(10.dp))
                        Log.e("TAG", "TalimTuriScreen data: $xAxisScaleData and $allDataLists")
                        if (xAxisScaleData.isNotEmpty() && allDataLists.isNotEmpty()) {

                            Log.e("TAG", "TalimTuriScreen: kurslar $allDataLists  : $xAxisScaleData  :  ", )
                            StackedBarGraph(
                                xAxisScaleData = xAxisScaleData,
                                allDataLists = allDataLists,
                                colors = colors,
                                height = 400.dp,
                                barWidth = 50.dp,
                                type = false,
                                yAxisScaleData = yAxisScaleData
                            )
                        }
                        FlowRow(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            LegendItem(color = colors[1], label = "To'lov kontrakti")
                            Spacer(modifier = Modifier.width(20.dp))
                            LegendItem(color = colors[0], label = "Davlat granti")
                            Spacer(modifier = Modifier.width(20.dp))
                        }
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(Color.White)
                            .padding(10.dp)
                    )
                    {
                        Text(text = "Yoshi", fontWeight = FontWeight.Bold, fontSize = 20.sp)

                        Spacer(modifier = Modifier.height(5.dp))
                        var xAxisScaleData by remember { mutableStateOf<List<String>>(emptyList()) }
                        var allDataLists by remember { mutableStateOf<List<List<Int>>>(emptyList()) }
                        var yAxisScaleData by remember { mutableStateOf<List<String>>(emptyList()) }
                        var topValue by remember { mutableStateOf<List<Int>>(emptyList()) }

                        LaunchedEffect(educationFormAndAge) {
                            educationFormAndAge?.let { data ->
                                xAxisScaleData = data.map { it.eduForm }.distinct()
                                yAxisScaleData = data.map { it.name }.distinct() // ["1-kurs", "2-kurs", ..., "6-kurs"]
                                val groupedData = data.groupBy { it.name }
                                    .mapValues { (_, values) -> values.associateBy { it.eduForm } }
                                allDataLists = yAxisScaleData.map { course ->
                                    xAxisScaleData.map { eduType ->
                                        groupedData[course]?.get(eduType)?.count ?: 0
                                    }
                                }
                                // To'g'ri count olish uchun groupedData ichidan qiymat bo'lmaganlar uchun nol qo'ymaymiz
                                topValue = yAxisScaleData.map { course ->
                                    val courseData = groupedData[course]
                                    if (courseData != null) {
                                        courseData.values.sumOf { it.count } // Kurs bo'yicha barcha countlarni yig'ish
                                    } else {
                                        0
                                    }
                                }                            }
                        }
                        LazyRow(modifier = Modifier.fillMaxWidth(),
                        ) {
                            items(yAxisScaleData.size) {index->
                                Column(horizontalAlignment = Alignment.Start) {
                                    val list= listOf("30 yoshdan katta","30 yoshdan kichik","30 yoshdan katta","30 yoshdan kichik","30 yoshdan katta","30 yoshdan kichik")
                                    Text(
                                        text = "${list[index]}",
                                        color = Color.LightGray,
                                        modifier = Modifier.padding(start = 5.dp)
                                    )
                                    Spacer(modifier = Modifier.height(5.dp))
                                    if (topValue.isNotEmpty()) {
                                        AnimatedCounter(
                                            targetNumber = topValue[index] ?: 0,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 25,
                                            modifier = Modifier
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.width(20.dp))


                            }
                        }
                        val colors = listOf(
                            ColorGreen, ColorLightBlue, ColorLightOrange, ColorDeepPurple, ColorLightYellow,
                            Color.Red, Color.Red) // Ranglarni o'zgartirish mumkin
                        Spacer(modifier = Modifier.height(10.dp))
                        Log.e("TAG", "TalimTuriScreen data: $xAxisScaleData and $allDataLists")
                        if (xAxisScaleData.isNotEmpty() && allDataLists.isNotEmpty()) {

                            Log.e("TAG", "TalimTuriScreen: kurslar $allDataLists  : $xAxisScaleData  :  ", )
                            StackedBarGraph(
                                xAxisScaleData = xAxisScaleData,
                                allDataLists = allDataLists,
                                colors = colors,
                                height = 400.dp,
                                barWidth = 50.dp,
                                type = false,
                                yAxisScaleData = yAxisScaleData
                            )
                        }
                        FlowRow(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            LegendItem(color = colors[1], label = "To'lov kontrakti")
                            Spacer(modifier = Modifier.width(20.dp))
                            LegendItem(color = colors[0], label = "Davlat granti")
                            Spacer(modifier = Modifier.width(20.dp))
                        }
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(Color.White)
                            .padding(10.dp)
                    )
                    {
                        Text(text = "Fuqaroligi", fontWeight = FontWeight.Bold, fontSize = 20.sp)

                        Spacer(modifier = Modifier.height(5.dp))
                        var xAxisScaleData by remember { mutableStateOf<List<String>>(emptyList()) }
                        var allDataLists by remember { mutableStateOf<List<List<Int>>>(emptyList()) }
                        var yAxisScaleData by remember { mutableStateOf<List<String>>(emptyList()) }
                        var topValue by remember { mutableStateOf<List<Int>>(emptyList()) }

                        LaunchedEffect(educationFormAndCitizenship) {
                            educationFormAndCitizenship?.let { data ->
                                xAxisScaleData = data.map { it.eduForm }.distinct()
                                yAxisScaleData = data.map { it.name }.distinct() // ["1-kurs", "2-kurs", ..., "6-kurs"]
                                val groupedData = data.groupBy { it.name }
                                    .mapValues { (_, values) -> values.associateBy { it.eduForm } }
                                allDataLists = yAxisScaleData.map { course ->
                                    xAxisScaleData.map { eduType ->
                                        groupedData[course]?.get(eduType)?.count ?: 0
                                    }
                                }
                                // To'g'ri count olish uchun groupedData ichidan qiymat bo'lmaganlar uchun nol qo'ymaymiz
                                topValue = yAxisScaleData.map { course ->
                                    val courseData = groupedData[course]
                                    if (courseData != null) {
                                        courseData.values.sumOf { it.count } // Kurs bo'yicha barcha countlarni yig'ish
                                    } else {
                                        0
                                    }
                                }                            }
                        }
                        LazyRow(modifier = Modifier.fillMaxWidth(),
                        ) {
                            items(yAxisScaleData.size) {index->
                                Column(horizontalAlignment = Alignment.Start) {
                                    Text(
                                        text = "${yAxisScaleData[index]}",
                                        color = Color.LightGray,
                                        modifier = Modifier.padding(start = 5.dp)
                                    )
                                    Spacer(modifier = Modifier.height(5.dp))
                                    if (topValue.isNotEmpty()) {
                                        AnimatedCounter(
                                            targetNumber = topValue[index] ?: 0,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 25,
                                            modifier = Modifier
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.width(20.dp))


                            }
                        }
                        val colors = listOf(
                            ColorGreen, ColorLightBlue, ColorLightOrange, ColorDeepPurple, ColorLightYellow,
                            Color.Red, Color.Red) // Ranglarni o'zgartirish mumkin
                        Spacer(modifier = Modifier.height(10.dp))
                        Log.e("TAG", "TalimTuriScreen data: $xAxisScaleData and $allDataLists")
                        if (xAxisScaleData.isNotEmpty() && allDataLists.isNotEmpty()) {

                            Log.e("TAG", "TalimTuriScreen: kurslar $allDataLists  : $xAxisScaleData  :  ", )
                            StackedBarGraph(
                                xAxisScaleData = xAxisScaleData,
                                allDataLists = allDataLists,
                                colors = colors,
                                height = 400.dp,
                                barWidth = 50.dp,
                                type = true,
                                yAxisScaleData = yAxisScaleData
                            )
                        }
                        FlowRow(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            LegendItem(color = colors[1], label = "To'lov kontrakti")
                            Spacer(modifier = Modifier.width(20.dp))
                            LegendItem(color = colors[0], label = "Davlat granti")
                            Spacer(modifier = Modifier.width(20.dp))
                        }
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(Color.White)
                            .padding(10.dp)
                    )
                    {
                        Text(text = "To'lov Shakli", fontWeight = FontWeight.Bold, fontSize = 20.sp)

                        Spacer(modifier = Modifier.height(5.dp))
                        var xAxisScaleData by remember { mutableStateOf<List<String>>(emptyList()) }
                        var allDataLists by remember { mutableStateOf<List<List<Int>>>(emptyList()) }
                        var yAxisScaleData by remember { mutableStateOf<List<String>>(emptyList()) }
                        var topValue by remember { mutableStateOf<List<Int>>(emptyList()) }

                        LaunchedEffect(educationFormAndPaymentForm) {
                            educationFormAndPaymentForm?.let { data ->
                                xAxisScaleData = data.map { it.eduForm }.distinct()
                                yAxisScaleData = data.map { it.name }.distinct() // ["1-kurs", "2-kurs", ..., "6-kurs"]
                                val groupedData = data.groupBy { it.name }
                                    .mapValues { (_, values) -> values.associateBy { it.eduForm } }
                                allDataLists = yAxisScaleData.map { course ->
                                    xAxisScaleData.map { eduType ->
                                        groupedData[course]?.get(eduType)?.count ?: 0
                                    }
                                }
                                // To'g'ri count olish uchun groupedData ichidan qiymat bo'lmaganlar uchun nol qo'ymaymiz
                                topValue = yAxisScaleData.map { course ->
                                    val courseData = groupedData[course]
                                    if (courseData != null) {
                                        courseData.values.sumOf { it.count } // Kurs bo'yicha barcha countlarni yig'ish
                                    } else {
                                        0
                                    }
                                }                            }
                        }
                        LazyRow(modifier = Modifier.fillMaxWidth(),
                        ) {
                            items(yAxisScaleData.size) {index->
                                Column(horizontalAlignment = Alignment.Start) {
                                    Text(
                                        text = "${yAxisScaleData[index]}",
                                        color = Color.LightGray,
                                        modifier = Modifier.padding(start = 5.dp)
                                    )
                                    Spacer(modifier = Modifier.height(5.dp))
                                    if (topValue.isNotEmpty()) {
                                        AnimatedCounter(
                                            targetNumber = topValue[index] ?: 0,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 25,
                                            modifier = Modifier
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.width(20.dp))


                            }
                        }
                        val colors = listOf(
                            ColorGreen, ColorLightBlue, ColorLightOrange, ColorDeepPurple, ColorLightYellow,
                            Color.Red, Color.Red) // Ranglarni o'zgartirish mumkin
                        Spacer(modifier = Modifier.height(10.dp))
                        Log.e("TAG", "TalimTuriScreen data: $xAxisScaleData and $allDataLists")
                        if (xAxisScaleData.isNotEmpty() && allDataLists.isNotEmpty()) {

                            Log.e("TAG", "TalimTuriScreen: kurslar $allDataLists  : $xAxisScaleData  :  ", )
                            StackedBarGraph(
                                xAxisScaleData = xAxisScaleData,
                                allDataLists = allDataLists,
                                colors = colors,
                                height = 400.dp,
                                barWidth = 50.dp,
                                type = true,
                                yAxisScaleData = yAxisScaleData
                            )
                        }
                        FlowRow(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            LegendItem(color = colors[1], label = "To'lov kontrakti")
                            Spacer(modifier = Modifier.width(20.dp))
                            LegendItem(color = colors[0], label = "Davlat granti")
                            Spacer(modifier = Modifier.width(20.dp))
                        }
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(Color.White)
                            .padding(10.dp)
                    )
                    {
                        Text(text = "Kurslar", fontWeight = FontWeight.Bold, fontSize = 20.sp)

                        Spacer(modifier = Modifier.height(5.dp))
                        var xAxisScaleData by remember { mutableStateOf<List<String>>(emptyList()) }
                        var allDataLists by remember { mutableStateOf<List<List<Int>>>(emptyList()) }
                        var yAxisScaleData by remember { mutableStateOf<List<String>>(emptyList()) }
                        var topValue by remember { mutableStateOf<List<Int>>(emptyList()) }

                        LaunchedEffect(educationFormAndCourse) {
                            educationFormAndCourse?.let { data ->
                                xAxisScaleData = data.map { it.eduForm }.distinct()
                                yAxisScaleData = data.map { it.name }.distinct() // ["1-kurs", "2-kurs", ..., "6-kurs"]
                                val groupedData = data.groupBy { it.name }
                                    .mapValues { (_, values) -> values.associateBy { it.eduForm } }
                                allDataLists = yAxisScaleData.map { course ->
                                    xAxisScaleData.map { eduType ->
                                        groupedData[course]?.get(eduType)?.count ?: 0
                                    }
                                }
                                // To'g'ri count olish uchun groupedData ichidan qiymat bo'lmaganlar uchun nol qo'ymaymiz
                                topValue = yAxisScaleData.map { course ->
                                    val courseData = groupedData[course]
                                    if (courseData != null) {
                                        courseData.values.sumOf { it.count } // Kurs bo'yicha barcha countlarni yig'ish
                                    } else {
                                        0
                                    }
                                }                            }
                        }
                        LazyRow(modifier = Modifier.fillMaxWidth(),
                        ) {
                            items(yAxisScaleData.size) {index->
                                Column(horizontalAlignment = Alignment.Start) {
                                    Text(
                                        text = "${yAxisScaleData[index]}",
                                        color = Color.LightGray,
                                        modifier = Modifier.padding(start = 5.dp)
                                    )
                                    Spacer(modifier = Modifier.height(5.dp))
                                    if (topValue.isNotEmpty()) {
                                        AnimatedCounter(
                                            targetNumber = topValue[index] ?: 0,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 25,
                                            modifier = Modifier
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.width(20.dp))


                            }
                        }
                        val colors = listOf(
                            ColorGreen, ColorLightBlue, ColorLightOrange, ColorDeepPurple, ColorLightYellow,
                            Color.Red, Color.Red) // Ranglarni o'zgartirish mumkin
                        Spacer(modifier = Modifier.height(10.dp))
                        Log.e("TAG", "TalimTuriScreen data: $xAxisScaleData and $allDataLists")
                        if (xAxisScaleData.isNotEmpty() && allDataLists.isNotEmpty()) {

                            Log.e("TAG", "TalimTuriScreen: kurslar $allDataLists  : $xAxisScaleData  :  ", )
                            StackedBarGraph(
                                xAxisScaleData = xAxisScaleData,
                                allDataLists = allDataLists,
                                colors = colors,
                                height = 400.dp,
                                barWidth = 50.dp,
                                type = true,
                                yAxisScaleData = yAxisScaleData
                            )
                        }
                        FlowRow(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            LegendItem(color = colors[1], label = "To'lov kontrakti")
                            Spacer(modifier = Modifier.width(20.dp))
                            LegendItem(color = colors[0], label = "Davlat granti")
                            Spacer(modifier = Modifier.width(20.dp))
                        }
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(Color.White)
                            .padding(10.dp)
                    )
                    {
                        Text(text = "Yashash joyi", fontWeight = FontWeight.Bold, fontSize = 20.sp)

                        Spacer(modifier = Modifier.height(5.dp))
                        var xAxisScaleData by remember { mutableStateOf<List<String>>(emptyList()) }
                        var allDataLists by remember { mutableStateOf<List<List<Int>>>(emptyList()) }
                        var yAxisScaleData by remember { mutableStateOf<List<String>>(emptyList()) }
                        var topValue by remember { mutableStateOf<List<Int>>(emptyList()) }

                        LaunchedEffect(educationFormAndAccommodation) {
                            educationFormAndAccommodation?.let { data ->
                                xAxisScaleData = data.map { it.eduForm }.distinct()
                                yAxisScaleData = data.map { it.name }.distinct() // ["1-kurs", "2-kurs", ..., "6-kurs"]
                                val groupedData = data.groupBy { it.name }
                                    .mapValues { (_, values) -> values.associateBy { it.eduForm } }
                                allDataLists = yAxisScaleData.map { course ->
                                    xAxisScaleData.map { eduType ->
                                        groupedData[course]?.get(eduType)?.count ?: 0
                                    }
                                }
                                // To'g'ri count olish uchun groupedData ichidan qiymat bo'lmaganlar uchun nol qo'ymaymiz
                                topValue = yAxisScaleData.map { course ->
                                    val courseData = groupedData[course]
                                    if (courseData != null) {
                                        courseData.values.sumOf { it.count } // Kurs bo'yicha barcha countlarni yig'ish
                                    } else {
                                        0
                                    }
                                }                            }
                        }
                        LazyRow(modifier = Modifier.fillMaxWidth(),
                        ) {
                            items(yAxisScaleData.size) {index->
                                Column(horizontalAlignment = Alignment.Start) {
                                    Text(
                                        text = "${yAxisScaleData[index]}",
                                        color = Color.LightGray,
                                        modifier = Modifier.padding(start = 5.dp)
                                    )
                                    Spacer(modifier = Modifier.height(5.dp))
                                    if (topValue.isNotEmpty()) {
                                        AnimatedCounter(
                                            targetNumber = topValue[index] ?: 0,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 25,
                                            modifier = Modifier
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.width(20.dp))


                            }
                        }
                        val colors = listOf(
                            ColorGreen, ColorLightBlue, ColorLightOrange, ColorDeepPurple, ColorLightYellow,
                            Color.Red, Color.Red) // Ranglarni o'zgartirish mumkin
                        Spacer(modifier = Modifier.height(10.dp))
                        Log.e("TAG", "TalimTuriScreen data: $xAxisScaleData and $allDataLists")
                        if (xAxisScaleData.isNotEmpty() && allDataLists.isNotEmpty()) {

                            Log.e("TAG", "TalimTuriScreen: kurslar $allDataLists  : $xAxisScaleData  :  ", )
                            StackedBarGraph(
                                xAxisScaleData = xAxisScaleData,
                                allDataLists = allDataLists,
                                colors = colors,
                                height = 400.dp,
                                barWidth = 50.dp,
                                type = true,
                                yAxisScaleData = yAxisScaleData
                            )
                        }
                        FlowRow(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            LegendItem(color = colors[1], label = "To'lov kontrakti")
                            Spacer(modifier = Modifier.width(20.dp))
                            LegendItem(color = colors[0], label = "Davlat granti")
                            Spacer(modifier = Modifier.width(20.dp))
                        }
                    }
                }

                }
        }
    }
}
