package xyz.ummo.user.data.dinerRoomData.dao

import androidx.room.*
import xyz.ummo.user.data.dinerRoomData.entities.RetailOutlet
import xyz.ummo.user.data.dinerRoomData.relations.oneToOne.RetailOutletAndMenu

@Dao
interface RetailOutletDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRetailOutlet(retailOutlet: RetailOutlet)

    @Transaction
    @Query("SELECT * FROM retailoutlet WHERE retailOutletId = :retailOutletId")
    suspend fun getRetailOutletWithId(retailOutletId: String): RetailOutlet

    @Transaction /** To ensure we don't have conflicting queries; thread-safe **/
    @Query("SELECT * FROM retailOutlet WHERE menuId = :menuId")
    suspend fun getRetailOutletWithMenu(menuId: String): List<RetailOutletAndMenu>

    @Update
    suspend fun updateRetailOutlet(retailOutlet: RetailOutlet)

    @Delete
    suspend fun deleteRetailOutlet(retailOutlet: RetailOutlet)
}