package pl.inpost.recruitmenttask.data.remote.api

import android.content.Context
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.delay
import pl.inpost.recruitmenttask.R
import pl.inpost.recruitmenttask.data.remote.ApiTypeAdapter
import pl.inpost.recruitmenttask.data.remote.dto.CustomerNetworkDTO
import pl.inpost.recruitmenttask.data.remote.dto.EventLogNetworkDTO
import pl.inpost.recruitmenttask.data.remote.dto.OperationsNetworkDTO
import pl.inpost.recruitmenttask.data.remote.dto.ShipmentDTO
import pl.inpost.recruitmenttask.data.remote.dto.ShipmentStatus
import pl.inpost.recruitmenttask.data.remote.dto.ShipmentType
import pl.inpost.recruitmenttask.data.remote.dto.ShipmentsResponse
import pl.inpost.recruitmenttask.domain.repository.ShipmentRepository
import java.time.ZonedDateTime
import kotlin.random.Random

class MockShipmentApi(
    @ApplicationContext private val context: Context,
    apiTypeAdapter: ApiTypeAdapter
)  {

    val response by lazy {
        val json = context.resources.openRawResource(R.raw.mock_shipment_api_response)
            .bufferedReader()
            .use { it.readText() }

        val jsonAdapter = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .add(apiTypeAdapter)
            .build()
            .adapter(ShipmentsResponse::class.java)

        jsonAdapter.fromJson(json) as ShipmentsResponse
    }
}

private fun mockShipmentNetwork(
    number: String = Random.nextLong(1, 9999_9999_9999_9999).toString(),
    type: ShipmentType = ShipmentType.PARCEL_LOCKER,
    status: ShipmentStatus = ShipmentStatus.DELIVERED,
    sender: CustomerNetworkDTO? = mockCustomerNetwork(),
    receiver: CustomerNetworkDTO? = mockCustomerNetwork(),
    operations: OperationsNetworkDTO = mockOperationsNetwork(),
    eventLog: List<EventLogNetworkDTO> = emptyList(),
    openCode: String? = null,
    expireDate: ZonedDateTime? = null,
    storedDate: ZonedDateTime? = null,
    pickupDate: ZonedDateTime? = null
) = ShipmentDTO(
    number = number,
    shipmentType = type.name,
    status = status.name,
    eventLog = eventLog,
    openCode = openCode,
    expiryDate = expireDate,
    storedDate = storedDate,
    pickUpDate = pickupDate,
    receiver = receiver,
    sender = sender,
    operations = operations
)

private fun mockCustomerNetwork(
    email: String = "name@email.com",
    phoneNumber: String = "123 123 123",
    name: String = "Jan Kowalski"
) = CustomerNetworkDTO(
    email = email,
    phoneNumber = phoneNumber,
    name = name
)

private fun mockOperationsNetwork(
    manualArchive: Boolean = false,
    delete: Boolean = false,
    collect: Boolean = false,
    highlight: Boolean = false,
    expandAvizo: Boolean = false,
    endOfWeekCollection: Boolean = false
) = OperationsNetworkDTO(
    manualArchive = manualArchive,
    delete = delete,
    collect = collect,
    highlight = highlight,
    expandAvizo = expandAvizo,
    endOfWeekCollection = endOfWeekCollection
)
