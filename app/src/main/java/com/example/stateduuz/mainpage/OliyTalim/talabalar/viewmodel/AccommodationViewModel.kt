package com.example.stateduuz.mainpage.OliyTalim.talabalar.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stateduuz.data.repository.UniversitetRepository
import com.example.stateduuz.model.accommodationAndGender.accommodationAndGender
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
@HiltViewModel
class AccommodationViewModel @Inject constructor(
    private val repository: UniversitetRepository
) : ViewModel() {

    private val _accommodationAndGender = MutableStateFlow<ChartUiState?>(null)
    val accommodationAndGender: StateFlow<ChartUiState?> = _accommodationAndGender.asStateFlow()

    fun fetchAccommodationAndGender() {
        viewModelScope.launch {
            try {
                val data = withContext(Dispatchers.IO) {
                    repository.getAccommodationAndGender()
                }
                withContext(Dispatchers.Default) {
                    val xAxisData = data.map { it.accommodation }.distinct()
                    val yAxisData = data.map { it.name }.distinct()
                    val groupedData = data.groupBy { it.name }
                        .mapValues { (_, values) -> values.associateBy { it.accommodation } }
                    val allData = yAxisData.map { name ->
                        xAxisData.map { accommodation ->
                            groupedData[name]?.get(accommodation)?.count ?: 0
                        }
                    }
                    val topValues = yAxisData.map { name ->
                        groupedData[name]?.values?.sumOf { it.count } ?: 0
                    }
                    _accommodationAndGender.value = ChartUiState(
                        xAxis = xAxisData,
                        yAxis = yAxisData,
                        allData = allData,
                        topValues = topValues
                    )
                }
            } catch (e: Exception) {
                Log.e("AccommodationViewModel", "fetchAccommodationAndGender: Exception - ${e.message}")
                e.printStackTrace()
            }
        }
    }
}
