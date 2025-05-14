package com.example.stateduuz.mainpage.OliyTalim.umumiy

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stateduuz.chart.PieChartInput
import com.example.stateduuz.data.repository.UniversitetRepository
import com.example.stateduuz.model.UniversityOwnership.universityOwnership
import com.example.stateduuz.model.ownership.ownership
import com.example.stateduuz.model.ownershipAndAcademicDegree.ownershipAndAcademicDegree
import com.example.stateduuz.model.ownershipAndAcademicDegree.ownershipAndAcademicDegreeItem
import com.example.stateduuz.model.ownershipAndAcademicRank.ownershipAndAcademicRank
import com.example.stateduuz.model.ownershipAndAcademicRank.ownershipAndAcademicRankItem
import com.example.stateduuz.model.ownershipAndEduForm.ownershipAndEduForm
import com.example.stateduuz.model.ownershipAndEduForm.ownershipAndEduFormItem
import com.example.stateduuz.model.ownershipAndEduType.ownershipAndEduType
import com.example.stateduuz.model.ownershipAndEduType.ownershipAndEduTypeItem
import com.example.stateduuz.model.ownershipAndGender.ownershipAndGender
import com.example.stateduuz.model.ownershipAndGender.ownershipAndGenderItem
import com.example.stateduuz.model.ownershipAndPaymentType.ownershipAndPaymentType
import com.example.stateduuz.model.ownershipAndPaymentType.ownershipAndPaymentTypeItem
import com.example.stateduuz.model.ownershipCourse.ownershipAndCourse
import com.example.stateduuz.model.studentAddress.studentAddress
import com.example.stateduuz.model.topFiveUniversity.topFiveUniversity
import com.example.stateduuz.model.universitety.University
import com.example.stateduuz.model.universityAddress.universityAddress
import com.example.stateduuz.model.universityAll.UniversitetAll
import com.example.stateduuz.model.universityType.UniversityType
import com.example.stateduuz.ui.theme.ColorYellow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class UmumiyViewModel @Inject constructor(
    private val repository: UniversitetRepository
) : ViewModel() {

    // Unified UI state data class
    data class UiState(
        val isLoading: Boolean = false,
        val error: String? = null,
        val univerCount: List<Int> = emptyList(),
        val teachersCount: List<Int> = emptyList(),
        val studentsCount: List<Int> = emptyList(),
        val universityOwnership: PieChartData? = null,
        val universityType: ChartData? = null,
        val ownershipAndGender: PieChartData? = null,
        val ownershipAndEduType: ChartData? = null,
        val ownershipAndCourse: ChartData? = null,
        val ownershipAndPayment: PieChartData? = null,
        val ownershipAndEduForm: ChartData? = null,
        val universityAddress: ChartData? = null,
        val top5DenseRegions: ChartData? = null,
        val top5GraduateRegions: ChartData? = null,
        val studentsByRegion: ChartData? = null,
        val topFiveUniversity: ChartData? = null,
        val ownershipData: ChartData? = null,
        val genderTeacherData: PieChartData? = null,
        val ownershipAndAcademicRank: PieChartData? = null,
        val ownershipAndAcademicDegree: PieChartData? = null
    )

    // StateFlow for UI state
    private val _uiState = MutableStateFlow(UiState(isLoading = true))
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    // Cache for static or slowly changing data
    private val cache = mutableMapOf<String, Any>()

    // Flow for category selection with debouncing
    private val categoryFlow = MutableStateFlow("Jami")

    init {
        // Fetch initial data
        fetchInitialData()

        // Handle category changes with debouncing
        viewModelScope.launch {
            categoryFlow
                .debounce(300) // Prevent rapid API calls
                .collect { category ->
                    fetchCategoryDependentData(category)
                }
        }
    }

    // Trigger category change
    fun setCategory(category: String) {
        categoryFlow.tryEmit(category)
    }

    // Fetch initial data (called once)
    private fun fetchInitialData() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _uiState.update { it.copy(isLoading = true, error = null) }

                // Parallel data fetching
                val universityDeferred = async{ repository.getUniversity() }
                val teachersDeferred = async { repository.getOwnershipAndGenderTeacher() }
                val educationDeferred = async { repository.getOwnershipAndEduType() }
                val ownershipDeferred = async { repository.getUniversityOwnership() }
                val universityTypeDeferred = async { repository.getUniversityType() }
                val universityAddressDeferred = async { repository.getUniversityAddress() }
                val topFiveUniversityDeferred = async { repository.getTopFiveUniversity() }
                val ownershipDataDeferred = async { repository.getOwnership() }

                // Process results
                _uiState.update { state ->
                    state.copy(
                        univerCount = universityDeferred.await().let { uni ->
                            listOf(
                                uni.count { it.ownership_form == 11 },
                                uni.count { it.ownership_form == 12 },
                                uni.count { it.ownership_form == 13 }
                            )
                        },
                        teachersCount = teachersDeferred.await().let { teachers ->
                            listOf(
                                teachers.sumOf { it.maleCount },
                                teachers.sumOf { it.femaleCount }
                            )
                        },
                        studentsCount = educationDeferred.await().let { edu ->
                            listOf(
                                edu.sumOf { it.bachelorCount },
                                edu.sumOf { it.masterCount },
                                edu.sumOf { it.ordinaturaCount }
                            )
                        },
                        universityOwnership = ownershipDeferred.await().let { ownership ->
                            cache.getOrPut("universityOwnership") {
                                val inputs = ownership.mapIndexed { index, item ->
                                    PieChartInput(
                                        color = colors.getOrElse(index) { Color.Yellow },
                                        value = item.count,
                                        description = item.name
                                    )
                                }
                                PieChartData(inputs, inputs.sumOf { it.value })
                            } as PieChartData
                        },
                        universityType = universityTypeDeferred.await()?.let { list ->
                            if (list.isNotEmpty()) {
                                cache.getOrPut("universityType") {
                                    val dataList = list.map { it.count }
                                    val datesList = list.map { it.name }
                                    val maxCount = dataList.maxOrNull() ?: 1
                                    val floatValue = dataList.map { it.toFloat() / maxCount.toFloat() }
                                    ChartData(dataList, datesList, floatValue)
                                } as ChartData
                            } else null
                        },
                        universityAddress = universityAddressDeferred.await().let { list ->
                            if (list.isNotEmpty()) {
                                cache.getOrPut("universityAddress") {
                                    val dataList = list.map { it.count }
                                    val datesList = list.map { it.region }
                                    val maxCount = dataList.maxOrNull() ?: 1
                                    val floatValue = dataList.map { it.toFloat() / maxCount.toFloat() }
                                    ChartData(dataList, datesList, floatValue)
                                } as ChartData
                            } else null
                        },
                        top5DenseRegions = universityAddressDeferred.await().let { list ->
                            if (list.isNotEmpty()) {
                                cache.getOrPut("top5DenseRegions") {
                                    val top5List = list.sortedByDescending { it.count }.take(5)
                                    val dataList = top5List.map { it.count }
                                    val datesList = top5List.map { it.region }
                                    val maxCount = dataList.maxOrNull() ?: 1
                                    val floatValue = dataList.map { it.toFloat() / maxCount.toFloat() }
                                    ChartData(dataList, datesList, floatValue)
                                } as ChartData
                            } else null
                        },
                        topFiveUniversity = topFiveUniversityDeferred.await().let { list ->
                            if (list.isNotEmpty()) {
                                cache.getOrPut("topFiveUniversity") {
                                    val dataList = list.map { it.count }
                                    val datesList = list.map { it.name }
                                    val maxCount = dataList.maxOrNull() ?: 1
                                    val floatValue = dataList.map { it.toFloat() / maxCount.toFloat() }
                                    ChartData(dataList, datesList, floatValue)
                                } as ChartData
                            } else null
                        },
                        ownershipData = ownershipDataDeferred.await().let { list ->
                            if (list.isNotEmpty()) {
                                cache.getOrPut("ownershipData") {
                                    val dataList = list.map { it.count }
                                    val datesList = list.map { it.name }
                                    val maxCount = dataList.maxOrNull() ?: 1
                                    val floatValue = dataList.map { it.toFloat() / maxCount.toFloat() }
                                    ChartData(dataList, datesList, floatValue)
                                } as ChartData
                            } else null
                        }
                    )
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message) }
                Log.e("UmumiyViewModel", "Error fetching initial data: ${e.message}", e)
            } finally {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    // Fetch data that depends on category selection
    private fun fetchCategoryDependentData(category: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                // Fetch category-dependent data in parallel
                val ownershipAndGenderDeferred = async { repository.getOwnershipAndGender() }
                val ownershipAndEduTypeDeferred = async { repository.getOwnershipAndEduType() }
                val ownershipAndCourseDeferred = async { repository.getOwnershipAndCourse() }
                val ownershipAndPaymentDeferred = async { repository.getOwnershipAndPaymentType() }
                val ownershipAndEduFormDeferred = async { repository.getOwnershipAndEduForm() }
                val genderTeacherDeferred = async { repository.getOwnershipAndGenderTeacher() }
                val academicRankDeferred = async { repository.getOwnershipAndAcademicRank() }
                val academicDegreeDeferred = async { repository.getOwnershipAndAcademicDegree() }
                val studentAddressDeferred = async { repository.getStudentAddress() }

                _uiState.update { state ->
                    state.copy(
                        ownershipAndGender = ownershipAndGenderDeferred.await().let { list ->
                            cache.getOrPut("ownershipAndGender_$category") {
                                val filteredData = when (category) {
                                    "Davlat", "Nodavlat", "Xorijiy" -> {
                                        val item = list.find { it.ownership == category }
                                        listOf(item?.maleCount ?: 0, item?.femaleCount ?: 0)
                                    }
                                    "Jami" -> listOf(list.sumOf { it.maleCount }, list.sumOf { it.femaleCount })
                                    else -> emptyList()
                                }
                                val totalCount = filteredData.sum()
                                val inputs = filteredData.mapIndexed { index, item ->
                                    PieChartInput(
                                        color = colors.getOrElse(index) { Color.Yellow },
                                        value = item,
                                        description = listOf("Erkaklar", "Ayollar").getOrElse(index) { "" }
                                    )
                                }
                                PieChartData(inputs, totalCount)
                            } as PieChartData
                        },
                        ownershipAndEduType = ownershipAndEduTypeDeferred.await().let { list ->
                            cache.getOrPut("ownershipAndEduType_$category") {
                                val filteredData = when (category) {
                                    "Davlat", "Nodavlat", "Xorijiy" -> {
                                        val item = list.find { it.ownership == category }
                                        listOf(
                                            item?.bachelorCount ?: 0,
                                            item?.masterCount ?: 0,
                                            item?.ordinaturaCount ?: 0
                                        )
                                    }
                                    "Jami" -> listOf(
                                        list.sumOf { it.bachelorCount },
                                        list.sumOf { it.masterCount },
                                        list.sumOf { it.ordinaturaCount }
                                    )
                                    else -> emptyList()
                                }
                                val dataList = filteredData
                                val datesList = listOf("Bakalavr", "Magistr", "Ordinatura")
                                val maxCount = dataList.maxOrNull() ?: 1
                                val floatValue = dataList.map { it.toFloat() / maxCount.toFloat() }
                                ChartData(dataList, datesList, floatValue)
                            } as ChartData
                        },
                        ownershipAndCourse = ownershipAndCourseDeferred.await().let { list ->
                            cache.getOrPut("ownershipAndCourse_$category") {
                                val filteredData = when (category) {
                                    "Davlat", "Nodavlat", "Xorijiy" -> {
                                        val item = list.find { it.ownership == category }
                                        listOf(
                                            item?.course1Count ?: 0,
                                            item?.course2Count ?: 0,
                                            item?.course3Count ?: 0,
                                            item?.course4Count ?: 0,
                                            item?.course5Count ?: 0,
                                            item?.course6Count ?: 0
                                        )
                                    }
                                    "Jami" -> listOf(
                                        list.sumOf { it.course1Count },
                                        list.sumOf { it.course2Count },
                                        list.sumOf { it.course3Count },
                                        list.sumOf { it.course4Count },
                                        list.sumOf { it.course5Count },
                                        list.sumOf { it.course6Count }
                                    )
                                    else -> emptyList()
                                }
                                val datesList = listOf("1-kurs", "2-kurs", "3-kurs", "4-kurs", "5-kurs", "6-kurs")
                                val maxCount = filteredData.maxOrNull() ?: 1
                                val floatValue = filteredData.map { it.toFloat() / maxCount.toFloat() }
                                ChartData(filteredData, datesList, floatValue)
                            } as ChartData
                        },
                        ownershipAndPayment = ownershipAndPaymentDeferred.await().let { list ->
                            cache.getOrPut("ownershipAndPayment_$category") {
                                val filteredData = when (category) {
                                    "Davlat", "Nodavlat", "Xorijiy" -> {
                                        val item = list.find { it.ownership == category }
                                        listOf(item?.grandCount ?: 0, item?.contractCount ?: 0)
                                    }
                                    "Jami" -> listOf(
                                        list.sumOf { it.grandCount },
                                        list.sumOf { it.contractCount }
                                    )
                                    else -> emptyList()
                                }
                                val totalCount = filteredData.sum()
                                val inputs = filteredData.mapIndexed { index, item ->
                                    PieChartInput(
                                        color = colors.getOrElse(index) { Color.Yellow },
                                        value = item,
                                        description = listOf("Grand", "To'lov kontrakti").getOrElse(index) { "" }
                                    )
                                }
                                PieChartData(inputs, totalCount)
                            } as PieChartData
                        },
                        ownershipAndEduForm = ownershipAndEduFormDeferred.await().let { list ->
                            cache.getOrPut("ownershipAndEduForm_$category") {
                                val filteredData = when (category) {
                                    "Davlat", "Nodavlat", "Xorijiy" -> {
                                        val item = list.find { it.ownership == category }
                                        listOf(
                                            item?.externalCount ?: 0,
                                            item?.jointCount ?: 0,
                                            item?.specialExternalCount ?: 0,
                                            item?.remoteCount ?: 0,
                                            item?.daytimeCount ?: 0,
                                            item?.eveningCount ?: 0,
                                            item?.secondEveningCount ?: 0,
                                            item?.secondDaytimeCount ?: 0,
                                            item?.secondExternalCount ?: 0
                                        )
                                    }
                                    "Jami" -> listOf(
                                        list.sumOf { it.externalCount },
                                        list.sumOf { it.jointCount },
                                        list.sumOf { it.specialExternalCount },
                                        list.sumOf { it.remoteCount },
                                        list.sumOf { it.daytimeCount },
                                        list.sumOf { it.eveningCount },
                                        list.sumOf { it.secondEveningCount },
                                        list.sumOf { it.secondDaytimeCount },
                                        list.sumOf { it.secondExternalCount }
                                    )
                                    else -> emptyList()
                                }
                                val datesList = listOf(
                                    "Sirtqi", "Qo'shma", "Maxsus sirtqi", "Masofaviy", "Kunduzgi",
                                    "Kechki", "Ikkinchi Oliy(kechki)", "Ikkinchi Oliy(kunduzgi)", "Ikkinchi Oliy(sirtqi)"
                                )
                                val maxCount = filteredData.maxOrNull() ?: 1
                                val floatValue = filteredData.map { it.toFloat() / maxCount.toFloat() }
                                ChartData(filteredData, datesList, floatValue)
                            } as ChartData
                        },
                        genderTeacherData = genderTeacherDeferred.await().let { list ->
                            cache.getOrPut("genderTeacherData_$category") {
                                val filteredData = when (category) {
                                    "Davlat", "Nodavlat", "Xorijiy" -> {
                                        val item = list.find { it.ownership == category }
                                        listOf(item?.maleCount ?: 0, item?.femaleCount ?: 0)
                                    }
                                    "Jami" -> listOf(
                                        list.sumOf { it.maleCount },
                                        list.sumOf { it.femaleCount }
                                    )
                                    else -> emptyList()
                                }
                                val totalCount = filteredData.sum()
                                val inputs = filteredData.mapIndexed { index, item ->
                                    PieChartInput(
                                        color = colors.getOrElse(index) { Color.Yellow },
                                        value = item,
                                        description = listOf("Erkaklar", "Ayollar").getOrElse(index) { "" }
                                    )
                                }
                                PieChartData(inputs, totalCount)
                            } as PieChartData
                        },
                        ownershipAndAcademicRank = academicRankDeferred.await().let { list ->
                            cache.getOrPut("academicRank_$category") {
                                val filteredData = when (category) {
                                    "Davlat", "Nodavlat", "Xorijiy" -> {
                                        val item = list.find { it.ownership == category }
                                        listOf(
                                            item?.withoutRankCount ?: 0,
                                            item?.dotsentCount ?: 0,
                                            item?.seniorResearcherCount ?: 0,
                                            item?.professorCount ?: 0
                                        )
                                    }
                                    "Jami" -> listOf(
                                        list.sumOf { it.withoutRankCount },
                                        list.sumOf { it.dotsentCount },
                                        list.sumOf { it.seniorResearcherCount },
                                        list.sumOf { it.professorCount }
                                    )
                                    else -> emptyList()
                                }
                                val totalCount = filteredData.sum()
                                val inputs = filteredData.mapIndexed { index, item ->
                                    PieChartInput(
                                        color = colors.getOrElse(index) { Color.Yellow },
                                        value = item,
                                        description = listOf("Unvonsiz", "Dotsent", "Katta Ilmiy xodim", "Professor")
                                            .getOrElse(index) { "" }
                                    )
                                }
                                PieChartData(inputs, totalCount)
                            } as PieChartData
                        },
                        ownershipAndAcademicDegree = academicDegreeDeferred.await().let { list ->
                            cache.getOrPut("academicDegree_$category") {
                                val filteredData = when (category) {
                                    "Davlat", "Nodavlat", "Xorijiy" -> {
                                        val item = list.find { it.ownership == category }
                                        listOf(
                                            item?.withoutDegreeCount ?: 0,
                                            item?.scienceCandidateCount ?: 0,
                                            item?.scienceDoctorCount ?: 0
                                        )
                                    }
                                    "Jami" -> listOf(
                                        list.sumOf { it.withoutDegreeCount },
                                        list.sumOf { it.scienceCandidateCount },
                                        list.sumOf { it.scienceDoctorCount }
                                    )
                                    else -> emptyList()
                                }
                                val totalCount = filteredData.sum()
                                val inputs = filteredData.mapIndexed { index, item ->
                                    PieChartInput(
                                        color = colors.getOrElse(index) { Color.Yellow },
                                        value = item,
                                        description = listOf("Darajasiz", "Fan nomzodi,PHD", "Fan doktori,DSc")
                                            .getOrElse(index) { "" }
                                    )
                                }
                                PieChartData(inputs, totalCount)
                            } as PieChartData
                        },
                        top5GraduateRegions = studentAddressDeferred.await().let { list ->
                            if (list.isNotEmpty()) {
                                cache.getOrPut("top5GraduateRegions") {
                                    val top5List = list.sortedByDescending { it.count }.take(5)
                                    val dataList = top5List.map { it.count }
                                    val datesList = top5List.map { it.region }
                                    val maxCount = dataList.maxOrNull() ?: 1
                                    val floatValue = dataList.map { it.toFloat() / maxCount.toFloat() }
                                    ChartData(dataList, datesList, floatValue)
                                } as ChartData
                            } else null
                        },
                        studentsByRegion = studentAddressDeferred.await().let { list ->
                            if (list.isNotEmpty()) {
                                cache.getOrPut("studentsByRegion") {
                                    val dataList = list.map { it.count }
                                    val datesList = list.map { it.region }
                                    val maxCount = dataList.maxOrNull() ?: 1
                                    val floatValue = dataList.map { it.toFloat() / maxCount.toFloat() }
                                    ChartData(dataList, datesList, floatValue)
                                } as ChartData
                            } else null
                        }
                    )
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message) }
                Log.e("UmumiyViewModel", "Error fetching category data: ${e.message}", e)
            }
        }
    }

    // Clear cache if needed (e.g., on refresh)
    fun clearCache() {
        cache.clear()
        fetchInitialData()
    }
}

data class PieChartData(
    val inputs: List<PieChartInput>,
    val totalCount: Int
)

data class PieChartInput(
    val color: Color,
    val value: Int,
    val description: String
)

data class ChartData(
    val dataList: List<Int>,
    val datesList: List<String>,
    val float: List<Float>
)