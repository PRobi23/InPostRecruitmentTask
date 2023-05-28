package pl.inpost.recruitmenttask.presentation.shipmentList

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import pl.inpost.recruitmenttask.databinding.ShipmentHeaderBinding

class ShipmentHeaderAdapter : RecyclerView.Adapter<ShipmentHeaderAdapter.HeaderViewHolder>() {

    private val shipmentTypes: MutableList<ShipmentType> = mutableListOf()

    fun setItems(items: List<ShipmentType>) {
        shipmentTypes.clear()
        shipmentTypes.addAll(items)
        notifyDataSetChanged()
    }

    inner class HeaderViewHolder(val binding: ShipmentHeaderBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HeaderViewHolder {
        val binding = ShipmentHeaderBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return HeaderViewHolder(binding)
    }

    override fun getItemCount(): Int = shipmentTypes.size

    override fun onBindViewHolder(holder: HeaderViewHolder, position: Int) {
        with(holder.binding) {
            shipmentHeaderTitle.text = "LÓFASZT"
        }
    }
}