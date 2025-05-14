package com.example.stateduuz.mainpage.OliyTalim.Teachers

import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stateduuz.chart.PieChartInput
import com.example.stateduuz.data.repository.UniversitetRepository
import com.example.stateduuz.model.techerStatisticAcademicDegree.techerStatisticAcademicDegree
import com.example.stateduuz.model.techerStatisticAcademicRank.techerStatisticAcademicRank
import com.example.stateduuz.model.techerStatisticAge.techerStatisticAge
import com.example.stateduuz.model.techerStatisticChiefPosition.techerStatisticChiefPosition
import com.example.stateduuz.model.techerStatisticCitizenship.techerStatisticCitizenship
import com.example.stateduuz.model.techerStatisticEmployeeForm.techerStatisticEmployeeForm
import com.example.stateduuz.model.techerStatisticGender.techerStatisticGender
import com.example.stateduuz.model.techerStatisticPosition.techerStatisticPosition
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class TeachersViewModel @Inject constructor(
    private val repository: UniversitetRepository
) : ViewModel() {

    // Core data
    private val _teacherStatisticGender = MutableLiveData<techerStatisticGender>()
    val teacherStatisticGender: LiveData<techerStatisticGender> get() = _teacherStatisticGender

    private val _teacherStatisticChiefPosition = MutableLiveData<techerStatisticChiefPosition>()
    val teacherStatisticChiefPosition: LiveData<techerStatisticChiefPosition> get() = _teacherStatisticChiefPosition

    private val _teacherStatisticAcademicDegree = MutableLiveData<techerStatisticAcademicDegree>()
    val teacherStatisticAcademicDegree: LiveData<techerStatisticAcademicDegree> get() = _teacherStatisticAcademicDegree

    private val _teacherStatisticAcademicRank = MutableLiveData<techerStatisticAcademicRank>()
    val teacherStatisticAcademicRank: LiveData<techerStatisticAcademicRank> get() = _teacherStatisticAcademicRank

    private val _teacherStatisticPosition = MutableLiveData<techerStatisticPosition>()
    val teacherStatisticPosition: LiveData<techerStatisticPosition> get() = _teacherStatisticPosition

    private val _teacherStatisticEmployeeForm = MutableLiveData<techerStatisticEmployeeForm>()
    val teacherStatisticEmployeeForm: LiveData<techerStatisticEmployeeForm> get() = _teacherStatisticEmployeeForm

    private val _teacherStatisticCitizenship = MutableLiveData<techerStatisticCitizenship>()
    val teacherStatisticCitizenship: LiveData<techerStatisticCitizenship> get() = _teacherStatisticCitizenship

    private val _teacherStatisticAge = MutableLiveData<techerStatisticAge>()
    val teacherStatisticAge: LiveData<techerStatisticAge> get() = _teacherStatisticAge

    // UI data structures
    data class PieChartData(
        val inputs: List<PieChartInput>,
        val totalCount: Int
    )

    data class BarChartData(
        val xAxisScale: List<String>,
        val yAxisScale: List<String>,
        val allDataLists: List<List<Int>>,
        val topValues: List<Int>
    )

    // Chart data flows
    private val _genderPieChartData = MutableStateFlow<PieChartData?>(null)
    val genderPieChartData: StateFlow<PieChartData?> = _genderPieChartData.asStateFlow()

    private val _chiefPositionPieChartData = MutableStateFlow<PieChartData?>(null)
    val chiefPositionPieChartData: StateFlow<PieChartData?> = _chiefPositionPieChartData.asStateFlow()

    private val _academicDegreePieChartData = MutableStateFlow<PieChartData?>(null)
    val academicDegreePieChartData: StateFlow<PieChartData?> = _academicDegreePieChartData.asStateFlow()

    private val _academicRankBarChartData = MutableStateFlow<BarChartData?>(null)
    val academicRankBarChartData: StateFlow<BarChartData?> = _academicRankBarChartData.asStateFlow()

    private val _positionPieChartData = MutableStateFlow<PieChartData?>(null)
    val positionPieChartData: StateFlow<PieChartData?> = _positionPieChartData.asStateFlow()

    private val _employeeFormPieChartData = MutableStateFlow<PieChartData?>(null)
    val employeeFormPieChartData: StateFlow<PieChartData?> = _employeeFormPieChartData.asStateFlow()

    private val _citizenshipPieChartData = MutableStateFlow<PieChartData?>(null)
    val citizenshipPieChartData: StateFlow<PieChartData?> = _citizenshipPieChartData.asStateFlow()

    private val _ageBarChartData = MutableStateFlow<BarChartData?>(null)
    val ageBarChartData: StateFlow<BarChartData?> = _ageBarChartData.asStateFlow()

    // Colors for pie charts
    val colors = listOf(
        Color(0xFF4DA2F1),
        Color(0xFFFF6482),
        Color(0xFF43B1A0),
        Color(0xFFFF7F00),
        Color(0xFFFFD426),
        Color(0xFF7D7AFF),
        Color(0xFF4DA2F1),
        Color(0xFFFF6482),
        Color(0xFF43B1A0),
        Color(0xFF43B1A0),
        Color(0xFFFF7F00),
        Color(0xFFFFD426)
    )
    init {
        teacherStatisticGender.observeForever { it?.let { processGenderData(it) } }
        teacherStatisticChiefPosition.observeForever { it?.let { processChiefPositionData(it) } }
        teacherStatisticAcademicDegree.observeForever { it?.let { processAcademicDegreeData(it) } }
        teacherStatisticAcademicRank.observeForever { it?.let { processAcademicRankData(it) } }
        teacherStatisticPosition.observeForever { it?.let { processPositionData(it) } }
        teacherStatisticEmployeeForm.observeForever { it?.let { processEmployeeFormData(it) } }
        teacherStatisticCitizenship.observeForever { it?.let { processCitizenshipData(it) } }
        teacherStatisticAge.observeForever { it?.let { processAgeData(it) } }
    }

    // Data fetching
    fun fetchTeacherStatisticGender() = fetchData(repository::getTeacherStatisticGender, _teacherStatisticGender)
    fun fetchTeacherStatisticChiefPosition() = fetchData(repository::getTeacherStatisticChiefPosition, _teacherStatisticChiefPosition)
    fun fetchTeacherStatisticAcademicDegree() = fetchData(repository::getTeacherStatisticAcademicDegree, _teacherStatisticAcademicDegree)
    fun fetchTeacherStatisticAcademicRank() = fetchData(repository::getTeacherStatisticAcademicRank, _teacherStatisticAcademicRank)
    fun fetchTeacherStatisticPosition() = fetchData(repository::getTeacherStatisticPosition, _teacherStatisticPosition)
    fun fetchTeacherStatisticEmployeeForm() = fetchData(repository::getTeacherStatisticEmployeeForm, _teacherStatisticEmployeeForm)
    fun fetchTeacherStatisticCitizenship() = fetchData(repository::getTeacherStatisticCitizenship, _teacherStatisticCitizenship)
    fun fetchTeacherStatisticAge() = fetchData(repository::getTeacherStatisticAge, _teacherStatisticAge)

    private fun <T> fetchData(fetchFunction: suspend () -> T, liveData: MutableLiveData<T>) {
        viewModelScope.launch {
            try {
                val data = fetchFunction()
                liveData.postValue(data)
                Log.d("TeachersViewModel", "Fetched data: $data")
            } catch (e: Exception) {
                Log.e("TeachersViewModel", "Error fetching data: ${e.message}")
            }
        }
    }

    // Data processing
    private fun processGenderData(data: techerStatisticGender) {
        val inputs = data.mapIndexed { index, item ->
            PieChartInput(
                color = colors.getOrElse(index) { Color(0xFFFFC107) },
                value = item.count,
                description = item.name
            )
        }
        _genderPieChartData.value = PieChartData(
            inputs = inputs,
            totalCount = inputs.sumOf { it.value }
        )
    }

    private fun processChiefPositionData(data: techerStatisticChiefPosition) {
        val inputs = data.mapIndexed { index, item ->
            PieChartInput(
                color = colors.getOrElse(index) { Color(0xFFFFC107) },
                value = item.count,
                description = item.name
            )
        }
        _chiefPositionPieChartData.value = PieChartData(
            inputs = inputs,
            totalCount = inputs.sumOf { it.value }
        )
    }

    private fun processAcademicDegreeData(data: techerStatisticAcademicDegree) {
        val darajasizlarCount = data.filter { it.name == "Darajasiz" }.sumOf { it.count }
        val darajaliCount = data.filter { it.name != "Darajasiz" }.sumOf { it.count }
        val inputs = listOf(
            PieChartInput(color = colors.getOrElse(0) { Color(0xFFFFC107) }, value = darajasizlarCount, description = "Darajasiz"),
            PieChartInput(color = colors.getOrElse(1) { Color(0xFF2196F3) }, value = darajaliCount, description = "Darajali")
        )
        _academicDegreePieChartData.value = PieChartData(
            inputs = inputs,
            totalCount = inputs.sumOf { it.value }
        )
    }

    private fun processAcademicRankData(data: techerStatisticAcademicRank) {
        val xAxisScale = data.map { it.name }.distinct()
        val yAxisScale = data.map { it.gender }.distinct()
        val groupedData = data.groupBy { it.gender }.mapValues { (_, values) -> values.associateBy { it.name } }
        val allDataLists = yAxisScale.map { gender ->
            xAxisScale.map { name -> groupedData[gender]?.get(name)?.count ?: 0 }
        }
        val topValues = yAxisScale.map { gender ->
            groupedData[gender]?.values?.sumOf { it.count } ?: 0
        }
        _academicRankBarChartData.value = BarChartData(
            xAxisScale = xAxisScale,
            yAxisScale = yAxisScale,
            allDataLists = allDataLists,
            topValues = topValues
        )
    }

    private fun processPositionData(data: techerStatisticPosition) {
        val inputs = data.mapIndexed { index, item ->
            PieChartInput(
                color = colors.getOrElse(index) { Color(0xFFFFC107) },
                value = item.count,
                description = item.name
            )
        }
        _positionPieChartData.value = PieChartData(
            inputs = inputs,
            totalCount = inputs.sumOf { it.value }
        )
    }

    private fun processEmployeeFormData(data: techerStatisticEmployeeForm) {
        val inputs = data.mapIndexed { index, item ->
            PieChartInput(
                color = colors.getOrElse(index) { Color(0xFFFFC107) },
                value = item.count,
                description = item.name
            )
        }
        _employeeFormPieChartData.value = PieChartData(
            inputs = inputs,
            totalCount = inputs.sumOf { it.value }
        )
    }

    private fun processCitizenshipData(data: techerStatisticCitizenship) {
        val inputs = data.mapIndexed { index, item ->
            PieChartInput(
                color = colors.getOrElse(index) { Color(0xFFFFC107) },
                value = item.count,
                description = item.name
            )
        }
        _citizenshipPieChartData.value = PieChartData(
            inputs = inputs,
            totalCount = inputs.sumOf { it.value }
        )
    }

    private fun processAgeData(data: techerStatisticAge) {
        val xAxisScale = data.map { it.age }.distinct()
        val yAxisScale = data.map { it.gender }.distinct()
        val groupedData = data.groupBy { it.gender }.mapValues { (_, values) -> values.associateBy { it.age } }
        val allDataLists = yAxisScale.map { gender ->
            xAxisScale.map { age -> groupedData[gender]?.get(age)?.count ?: 0 }
        }
        val topValues = yAxisScale.map { gender ->
            groupedData[gender]?.values?.sumOf { it.count } ?: 0
        }
        _ageBarChartData.value = BarChartData(
            xAxisScale = xAxisScale,
            yAxisScale = yAxisScale,
            allDataLists = allDataLists,
            topValues = topValues
        )
    }

    override fun onCleared() {
        super.onCleared()
    }
}