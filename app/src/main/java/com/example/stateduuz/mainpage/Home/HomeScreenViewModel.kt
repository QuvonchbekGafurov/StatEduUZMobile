package com.example.stateduuz.mainpage.Home

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.stateduuz.data.repository.UniversitetRepository
import com.example.stateduuz.model.Professional.profEduType.ProfEduType
import com.example.stateduuz.model.ownership.ownership
import com.example.stateduuz.model.ownershipAndGender.ownershipAndGender
import com.example.stateduuz.model.techerStatisticPosition.techerStatisticPosition
import com.example.stateduuz.model.universitety.University
import com.example.stateduuz.model.universityAll.UniversitetAll
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val repository: UniversitetRepository
) : ViewModel() {

    private val _ownership = MutableLiveData<ownership>()
    val ownership: LiveData<ownership> get() = _ownership

    private val _position = MutableLiveData<techerStatisticPosition>()
    val position: LiveData<techerStatisticPosition> get() = _position

    private val _otm = MutableLiveData<University>()
    val otm: LiveData<University> get() = _otm

    private val _educationType = MutableLiveData<ProfEduType>()
    val educationType: LiveData<ProfEduType> get() = _educationType

    fun fetchPosition() = fetchData({ repository.getTeacherStatisticPosition() }, _position)
    fun fetchOwnership() = fetchData({ repository.getOwnership() }, _ownership)
    fun fetchOtm() = fetchData({ repository.getUniversity() }, _otm)
    fun fetchEducationType() = fetchData({ repository.getEducationTypes() }, _educationType)

    private fun <T> fetchData(fetchFunction: suspend () -> T, liveData: MutableLiveData<T>) {
        viewModelScope.launch(Dispatchers.IO) { // Use Dispatchers.IO for repository calls
            try {
                val data = fetchFunction()
                withContext(Dispatchers.Main) { // Switch to Main for LiveData updates
                    liveData.value = data ?: throw IllegalStateException("Received null data")
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Log.e("ViewModel", "fetchData: Exception - ${e.message}", e)
                }
            }
        }
    }
}