package dev.rinuuri.rinunederlands

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
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
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {

    private val checkBoxes = ArrayList<ViewWrapper>()
    private lateinit var viewModel: MainViewModel;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!AppLaunchMonitor.enabled) {
            val intent = Intent(this, EnableServiceActivity::class.java)
            startActivity(intent)
            finish()
            return
        }

        val instance = this;
        setContentView(R.layout.activity_main)
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        val scrollLayout = findViewById<LinearLayout>(R.id.scrollayout)

        lifecycleScope.launch(Dispatchers.Default) {
            viewModel.simpleFlow.collect { it ->
                if (it == null) {

                    return@collect
                }
                val layout = LinearLayout(instance)
                layout.setLayoutParams(LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT))
                layout.orientation = LinearLayout.HORIZONTAL

                val checkBox = CheckBox(instance)
                checkBox.text = it.label
                checkBox.textSize = 14F
                checkBox.isChecked = it.enabled
                val pkg = it.packageName
                checkBox.setOnClickListener {
                    Repository.setAppEnabled(pkg, (it as CheckBox).isChecked, instance)
                }
                val iv = ImageView(instance)
                iv.setImageDrawable(it.icon)
                val params = ViewGroup.LayoutParams(130, 130)
                iv.setLayoutParams(params)
                iv.setPadding(0, 4,0,4)
                layout.addView(iv)
                layout.addView(checkBox)
                checkBoxes.add(ViewWrapper(layout, checkBox.text.toString()))
                launch(Dispatchers.Main) { scrollLayout.addView(layout) }
            }
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        findViewById<EditText>(R.id.search).addTextChangedListener {
            val query = it.toString()
            for (cb in checkBoxes) {
                cb.view.isVisible = cb.pkg.contains(query, ignoreCase = true)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        if (!AppLaunchMonitor.enabled) {
            val intent = Intent(this, EnableServiceActivity::class.java)
            startActivity(intent)
            finish()
        }

    }
}

class ViewWrapper (
    val view: View,
    val pkg: String
)