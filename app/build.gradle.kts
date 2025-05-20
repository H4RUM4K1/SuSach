plugins {
    // ...existing code...
    id("com.google.gms.google-services")
}

dependencies {
    // ...existing dependencies...
    
    // Firebase
    implementation(platform("com.google.firebase:firebase-bom:32.7.0"))
    implementation("com.google.firebase:firebase-auth-ktx")
    implementation("com.google.firebase:firebase-firestore-ktx")
    implementation("com.google.firebase:firebase-analytics-ktx")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.7.3")

    // Retrofit & Gson
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.google.code.gson:gson:2.10.1") // Added Gson for parsing
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0") // Added OkHttp Logging Interceptor

    // Navigation
    implementation("androidx.navigation:navigation-compose:2.7.6")

    // Coil for image loading
    implementation("io.coil-kt:coil-compose:2.6.0")

    implementation("com.squareup.retrofit2:converter-scalars:2.9.0") // Or the latest version}
// ...existing code...