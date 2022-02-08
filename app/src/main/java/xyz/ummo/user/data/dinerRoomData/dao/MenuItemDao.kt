package xyz.ummo.user.data.dinerRoomData.dao

import androidx.room.*
import xyz.ummo.user.data.dinerRoomData.entities.MenuItem
import xyz.ummo.user.data.dinerRoomData.relations.oneToMany.MenuWithMenuItems

@Dao
interface MenuItemDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMenuItem(menuItem: MenuItem)

    @Transaction /** To ensure we don't have conflicting queries; thread-safe **/
    @Query("SELECT * FROM menuItem WHERE menuId = :menuId")
    suspend fun getMenuItemsWithMenuId(menuId: String): List<MenuWithMenuItems>

    @Update
    suspend fun updateMenuItems(menuItems: List<MenuItem>)

    @Query("DELETE FROM menuItem WHERE menuId = :menuId")
    suspend fun deleteMenuItems(menuId: String)
}