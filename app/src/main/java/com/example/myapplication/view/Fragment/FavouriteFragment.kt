package com.example.myapplication.view.Fragment

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.Adapter.FavouriteAdapter
import com.example.myapplication.Model.FavoriteItem
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentFavouriteBinding
import com.example.myapplication.databinding.FragmentHistoryBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class FavouriteFragment : Fragment() {

    private lateinit var favRecyclerView: RecyclerView
    private lateinit var favouriteAdapter: FavouriteAdapter
    private lateinit var favouriteList: MutableList<FavoriteItem>
    private lateinit var favRef: DatabaseReference
    private var _binding: FragmentFavouriteBinding? = null
    private val binding get() = _binding!!

    //    private lateinit var animatorSet: AnimatorSet
    private lateinit var animator: ObjectAnimator
    private lateinit var textAnimator: ObjectAnimator

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        /*val rootView = inflater.inflate(R.layout.fragment_favourite, container, false)

        favRecyclerView = rootView.findViewById(R.id.favView)
        favRecyclerView.layoutManager = LinearLayoutManager(context)

        favouriteList = mutableListOf()
        favouriteAdapter = FavouriteAdapter(favouriteList)
        favRecyclerView.adapter = favouriteAdapter

        favRef = FirebaseDatabase.getInstance().getReference("favorites")

        fetchFavorites()

        return rootView*/
        _binding = FragmentFavouriteBinding.inflate(inflater, container, false)

        binding.favView.layoutManager = LinearLayoutManager(requireContext())

        favouriteList = mutableListOf()
        favouriteAdapter = FavouriteAdapter(favouriteList)
        binding.favView.adapter = favouriteAdapter

        favRef = FirebaseDatabase.getInstance().getReference("favorites")

        fetchFavorites()

        return binding.root
    }

    private fun fetchFavorites() {

        showLoading()

        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId != null) {
            val favRef = FirebaseDatabase.getInstance().getReference("users/$userId/favouriteItems")

            favRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    favouriteList.clear()  // Clear previous list

                    // Iterate through the children of the snapshot (favourite items)
                    for (favSnapshot in snapshot.children) {
                        val favoriteItem = favSnapshot.getValue(FavoriteItem::class.java)
                        if (favoriteItem != null) {
                            favouriteList.add(favoriteItem)  // Add the favorite item to the list
                        }
                    }

                    // Notify the adapter that the data has been updated
                    favouriteAdapter.notifyDataSetChanged()
                    hideLoading()
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle the error, for example, by logging it
                    hideLoading()
                    Log.e("Firebase", "Error fetching favorites: ${error.message}")
                }
            })
        } else {
            // Handle case when userId is null (not logged in)
            Log.e("Firebase", "User not logged in")
        }
    }

    private fun View.applyAnimation() {

        animator = ObjectAnimator.ofFloat(
            this,
            View.TRANSLATION_Y,
            0f,
            -25f,
            0f
        )

        animator.duration = 500
        animator.repeatCount = ObjectAnimator.INFINITE
        animator.repeatMode = ObjectAnimator.RESTART
        animator.start()
    }
    private fun animateLoadingText() {

        val binding = _binding ?: return

        textAnimator = ObjectAnimator.ofFloat(
            binding.loadingTxt,
            View.ALPHA,
            0.3f,
            1f,
            0.3f
        ).apply {
            duration = 900
            repeatCount = ValueAnimator.INFINITE
            repeatMode = ValueAnimator.RESTART
            start()
        }
    }

    private fun showLoading() {

        val binding = _binding ?: return

        binding.favouriteProgressBar.visibility = View.VISIBLE
        binding.loadingTxt.visibility = View.VISIBLE
        binding.favView.visibility = View.GONE

        binding.favouriteProgressBar.applyAnimation()
        animateLoadingText()
    }

    private fun hideLoading() {

        val binding = _binding ?: return

        if (::animator.isInitialized) {
            animator.cancel()
        }
        if (::textAnimator.isInitialized) {
            textAnimator.cancel()
        }

        binding.favouriteProgressBar.apply {
            translationY = 0f
            visibility = View.GONE
        }
        binding.loadingTxt.apply {
            visibility = View.GONE
            alpha = 1f
        }

        binding.favView.visibility = View.VISIBLE
    }


    override fun onDestroyView() {
        super.onDestroyView()

        if (::animator.isInitialized) {
            animator.cancel()
        }

        _binding = null
    }
}
