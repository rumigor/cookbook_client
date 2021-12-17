package ru.rumigor.cookbook


import android.util.Base64
import android.util.Base64.encodeToString
import com.bumptech.glide.load.model.LazyHeaderFactory

class BasicAuthorization(
    private var username: String,
    private var password: String
): LazyHeaderFactory{
    override fun buildHeader(): String? {
        return "Basic " + encodeToString(("$username:$password").encodeToByteArray(), Base64.NO_WRAP)
    }
}