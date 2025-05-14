package com.example.stateduuz.mainpage.OliyTalim.talabalar.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stateduuz.data.repository.UniversitetRepository
import com.example.stateduuz.model.regionAndEduForm.regionAndEduForm
import com.example.stateduuz.model.regionAndEduTpe.regionAndEduType
import com.example.stateduuz.model.regionAndGender.regionAndGender
import com.example.stateduuz.model.regionAndGender.regionAndGenderItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Thread.State
import javax.inject.Inject

@HiltViewModel
class RegionViewModel @Inject constructor(
    private val repository: UniversitetRepository
) : ViewModel() {

    // regionAndGender
    private val _regionAndGender = MutableStateFlow<ChartUiState?>(null)
    val regionAndGender: StateFlow<ChartUiState?> = _regionAndGender.asStateFlow()

    // regionAndEduType
    private val _regionAndEduType = MutableStateFlow<ChartUiState?>(null)
    val regionAndEduType: StateFlow<ChartUiState?> = _regionAndEduType.asStateFlow()

    // regionAndEduForm
    private val _regionAndEduForm = MutableStateFlow<ChartUiState?>(null)
    val regionAndEduForm: StateFlow<ChartUiState?> = _regionAndEduForm.asStateFlow()

    // regionAndCourse
    private val _regionAndCourse = MutableStateFlow<ChartUiState?>(null)
    val regionAndCourse: StateFlow<ChartUiState?> = _regionAndCourse.asStateFlow()

    // regionAndAge
    private val _regionAndAge = MutableStateFlow<ChartUiState?>(null)
    val regionAndAge: StateFlow<ChartUiState?> = _regionAndAge.asStateFlow()

    // regionAndPaymentType
    private val _regionAndPaymentType = MutableStateFlow<ChartUiState?>(null)
    val regionAndPaymentType: StateFlow<ChartUiState?> = _regionAndPaymentType.asStateFlow()

    // regionAndCitizenship
    private val _regionAndCitizenship = MutableStateFlow<ChartUiState?>(null)
    val regionAndCitizenship: StateFlow<ChartUiState?> = _regionAndCitizenship.asStateFlow()

    // regionAndAccommodation
    private val _regionAndAccommodation = MutableStateFlow<ChartUiState?>(null)
    val regionAndAccommodation: StateFlow<ChartUiState?> = _regionAndAccommodation.asStateFlow()

    // Umumiy fetch funksiyasi
    private fun <T> fetchData(
        fetchFunction: suspend () -> List<T>,
        stateFlow: MutableStateFlow<ChartUiState?>,
        extractRegion: (T) -> String,
        extractName: (T) -> String,
        extractCount: (T) -> Int
    ) {
        viewModelScope.launch {
            try {
                val data = withContext(Dispatchers.IO) {
                    fetchFunction()
                }
                data.let { dataList ->
                    // X va Y o'q ma'lumotlarini olish
                    val xAxisData = dataList.map(extractRegion).distinct()
                    val yAxisData = dataList.map(extractName).distinct()

                    // Ma'lumotlarni guruhlash
                    val groupedData = dataList.groupBy(extractName)
                        .mapValues { (_, values) -> values.associateBy(extractRegion) }

                    // AllData ni tashkil etish
                    val allData = yAxisData.map { name ->
                        xAxisData.map { region ->
                            groupedData[name]?.get(region)?.let(extractCount) ?: 0
                        }
                    }

                    // Top values ni hisoblash
                    val topValues = yAxisData.map { name ->
                        val nameData = groupedData[name]
                        nameData?.values?.sumOf(extractCount) ?: 0
                    }

                    // ChartUiState ni yangilash
                    stateFlow.value = ChartUiState(
                        xAxis = xAxisData,
                        yAxis = yAxisData,
                        allData = allData,
                        topValues = topValues
                    )
                }
            } catch (e: Exception) {
                Log.e("RegionViewModel", "fetchData: Exception - ${e.message}")
            }
        }
    }

    // Har bir ma'lumot turi uchun fetch funksiyalari
    fun fetchRegionAndGender() {
        fetchData(
            fetchFunction = { repository.getRegionAndGender() },
            stateFlow = _regionAndGender,
            extractRegion = { it.region },
            extractName = { it.name },
            extractCount = { it.count }
        )
    }

    fun fetchRegionAndEduType() {
        fetchData(
            fetchFunction = { repository.getRegionAndEduType() },
            stateFlow = _regionAndEduType,
            extractRegion = { it.region },
            extractName = { it.name },
            extractCount = { it.count }
        )
    }

    fun fetchRegionAndEduForm() {
        fetchData(
            fetchFunction = { repository.getRegionAndEduForm() },
            stateFlow = _regionAndEduForm,
            extractRegion = { it.region },
            extractName = { it.name },
            extractCount = { it.count }
        )
    }

    fun fetchRegionAndCourse() {
        fetchData(
            fetchFunction = { repository.getRegionAndCourse() },
            stateFlow = _regionAndCourse,
            extractRegion = { it.region },
            extractName = { it.name },
            extractCount = { it.count }
        )
    }

    fun fetchRegionAndAge() {
        fetchData(
            fetchFunction = { repository.getRegionAndAge() },
            stateFlow = _regionAndAge,
            extractRegion = { it.region },
            extractName = { it.name },
            extractCount = { it.count }
        )
    }

    fun fetchRegionAndPaymentType() {
        fetchData(
            fetchFunction = { repository.getRegionAndPaymentType() },
            stateFlow = _regionAndPaymentType,
            extractRegion = { it.region },
            extractName = { it.name },
            extractCount = { it.count }
        )
    }

    fun fetchRegionAndCitizenship() {
        fetchData(
            fetchFunction = { repository.getRegionAndCitizenship() },
            stateFlow = _regionAndCitizenship,
            extractRegion = { it.region },
            extractName = { it.name },
            extractCount = { it.count }
        )
    }

    fun fetchRegionAndAccommodation() {
        fetchData(
            fetchFunction = { repository.getRegionAndAccommodation() },
            stateFlow = _regionAndAccommodation,
            extractRegion = { it.region },
            extractName = { it.name },
            extractCount = { it.count }
        )
    }
}
