package com.example.stateduuz.mainpage.OliyTalim.umumiy

import android.util.Log
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.example.stateduuz.chart.BarGraph
import com.example.stateduuz.chart.BarType
import com.example.stateduuz.chart.PieChart
import com.example.stateduuz.chart.PieChartInput
import com.example.stateduuz.ui.theme.ColorBlue
import com.example.stateduuz.ui.theme.ColorGreen
import com.example.stateduuz.ui.theme.ColorLightBlue
import com.example.stateduuz.ui.theme.ColorLightGreen
import com.example.stateduuz.ui.theme.ColorOrange
import com.example.stateduuz.ui.theme.ColorYellow
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.stateduuz.R
import com.example.stateduuz.chart.BarGraphCard
import com.example.stateduuz.chart.PieChartCard
import com.example.stateduuz.utils.AnimatedSelection
import com.example.stateduuz.utils.CardUtils
import kotlinx.coroutines.delay

val colors = listOf(
    Color(0xFF4DA2F1),
    Color(0xFFFF6482),
    Color(0xFF43B1A0),
    Color(0xFFFF7F00),
    Color(0xFFFFD426),
    Color(0xFF7D7AFF),
    Color(0xFF4DA2F1),
    Color(0xFFFF6482),
    Color(0xFF43B1A0),
    Color(0xFF43B1A0),
    Color(0xFFFF7F00),
    Color(0xFFFFD426)
)

