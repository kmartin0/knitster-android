package nl.kmartin.knitster.data.datasource

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.LocaleManagerCompat
import androidx.core.os.LocaleListCompat
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Observes changes to the application's selected locales.
 *
 * The initial locale state is read using [LocaleManagerCompat] so it is available
 * before AppCompat has been fully initialized. Subsequent updates are synchronized
 * from [AppCompatDelegate].
 *
 * @param context Application context used to retrieve the initial application locales.
 */
@Singleton
class AppLocaleObserver @Inject constructor(
    @param:ApplicationContext private val context: Context
) : BroadcastReceiver() {
    // Initial application locales, read via [LocaleManagerCompat] before AppCompat is initialized.
    private val _appLocales = MutableStateFlow(LocaleManagerCompat.getApplicationLocales(context))

    val appLocales: StateFlow<LocaleListCompat> = _appLocales.asStateFlow()

    /**
     * Synchronizes the application locales when a locale change broadcast is received.
     */
    override fun onReceive(context: Context, intent: Intent) {
        syncAppLocales()
    }

    /**
     * Synchronizes the observed locales with the current AppCompat application locales.
     */
    fun syncAppLocales() {
        _appLocales.value = AppCompatDelegate.getApplicationLocales()
    }
}