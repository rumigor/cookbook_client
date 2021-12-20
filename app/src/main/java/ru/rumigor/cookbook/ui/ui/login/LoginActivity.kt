package ru.rumigor.cookbook.ui.ui.login

import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import by.kirich1409.viewbindingdelegate.viewBinding
import moxy.ktx.moxyPresenter
import okhttp3.Credentials
import ru.rumigor.cookbook.AppPreferences
import ru.rumigor.cookbook.CookbookApp
import ru.rumigor.cookbook.R
import ru.rumigor.cookbook.data.api.AuthorizationInterceptor
import ru.rumigor.cookbook.data.di.CookbookApplicationComponent
import ru.rumigor.cookbook.data.di.modules.CookbookApiModule
import ru.rumigor.cookbook.data.repository.RecipeRepository
import ru.rumigor.cookbook.databinding.LoginBinding
import ru.rumigor.cookbook.scheduler.DefaultSchedulers
import ru.rumigor.cookbook.scheduler.Schedulers
import ru.rumigor.cookbook.ui.Main2Activity
import ru.rumigor.cookbook.ui.UserViewModel
import ru.rumigor.cookbook.ui.abs.AbsActivity
import ru.rumigor.cookbook.ui.recipeDetails.RecipeDetailsPresenter
import javax.inject.Inject

class LoginActivity : AbsActivity(R.layout.login),  LoginView{
    @Inject
    lateinit var schedulers: Schedulers

    @Inject
    lateinit var recipeRepository: RecipeRepository


    private val presenter: LoginPresenter by moxyPresenter {
        LoginPresenter(
            schedulers = schedulers,
            recipeRepository = recipeRepository
        )
    }

    companion object {
        fun getStartIntent(context: Context) = Intent(context, LoginActivity::class.java)
    }


    private val ui: LoginBinding by viewBinding()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppPreferences.setup(applicationContext)
        setContentView(R.layout.login)
        AppPreferences.authorized?.let { authorized ->
            if (authorized){
                startActivity(Main2Activity.getStartIntent(this))
                finish()
            }
        }
        ui.button.setOnClickListener {
            if (ui.userName.text.toString() != "" && ui.userPassword.text.toString() != ""){
                AppPreferences.username = ui.userName.text.toString()
                AppPreferences.password = ui.userPassword.text.toString()

                presenter.logon()
            }
        }

    }

    override fun login(users: List<UserViewModel>) {
        for (user in users) {
            if (AppPreferences.username == user.username) {
                AppPreferences.userId = user.id
                AppPreferences.authorized = true
            }
        }
        if (AppPreferences.authorized!!) {
            startActivity(Main2Activity.getStartIntent(this))
            finish()
        } else Toast.makeText(this, "Имя/пароль не найдены!", Toast.LENGTH_LONG).show()
    }

    override fun showError(error: Throwable) {
        Toast.makeText(this, "Ошибка подключения", Toast.LENGTH_LONG).show()
        AppPreferences.authorized = false
    }

    override fun onBackPressed() {
        recreate()
    }
}