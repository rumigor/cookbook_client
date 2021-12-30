package ru.rumigor.cookbook.ui

import android.content.Context
import android.content.Intent
import androidx.work.Worker
import androidx.work.WorkerParameters

import android.content.IntentFilter

import com.google.gson.GsonBuilder

import io.reactivex.disposables.CompositeDisposable


import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import org.hildan.krossbow.stomp.StompClient
import org.hildan.krossbow.stomp.StompSession
import org.hildan.krossbow.stomp.conversions.kxserialization.withJsonConversions
import org.hildan.krossbow.stomp.subscribeText
import org.hildan.krossbow.websocket.okhttp.OkHttpWebSocketClient
import org.hildan.krossbow.websocket.sockjs.SockJSClient
import ru.rumigor.cookbook.AppPreferences
import kotlin.coroutines.CoroutineContext


const val ACTION_SEND_MSG = "ru.rumigor.cookbook.NEW_RECIPE"
const val NAME_MSG = "MSG"
const val FLAG_RECEIVER_INCLUDE_BACKGROUND = 0x01000000;
private const val LOGIN = "login"
private const val PASSCODE = "passcode"

class PushWorker(
    context: Context, workerParams: WorkerParameters,
) : Worker(context, workerParams), CoroutineScope {
    override val coroutineContext: CoroutineContext by lazy {
        Dispatchers.IO + Job()
    }


    private var mRestPingDisposable: io.reactivex.disposables.Disposable? = null
    private var mGson = GsonBuilder().create()
    private var compositeDisposable = CompositeDisposable()
    private lateinit var collectorJob: Job


    override fun doWork(): Result {
        applicationContext.registerReceiver(RecipeReceiver(), IntentFilter(ACTION_SEND_MSG))


        val client = StompClient(OkHttpWebSocketClient())
        launch {
            val session: StompSession = client.connect(
                "ws://cookbook-env.eba-ggumuimp.ap-south-1.elasticbeanstalk.com/stomp/websocket",
                AppPreferences.username,
                AppPreferences.password
            )
            val jsonStompSession = session.withJsonConversions()

            val subscription: Flow<String> = session.subscribeText("/cookbook/new")
            val intent = Intent()
            collectorJob = launch {
                subscription.collect { recipe ->
                    println(recipe)
                    intent.action = ACTION_SEND_MSG
                    intent.putExtra(NAME_MSG, recipe)
                    applicationContext.sendBroadcast(intent)
                }
            }


            session.disconnect()
        }

        return Result.success()
    }

    override fun onStopped() {
        super.onStopped()
        collectorJob.cancel()
        applicationContext.unregisterReceiver(RecipeReceiver())
        coroutineContext.cancel()
    }


}