@Composable
fun UmumiyScreen(viewModel: UmumiyViewModel = hiltViewModel()) {
    // Collect the UI state from the ViewModel
    val uiState by viewModel.uiState.collectAsState()

    // State to manage data loading animation (optional, for 3-second delay effect)
    var isDataLoaded by remember { mutableStateOf(false) }
    LaunchedEffect(uiState.isLoading) {
        if (!uiState.isLoading) {
            delay(1000)
            isDataLoaded = true
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Show loading indicator if data is not loaded
        if (uiState.isLoading || !isDataLoaded) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center)
            )
        } else if (uiState.error != null) {
            Text(
                text = "Xatolik: ${uiState.error}",
                color = Color.Red,
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(16.dp),
                textAlign = TextAlign.Center
            )
        } else {
            // Main content
            val scrollState = rememberScrollState()
            LazyColumn(
                modifier = Modifier
                    .padding(horizontal = 20.dp)
            ) {
                item {
                    Column(modifier = Modifier.padding(top = 20.dp)) {
                        Row {
                            CardUtils(
                                maintext = "Oliy talim muassasalar soni",
                                modifier = Modifier.weight(1f),
                                icon = R.drawable.student_back,
                                iconSize = 40.dp,
                                lists = uiState.univerCount,
                                listsname = listOf("Davlat", "Nodavlat", "Xorijiy")
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            CardUtils(
                                maintext = "Professor-o'qituvchilar soni",
                                modifier = Modifier.weight(1f),
                                iconSize = 40.dp,
                                icon = R.drawable.umumiy_prof,
                                lists = uiState.teachersCount,
                                listsname = listOf("Erkak", "Ayollar")
                            )
                        }
                        Spacer(modifier = Modifier.height(10.dp))

                        CardUtils(
                            maintext = "Talabalar Soni",
                            modifier = Modifier.fillMaxWidth(),
                            iconSize = 50.dp,
                            icon = R.drawable.umumiy_3,
                            lists = uiState.studentsCount,
                            listsname = listOf("Bakalavr", "Magistratura", "Ordinatura")
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))

                    // University Ownership Pie Chart
                    uiState.universityOwnership?.let { data ->
                        if (data.totalCount > 0) {
                            PieChartCard(
                                title = "OTMlar soni mulkchilik shakli bo'yicha",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(250.dp),
                                radius = 100.dp,
                                innerRadius = 50.dp,
                                input = data.inputs,
                                centerText = "${data.totalCount}"
                            )
                        } else {
                            Text(
                                text = "Ma'lumot mavjud emas",
                                fontSize = 16.sp,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    } ?: Text(
                        text = "Ma'lumot yuklanmoqda...",
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(15.dp))

                    // University Type Bar Graph
                    uiState.universityType?.let { data ->
                        if (data.dataList.isNotEmpty()) {
                            BarGraphCard(
                                title = "OTMlar soni Tashkiliy turi bo'yicha",
                                graphBarData = data.float,
                                xAxisScaleData = data.datesList,
                                barData_ = data.dataList,
                                height = 300.dp,
                                roundType = BarType.TOP_CURVED,
                                barWidth = 30.dp,
                                barColor = ColorLightGreen,
                                barArrangement = if (data.dataList.size < 6) Arrangement.SpaceBetween else Arrangement.spacedBy(
                                    15.dp
                                )
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(20.dp))

                    // Ownership and Gender Pie Chart
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(MaterialTheme.colorScheme.onSecondary)
                            .padding(10.dp)
                    ) {
                        val myList = listOf("Jami", "Davlat", "Nodavlat", "Xorijiy")
                        var selectedCategory by remember { mutableStateOf("Jami") }

                        Text(text = "Talabalar soni jins kesimida", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.secondary)
                        Spacer(modifier = Modifier.height(10.dp))
                        AnimatedSelection(list = myList) { selected ->
                            selectedCategory = selected
                            viewModel.setCategory(selected) // Trigger category change
                            Log.d("TAG", "Tanlangan: $selected")
                        }
                        Spacer(modifier = Modifier.height(10.dp))

                        uiState.ownershipAndGender?.let { data ->
                            key(data) {
                                PieChart(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(250.dp),
                                    radius = 100.dp,
                                    innerRadius = 50.dp,
                                    input = data.inputs,
                                    centerText = "${data.totalCount}"
                                )
                            }
                        } ?: Text(
                            text = "Ma'lumot yuklanmoqda...",
                            fontSize = 16.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    Spacer(modifier = Modifier.height(20.dp))

                    // Ownership and Education Type Bar Graph
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(MaterialTheme.colorScheme.onSecondary)
                            .padding(10.dp)
                    ) {
                        val myList = listOf("Jami", "Davlat", "Nodavlat", "Xorijiy")
                        var selectedCategory by remember { mutableStateOf("Jami") }

                        Text(
                            text = "Talabalar soni ta'lim turi kesimida",
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.secondary
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        AnimatedSelection(list = myList) { selected ->
                            selectedCategory = selected
                            viewModel.setCategory(selected)
                            Log.d("TAG", "Tanlangan: $selected")
                        }
                        Spacer(modifier = Modifier.height(20.dp))

                        uiState.ownershipAndEduType?.let { data ->
                            key(data) {
                                BarGraph(
                                    graphBarData = data.float,
                                    xAxisScaleData = data.datesList,
                                    barData_ = data.dataList,
                                    height = 300.dp,
                                    roundType = BarType.TOP_CURVED,
                                    barWidth = 30.dp,
                                    barColor = ColorLightGreen,
                                    barArrangement = if (data.dataList.size < 6) Arrangement.SpaceBetween else Arrangement.spacedBy(
                                        15.dp
                                    )
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(20.dp))

                    // Ownership and Course Bar Graph
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(MaterialTheme.colorScheme.onSecondary)
                            .padding(10.dp)
                    ) {
                        val myList = listOf("Jami", "Davlat", "Nodavlat", "Xorijiy")
                        var selectedCategory by remember { mutableStateOf("Jami") }

                        Text(text = "Talabalar soni kurslar kesimida", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.secondary)
                        Spacer(modifier = Modifier.height(10.dp))
                        AnimatedSelection(list = myList) { selected ->
                            selectedCategory = selected
                            viewModel.setCategory(selected)
                            Log.d("TAG", "Tanlangan: $selected")
                        }
                        Spacer(modifier = Modifier.height(20.dp))

                        uiState.ownershipAndCourse?.let { data ->
                            key(data) {
                                BarGraph(
                                    graphBarData = data.float,
                                    xAxisScaleData = data.datesList,
                                    barData_ = data.dataList,
                                    height = 300.dp,
                                    roundType = BarType.TOP_CURVED,
                                    barWidth = 30.dp,
                                    barColor = ColorLightGreen,
                                    barArrangement = if (data.dataList.size < 6) Arrangement.SpaceBetween else Arrangement.spacedBy(
                                        15.dp
                                    )
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(20.dp))

                    // Ownership and Payment Pie Chart
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(MaterialTheme.colorScheme.onSecondary)
                            .padding(10.dp)
                    ) {
                        val myList = listOf("Jami", "Davlat", "Nodavlat", "Xorijiy")
                        var selectedCategory by remember { mutableStateOf("Jami") }

                        Text(
                            text = "Talabalar soni to'lov shakli kesimida",
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.secondary
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        AnimatedSelection(list = myList) { selected ->
                            selectedCategory = selected
                            viewModel.setCategory(selected)
                            Log.d("TAG", "Tanlangan: $selected")
                        }
                        Spacer(modifier = Modifier.height(10.dp))

                        uiState.ownershipAndPayment?.let { data ->
                            key(data) {
                                PieChart(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(250.dp),
                                    radius = 100.dp,
                                    innerRadius = 50.dp,
                                    input = data.inputs,
                                    centerText = "${data.totalCount}"
                                )
                            }
                        } ?: Text(
                            text = "Ma'lumot yuklanmoqda...",
                            fontSize = 16.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    Spacer(modifier = Modifier.height(20.dp))

                    // Ownership and Education Form Bar Graph
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(MaterialTheme.colorScheme.onSecondary)
                            .padding(10.dp)
                    ) {
                        val myList = listOf("Jami", "Davlat", "Nodavlat", "Xorijiy")
                        var selectedCategory by remember { mutableStateOf("Jami") }

                        Text(
                            text = "Talabalar soni ta'lim shakli kesimida",
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.secondary
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        AnimatedSelection(list = myList) { selected ->
                            selectedCategory = selected
                            viewModel.setCategory(selected)
                        }
                        Spacer(modifier = Modifier.height(20.dp))

                        uiState.ownershipAndEduForm?.let { data ->
                            key(data) {
                                BarGraph(
                                    graphBarData = data.float,
                                    xAxisScaleData = data.datesList,
                                    barData_ = data.dataList,
                                    height = 300.dp,
                                    roundType = BarType.TOP_CURVED,
                                    barWidth = 30.dp,
                                    barColor = ColorBlue,
                                    barArrangement = if (data.dataList.size < 6) Arrangement.SpaceBetween else Arrangement.spacedBy(
                                        15.dp
                                    )
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(20.dp))

                    // University Address Bar Graph
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(MaterialTheme.colorScheme.onSecondary)
                            .padding(10.dp)
                    ) {
                        uiState.universityAddress?.let { data ->
                            if (data.dataList.isNotEmpty()) {
                                Text(
                                    text = "Respublikada ta'lim olayotgan talabalar soni OTM joylashgan hudud kesimida",
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.secondary
                                )
                                Spacer(modifier = Modifier.height(20.dp))
                                BarGraph(
                                    graphBarData = data.float,
                                    xAxisScaleData = data.datesList,
                                    barData_ = data.dataList,
                                    height = 300.dp,
                                    roundType = BarType.TOP_CURVED,
                                    barWidth = 30.dp,
                                    barColor = ColorLightBlue,
                                    barArrangement = if (data.dataList.size < 6) Arrangement.SpaceBetween else Arrangement.spacedBy(
                                        15.dp
                                    )
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(20.dp))

                    // Top 5 Dense Regions Bar Graph
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(MaterialTheme.colorScheme.onSecondary)
                            .padding(10.dp)
                    ) {
                        uiState.top5DenseRegions?.let { data ->
                            if (data.dataList.isNotEmpty()) {
                                Text(
                                    text = "Talabalar eng zich joylashgan top 5 ta hudud",
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.secondary
                                )
                                Spacer(modifier = Modifier.height(20.dp))
                                BarGraph(
                                    graphBarData = data.float,
                                    xAxisScaleData = data.datesList,
                                    barData_ = data.dataList,
                                    height = 300.dp,
                                    roundType = BarType.TOP_CURVED,
                                    barWidth = 30.dp,
                                    barColor = ColorOrange,
                                    barArrangement = if (data.dataList.size < 6) Arrangement.SpaceBetween else Arrangement.spacedBy(
                                        15.dp
                                    )
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(20.dp))

                    // Top 5 Graduate Regions Bar Graph
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(MaterialTheme.colorScheme.onSecondary)
                            .padding(10.dp)
                    ) {
                        uiState.top5GraduateRegions?.let { data ->
                            if (data.dataList.isNotEmpty()) {
                                Text(
                                    text = "Eng ko'p oliy ma'lumotli kadrlar yetishib chiqayotgan top 5 ta hudud",
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.secondary
                                )
                                Spacer(modifier = Modifier.height(20.dp))
                                BarGraph(
                                    graphBarData = data.float,
                                    xAxisScaleData = data.datesList,
                                    barData_ = data.dataList,
                                    height = 300.dp,
                                    roundType = BarType.TOP_CURVED,
                                    barWidth = 30.dp,
                                    barColor = ColorOrange,
                                    barArrangement = if (data.dataList.size < 6) Arrangement.SpaceBetween else Arrangement.spacedBy(
                                        15.dp
                                    )
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(20.dp))

                    // Students by Region Bar Graph
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(MaterialTheme.colorScheme.onSecondary)
                            .padding(10.dp)
                    ) {
                        uiState.studentsByRegion?.let { data ->
                            if (data.dataList.isNotEmpty()) {
                                Text(
                                    text = "Respublikada ta'lim olayotgan talabalar soni OTM joylashgan hudud kesimida",
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.secondary
                                )
                                Spacer(modifier = Modifier.height(20.dp))
                                BarGraph(
                                    graphBarData = data.float,
                                    xAxisScaleData = data.datesList,
                                    barData_ = data.dataList,
                                    height = 300.dp,
                                    roundType = BarType.TOP_CURVED,
                                    barWidth = 30.dp,
                                    barColor = ColorGreen,
                                    barArrangement = if (data.dataList.size < 6) Arrangement.SpaceBetween else Arrangement.spacedBy(
                                        15.dp
                                    )
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(20.dp))

                    // Top 5 Universities Bar Graph
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(MaterialTheme.colorScheme.onSecondary)
                            .padding(10.dp)
                    ) {
                        uiState.topFiveUniversity?.let { data ->
                            if (data.dataList.isNotEmpty()) {
                                Text(
                                    text = "Eng ko'p talabaga ega top 5 ta OTM",
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.secondary
                                )
                                Spacer(modifier = Modifier.height(20.dp))
                                BarGraph(
                                    graphBarData = data.float,
                                    xAxisScaleData = data.datesList,
                                    barData_ = data.dataList,
                                    height = 300.dp,
                                    roundType = BarType.TOP_CURVED,
                                    barWidth = 30.dp,
                                    barColor = ColorBlue,
                                    barArrangement = if (data.dataList.size < 6) Arrangement.SpaceBetween else Arrangement.spacedBy(
                                        15.dp
                                    )
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(20.dp))

                    // Ownership Data Bar Graph
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(MaterialTheme.colorScheme.onSecondary)
                            .padding(10.dp)
                    ) {
                        uiState.ownershipData?.let { data ->
                            if (data.dataList.isNotEmpty()) {
                                Text(
                                    text = "Talabalar soni OTM mulkchilik shakli kesimida",
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.secondary
                                )
                                Spacer(modifier = Modifier.height(20.dp))
                                BarGraph(
                                    graphBarData = data.float,
                                    xAxisScaleData = data.datesList,
                                    barData_ = data.dataList,
                                    height = 300.dp,
                                    roundType = BarType.TOP_CURVED,
                                    barWidth = 30.dp,
                                    barColor = ColorLightGreen,
                                    barArrangement = if (data.dataList.size < 6) Arrangement.SpaceBetween else Arrangement.spacedBy(
                                        15.dp
                                    )
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(20.dp))

                    // Gender Teacher Data Pie Chart
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(MaterialTheme.colorScheme.onSecondary)
                            .padding(10.dp)
                    ) {
                        val myList = listOf("Jami", "Davlat", "Nodavlat", "Xorijiy")
                        var selectedCategory by remember { mutableStateOf("Jami") }

                        Text(
                            text = "Professor-o'qituvchilar jins kesimida",
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.secondary
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        AnimatedSelection(list = myList) { selected ->
                            selectedCategory = selected
                            viewModel.setCategory(selected)
                            Log.d("TAG", "Tanlangan: $selected")
                        }
                        Spacer(modifier = Modifier.height(10.dp))

                        uiState.genderTeacherData?.let { data ->
                            key(data) {
                                PieChart(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(250.dp),
                                    radius = 100.dp,
                                    innerRadius = 50.dp,
                                    input = data.inputs,
                                    centerText = "${data.totalCount}"
                                )
                            }
                        } ?: Text(
                            text = "Ma'lumot yuklanmoqda...",
                            fontSize = 16.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    Spacer(modifier = Modifier.height(20.dp))

                    // Ownership and Academic Rank Pie Chart
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(MaterialTheme.colorScheme.onSecondary)
                            .padding(10.dp)
                    ) {
                        val myList = listOf("Jami", "Davlat", "Nodavlat", "Xorijiy")
                        var selectedCategory by remember { mutableStateOf("Jami") }

                        Text(
                            text = "Professor-o'qituvchilar ilmiy unvon kesimida",
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.secondary
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        AnimatedSelection(list = myList) { selected ->
                            selectedCategory = selected
                            viewModel.setCategory(selected)
                            Log.d("TAG", "Tanlangan: $selected")
                        }
                        Spacer(modifier = Modifier.height(10.dp))

                        uiState.ownershipAndAcademicRank?.let { data ->
                            if (data.totalCount > 0) {
                                key(data) {
                                    PieChart(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(250.dp),
                                        radius = 100.dp,
                                        innerRadius = 50.dp,
                                        input = data.inputs,
                                        centerText = "${data.totalCount}"
                                    )
                                }
                            } else {
                                Text(
                                    text = "Ma'lumot mavjud emas",
                                    fontSize = 16.sp,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        } ?: Text(
                            text = "Ma'lumot yuklanmoqda...",
                            fontSize = 16.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    Spacer(modifier = Modifier.height(20.dp))

                    // Ownership and Academic Degree Pie Chart
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(MaterialTheme.colorScheme.onSecondary)
                            .padding(10.dp)
                    ) {
                        val myList = listOf("Jami", "Davlat", "Nodavlat", "Xorijiy")
                        var selectedCategory by remember { mutableStateOf("Jami") }

                        Text(
                            text = "Professor-o'qituvchilar ilmiy daraja kesimida",
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.secondary
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        AnimatedSelection(list = myList) { selected ->
                            selectedCategory = selected
                            viewModel.setCategory(selected)
                        }
                        Spacer(modifier = Modifier.height(10.dp))

                        uiState.ownershipAndAcademicDegree?.let { data ->
                            if (data.totalCount > 0) {
                                key(data) {
                                    PieChart(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(250.dp),
                                        radius = 100.dp,
                                        innerRadius = 50.dp,
                                        input = data.inputs,
                                        centerText = "${data.totalCount}"
                                    )
                                }
                            } else {
                                Text(
                                    text = "Ma'lumot mavjud emas",
                                    fontSize = 16.sp,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        } ?: Text(
                            text = "Ma'lumot yuklanmoqda...",
                            fontSize = 16.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
        }
    }
}

