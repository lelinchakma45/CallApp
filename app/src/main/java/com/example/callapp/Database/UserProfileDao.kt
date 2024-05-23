package com.example.callapp.Database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.callapp.Model.PhoneLogin


@Dao
interface UserProfileDao {
    @Insert
    suspend fun insert(userProfile: PhoneLogin)

    @Update
    suspend fun update(userProfile: PhoneLogin)

    @Query("SELECT * FROM caller_app")
    fun getAllUserProfiles(): LiveData<List<PhoneLogin>>
}