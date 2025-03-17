package com.example.stateduuz.data.repository

import com.example.stateduuz.data.retrofit.StudentApiService
import com.example.stateduuz.model.universitetall.UniversitetAll
import com.example.stateduuz.model.universitetall.eduTypeAndGender.eduTypeAndGender
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Inject

@ActivityRetainedScoped
class UniversitetRepository @Inject constructor(
    private val apiService: StudentApiService
) {
    suspend fun getUniversitetAll(): UniversitetAll {
        return apiService.getStudents()
    }
    suspend fun getEduTypeAndGender():eduTypeAndGender{
        return apiService.getEduTypeAndGender()
    }
}
