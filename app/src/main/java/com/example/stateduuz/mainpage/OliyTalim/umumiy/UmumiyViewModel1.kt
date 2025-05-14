package com.example.stateduuz.mainpage.OliyTalim.umumiy

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stateduuz.data.repository.UniversitetRepository
import com.example.stateduuz.model.UniversityOwnership.universityOwnership
import com.example.stateduuz.model.ownership.ownership
import com.example.stateduuz.model.ownershipAndAcademicDegree.ownershipAndAcademicDegree
import com.example.stateduuz.model.ownershipAndAcademicRank.ownershipAndAcademicRank
import com.example.stateduuz.model.ownershipAndEduForm.ownershipAndEduForm
import com.example.stateduuz.model.ownershipAndEduType.ownershipAndEduType
import com.example.stateduuz.model.ownershipAndGender.ownershipAndGender
import com.example.stateduuz.model.ownershipAndPaymentType.ownershipAndPaymentType
import com.example.stateduuz.model.ownershipCourse.ownershipAndCourse
import com.example.stateduuz.model.studentAddress.studentAddress
import com.example.stateduuz.model.topFiveUniversity.topFiveUniversity
import com.example.stateduuz.model.universitety.University
import com.example.stateduuz.model.universityAddress.universityAddress
import com.example.stateduuz.model.universityType.UniversityType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import javax.inject.Inject

@HiltViewModel
class UmumiyViewModel1 @Inject constructor(
    private val repository: UniversitetRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    init {
        fetchAllData()
    }

    private fun fetchAllData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            supervisorScope {
                try {
                    val universitetAll = async{ repository.getUniversity() }
                    val universityType = async { repository.getUniversityType() }
                    val universityOwnership = async { repository.getUniversityOwnership() }
                    val ownershipAndGender = async { repository.getOwnershipAndGender() }
                    val ownershipAndEduType = async { repository.getOwnershipAndEduType() }
                    val ownershipAndCourse = async { repository.getOwnershipAndCourse() }
                    val ownershipAndPaymentType = async { repository.getOwnershipAndPaymentType() }
                    val ownershipAndEduForm = async { repository.getOwnershipAndEduForm() }
                    val universityAddress = async { repository.getUniversityAddress() }
                    val studentAddress = async { repository.getStudentAddress() }
                    val topFiveUniversity = async { repository.getTopFiveUniversity() }
                    val ownership = async { repository.getOwnership() }
                    val ownershipAndGenderTeacher = async { repository.getOwnershipAndGenderTeacher() }
                    val ownershipAndAcademicDegree = async { repository.getOwnershipAndAcademicDegree() }

                    _uiState.update {
                        it.copy(
                            universitetAll = universitetAll.await(),
                            universityType = universityType.await(),
                            universityOwnership = universityOwnership.await(),
                            ownershipAndGender = ownershipAndGender.await(),
                            ownershipAndEduType = ownershipAndEduType.await(),
                            ownershipAndCourse = ownershipAndCourse.await(),
                            ownershipAndPaymentType = ownershipAndPaymentType.await(),
                            ownershipAndEduForm = ownershipAndEduForm.await(),
                            universityAddress = universityAddress.await(),
                            studentAddress = studentAddress.await(),
                            topFiveUniversity = topFiveUniversity.await(),
                            ownership = ownership.await(),
                            ownershipAndGenderTeacher = ownershipAndGenderTeacher.await(),
                            ownershipAndAcademicDegree = ownershipAndAcademicDegree.await(),
                            isLoading = false
                        )
                    }
                } catch (e: Exception) {
                    _uiState.update {
                        it.copy(isLoading = false, error = e.message ?: "Xatolik yuz berdi")
                    }
                }
            }
        }
    }

    fun retry() {
        fetchAllData()
    }

    data class UiState(
        val isLoading: Boolean = false,
        val error: String? = null,
        val universitetAll: University? = null,
        val universityType: UniversityType? = null,
        val universityOwnership: universityOwnership? = null,
        val ownershipAndGender: ownershipAndGender? = null,
        val ownershipAndEduType: ownershipAndEduType? = null,
        val ownershipAndCourse: ownershipAndCourse? = null,
        val ownershipAndPaymentType: ownershipAndPaymentType? = null,
        val ownershipAndEduForm: ownershipAndEduForm? = null,
        val universityAddress: universityAddress? = null,
        val studentAddress: studentAddress? = null,
        val topFiveUniversity: topFiveUniversity? = null,
        val ownership: ownership? = null,
        val ownershipAndGenderTeacher: ownershipAndGender? = null,
        val ownershipAndAcademicRank: ownershipAndAcademicRank? = null,
        val ownershipAndAcademicDegree: ownershipAndAcademicDegree? = null
    )
}
