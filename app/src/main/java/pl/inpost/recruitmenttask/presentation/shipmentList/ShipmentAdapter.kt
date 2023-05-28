package pl.inpost.recruitmenttask.presentation.shipmentList

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import pl.inpost.recruitmenttask.R
import pl.inpost.recruitmenttask.core.util.formatZonedDateTime
import pl.inpost.recruitmenttask.databinding.ShipmentHeaderBinding
import pl.inpost.recruitmenttask.databinding.ShipmentItemBinding
import pl.inpost.recruitmenttask.domain.data.DateType
import pl.inpost.recruitmenttask.domain.data.Shipment
import pl.inpost.recruitmenttask.domain.mapper.ShipmentDtoToShipmentMapper

class ShipmentAdapter : RecyclerView.Adapter<ShipmentAdapter.ShipmentElementViewHolder>() {

    private val shipments: MutableList<Shipment> = mutableListOf()

    fun setItems(items: List<Shipment>) {
        shipments.clear()
        shipments.addAll(items)
        notifyDataSetChanged()
    }

    inner class ShipmentElementViewHolder(val binding: ShipmentItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShipmentElementViewHolder {
        val binding =
            ShipmentItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ShipmentElementViewHolder(binding)
    }

    override fun getItemCount(): Int = shipments.size

    override fun onBindViewHolder(holder: ShipmentElementViewHolder, position: Int) {
        val shipment = shipments[position]
        val dateToShow =
            shipment.pickUpDate ?: shipment.expiryDate ?: shipment.storedDate

        with(holder.binding) {
            shipmentNumberValue.text = shipment.number
            shipmentStatusText.text = shipment.status.name

            dateToShow?.let { dateToShow ->
                val dateType = ShipmentDtoToShipmentMapper().getDateType(
                    expiryDate = shipment.expiryDate,
                    pickUpDate = shipment.pickUpDate,
                    storedDate = shipment.storedDate
                )

                shipmentDateValue.visibility = View.VISIBLE
                shipmentDateTitle.visibility = View.VISIBLE

                shipmentDateValue.text = dateToShow.formatZonedDateTime()
                shipmentIcon.setImageResource(R.drawable.ic_paczkomat)
                shipmentDateTitle.text = when (dateType) {
                    DateType.EXPIRY -> holder.itemView.context.getText(R.string.shipment_expiry_delivery)
                    DateType.STORED -> holder.itemView.context.getText(R.string.shipment_stored_delivery)
                    DateType.PICK_UP -> holder.itemView.context.getText(R.string.shipment_pick_up)
                    else -> ""
                }

            } ?: run {
                shipmentDateValue.visibility = View.GONE
                shipmentDateTitle.visibility = View.GONE

                shipmentIcon.setImageResource(R.drawable.ic_kurier)
            }

            shipmentSenderEmailValue.text =
                shipment.senderEmail ?: holder.itemView.context.getText(
                    R.string.unknown_sender_email
                )
        }
    }
}