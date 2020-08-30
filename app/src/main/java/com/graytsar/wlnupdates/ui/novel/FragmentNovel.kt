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

        binding.includeToolbarNovel.includeDescription.apply {
            lifecycleOwner = this@FragmentNovel
            viewModelNovel = this@FragmentNovel.viewModelNovel
        }
        binding.includeToolbarNovel.includeChapter.apply {
            lifecycleOwner = this@FragmentNovel
            viewModelNovel = this@FragmentNovel.viewModelNovel
        }
        binding.includeToolbarNovel.includeGenre.apply {
            lifecycleOwner = this@FragmentNovel
            viewModelNovel = this@FragmentNovel.viewModelNovel
        }
        binding.includeToolbarNovel.includeTag.apply {
            lifecycleOwner = this@FragmentNovel
            viewModelNovel = this@FragmentNovel.viewModelNovel
        }
        binding.includeToolbarNovel.includeInfo.apply {
            lifecycleOwner = this@FragmentNovel
            viewModelNovel = this@FragmentNovel.viewModelNovel
        }
        binding.includeToolbarNovel.includeGroup.apply {
            lifecycleOwner = this@FragmentNovel
            viewModelNovel = this@FragmentNovel.viewModelNovel
        }
        binding.includeToolbarNovel.includeIllustrator.apply {
            lifecycleOwner = this@FragmentNovel
            viewModelNovel = this@FragmentNovel.viewModelNovel
        }
        binding.includeToolbarNovel.includePublisher.apply {
            lifecycleOwner = this@FragmentNovel
            viewModelNovel = this@FragmentNovel.viewModelNovel
        }
        binding.includeToolbarNovel.includeAlternateNames.apply {
            lifecycleOwner = this@FragmentNovel
            viewModelNovel = this@FragmentNovel.viewModelNovel
        }


        /*
        binding.includeToolbarNovel.includeDescription.lifecycleOwner = this
        binding.includeToolbarNovel.includeChapter
        binding.includeToolbarNovel.includeGenre
        binding.includeToolbarNovel.includeTag
        binding.includeToolbarNovel.includeInfo
        binding.includeToolbarNovel.includeIllustrator
        binding.includeToolbarNovel.includePublisher
        binding.includeToolbarNovel.includeAlternateNames

         */

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

        viewModelNovel.description.observe(viewLifecycleOwner, Observer {
            binding.includeToolbarNovel.includeDescription.textNovelDescriptionContent.text = it
        })

        viewModelNovel.cover.observe(viewLifecycleOwner, Observer {
            binding.includeToolbarNovel.imageNovelCover.load(it)
        })

        binding.includeToolbarNovel.includeChapter.cardItemNovelChapter.setOnClickListener {
            viewModelNovel.listChapter.value?.let {
                val bundle = Bundle()
                bundle.putParcelableArrayList(ARG_PARCEL_NOVEL_CHAPTER, ArrayList(it))

                navController.navigate(R.id.fragmentNovelChapter, bundle)
            }

        }

        binding.includeToolbarNovel.includeGenre.cardItemNovelGenre.setOnClickListener {
            viewModelNovel.listGenre.value?.let {
                val bundle = Bundle()
                bundle.putParcelableArrayList(ARG_PARCEL_NOVEL_GENRE, ArrayList(it))

                navController.navigate(R.id.fragmentNovelGenre, bundle)
            }
        }

        binding.includeToolbarNovel.includeTag.cardItemNovelTag.setOnClickListener {
            viewModelNovel.listTag.value?.let {
                val bundle = Bundle()
                bundle.putParcelableArrayList(ARG_PARCEL_NOVEL_TAG, ArrayList(it))

                navController.navigate(R.id.fragmentNovelTag, bundle)
            }
        }

        binding.includeToolbarNovel.includeGroup.cardItemNovelGroup.setOnClickListener {
            viewModelNovel.listGroup.value?.let {
                val bundle = Bundle()
                bundle.putParcelableArrayList(ARG_ID_NOVEL_GROUP, ArrayList(it))

                navController.navigate(R.id.fragmentNovelGroup, bundle)
            }
        }

        binding.includeToolbarNovel.includeIllustrator.cardItemNovelIllustrator.setOnClickListener {
            viewModelNovel.listIllustrator.value?.let {
                val bundle = Bundle()
                bundle.putParcelableArrayList(ARG_PARCEL_NOVEL_ILLUSTRATOR, ArrayList(it))

                navController.navigate(R.id.fragmentNovelIllustrator, bundle)
            }
        }

        binding.includeToolbarNovel.includePublisher.cardItemNovelPublisher.setOnClickListener {
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