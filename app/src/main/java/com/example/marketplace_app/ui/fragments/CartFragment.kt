package com.example.marketplace_app.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.marketplace_app.data.viewModel.CartViewModel
import com.example.marketplace_app.data.viewModel.CartViewModelFactory
import com.example.marketplace_app.databinding.FragmentCartBinding
import com.example.marketplace_app.ui.MainApplication
import com.example.marketplace_app.ui.adapters.CartItemAdapter
import kotlinx.coroutines.launch

class CartFragment : Fragment() {

    private lateinit var binding: FragmentCartBinding
    private lateinit var cartItemAdapter: CartItemAdapter

    private val cartViewModel: CartViewModel by lazy {
        val cartRepository = mainApplication.cartRepository
        ViewModelProvider(this, CartViewModelFactory(cartRepository))[CartViewModel::class.java]
    }

    private val mainApplication: MainApplication by lazy {
        requireActivity().application as MainApplication
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
        lifecycleScope.launch {
            observeCartItems()
        }
    }

    private fun setupRecyclerView() {
        binding.recyclerViewCart.layoutManager = LinearLayoutManager(requireContext())
        cartItemAdapter = CartItemAdapter()
        binding.recyclerViewCart.adapter = cartItemAdapter
    }

    private suspend fun observeCartItems() {
        cartViewModel.state.collect { state ->
            Log.d("CartFragment", "Cart Items: ${state.cartItems}")
            cartItemAdapter.submitList(state.cartItems)
        }
    }
}
