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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
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
import com.example.stateduuz.utils.AnimatedCounter
import com.example.stateduuz.chart.stackedbar.StackedBarGraph
import com.example.stateduuz.mainpage.OliyTalim.talabalar.viewmodel.ChartUiState
import com.example.stateduuz.mainpage.OliyTalim.talabalar.viewmodel.EduFormViewModel
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
    val educationFormAndCitizenship by viewModel.educationFormAndCitizenship.observeAsState()

    // Trigger data fetching once
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

                    // Gender Chart
                    ChartSection(
                        title = "Jins bo'yicha",
                        chartData = educationFormAndGender,
                        type = false
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    // Age Chart
                    ChartSection(
                        title = "Yoshi",
                        chartData = educationFormAndAge,
                        type = false
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    // Citizenship Chart
                    ChartSection(
                        title = "Fuqaroligi",
                        chartData = educationFormAndCitizenship,
                        type = true
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    // Payment Form Chart
                    ChartSection(
                        title = "To'lov Shakli",
                        chartData = educationFormAndPaymentForm,
                        type = true
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    // Course Chart
                    ChartSection(
                        title = "Kurslar",
                        chartData = educationFormAndCourse,
                        type = true
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    // Accommodation Chart
                    ChartSection(
                        title = "Yashash joyi",
                        chartData = educationFormAndAccommodation,
                        type = true
                    )

                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
        }
    }
}
