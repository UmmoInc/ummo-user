package xyz.ummo.user.data.dinerRoomData.dao

import androidx.room.*
import xyz.ummo.user.data.dinerRoomData.entities.FavouriteItems
import xyz.ummo.user.data.dinerRoomData.entities.Menu
import xyz.ummo.user.data.dinerRoomData.entities.Profile
import xyz.ummo.user.data.dinerRoomData.relations.oneToMany.MenuWithMenuItems
import xyz.ummo.user.data.dinerRoomData.relations.oneToOne.RetailOutletAndMenu

@Dao
interface FavouriteItemsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavouriteItem(favouriteItems: FavouriteItems)

    @Transaction
    @Query("SELECT * FROM favouriteItems WHERE menuItemId = :favouriteItemId")
    suspend fun getFavouriteItemsById(favouriteItemId: String): FavouriteItems

    /** Selecting Profile info **/
    /*@Transaction *//** To ensure we don't have conflicting queries; thread-safe **//*
    @Query("SELECT * FROM menu WHERE retailOutletId = :retailOutletId")
    suspend fun getMenuWithRetailOutletId(retailOutletId: String): List<RetailOutletAndMenu>

    @Transaction *//** To ensure we don't have conflicting queries; thread-safe **//*
    @Query("SELECT * FROM menuItem WHERE menuId = :menuId")
    suspend fun getMenuItemsWithMenu(menuId: String): List<MenuWithMenuItems>*/

    @Update
    suspend fun updateFavouriteItems(favouriteItems: FavouriteItems)

    @Delete
    suspend fun deleteFavouriteItems(favouriteItems: FavouriteItems)
}