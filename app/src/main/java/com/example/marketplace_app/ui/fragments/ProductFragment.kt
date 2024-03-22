package com.example.marketplace_app.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.marketplace_app.R
import com.example.marketplace_app.data.api.ProductApi
import com.example.marketplace_app.data.models.Product
import com.example.marketplace_app.databinding.FragmentProductBinding
import com.example.marketplace_app.data.repository.ProductRepository
import com.example.marketplace_app.ui.adapters.ImageCarouselAdapter
import com.example.marketplace_app.data.viewModel.ProductsViewModel
import com.example.marketplace_app.data.viewModel.ProductsViewModelFactory

class ProductFragment : Fragment(R.layout.fragment_product) {

    private lateinit var binding: FragmentProductBinding

    private val productViewModel: ProductsViewModel by lazy {
        val productRepository = ProductRepository(ProductApi.INSTANCE)
        ViewModelProvider(this, ProductsViewModelFactory(productRepository)).get(ProductsViewModel::class.java)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProductBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.backButton.setOnClickListener {
            view.findNavController().popBackStack()
        }
        initViews()
        loadProduct()
    }

    private fun initViews() {
        binding.imageCarousel.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
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
    }

    //TODO connect the Room and save the product to the cart, add one more fragmentCart
    private fun setAddToCartClickListener() {
        binding.fabAddToCart.setOnClickListener {

        }
    }

    private fun loadProduct() {

        val productId = arguments?.getLong("productId") ?: return
        productViewModel.loadProduct(productId)
        productViewModel.product.observe(viewLifecycleOwner) { product ->
            product?.let {
                setProductDetails(it)
                setCarouselImages(it.images)
                setAddToCartClickListener()
            }
        }
    }
}
