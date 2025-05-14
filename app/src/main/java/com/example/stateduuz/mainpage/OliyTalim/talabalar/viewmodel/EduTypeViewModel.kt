package com.example.stateduuz.mainpage.OliyTalim.talabalar.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stateduuz.data.repository.UniversitetRepository
import com.example.stateduuz.model.eduTypeAndAccommodation.eduTypeAndAccommodation
import com.example.stateduuz.model.eduTypeAndAge.eduTypeAndAge
import com.example.stateduuz.model.eduTypeAndCitizenship.eduTypeAndCitizenship
import com.example.stateduuz.model.eduTypeAndCourse.eduTypeAndCourse
import com.example.stateduuz.model.eduTypeAndEduForm.eduTypeAndEduForm
import com.example.stateduuz.model.eduTypeAndGander.eduTypeAndGender
import com.example.stateduuz.model.eduTypeAndPaymentType.eduTypeAndPaymentType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
@HiltViewModel
class EduTypeViewModel @Inject constructor(
    private val repository: UniversitetRepository
) : ViewModel() {

    private val _eduTypeAndGender = MutableStateFlow<List<GenderCardData>?>(null)
    val eduTypeAndGender: StateFlow<List<GenderCardData>?> = _eduTypeAndGender.asStateFlow()

    private val _eduTypeAndAge = MutableStateFlow<ChartUiState?>(null)
    val eduTypeAndAge: StateFlow<ChartUiState?> = _eduTypeAndAge.asStateFlow()

    private val _eduTypeAndPaymentType = MutableStateFlow<ChartUiState?>(null)
    val eduTypeAndPaymentType: StateFlow<ChartUiState?> = _eduTypeAndPaymentType.asStateFlow()

    private val _eduTypeAndCourse = MutableStateFlow<ChartUiState?>(null)
    val eduTypeAndCourse: StateFlow<ChartUiState?> = _eduTypeAndCourse.asStateFlow()

    private val _eduTypeAndCourseBakalavr = MutableStateFlow<BarGraphData?>(null)
    val eduTypeAndCourseBakalavr: StateFlow<BarGraphData?> = _eduTypeAndCourseBakalavr.asStateFlow()

    private val _eduTypeAndCourseMagistr = MutableStateFlow<BarGraphData?>(null)
    val eduTypeAndCourseMagistr: StateFlow<BarGraphData?> = _eduTypeAndCourseMagistr.asStateFlow()

    private val _eduTypeAndCitizenship = MutableStateFlow<ChartUiState?>(null)
    val eduTypeAndCitizenship: StateFlow<ChartUiState?> = _eduTypeAndCitizenship.asStateFlow()

    private val _eduTypeAndAccommodation = MutableStateFlow<ChartUiState?>(null)
    val eduTypeAndAccommodation: StateFlow<ChartUiState?> = _eduTypeAndAccommodation.asStateFlow()

    private val _eduTypeAndEduForm = MutableStateFlow<ChartUiState?>(null)
    val eduTypeAndEduForm: StateFlow<ChartUiState?> = _eduTypeAndEduForm.asStateFlow()

    private val _eduTypeAndEduFormBakalavr = MutableStateFlow<BarGraphData?>(null)
    val eduTypeAndEduFormBakalavr: StateFlow<BarGraphData?> = _eduTypeAndEduFormBakalavr.asStateFlow()

    private val _eduTypeAndEduFormMagistr = MutableStateFlow<BarGraphData?>(null)
    val eduTypeAndEduFormMagistr: StateFlow<BarGraphData?> = _eduTypeAndEduFormMagistr.asStateFlow()

    // Generic fetch function for ChartUiState
    private fun <T> fetchChartData(
        fetchFunction: suspend () -> List<T>,
        stateFlow: MutableStateFlow<ChartUiState?>,
        extractEduType: (T) -> String,
        extractName: (T) -> String,
        extractCount: (T) -> Int
    ) {
        viewModelScope.launch {
            try {
                val data = withContext(Dispatchers.IO) {
                    fetchFunction()
                }
                withContext(Dispatchers.Default) {
                    val xAxisData = data.map(extractEduType).distinct()
                    val yAxisData = data.map(extractName).distinct()
                    val groupedData = data.groupBy(extractName)
                        .mapValues { (_, values) -> values.associateBy(extractEduType) }
                    val allData = yAxisData.map { name ->
                        xAxisData.map { eduType ->
                            groupedData[name]?.get(eduType)?.let(extractCount) ?: 0
                        }
                    }
                    val topValues = yAxisData.map { name ->
                        groupedData[name]?.values?.sumOf(extractCount) ?: 0
                    }
                    stateFlow.value = ChartUiState(
                        xAxis = xAxisData,
                        yAxis = yAxisData,
                        allData = allData,
                        topValues = topValues
                    )
                }
            } catch (e: Exception) {
                Log.e("EduTypeViewModel", "fetchChartData: Exception - ${e.message}")
                e.printStackTrace()
            }
        }
    }

    // Generic fetch function for BarGraphData
    private fun <T> fetchBarGraphData(
        fetchFunction: suspend () -> List<T>,
        stateFlow: MutableStateFlow<BarGraphData?>,
        eduType: String,
        extractEduType: (T) -> String,
        extractName: (T) -> String,
        extractCount: (T) -> Int
    ) {
        viewModelScope.launch {
            try {
                val data = withContext(Dispatchers.IO) {
                    fetchFunction()
                }
                withContext(Dispatchers.Default) {
                    val filteredList = data.filter { extractEduType(it) == eduType }
                    val counts = filteredList.map(extractCount)
                    val labels = filteredList.map(extractName).distinct()
                    val maxCount = counts.maxOrNull() ?: 1
                    val normalizedValues = counts.map { it.toFloat() / maxCount.toFloat() }
                    stateFlow.value = BarGraphData(
                        counts = counts,
                        labels = labels,
                        normalizedValues = normalizedValues
                    )
                }
            } catch (e: Exception) {
                Log.e("EduTypeViewModel", "fetchBarGraphData: Exception - ${e.message}")
                e.printStackTrace()
            }
        }
    }

    // Fetch Gender data for CardUtils
    fun fetchEduTypeAndGender() {
        viewModelScope.launch {
            try {
                val data = withContext(Dispatchers.IO) {
                    repository.getEduTypeAndGender() // Should return List<GenderData>
                }
                withContext(Dispatchers.Default) {
                    val groupedData = data.groupBy { it.eduType }
                    val cardData = groupedData.entries.map { (eduType, items) ->
                        GenderCardData(
                            eduType = eduType,
                            maleCount = items.find { it.name == "Erkak" }?.count ?: 0,
                            femaleCount = items.find { it.name == "Ayol" }?.count ?: 0
                        )
                    }.sortedBy { it.eduType }
                    _eduTypeAndGender.value = cardData
                }
            } catch (e: Exception) {
                Log.e("EduTypeViewModel", "fetchEduTypeAndGender: Exception - ${e.message}")
                e.printStackTrace()
            }
        }
    }

    // Fetch Age data
    fun fetchEduTypeAndAge() {
        fetchChartData(
            fetchFunction = { repository.getEduTypeAndAge() }, // Should return List<ChartDataItem>
            stateFlow = _eduTypeAndAge,
            extractEduType = { it.eduType },
            extractName = { it.name },
            extractCount = { it.count }
        )
    }

    // Fetch Payment Type data
    fun fetchEduTypeAndPaymentType() {
        fetchChartData(
            fetchFunction = { repository.getEduTypeAndPaymentType() }, // Should return List<ChartDataItem>
            stateFlow = _eduTypeAndPaymentType,
            extractEduType = { it.eduType },
            extractName = { it.name },
            extractCount = { it.count }
        )
    }

    // Fetch Course data
    fun fetchEduTypeAndCourse() {
        fetchChartData(
            fetchFunction = { repository.getEduTypeAndCourse() }, // Should return List<ChartDataItem>
            stateFlow = _eduTypeAndCourse,
            extractEduType = { it.eduType },
            extractName = { it.name },
            extractCount = { it.count }
        )
        fetchBarGraphData(
            fetchFunction = { repository.getEduTypeAndCourse() }, // Should return List<ChartDataItem>
            stateFlow = _eduTypeAndCourseBakalavr,
            eduType = "Bakalavr",
            extractEduType = { it.eduType },
            extractName = { it.name },
            extractCount = { it.count }
        )
        fetchBarGraphData(
            fetchFunction = { repository.getEduTypeAndCourse() }, // Should return List<ChartDataItem>
            stateFlow = _eduTypeAndCourseMagistr,
            eduType = "Magistr",
            extractEduType = { it.eduType },
            extractName = { it.name },
            extractCount = { it.count }
        )
    }

    // Fetch Citizenship data
    fun fetchEduTypeAndCitizenship() {
        fetchChartData(
            fetchFunction = { repository.getEduTypeAndCitizenship() }, // Should return List<ChartDataItem>
            stateFlow = _eduTypeAndCitizenship,
            extractEduType = { it.eduType },
            extractName = { it.name },
            extractCount = { it.count }
        )
    }

    // Fetch Accommodation data
    fun fetchEduTypeAndAccommodation() {
        fetchChartData(
            fetchFunction = { repository.getEduTypeAndAccommodation() }, // Should return List<ChartDataItem>
            stateFlow = _eduTypeAndAccommodation,
            extractEduType = { it.eduType },
            extractName = { it.name },
            extractCount = { it.count }
        )
    }

    // Fetch Education Form data
    fun fetchEduTypeAndEduForm() {
        fetchChartData(
            fetchFunction = { repository.getEduTypeAndEduForm() }, // Should return List<ChartDataItem>
            stateFlow = _eduTypeAndEduForm,
            extractEduType = { it.eduType },
            extractName = { it.name },
            extractCount = { it.count }
        )
        fetchBarGraphData(
            fetchFunction = { repository.getEduTypeAndEduForm() }, // Should return List<ChartDataItem>
            stateFlow = _eduTypeAndEduFormBakalavr,
            eduType = "Bakalavr",
            extractEduType = { it.eduType },
            extractName = { it.name },
            extractCount = { it.count }
        )
        fetchBarGraphData(
            fetchFunction = { repository.getEduTypeAndEduForm() }, // Should return List<ChartDataItem>
            stateFlow = _eduTypeAndEduFormMagistr,
            eduType = "Magistr",
            extractEduType = { it.eduType },
            extractName = { it.name },
            extractCount = { it.count }
        )
    }
}

data class GenderCardData(
    val eduType: String,
    val maleCount: Int,
    val femaleCount: Int
)

data class BarGraphData(
    val counts: List<Int>,
    val labels: List<String>,
    val normalizedValues: List<Float>
)


// Data class for repository response
data class ChartDataItem(
    val eduType: String,
    val name: String,
    val count: Int
)

data class GenderData(
    val eduType: String,
    val name: String,
    val count: Int
)