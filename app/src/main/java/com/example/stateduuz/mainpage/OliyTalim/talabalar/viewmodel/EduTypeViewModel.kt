package com.example.stateduuz.mainpage.OliyTalim.talabalar.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stateduuz.data.repository.UniversitetRepository
import com.example.stateduuz.model.eduTypeAndGander.eduTypeAndGender
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TalimTuriViewModel @Inject constructor(
    private val repository: UniversitetRepository
) : ViewModel() {
    private val _eduTypeAndGender = MutableLiveData<eduTypeAndGender>()
    val eduTypeAndGender: LiveData<eduTypeAndGender> get() = _eduTypeAndGender

    fun fetchEduTypeAndgender() {
        viewModelScope.launch{
            try {
                _eduTypeAndGender.value = repository.getEduTypeAndGender()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
