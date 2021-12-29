package ru.rumigor.cookbook.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import by.kirich1409.viewbindingdelegate.viewBinding
import moxy.ktx.moxyPresenter
import ru.rumigor.cookbook.AppPreferences
import ru.rumigor.cookbook.R
import ru.rumigor.cookbook.data.repository.RecipeRepository
import ru.rumigor.cookbook.databinding.ActivityMain2Binding
import ru.rumigor.cookbook.scheduler.Schedulers
import ru.rumigor.cookbook.ui.abs.AbsActivity
import ru.rumigor.cookbook.ui.login.LoginActivity
import javax.inject.Inject
import android.app.NotificationChannel

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.IntentFilter

import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.PeriodicWorkRequest
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import ru.rumigor.cookbook.data.stomp.Stomp
import ru.rumigor.cookbook.data.stomp.Subscription
import java.util.concurrent.TimeUnit
import kotlin.coroutines.CoroutineContext
import ru.rumigor.cookbook.data.stomp.ListenerWSNetwork





private const val TOP_RANK = "TOP_RANK"
private const val QUICK_RECIPES = "Quick_Recipes"
private const val MY_RECIPES = "MY_RECIPES"


class Main2Activity : AbsActivity(R.layout.activity_main2), MainView {


    private lateinit var appBarConfiguration: AppBarConfiguration
    private val binding: ActivityMain2Binding by viewBinding()
    private lateinit var navView: NavigationView

    @Inject
    lateinit var schedulers: Schedulers

    @Inject
    lateinit var recipeRepository: RecipeRepository


    private val presenter: MainPresenter by moxyPresenter {
        MainPresenter(
            schedulers = schedulers,
            recipeRepository = recipeRepository
        )
    }

    companion object {
        fun getStartIntent(context: Context) = Intent(context, Main2Activity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        AppPreferences.setup(applicationContext)
        setSupportActionBar(binding.appBarMain2.toolbar)

        val drawerLayout: DrawerLayout = binding.drawerLayout
        navView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main2)
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home,
                R.id.nav_tagFilter,
                R.id.nav_category,
                R.id.nav_top,
                R.id.nav_quickest,
                R.id.nav_favorites
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_home -> {
                    navController.navigate(R.id.recipesListFragment)
                    drawerLayout.close()
                    true
                }
                R.id.nav_favorites -> {
                    navController.navigate(R.id.favoritesFragment)
                    drawerLayout.close()
                    true
                }
                R.id.nav_category -> {
                    navController.navigate(R.id.categoryFragment)
                    drawerLayout.close()
                    true
                }
                R.id.nav_top -> {
                    val bundle = Bundle()
                    bundle.putString(TOP_RANK, TOP_RANK)
                    navController.navigate(R.id.recipesListFragment, bundle)
                    drawerLayout.close()
                    true
                }
                R.id.nav_quickest -> {
                    val bundle = Bundle()
                    bundle.putString(QUICK_RECIPES, QUICK_RECIPES)
                    navController.navigate(R.id.recipesListFragment, bundle)
                    drawerLayout.close()
                    true
                }
                R.id.nav_tagFilter -> {
                    navController.navigate(R.id.tagsFragment)
                    drawerLayout.close()
                    true
                }
                R.id.nav_myRecipes ->{
                    val bundle = Bundle()
                    bundle.putString(MY_RECIPES, "YES")
                    navController.navigate(R.id.recipesListFragment, bundle)
                    drawerLayout.close()
                    true
                }
                else -> true
            }
        }

        initNotificationChannel()
        val myWorkRequest = PeriodicWorkRequestBuilder<PushWorker>(15, TimeUnit.MINUTES).build()
        WorkManager.getInstance(this).enqueue(myWorkRequest)
        val headersSetup: Map<String, String> = HashMap()
        val stomp = Stomp(
            "ws://cookbook-env.eba-ggumuimp.ap-south-1.elasticbeanstalk.com/stomp/websocket", headersSetup, ListenerWSNetwork {  }
        )
        stomp.connect()

        stomp.subscribe(Subscription(
            "cookboo/new"
        ) { headers, body ->
            body?.let{
                val intent = Intent()
                intent.action = ACTION_SEND_MSG
                intent.putExtra(NAME_MSG, it)
                applicationContext.sendBroadcast(intent)
            }
        })
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main2, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main2)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onBackPressed() {
        val navController = findNavController(R.id.nav_host_fragment_content_main2)
        navController.popBackStack()
    }

    override fun getUser(user: UserViewModel) {
        val header = navView.getHeaderView(0)
        val logoutButton = header.findViewById<Button>(R.id.logout)
        logoutButton.setOnClickListener {
            AppPreferences.authorized = false
            startActivity(LoginActivity.getStartIntent(this))
            finish()
        }
        val userName = header.findViewById<TextView>(R.id.userNameInNav)
        userName.text = user.username
        val userMail = header.findViewById<TextView>(R.id.userMail)
        userMail.text = user.email
    }

    override fun showError(error: Throwable) {
        Toast.makeText(this, error.message, Toast.LENGTH_SHORT).show()
    }

    private fun initNotificationChannel() {
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val importance = NotificationManager.IMPORTANCE_LOW
        val channel = NotificationChannel("2", "name", importance)
        notificationManager.createNotificationChannel(channel)
    }

    override fun onDestroy() {
        super.onDestroy()
    }




}