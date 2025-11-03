# Add project specific ProGuard rules here.

# Retrofit
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keep interface retrofit2.** { *; }

# Gson
-keep class com.google.gson.annotations.** { *; }
-keep class com.google.gson.** { *; }

# Firebase
-keep class com.google.firebase.** { *; }
