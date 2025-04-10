package dev.rinuuri.rinunederlands

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityServiceInfo
import android.content.Intent
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.widget.Toast

class AppLaunchMonitor : AccessibilityService() {

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
            Log.i("service", packageName)
            Toast.makeText(this, packageName, Toast.LENGTH_SHORT).show()
            if (packageName == "com.google.android.youtube") {
                // Launch your own activity instead
                val intent = Intent(this, Popup::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
            }
        }
    }

    override fun onInterrupt() {
    }
}