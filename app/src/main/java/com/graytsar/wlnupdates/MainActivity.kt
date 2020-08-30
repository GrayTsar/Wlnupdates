package com.graytsar.wlnupdates

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView

const val ARG_ID_NOVEL:String = "argIdNovel"

const val ARG_ID_AUTHOR:String = "argIdAuthor"
const val ARG_ID_GROUP:String = "argIdGroup"
const val ARG_ID_PUBLISHER:String = "argIdPublisher"
const val ARG_ID_ILLUSTRATOR:String = "argIdPublisher"

const val ARG_ID_NOVEL_GROUP:String = "argIdNovelGroup"

const val ARG_PARCEL_NOVEL_CHAPTER:String = "argParcelNovelChapter"
const val ARG_PARCEL_NOVEL_GENRE:String = "argParcelNovelGenre"
const val ARG_PARCEL_NOVEL_TAG:String = "argParcelNovelTag"
const val ARG_PARCEL_NOVEL_ILLUSTRATOR:String = "argParcelNovelIllustrator"
const val ARG_PARCEL_NOVEL_PUBLISHER:String = "argParcelNovelPublisher"
const val ARG_PARCEL_ADVANCED_SEARCH_RESULT:String = "argParcelAdvancedSearchResult"

class MainActivity : AppCompatActivity(), NavController.OnDestinationChangedListener {
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar) //replace toolbar

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController: NavController = navHostFragment.navController

        val bottomNavigationView = findViewById<View>(R.id.bottom_navigation) as BottomNavigationView


        bottomNavigationView.setOnNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.fragmentLibrary -> {
                    //navController.popBackStack(R.id.fragmentLibrary, false)
                    navController.navigate(R.id.fragmentLibrary)
                    true

                }
                R.id.fragmentCollectionRecent -> {
                    //navController.popBackStack(R.id.fragmentCollectionRecent, false)
                    navController.navigate(R.id.fragmentCollectionRecent)
                    true

                }
                R.id.fragmentSearch -> {
                    //navController.popBackStack(R.id.fragmentSearch, false)
                    navController.navigate(R.id.fragmentSearch)
                    true

                }
                R.id.fragmentAdvancedSearch -> {
                    //navController.popBackStack(R.id.fragmentAdvancedSearch, false)
                    navController.navigate(R.id.fragmentAdvancedSearch)
                    true

                }
                else -> {
                    false
                }
            }
        }




        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.fragmentLibrary, R.id.fragmentCollectionRecent, R.id.fragmentSearch, R.id.fragmentAdvancedSearch
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        bottomNavigationView.setupWithNavController(navController)
        navController.addOnDestinationChangedListener(this)

        //navController.navigate(R.id.fragmentNovel)
        //navController.navigate(R.id.fragmentSearch)
    }

    //when navigation back button is pressed
    override fun onSupportNavigateUp(): Boolean {
        val f = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController:NavController = f.navController //for fragment switch
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onDestinationChanged(controller: NavController, destination: NavDestination, arguments: Bundle?) {
        val c = 0
    }

}