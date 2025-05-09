package dev.rinuuri.rinunederlands

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityServiceInfo
import android.content.Intent
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import dev.rinuuri.rinunederlands.data.AppList
import java.time.Clock

class AppLaunchMonitor : AccessibilityService() {

    companion object {
        val unlocked = HashMap<String, Long>()
        var enabled = false
    }

    override fun onServiceConnected() {
        Log.i("service", "Service connected")
        val serviceInfo = AccessibilityServiceInfo()
        serviceInfo.eventTypes = AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED
        serviceInfo.packageNames = null
        serviceInfo.feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC
        serviceInfo.notificationTimeout = 100
        this.serviceInfo = serviceInfo
        enabled = true
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent) {
        if (event.eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            val packageName = event.packageName.toString()
            if (packageName in AppList.apps) {
                if (unlocked.containsKey(packageName)
                    && unlocked[packageName]!! > Clock.systemUTC().millis()/1000) {
                    return
                }

                val intent = Intent(this, Popup::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                intent.putExtra("package_id",packageName)
                startActivity(intent)
            }
        }
    }

    override fun onInterrupt() {
        enabled = false
    }

    override fun onDestroy() {
        enabled = false
    }
}