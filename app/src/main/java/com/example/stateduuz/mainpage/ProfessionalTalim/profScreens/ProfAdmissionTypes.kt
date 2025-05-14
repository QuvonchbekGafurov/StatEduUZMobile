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
import com.example.stateduuz.mainpage.ProfessionalTalim.OquvchilarScreenCard1
import com.example.stateduuz.mainpage.ProfessionalTalim.OquvchilarViewModel
import com.example.stateduuz.model.Professional.profAdmissionType.ProfAdmissionType
@Composable
fun ProfAdmissionType(viewModel: OquvchilarViewModel = hiltViewModel()) {
    val scrollState = rememberScrollState()
    val genderChartData by viewModel.admGenderChartData.collectAsState()
    val ageChartData by viewModel.admAgeChartData.collectAsState()
    val livePlaceChartData by viewModel.admLivePlaceChartData.collectAsState()
    val courseChartData by viewModel.admCourseChartData.collectAsState()
    val citizenshipChartData by viewModel.admCitizenshipChartData.collectAsState()

    LaunchedEffect(Unit) { viewModel.fetchAdmissionTypes() }

    Column(
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .verticalScroll(scrollState)
    ) {
        Spacer(modifier = Modifier.height(10.dp))
        genderChartData?.let { data ->
            CustomHorizontalBarChart(
                title = "Jinsi bo'yicha",
                xAxisScaleData = data.xAxisScale,
                yAxisScaleData = data.yAxisScale,
                allDataList = data.allDataList,
                type = data.type
            )
            Spacer(modifier = Modifier.height(10.dp))
        }
        ageChartData?.let { data ->
            CustomHorizontalBarChart(
                title = "Yoshi",
                xAxisScaleData = data.xAxisScale,
                yAxisScaleData = data.yAxisScale,
                allDataList = data.allDataList,
                type = data.type
            )
            Spacer(modifier = Modifier.height(10.dp))
        }
        courseChartData?.let { data ->
            OquvchilarScreenCard1(
                xAxisScaleData = data.xAxisScale,
                allDataLists = data.allDataList,
                yAxisScaleData = data.yAxisScale,
                topValue = data.topValues,
                list = data.yAxisScale,
                title = "Kurslar bo'yicha"
            )
            Spacer(modifier = Modifier.height(10.dp))
        }
        citizenshipChartData?.let { data ->
            OquvchilarScreenCard1(
                xAxisScaleData = data.xAxisScale,
                allDataLists = data.allDataList,
                yAxisScaleData = data.yAxisScale,
                topValue = data.topValues,
                list = data.yAxisScale,
                title = "Fuqoroligi bo'yicha"
            )
            Spacer(modifier = Modifier.height(10.dp))
        }
        livePlaceChartData?.let { data ->
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
