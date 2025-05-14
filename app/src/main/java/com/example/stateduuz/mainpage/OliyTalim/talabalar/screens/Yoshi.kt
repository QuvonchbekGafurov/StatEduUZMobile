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
import com.example.stateduuz.mainpage.OliyTalim.talabalar.viewmodel.AgeViewModel
import com.example.stateduuz.ui.theme.ColorDeepPurple
import com.example.stateduuz.ui.theme.ColorGreen
import com.example.stateduuz.ui.theme.ColorLightBlue
import com.example.stateduuz.ui.theme.ColorLightOrange
import com.example.stateduuz.ui.theme.ColorLightYellow

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun YoshiScreen(viewModel: AgeViewModel = hiltViewModel()) {
    val ageAndGender by viewModel.ageAndGender.collectAsState()
    val ageAndAccommodation by viewModel.ageAndAccommodation.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchAgeAndGender()
        viewModel.fetchAgeAndAccommodation()
    }

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
                    chartData = ageAndGender,
                    type = false
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Accommodation Chart
                ChartSection(
                    title = "Yashash joyi",
                    chartData = ageAndAccommodation,
                    type = false
                )

                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }

}
