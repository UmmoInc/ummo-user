package xyz.ummo.user.data.dinerRoomData.dao

import androidx.room.*
import xyz.ummo.user.data.dinerRoomData.entities.FavouriteOutlets
import xyz.ummo.user.data.dinerRoomData.entities.Menu
import xyz.ummo.user.data.dinerRoomData.entities.Profile
import xyz.ummo.user.data.dinerRoomData.relations.oneToMany.MenuWithMenuItems
import xyz.ummo.user.data.dinerRoomData.relations.oneToOne.RetailOutletAndMenu

@Dao
interface FavouriteOutletDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavouriteOutlet(favouriteOutlets: FavouriteOutlets)

    @Transaction
    @Query("SELECT * FROM favouriteOutlets WHERE retailOutletId = :favouriteOutletId")
    suspend fun getFavouriteOutletById(favouriteOutletId: String): FavouriteOutlets

    /** Selecting Favourite Outlet info **/
    /*@Transaction *//** To ensure we don't have conflicting queries; thread-safe **//*
    @Query("SELECT * FROM menu WHERE retailOutletId = :retailOutletId")
    suspend fun getMenuWithRetailOutletId(retailOutletId: String): List<RetailOutletAndMenu>

    @Transaction *//** To ensure we don't have conflicting queries; thread-safe **//*
    @Query("SELECT * FROM menuItem WHERE menuId = :menuId")
    suspend fun getMenuItemsWithMenu(menuId: String): List<MenuWithMenuItems>*/

    @Update /** There won't be a use-case whereby the User updates a Favourite Outlet entry. **/
    suspend fun updateFavouriteOutlet(favouriteOutlets: FavouriteOutlets)

    @Delete
    suspend fun deleteFavouriteOutlet(favouriteOutlets: FavouriteOutlets)
}