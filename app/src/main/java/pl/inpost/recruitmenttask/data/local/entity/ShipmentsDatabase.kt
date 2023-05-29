package pl.inpost.recruitmenttask.data.local.entity

import androidx.room.Database
import androidx.room.RoomDatabase

/***
 * Favourite images database class.
 */
@Database(
    entities = [ShipmentEntity::class],
    version = 1,
    exportSchema = false
)
abstract class ShipmentsDatabase : RoomDatabase() {

    abstract val getShipmentDao: ShipmentsDao
}