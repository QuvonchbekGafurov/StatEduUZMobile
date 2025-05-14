package com.example.stateduuz.mainpage.ProfessionalTalim

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stateduuz.data.repository.UniversitetRepository
import com.example.stateduuz.model.Professional.profEduType.ProfEduType
import com.example.stateduuz.model.muassasalar.muassasalar
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MuassasalarViewModel @Inject constructor(
    private val repository: UniversitetRepository
) : ViewModel() {

    private val _muassasalar = MutableLiveData<muassasalar>()
    val muassasalar: LiveData<muassasalar> get() = _muassasalar
    fun fetchEducationTypes() = fetchData(repository::getMuassasalar, _muassasalar)
    private fun <T> fetchData(fetchFunction: suspend () -> T, liveData: MutableLiveData<T>) {
        viewModelScope.launch {
            try {
                val data = fetchFunction()
                liveData.value = data
                Log.d("ProfimViewModel", "Oquvchilar edu types -> $data") // ← Bu yerga ham
            } catch (e: Exception) {
                Log.e("ProfimviewModel", "fetchData: Exception - ${e.message}")
                e.printStackTrace()
            }
        }
    }
}