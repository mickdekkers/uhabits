/*
 * Copyright (C) 2015-2017 Álinson Santos Xavier <isoron@gmail.com>
 *
 * This file is part of Loop Habit Tracker.
 *
 * Loop Habit Tracker is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * Loop Habit Tracker is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
 * more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package org.isoron.uhabits.automation

import android.content.*
import org.isoron.uhabits.*
import org.isoron.uhabits.core.models.*

const val RESULT_CONDITION_SATISFIED = 16
const val RESULT_CONDITION_UNKNOWN = 18
const val ACTION_QUERY_CONDITION = "com.twofortyfouram.locale.intent.action.QUERY_CONDITION"

class QueryEventReceiver : BroadcastReceiver() {

    private lateinit var allHabits: HabitList
    private lateinit var automationEventManager: AutomationEventManager

    override fun onReceive(context: Context, intent: Intent) {
        val app = context.applicationContext as HabitsApplication

        allHabits = app.component.habitList
        automationEventManager = app.component.automationEventManager

        if (!isOrderedBroadcast()) return

        if (intent.action != ACTION_QUERY_CONDITION) {
            resultCode = RESULT_CONDITION_UNKNOWN
            return
        }

        if (EventUtils.parseIntent(intent, allHabits) == null) {
            resultCode = RESULT_CONDITION_UNKNOWN
            return
        }

        val messageID = TaskerPlugin.Event.retrievePassThroughMessageID(intent)

        if (messageID == -1) {
            resultCode = RESULT_CONDITION_UNKNOWN
            return
        }

        val event = automationEventManager.popEvent(messageID)

        if (event == null) {
            resultCode = RESULT_CONDITION_UNKNOWN
            return
        }

        resultCode = RESULT_CONDITION_SATISFIED
    }

}
