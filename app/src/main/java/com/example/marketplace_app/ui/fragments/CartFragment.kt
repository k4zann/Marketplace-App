package com.example.marketplace_app.ui.fragments

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.marketplace_app.data.models.CartEvent
import com.example.marketplace_app.data.models.Product
import com.example.marketplace_app.data.viewModel.CartViewModel
import com.example.marketplace_app.data.viewModel.CartViewModelFactory
import com.example.marketplace_app.databinding.FragmentCartBinding
import com.example.marketplace_app.data.repository.CartRepository
import com.example.marketplace_app.ui.adapters.CartItemAdapter
import com.example.marketplace_app.utils.repository.DataRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class CartFragment : Fragment() {

    private lateinit var binding: FragmentCartBinding
    private lateinit var cartItemAdapter: CartItemAdapter

    @Inject
    lateinit var cartRepository: CartRepository

    @Inject
    lateinit var dataRepository: DataRepository

    private val cartViewModel: CartViewModel by lazy {
        ViewModelProvider(this, CartViewModelFactory(cartRepository, dataRepository))[CartViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupSwipeRefreshLayout()
        setupRecyclerView()
        observeCartItems()
//        cartViewModel.loadAddedProducts()
    }


    private fun setupSwipeRefreshLayout() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            cartViewModel.onEvent(CartEvent.LoadCart)
            cartViewModel.saveDataAndFetch()
            binding.swipeRefreshLayout.isRefreshing = false
        }
    }
    private fun setupRecyclerView() {
        binding.recyclerViewCart.layoutManager = LinearLayoutManager(requireContext())
        cartItemAdapter = CartItemAdapter(
            onItemClick = { product ->
                showAlertDialog(product)
            }
        )
        binding.recyclerViewCart.adapter = cartItemAdapter
    }

    private fun showAlertDialog(product: Product) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Remove from Cart")
        builder.setMessage("Are you sure you want to remove ${product.name} from cart?")
        builder.setPositiveButton("Yes") { _, _ ->
            cartViewModel.onEvent(CartEvent.RemoveFromCart(product))
        }
        builder.setNegativeButton("No") { dialog, _ ->
            dialog.dismiss()
        }
        builder.create().show()
    }

    @SuppressLint("SetTextI18n")
    private fun observeCartItems() {
        cartViewModel.onEvent(CartEvent.LoadCart)
        lifecycleScope.launch {
            cartViewModel.state.collect { cartState ->
                cartItemAdapter.submitList(cartState.cartItems)
                val totalPrice = cartState.cartItems.sumOf { it.productPrice }
                binding.textViewTotalPrice.text = "Total Price: $${totalPrice}"
            }
        }
//        cartViewModel.addedProductLiveData.observe(viewLifecycleOwner, cartItemAdapter::submitList)
    }
}
