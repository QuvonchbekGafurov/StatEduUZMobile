package com.example.stateduuz.mainpage.ProfessionalTalim

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.MaterialTheme
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
import com.example.stateduuz.FilterScreenManager
import com.example.stateduuz.chart.stackedbar.StackedBarGraph
import com.example.stateduuz.mainpage.OliyTalim.talabalar.TalabalarManager
import com.example.stateduuz.mainpage.OliyTalim.talabalar.screens.FuqaroligiScreen
import com.example.stateduuz.mainpage.OliyTalim.talabalar.screens.HududlarKesimidaScreen
import com.example.stateduuz.mainpage.OliyTalim.talabalar.screens.KurslarScreen
import com.example.stateduuz.mainpage.OliyTalim.talabalar.screens.TalimShakliScreen
import com.example.stateduuz.mainpage.OliyTalim.talabalar.screens.TalimTuriScreen
import com.example.stateduuz.mainpage.OliyTalim.talabalar.screens.TolovShakliScreen
import com.example.stateduuz.mainpage.OliyTalim.talabalar.screens.YashashJoyiScreen
import com.example.stateduuz.mainpage.OliyTalim.talabalar.screens.YoshiScreen
import com.example.stateduuz.mainpage.ProfessionalTalim.profScreens.ProfAdmissionType
import com.example.stateduuz.mainpage.ProfessionalTalim.profScreens.ProfAgeScreen
import com.example.stateduuz.mainpage.ProfessionalTalim.profScreens.ProfCitizenship
import com.example.stateduuz.mainpage.ProfessionalTalim.profScreens.ProfCourse
import com.example.stateduuz.mainpage.ProfessionalTalim.profScreens.ProfCurrentLiveScreen
import com.example.stateduuz.mainpage.ProfessionalTalim.profScreens.ProfEduFormScreen
import com.example.stateduuz.mainpage.ProfessionalTalim.profScreens.ProfEduTypeScreen
import com.example.stateduuz.mainpage.ProfessionalTalim.profScreens.ProfRegionScreen
import com.example.stateduuz.model.Professional.profEduType.ProfEduType
import com.example.stateduuz.model.Professional.region.ProfRegion
import com.example.stateduuz.utils.AnimatedCounter

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun OquvchilarScreen() {
    FilterScreenManager.selectedTabScreen="Prof Screen"

    when(OquvchilarManager.selectedTabScreen){
        "Ta’lim turi" -> {
            ProfEduTypeScreen()
        }
        "Ta’lim shakli" -> {
            ProfEduFormScreen()
        }
        "To‘lov shakli" -> {
            ProfAdmissionType()
        }
        "Fuqaroligi" -> {
            ProfCitizenship()
        }
        "Kurslar" -> {
            ProfCourse()
        }
        "Yoshi" -> {
            ProfAgeScreen()
        }
        "Yashash hududi" -> {
            ProfCurrentLiveScreen()
        }
        "Hududlar kesimida" -> {
            ProfRegionScreen()
        }
    }
}
object OquvchilarManager {
    var selectedTabScreen by mutableStateOf("Ta’lim turi")
}

@Composable
fun OquvchilarScreenCard1(
    xAxisScaleData: List<String>,
    allDataLists: List<List<Int>>,
    yAxisScaleData: List<String>,
    topValue: List<Int>,
    list: List<String>,
    title: String = "",
    type:Boolean=true
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(MaterialTheme.colorScheme.onSecondary)
            .padding(10.dp)
    )
    {
        Text(text = title, fontWeight = FontWeight.Bold, fontSize = 20.sp, color = MaterialTheme.colorScheme.secondary)
        Spacer(modifier = Modifier.height(5.dp))

        val colors = listOf(Color.Blue, Color.Red) // Ranglarni o'zgartirish mumkin
        Spacer(modifier = Modifier.height(10.dp))
        if (xAxisScaleData.isNotEmpty() && allDataLists.isNotEmpty()) {

            StackedBarGraph(
                xAxisScaleData = xAxisScaleData,
                allDataLists = allDataLists,
                height = 400.dp,
                barWidth = 50.dp,
                type = type,
                yAxisScaleData = list
            )
        }
    }
}