package com.example.marketplace_app.ui.fragments

import android.os.Bundle
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
import com.example.marketplace_app.ui.adapters.CartItemAdapter

class CartFragment : Fragment() {

    private lateinit var binding: FragmentCartBinding
    private lateinit var cartItemAdapter: CartItemAdapter
    private lateinit var cartDatabase: CartDatabase

    private val productViewModel: ProductsViewModel by lazy {
        val productRepository = ProductRepository(ProductApi.INSTANCE, cartDatabase.cartItemDao())
        ViewModelProvider(this, ProductsViewModelFactory(productRepository)).get(ProductsViewModel::class.java)
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
            cartItemAdapter.submitList(cartItems)
        }
    }

    private fun setupRecyclerView() {
        binding.recyclerViewCart.layoutManager = LinearLayoutManager(requireContext())
        cartItemAdapter = CartItemAdapter()
        binding.recyclerViewCart.adapter = cartItemAdapter
    }

    private fun loadCartItems() {
        cartDatabase = CartDatabase.create(requireContext())
        productViewModel.getCartItems()
    }
}
