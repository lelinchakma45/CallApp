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


class UserProfileViewModel(application: Application): AndroidViewModel(application) {
    private val repository: UserProfileRepository
    init {
        val userProfileDao = UserDatabase.getDatabase(application).userProfileDao()
        repository = UserProfileRepository(userProfileDao)
    }

    fun getUserProfiles(): LiveData<List<PhoneLogin>> {
        return repository.getUserProfiles()
    }


    fun insertUserProfile(userProfile: PhoneLogin) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insert(userProfile)
        }
    }

    fun updateUserProfile(userProfile: PhoneLogin) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.update(userProfile)
        }
    }
}