package xyz.ummo.bite.localdatabase.Dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import xyz.ummo.bite.localdatabase.models.Profile
@Dao
interface ProfileDao {

    @Insert
        ( onConflict = OnConflictStrategy.REPLACE)
    suspend fun addProfile(userProfile: Profile)

//GET most recently added user's number
    @Query("SELECT *, MAX(userprofile_id) FROM profile")
    suspend fun getTopUser():Profile



}