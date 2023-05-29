package pl.inpost.recruitmenttask.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import pl.inpost.recruitmenttask.data.local.dao.ShipmentsDao
import pl.inpost.recruitmenttask.data.local.entity.ShipmentEntity

/***
 * Favourite images database class.
 */
@Database(
    entities = [ShipmentEntity::class],
    version = 1,
    exportSchema = false
)
abstract class ShipmentsDatabase : RoomDatabase() {

    abstract val getShipmentsDao: ShipmentsDao
}