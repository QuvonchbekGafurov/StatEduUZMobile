package com.example.stateduuz.mainpage.OliyTalim.Teachers

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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.stateduuz.chart.PieChart
import com.example.stateduuz.chart.PieChartInput
import com.example.stateduuz.chart.stackedbar.StackedBarGraph
import com.example.stateduuz.mainpage.OliyTalim.talabalar.screens.LegendItem
import com.example.stateduuz.mainpage.OliyTalim.umumiy.colors
import com.example.stateduuz.ui.theme.ColorBlue
import com.example.stateduuz.ui.theme.ColorDeepPurple
import com.example.stateduuz.ui.theme.ColorGreen
import com.example.stateduuz.ui.theme.ColorLightBlue
import com.example.stateduuz.ui.theme.ColorLightOrange
import com.example.stateduuz.ui.theme.ColorLightYellow
import com.example.stateduuz.ui.theme.ColorYellow
import com.example.stateduuz.utils.AnimatedCounter

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun OqtuvchilarScreen(viewModel: TeachersViewModel = hiltViewModel()) {
    val scrollState = rememberScrollState()
    val genderPieChartData by viewModel.genderPieChartData.collectAsState()
    val chiefPositionPieChartData by viewModel.chiefPositionPieChartData.collectAsState()
    val academicDegreePieChartData by viewModel.academicDegreePieChartData.collectAsState()
    val academicRankBarChartData by viewModel.academicRankBarChartData.collectAsState()
    val positionPieChartData by viewModel.positionPieChartData.collectAsState()
    val employeeFormPieChartData by viewModel.employeeFormPieChartData.collectAsState()
    val citizenshipPieChartData by viewModel.citizenshipPieChartData.collectAsState()
    val ageBarChartData by viewModel.ageBarChartData.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchTeacherStatisticGender()
        viewModel.fetchTeacherStatisticChiefPosition()
        viewModel.fetchTeacherStatisticAcademicDegree()
        viewModel.fetchTeacherStatisticAcademicRank()
        viewModel.fetchTeacherStatisticPosition()
        viewModel.fetchTeacherStatisticEmployeeForm()
        viewModel.fetchTeacherStatisticCitizenship()
        viewModel.fetchTeacherStatisticAge()
    }

    Column(
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .verticalScroll(scrollState)
    ) {
        Spacer(modifier = Modifier.height(10.dp))
        PieChartSection(
            title = "O'qituvchilar (Jins bo'yicha)",
            data = genderPieChartData
        )
        Spacer(modifier = Modifier.height(15.dp))
        PieChartSection(
            title = "Rahbar xodimlar",
            data = chiefPositionPieChartData
        )
        Spacer(modifier = Modifier.height(15.dp))
        PieChartSection(
            title = "O'qituvchilar ilmiy salohiyati bo'yicha",
            data = academicDegreePieChartData
        )
        Spacer(modifier = Modifier.height(15.dp))
        BarChartSection(
            title = "O'qituvchilar ilmiy daraja bo'yicha",
            data = academicRankBarChartData
        )
        Spacer(modifier = Modifier.height(20.dp))
        PieChartSection(
            title = "Lavozim bo'yicha",
            data = positionPieChartData
        )
        Spacer(modifier = Modifier.height(20.dp))
        PieChartSection(
            title = "Mehnat shakli",
            data = employeeFormPieChartData
        )
        Spacer(modifier = Modifier.height(20.dp))
        PieChartSection(
            title = "O'qituvchilar Fuqoroligi bo'yicha",
            data = citizenshipPieChartData
        )
        Spacer(modifier = Modifier.height(20.dp))
        BarChartSection(
            title = "O'qituvchilar yoshi bo'yicha",
            data = ageBarChartData
        )
        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Composable
private fun PieChartSection(title: String, data: TeachersViewModel.PieChartData?) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(MaterialTheme.colorScheme.onSecondary)
            .padding(10.dp)
    ) {
        Text(text = title, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.secondary, fontSize = 20.sp)
        if (data != null && data.totalCount > 0) {
            PieChart(
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
                text = "Ma'lumot yuklanmoqda...",
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun BarChartSection(title: String, data: TeachersViewModel.BarChartData?) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(MaterialTheme.colorScheme.onSecondary)
            .padding(10.dp)
    ) {
        Text(text = title, fontWeight = FontWeight.Bold, fontSize = 20.sp, color = MaterialTheme.colorScheme.secondary)
        Spacer(modifier = Modifier.height(5.dp))
        if (data != null && data.xAxisScale.isNotEmpty() && data.allDataLists.isNotEmpty()) {
            StackedBarGraph(
                xAxisScaleData = data.xAxisScale,
                allDataLists = data.allDataLists,
                height = 400.dp,
                barWidth = 50.dp,
                type = false,
                yAxisScaleData = data.yAxisScale
            )
        } else {
            Text(
                text = "Ma'lumot yuklanmoqda...",
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
    }
}