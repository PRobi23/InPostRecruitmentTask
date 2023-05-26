package pl.inpost.recruitmenttask.presentation.shipmentList

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import pl.inpost.recruitmenttask.databinding.ShipmentItemBinding
import pl.inpost.recruitmenttask.domain.data.Shipment

class ShipmentAdapter() : RecyclerView.Adapter<ShipmentAdapter.ViewHolder>() {

    private val shipments: MutableList<Shipment> = mutableListOf()

    fun setItems(items: List<Shipment>) {
        shipments.clear()
        shipments.addAll(items)
        notifyDataSetChanged()
    }

    inner class ViewHolder(val binding: ShipmentItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ShipmentItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = shipments.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val shipment = shipments[position]

        with(holder.binding) {
            shipmentNumber.text = shipment.number
            status.text = shipment.status
        }
    }
}