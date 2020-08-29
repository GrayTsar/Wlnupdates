package com.graytsar.wlnupdates.ui.author

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.graytsar.wlnupdates.ARG_ID_AUTHOR
import com.graytsar.wlnupdates.MainActivity
import com.graytsar.wlnupdates.R
import com.graytsar.wlnupdates.databinding.FragmentAuthorBinding
import kotlinx.android.synthetic.main.toolbar_author.view.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class FragmentAuthor : Fragment() {
    private lateinit var binding:FragmentAuthorBinding
    private val viewModelAuthor by viewModels<ViewModelAuthor>()
    private val adapterAuthor = AdapterAuthor(this)

    private var idAuthor:Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            idAuthor = it.getInt(ARG_ID_AUTHOR, -1)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAuthorBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.viewModelAuthor = viewModelAuthor

        val toolbar: Toolbar = binding.includeToolbarAuthor.toolbarAuthor
        (requireActivity() as MainActivity).setSupportActionBar(toolbar)

        val navController = NavHostFragment.findNavController(this)
        NavigationUI.setupActionBarWithNavController(this.context as MainActivity, navController)

        binding.recyclerAuthor.adapter = adapterAuthor

        viewModelAuthor.isLoading.observe(viewLifecycleOwner, {
            binding.progressBarAuthor.visibility = if(it){
                View.VISIBLE
            } else {
                View.GONE
            }
        })

        viewModelAuthor.list.observe(viewLifecycleOwner, {
            adapterAuthor.submitList(it)
        })

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(idAuthor > 0) {
            viewModelAuthor.getDataAuthor(idAuthor)
        }
    }
}