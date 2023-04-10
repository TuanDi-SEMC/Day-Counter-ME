package com.diagonalley.daycounterme.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.diagonalley.daycounterme.databinding.ItemSignInBinding
import com.diagonalley.daycounterme.utils.setSingleClick

enum class SignIn {
    FACEBOOK, GOOGLE, PHONE_NUMBER
}

data class SignInView(
    val signIn: SignIn,
    @DrawableRes val drawableRes: Int,
    @StringRes val stringRes: Int,
)

class SignInAdapter constructor(
    private val onClick: (SignInView) -> Unit,
) :
    ListAdapter<SignInView, SignInAdapter.SignInViewHolder>(FeatureDiffCallback()) {

    inner class SignInViewHolder(
        private val binding: ItemSignInBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: SignInView) {
            binding.apply {
                root.setSingleClick {
                    onClick(item)
                }
                imgIcon.setImageResource(item.drawableRes)
                tvTitle.setText(item.stringRes)
                executePendingBindings()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SignInViewHolder {
        return SignInViewHolder(
            ItemSignInBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: SignInViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class FeatureDiffCallback : DiffUtil.ItemCallback<SignInView>() {

    override fun areItemsTheSame(oldItem: SignInView, newItem: SignInView): Boolean {
        return oldItem.drawableRes == newItem.drawableRes
    }

    override fun areContentsTheSame(oldItem: SignInView, newItem: SignInView): Boolean {
        return oldItem.stringRes == newItem.stringRes
    }
}