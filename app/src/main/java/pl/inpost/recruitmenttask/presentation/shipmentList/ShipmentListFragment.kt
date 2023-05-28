package pl.inpost.recruitmenttask.presentation.shipmentList

import android.os.Bundle
import android.view.*
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import pl.inpost.recruitmenttask.R
import pl.inpost.recruitmenttask.databinding.FragmentShipmentListBinding
import pl.inpost.recruitmenttask.databinding.ShipmentItemBinding


@AndroidEntryPoint
class ShipmentListFragment : Fragment() {

    private val viewModel: ShipmentListViewModel by viewModels()
    private var _binding: FragmentShipmentListBinding? = null
    private val shipmentAdapter by lazy {
        ShipmentAdapter()
    }
    private val shipmentHeaderAdapter by lazy {
        ShipmentHeaderAdapter()
    }
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentShipmentListBinding.inflate(inflater, container, false)
        addCustomMenu()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.shipmentsView.apply {
            adapter = ConcatAdapter(shipmentHeaderAdapter, shipmentAdapter)
            addItemDecoration(SpacingItemDecoration(resources.getDimensionPixelSize(R.dimen.item_spacing)))
        }
        binding.swipeRefresh.setOnRefreshListener {
            viewModel.refreshData()
        }

        viewModel.viewState.observe(requireActivity()) { shipmentItems ->
            if (shipmentItems.isEmpty()) {
                binding.emptyShipmentsText.visibility = View.VISIBLE
            } else {
                binding.emptyShipmentsText.visibility = View.GONE

                val headerItems = shipmentItems.map { it.shipmentType }
                val shipmentItemsList = shipmentItems.flatMap { it.shipments }

                shipmentHeaderAdapter.setItems(headerItems)
                shipmentAdapter.setItems(shipmentItemsList)
            }
            binding.swipeRefresh.isRefreshing = false
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun addCustomMenu() {
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.shipment_list_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.readyToPickupShipmentsMenu -> {
                        true
                    }

                    R.id.otherShipmentsMenu -> {
                        true
                    }

                    R.id.allShipmentsMenu -> {
                        true
                    }

                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }
}
