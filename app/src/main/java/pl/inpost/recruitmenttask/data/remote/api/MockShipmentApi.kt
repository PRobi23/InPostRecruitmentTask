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
) {

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
