package com.graytsar.wlnupdates

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.NotificationCompat
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.room.Room
import androidx.room.Update
import androidx.work.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.ActivityResult
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import com.google.android.play.core.ktx.bytesDownloaded
import com.google.android.play.core.ktx.totalBytesToDownload
import com.graytsar.wlnupdates.background.UpdateWorker
import com.graytsar.wlnupdates.database.DatabaseService
import com.graytsar.wlnupdates.database.LibraryDatabase
import com.graytsar.wlnupdates.databinding.ActivityMainBinding
import com.graytsar.wlnupdates.rest.interfaces.RestService
import java.util.concurrent.TimeUnit

const val ARG_ID_NOVEL:String = "argIdNovel"

const val ARG_ID_AUTHOR:String = "argIdAuthor"
const val ARG_ID_GROUP:String = "argIdGroup"
const val ARG_ID_PUBLISHER:String = "argIdPublisher"
const val ARG_ID_ILLUSTRATOR:String = "argIdPublisher"

const val ARG_ID_NOVEL_GROUP:String = "argIdNovelGroup"

const val ARG_PARCEL_LIBRARY_ITEM:String = "argParcelLibraryItem"

const val ARG_PARCEL_NOVEL_CHAPTER:String = "argParcelNovelChapter"
const val ARG_PARCEL_NOVEL_GENRE:String = "argParcelNovelGenre"
const val ARG_PARCEL_NOVEL_TAG:String = "argParcelNovelTag"
const val ARG_PARCEL_NOVEL_ILLUSTRATOR:String = "argParcelNovelIllustrator"
const val ARG_PARCEL_NOVEL_PUBLISHER:String = "argParcelNovelPublisher"
const val ARG_PARCEL_NOVEL_SIMILAR_SERIES:String = "argParcelNovelSimilarSeries"
const val ARG_PARCEL_ADVANCED_SEARCH_RESULT:String = "argParcelAdvancedSearchResult"

const val channelID:String = "com.graytsar.wlnupdates.Updates"
const val notificationID:Int = 101



const val keyPreferenceUtils:String = "keyPreferenceUtils"
const val keyPreferenceRequestReview:String = "keyPreferenceRequestReview"
const val requestReviewAt:Int = 50
const val RC_UPDATE_IMMEDIATE = 100
const val RC_UPDATE_FLEXIBLE = 101

//const val keyPreferenceCookieStore:String = "keyPreferenceCookieStore"
const val keyCookieDomain:String = "www.wlnupdates.com"
var cookieSession:String? = null //must be set before RestService is created

const val keyPreferenceCredentials:String = "keyPreferenceCredentials"
const val keyPreferenceUsername:String = "keyPreferenceUsername"
const val keyPreferencePassword:String = "keyPreferencePassword"

