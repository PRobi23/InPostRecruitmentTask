package pl.inpost.recruitmenttask.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import pl.inpost.recruitmenttask.core.util.Constants

@Entity(tableName = Constants.SHIPMENT_TABLE_NAME)
data class ShipmentEntity(
     @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val number: String,
    val shipmentType: String,
    val status: String,
    val operationsHighlight: Boolean,
    val senderEmail: String?,
    val expiryDate: Long?,
    val storedDate: Long?,
    val pickUpDate: Long?,
    val archived: Boolean = false
)