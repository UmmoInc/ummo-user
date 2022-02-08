package xyz.ummo.user.data.dinerRoomData.dao

import androidx.room.*
import xyz.ummo.user.data.dinerRoomData.entities.Profile

@Dao
interface ProfileDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProfile(profile: Profile)

    /** Selecting Profile info **/
    /*@Transaction *//** To ensure we don't have conflicting queries; thread-safe **//*
    @Query("SELECT * FROM menu WHERE retailOutletId = :retailOutletId")
    suspend fun getMenuWithRetailOutletId(retailOutletId: String): List<RetailOutletAndMenu>

    @Transaction *//** To ensure we don't have conflicting queries; thread-safe **//*
    @Query("SELECT * FROM menuItem WHERE menuId = :menuId")
    suspend fun getMenuItemsWithMenu(menuId: String): List<MenuWithMenuItems>*/

    /** Reading the Profile by contact info **/
    @Transaction
    @Query("SELECT * FROM Profile WHERE contact = :contact")
    suspend fun getProfile(contact: String): Profile

    /** Searching for a Favourite Outlet based on the Favourite Outlet ID **/
//    suspend fun getFavouriteOutletInfoByID(favouriteOutletId: String):

    @Update
    suspend fun updateProfile(profile: Profile)

    @Delete
    suspend fun deleteProfile(profile: Profile)
}