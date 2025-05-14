package com.example.stateduuz.mainpage.OliyTalim.talabalar.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stateduuz.data.repository.UniversitetRepository
import com.example.stateduuz.model.paymentTypeAndAccommodation.paymentTypeAndAccommodation
import com.example.stateduuz.model.paymentTypeAndAge.paymentTypeAndAge
import com.example.stateduuz.model.paymentTypeAndCitizenship.paymentTypeAndCitizenship
import com.example.stateduuz.model.paymentTypeAndCourse.paymentTypeAndCourse
import com.example.stateduuz.model.paymentTypeAndGender.paymentTypeAndGender
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
@HiltViewModel
class PaymentTypeViewModel @Inject constructor(
    private val repository: UniversitetRepository
) : ViewModel() {

    private val _paymentTypeAndGender = MutableStateFlow<ChartUiState?>(null)
    val paymentTypeAndGender: StateFlow<ChartUiState?> = _paymentTypeAndGender.asStateFlow()

    private val _paymentTypeAndAge = MutableStateFlow<ChartUiState?>(null)
    val paymentTypeAndAge: StateFlow<ChartUiState?> = _paymentTypeAndAge.asStateFlow()

    private val _paymentTypeAndCitizenship = MutableStateFlow<ChartUiState?>(null)
    val paymentTypeAndCitizenship: StateFlow<ChartUiState?> = _paymentTypeAndCitizenship.asStateFlow()

    private val _paymentTypeAndCourse = MutableStateFlow<ChartUiState?>(null)
    val paymentTypeAndCourse: StateFlow<ChartUiState?> = _paymentTypeAndCourse.asStateFlow()

    private val _paymentTypeAndAccommodation = MutableStateFlow<ChartUiState?>(null)
    val paymentTypeAndAccommodation: StateFlow<ChartUiState?> = _paymentTypeAndAccommodation.asStateFlow()

    // Generic fetch function for ChartUiState
    private fun <T> fetchChartData(
        fetchFunction: suspend () -> List<T>,
        stateFlow: MutableStateFlow<ChartUiState?>,
        extractPaymentType: (T) -> String,
        extractName: (T) -> String,
        extractCount: (T) -> Int
    ) {
        viewModelScope.launch {
            try {
                val data = withContext(Dispatchers.IO) {
                    fetchFunction()
                }
                withContext(Dispatchers.Default) {
                    val xAxisData = data.map(extractPaymentType).distinct()
                    val yAxisData = data.map(extractName).distinct()
                    val groupedData = data.groupBy(extractName)
                        .mapValues { (_, values) -> values.associateBy(extractPaymentType) }
                    val allData = yAxisData.map { name ->
                        xAxisData.map { paymentType ->
                            groupedData[name]?.get(paymentType)?.let(extractCount) ?: 0
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
                Log.e("PaymentTypeViewModel", "fetchChartData: Exception - ${e.message}")
                e.printStackTrace()
            }
        }
    }

    fun fetchPaymentTypeAndGender() {
        fetchChartData(
            fetchFunction = { repository.getPaymentTypeAndGender() },
            stateFlow = _paymentTypeAndGender,
            extractPaymentType = { it.paymentType },
            extractName = { it.name },
            extractCount = { it.count }
        )
    }

    fun fetchPaymentTypeAndAge() {
        fetchChartData(
            fetchFunction = { repository.getPaymentTypeAndAge() },
            stateFlow = _paymentTypeAndAge,
            extractPaymentType = { it.paymentType },
            extractName = { it.name },
            extractCount = { it.count }
        )
    }

    fun fetchPaymentTypeAndCitizenship() {
        fetchChartData(
            fetchFunction = { repository.getPaymentTypeAndCitizenship() },
            stateFlow = _paymentTypeAndCitizenship,
            extractPaymentType = { it.paymentType },
            extractName = { it.name },
            extractCount = { it.count }
        )
    }

    fun fetchPaymentTypeAndCourse() {
        fetchChartData(
            fetchFunction = { repository.getPaymentTypeAndCourse() },
            stateFlow = _paymentTypeAndCourse,
            extractPaymentType = { it.paymentType },
            extractName = { it.name },
            extractCount = { it.count }
        )
    }

    fun fetchPaymentTypeAndAccommodation() {
        fetchChartData(
            fetchFunction = { repository.getPaymentTypeAndAccommodation() },
            stateFlow = _paymentTypeAndAccommodation,
            extractPaymentType = { it.paymentType },
            extractName = { it.name },
            extractCount = { it.count }
        )
    }
}
