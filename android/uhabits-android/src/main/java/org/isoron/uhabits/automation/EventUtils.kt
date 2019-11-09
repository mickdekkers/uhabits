package org.isoron.uhabits.automation

import android.content.Intent
import org.isoron.uhabits.core.models.Habit
import org.isoron.uhabits.core.models.HabitList

object EventUtils {
    @JvmStatic
    fun parseIntent(intent: Intent, allHabits: HabitList): Arguments? {
        val bundle = intent.getBundleExtra(EXTRA_BUNDLE) ?: return null
        val event = bundle.getInt("event")
        if (event < 0 || event > 1) return null
        val habit = allHabits.getById(bundle.getLong("habit")) ?: return null
        return Arguments(event, habit)
    }

    class Arguments(var event: Int, var habit: Habit)
}