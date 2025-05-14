package com.example.stateduuz.mainpage.OliyTalim.talabalar.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.stateduuz.utils.AnimatedCounter
import com.example.stateduuz.chart.stackedbar.StackedBarGraph
import com.example.stateduuz.mainpage.OliyTalim.talabalar.viewmodel.ChartUiState
import com.example.stateduuz.mainpage.OliyTalim.talabalar.viewmodel.CitizenshipViewModel
import com.example.stateduuz.ui.theme.ColorDeepPurple
import com.example.stateduuz.ui.theme.ColorGreen
import com.example.stateduuz.ui.theme.ColorLightBlue
import com.example.stateduuz.ui.theme.ColorLightOrange
import com.example.stateduuz.ui.theme.ColorLightYellow

@Composable
fun FuqaroligiScreen(viewModel: CitizenshipViewModel = hiltViewModel()) {
    val genderState by viewModel.genderChartState.collectAsState()
    val ageState by viewModel.ageChartState.collectAsState()
    val courseState by viewModel.courseChartState.collectAsState()

    LazyColumn(
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .fillMaxSize()
    ) {
        item {
            Spacer(modifier = Modifier.height(20.dp))
            ChartSection("Gender", genderState, type = false)
            Spacer(modifier = Modifier.height(20.dp))

            ChartSection("Yoshi", ageState, type = true)
            Spacer(modifier = Modifier.height(20.dp))

            ChartSection("Kurslar", courseState, type = true)
            Spacer(modifier = Modifier.height(20.dp))
        }
    }


}

@Composable
fun ChartSection(title: String, chartData: ChartUiState?, type: Boolean) {
    Column(
        modifier = Modifier
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

        if (chartData != null && chartData.xAxis.isNotEmpty() && chartData.allData.isNotEmpty()) {
            StackedBarGraph(
                xAxisScaleData = chartData.xAxis,
                yAxisScaleData = chartData.yAxis,
                allDataLists = chartData.allData,
                height = 400.dp,
                barWidth = 50.dp,
                type = type
            )
        } else {
            CircularProgressIndicator()
        }

        Spacer(modifier = Modifier.height(10.dp))
    }
}
