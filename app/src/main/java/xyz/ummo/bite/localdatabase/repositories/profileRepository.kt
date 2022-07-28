package xyz.ummo.bite.localdatabase.repositories

import xyz.ummo.bite.localdatabase.Dao.ProfileDao
import xyz.ummo.bite.localdatabase.models.Profile

class profileRepository(private val profileDao: ProfileDao) {


    suspend fun addProfile (Profile: Profile){
        profileDao.addProfile(Profile)

    }

 suspend fun  getTopUser()=
    profileDao.getTopUser()


}