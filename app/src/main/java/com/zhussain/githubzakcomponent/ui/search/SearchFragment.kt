package com.zhussain.githubzakcomponent.ui.search

import android.content.Context
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.os.IBinder
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.zhussain.githubzakcomponent.AppExecutors
import com.zhussain.githubzakcomponent.GithubApp
import com.zhussain.githubzakcomponent.MainActivity

import com.zhussain.githubzakcomponent.R
import com.zhussain.githubzakcomponent.binding.FragmentDataBindingComponent
import com.zhussain.githubzakcomponent.databinding.SearchFragmentBinding
import com.zhussain.githubzakcomponent.dataclass.Repo
import com.zhussain.githubzakcomponent.ui.common.RepoListAdapter
import com.zhussain.githubzakcomponent.ui.common.RetryCallback
import com.zhussain.githubzakcomponent.util.autoCleared
import javax.inject.Inject

/**
 * Allows you to search repositories on Github. Each search result is kept in the database in
 * RepoSearchResult table where the list of repository IDs are denormalized into a single column.
 * The actual Repo instances live in the Repo table.
Each time a new page is fetched, the same RepoSearchResult record in the Database is updated
with the new list of repository ids.
 */
class SearchFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    val searchViewModel: SearchViewModel by viewModels { viewModelFactory }

    @Inject
    lateinit var appExecutors: AppExecutors

    var dataBindingComponent: DataBindingComponent = FragmentDataBindingComponent(this)

    var binding by autoCleared<SearchFragmentBinding>()

    var adapter by autoCleared<RepoListAdapter>()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        ((activity as MainActivity).application as GithubApp).appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate<SearchFragmentBinding>(
            inflater,
            R.layout.search_fragment,
            container,
            false,
            dataBindingComponent
        )


        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = viewLifecycleOwner
        initRecyclerView()
        val rvAdapter = RepoListAdapter(
            dataBindingComponent = dataBindingComponent,
            appExecutors = appExecutors,
            showFullName = true
        ) {
            findNavController().navigate(SearchFragmentDirections.repoShow(it.name, it.owner.login))
        }
        binding.query = searchViewModel.query
        binding.recyRepoList.adapter = rvAdapter
        adapter = rvAdapter
        initSearchInputListener()

        binding.callback = object : RetryCallback {
            override fun retry() {
                searchViewModel.refresh()
            }
        }

    }

    private fun initSearchInputListener() {
        binding.searchBox.setOnEditorActionListener { view: View, actionId: Int, _: KeyEvent? ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                doSearch(view)
                true
            } else {
                false
            }
        }

        binding.searchBox.setOnKeyListener { view: View, keyCode: Int, event: KeyEvent ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                doSearch(view)
                true
            } else {
                false
            }
        }
    }

    private fun doSearch(v: View) {
        val query = binding.searchBox.text.toString()
        // Dismiss keyboard
        dismissKeyboard(v.windowToken)
        searchViewModel.setQuery(query)
    }

    private fun initRecyclerView() {
        binding.recyRepoList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val lastPosition = layoutManager.findLastVisibleItemPosition()
                if (lastPosition == adapter.itemCount - 1) {
                    searchViewModel.loadNextPage()
                }
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
            }
        })

        binding.searchResult = searchViewModel.result
        searchViewModel.result.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it?.data)
        })

        searchViewModel.loadMoreStatus.observe(viewLifecycleOwner, Observer { loadingMore ->
            if (loadingMore == null) {
                binding.loadingMore = false
            } else {
                binding.loadingMore = loadingMore.isRunning
                val error = loadingMore.errorMessageIfNotHandled
                if (error != null) {
                    Snackbar.make(binding.bottomProgressBar, error, Snackbar.LENGTH_LONG).show()
                }
            }

        })

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }

    private fun dismissKeyboard(windowToken: IBinder) {
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.hideSoftInputFromWindow(windowToken, 0)
    }
}
