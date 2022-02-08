package xyz.ummo.user.data.dinerRoomData.dao

import androidx.room.*
import xyz.ummo.user.data.dinerRoomData.entities.ItemReview
import xyz.ummo.user.data.dinerRoomData.entities.Menu
import xyz.ummo.user.data.dinerRoomData.entities.Profile
import xyz.ummo.user.data.dinerRoomData.relations.oneToMany.MenuWithMenuItems
import xyz.ummo.user.data.dinerRoomData.relations.oneToOne.RetailOutletAndMenu

@Dao
interface ItemReviewDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItemReview(itemReview: ItemReview)

    @Transaction
    @Query("SELECT * FROM itemReview WHERE itemReviewId = :itemReviewId")
    suspend fun getItemReviewById(itemReviewId: String): ItemReview

    /** Selecting Profile info **/
    /*@Transaction *//** To ensure we don't have conflicting queries; thread-safe **//*
    @Query("SELECT * FROM menu WHERE retailOutletId = :retailOutletId")
    suspend fun getMenuWithRetailOutletId(retailOutletId: String): List<RetailOutletAndMenu>

    @Transaction *//** To ensure we don't have conflicting queries; thread-safe **//*
    @Query("SELECT * FROM menuItem WHERE menuId = :menuId")
    suspend fun getMenuItemsWithMenu(menuId: String): List<MenuWithMenuItems>*/

    @Update
    suspend fun updateItemReview(itemReview: ItemReview)

    @Delete
    suspend fun deleteItemReview(itemReview: ItemReview)
}