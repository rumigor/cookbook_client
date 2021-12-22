package ru.rumigor.cookbook.ui.registration

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import by.kirich1409.viewbindingdelegate.viewBinding
import moxy.ktx.moxyPresenter
import ru.rumigor.cookbook.R
import ru.rumigor.cookbook.data.repository.RecipeRepository
import ru.rumigor.cookbook.scheduler.Schedulers
import ru.rumigor.cookbook.ui.abs.AbsActivity
import ru.rumigor.cookbook.ui.login.LoginActivity
import ru.rumigor.cookbook.databinding.ActivityRegistrationBinding
import javax.inject.Inject

class RegistrationActivity : AbsActivity(R.layout.activity_registration), RegistrationView {

    @Inject
    lateinit var schedulers: Schedulers

    @Inject
    lateinit var recipeRepository: RecipeRepository


    private val presenter: RegistrationPresenter by moxyPresenter {
        RegistrationPresenter(
            schedulers = schedulers,
            recipeRepository = recipeRepository
        )
    }

    companion object {
        fun getStartIntent(context: Context) = Intent(context, RegistrationActivity::class.java)
    }

    private val ui: ActivityRegistrationBinding by viewBinding()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)
        ui.registerButton.setOnClickListener {
            if (ui.login.text.toString() != "" && ui.email.text.toString() != ""
                && ui.password.text.toString() != "" && ui.matchingPassword.text.toString() != ""
                && ui.password.text.toString() == ui.matchingPassword.text.toString()){
                presenter.registration(ui.login.text.toString(), ui.password.text.toString(), ui.email.text.toString())
            } else Toast.makeText(this, "Регистрационная форма заполнена некорректно!", Toast.LENGTH_SHORT).show()
        }
        ui.cancelButton.setOnClickListener {
            startActivity(LoginActivity.getStartIntent(this))
            finish()
        }
    }

    override fun onSuccess() {
        Toast.makeText(this, "Регистрация успешна!", Toast.LENGTH_SHORT).show()
        startActivity(LoginActivity.getStartIntent(this))
        finish()
    }

    override fun showError(error: Throwable) {
        Toast.makeText(this, "Такой логин уже существует либо e-mail указан неверно!", Toast.LENGTH_SHORT).show()
    }
}