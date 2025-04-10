package dev.rinuuri.rinunederlands

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class Popup : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Show your window or layout here
        // ...
        // Get the launch intent of the target app
        val launchIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("launch_intent", Intent::class.java)
        } else {
            intent.getParcelableExtra("launch_intent");
        }

        findViewById<Button>(R.id.yes).setOnClickListener {
            findViewById<TextView>(R.id.textView).text = "Ik hou van Belgee"
        }

        // Launch the target app
        startActivity(launchIntent)
    }
}