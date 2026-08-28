package com.cryptocash.android.payment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.cryptocash.android.R
import com.cryptocash.android.databinding.ItemPaymentMethodBinding

class PaymentMethodAdapter(
    private val onSelect: (PaymentMethod) -> Unit,
    private val onDelete: (PaymentMethod) -> Unit
) : ListAdapter<PaymentMethod, PaymentMethodAdapter.VH>(DIFF) {

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<PaymentMethod>() {
            override fun areItemsTheSame(a: PaymentMethod, b: PaymentMethod) = a.id == b.id
            override fun areContentsTheSame(a: PaymentMethod, b: PaymentMethod) = a == b
        }
    }

    inner class VH(private val binding: ItemPaymentMethodBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: PaymentMethod) {
            binding.ivPaymentIcon.setImageResource(item.iconRes)
            binding.tvPaymentLabel.text = item.label
            binding.tvPaymentSubLabel.text = item.subLabel
            binding.ivDefault.visibility = if (item.isDefault) View.VISIBLE else View.GONE

            binding.root.contentDescription = buildString {
                append(item.label)
                append(", ")
                append(item.subLabel)
                if (item.isDefault) append(", default")
            }

            binding.root.setOnClickListener { onSelect(item) }
            binding.btnDelete.setOnClickListener { onDelete(item) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = ItemPaymentMethodBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return VH(binding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) = holder.bind(getItem(position))
}
