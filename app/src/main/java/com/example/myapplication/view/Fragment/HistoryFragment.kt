package com.example.myapplication.view.Fragment

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.Adapter.History.HistoryItemAdapter
import com.example.myapplication.Model.OrderHistory
import com.example.myapplication.databinding.FragmentHistoryBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class HistoryFragment : Fragment() {

    private lateinit var historyAdapter: HistoryItemAdapter
    private val orderList = mutableListOf<OrderHistory>()
    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!

    //    private lateinit var animatorSet: AnimatorSet
    private lateinit var animator: ObjectAnimator
    private lateinit var textAnimator: ObjectAnimator

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize RecyclerView from ViewBinding
//        val binding = FragmentHistoryBinding.bind(view)
        binding.historyView.layoutManager = LinearLayoutManager(requireContext())
        historyAdapter = HistoryItemAdapter(orderList)
        binding.historyView.adapter = historyAdapter

        // Fetch order history
        fetchOrderHistory()
    }

    private fun fetchOrderHistory() {

        showLoading()

        // Get the current user's UID
        val currentUserUid = FirebaseAuth.getInstance().currentUser?.uid
        if (currentUserUid == null) {
            Log.e("Firebase", "No authenticated user found")
            return
        }

        // Reference the user's orders in the database
        val database =
            FirebaseDatabase.getInstance().getReference("orderCompleted").child(currentUserUid)

        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                orderList.clear() // Clear the order list before adding new data

                // Iterate through all orders
                for (orderSnapshot in snapshot.children) {
                    // Get order details from snapshot
                    val orderId = orderSnapshot.key ?: ""
                    val amount = orderSnapshot.child("amount").getValue(String::class.java) ?: "0.0"
                    val itemsSnapshot = orderSnapshot.child("items")

                    // Initialize a list to store the items for this order
                    val items = mutableListOf<OrderHistory.Item>()

                    // Iterate through each item in the order
                    for (itemSnapshot in itemsSnapshot.children) {
                        val title = itemSnapshot.child("title").getValue(String::class.java) ?: ""
                        val price = itemSnapshot.child("price").getValue(Double::class.java) ?: 0.0
                        val totalEachItem =
                            itemSnapshot.child("totalEachItem").getValue(Double::class.java) ?: 0.0
                        val numberInCart =
                            itemSnapshot.child("numberInCart").getValue(Int::class.java) ?: 1

                        // Fetch the picUrl as ArrayList<String>
                        val picUrl = itemSnapshot.child("picUrl")
                            .getValue(object : GenericTypeIndicator<ArrayList<String>>() {})
                            ?: ArrayList()

                        // Add item to the list
                        items.add(
                            OrderHistory.Item(
                                title = title,
                                price = price,
                                picUrl = picUrl,
                                totalEachItem = totalEachItem,
                                numberInCart = numberInCart
                            )
                        )
                    }

                    // Add the order with its items to the order list, including totalAmount
                    orderList.add(
                        OrderHistory(
                            orderId = orderId,
                            totalAmount = amount,  // Add the amount to the order
                            items = items // Add items to the order
                        )
                    )
                }

                orderList.sortByDescending { it.orderId }
                // Notify the adapter that the data has been updated
                historyAdapter.notifyDataSetChanged()
                hideLoading()
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
                Log.e("Firebase", "Failed to fetch order history: ${error.message}")
                hideLoading()
            }
        })
    }

    /*private fun View.applyAnimation() {

        val bounce = ObjectAnimator.ofFloat(
            this,
            View.TRANSLATION_Y,
            0f, -35f, 0f
        ).apply {
            duration = 700
            repeatCount = ValueAnimator.INFINITE
            interpolator = AccelerateDecelerateInterpolator()
        }

        val scaleX = ObjectAnimator.ofFloat(
            this,
            View.SCALE_X,
            1f, 1.15f, 1f
        ).apply {
            duration = 700
            repeatCount = ValueAnimator.INFINITE
        }

        val scaleY = ObjectAnimator.ofFloat(
            this,
            View.SCALE_Y,
            1f, 1.15f, 1f
        ).apply {
            duration = 700
            repeatCount = ValueAnimator.INFINITE
        }

        animatorSet = AnimatorSet().apply {
            playTogether(bounce, scaleX, scaleY)
            start()
        }
    }*/

    private fun View.applyAnimation() {
        animator = ObjectAnimator.ofFloat(
            this,
            View.TRANSLATION_Y,
            0f, -30f, 0f
        ).apply {
            duration = 700
            repeatCount = ValueAnimator.INFINITE
            interpolator = AccelerateDecelerateInterpolator()
            start()
        }
    }
    private fun animateLoadingText() {

        val binding = _binding ?: return

        ObjectAnimator.ofFloat(
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

        binding.historyProgressBar.visibility = View.VISIBLE
        binding.loadingTxt.visibility = View.VISIBLE
        binding.historyView.visibility = View.GONE

        binding.historyProgressBar.applyAnimation()
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

        binding.historyProgressBar.apply {
            translationY = 0f
            scaleX = 1.2f
            scaleY = 1.2f
            visibility = View.GONE
        }
        binding.loadingTxt.apply {
            alpha = 1f
            visibility = View.GONE
        }

        binding.historyProgressBar.visibility = View.GONE
        binding.loadingTxt.visibility = View.GONE
        binding.historyView.visibility = View.VISIBLE
    }
}
