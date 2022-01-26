package com.example.myapplication

import android.app.IntentService
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.Intent
import android.location.Location
import android.util.Log
import android.widget.TextView
import java.io.File
import java.io.FileOutputStream
import java.util.*
import androidx.localbroadcastmanager.content.LocalBroadcastManager

import android.os.Bundle
import android.widget.Toast
import java.io.IOException


class BluetoothService : IntentService("MyIntentService") {

    init {
        instance = this
    }
    companion object {
        private lateinit var instance: BluetoothService
        var isRunning = false

        fun stopService() {
            Log.d("BluetoothService", "Bluetooth disconnecting")
            isRunning = false
            instance.stopSelf()
        }
    }

    private fun sendMessageToActivity(msg: String) {
        val intent = Intent("intentKey")
        // You can also include some extra data.
        intent.putExtra("key", msg)
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
    }

    override fun onHandleIntent(p0: Intent?) {
        try {
            isRunning = true
            val btManager: BluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
            val btAdapter: BluetoothAdapter = btManager.getAdapter()
            val device = btAdapter.getRemoteDevice("B8:27:EB:1C:3D:95")
            val uuid: UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
            val socket = device.createRfcommSocketToServiceRecord(uuid)
            val inputStream = socket.inputStream
            val outputSteam = socket.outputStream
            try {
                socket.connect()
            } catch (e: IOException) {
                Log.d("BluetoothService", "Unable to connect to device")
                isRunning = false
                Toast.makeText(applicationContext, "Device not found", Toast.LENGTH_LONG).show()
            }

            while(isRunning) {

                if(inputStream.available() > 1){
                    val message: ByteArray = ByteArray(inputStream.available())
                    inputStream.read(message)
                    Log.d("BluetoothService", String(message))
                    sendMessageToActivity(String(message))
                }
            }
            Log.d("BluetoothService", "Socket closed")
            socket.close()

        } catch(e: InterruptedException) {
            Thread.currentThread().interrupt()
        }

    }
}