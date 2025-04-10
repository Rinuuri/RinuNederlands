package dev.rinuuri.rinunederlands

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityServiceInfo
import android.content.Intent
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import java.time.Clock

class AppLaunchMonitor : AccessibilityService() {

    companion object {
        val unlocked = HashMap<String, Long>()
    }

    override fun onServiceConnected() {
        Log.i("service", "Service connected")

        val serviceInfo = AccessibilityServiceInfo()
        serviceInfo.eventTypes = AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED
        serviceInfo.packageNames = null
        serviceInfo.feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC
        serviceInfo.notificationTimeout = 100
        this.serviceInfo = serviceInfo
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent) {
        if (event.eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            val packageName = event.packageName.toString()
            //Toast.makeText(this, packageName, Toast.LENGTH_SHORT).show()
            if (packageName == "com.google.android.youtube") {
                val i =unlocked[packageName].toString();
                val t = Clock.systemUTC().millis()/1000;
                Log.i("service", "111 $i $t")
                if (unlocked.containsKey(packageName)
                    && unlocked[packageName]!! > Clock.systemUTC().millis()/1000) {
                    return
                }

                // Launch your own activity instead
                val intent = Intent(this, Popup::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                intent.putExtra("package_id",packageName)
                startActivity(intent)
            }
        }
    }

    override fun onInterrupt() {
    }
}