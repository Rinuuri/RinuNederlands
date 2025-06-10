package dev.rinuuri.rinunederlands

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

class Repository {
    companion object {
        private val gson = Gson()

        var apps = HashSet<String>()

        fun setAppEnabled(app: String, enabled: Boolean, context: Context) {
            if (enabled) apps.add(app)
            else apps.remove(app)
            saveEnabledApps(context, apps)
        }

        fun isAppEnabled(app: String): Boolean = apps.contains(app)

        fun saveEnabledApps(context: Context, apps: HashSet<String>) {
            val file = File(context.filesDir, "apps.json")
            try {
                val fos = FileOutputStream(file)
                fos.write(gson.toJson(apps).toByteArray())
                fos.close()
            } catch (e: Exception) {
                Log.e("SAVE", "Error saving apps", e)
            }
        }

        fun loadEnabledApps(context: Context) {
            val file = File(context.filesDir, "apps.json")
            if (!(file.exists() && file.length() > 0)) {
                file.createNewFile()
                return
            }
            try {
                val fis = FileInputStream(file)
                val buffer = ByteArray(file.length().toInt())
                fis.read(buffer)
                fis.close()
                apps = gson.fromJson(String(buffer),
                    object : TypeToken<HashSet<String>>() {}.type
                ) as HashSet<String>
            } catch (e: Exception) {
                Log.e("LOAD", "Error loading apps", e)
                throw e
            }
        }
    }
}