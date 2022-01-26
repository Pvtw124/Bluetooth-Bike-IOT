package com.example.myapplication

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import org.w3c.dom.Text
import java.util.*
import kotlin.concurrent.fixedRateTimer
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val connectButton: Button = findViewById(R.id.connect_button)
        val disconnectButton: Button = findViewById(R.id.disconnect_button)
        val speed: TextView = findViewById(R.id.speed_textView)
        val distance: TextView = findViewById(R.id.distance_textView)
        disconnectButton.visibility = View.GONE

        val mMessageReceiver: BroadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                val message = intent.getStringExtra("key")?.split(",")

                if(message != null && message[0] != "0|0") {
                    val speedNum = message[0].split("|")[0].toInt()
                    val distanceNum = message[0].split("|")[1].toDouble()

                    speed.text = speedNum.toString() + " mph"

                    if(distanceNum/5280 > 1){ //if less than mile use feet, if greater use miles

                        distance.text = String.format("%.1f", (distanceNum/5280)) + " mi"
                    }
                    else {
                        distance.text = distanceNum.toInt().toString() + " ft"
                    }
                }
            }
        }
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, IntentFilter("intentKey"));

        connectButton.setOnClickListener {
            Intent(this, BluetoothService::class.java).also {
                startService(it)
                distance.text = "0 ft"
                distance.visibility = View.VISIBLE
                speed.text = "0 mph"
                speed.visibility = View.VISIBLE
                connectButton.visibility = View.GONE
                disconnectButton.visibility = View.VISIBLE
            }
        }
        disconnectButton.setOnClickListener {
            BluetoothService.stopService()
            connectButton.visibility = View.VISIBLE
            distance.visibility = View.GONE
            disconnectButton.visibility = View.GONE
            speed.visibility = View.GONE
        }





    }
}