# Add project-specific ProGuard rules here.

# Keep the AccessibilityService subclass so the system can bind to it
-keep class com.lockscreen.app.LockScreenService { *; }

# Keep the Activity so the launcher can start it
-keep class com.lockscreen.app.LockActivity { *; }
