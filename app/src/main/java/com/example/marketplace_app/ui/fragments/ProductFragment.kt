package com.example.marketplace_app.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.marketplace_app.R
import com.example.marketplace_app.api.ProductApi
import com.example.marketplace_app.data.Product
import com.example.marketplace_app.databinding.FragmentProductBinding
import com.example.marketplace_app.repository.ProductRepository
import com.example.marketplace_app.ui.adapters.ImageCarouselAdapter
import com.example.marketplace_app.viewModel.ProductsViewModel
import com.example.marketplace_app.viewModel.ProductsViewModelFactory

class ProductFragment : Fragment(R.layout.fragment_product) {

    private lateinit var binding: FragmentProductBinding
    private val productRepository = ProductRepository(ProductApi.INSTANCE)
    private lateinit var productViewModel: ProductsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProductBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        productViewModel = ViewModelProvider(this, ProductsViewModelFactory(productRepository))
            .get(ProductsViewModel::class.java)

        binding.backButton.setOnClickListener {
            requireActivity().onBackPressed()
        }

        loadProduct()
    }


    @SuppressLint("SetTextI18n")
    private fun setProductDetails(product: Product) {
        binding.productNameValue.text = product.name
        binding.productDescriptionText.text = product.description
        binding.productPriceValue.text = "${product.productPrice}$"
        binding.productCategoryValue.text = "Category: ${product.category.capitalize()}"
        binding.productRatingValue.text = "Rating: ${product.rating}"
        binding.productStockValue.text = "Stock: ${product.stock}"
        binding.productBrandValue.text = "Brand: ${product.brand}"
    }

    private fun setCarouselImages(images: List<String>) {
        val adapter = ImageCarouselAdapter(images)
        binding.imageCarousel.adapter = adapter
        binding.imageCarousel.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
    }

    private fun setAddToCartClickListener() {
        binding.fabAddToCart.setOnClickListener {
            // Handle click event
        }
    }

    private fun loadProduct() {
        productViewModel.product.observe(viewLifecycleOwner) { product ->
            Log.d("ProductFragment", "Product: $product")
            setProductDetails(product)
            setCarouselImages(product.images)
            setAddToCartClickListener()
        }
    }
}
