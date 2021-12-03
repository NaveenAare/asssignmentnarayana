package com.narayanagroup.assiginment.viewmodels


import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import com.narayanagroup.assiginment.data.repository.MyRepository
import com.narayanagroup.assiginment.models.items
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: MyRepository
) : ViewModel() {
    private var currentResult: Flow<PagingData<items>>? = null

    @ExperimentalPagingApi
    suspend fun searchPlayers(s:String, net:Boolean): Flow<PagingData<items>> {
        val newResult: Flow<PagingData<items>> =
            repository.getPlayers(s,net).cachedIn(viewModelScope)
        currentResult = newResult
       return newResult
    }

    /**
     * Same thing but with Livedata
     */
    private var currentResultLiveData: LiveData<PagingData<items>>? = null

    fun searchPlayersLiveData(s:String): LiveData<PagingData<items>> {
        val newResultLiveData: LiveData<PagingData<items>> =
            repository.                                                                                                                                             getPlayersLiveData(s).cachedIn(viewModelScope)
        currentResultLiveData = newResultLiveData
        return newResultLiveData
    }


}