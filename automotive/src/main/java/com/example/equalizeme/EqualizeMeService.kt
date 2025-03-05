package com.example.equalizeme

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log

class EqualizeMeService : Service() {
    private val mBinder = object : IEqualizeMe.Stub() {
        override fun setBass(value: Int): Boolean {
            if(value in 0..10) {
                Log.d("EqualizeMeService", "setBass value: $value")
                return true
            }
            Log.d("EqualizeMeService", "value set for bass is out of range:  $value")
            return false
        }

        override fun setMid(value: Int): Boolean {
            if(value in 0..10) {
                Log.d("EqualizeMeService", "setMid value: $value")
                return true
            }
            Log.d("EqualizeMeService", "value set for mid is out of range:  $value")
            return false
        }

        override fun setTreble(value: Int): Boolean {
            if(value in 0..10) {
                Log.d("EqualizeMeService", "setTreble value: $value")
                return true
            }
            Log.d("EqualizeMeService", "value set for treble is out of range:  $value")
            return false
        }

    }

    override fun onBind(intent: Intent): IBinder {
        return mBinder;
    }
}