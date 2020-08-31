package com.graytsar.wlnupdates.ui.novel

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import coil.load
import com.google.android.material.snackbar.Snackbar
import com.graytsar.wlnupdates.*
import com.graytsar.wlnupdates.database.DatabaseService
import com.graytsar.wlnupdates.database.ModelLibrary
import com.graytsar.wlnupdates.databinding.FragmentNovelBinding
import com.graytsar.wlnupdates.rest.Genre
import com.graytsar.wlnupdates.rest.interfaces.RestService
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList

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
        setHasOptionsMenu(true)
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
            binding.includeToolbarNovel.imageNovelCover.load(it) {
                placeholder(R.drawable.ic_app_white)
                error(R.drawable.ic_app_white)
            }
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_novel,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.menuNovelAdd -> {
                var volume:Double = 0.0
                var chapter:Double = 0.0
                val title = viewModelNovel.title.value!!
                val cover = if(viewModelNovel.cover.value != null) {
                    viewModelNovel.cover.value!!
                } else {
                    ""
                }

                val release = viewModelNovel.listChapter.value?.firstOrNull()

                val rVolume = release?.volume
                val rChapter = release?.chapter

                rVolume?.let {
                    volume = it
                }
                rChapter?.let {
                    chapter = it
                }

                val array = DatabaseService.db?.daoLibrary()!!.selectWhereIdWlnupdates(argIdNovel)

                if(array.isNotEmpty()) {
                    val model = array[0]

                    model.volume = volume
                    model.chapter = volume
                    DatabaseService.db?.daoLibrary()!!.update(model)
                } else {
                    DatabaseService.db?.daoLibrary()!!.insert(ModelLibrary(0, argIdNovel, title, cover, 0, false, volume, chapter))
                }
                Snackbar.make(binding.root, "Added to Library", Snackbar.LENGTH_LONG).show()
            }
            else -> {

            }
        }

        return super.onOptionsItemSelected(item)
    }
}