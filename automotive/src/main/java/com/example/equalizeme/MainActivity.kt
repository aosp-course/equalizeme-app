package com.example.equalizeme

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import com.example.equalizeme.viewmodel.MainViewModel


class MainActivity : AppCompatActivity() {
    val mViewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d("MainActivity", "bind Equalizer service")
        val intent = Intent(this@MainActivity, EqualizeMeService::class.java)
        intent.action = IEqualizeMe::class.java.name
        bindService(intent, mViewModel.mConnection, Context.BIND_AUTO_CREATE)
    }

    override fun onDestroy() {
        super.onDestroy()
        unbindService(mViewModel.mConnection)
        mViewModel.unbindService()
    }
}