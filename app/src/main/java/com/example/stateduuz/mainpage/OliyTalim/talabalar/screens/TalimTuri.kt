package com.example.stateduuz.mainpage.OliyTalim.talabalar.sort

import android.content.ClipData.Item
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.stateduuz.AnimatedCounter
import com.example.stateduuz.R
import com.example.stateduuz.calculatePercentage
import com.example.stateduuz.chart.BarGraph
import com.example.stateduuz.chart.BarType
import com.example.stateduuz.chart.stackedbar.StackedBarGraph
import com.example.stateduuz.formatNumber
import com.example.stateduuz.mainpage.OliyTalim.talabalar.viewmodel.EduTypeViewModel
import com.example.stateduuz.model.eduTypeAndCourse.eduTypeAndCourse
import com.example.stateduuz.ui.theme.ColorBgSecondary
import com.example.stateduuz.ui.theme.ColorBgTertiary
import com.example.stateduuz.ui.theme.ColorBlue
import com.example.stateduuz.ui.theme.ColorDeepPurple
import com.example.stateduuz.ui.theme.ColorGreen
import com.example.stateduuz.ui.theme.ColorLightBlue
import com.example.stateduuz.ui.theme.ColorLightGreen
import com.example.stateduuz.ui.theme.ColorLightOrange
import com.example.stateduuz.ui.theme.ColorLightYellow
import com.example.stateduuz.ui.theme.ColorPink

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TalimTuriScreen(viewModel: EduTypeViewModel = hiltViewModel()) {
    val eduTypeAndGender by viewModel.eduTypeAndGender.observeAsState()
    val eduTypeAndAge by viewModel.eduTypeAndAge.observeAsState()
    val eduTypeAndPaymentType by viewModel.eduTypeAndPaymentType.observeAsState()
    val eduTypeAndCourse by viewModel.eduTypeAndCourse.observeAsState()
    val eduTypeAndCitizenship by viewModel.eduTypeAndCitizenship.observeAsState()
    val eduTypeAndAccommodation by viewModel.eduTypeAndAccommodation.observeAsState()
    val eduTypeAndEduForm by viewModel.eduTypeAndEduForm.observeAsState()
    LaunchedEffect(Unit) {
        viewModel.fetchEduTypeAndGender()
        viewModel.fetchEduTypeAndAge()
        viewModel.fetchEduTypeAndPaymentType()
        viewModel.fetchEduTypeAndCourse()
        viewModel.fetchEduTypeAndCitizenship()
        viewModel.fetchEduTypeAndAccommodation()
        viewModel.fetchEduTypeAndEduForm()
    }

    val scrollState = rememberScrollState()
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
            val groupedData = eduTypeAndGender?.groupBy { it.eduType } ?: emptyMap()

            groupedData.forEach { (eduType, items) ->
                val erkaklar = items.find { it.name == "Erkak" }?.count ?: 0
                val ayollar = items.find { it.name == "Ayol" }?.count ?: 0
                val umumiy = erkaklar + ayollar

                item {
                    TalimTuriMainItem(
                        umumiy = umumiy,
                        Erkaklar = erkaklar,
                        Ayollar = ayollar,
                        name = eduType // `eduType` ni title sifatida yuboramiz
                    )
                }
            }
            item {
                Column {
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

                        LaunchedEffect(eduTypeAndAge) {
                            eduTypeAndAge?.let { data ->
                                xAxisScaleData = data.map { it.eduType }.distinct()
                                yAxisScaleData = data.map { it.name }.distinct() // ["1-kurs", "2-kurs", ..., "6-kurs"]
                                allDataLists = data.groupBy { it.name }
                                    .mapValues { (_, values) -> values.map { it.count } }
                                    .values.toList()
                                // To'g'ri count olish uchun groupedData ichidan qiymat bo'lmaganlar uchun nol qo'ymaymiz
                                topValue = yAxisScaleData.map { name ->
                                    val courseData = eduTypeAndAge?.find { it.name ==name }?.count // [course]
                                    if (courseData != null) {
                                        courseData
                                    } else {
                                        0
                                    }
                                }                            }
                        }

                        LazyRow(modifier = Modifier.fillMaxWidth(),
                        ) {
                            val list=listOf("30 yoshdan kattalar", "30 yoshdan kichiklar")
                            items(yAxisScaleData.size) {index->
                                Column(horizontalAlignment = Alignment.Start) {
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
                        val colors = listOf(Color.Blue, Color.Red) // Ranglarni o'zgartirish mumkin
                        Spacer(modifier = Modifier.height(10.dp))
                        Log.e("TAG", "TalimTuriScreen data: $xAxisScaleData and $allDataLists")
                        if (xAxisScaleData.isNotEmpty() && allDataLists.isNotEmpty()) {

                            StackedBarGraph(
                                xAxisScaleData = xAxisScaleData,
                                allDataLists = allDataLists,
                                colors = colors,
                                height = 400.dp,
                                barWidth = 50.dp,
                                type = true,
                                yAxisScaleData = listOf("30 yoshdan kattalar", "30 yoshdan kichiklar")
                            )
                        }
                        FlowRow(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            LegendItem(color = colors[1], label = "30 yoshdan kichiklar")
                            Spacer(modifier = Modifier.width(20.dp))
                            LegendItem(color = colors[0], label = "30 yoshdan kattalar")
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
                        Text(text = "To'lov shakli", fontWeight = FontWeight.Bold, fontSize = 20.sp)

                        Spacer(modifier = Modifier.height(5.dp))
                        var xAxisScaleData by remember { mutableStateOf<List<String>>(emptyList()) }
                        var allDataLists by remember { mutableStateOf<List<List<Int>>>(emptyList()) }
                        var yAxisScaleData by remember { mutableStateOf<List<String>>(emptyList()) }
                        var topValue by remember { mutableStateOf<List<Int>>(emptyList()) }

                        LaunchedEffect(eduTypeAndPaymentType) {
                            eduTypeAndPaymentType?.let { data ->
                                xAxisScaleData = data.map { it.eduType }.distinct()
                                allDataLists = data.groupBy { it.name }
                                    .mapValues { (_, values) -> values.map { it.count } }
                                    .values.toList()
                                yAxisScaleData = data.map { it.name }.distinct()
                                topValue = yAxisScaleData.map { course ->
                                    val courseData = eduTypeAndPaymentType?.find { it.name == course }?.count // [course]
                                    if (courseData != null) {
                                        courseData
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
                        val colors = listOf(Color.Blue, Color.Red) // Ranglarni o'zgartirish mumkin
                        Spacer(modifier = Modifier.height(10.dp))
                        Log.e("TAG", "TalimTuriScreen data: $xAxisScaleData and $allDataLists")
                        if (xAxisScaleData.isNotEmpty() && allDataLists.isNotEmpty()) {

                            StackedBarGraph(
                                xAxisScaleData = xAxisScaleData,
                                allDataLists = allDataLists,
                                colors = colors,
                                height = 400.dp,
                                barWidth = 50.dp,
                                type = true,
                                yAxisScaleData = listOf("30 yoshdan kattalar", "30 yoshdan kichiklar")
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
                            LegendItem(color = colors[0], label = "Davlat granti")
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

                        LaunchedEffect(eduTypeAndCourse) {
                            eduTypeAndCourse?.let { data ->
                                xAxisScaleData = data.map { it.eduType }.distinct()
                                val allCourses = data.map { it.name }.distinct() // ["1-kurs", "2-kurs", ..., "6-kurs"]
                                val groupedData = data.groupBy { it.name }
                                    .mapValues { (_, values) -> values.associateBy { it.eduType } }
                                allDataLists = allCourses.map { course ->
                                    xAxisScaleData.map { eduType ->
                                        groupedData[course]?.get(eduType)?.count ?: 0
                                    }
                                }
                                yAxisScaleData = data.map { it.name }.distinct()
                                topValue = yAxisScaleData.map { course ->
                                    val courseData = eduTypeAndCourse?.find { it.name == course }?.count // [course]
                                    if (courseData != null) {
                                        courseData
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
                        val colors = listOf(ColorGreen, ColorLightBlue, ColorLightOrange, ColorDeepPurple, ColorLightYellow,
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
                                yAxisScaleData = listOf("1-kurs", "2-kurs", "3-kurs", "4-kurs", "5-kurs", "6-kurs")
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
                        var dataList by remember { mutableStateOf(emptyList<Int>()) }
                        var datesList by remember { mutableStateOf(emptyList<String>()) }
                        var floatValue by remember { mutableStateOf(emptyList<Float>()) }
                        LaunchedEffect(eduTypeAndCourse) {
                            eduTypeAndCourse?.let { list ->
                                if (list.isNotEmpty()) {
                                    val filteredList = list.filter { it.eduType == "Bakalavr" }

                                      dataList = filteredList.map { it.count }
                                     datesList = filteredList.map { it.name }.distinct()

                                    val maxCount = dataList.maxOrNull() ?: 1  // Nolga bo‘linish xatosining oldini olish
                                    floatValue = dataList.map { it.toFloat() / maxCount.toFloat() }
                                }
                            }
                        }
                        if (dataList.isNotEmpty()) {
                            Row {
                                Text(text = "Kurslar (Bakalavr)", fontWeight = FontWeight.Bold)
                                AnimatedCounter(
                                    targetNumber = dataList.sum(),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16,
                                    modifier = Modifier
                                )
                            }
                            Spacer(modifier = Modifier.height(20.dp))
                            BarGraph(
                                graphBarData = floatValue,
                                xAxisScaleData = datesList,
                                barData_ = dataList,
                                height = 300.dp,
                                roundType = BarType.TOP_CURVED,
                                barWidth = 30.dp,
                                barColor = ColorLightGreen,
                                barArrangement = if (dataList.size < 6) Arrangement.SpaceBetween else Arrangement.spacedBy(
                                    15.dp
                                )
                            )
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
                        var dataList by remember { mutableStateOf(emptyList<Int>()) }
                        var datesList by remember { mutableStateOf(emptyList<String>()) }
                        var floatValue by remember { mutableStateOf(emptyList<Float>()) }
                        LaunchedEffect(eduTypeAndCourse) {
                            eduTypeAndCourse?.let { list ->
                                if (list.isNotEmpty()) {
                                    val filteredList = list.filter { it.eduType == "Magistr" }

                                    dataList = filteredList.map { it.count }
                                    datesList = filteredList.map { it.name }.distinct()

                                    val maxCount = dataList.maxOrNull() ?: 1  // Nolga bo‘linish xatosining oldini olish
                                    floatValue = dataList.map { it.toFloat() / maxCount.toFloat() }
                                }
                            }
                        }
                        if (dataList.isNotEmpty()) {
                            Row {
                                Text(text = "Kurslar (Magistratura)", fontWeight = FontWeight.Bold)
                                AnimatedCounter(
                                    targetNumber = dataList.sum(),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16,
                                    modifier = Modifier
                                )
                            }
                            Spacer(modifier = Modifier.height(20.dp))
                            BarGraph(
                                graphBarData = floatValue,
                                xAxisScaleData = datesList,
                                barData_ = dataList,
                                height = 300.dp,
                                roundType = BarType.TOP_CURVED,
                                barWidth = 30.dp,
                                barColor = ColorLightGreen,
                                barArrangement = if (dataList.size < 6) Arrangement.SpaceBetween else Arrangement.spacedBy(
                                    15.dp
                                )
                            )
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

                        LaunchedEffect(eduTypeAndCitizenship) {
                            eduTypeAndCitizenship?.let { data ->
                                xAxisScaleData = data.map { it.eduType }.distinct()
                                yAxisScaleData = data.map { it.name }.distinct() // ["1-kurs", "2-kurs", ..., "6-kurs"]
                                val groupedData = data.groupBy { it.name }
                                    .mapValues { (_, values) -> values.associateBy { it.eduType } }
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
                        val colors = listOf(ColorGreen, ColorLightBlue, ColorLightOrange, ColorDeepPurple, ColorLightYellow,
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

                        LaunchedEffect(eduTypeAndAccommodation) {
                            eduTypeAndAccommodation?.let { data ->
                                xAxisScaleData = data.map { it.eduType }.distinct()
                                yAxisScaleData = data.map { it.name }.distinct() // ["1-kurs", "2-kurs", ..., "6-kurs"]
                                val groupedData = data.groupBy { it.name }
                                    .mapValues { (_, values) -> values.associateBy { it.eduType } }
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
                        val colors = listOf(ColorGreen, ColorLightBlue, ColorLightOrange, ColorDeepPurple, ColorLightYellow,
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
                        Text(text = "Ta'lim shakli", fontWeight = FontWeight.Bold, fontSize = 20.sp)

                        Spacer(modifier = Modifier.height(5.dp))
                        var xAxisScaleData by remember { mutableStateOf<List<String>>(emptyList()) }
                        var allDataLists by remember { mutableStateOf<List<List<Int>>>(emptyList()) }
                        var yAxisScaleData by remember { mutableStateOf<List<String>>(emptyList()) }
                        var topValue by remember { mutableStateOf<List<Int>>(emptyList()) }

                        LaunchedEffect(eduTypeAndEduForm) {
                            eduTypeAndEduForm?.let { data ->
                                xAxisScaleData = data.map { it.eduType }.distinct()
                                yAxisScaleData = data.map { it.name }.distinct() // ["1-kurs", "2-kurs", ..., "6-kurs"]
                                val groupedData = data.groupBy { it.name }
                                    .mapValues { (_, values) -> values.associateBy { it.eduType } }
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
                        val colors = listOf(ColorGreen, ColorLightBlue, ColorLightOrange, ColorDeepPurple, ColorLightYellow,
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
                        var dataList by remember { mutableStateOf(emptyList<Int>()) }
                        var datesList by remember { mutableStateOf(emptyList<String>()) }
                        var floatValue by remember { mutableStateOf(emptyList<Float>()) }
                        LaunchedEffect(eduTypeAndEduForm) {
                            eduTypeAndEduForm?.let { list ->
                                if (list.isNotEmpty()) {
                                    val filteredList = list.filter { it.eduType == "Magistr" }

                                    dataList = filteredList.map { it.count }
                                    datesList = filteredList.map { it.name }.distinct()

                                    val maxCount = dataList.maxOrNull() ?: 1  // Nolga bo‘linish xatosining oldini olish
                                    floatValue = dataList.map { it.toFloat() / maxCount.toFloat() }
                                }
                            }
                        }
                        if (dataList.isNotEmpty()) {
                            Row {
                                Text(text = "Kurslar (Magistratura)", fontWeight = FontWeight.Bold)
                                AnimatedCounter(
                                    targetNumber = dataList.sum(),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16,
                                    modifier = Modifier
                                )
                            }
                            Spacer(modifier = Modifier.height(20.dp))
                            BarGraph(
                                graphBarData = floatValue,
                                xAxisScaleData = datesList,
                                barData_ = dataList,
                                height = 300.dp,
                                roundType = BarType.TOP_CURVED,
                                barWidth = 30.dp,
                                barColor = ColorLightGreen,
                                barArrangement = if (dataList.size < 6) Arrangement.SpaceBetween else Arrangement.spacedBy(
                                    15.dp
                                )
                            )
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
                        var dataList by remember { mutableStateOf(emptyList<Int>()) }
                        var datesList by remember { mutableStateOf(emptyList<String>()) }
                        var floatValue by remember { mutableStateOf(emptyList<Float>()) }
                        LaunchedEffect(eduTypeAndEduForm) {
                            eduTypeAndEduForm?.let { list ->
                                if (list.isNotEmpty()) {
                                    val filteredList = list.filter { it.eduType == "Bakalavr" }

                                    dataList = filteredList.map { it.count }
                                    datesList = filteredList.map { it.name }.distinct()

                                    val maxCount = dataList.maxOrNull() ?: 1  // Nolga bo‘linish xatosining oldini olish
                                    floatValue = dataList.map { it.toFloat() / maxCount.toFloat() }
                                }
                            }
                        }
                        if (dataList.isNotEmpty()) {
                            Row {
                                Text(text = "Kurslar (Bakalavr)", fontWeight = FontWeight.Bold)
                                AnimatedCounter(
                                    targetNumber = dataList.sum(),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16,
                                    modifier = Modifier
                                )
                            }
                            Spacer(modifier = Modifier.height(20.dp))
                            BarGraph(
                                graphBarData = floatValue,
                                xAxisScaleData = datesList,
                                barData_ = dataList,
                                height = 300.dp,
                                roundType = BarType.TOP_CURVED,
                                barWidth = 30.dp,
                                barColor = ColorLightGreen,
                                barArrangement = if (dataList.size < 6) Arrangement.SpaceBetween else Arrangement.spacedBy(
                                    15.dp
                                )
                            )
                        }
                    }
                }
            }
        }

    }
}

@Composable
fun LegendItem(color: Color, label: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(16.dp)
                .background(color, shape = CircleShape)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(text = label, fontSize = 14.sp, color = Color.Black)
    }
}

@Composable
fun TalimTuriMainItem(
    name: String,
    imageRes: Int = R.drawable.school,
    umumiy: Int,
    Erkaklar: Int,
    Ayollar: Int
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(15.dp))
            .background(Color.White)
            .padding(20.dp)
    ) {
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = null,
            modifier = Modifier
                .size(60.dp)
                .clip(CircleShape)
                .background(ColorBgSecondary)
                .padding(10.dp),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(text = "$name", fontWeight = FontWeight.Bold, fontSize = 20.sp)
        Spacer(modifier = Modifier.height(10.dp))
        Text(text = "Umumiy", color = Color.LightGray, fontSize = 15.sp)
        Spacer(modifier = Modifier.height(6.dp))
        AnimatedCounter(
            targetNumber = umumiy,
            fontWeight = FontWeight.Bold,
            fontSize = 32,
            modifier = Modifier
        )
        Spacer(modifier = Modifier.height(10.dp))
        Row {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                Text(text = "Erkaklar", color = Color.LightGray, fontSize = 15.sp)
                Row {
                    Text(
                        text = formatNumber(Erkaklar),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        text = calculatePercentage(Erkaklar, umumiy),
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = ColorGreen
                    )
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)

            ) {
                Text(text = "Ayollar", color = Color.LightGray, fontSize = 15.sp)
                Row {
                    Text(
                        text = formatNumber(Ayollar),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        text = calculatePercentage(Ayollar, umumiy),
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = ColorPink
                    )
                }
            }
        }
    }
}
