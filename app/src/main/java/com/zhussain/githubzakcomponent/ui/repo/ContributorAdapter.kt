package com.zhussain.githubzakcomponent.ui.repo

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import com.zhussain.githubzakcomponent.AppExecutors
import com.zhussain.githubzakcomponent.R
import com.zhussain.githubzakcomponent.databinding.ContributorItemBinding
import com.zhussain.githubzakcomponent.dataclass.Contributor
import com.zhussain.githubzakcomponent.ui.common.DataBoundListAdapter

class ContributorAdapter(
    private val dataBindingComponent: DataBindingComponent,
    appExecutors: AppExecutors,
    private val callBack: ((Contributor, ImageView) -> Unit)?
) : DataBoundListAdapter<Contributor, ContributorItemBinding>(
    appExecutors = appExecutors,
    diffCallback = object : DiffUtil.ItemCallback<Contributor>() {
        override fun areItemsTheSame(oldItem: Contributor, newItem: Contributor): Boolean {
            return oldItem.login == newItem.login
        }

        override fun areContentsTheSame(oldItem: Contributor, newItem: Contributor): Boolean {
            return oldItem.avatarUrl == newItem.avatarUrl
                    && oldItem.contributions == newItem.contributions
        }
    }) {
    override fun createBinding(parent: ViewGroup): ContributorItemBinding {
        val binding = DataBindingUtil.inflate<ContributorItemBinding>(
            LayoutInflater.from(parent.context),
            R.layout.contributor_item,
            parent,
            false,
            dataBindingComponent
        )

        binding.root.setOnClickListener {
            // attach call back..
            binding.contributor?.let {
                callBack?.invoke(it,binding.loginImage)
            }
        }
        return binding;
    }

    override fun bind(binding: ContributorItemBinding, item: Contributor) {
        // pass each contributor object to the xml file for displaying the view.
        binding.contributor = item
    }
}