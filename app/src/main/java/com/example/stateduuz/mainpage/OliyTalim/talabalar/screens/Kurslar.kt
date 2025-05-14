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
import androidx.compose.material.CircularProgressIndicator
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
import com.example.stateduuz.mainpage.OliyTalim.talabalar.viewmodel.CourseViewModel
import com.example.stateduuz.ui.theme.ColorDeepPurple
import com.example.stateduuz.ui.theme.ColorGreen
import com.example.stateduuz.ui.theme.ColorLightBlue
import com.example.stateduuz.ui.theme.ColorLightOrange
import com.example.stateduuz.ui.theme.ColorLightYellow

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun KurslarScreen(viewModel: CourseViewModel = hiltViewModel()) {
    val courseAndGender by viewModel.courseAndGender.collectAsState()
    val courseAndAge by viewModel.courseAndAge.collectAsState()
    val courseAndAccommodation by viewModel.courseAndAccommodation.collectAsState()

    // Trigger data fetching
    LaunchedEffect(Unit) {
        viewModel.fetchCourseAndGender()
        viewModel.fetchCourseAndAge()
        viewModel.fetchCourseAndAccommodation()
    }

    Column(
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .fillMaxSize()
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Column {
                    Spacer(modifier = Modifier.height(20.dp))

                    // Course and Gender Chart
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(MaterialTheme.colorScheme.onSecondary)
                            .padding(10.dp)
                    ) {
                        Text(
                            text = "Jins bo'yicha",
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            color = MaterialTheme.colorScheme.secondary
                        )
                        Spacer(modifier = Modifier.height(5.dp))

                        courseAndGender?.let { chartData ->
                            if (chartData.xAxis.isNotEmpty() && chartData.allData.isNotEmpty()) {
                                StackedBarGraph(
                                    xAxisScaleData = chartData.xAxis,
                                    allDataLists = chartData.allData,
                                    height = 400.dp,
                                    barWidth = 50.dp,
                                    type = false,
                                    yAxisScaleData = chartData.yAxis
                                )
                            }
                        } ?: CircularProgressIndicator() // Show loading indicator
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Course and Accommodation Chart
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(MaterialTheme.colorScheme.onSecondary)
                            .padding(10.dp)
                    ) {
                        Text(
                            text = "Yashash joyi",
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            color = MaterialTheme.colorScheme.secondary
                        )
                        Spacer(modifier = Modifier.height(5.dp))

                        courseAndAccommodation?.let { chartData ->
                            if (chartData.xAxis.isNotEmpty() && chartData.allData.isNotEmpty()) {
                                StackedBarGraph(
                                    xAxisScaleData = chartData.xAxis,
                                    allDataLists = chartData.allData,
                                    height = 400.dp,
                                    barWidth = 50.dp,
                                    type = true,
                                    yAxisScaleData = chartData.yAxis
                                )
                            }
                        } ?: CircularProgressIndicator() // Show loading indicator
                    }

                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
        }
    }
}