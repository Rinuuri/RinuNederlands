package dev.rinuuri.rinunederlands;

import android.app.Application
import android.content.pm.ApplicationInfo
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class MainViewModel(application: Application) : AndroidViewModel(application) {

    val simpleFlow: Flow<Entry> = flow {
        val packages = application.packageManager.getInstalledPackages(0)
        Repository.loadEnabledApps(application)
        Log.i("app", "run")
        for (pkg in packages) {
            Log.i("app", "package")
            if (pkg.applicationInfo == null)
                continue
            Log.i("app", "passed")
            val label =  pkg.applicationInfo!!.loadLabel(application.packageManager)
            emit(Entry(
                pkg.applicationInfo!!.loadIcon(application.packageManager),
                label,
                Repository.isAppEnabled(label.toString())
            ))
        }
    }

    private fun isUserApp(ai: ApplicationInfo): Boolean {
        val mask = ApplicationInfo.FLAG_SYSTEM or ApplicationInfo.FLAG_UPDATED_SYSTEM_APP
        return (ai.flags and mask) == 0
    }

    data class Entry(val icon: Drawable, val label: CharSequence, val enabled: Boolean)

    override fun onCleared() {
        Repository.saveEnabledApps(getApplication(), Repository.apps)
    }
}