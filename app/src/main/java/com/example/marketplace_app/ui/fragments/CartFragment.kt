package com.example.marketplace_app.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.marketplace_app.data.local.CartDatabase
import com.example.marketplace_app.databinding.FragmentCartBinding
import com.example.marketplace_app.ui.adapters.CartItemAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CartFragment : Fragment() {

    private lateinit var binding: FragmentCartBinding
    private lateinit var cartItemAdapter: CartItemAdapter
    private lateinit var cartDatabase: CartDatabase


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
    }

    private fun setupRecyclerView() {
        binding.recyclerViewCart.layoutManager = LinearLayoutManager(requireContext())
        cartItemAdapter = CartItemAdapter()
        binding.recyclerViewCart.adapter = cartItemAdapter
    }

    private fun loadCartItems() {
        GlobalScope.launch(Dispatchers.Main) {
            val cartItems = withContext(Dispatchers.IO) {

                cartDatabase = CartDatabase.getInstance(requireContext())
                cartDatabase.cartItemDao().getAllCartItems()
            }
            cartItemAdapter.submitList(cartItems)
        }
    }
}
