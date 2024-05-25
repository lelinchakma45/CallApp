package com.example.callapp.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.callapp.Database.UserDatabase
import com.example.callapp.Model.PhoneLogin
import com.example.callapp.Repository.UserProfileRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UserProfileViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: UserProfileRepository
    private val _user = MutableLiveData<PhoneLogin?>()
    val user: LiveData<PhoneLogin?> = _user

    private val _insertionStatus = MutableLiveData<Boolean>()
    val insertionStatus: LiveData<Boolean> = _insertionStatus

    init {
        val userProfileDao = UserDatabase.getDatabase(application).userProfileDao()
        repository = UserProfileRepository(userProfileDao)
    }

    fun getUserProfiles(): LiveData<List<PhoneLogin>> {
        return repository.getUserProfiles()
    }

    fun insertUserProfile(userProfile: PhoneLogin) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repository.insert(userProfile)
                _insertionStatus.postValue(true) // Insertion successful
            } catch (e: Exception) {
                _insertionStatus.postValue(false) // Insertion failed
            }
        }
    }

    fun updateUserProfile(userProfile: PhoneLogin) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.update(userProfile)
        }
    }

    fun fetchUserById(uid: Int) {
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                repository.getUserById(uid)
            }
            _user.value = result
        }
    }
}