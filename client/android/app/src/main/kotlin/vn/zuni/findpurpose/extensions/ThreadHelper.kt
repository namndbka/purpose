package vn.zuni.findpurpose.extensions

import io.reactivex.schedulers.Schedulers
import java.util.concurrent.Executors

/**
 *
 * Created by namnd on 10-Oct-17.
 */

class ThreadHelper {

    val scheduler = Schedulers.from(Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() + 2
    ) { r ->
        val thread = Thread(r)
        thread.priority = 1
        thread
    })!!

    companion object {
        val instance = ThreadHelper()
    }
}
