package ru.rumigor.cookbook.ui.ui.login

import android.os.Bundle
import android.widget.Toast
import by.kirich1409.viewbindingdelegate.viewBinding
import moxy.ktx.moxyPresenter
import ru.rumigor.cookbook.AppPreferences
import ru.rumigor.cookbook.R
import ru.rumigor.cookbook.data.repository.RecipeRepository
import ru.rumigor.cookbook.databinding.LoginBinding
import ru.rumigor.cookbook.scheduler.Schedulers
import ru.rumigor.cookbook.ui.Main2Activity
import ru.rumigor.cookbook.ui.UserViewModel
import ru.rumigor.cookbook.ui.abs.AbsActivity
import ru.rumigor.cookbook.ui.recipeDetails.RecipeDetailsPresenter
import javax.inject.Inject

class LoginActivity : AbsActivity(R.layout.login), LoginView {
    @Inject
    lateinit var schedulers: Schedulers

    @Inject
    lateinit var recipeRepository: RecipeRepository

    @Inject
    lateinit var preferences: AppPreferences

    private val presenter: LoginPresenter by moxyPresenter {
        LoginPresenter(
            schedulers = schedulers,
            recipeRepository = recipeRepository,
            preferences = preferences
        )
    }

    private val ui: LoginBinding by viewBinding()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)
        AppPreferences.setup(applicationContext)
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
        startActivity(Main2Activity.getStartIntent(this))
    }

    override fun showError(error: Throwable) {
        Toast.makeText(this, "Имя/пароль не найдены!", Toast.LENGTH_LONG).show()
    }
}