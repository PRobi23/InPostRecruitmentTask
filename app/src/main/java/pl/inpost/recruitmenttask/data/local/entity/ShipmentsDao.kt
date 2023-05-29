package pl.inpost.recruitmenttask.data.local.entity

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

/***
 * Shipments dao class.
 */
@Dao
interface ShipmentsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertShipments(shipments: List<ShipmentEntity>)

    @Query("DELETE FROM shipments WHERE number=:number")
    suspend fun deleteShipmentByNumber(number: String)

    @Query("DELETE FROM shipments")
    suspend fun deleteAllShipments()

    @Query("SELECT * FROM shipments")
    suspend fun getAllShipments(): List<ShipmentEntity>
}