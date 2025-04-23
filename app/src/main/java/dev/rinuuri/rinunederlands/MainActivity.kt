package dev.rinuuri.rinunederlands

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import dev.rinuuri.rinunederlands.data.AppList


class MainActivity : AppCompatActivity() {

    private val checkBoxes = ArrayList<ViewWrapper>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppList.load(this)
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
            val layout = LinearLayout(this)
            layout.setLayoutParams(LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT))
            layout.orientation = LinearLayout.HORIZONTAL

            val checkBox = CheckBox(this)
            checkBox.text =
                if (pkg.applicationInfo != null) pkg.applicationInfo!!.loadLabel(packageManager)
                else pkg.packageName
            checkBox.textSize = 14F
            checkBox.isChecked = AppList.apps.contains(pkg.packageName)
            checkBox.setOnClickListener {
                //val pkg = (it as CheckBox).text.toString()
                if ((it as CheckBox).isChecked) AppList.apps.add(pkg.packageName)
                else AppList.apps.remove(pkg.packageName)
                }
            if (pkg.applicationInfo != null){
                val iv = ImageView(this)
                iv.setImageDrawable(pkg.applicationInfo!!.loadIcon(packageManager))
                val params = ViewGroup.LayoutParams(130, 130)
                iv.setLayoutParams(params)
                iv.setPadding(0, 4,0,4)
                layout.addView(iv)
            }
            layout.addView(checkBox)
            //scrollLayout.addView(LinearLayout(this, AttributeSet())
            scrollLayout.addView(layout)
            checkBoxes.add(ViewWrapper(layout, checkBox.text.toString()))
        }

        settings.setOnClickListener {
            val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }

        findViewById<EditText>(R.id.search).addTextChangedListener {
            val query = it.toString()
            for (cb in checkBoxes) {
                cb.view.isVisible = cb.pkg.contains(query, ignoreCase = true)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        AppList.save(this)

    }
}

class ViewWrapper (
    val view: View,
    val pkg: String
)