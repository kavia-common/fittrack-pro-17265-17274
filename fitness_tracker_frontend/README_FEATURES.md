# FitTrack Android App

Features:
- User registration and login (local demo using SharedPreferences).
- Daily activity tracking (steps, distance).
- Workout logging (type, duration, calories).
- Goal setting and tracking (daily/weekly steps) with progress bars.
- Progress visualization (7-day mini bar chart).
- Reminders and notifications (daily reminder using AlarmManager).

Build/Run:
- ./gradlew :app:assembleDebug
- Install and run on device/emulator.

Notes:
- This demo uses local storage; replace repositories to integrate a real backend.
- If targeting Android 13+, request POST_NOTIFICATIONS permission at runtime as needed.
