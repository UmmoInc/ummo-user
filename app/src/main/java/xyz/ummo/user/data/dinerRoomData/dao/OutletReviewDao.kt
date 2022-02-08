package xyz.ummo.user.data.dinerRoomData.dao

import androidx.room.*
import xyz.ummo.user.data.dinerRoomData.entities.Menu
import xyz.ummo.user.data.dinerRoomData.entities.OutletReview
import xyz.ummo.user.data.dinerRoomData.entities.Profile
import xyz.ummo.user.data.dinerRoomData.relations.oneToMany.MenuWithMenuItems
import xyz.ummo.user.data.dinerRoomData.relations.oneToOne.RetailOutletAndMenu

@Dao
interface OutletReviewDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOutletReview(outletReview: OutletReview)

    @Transaction
    @Query("SELECT * FROM outletReview WHERE outletReviewId = :outletReviewId")
    suspend fun getOutletReviewById(outletReviewId: String): OutletReview

    /** Selecting Profile info **/
    /*@Transaction *//** To ensure we don't have conflicting queries; thread-safe **//*
    @Query("SELECT * FROM menu WHERE retailOutletId = :retailOutletId")
    suspend fun getMenuWithRetailOutletId(retailOutletId: String): List<RetailOutletAndMenu>

    @Transaction *//** To ensure we don't have conflicting queries; thread-safe **//*
    @Query("SELECT * FROM menuItem WHERE menuId = :menuId")
    suspend fun getMenuItemsWithMenu(menuId: String): List<MenuWithMenuItems>*/

    @Update
    suspend fun updateOutletReview(outletReview: OutletReview)

    @Delete
    suspend fun deleteOutletReview(outletReview: OutletReview)
}