package com.example.marketplace_app.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.marketplace_app.data.api.ProductApi
import com.example.marketplace_app.data.local.CartDatabase
import com.example.marketplace_app.data.repository.ProductRepository
import com.example.marketplace_app.data.viewModel.ProductsViewModel
import com.example.marketplace_app.data.viewModel.ProductsViewModelFactory
import com.example.marketplace_app.databinding.FragmentCartBinding
import com.example.marketplace_app.ui.MainApplication
import com.example.marketplace_app.ui.adapters.CartItemAdapter

class CartFragment : Fragment() {

    private lateinit var binding: FragmentCartBinding
    private lateinit var cartItemAdapter: CartItemAdapter

    private val productViewModel: ProductsViewModel by lazy {
        val productRepository = mainApplication.repository
        ViewModelProvider(this, ProductsViewModelFactory(productRepository)).get(ProductsViewModel::class.java)
    }

    private val mainApplication: MainApplication by lazy {
        requireActivity().application as MainApplication
    }


    private val cartDatabase: CartDatabase by lazy {
        CartDatabase.create(requireContext())
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
        setupRecyclerView()
        loadCartItems()
        observeItems()
    }

    private fun observeItems() {
        productViewModel.cartItems.observe(viewLifecycleOwner) { cartItems ->
            Log.d("CartFragment", "Cart Items: $cartItems")
            cartItemAdapter.submitList(cartItems)
        }
    }

    private fun setupRecyclerView() {
        binding.recyclerViewCart.layoutManager = LinearLayoutManager(requireContext())
        cartItemAdapter = CartItemAdapter()
        binding.recyclerViewCart.adapter = cartItemAdapter
    }

    private fun loadCartItems() {
        productViewModel.getCartItems()
    }
}
