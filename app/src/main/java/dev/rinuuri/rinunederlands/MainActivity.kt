package dev.rinuuri.rinunederlands

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.CheckBox
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val settings = findViewById<Button?>(R.id.openSettings)

        val packages = packageManager.getInstalledPackages(0)

        @SuppressLint("MissingInflatedId")
        val scrollLayout= findViewById<LinearLayout>(R.id.scrollayout)

        for (pkg in packages) {
            val checkBox = CheckBox(this)
            checkBox.text =
                if (pkg.applicationInfo != null && pkg.applicationInfo!!.nonLocalizedLabel != null) pkg.applicationInfo!!.nonLocalizedLabel.toString()
                else pkg.packageName
            checkBox.textSize = 14F
            checkBox.isChecked = false
            scrollLayout.addView(checkBox)
        }



        //settings.isActivated = false
        //if (AppLaunchMonitor.enabled) {
        //    settings.isActivated = false
        //} else {
            settings.setOnClickListener {
                val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
        //}
    }
}