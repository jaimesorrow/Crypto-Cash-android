package com.cryptocash.android.history

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.cryptocash.android.R
import com.cryptocash.android.databinding.ItemTransactionBinding

class TransactionAdapter(
    private val onClick: (TransactionItem) -> Unit
) : ListAdapter<TransactionItem, TransactionAdapter.VH>(DIFF) {

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<TransactionItem>() {
            override fun areItemsTheSame(a: TransactionItem, b: TransactionItem) = a.id == b.id
            override fun areContentsTheSame(a: TransactionItem, b: TransactionItem) = a == b
        }
    }

    inner class VH(private val binding: ItemTransactionBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: TransactionItem) {
            binding.ivTxIcon.setImageResource(item.iconRes)
            binding.tvTxTitle.text = item.title
            binding.tvTxSubtitle.text = item.subtitle
            binding.tvTxAmountCrypto.text = item.amountCrypto
            binding.tvTxAmountFiat.text = item.amountFiat
            binding.tvTxStatus.text = item.status

            val amountColor = if (item.isPositive) R.color.green_500 else R.color.red_500
            binding.tvTxAmountCrypto.setTextColor(
                ContextCompat.getColor(binding.root.context, amountColor)
            )

            // Accessibility: describe entire row for TalkBack
            binding.root.contentDescription = buildString {
                append(item.title)
                append(". ")
                append(item.amountCrypto)
                append(". ")
                append(item.amountFiat)
                append(". Status: ")
                append(item.status)
            }

            binding.root.setOnClickListener { onClick(item) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = ItemTransactionBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return VH(binding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) = holder.bind(getItem(position))
}
