# Dhikr Reminder for Android

This is the native Android Studio project source for a simple spoken dhikr reminder.

## Included
- Speaks the five dhikr phrases in Arabic using Android Text-to-Speech.
- Starts repeating every five minutes after the service starts.
- Uses a foreground service and persistent notification to keep working with the screen locked.
- Start and pause buttons.
- No internet connection required after Android has an Arabic text-to-speech voice installed.

## Important
This is the **Android project source**, not a prebuilt APK. It has not been compiled or tested on your specific phone from this environment. Android Studio is needed to build and install the APK.

## Build the APK
1. Install Android Studio on a computer.
2. Open this `DhikrReminderAndroid` folder in Android Studio.
3. Let Gradle sync and install any requested Android SDK components.
4. On the phone, ensure an Arabic speech voice is installed under Settings > General management (or Accessibility) > Text-to-speech.
5. In Android Studio choose **Build > Build Bundle(s) / APK(s) > Build APK(s)**.
6. Install the generated APK on your Android phone.

## First run
1. Open Dhikr Reminder and allow notifications.
2. Tap **Start reminders**. A persistent notification should appear.
3. The first spoken reminder happens after five minutes; the phrases then rotate in order.
4. Tap **Pause reminders** to stop.
5. On some Android phones, set Settings > Apps > Dhikr Reminder > Battery to Unrestricted to reduce the chance of the system stopping the service.

## Limitations
- Android manufacturers can still stop background services; battery settings may be required.
- The service does not implement quiet hours or a custom voice selector yet.
- Speech quality and Arabic availability depend on the Android TTS engine installed.
