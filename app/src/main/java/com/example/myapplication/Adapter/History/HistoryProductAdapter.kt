package com.example.myapplication.Adapter.History

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.myapplication.Model.OrderHistory
import com.example.myapplication.databinding.ItemHistoryProductBinding

class HistoryProductAdapter(
    private val itemList: List<OrderHistory.Item>
) : RecyclerView.Adapter<HistoryProductAdapter.ViewHolder>() {

    inner class ViewHolder(
        val binding: ItemHistoryProductBinding
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            ItemHistoryProductBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            )
        )
    }

    override fun getItemCount() = itemList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = itemList[position]

        holder.binding.txtTitle.text = item.title

        holder.binding.txtPrice.text = " ₹${item.totalEachItem}"
//            "₹${item.price} × ${item.numberInCart} = ₹${item.totalEachItem}"

        holder.binding.txtQty.text = "Qty : ${item.numberInCart}"

        if (item.picUrl.isNotEmpty()) {
            Glide.with(holder.itemView)
                .load(item.picUrl[0])
                .apply(RequestOptions().centerCrop())
                .into(holder.binding.imgProduct)
        }
    }
}