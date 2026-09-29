package com.amity.socialcloud.uikit.sample

import android.content.Context
import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.ekoapp.ekosdk.internal.api.http.AmityNetworkActivity
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import retrofit2.Invocation
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Records every server call the SDK makes, so "this module is switched off" can be
 * checked against the wire and not only against the screen.
 *
 * ponytail: temporary diagnostic. One file, truncated per launch, no rotation and
 * no upload — pull it with `adb pull`. Delete this file and its one call site in
 * AmitySampleApp when the module-flag audit is done.
 *
 * The first line names the modules that were off for the run. Without it an empty
 * file cannot be told apart from a run where the tracer never started.
 */
object ApiTrace {

    private const val FILE_NAME = "amity-api-trace.jsonl"

    private val stamp = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.US)
    private var file: File? = null
    private var disposable: Disposable? = null

    fun start(context: Context) {
        if (disposable != null) return

        // Appends rather than truncates. A process that dies mid-run and restarts
        // used to take the whole trace with it, and the evidence looked like a
        // clean run rather than a lost one. The harness deletes the file before
        // launching; a second header inside one file means the app restarted, and
        // that is worth seeing.
        file = File(context.getExternalFilesDir(null), FILE_NAME)

        write("""{"ts":"${now()}","kind":"run"}""")

        disposable = AmityCoreClient.observeNetworkActivities()
            .observeOn(Schedulers.single())
            .subscribe({ activity ->
                if (activity is AmityNetworkActivity.HTTP) {
                    run {
                        val request = activity.response.request
                        val invocation = request.tag(Invocation::class.java)
                        val api = invocation
                            ?.let { "${it.method().declaringClass.simpleName}.${it.method().name}" }
                            ?: "?"
                        write(
                            """{"ts":"${now()}","kind":"http","api":"${esc(api)}",""" +
                                """"method":"${esc(request.method)}",""" +
                                """"path":"${esc(request.url.encodedPath)}",""" +
                                // The query is what tells two callers of the same
                                // endpoint apart: the clip feed asks the global
                                // feed for dataTypes[]=clip, the post feed does not.
                                """"query":"${esc(request.url.encodedQuery ?: "")}",""" +
                                """"code":${activity.response.code}}"""
                        )
                    }
                }
            }, { error ->
                write("""{"ts":"${now()}","kind":"error","message":"${esc(error.toString())}"}""")
            })
    }

    @Synchronized
    private fun write(line: String) {
        file?.appendText(line + "\n")
    }

    private fun now(): String = stamp.format(Date())

    private fun esc(value: String): String =
        value.replace("\\", "\\\\").replace("\"", "\\\"")

}
