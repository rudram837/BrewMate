package com.example.myapplication.Utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import java.util.Calendar

object Utility {

    fun getGreeting(): String {
        val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)

        return when (hour) {
            in 5..11 -> "Good Morning ☀️"
            in 12..16 -> "Good Afternoon 🌤️"
            in 17..20 -> "Good Evening 🌇"
            else -> "Good Night 🌙"
        }
    }

    fun getGreetingMessage(): String {
        return when (Calendar.getInstance().get(Calendar.HOUR_OF_DAY)) {
            in 5..11 -> "Grab your first coffee this morning ☕"
            in 12..16 -> "Take a refreshing coffee break ☕"
            in 17..20 -> "Relax with an evening coffee ☕"
            else -> "End your day with a warm coffee ☕"
        }
    }

    fun isInternetAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false

        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
                capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
    }
}