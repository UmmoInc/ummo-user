package xyz.ummo.user.data.dinerRoomData.dao

import androidx.room.*
import xyz.ummo.user.data.dinerRoomData.entities.Menu
import xyz.ummo.user.data.dinerRoomData.entities.Order
import xyz.ummo.user.data.dinerRoomData.entities.Profile
import xyz.ummo.user.data.dinerRoomData.relations.oneToMany.MenuWithMenuItems
import xyz.ummo.user.data.dinerRoomData.relations.oneToOne.RetailOutletAndMenu

@Dao
interface OrderDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrder(order: Order)

    @Transaction
    @Query("SELECT * FROM `order` WHERE orderId = :orderId")
    suspend fun getOrderById(orderId: String): Order

    /** Selecting Cart info **/
    /*@Transaction *//** To ensure we don't have conflicting queries; thread-safe **//*
    @Query("SELECT * FROM menu WHERE retailOutletId = :retailOutletId")
    suspend fun getMenuWithRetailOutletId(retailOutletId: String): List<RetailOutletAndMenu>

    @Transaction *//** To ensure we don't have conflicting queries; thread-safe **//*
    @Query("SELECT * FROM menuItem WHERE menuId = :menuId")
    suspend fun getMenuItemsWithMenu(menuId: String): List<MenuWithMenuItems>*/

    @Update
    suspend fun updateCart(order: Order)

    @Delete
    suspend fun deleteOrder(order: Order)
}