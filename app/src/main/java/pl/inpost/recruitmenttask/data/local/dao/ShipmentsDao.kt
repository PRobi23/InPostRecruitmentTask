package pl.inpost.recruitmenttask.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import pl.inpost.recruitmenttask.data.local.entity.ShipmentEntity

/***
 * Shipments dao class.
 */
@Dao
interface ShipmentsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertShipments(shipments: List<ShipmentEntity>)

    @Query("SELECT * FROM shipments WHERE number=:number")
    suspend fun getShipmentByNumber(number: String): ShipmentEntity

    @Query("DELETE FROM shipments WHERE archived != 1")
    suspend fun deleteAllActiveShipments()

    @Query("SELECT * FROM shipments WHERE archived != 1")
    suspend fun getAllActiveShipments(): List<ShipmentEntity>

    @Query("SELECT * FROM shipments WHERE archived == 1")
    suspend fun getAllArchivedShipments(): List<ShipmentEntity>

    @Update
    suspend fun update(entity: ShipmentEntity)
}