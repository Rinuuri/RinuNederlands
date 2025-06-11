package dev.rinuuri.rinunederlands;

import android.app.Application
import android.content.pm.ApplicationInfo
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch


class MainViewModel(application: Application) : AndroidViewModel(application) {

    val simpleFlow: Flow<Entry?> = flow {
        val packages = application.packageManager.getInstalledPackages(0)
        Repository.loadEnabledApps(application)
        val corutines = HashSet<Deferred<Entry?>>()

        for (pkg in packages) {
            coroutineScope{
                corutines.add(
                    async (Dispatchers.IO)  {
                        if (pkg.applicationInfo == null)
                            return@async null
                        val label =  pkg.applicationInfo!!.loadLabel(application.packageManager)
                        if (label == pkg.packageName) return@async null
                        val icon = pkg.applicationInfo!!.loadIcon(application.packageManager) ?: return@async null
                        Entry(
                            icon,
                            label,
                            Repository.isAppEnabled(pkg.packageName),
                            pkg.packageName
                        )
                    }
                )
            }
        }
        for (corutine in corutines) {
            emit(corutine.await() ?: continue)
        }
    }

    private fun isUserApp(ai: ApplicationInfo): Boolean {
        val mask = ApplicationInfo.FLAG_SYSTEM or ApplicationInfo.FLAG_UPDATED_SYSTEM_APP
        return (ai.flags and mask) == 0
    }

    data class Entry(val icon: Drawable, val label: CharSequence, val enabled: Boolean, val packageName: String)

    override fun onCleared() {
        Repository.saveEnabledApps(getApplication())
    }
}