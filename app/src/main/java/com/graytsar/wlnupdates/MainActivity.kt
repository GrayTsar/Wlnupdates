package com.graytsar.wlnupdates

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController

const val ARG_ID_NOVEL:String = "argIdNovel"

const val ARG_ID_AUTHOR:String = "argIdAuthor"
const val ARG_ID_GROUP:String = "argIdAuthor"
const val ARG_ID_PUBLISHER:String = "argIdPublisher"
const val ARG_ID_ILLUSTRATOR:String = "argIdPublisher"

const val ARG_PARCEL_NOVEL_CHAPTER:String = "argParcelNovelChapter"
const val ARG_PARCEL_NOVEL_GENRE:String = "argParcelNovelGenre"
const val ARG_PARCEL_NOVEL_TAG:String = "argParcelNovelTag"
const val ARG_PARCEL_NOVEL_ILLUSTRATOR:String = "argParcelNovelIllustrator"
const val ARG_PARCEL_NOVEL_PUBLISHER:String = "argParcelNovelPublisher"

class MainActivity : AppCompatActivity(), NavController.OnDestinationChangedListener {
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar) //replace toolbar

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController: NavController = navHostFragment.navController

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.fragmentCollectionRecent
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
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

    }


}