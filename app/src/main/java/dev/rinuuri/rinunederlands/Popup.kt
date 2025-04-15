package dev.rinuuri.rinunederlands

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.time.Clock


class Popup : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_popup)

        findViewById<Button>(R.id.yes).setOnClickListener {
            findViewById<TextView>(R.id.textView).text = "Ik hou van Belgiee"
        }

        findViewById<Button>(R.id.open).setOnClickListener {
            val id = intent.extras?.getString("package_id")
            if (id != null) {
                AppLaunchMonitor.unlocked[id] = Clock.systemUTC().millis()/1000+15
                Log.i("service", AppLaunchMonitor.unlocked[id].toString())
                Log.i("service", id)
                val intent = Intent(Intent.ACTION_MAIN)
                intent.setPackage(id)
                intent.addCategory(Intent.CATEGORY_LAUNCHER)
                startActivity(intent)
                finishAffinity()
                finish()
                /*val launchIntent = packageManager.getLaunchIntentForPackage(id);
                if (launchIntent != null) {
                    launchIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(launchIntent)
                } else {
                    Log.e("popup", "intent = null")
                }*/
            } else Log.e("popup", "id = null")
        }

        // Launch the target app
        //startActivity(launchIntent)
    }
}