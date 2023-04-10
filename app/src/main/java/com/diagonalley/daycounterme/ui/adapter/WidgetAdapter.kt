package com.diagonalley.daycounterme.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.diagonalley.daycounterme.databinding.AppWidgetWeddingBinding
import com.diagonalley.daycounterme.databinding.ItemBirthdayBinding
import com.diagonalley.daycounterme.databinding.ItemWeddingWidgetPreviewBinding
import com.diagonalley.daycounterme.global.WEDDING
import com.diagonalley.daycounterme.utils.load
import com.diagonalley.daycounterme.utils.setSingleClick

data class WidgetView(
    val widgetType: Int, val url: String, val title: String? = null, val subTitle: String? = null,
)

class WidgetAdapter constructor(
    private val onClick: (WidgetView) -> Unit,
) : ListAdapter<WidgetView, RecyclerView.ViewHolder>(WidgetDiffCallback()) {

    inner class WeddingViewHolder(
        private val binding: ItemWeddingWidgetPreviewBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: WidgetView) {
            binding.apply {
                root.setSingleClick {
                    onClick(item)
                }
//                imgBackground.load(item.url)
                executePendingBindings()
            }
        }
    }

    inner class BirthdayViewHolder(private val binding: ItemBirthdayBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: WidgetView) {
            binding.apply {
                root.setSingleClick {
                    onClick.invoke(item)
                }
                imgAvatar.load(item.url)
                executePendingBindings()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            WEDDING -> WeddingViewHolder(
                ItemWeddingWidgetPreviewBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )
            else -> {
                //BIRTHDAY
                BirthdayViewHolder(
                    ItemBirthdayBinding.inflate(
                        LayoutInflater.from(parent.context), parent, false
                    )
                )
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position).widgetType
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
//        val layoutParams = holder.itemView.layoutParams as StaggeredGridLayoutManager.LayoutParams
//        layoutParams.isFullSpan = true
        when (holder) {
            is WeddingViewHolder -> {
                holder.bind(getItem(position))
            }
            is BirthdayViewHolder -> {
                holder.bind(getItem(position))
            }
        }
    }
}

class WidgetDiffCallback : DiffUtil.ItemCallback<WidgetView>() {

    override fun areItemsTheSame(oldItem: WidgetView, newItem: WidgetView): Boolean {
        return oldItem.widgetType == newItem.widgetType
    }

    override fun areContentsTheSame(oldItem: WidgetView, newItem: WidgetView): Boolean {
        return oldItem.url == newItem.url
    }
}