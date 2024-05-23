package com.example.callapp

import android.os.Bundle
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.callapp.Adapter.CallSingleDetailsAdapter
import com.example.callapp.Utils.LogCallUtils
import com.example.callapp.databinding.ActivitySingleContactBinding

class SingleContactActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySingleContactBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySingleContactBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        initValues()
    }

    private fun initValues() {
        val number = intent.getStringExtra("CONTACT_PHONE")
        val name = intent.getStringExtra("CONTACT_NAME")
        if (number == null) {
            finish()
            return
        }

        val callLogUtils = LogCallUtils.getInstance(applicationContext)

        val callLogs = callLogUtils.getAllCallLogs(number)

        val adapter = CallSingleDetailsAdapter(callLogs)
        binding.recyclerViewCallLogs.adapter = adapter
        binding.recyclerViewCallLogs.layoutManager = LinearLayoutManager(this)

        binding.textViewNumber.text = number
        binding.textViewName.text = if (TextUtils.isEmpty(name)) number else name
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home)
            finish()
        return super.onOptionsItemSelected(item)
    }
}
