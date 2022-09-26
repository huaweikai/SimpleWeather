package com.hua.simpleweather.db.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.hua.simpleweather.db.dao.bean.LocalCity

/**
 * @author : huaweikai
 * @Date   : 2021/12/22
 * @Desc   : dao
 */
@Dao
interface CityDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCity(localCity: LocalCity)

    @Query("select * from localcity where cityName like :query")
    suspend fun selectCity(query: String):List<LocalCity>

    @Query("select * from localcity where cityName like :query")
    fun selectPagingCity(query: String): PagingSource<Int,LocalCity>

}