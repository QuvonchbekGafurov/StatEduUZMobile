package com.example.stateduuz.mainpage.OliyTalim.talabalar.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.stateduuz.utils.AnimatedCounter
import com.example.stateduuz.R
import com.example.stateduuz.utils.calculatePercentage
import com.example.stateduuz.chart.BarGraph
import com.example.stateduuz.chart.BarType
import com.example.stateduuz.chart.stackedbar.StackedBarGraph
import com.example.stateduuz.mainpage.OliyTalim.talabalar.viewmodel.BarGraphData
import com.example.stateduuz.mainpage.OliyTalim.talabalar.viewmodel.ChartUiState
import com.example.stateduuz.utils.formatNumber
import com.example.stateduuz.mainpage.OliyTalim.talabalar.viewmodel.EduTypeViewModel
import com.example.stateduuz.mainpage.OliyTalim.talabalar.viewmodel.GenderCardData
import com.example.stateduuz.ui.theme.BackgroundBlur
import com.example.stateduuz.ui.theme.ColorBgSecondary
import com.example.stateduuz.ui.theme.ColorDeepPurple
import com.example.stateduuz.ui.theme.ColorGreen
import com.example.stateduuz.ui.theme.ColorGreenMain
import com.example.stateduuz.ui.theme.ColorLightBlue
import com.example.stateduuz.ui.theme.ColorLightGreen
import com.example.stateduuz.ui.theme.ColorLightOrange
import com.example.stateduuz.ui.theme.ColorLightRed
import com.example.stateduuz.ui.theme.ColorLightYellow
import com.example.stateduuz.ui.theme.ColorPink
import com.example.stateduuz.utils.CardUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TalimTuriScreen(viewModel: EduTypeViewModel = hiltViewModel()) {
    // Collect individual StateFlows
    val eduTypeAndGender by viewModel.eduTypeAndGender.collectAsStateWithLifecycle()
    val eduTypeAndPaymentType by viewModel.eduTypeAndPaymentType.collectAsStateWithLifecycle()
    val eduTypeAndCourse by viewModel.eduTypeAndCourse.collectAsStateWithLifecycle()
    val eduTypeAndCourseBakalavr by viewModel.eduTypeAndCourseBakalavr.collectAsStateWithLifecycle()
    val eduTypeAndCourseMagistr by viewModel.eduTypeAndCourseMagistr.collectAsStateWithLifecycle()
    val eduTypeAndCitizenship by viewModel.eduTypeAndCitizenship.collectAsStateWithLifecycle()
    val eduTypeAndAccommodation by viewModel.eduTypeAndAccommodation.collectAsStateWithLifecycle()
    val eduTypeAndEduForm by viewModel.eduTypeAndEduForm.collectAsStateWithLifecycle()
    val eduTypeAndEduFormBakalavr by viewModel.eduTypeAndEduFormBakalavr.collectAsStateWithLifecycle()
    val eduTypeAndEduFormMagistr by viewModel.eduTypeAndEduFormMagistr.collectAsStateWithLifecycle()

    // State to track loading
    var isLoading by remember { mutableStateOf(true) }
    var isError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Trigger data fetching and track loading/error
    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
            try {
                isLoading = true
                viewModel.fetchEduTypeAndGender()
                viewModel.fetchEduTypeAndAge()
                viewModel.fetchEduTypeAndPaymentType()
                viewModel.fetchEduTypeAndCourse()
                viewModel.fetchEduTypeAndCitizenship()
                viewModel.fetchEduTypeAndAccommodation()
                viewModel.fetchEduTypeAndEduForm()


                isLoading = false

            } catch (e: Exception) {
                isError = true
                errorMessage = "Ma'lumotlarni yuklashda xatolik: ${e.message}"
                Log.e("TalimTuriScreen", "Error fetching data: ${e.message}", e)
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        when {
            isLoading -> {
                Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator()
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Ma'lumotlar yuklanmoqda...",
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }

            isError -> {
                Column(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = errorMessage ?: "Noma'lum xatolik yuz berdi",
                        color = Color.Red,
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = {
                            isLoading = true
                            isError = false
                            errorMessage = null
                            viewModel.fetchEduTypeAndGender()
                            viewModel.fetchEduTypeAndAge()
                            viewModel.fetchEduTypeAndPaymentType()
                            viewModel.fetchEduTypeAndCourse()
                            viewModel.fetchEduTypeAndCitizenship()
                            viewModel.fetchEduTypeAndAccommodation()
                            viewModel.fetchEduTypeAndEduForm()
                        },
                        modifier = Modifier.padding(8.dp)
                    ) {
                    }
                }
            }

            else -> {
                Column(
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    LazyColumn(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        contentPadding = PaddingValues(bottom = 16.dp)
                    ) {
                        item {
                            Spacer(modifier = Modifier.height(12.dp))
                            GenderCardsSection(eduTypeAndGender)
                            Spacer(modifier = Modifier.height(12.dp))
                            ChartSection(
                                title = "To'lov shakli",
                                chartData = eduTypeAndPaymentType,
                                type = true,
                                yAxisOverride = listOf(
                                    "Grant",
                                    "To'lov-kontrakt"
                                ) // Corrected yAxisOverride
                            )
                            Spacer(modifier = Modifier.height(20.dp))
                            ChartSection(
                                title = "Kurslar",
                                chartData = eduTypeAndCourse,
                                type = true,
                                yAxisOverride = listOf(
                                    "1-kurs",
                                    "2-kurs",
                                    "3-kurs",
                                    "4-kurs",
                                    "5-kurs",
                                    "6-kurs"
                                )
                            )
                            Spacer(modifier = Modifier.height(20.dp))
                            BarGraphSection(
                                title = "Kurslar (Bakalavr)",
                                barGraphData = eduTypeAndCourseBakalavr
                            )
                            Spacer(modifier = Modifier.height(20.dp))
                            BarGraphSection(
                                title = "Kurslar (Magistratura)",
                                barGraphData = eduTypeAndCourseMagistr
                            )
                            Spacer(modifier = Modifier.height(20.dp))
                            ChartSection(
                                title = "Fuqaroligi",
                                chartData = eduTypeAndCitizenship,
                                type = true,
                                yAxisOverride = listOf("O'zbekiston fuqarosi", "Xorijiy fuqaro")
                            )
                            Spacer(modifier = Modifier.height(20.dp))
                            ChartSection(
                                title = "Yashash joyi",
                                chartData = eduTypeAndAccommodation,
                                type = true,
                                yAxisOverride = listOf("Yotoqxonada", "Ijarada", "O'z uyida")
                            )
                            Spacer(modifier = Modifier.height(20.dp))
                            ChartSection(
                                title = "Ta'lim shakli",
                                chartData = eduTypeAndEduForm,
                                type = true,
                                yAxisOverride = listOf(
                                    "Kunduzgi", "Sirtqi", "Kechki", "Masofaviy",
                                    "Qo'shma", "Maxsus sirtqi", "Ikkinchi Oliy(kunduzgi)",
                                    "Ikkinchi Oliy(sirtqi)", "Ikkinchi Oliy(kechki)"
                                )
                            )
                            Spacer(modifier = Modifier.height(20.dp))
                            BarGraphSection(
                                title = "Ta'lim shakli (Bakalavr)",
                                barGraphData = eduTypeAndEduFormBakalavr
                            )
                            Spacer(modifier = Modifier.height(20.dp))
                            BarGraphSection(
                                title = "Ta'lim shakli (Magistratura)",
                                barGraphData = eduTypeAndEduFormMagistr
                            )
                            Spacer(modifier = Modifier.height(20.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun GenderCardsSection(cards: List<GenderCardData>?, modifier: Modifier = Modifier) {
    when {
        cards == null -> Text(
            text = "Ma'lumotlar yuklanmoqda...",
            modifier = modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            fontSize = 14.sp
        )

        cards.isEmpty() -> Text(
            text = "Jins bo'yicha ma'lumotlar mavjud emas",
            modifier = modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            fontSize = 14.sp,
            color = Color.Gray
        )

        else -> {
            Row(
                modifier = modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                cards.getOrNull(2)?.let { card ->
                    CardUtils(
                        maintext = card.eduType,
                        icon = R.drawable.stu_or,
                        modifier = Modifier.weight(1f),
                        lists = listOf(card.maleCount, card.femaleCount),
                        listsname = listOf("Erkak", "Ayollar")
                    )
                }
                cards.getOrNull(1)?.let { card ->
                    CardUtils(
                        maintext = card.eduType,
                        icon = R.drawable.stu_magist,
                        modifier = Modifier.weight(1f),
                        lists = listOf(card.maleCount, card.femaleCount),
                        listsname = listOf("Erkak", "Ayollar")
                    )
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                cards.getOrNull(0)?.let { card ->
                    CardUtils(
                        maintext = card.eduType,
                        icon = R.drawable.student_back,
                        modifier = Modifier.weight(1f),
                        lists = listOf(card.maleCount, card.femaleCount),
                        listsname = listOf("Erkak", "Ayollar")
                    )
                }
            }
        }
    }
}

@Composable
private fun ChartSection(
    title: String,
    chartData: ChartUiState?,
    type: Boolean,
    yAxisOverride: List<String>? = null,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(MaterialTheme.colorScheme.onSecondary)
            .padding(10.dp)
    ) {
        Text(
            text = title,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            color = MaterialTheme.colorScheme.secondary
        )
        Spacer(modifier = Modifier.height(5.dp))
        when {
            chartData == null -> Text(
                text = "Ma'lumotlar yuklanmoqda...",
                modifier = Modifier.align(Alignment.CenterHorizontally),
                textAlign = TextAlign.Center,
                fontSize = 14.sp
            )

            chartData.xAxis.isEmpty() || chartData.allData.isEmpty() -> Text(
                text = "Ma'lumotlar mavjud emas",
                modifier = Modifier.align(Alignment.CenterHorizontally),
                textAlign = TextAlign.Center,
                fontSize = 14.sp,
                color = Color.Gray
            )

            else -> StackedBarGraph(
                xAxisScaleData = chartData.xAxis,
                allDataLists = chartData.allData,
                height = 400.dp,
                barWidth = 50.dp,
                type = type,
                yAxisScaleData = yAxisOverride ?: chartData.yAxis
            )
        }
    }
}

@Composable
private fun BarGraphSection(
    title: String,
    barGraphData: BarGraphData?,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(MaterialTheme.colorScheme.onSecondary)
            .padding(10.dp)
    ) {
        when {
            barGraphData == null -> Text(
                text = "Ma'lumotlar yuklanmoqda...",
                modifier = Modifier.align(Alignment.CenterHorizontally),
                textAlign = TextAlign.Center,
                fontSize = 14.sp
            )

            barGraphData.counts.isEmpty() -> Text(
                text = "Ma'lumotlar mavjud emas",
                modifier = Modifier.align(Alignment.CenterHorizontally),
                textAlign = TextAlign.Center,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.secondary
            )

            else -> {
                Row {
                    Text(text = title, fontWeight = FontWeight.Bold, fontSize = 20.sp, color = MaterialTheme.colorScheme.secondary)
                    AnimatedCounter(
                        targetNumber = barGraphData.counts.sum(),
                        fontWeight = FontWeight.Bold,
                        fontSize = 16,
                        modifier = Modifier,
                    )
                }
                Spacer(modifier = Modifier.height(20.dp))
                BarGraph(
                    graphBarData = barGraphData.normalizedValues,
                    xAxisScaleData = barGraphData.labels,
                    barData_ = barGraphData.counts,
                    height = 300.dp,
                    roundType = BarType.TOP_CURVED,
                    barWidth = 30.dp,
                    barColor = ColorLightGreen,
                    barArrangement = if (barGraphData.counts.size < 6) Arrangement.SpaceBetween else Arrangement.spacedBy(
                        15.dp
                    )
                )
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
        Text(text = label, fontSize = 14.sp, color = MaterialTheme.colorScheme.secondary)
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
