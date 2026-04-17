package com.lockscreen.app

import android.accessibilityservice.AccessibilityServiceInfo
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.accessibility.AccessibilityManager

/**
 * Transparent, no-UI Activity launched when the user taps the app icon.
 *
 * Behaviour:
 *  - If the LockScreen accessibility service is already enabled → lock the
 *    screen immediately and finish (nothing visible to the user).
 *  - Otherwise → show a one-time dialog that takes the user to the
 *    Accessibility Settings so they can enable the service.
 */
class LockActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (isAccessibilityServiceEnabled()) {
            val service = LockScreenService.instance
            if (service != null) {
                service.lock()
                finish()
            } else {
                // Service is listed as enabled but the instance is not yet
                // connected (e.g. just after a reboot). Guide the user.
                showSetupDialog()
            }
        } else {
            showSetupDialog()
        }
    }

    private fun isAccessibilityServiceEnabled(): Boolean {
        val am = getSystemService(ACCESSIBILITY_SERVICE) as AccessibilityManager
        return am.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_ALL_MASK)
            .any {
                it.resolveInfo.serviceInfo.packageName == packageName &&
                        it.resolveInfo.serviceInfo.name == LockScreenService::class.java.name
            }
    }

    private fun showSetupDialog() {
        AlertDialog.Builder(this)
            .setTitle(R.string.dialog_title)
            .setMessage(R.string.dialog_message)
            .setPositiveButton(R.string.dialog_open_settings) { _, _ ->
                startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
                finish()
            }
            .setNegativeButton(android.R.string.cancel) { _, _ -> finish() }
            .setOnCancelListener { finish() }
            .show()
    }
}
