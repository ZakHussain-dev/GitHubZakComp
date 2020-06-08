package com.zhussain.githubzakcomponent.ui.repo

import android.content.Context
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnPreDraw
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.transition.TransitionInflater
import com.zhussain.githubzakcomponent.AppExecutors
import com.zhussain.githubzakcomponent.GithubApp
import com.zhussain.githubzakcomponent.MainActivity

import com.zhussain.githubzakcomponent.R
import com.zhussain.githubzakcomponent.binding.FragmentDataBindingComponent
import com.zhussain.githubzakcomponent.databinding.RepoFragmentBinding
import com.zhussain.githubzakcomponent.ui.common.RetryCallback
import com.zhussain.githubzakcomponent.util.autoCleared
import javax.inject.Inject

/**
 * The UI Controller for displaying a Github Repo's information with its contributors.
 */

class RepoFragment : Fragment() {


    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    val repoViewModel: RepoViewModel by viewModels { viewModelFactory }

    @Inject
    lateinit var appExecutors: AppExecutors

    // mutable for testing
    var dataBindingComponent: DataBindingComponent = FragmentDataBindingComponent(this)
    var binding by autoCleared<RepoFragmentBinding>()

    private val params by navArgs<RepoFragmentArgs>()
    private var adapter by autoCleared<ContributorAdapter>()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        ((activity as MainActivity).application as GithubApp).appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val dataBinding = DataBindingUtil.inflate<RepoFragmentBinding>(
            inflater,
            R.layout.repo_fragment,
            container,
            false
        )

        dataBinding.retryCallback = object : RetryCallback {
            override fun retry() {
                repoViewModel.retry()
            }
        }
        binding = dataBinding
        sharedElementReturnTransition =
            TransitionInflater.from(context).inflateTransition(R.transition.move)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        repoViewModel.setId(params.owner, params.name)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.repo = repoViewModel.repo

        val adapter =
            ContributorAdapter(dataBindingComponent, appExecutors) { contributor, imageView ->
                val extras = FragmentNavigatorExtras(imageView to contributor.login)
                findNavController().navigate(
                    RepoFragmentDirections.showUser(
                        contributor.avatarUrl,
                        contributor.login
                    ), extras
                )
            }

        this.adapter = adapter
        binding.contributorList.adapter = adapter
        postponeEnterTransition()
        binding.contributorList.doOnPreDraw {
            startPostponedEnterTransition()
        }
        initContributorList(repoViewModel)

    }

    private fun initContributorList(repoViewModel: RepoViewModel) {
        repoViewModel.contributors.observe(viewLifecycleOwner, Observer {
            // we don't need any null checks here for the adapter since LiveData guarantees that
            // it won't call us if fragment is stopped or not started.
            if (it.data != null) {
                adapter.submitList(it.data)
            } else {
                adapter.submitList(emptyList())
            }
        })
    }
}
