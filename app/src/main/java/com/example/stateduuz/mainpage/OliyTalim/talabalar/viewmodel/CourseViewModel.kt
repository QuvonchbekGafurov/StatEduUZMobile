package com.example.stateduuz.mainpage.OliyTalim.talabalar.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stateduuz.data.repository.UniversitetRepository
import com.example.stateduuz.model.courseAndAccomodation.courseAndAccomodation
import com.example.stateduuz.model.courseAndAge.courseAndAge
import com.example.stateduuz.model.courseAndGender.courseAndGender
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
@HiltViewModel
class CourseViewModel @Inject constructor(
    private val repository: UniversitetRepository
) : ViewModel() {

    // courseAndGender
    private val _courseAndGender = MutableStateFlow<ChartUiState?>(null)
    val courseAndGender: StateFlow<ChartUiState?> = _courseAndGender.asStateFlow()

    // courseAndAge
    private val _courseAndAge = MutableStateFlow<ChartUiState?>(null)
    val courseAndAge: StateFlow<ChartUiState?> = _courseAndAge.asStateFlow()

    // courseAndAccommodation
    private val _courseAndAccommodation = MutableStateFlow<ChartUiState?>(null)
    val courseAndAccommodation: StateFlow<ChartUiState?> = _courseAndAccommodation.asStateFlow()

    // Generic fetch function
    private fun <T> fetchData(
        fetchFunction: suspend () -> List<T>,
        stateFlow: MutableStateFlow<ChartUiState?>,
        extractCourse: (T) -> String,
        extractName: (T) -> String,
        extractCount: (T) -> Int
    ) {
        viewModelScope.launch {
            try {
                // Fetch data on IO Dispatcher
                val data = withContext(Dispatchers.IO) {
                    fetchFunction()
                }
                data.let { dataList ->
                    // Extract X and Y axis data
                    val xAxisData = dataList.map(extractCourse).distinct()
                    val yAxisData = dataList.map(extractName).distinct()

                    // Group data
                    val groupedData = dataList.groupBy(extractName)
                        .mapValues { (_, values) -> values.associateBy(extractCourse) }

                    // Build allData for the chart
                    val allData = yAxisData.map { name ->
                        xAxisData.map { course ->
                            groupedData[name]?.get(course)?.let(extractCount) ?: 0
                        }
                    }

                    // Calculate top values
                    val topValues = yAxisData.map { name ->
                        val nameData = groupedData[name]
                        nameData?.values?.sumOf(extractCount) ?: 0
                    }

                    // Update StateFlow with ChartUiState
                    stateFlow.value = ChartUiState(
                        xAxis = xAxisData,
                        yAxis = yAxisData,
                        allData = allData,
                        topValues = topValues
                    )
                }
            } catch (e: Exception) {
                Log.e("CourseViewModel", "fetchData: Exception - ${e.message}")
            }
        }
    }

    // Fetch functions for each data type
    fun fetchCourseAndGender() {
        fetchData(
            fetchFunction = { repository.getCourseAndGender() },
            stateFlow = _courseAndGender,
            extractCourse = { it.course },
            extractName = { it.name },
            extractCount = { it.count }
        )
    }

    fun fetchCourseAndAge() {
        fetchData(
            fetchFunction = { repository.getCourseAndAge() },
            stateFlow = _courseAndAge,
            extractCourse = { it.course },
            extractName = { it.name },
            extractCount = { it.count }
        )
    }

    fun fetchCourseAndAccommodation() {
        fetchData(
            fetchFunction = { repository.getCourseAndAccommodation() },
            stateFlow = _courseAndAccommodation,
            extractCourse = { it.course },
            extractName = { it.name },
            extractCount = { it.count }
        )
    }
}