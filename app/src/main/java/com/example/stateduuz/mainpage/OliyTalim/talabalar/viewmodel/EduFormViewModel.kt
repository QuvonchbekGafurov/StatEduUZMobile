package com.example.stateduuz.mainpage.OliyTalim.talabalar.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stateduuz.data.repository.UniversitetRepository
import com.example.stateduuz.model.educationFormAndAccommodation.educationFormAndAccommodation
import com.example.stateduuz.model.educationFormAndAge.educationFormAndAge
import com.example.stateduuz.model.educationFormAndCitizenship.educationFormAndCitizenship
import com.example.stateduuz.model.educationFormAndCourse.educationFormAndCourse
import com.example.stateduuz.model.educationFormAndGender.educationFormAndGender
import com.example.stateduuz.model.educationFormAndPaymentForm.educationFormAndPaymentForm
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
@HiltViewModel
class EduFormViewModel @Inject constructor(
    private val repository: UniversitetRepository
) : ViewModel() {

    private val _educationFormAndGender = MutableLiveData<ChartUiState?>()
    val educationFormAndGender: LiveData<ChartUiState?> get() = _educationFormAndGender

    private val _educationFormAndCitizenship = MutableLiveData<ChartUiState?>()
    val educationFormAndCitizenship: LiveData<ChartUiState?> get() = _educationFormAndCitizenship

    private val _educationFormAndAge = MutableLiveData<ChartUiState?>()
    val educationFormAndAge: LiveData<ChartUiState?> get() = _educationFormAndAge

    private val _educationFormAndPaymentForm = MutableLiveData<ChartUiState?>()
    val educationFormAndPaymentForm: LiveData<ChartUiState?> get() = _educationFormAndPaymentForm

    private val _educationFormAndCourse = MutableLiveData<ChartUiState?>()
    val educationFormAndCourse: LiveData<ChartUiState?> get() = _educationFormAndCourse

    private val _educationFormAndAccommodation = MutableLiveData<ChartUiState?>()
    val educationFormAndAccommodation: LiveData<ChartUiState?> get() = _educationFormAndAccommodation

    // Generic fetch function
    private fun <T> fetchData(
        fetchFunction: suspend () -> List<T>,
        liveData: MutableLiveData<ChartUiState?>,
        extractEduForm: (T) -> String,
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
                    val xAxisData = dataList.map(extractEduForm).distinct()
                    val yAxisData = dataList.map(extractName).distinct()

                    // Group data
                    val groupedData = dataList.groupBy(extractName)
                        .mapValues { (_, values) -> values.associateBy(extractEduForm) }

                    // Build allData for the chart
                    val allData = yAxisData.map { name ->
                        xAxisData.map { eduForm ->
                            groupedData[name]?.get(eduForm)?.let(extractCount) ?: 0
                        }
                    }

                    // Calculate top values
                    val topValues = yAxisData.map { name ->
                        val nameData = groupedData[name]
                        nameData?.values?.sumOf(extractCount) ?: 0
                    }

                    // Update LiveData with ChartUiState
                    liveData.value = ChartUiState(
                        xAxis = xAxisData,
                        yAxis = yAxisData,
                        allData = allData,
                        topValues = topValues
                    )
                }
            } catch (e: Exception) {
                Log.e("EduFormViewModel", "fetchData: Exception - ${e.message}")
                e.printStackTrace()
            }
        }
    }

    // Fetch functions for each data type
    fun fetchEducationFormAndGender() {
        fetchData(
            fetchFunction = { repository.getEducationFormAndGender() },
            liveData = _educationFormAndGender,
            extractEduForm = { it.eduForm },
            extractName = { it.name },
            extractCount = { it.count }
        )
    }

    fun fetchEducationFormAndAge() {
        fetchData(
            fetchFunction = { repository.getEducationFormAndAge() },
            liveData = _educationFormAndAge,
            extractEduForm = { it.eduForm },
            extractName = { it.name },
            extractCount = { it.count }
        )
    }

    fun fetchEducationFormAndPaymentForm() {
        fetchData(
            fetchFunction = { repository.getEducationFormAndPaymentForm() },
            liveData = _educationFormAndPaymentForm,
            extractEduForm = { it.eduForm },
            extractName = { it.name },
            extractCount = { it.count }
        )
    }

    fun fetchEducationFormAndCourse() {
        fetchData(
            fetchFunction = { repository.getEducationFormAndCourse() },
            liveData = _educationFormAndCourse,
            extractEduForm = { it.eduForm },
            extractName = { it.name },
            extractCount = { it.count }
        )
    }

    fun fetchEducationFormAndAccommodation() {
        fetchData(
            fetchFunction = { repository.getEducationFormAndAccommodation() },
            liveData = _educationFormAndAccommodation,
            extractEduForm = { it.eduForm },
            extractName = { it.name },
            extractCount = { it.count }
        )
    }

    fun fetchEducationFormAndCitizenship() {
        fetchData(
            fetchFunction = { repository.getEducationFormAndCitizenship() },
            liveData = _educationFormAndCitizenship,
            extractEduForm = { it.eduForm },
            extractName = { it.name },
            extractCount = { it.count }
        )
    }
}