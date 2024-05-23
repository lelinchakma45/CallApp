package com.example.callapp.Repository

import androidx.lifecycle.LiveData
import com.example.callapp.Database.UserProfileDao
import com.example.callapp.Model.PhoneLogin

class UserProfileRepository(private val userProfileDao: UserProfileDao) {
    fun getUserProfiles(): LiveData<List<PhoneLogin>>{
        return userProfileDao.getAllUserProfiles()
    }

    suspend fun insert(userProfile: PhoneLogin) {
        userProfileDao.insert(userProfile)
    }

    suspend fun update(userProfile: PhoneLogin) {
        userProfileDao.update(userProfile)
    }
}