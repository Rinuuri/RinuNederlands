package dev.rinuuri.rinunederlands.data

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream


class AppList {

    companion object {
        var apps = ArrayList<String>()
        private val gson = Gson()

        fun save(context: Context) {
            val file = File(context.filesDir, "apps.json")
            try {
                val fos = FileOutputStream(file)
                fos.write(gson.toJson(apps).toByteArray())
                fos.close()
            } catch (e: Exception) {
                Log.e("SAVE", "Error saving apps", e)
            }
        }

        fun load(context: Context) {
            val file = File(context.filesDir, "apps.json")
            if (!(file.exists() && file.length() > 0)) return
            try {
                val fis = FileInputStream(file)
                val buffer = ByteArray(file.length().toInt())
                fis.read(buffer)
                fis.close()
                apps = gson.fromJson(String(buffer),
                    object : TypeToken<ArrayList<String>>() {}.type) as ArrayList<String>
            } catch (e: Exception) {
                Log.e("LOAD", "Error loading apps", e)
            }
        }
    }

}