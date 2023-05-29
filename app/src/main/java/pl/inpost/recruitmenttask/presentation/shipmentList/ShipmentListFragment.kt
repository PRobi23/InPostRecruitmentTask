package pl.inpost.recruitmenttask.presentation.shipmentList

import ShipmentAdapter
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.ConcatAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import pl.inpost.recruitmenttask.R
import pl.inpost.recruitmenttask.databinding.FragmentShipmentListBinding


@AndroidEntryPoint
class ShipmentListFragment : Fragment() {

    private val viewModel: ShipmentListViewModel by viewModels()
    private var _binding: FragmentShipmentListBinding? = null
    private val shipmentAdapter by lazy {
        ShipmentAdapter(viewModel::archiveShipment)
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
            adapter = ConcatAdapter(shipmentAdapter)
            addItemDecoration(SpacingItemDecoration(resources.getDimensionPixelSize(R.dimen.item_spacing)))
        }
        binding.swipeRefresh.setOnRefreshListener {
            viewModel.refreshData()
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.shipmentListState.collect { shipmentListState ->
                    binding.swipeRefresh.isRefreshing = shipmentListState.isLoading

                    if (!shipmentListState.isLoading) {
                        showError(shipmentListState)
                        showListItems(shipmentListState)
                    }
                }
            }
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


    private fun showListItems(shipmentListState: ShipmentListState) {
        shipmentListState.shipmentItems?.let { shipmentItems ->
            if (shipmentItems.isEmpty() && !shipmentListState.isLoading) {
                binding.emptyShipmentsText.visibility = View.VISIBLE
            } else {
                binding.emptyShipmentsText.visibility = View.GONE
                shipmentAdapter.setItems(shipmentItems)
            }
            binding.swipeRefresh.isRefreshing = false
        }
    }

    private fun showError(shipmentListState: ShipmentListState) {
        shipmentListState.error?.let {
            Toast.makeText(
                requireContext(),
                it.asString(requireContext()),
                Toast.LENGTH_LONG
            ).show()
        }
    }
}
