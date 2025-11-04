# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.kts.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Keep source file names and line numbers for better stack traces
-keepattributes SourceFile,LineNumberTable

# Ktor Client
-keep class io.ktor.** { *; }
-keepclassmembers class io.ktor.** { *; }
-dontwarn io.ktor.**

# kotlinx.serialization
-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.AnnotationsKt
-dontnote kotlinx.serialization.SerializationKt
-keep,includedescriptorclasses class sk.o2.scratchcard.**$$serializer { *; }
-keepclassmembers class sk.o2.scratchcard.** {
    *** Companion;
}
-keepclasseswithmembers class sk.o2.scratchcard.** {
    kotlinx.serialization.KSerializer serializer(...);
}
-keep @kotlinx.serialization.Serializable class ** {
    *;
}

# Hilt
-keep class dagger.hilt.** { *; }
-keep class javax.inject.** { *; }
-keep class * extends dagger.hilt.android.lifecycle.HiltViewModel { *; }
-keep class * extends androidx.lifecycle.ViewModel { *; }
-keepclassmembers,allowobfuscation class * {
    @dagger.* *;
    @javax.inject.* *;
}

# Compose
-keep class androidx.compose.** { *; }
-dontwarn androidx.compose.**

# Coroutines
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}
-keepclassmembers class kotlinx.coroutines.** {
    volatile <fields>;
}

# Timber
-dontwarn org.jetbrains.annotations.**
