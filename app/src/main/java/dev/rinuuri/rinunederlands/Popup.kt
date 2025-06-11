package dev.rinuuri.rinunederlands

import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import java.time.Clock


class Popup : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_popup)

        findViewById<Button>(R.id.open).setOnClickListener {
            val id = intent.extras?.getString("package_id")
            if (id != null) {
                AppLaunchMonitor.unlocked[id] = Clock.systemUTC().millis()/1000+10
                Log.i("service", AppLaunchMonitor.unlocked[id].toString())
                Log.i("service", id)
                val intent = packageManager.getLaunchIntentForPackage(id)
                startActivity(intent)
                finish()
            } else Log.e("popup", "id = null")
        }

        // Launch the target app
        //startActivity(launchIntent)
    }
}