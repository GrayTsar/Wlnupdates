package com.graytsar.wlnupdates.ui.novel

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import coil.load
import com.graytsar.wlnupdates.*
import com.graytsar.wlnupdates.databinding.FragmentNovelBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class FragmentNovel : Fragment() {
    private lateinit var binding:FragmentNovelBinding
    val viewModelNovel by viewModels<ViewModelNovel>()

    private var argIdNovel:Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            argIdNovel = it.getInt(ARG_ID_NOVEL, -1)
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


        if(argIdNovel > 0){
            GlobalScope.launch {
                viewModelNovel.getRestData(argIdNovel)
            }
        }


        viewModelNovel.isLoading.observe(viewLifecycleOwner, Observer {
            binding.includeToolbarNovel.progressBarNovel.visibility = if(it){
                View.VISIBLE
            } else {
                View.GONE
            }
        })

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


        binding.includeToolbarNovel.layoutNovelChapters.chapterBackground.setOnClickListener { view ->
            val navHostFragment = (view.context as MainActivity).supportFragmentManager.findFragmentById(
                R.id.nav_host_fragment) as NavHostFragment
            val navController: NavController = navHostFragment.navController

            val bundle = Bundle()
            bundle.putParcelableArrayList(ARG_PARCEL_NOVEL_CHAPTER, viewModelNovel.listChapter)

            navController.navigate(R.id.fragmentNovelChapter, bundle)
        }

        binding.includeToolbarNovel.layoutNovelGenre.genreBackground.setOnClickListener { view ->
            val navHostFragment = (view.context as MainActivity).supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
            val navController: NavController = navHostFragment.navController

            val bundle = Bundle()
            bundle.putParcelableArrayList(ARG_PARCEL_NOVEL_GENRE, viewModelNovel.listGenre)

            navController.navigate(R.id.fragmentNovelGenre, bundle)
        }

        binding.includeToolbarNovel.layoutNovelTag.tagBackground.setOnClickListener { view ->
            val navHostFragment = (view.context as MainActivity).supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
            val navController: NavController = navHostFragment.navController

            val bundle = Bundle()
            bundle.putParcelableArrayList(ARG_PARCEL_NOVEL_TAG, viewModelNovel.listTag)

            navController.navigate(R.id.fragmentNovelTag, bundle)
        }

        binding.includeToolbarNovel.layoutNovelIllustrator.illustratorBackground.setOnClickListener { view ->
            val navHostFragment = (view.context as MainActivity).supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
            val navController: NavController = navHostFragment.navController

            val bundle = Bundle()
            bundle.putParcelableArrayList(ARG_PARCEL_NOVEL_ILLUSTRATOR, viewModelNovel.listIllustrator)

            navController.navigate(R.id.fragmentNovelIllustrator, bundle)
        }

        binding.includeToolbarNovel.layoutNovelPublisher.publisherBackground.setOnClickListener { view ->
            val navHostFragment = (view.context as MainActivity).supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
            val navController: NavController = navHostFragment.navController

            val bundle = Bundle()
            bundle.putParcelableArrayList(ARG_PARCEL_NOVEL_PUBLISHER, viewModelNovel.listPublisher)

            navController.navigate(R.id.fragmentNovelPublisher, bundle)
        }

        return binding.root
    }

}