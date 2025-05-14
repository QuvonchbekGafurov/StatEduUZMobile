package com.example.stateduuz.mainpage.OliyTalim.talabalar.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stateduuz.data.repository.UniversitetRepository
import com.example.stateduuz.model.ageAndAccomodation.ageAndAccomodation
import com.example.stateduuz.model.ageAndGender.ageAndGender
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
@HiltViewModel
class AgeViewModel @Inject constructor(
    private val repository: UniversitetRepository
) : ViewModel() {

    private val _ageAndGender = MutableStateFlow<ChartUiState?>(null)
    val ageAndGender: StateFlow<ChartUiState?> = _ageAndGender.asStateFlow()

    private val _ageAndAccommodation = MutableStateFlow<ChartUiState?>(null)
    val ageAndAccommodation: StateFlow<ChartUiState?> = _ageAndAccommodation.asStateFlow()

    // Generic fetch function for ChartUiState
    private fun <T> fetchChartData(
        fetchFunction: suspend () -> List<T>,
        stateFlow: MutableStateFlow<ChartUiState?>,
        extractAge: (T) -> String,
        extractName: (T) -> String,
        extractCount: (T) -> Int
    ) {
        viewModelScope.launch {
            try {
                val data = withContext(Dispatchers.IO) {
                    fetchFunction()
                }
                withContext(Dispatchers.Default) {
                    val xAxisData = data.map(extractAge).distinct()
                    val yAxisData = data.map(extractName).distinct()
                    val groupedData = data.groupBy(extractName)
                        .mapValues { (_, values) -> values.associateBy(extractAge) }
                    val allData = yAxisData.map { name ->
                        xAxisData.map { age ->
                            groupedData[name]?.get(age)?.let(extractCount) ?: 0
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
                Log.e("AgeViewModel", "fetchChartData: Exception - ${e.message}")
                e.printStackTrace()
            }
        }
    }

    fun fetchAgeAndGender() {
        fetchChartData(
            fetchFunction = { repository.getAgeAndGender() },
            stateFlow = _ageAndGender,
            extractAge = { it.age },
            extractName = { it.name },
            extractCount = { it.count }
        )
    }

    fun fetchAgeAndAccommodation() {
        fetchChartData(
            fetchFunction = { repository.getAgeAndAccommodation() },
            stateFlow = _ageAndAccommodation,
            extractAge = { it.age },
            extractName = { it.name },
            extractCount = { it.count }
        )
    }
}
