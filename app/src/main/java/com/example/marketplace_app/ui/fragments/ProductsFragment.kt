package com.example.marketplace_app.ui.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.marketplace_app.R
import com.example.marketplace_app.data.models.Product
import com.example.marketplace_app.databinding.FragmentProductsBinding
import com.example.marketplace_app.ui.adapters.CategoryAdapter
import com.example.marketplace_app.ui.adapters.ProductAdapter
import com.example.marketplace_app.data.viewModel.ProductsViewModel
import com.example.marketplace_app.data.viewModel.ProductsViewModelFactory
import com.example.marketplace_app.MainApplication
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
@AndroidEntryPoint
class ProductsFragment : Fragment() {

    private lateinit var binding: FragmentProductsBinding
    private lateinit var productAdapter: ProductAdapter
    private lateinit var categoryAdapter: CategoryAdapter
    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    private var isLoading = false

    private val viewModel: ProductsViewModel by lazy {
        val productRepository = mainApplication.productRepository
        ViewModelProvider(this, ProductsViewModelFactory(productRepository))[ProductsViewModel::class.java]
    }

    private val mainApplication: MainApplication by lazy {
        requireActivity().application as MainApplication
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProductsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        observeViewModel()
    }

    private fun initViews() {
        setupRecyclerViewCategory()
        setupRecyclerViewProducts()
        setupSearch()
    }

    private fun setupSearch() {
        binding.editTextSearch.addTextChangedListener(object : TextWatcher {
            private var searchJob: Job? = null

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                searchJob?.cancel()
                searchJob = coroutineScope.launch {
                    s?.let {
                        delay(500)
                        if (it.isEmpty()) {
                            viewModel.loadProducts()
                        } else {
                            viewModel.searchProducts(it.toString())
                        }
                    }
                }
            }
        })
    }

    private fun setupRecyclerViewCategory() {
        binding.apply {
            recyclerViewCategories.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            categoryAdapter = CategoryAdapter() { category ->
                loadByCategory(category)
            }
            recyclerViewCategories.adapter = categoryAdapter
        }
    }

    private fun setupRecyclerViewProducts() {
        binding.apply {
            val layoutManager = GridLayoutManager(requireContext(), 2)
            recyclerViewProducts.layoutManager = layoutManager
            productAdapter = ProductAdapter() { productId ->
                onProductClick(productId)
            }
            recyclerViewProducts.adapter = productAdapter

            recyclerViewProducts.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if (!isLoading && layoutManager.findLastCompletelyVisibleItemPosition() == productAdapter.itemCount - 1) {
                        loadMoreItems()
                    }
                }
            })
        }
    }

    private fun observeViewModel() {
        viewModel.apply {
            products.observe(viewLifecycleOwner) { products ->
                productAdapter.submitList(products as MutableList<Product>)
            }

            categories.observe(viewLifecycleOwner) { categories ->
                categoryAdapter.submitList(categories as MutableList<String>)
            }

            searchResults.observe(viewLifecycleOwner) { searchResults ->
                productAdapter.submitList(searchResults as MutableList<Product>)
            }

            isLoadingLiveData.observe(viewLifecycleOwner) { isLoading ->
                // handle loading state if needed
            }



            loadProducts()
            loadCategories()
        }
    }

    private fun loadMoreItems() {
        if (isLoading) return
        isLoading = true
        lifecycleScope.launch {
            viewModel.loadMoreProducts()
            isLoading = false
        }
    }

    private fun loadByCategory(category: String) {
        viewModel.loadProductsByCategory(category)
    }

    private fun onProductClick(productId : Long) {
        val bundle = bundleOf("productId" to productId)
        view?.findNavController()?.navigate(R.id.action_productsFragment_to_productFragment, bundle)
    }
}
