package com.example.prioritylist.data.backend

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import kotlinx.coroutines.flow.Flow
import java.util.Calendar
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import java.util.concurrent.TimeUnit

/**
 * [UserPreferencesRepository] is a repository used for saving user settings to file with dataStore
 * @param dataStore is a reference to [DataStore] object
 */

class UserPreferencesRepository(
    private val dataStore: DataStore<Preferences>
) {

    private companion object {
        val DEADLINE_PERIOD = intPreferencesKey("deadline_period")      //preferences key to deadlinePeriod
        const val TAG = "UserPreferencesRepo"   //tag for logging exception data

    }

    val THREE_DAYS: UInt = 3u

    /**
     * [saveDeadlinePeriodDays] saves deadlinePeriod to file
     * @param deadlinePeriodDays period to save
     */

    suspend fun saveDeadlinePeriodDays(deadlinePeriodDays: UInt) {
        dataStore.edit { preferences ->
            preferences[DEADLINE_PERIOD] = deadlinePeriodDays.toInt()
        }
    }

    /**
     * [deadlinePeriodDays] is a short amount of time in millis. System uses this field to adjust showing priority in deadline list.
     * tasks with deadlines in range (date, date + deadlinePeriodMs) will be showed as high priority tasks
     */

    val deadlinePeriodDays: Flow<UInt> = dataStore.data
        .catch {
            if(it is IOException) {
                Log.e(TAG, "Error reading preferences.", it)
                emit(emptyPreferences())
            } else {
                throw it
            }
        }
        .map { preferences ->
            preferences[DEADLINE_PERIOD]?.toUInt() ?: 1u
        }
}