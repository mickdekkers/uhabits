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

import android.app.*
import android.content.*
import android.os.*
import org.isoron.uhabits.*
import org.isoron.uhabits.core.models.*

const val EVENT_CHECKED = 0
const val EVENT_UNCHECKED = 1

class EditEventController(private val activity: Activity) {

    fun onSave(habit: Habit, event: Int) {
        if (habit.getId() == null) return
        val eventName = getEventName(event)
        val blurb = String.format("%s: %s", eventName, habit.name)

        val bundle = Bundle()
        bundle.putInt("event", event)
        bundle.putLong("habit", habit.getId()!!)

        // TODO: check what is necessary for condition/event editor
        activity.setResult(Activity.RESULT_OK, Intent().apply {
            putExtra(EXTRA_STRING_BLURB, blurb)
            putExtra(EXTRA_BUNDLE, bundle)
        })
        activity.finish()
    }

    private fun getEventName(event: Int): String {
        when (event) {
            EVENT_CHECKED -> return activity.getString(R.string.checked)
            EVENT_UNCHECKED -> return activity.getString(R.string.unchecked)
            else -> return "???"
        }
    }
}
