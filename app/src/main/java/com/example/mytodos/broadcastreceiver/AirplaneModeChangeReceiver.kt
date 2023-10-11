package com.example.mytodos.broadcastreceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.widget.Toast

class AirplaneModeChangeReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == Intent.ACTION_AIRPLANE_MODE_CHANGED) {
            val isAirplaneModeOn = intent.getBooleanExtra("state", false)

            if (isAirplaneModeOn) {
                Toast.makeText(context, "Airplane mode is ON", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(context, "Airplane mode is OFF", Toast.LENGTH_LONG).show()
            }
        }
    }

}