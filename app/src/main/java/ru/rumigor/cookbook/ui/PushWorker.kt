package ru.rumigor.cookbook.ui

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.google.gson.Gson
import io.reactivex.rxjava3.disposables.Disposable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectIndexed
import okhttp3.OkHttpClient
import org.hildan.krossbow.stomp.StompClient
import org.hildan.krossbow.stomp.StompSession
import org.hildan.krossbow.stomp.conversions.kxserialization.withJsonConversions
import org.hildan.krossbow.stomp.subscribeText
import org.hildan.krossbow.websocket.okhttp.OkHttpWebSocketClient
import java.time.Duration
import kotlin.coroutines.CoroutineContext
import android.content.Intent
import kotlinx.coroutines.*


const val ACTION_SEND_MSG = "new_recipe_on_server"
const val NAME_MSG = "MSG"
const val FLAG_RECEIVER_INCLUDE_BACKGROUND = 0x01000000;

class PushWorker(context: Context, workerParams: WorkerParameters,
) : Worker(context, workerParams),
    CoroutineScope {

    override val coroutineContext: CoroutineContext by lazy {
        Dispatchers.IO + Job()
    }

    override fun doWork(): Result {
        val client = StompClient(OkHttpWebSocketClient())
        launch {
            val session: StompSession = client.connect("ws://cookbook-env.eba-ggumuimp.ap-south-1.elasticbeanstalk.com/stomp/websocket")
            val jsonStompSession = session.withJsonConversions()

            val subscription: Flow<String> = session.subscribeText("/cookbook/new")

            val collectorJob = launch {
                subscription.collect { recipe ->
                    val intent = Intent()
                    intent.action = ACTION_SEND_MSG
                    intent.putExtra(NAME_MSG, recipe)
                    intent.addFlags(Intent.FLAG_FROM_BACKGROUND)
                    applicationContext.sendBroadcast(intent)
                }
            }
            collectorJob.cancel()

            session.disconnect()
        }

        val intent2 = Intent()
        intent2.action = ACTION_SEND_MSG
        intent2.putExtra(NAME_MSG, "test")
        intent2.addFlags(Intent.FLAG_FROM_BACKGROUND)
        applicationContext.sendBroadcast(intent2)
        return Result.success()
    }

    override fun onStopped() {
        super.onStopped()
        coroutineContext.cancel()
    }
}