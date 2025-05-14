package com.example.stateduuz.mainpage.Doktorantura

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stateduuz.data.repository.UniversitetRepository
import com.example.stateduuz.model.Doctorantura
import com.example.stateduuz.model.ownership.ownership
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DoctoranturaViewModel @Inject constructor(
    private val repository: UniversitetRepository
) : ViewModel() {
    private val _doctroantura = MutableLiveData<Doctorantura>()
    val doctoratura: LiveData<Doctorantura> get() = _doctroantura
    fun fetchdoctorantura() = fetchData(repository::getDoctorantura, _doctroantura)
    private fun <T> fetchData(fetchFunction: suspend () -> T, liveData: MutableLiveData<T>) {
        viewModelScope.launch {
            try {
                val data = fetchFunction()
                if (data == null) {
                    Log.e("ViewModel", "fetchData: Null data received for ${liveData.value}")
                }
                liveData.value = data
            } catch (e: Exception) {
                Log.e("ViewModel", "fetchData: Exception - ${e.message}")
                e.printStackTrace()
            }
        }
    }
}