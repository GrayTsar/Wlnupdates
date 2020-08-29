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
    private val viewModelNovel by viewModels<ViewModelNovel>()

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


        binding.includeToolbarNovel.layoutNovelChapters.chapterBackground.setOnClickListener {
            viewModelNovel.listChapter.value?.let {
                val bundle = Bundle()
                bundle.putParcelableArrayList(ARG_PARCEL_NOVEL_CHAPTER, ArrayList(it))

                navController.navigate(R.id.fragmentNovelChapter, bundle)
            }

        }

        binding.includeToolbarNovel.layoutNovelGenre.genreBackground.setOnClickListener {
            viewModelNovel.listGenre.value?.let {
                val bundle = Bundle()
                bundle.putParcelableArrayList(ARG_PARCEL_NOVEL_GENRE, ArrayList(it))

                navController.navigate(R.id.fragmentNovelGenre, bundle)
            }
        }

        binding.includeToolbarNovel.layoutNovelTag.tagBackground.setOnClickListener {
            viewModelNovel.listTag.value?.let {
                val bundle = Bundle()
                bundle.putParcelableArrayList(ARG_PARCEL_NOVEL_TAG, ArrayList(it))

                navController.navigate(R.id.fragmentNovelTag, bundle)
            }
        }

        binding.includeToolbarNovel.layoutNovelIllustrator.illustratorBackground.setOnClickListener {
            viewModelNovel.listIllustrator.value?.let {
                val bundle = Bundle()
                bundle.putParcelableArrayList(ARG_PARCEL_NOVEL_ILLUSTRATOR, ArrayList(it))

                navController.navigate(R.id.fragmentNovelIllustrator, bundle)
            }
        }

        binding.includeToolbarNovel.layoutNovelPublisher.publisherBackground.setOnClickListener {
            viewModelNovel.listPublisher.value?.let {
                val bundle = Bundle()
                bundle.putParcelableArrayList(ARG_PARCEL_NOVEL_PUBLISHER, ArrayList(it))

                navController.navigate(R.id.fragmentNovelPublisher, bundle)
            }
        }

        binding.includeToolbarNovel.textNovelAuthors.setOnClickListener {
            val bundle = Bundle()
            bundle.putInt(ARG_ID_AUTHOR, viewModelNovel.idAuthor)

            navController.navigate(R.id.fragmentAuthor, bundle)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(argIdNovel > 0){
            viewModelNovel.getDataNovel(argIdNovel)
        }
    }

}