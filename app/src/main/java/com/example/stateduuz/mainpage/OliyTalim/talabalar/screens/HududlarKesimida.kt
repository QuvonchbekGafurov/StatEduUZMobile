package com.example.stateduuz.mainpage.OliyTalim.talabalar.screens

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.stateduuz.utils.AnimatedCounter
import com.example.stateduuz.chart.stackedbar.StackedBarGraph
import com.example.stateduuz.mainpage.OliyTalim.talabalar.viewmodel.ChartUiState
import com.example.stateduuz.mainpage.OliyTalim.talabalar.viewmodel.RegionViewModel
import com.example.stateduuz.ui.theme.ColorDeepPurple
import com.example.stateduuz.ui.theme.ColorGreen
import com.example.stateduuz.ui.theme.ColorLightBlue
import com.example.stateduuz.ui.theme.ColorLightOrange
import com.example.stateduuz.ui.theme.ColorLightYellow

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun HududlarKesimidaScreen(viewModel: RegionViewModel = hiltViewModel()) {
    // Ma'lumotlarni StateFlow orqali kuzatish
    val regionAndGender by viewModel.regionAndGender.collectAsState()
    val regionAndEduType by viewModel.regionAndEduType.collectAsState()
    val regionAndEduForm by viewModel.regionAndEduForm.collectAsState()
    val regionAndCourse by viewModel.regionAndCourse.collectAsState()
    val regionAndAge by viewModel.regionAndAge.collectAsState()
    val regionAndPaymentType by viewModel.regionAndPaymentType.collectAsState()
    val regionAndCitizenship by viewModel.regionAndCitizenship.collectAsState()
    val regionAndAccommodation by viewModel.regionAndAccommodation.collectAsState()

    // Ma'lumotlarni olish uchun LaunchedEffect
    LaunchedEffect(Unit) {
        viewModel.fetchRegionAndGender()
        viewModel.fetchRegionAndEduType()
        viewModel.fetchRegionAndEduForm()
        viewModel.fetchRegionAndCourse()
        viewModel.fetchRegionAndAge()
        viewModel.fetchRegionAndPaymentType()
        viewModel.fetchRegionAndCitizenship()
        viewModel.fetchRegionAndAccommodation()
    }


    LazyColumn(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
    ) {
        item {
            Spacer(modifier = Modifier.height(20.dp))

            // Jins
            regionAndGender?.let { data ->
                ChartCard(title = "Jins", data = data, type = true)
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Ta'lim turi
            regionAndEduType?.let { data ->
                ChartCard(title = "Ta'lim turi", data = data, type = false)
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Yosh
            regionAndAge?.let { data ->
                ChartCard(title = "Yosh", data = data, type = true)
            }

            Spacer(modifier = Modifier.height(20.dp))

            // To'lov shakli
            regionAndPaymentType?.let { data ->
                ChartCard(title = "To'lov shakli", data = data, type = true)
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Kurslar
            regionAndCourse?.let { data ->
                ChartCard(title = "Kurslar", data = data, type = true)
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Fuqarolik
            regionAndCitizenship?.let { data ->
                ChartCard(title = "Fuqarolik", data = data, type = true)
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Yashash joyi
            regionAndAccommodation?.let { data ->
                ChartCard(title = "Yashash joyi", data = data, type = true)
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Ta'lim shakli
            regionAndEduForm?.let { data ->
                ChartCard(title = "Ta'lim shakli", data = data, type = true)
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }

}

// ChartCard komponenti
@Composable
fun ChartCard(title: String, data: ChartUiState, type: Boolean) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(MaterialTheme.colorScheme.onSecondary)
            .padding(10.dp)
    ) {
        Text(text = title, fontWeight = FontWeight.Bold, fontSize = 20.sp, color = MaterialTheme.colorScheme.secondary)
        Spacer(modifier = Modifier.height(5.dp))
        StackedBarGraph(
            xAxisScaleData = data.xAxis,
            allDataLists = data.allData,
            yAxisScaleData = data.yAxis,
            height = 400.dp,
            barWidth = 50.dp,
            type = type
        )
    }
}