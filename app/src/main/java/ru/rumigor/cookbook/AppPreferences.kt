package ru.rumigor.cookbook

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import androidx.core.content.edit

object AppPreferences {
    private var sharedPreferences: SharedPreferences? = null

    fun setup(context: Context) {
        sharedPreferences = context.getSharedPreferences("CookbookApp.sharedprefs", MODE_PRIVATE)
    }

    var username: String?
        get() = Key.USER.getString()
        set(value) = Key.USER.setString(value)

    var password: String?
        get() = Key.PASSWORD.getString()
        set(value) = Key.PASSWORD.setString(value)

    var authorized: Boolean?
    get() = Key.AUTHORIZED.getBoolean()
    set(value) = Key.AUTHORIZED.setBoolean(value)


    private enum class Key {
        USER, PASSWORD, AUTHORIZED;

        fun getString(): String? =
            if (sharedPreferences!!.contains(name)) sharedPreferences!!.getString(
                name,
                ""
            ) else null

        fun setString(value: String?) =
            value?.let {
                sharedPreferences!!.edit { putString(name, value) }
            } ?: remove()

        fun getBoolean(): Boolean? = if (sharedPreferences!!.contains(name)) sharedPreferences!!.getBoolean(name, false) else null
        fun setBoolean(value: Boolean?) = value?.let { sharedPreferences!!.edit { putBoolean(name, value) } } ?: remove()

        fun remove() = sharedPreferences!!.edit { remove(name) }
    }
}