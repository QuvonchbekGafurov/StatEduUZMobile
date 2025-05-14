package com.example.stateduuz.mainpage.ProfessionalTalim.profScreens

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.stateduuz.mainpage.ProfessionalTalim.CustomHorizontalBarChart
import com.example.stateduuz.mainpage.ProfessionalTalim.OquvchilarViewModel
import com.example.stateduuz.model.Professional.profcurrentLive.ProfCurrentLive


@Composable
fun ProfCurrentLiveScreen(viewModel: OquvchilarViewModel = hiltViewModel()) {
    val scrollState = rememberScrollState()
    val genderChartData by viewModel.liveGenderChartData.collectAsState()

    LaunchedEffect(Unit) { viewModel.fetchCurrentLivePlaces() }

    Column(
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .verticalScroll(scrollState)
    ) {
        Spacer(modifier = Modifier.height(10.dp))
        genderChartData?.let { data ->
            CustomHorizontalBarChart(
                title = "Yashash joyi",
                xAxisScaleData = data.xAxisScale,
                yAxisScaleData = data.yAxisScale,
                allDataList = data.allDataList,
                type = data.type
            )
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}