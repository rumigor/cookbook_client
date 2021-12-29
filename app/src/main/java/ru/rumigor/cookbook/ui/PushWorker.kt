package ru.rumigor.cookbook.ui

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters

import android.content.IntentFilter
import android.util.Log
import android.widget.Toast
import com.google.gson.GsonBuilder
import io.reactivex.Completable
import io.reactivex.CompletableTransformer
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

import kotlinx.coroutines.*
import ru.rumigor.cookbook.AppPreferences
import ru.rumigor.cookbook.data.stomp.stomp.Stomp


import ru.rumigor.cookbook.data.stomp.stomp.StompClient
import ru.rumigor.cookbook.data.stomp.stomp.dto.StompHeader
import java.util.*


const val ACTION_SEND_MSG = "ru.rumigor.cookbook.NEW_RECIPE"
const val NAME_MSG = "MSG"
const val FLAG_RECEIVER_INCLUDE_BACKGROUND = 0x01000000;
private const val LOGIN = "login"
private const val PASSCODE = "passcode"

class PushWorker(
    context: Context, workerParams: WorkerParameters,
) : Worker(context, workerParams)
      {


          private var mRestPingDisposable: io.reactivex.disposables.Disposable? = null
          private var mGson = GsonBuilder().create()
          private var compositeDisposable = CompositeDisposable()


    override fun doWork(): Result {
        applicationContext.registerReceiver(RecipeReceiver(), IntentFilter(ACTION_SEND_MSG))
        val mStompClient = Stomp.over(
            Stomp.ConnectionProvider.OKHTTP, "ws://cookbook-env.eba-ggumuimp.ap-south-1.elasticbeanstalk.com/stomp/websocket"
        )
        resetSubscriptions()
        val headers = mutableListOf<StompHeader>()
        headers.add(StompHeader(LOGIN, AppPreferences.username))
        headers.add(StompHeader(PASSCODE, AppPreferences.password))
        mStompClient.withClientHeartbeat(1000).withServerHeartbeat(1000)

        val dispLifecycle = mStompClient.lifecycle()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { lifecycleEvent ->
                when (lifecycleEvent.type) {
                    OPENED -> toast("Stomp connection opened")
                    ERROR -> {
                        Log.e(
                            ua.naiksoftware.stompclientexample.MainActivity.TAG,
                            "Stomp connection error",
                            lifecycleEvent.exception
                        )
                        toast("Stomp connection error")
                    }
                    CLOSED -> {
                        toast("Stomp connection closed")
                        resetSubscriptions()
                    }
                    FAILED_SERVER_HEARTBEAT -> toast("Stomp failed server heartbeat")
                }
            }

        compositeDisposable.add(dispLifecycle)

        // Receive greetings

        // Receive greetings
        val dispTopic = mStompClient.topic("/topic/greetings")
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.single())
            .subscribe({ topicMessage ->
                Log.d(
                    ua.naiksoftware.stompclientexample.MainActivity.TAG,
                    "Received " + topicMessage.payload
                )
                addItem(mGson.fromJson(topicMessage.payload, EchoModel::class.java))
            }) { throwable ->
                Log.e(
                    ua.naiksoftware.stompclientexample.MainActivity.TAG,
                    "Error on subscribe topic",
                    throwable
                )
            }

        compositeDisposable.add(dispTopic)

        mStompClient.connect(headers)

//        val headersSetup: Map<String, String> = HashMap()
//        stomp = Stomp(
//            "ws://cookbook-env.eba-ggumuimp.ap-south-1.elasticbeanstalk.com/stomp/websocket", headersSetup
//        ) { }
//        stomp.connect()
//
//        stomp.subscribe(Subscription(
//            "cookboo/new"
//        ) { headers, body ->
//            body?.let{
//                val intent = Intent()
//                intent.action = ACTION_SEND_MSG
//                intent.putExtra(NAME_MSG, it)
//                applicationContext.sendBroadcast(intent)
//            }
//        })

//        val client = StompClient(OkHttpWebSocketClient())
//        launch {
//            val session: StompSession = client.connect(
//                "ws://cookbook-env.eba-ggumuimp.ap-south-1.elasticbeanstalk.com/stomp/websocket",
//                AppPreferences.username,
//                AppPreferences.password
//            )
//            val jsonStompSession = session.withJsonConversions()
//
//            val subscription: Flow<String> = session.subscribeText("/cookbook/new")
//            val intent = Intent()
//            val collectorJob = launch {
//                subscription.collect { recipe ->
//                    println(recipe)
//                    intent.action = ACTION_SEND_MSG
//                    intent.putExtra(NAME_MSG, recipe)
//                    applicationContext.sendBroadcast(intent)
//                }
//            }
//            collectorJob.cancel()
//
//            session.disconnect()
//        }

        return Result.success()
    }

    override fun onStopped() {
        super.onStopped()
        applicationContext.unregisterReceiver(RecipeReceiver())
//        stomp.disconnect()
    }

          private fun resetSubscriptions() {
              if (compositeDisposable != null) {
                  compositeDisposable.dispose()
              }
              compositeDisposable = CompositeDisposable()
          }
          private fun addItem(echoModel: EchoModel) {
              mDataSet.add(echoModel.getEcho().toString() + " - " + mTimeFormat.format(Date()))
              mAdapter.notifyDataSetChanged()
              mRecyclerView.smoothScrollToPosition(mDataSet.size - 1)
          }

          private fun toast(text: String) {
              Log.i(ua.naiksoftware.stompclientexample.MainActivity.TAG, text)
              Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
          }

          protected fun applySchedulers(): CompletableTransformer? {
              return CompletableTransformer { upstream: Completable ->
                  upstream
                      .unsubscribeOn(Schedulers.newThread())
                      .subscribeOn(Schedulers.io())
                      .observeOn(AndroidSchedulers.mainThread())
              }
          }
}