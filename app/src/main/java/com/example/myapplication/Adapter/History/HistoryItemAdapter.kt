package com.example.myapplication.Adapter.History

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.Model.OrderHistory
import com.example.myapplication.databinding.ItemHistoryBinding

class HistoryItemAdapter(
    private val orderList: List<OrderHistory>
) : RecyclerView.Adapter<HistoryItemAdapter.ViewHolder>() {

    inner class ViewHolder(
        val binding: ItemHistoryBinding
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val binding = ItemHistoryBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )

        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = orderList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val order = orderList[position]

      /*  val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH)
        val timeFormat = SimpleDateFormat("hh:mm a", Locale.ENGLISH)

        holder.binding.txtDate.text =
            "${dateFormat.format(Date(order.currentTime))} • ${timeFormat.format(Date(order.currentTime))}"
*/

//        holder.binding.txtOrderId.text = order.orderId

        holder.binding.txtTotalAmount.text = order.totalAmount

        holder.binding.txtTotalQty.text = "Total Qty : ${order.items.sumOf { it.numberInCart }}"

        holder.binding.rvItems.layoutManager = LinearLayoutManager(holder.itemView.context)

        holder.binding.rvItems.adapter = HistoryProductAdapter(order.items)
    }
}