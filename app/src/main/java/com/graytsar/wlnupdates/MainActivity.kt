package com.graytsar.wlnupdates

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.media.RingtoneManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.core.app.NotificationCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.room.Database
import androidx.room.Room
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.graytsar.wlnupdates.background.UpdateWorker
import com.graytsar.wlnupdates.database.DatabaseService
import com.graytsar.wlnupdates.database.LibraryDatabase
import java.util.concurrent.TimeUnit

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

const val channelID:String = "com.graytsar.wlnupdates.Updates"
const val notificationID:Int = 101

class MainActivity : AppCompatActivity(), NavController.OnDestinationChangedListener {
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var notificationManager:NotificationManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        createNotificationChannel(channelID, "Updates", "New Chapter Released")

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar) //replace toolbar

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController: NavController = navHostFragment.navController

        val bottomNavigationView = findViewById<View>(R.id.bottom_navigation) as BottomNavigationView

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.fragmentLibrary, R.id.fragmentCollectionRecent, R.id.fragmentSearch, R.id.fragmentAdvancedSearch
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        bottomNavigationView.setupWithNavController(navController)
        navController.addOnDestinationChangedListener(this)

        DatabaseService.db = Room.databaseBuilder(
            applicationContext,
            LibraryDatabase::class.java, "Library_Database"
        ).allowMainThreadQueries().build()

        val constraints = Constraints.Builder().build()
        val periodicWorkRequest = PeriodicWorkRequestBuilder<UpdateWorker>(7, TimeUnit.HOURS).setConstraints(constraints).build()
        WorkManager.getInstance(this).enqueueUniquePeriodicWork("UpdateLibraryWork", ExistingPeriodicWorkPolicy.KEEP, periodicWorkRequest)
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

    fun pushNotify(title:String, text:String){
        val launchIntent = packageManager.getLaunchIntentForPackage("com.graytsar.wlnupdates")
        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, launchIntent, PendingIntent.FLAG_CANCEL_CURRENT)

        //switch does not update with MutableLiveData change, remove this feature till i think of something different that works
        //val deleteIntent = Intent(this, ModelBattery::class.java)
        //deleteIntent.action = "notification_cancelled"
        //val pendingDeleteIntent = PendingIntent.getBroadcast(this, 1, deleteIntent, PendingIntent.FLAG_CANCEL_CURRENT)

        val notification = NotificationCompat.Builder(this, channelID).apply {
            setContentTitle(title)
            setContentText(text)
            setSmallIcon(R.drawable.ic_app)
            setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
            setContentIntent(pendingIntent)
            //setDeleteIntent(pendingDeleteIntent)
            setChannelId(channelID)
        }.build()

        notificationManager.notify(notificationID, notification)
    }

    private fun createNotificationChannel(id: String, name: String, description: String) {
        if(Build.VERSION.SDK_INT < 26 )
            return

        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(id, name, importance)

        channel.description = description
        notificationManager.createNotificationChannel(channel)
    }
}