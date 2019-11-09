package org.isoron.uhabits.automation

import android.content.Context
import android.content.Intent
import com.google.common.cache.Cache
import com.google.common.cache.CacheBuilder
import org.isoron.androidbase.AppContext
import java.util.concurrent.TimeUnit
import javax.inject.*

const val ACTION_REQUEST_QUERY = "com.twofortyfouram.locale.intent.action.REQUEST_QUERY"
const val EXTRA_STRING_ACTIVITY_CLASS_NAME = "com.twofortyfouram.locale.intent.extra.ACTIVITY"

class AutomationEventManager
@Inject constructor(@AppContext private val context: Context) {
    private var eventCache: Cache<Int, EventUtils.Arguments> = CacheBuilder.newBuilder()
            .maximumSize(100)
            .expireAfterWrite(10, TimeUnit.MINUTES)
            .build()

    // TODO: call this wherever a habit is checked/unchecked
    fun triggerEvent(eventData: EventUtils.Arguments) {
        val messageID = TaskerPlugin.getPositiveNonRepeatingRandomInteger()
        eventCache.put(messageID, eventData)

        val requestQueryIntent = Intent(ACTION_REQUEST_QUERY).apply {
            putExtra(EXTRA_STRING_ACTIVITY_CLASS_NAME, EditEventActivity::class.java.name)
            TaskerPlugin.Event.addPassThroughMessageID(this, messageID)
        }
        context.sendBroadcast(requestQueryIntent)
    }

    fun popEvent(messageID: Int): EventUtils.Arguments? {
        val event = eventCache.getIfPresent(messageID)
        if (event != null) {
            eventCache.invalidate(messageID)
        }
        return event
    }
}