package com.graytsar.wlnupdates.ui.novel

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import coil.ImageLoader
import coil.load
import coil.request.ImageRequest
import com.graytsar.wlnupdates.MainActivity
import com.graytsar.wlnupdates.databinding.FragmentNovelBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class FragmentNovel : Fragment() {
    private lateinit var binding:FragmentNovelBinding
    val viewModelNovel by viewModels<ViewModelNovel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNovelBinding.inflate(inflater, container, false)
        binding.includeToolbarNovel.viewModelNovel = viewModelNovel
        binding.lifecycleOwner = this


        val toolbar: Toolbar = binding.includeToolbarNovel.toolbarNovel
        (requireActivity() as MainActivity).setSupportActionBar(toolbar)

        val navController = NavHostFragment.findNavController(this)
        NavigationUI.setupActionBarWithNavController(this.context as MainActivity, navController)

        viewModelNovel.getRestData()

        viewModelNovel.latestChapter.observe(viewLifecycleOwner, Observer {
            binding.includeToolbarNovel.layoutNovelChapters.textChapterLastDate.text = it
        })

        viewModelNovel.genre.observe(viewLifecycleOwner, Observer {
            binding.includeToolbarNovel.layoutNovelGenre.textGenreItems.text = it
        })

        viewModelNovel.tags.observe(viewLifecycleOwner, Observer {
            binding.includeToolbarNovel.layoutNovelTag.textTagItems.text = it
        })

        viewModelNovel.illustrator.observe(viewLifecycleOwner, Observer {
            binding.includeToolbarNovel.layoutNovelIllustrator.textIllustratorItem.text = it
        })

        viewModelNovel.publisher.observe(viewLifecycleOwner, Observer {
            binding.includeToolbarNovel.layoutNovelPublisher.textPublisherItem.text = it
        })

        viewModelNovel.cover.observe(viewLifecycleOwner, Observer {
            binding.includeToolbarNovel.imageNovelCover.load(it)
        })

        return binding.root
    }

}