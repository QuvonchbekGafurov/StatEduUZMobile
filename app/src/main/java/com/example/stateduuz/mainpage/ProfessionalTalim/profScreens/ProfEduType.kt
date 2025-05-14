package com.example.stateduuz.mainpage.ProfessionalTalim.profScreens

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import com.example.stateduuz.mainpage.ProfessionalTalim.RowChartItem
import com.example.stateduuz.mainpage.ProfessionalTalim.RowChartModel
import com.example.stateduuz.model.Professional.profEduType.ProfEduType
import com.example.stateduuz.ui.theme.BackgroundBlur
import com.example.stateduuz.ui.theme.ColorGreenMain
import com.example.stateduuz.ui.theme.ColorLightRed
import com.example.stateduuz.utils.CardUtils


@Composable
fun ProfEduTypeScreen(viewModel: OquvchilarViewModel = hiltViewModel()) {
    val scrollState = rememberScrollState()
    val genderCardData by viewModel.eduTypeGenderCardData.collectAsState()
    val ageChartData by viewModel.eduTypeAgeChartData.collectAsState()
    val ptChartData by viewModel.eduTypePTChartData.collectAsState()
    val courseChartData by viewModel.eduTypeCourseChartData.collectAsState()
    val citizenshipChartData by viewModel.eduTypeCitizenshipChartData.collectAsState()
    val etChartData by viewModel.eduTypeETChartData.collectAsState()
    val livePlaceChartData by viewModel.eduTypeLivePlaceChartData.collectAsState()

    LaunchedEffect(Unit) { viewModel.fetchEducationTypes() }

    Column(
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .verticalScroll(scrollState)
    ) {
        Spacer(modifier = Modifier.height(10.dp))
        genderCardData?.let { data ->
            Column(modifier = Modifier.padding(top = 20.dp)) {
                Row {
                    CardUtils(
                        maintext = "Kollejlar",
                        modifier = Modifier.weight(1f),
                        lists = data.college,
                        listsname = listOf("Erkaklar", "Ayollar")
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    CardUtils(
                        maintext = "Texnikumlar",
                        modifier = Modifier.weight(1f),
                        lists = data.technicum,
                        listsname = listOf("Erkaklar", "Ayollar")
                    )
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
        }
        ageChartData?.let { data ->
            OquvchilarScreenCard1(
                xAxisScaleData = data.xAxisScale,
                allDataLists = data.allDataList,
                yAxisScaleData = data.yAxisScale,
                topValue = data.topValues,
                list = data.yAxisScale,
                title = "Yoshi"
            )
            Spacer(modifier = Modifier.height(10.dp))
        }
        ptChartData?.let { data ->
            OquvchilarScreenCard1(
                xAxisScaleData = data.xAxisScale,
                allDataLists = data.allDataList,
                yAxisScaleData = data.yAxisScale,
                topValue = data.topValues,
                list = data.yAxisScale,
                title = "To'lov Shakli"
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
                title = "Kurslar"
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
                title = "Fuqoroligi"
            )
            Spacer(modifier = Modifier.height(10.dp))
        }
        livePlaceChartData?.let { data ->
            if (data.allDataList.size >= 3) {
                CustomHorizontalBarChart(
                    type = data.type,
                    xAxisScaleData = data.xAxisScale,
                    yAxisScaleData = data.yAxisScale,
                    allDataList = data.allDataList
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
        }
        etChartData?.let { data ->
            OquvchilarScreenCard1(
                xAxisScaleData = data.xAxisScale,
                allDataLists = data.allDataList,
                yAxisScaleData = data.yAxisScale,
                topValue = data.topValues,
                list = data.yAxisScale,
                title = "Ta'lim shakli"
            )
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}