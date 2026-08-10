package nl.kmartin.knitster

import android.app.Application
import android.content.Intent
import android.content.IntentFilter
import dagger.hilt.android.HiltAndroidApp
import nl.kmartin.knitster.data.datasource.AppLocaleObserver
import javax.inject.Inject

/**
 * Application entry point.
 *
 * Initializes dependency injection and registers the application locale observer.
 */
@HiltAndroidApp
class KnitsterApplication : Application() {
    @Inject
    lateinit var appLocaleObserver: AppLocaleObserver

    override fun onCreate() {
        super.onCreate()
        registerReceiver(appLocaleObserver, IntentFilter(Intent.ACTION_LOCALE_CHANGED))
    }
}