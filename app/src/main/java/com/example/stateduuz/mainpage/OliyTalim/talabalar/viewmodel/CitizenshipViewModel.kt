package com.example.stateduuz.mainpage.OliyTalim.talabalar.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stateduuz.data.repository.UniversitetRepository
import com.example.stateduuz.model.citizenshipAndAge.citizenshipAndAge
import com.example.stateduuz.model.citizenshipAndCourse.citizenshipAndCourse
import com.example.stateduuz.model.citizenshipAndGender.citizenshipAndGender
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
@HiltViewModel
class CitizenshipViewModel @Inject constructor(
    private val repository: UniversitetRepository
) : ViewModel() {

    private val _genderChartState = MutableStateFlow<ChartUiState?>(null)
    val genderChartState: StateFlow<ChartUiState?> = _genderChartState.asStateFlow()

    private val _ageChartState = MutableStateFlow<ChartUiState?>(null)
    val ageChartState: StateFlow<ChartUiState?> = _ageChartState.asStateFlow()

    private val _courseChartState = MutableStateFlow<ChartUiState?>(null)
    val courseChartState: StateFlow<ChartUiState?> = _courseChartState.asStateFlow()

    init {
        fetchAll()
    }

    fun fetchAll() {
        fetchCitizenshipAndGender()
        fetchCitizenshipAndAge()
        fetchCitizenshipAndCourse()
    }

    fun fetchCitizenshipAndGender() {
        viewModelScope.launch {
            val data = repository.getCitizenshipAndGender()
            val xAxis = data.map { it.citizenship }.distinct()
            val yAxis = data.map { it.name }.distinct()
            val grouped = data.groupBy { it.name }.mapValues { (_, v) -> v.associateBy { it.citizenship } }

            val allData = yAxis.map { y ->
                xAxis.map { x -> grouped[y]?.get(x)?.count ?: 0 }
            }

            val topValues = yAxis.map { grouped[it]?.values?.sumOf { it.count } ?: 0 }

            _genderChartState.value = ChartUiState(xAxis, yAxis, allData, topValues)
        }
    }

    fun fetchCitizenshipAndAge() {
        viewModelScope.launch {
            val data = repository.getCitizenshipAndAge()
            val xAxis = data.map { it.citizenship }.distinct()
            val yAxis = data.map { it.name }.distinct()
            val grouped = data.groupBy { it.name }.mapValues { (_, v) -> v.associateBy { it.citizenship } }

            val allData = yAxis.map { y ->
                xAxis.map { x -> grouped[y]?.get(x)?.count ?: 0 }
            }

            val topValues = yAxis.map { grouped[it]?.values?.sumOf { it.count } ?: 0 }

            _ageChartState.value = ChartUiState(xAxis, yAxis, allData, topValues)
        }
    }

    fun fetchCitizenshipAndCourse() {
        viewModelScope.launch {
            val data = repository.getCitizenshipAndCourse()
            val xAxis = data.map { it.name }.distinct()
            val yAxis = data.map { it.citizenship }.distinct()
            val grouped = data.groupBy { it.citizenship }.mapValues { (_, v) -> v.associateBy { it.name } }

            val allData = yAxis.map { y ->
                xAxis.map { x -> grouped[y]?.get(x)?.count ?: 0 }
            }

            val topValues = yAxis.map { grouped[it]?.values?.sumOf { it.count } ?: 0 }

            _courseChartState.value = ChartUiState(xAxis, yAxis, allData, topValues)
        }
    }
}


data class ChartUiState(
    val xAxis: List<String> = emptyList(),
    val yAxis: List<String> = emptyList(),
    val allData: List<List<Int>> = emptyList(),
    val topValues: List<Int> = emptyList()
)

