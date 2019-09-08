package com.chinalwb.are.demo

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.chinalwb.are.AREditor
import kotlinx.android.synthetic.main.activity_are__multi_instance.*
import kotlinx.android.synthetic.main.activity_main.*

class ARE_MultiInstanceActivity : AppCompatActivity() {

    var activeARE : AREditor? = null

    private val areFocusChangeListener = AREditor.ARE_FocusChangeListener { arEditor, hasFocus -> if (hasFocus && arEditor != null) activeARE = arEditor }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_are__multi_instance)

        are_1.setAreFocusChangeListener(areFocusChangeListener)
        are_2.setAreFocusChangeListener(areFocusChangeListener)
        are_3.setAreFocusChangeListener(areFocusChangeListener)
        are_4.setAreFocusChangeListener(areFocusChangeListener)

        activeARE = are_1
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        this.activeARE?.onActivityResult(requestCode, resultCode, data)
    }
}
