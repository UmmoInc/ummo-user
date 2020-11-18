package xyz.ummo.user.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import xyz.ummo.user.data.entity.ServiceEntity

@Dao
interface ServiceDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertService(serviceEntity: ServiceEntity)

    @get:Query("SELECT * FROM service")
    val serviceLiveData: LiveData<ServiceEntity?>?

    @get:Query("SELECT * FROM service")
    val serviceListData: List<ServiceEntity>

    @Query("SELECT * FROM service WHERE service_id = :serviceId ")
    fun getServiceLiveDataById(serviceId: String?): LiveData<ServiceEntity>

    @Update
    fun updateService(serviceEntity: ServiceEntity?)

    @Query("DELETE FROM service")
    fun deleteServices()
}