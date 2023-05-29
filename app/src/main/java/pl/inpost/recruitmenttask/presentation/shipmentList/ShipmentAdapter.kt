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
import pl.inpost.recruitmenttask.domain.mapper.ShipmentEntityToShipmentMapper
import pl.inpost.recruitmenttask.domain.mapper.getDateType
import pl.inpost.recruitmenttask.presentation.shipmentList.ShipmentItem
import pl.inpost.recruitmenttask.presentation.shipmentList.ShipmentType

class ShipmentAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val shipmentItems: MutableList<ShipmentItem> = mutableListOf()

    fun setItems(data: List<ShipmentItem>) {
        shipmentItems.clear()
        shipmentItems.addAll(data)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        var count = 0
        shipmentItems.forEach { (_, items) ->
            count += 1 + items.size // Add 1 for the header
        }
        return count
    }

    override fun getItemViewType(position: Int): Int {
        var cumulativeItemCount = 0
        shipmentItems.forEachIndexed { _, (_, items) ->
            if (position == cumulativeItemCount) {
                return VIEW_TYPE_HEADER
            } else if (position > cumulativeItemCount && position <= cumulativeItemCount + items.size) {
                return VIEW_TYPE_SHIPMENT
            }
            cumulativeItemCount += items.size + 1 // Add 1 for the header
        }
        return VIEW_TYPE_UNKNOWN
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_HEADER -> {
                val binding = ShipmentHeaderBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                HeaderViewHolder(binding)
            }

            VIEW_TYPE_SHIPMENT -> {
                val binding = ShipmentItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                ShipmentElementViewHolder(binding)
            }

            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        var cumulativeItemCount = 0
        shipmentItems.forEachIndexed { index, (header, items) ->
            if (position == cumulativeItemCount) {
                val headerViewHolder = holder as HeaderViewHolder
                headerViewHolder.bind(header)
                return
            } else if (position > cumulativeItemCount && position <= cumulativeItemCount + items.size) {
                val itemIndex = position - cumulativeItemCount - 1 // Subtract 1 for the header
                val shipment = items[itemIndex]
                val shipmentViewHolder = holder as ShipmentElementViewHolder
                shipmentViewHolder.bind(shipment)
                return
            }
            cumulativeItemCount += items.size + 1 // Add 1 for the header
        }
    }

    // ...

    inner class HeaderViewHolder(val binding: ShipmentHeaderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(shipmentType: ShipmentType) {
            // Bind header data to the view
            binding.shipmentHeaderTitle.text = shipmentType.name
        }
    }

    inner class ShipmentElementViewHolder(val binding: ShipmentItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(shipment: Shipment) {
            val dateToShow =
                shipment.pickUpDate ?: shipment.expiryDate ?: shipment.storedDate

            with(binding) {
                shipmentNumberValue.text = shipment.number
                shipmentStatusText.text = shipment.status.name

                dateToShow?.let { dateToShow ->
                    val dateType = getDateType(
                        expiryDate = shipment.expiryDate,
                        pickUpDate = shipment.pickUpDate,
                        storedDate = shipment.storedDate
                    )

                    shipmentDateValue.visibility = View.VISIBLE
                    shipmentDateTitle.visibility = View.VISIBLE

                    shipmentDateValue.text = dateToShow.formatZonedDateTime()
                    shipmentIcon.setImageResource(R.drawable.ic_paczkomat)
                    shipmentDateTitle.text = when (dateType) {
                        DateType.EXPIRY -> itemView.context.getText(R.string.shipment_expiry_delivery)
                        DateType.STORED -> itemView.context.getText(R.string.shipment_stored_delivery)
                        DateType.PICK_UP -> itemView.context.getText(R.string.shipment_pick_up)
                        else -> ""
                    }

                } ?: run {
                    shipmentDateValue.visibility = View.GONE
                    shipmentDateTitle.visibility = View.GONE

                    shipmentIcon.setImageResource(R.drawable.ic_kurier)
                }

                shipmentSenderEmailValue.text =
                    shipment.senderEmail ?: itemView.context.getText(
                        R.string.unknown_sender_email
                    )
            }
        }
    }

    companion object {
        const val VIEW_TYPE_HEADER = 0
        const val VIEW_TYPE_SHIPMENT = 1
        const val VIEW_TYPE_UNKNOWN = 2
    }
}