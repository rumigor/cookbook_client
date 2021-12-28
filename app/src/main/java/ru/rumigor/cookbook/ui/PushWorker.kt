package ru.rumigor.cookbook.ui

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.google.gson.Gson
import io.reactivex.rxjava3.disposables.Disposable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectIndexed
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import org.hildan.krossbow.stomp.StompClient
import org.hildan.krossbow.stomp.StompSession
import org.hildan.krossbow.stomp.conversions.kxserialization.withJsonConversions
import org.hildan.krossbow.stomp.subscribeText
import org.hildan.krossbow.websocket.okhttp.OkHttpWebSocketClient
import java.time.Duration
import kotlin.coroutines.CoroutineContext
import android.content.Intent




const val ACTION_SEND_MSG = "new_recipe_on_server"
const val NAME_MSG = "MSG"
const val FLAG_RECEIVER_INCLUDE_BACKGROUND = 0x01000000;

class PushWorker(context: Context, workerParams: WorkerParameters,
                 override val coroutineContext: CoroutineContext
) : Worker(context, workerParams),
    CoroutineScope {


    override fun doWork(): Result {
        val client = StompClient()
        launch {
            val session: StompSession = client.connect("http://cookbook-env.eba-ggumuimp.ap-south-1.elasticbeanstalk.com/stomp/")
            val jsonStompSession = session.withJsonConversions()

            val subscription: Flow<String> = session.subscribeText("http://cookbook-env.eba-ggumuimp.ap-south-1.elasticbeanstalk.com/cookbook/new")

            val collectorJob = launch {
                subscription.collect { recipe ->
                    val intent = Intent()
                    intent.action = ACTION_SEND_MSG
                    intent.putExtra(NAME_MSG, recipe)
                    intent.addFlags(Intent.FLAG_FROM_BACKGROUND)
                    applicationContext.sendBroadcast(intent)
                }
            }
        }
        return Result.success()
    }
}