package com.example.myapplication.view.Fragment

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.view.Activity.SearchActivity
import com.example.myapplication.Adapter.CategoryAdapter
import com.example.myapplication.Adapter.PopularAdapter
import com.example.myapplication.Adapter.OfferAdapter
import com.example.myapplication.Utils.Utility
import com.example.myapplication.ViewModel.MainViewModel
import com.example.myapplication.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
//        binding.greetingText.text = Utility.getGreeting()
//        binding.textView4.text = Utility.getGreetingMessage()

        initCategory()
        initPopular()
        initOffers()
        initBrew()

        binding.search.setOnClickListener {
            startActivity(Intent(requireContext(), SearchActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        greetingRunnable.run()
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(greetingRunnable)
    }

    private val handler = Handler(Looper.getMainLooper())

    private val greetingRunnable = object : Runnable {
        override fun run() {
            _binding?.let {
                it.greetingText.text = Utility.getGreeting()
                it.textView4.text = Utility.getGreetingMessage()
            }

            handler.postDelayed(this, 60_000) // Every 1 minute
        }
    }

    private fun initCategory() {
        showCategoryShimmer()
        viewModel.category.observe(viewLifecycleOwner) { categoryList ->
            binding.recyclerViewCategory.layoutManager = LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.HORIZONTAL,
                false
            )
            binding.recyclerViewCategory.adapter = CategoryAdapter(categoryList)
            hideCategoryShimmer()
        }
        viewModel.loadCategory()
    }

    private fun initPopular() {
        showPopularShimmer()
        viewModel.popular.observe(viewLifecycleOwner) {
            binding.recyclerViewPopular.layoutManager = LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.HORIZONTAL,
                false
            )
            binding.recyclerViewPopular.adapter = PopularAdapter(it)
            hidePopularShimmer()
        }
        viewModel.loadPopular()
    }

    private fun initBrew() {
        showBrewShimmer()
        viewModel.brewBased.observe(viewLifecycleOwner) {
            binding.recyclerViewBrewBased.layoutManager = LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.HORIZONTAL,
                false
            )
            binding.recyclerViewBrewBased.adapter = PopularAdapter(it)
            hideBrewShimmer()
        }
        viewModel.loadbrewBased()
    }

    private fun initOffers() {
//        binding.progressBarOffer.visibility = View.VISIBLE
        showOfferShimmer()
        viewModel.offer.observe(viewLifecycleOwner) {
            binding.recyclerViewOffer.layoutManager = LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.HORIZONTAL,
                false
            )
            binding.recyclerViewOffer.adapter = OfferAdapter(it)
//            binding.progressBarOffer.visibility = View.GONE
            hideOfferShimmer()
        }
        viewModel.loadOffer()
    }

    private fun showCategoryShimmer() {
        binding.shimmerCategory.visibility = View.VISIBLE
        binding.shimmerCategory.startShimmer()
        binding.recyclerViewCategory.visibility = View.GONE
    }

    private fun hideCategoryShimmer() {
        binding.shimmerCategory.stopShimmer()
        binding.shimmerCategory.visibility = View.GONE
        binding.recyclerViewCategory.visibility = View.VISIBLE
    }

    private fun showPopularShimmer() {
        binding.shimmerPopular.visibility = View.VISIBLE
        binding.shimmerPopular.startShimmer()
        binding.recyclerViewPopular.visibility = View.GONE
    }

    private fun hidePopularShimmer() {
        binding.shimmerPopular.stopShimmer()
        binding.shimmerPopular.visibility = View.GONE
        binding.recyclerViewPopular.visibility = View.VISIBLE
    }
    private fun showBrewShimmer() {
        binding.shimmerBrew.visibility = View.VISIBLE
        binding.shimmerBrew.startShimmer()
        binding.recyclerViewBrewBased.visibility = View.GONE
    }

    private fun hideBrewShimmer() {
        binding.shimmerBrew.stopShimmer()
        binding.shimmerBrew.visibility = View.GONE
        binding.recyclerViewBrewBased.visibility = View.VISIBLE
    }
    private fun showOfferShimmer() {
        binding.shimmerOffer.visibility = View.VISIBLE
        binding.shimmerOffer.startShimmer()
        binding.recyclerViewOffer.visibility = View.GONE
    }

    private fun hideOfferShimmer() {
        binding.shimmerOffer.stopShimmer()
        binding.shimmerOffer.visibility = View.GONE
        binding.recyclerViewOffer.visibility = View.VISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        handler.removeCallbacks(greetingRunnable)
        _binding = null
    }
}