class MainActivity : AppCompatActivity(), NavController.OnDestinationChangedListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var notificationManager:NotificationManager

    private var appUpdateManager:AppUpdateManager? = null
    private var updateListener:InstallStateUpdatedListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        createNotificationChannel(channelID, "Updates", "New Chapter Released")

        val toolbar: Toolbar = binding.includeToolbar.toolbar
        setSupportActionBar(toolbar) //replace actionbar

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController: NavController = navHostFragment.navController

        val bottomNavigationView = binding.bottomNavigation

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.fragmentLibrary,
                R.id.fragmentCollectionRecent,
                R.id.fragmentSearch,
                R.id.fragmentAdvancedSearch
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        bottomNavigationView.setupWithNavController(navController)
        navController.addOnDestinationChangedListener(this)


        val sp = getSharedPreferences(keyPreferenceCredentials, Context.MODE_PRIVATE)
        cookieSession = sp.getString(keyCookieDomain, null)
        RestService.sharedPreference = sp


        DatabaseService.db = Room.databaseBuilder(
            applicationContext,
            LibraryDatabase::class.java, "Library_Database"
        ).allowMainThreadQueries().build()

        val constraints = Constraints.Builder().build()

        //val www = OneTimeWorkRequestBuilder<UpdateWorker>().setConstraints(constraints).build()
        //WorkManager.getInstance(this).enqueue(www)

        val periodicWorkRequest = PeriodicWorkRequestBuilder<UpdateWorker>(2, TimeUnit.HOURS, 1, TimeUnit.HOURS)
            .setConstraints(constraints)
            .build()
        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "UpdateLibraryWork",
            ExistingPeriodicWorkPolicy.KEEP,
            periodicWorkRequest
        )







        //requestInAppUpdate()
    }

    /*
    override fun onResume() {
        super.onResume()

        appUpdateManager?.appUpdateInfo?.addOnSuccessListener { appUpdateInfo ->
            when (appUpdateInfo.installStatus()) {
                InstallStatus.DOWNLOADING -> {
                    binding.progressMainUpdateIndicator.visibility = View.VISIBLE
                    binding.progressMainUpdateIndicator.progress = ((appUpdateInfo.bytesDownloaded / appUpdateInfo.totalBytesToDownload) * 100).toInt()
                }
                InstallStatus.DOWNLOADED -> {
                    binding.progressMainUpdateIndicator.progress = 100
                    binding.progressMainUpdateIndicator.visibility = View.GONE
                    popupSnackbarForCompleteUpdate()
                }
                else -> {

                }
            }
        }

        appUpdateManager?.appUpdateInfo?.addOnSuccessListener { appUpdateInfo ->
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                // If an in-app update is already running, resume the update.
                appUpdateManager?.startUpdateFlowForResult(appUpdateInfo, AppUpdateType.IMMEDIATE, this, RC_UPDATE_IMMEDIATE)
            }
        }
    }
     */

    //when navigation back button is pressed
    override fun onSupportNavigateUp(): Boolean {
        val f = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController:NavController = f.navController //for fragment switch
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onDestinationChanged(
        controller: NavController,
        destination: NavDestination,
        arguments: Bundle?) {
    }

    fun pushNotify(title: String, text: String){
        val launchIntent = packageManager.getLaunchIntentForPackage("com.graytsar.wlnupdates")
        val pendingIntent: PendingIntent = PendingIntent.getActivity(
            this,
            0,
            launchIntent,
            PendingIntent.FLAG_CANCEL_CURRENT
        )

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

    /*
    private fun requestInAppUpdate() {
        appUpdateManager = AppUpdateManagerFactory.create(this)
        val appUpdateInfoTask = appUpdateManager!!.appUpdateInfo

        appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
            val availability = appUpdateInfo.updateAvailability()
            if (availability > 0) {

                appUpdateManager!!.startUpdateFlowForResult(
                    appUpdateInfo,
                    AppUpdateType.FLEXIBLE,
                    this,
                    RC_UPDATE_FLEXIBLE
                )

                updateListener = InstallStateUpdatedListener { state ->
                    when (state.installStatus()) {
                        InstallStatus.DOWNLOADING -> {
                            binding.progressMainUpdateIndicator.visibility = View.VISIBLE
                            binding.progressMainUpdateIndicator.progress = ((state.bytesDownloaded / state.totalBytesToDownload) * 100).toInt()
                        }
                        InstallStatus.DOWNLOADED -> {
                            binding.progressMainUpdateIndicator.progress = 100
                            binding.progressMainUpdateIndicator.visibility = View.GONE
                            popupSnackbarForCompleteUpdate()
                        }
                        else -> {

                        }
                    }
                }

                appUpdateManager!!.registerListener(updateListener!!)
            }
        }
    }
     */
    /*
    private fun requestInAppUpdate() {
        appUpdateManager = AppUpdateManagerFactory.create(this)
        val appUpdateInfoTask = appUpdateManager!!.appUpdateInfo

        appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {

                if(appUpdateInfo.updatePriority() > 3) {
                    appUpdateManager!!.startUpdateFlowForResult(
                        appUpdateInfo,
                        AppUpdateType.IMMEDIATE,
                        this,
                        RC_UPDATE_IMMEDIATE
                    )
                }

                appUpdateInfo.clientVersionStalenessDays()?.let {
                    if(it >= 14) {
                        appUpdateManager!!.startUpdateFlowForResult(
                            appUpdateInfo,
                            AppUpdateType.IMMEDIATE,
                            this,
                            RC_UPDATE_IMMEDIATE
                        )
                    }
                }
            } else if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)) {

                updateListener = InstallStateUpdatedListener { state ->
                    when (state.installStatus()) {
                        InstallStatus.DOWNLOADING -> {

                        }
                        InstallStatus.DOWNLOADED -> {
                            popupSnackbarForCompleteUpdate()
                        }
                        else -> {

                        }
                    }
                }

                appUpdateManager!!.registerListener(updateListener!!)
            }
        }
    }
    */

    /*
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_UPDATE_IMMEDIATE) {
            when (resultCode) {
                RESULT_OK -> {

                }
                RESULT_CANCELED -> {

                }
                ActivityResult.RESULT_IN_APP_UPDATE_FAILED -> {

                }
                else -> {

                }
            }
        } else if (requestCode == RC_UPDATE_FLEXIBLE) {
            when (resultCode) {
                RESULT_OK -> {
                    //The user has accepted the update successfully.
                }
                RESULT_CANCELED -> {
                    //The user has canceled the update.
                }
                ActivityResult.RESULT_IN_APP_UPDATE_FAILED -> {
                    //Some other error occurred or the failure of the In-App update.
                }
                else -> {

                }
            }
        }
    }

     */

    /*
    private fun popupSnackbarForCompleteUpdate() {
        appUpdateManager?.let { appUpdateManager ->
            Snackbar.make(
                findViewById(R.id.drawer_layout),
                "An update has just been downloaded.",
                Snackbar.LENGTH_INDEFINITE
            ).apply {
                setAction("RESTART") { appUpdateManager.completeUpdate() }
                show()
            }

            updateListener?.let {
                appUpdateManager.unregisterListener(it)
            }
        }
    }
     */
}