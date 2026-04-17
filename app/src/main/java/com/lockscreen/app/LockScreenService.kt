package com.lockscreen.app

import android.accessibilityservice.AccessibilityService
import android.view.accessibility.AccessibilityEvent

/**
 * Minimal AccessibilityService whose only job is to call
 * [GLOBAL_ACTION_LOCK_SCREEN] on demand.
 *
 * The service does not listen to any accessibility events. It simply holds
 * a static reference to itself so that [LockActivity] can trigger a lock.
 *
 * Setup: the user must enable this service once in
 * Settings → Accessibility → Lock Screen.
 */
class LockScreenService : AccessibilityService() {

    override fun onServiceConnected() {
        instance = this
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        // No events needed – this service only performs a global action.
    }

    override fun onInterrupt() {}

    override fun onDestroy() {
        if (instance === this) instance = null
        super.onDestroy()
    }

    /** Locks the device screen immediately. */
    fun lock() {
        performGlobalAction(GLOBAL_ACTION_LOCK_SCREEN)
    }

    companion object {
        /** Non-null while the service is connected; null otherwise. */
        var instance: LockScreenService? = null
            private set
    }
}
