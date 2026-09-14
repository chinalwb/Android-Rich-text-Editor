package com.chinalwb.are.demo

import android.os.Bundle
import com.chinalwb.are.AREditor
import com.chinalwb.are.demo.databinding.ActivityAreMultiInstanceBinding

class ARE_MultiInstanceActivity : AREDemoBaseActivity() {

    private lateinit var binding: ActivityAreMultiInstanceBinding
    private var activeARE : AREditor? = null

    private val areFocusChangeListener = AREditor.ARE_FocusChangeListener { arEditor, hasFocus -> if (hasFocus && arEditor != null) activeARE = arEditor }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAreMultiInstanceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.are1.setAreFocusChangeListener(areFocusChangeListener)
        binding.are2.setAreFocusChangeListener(areFocusChangeListener)
        binding.are3.setAreFocusChangeListener(areFocusChangeListener)
        binding.are4.setAreFocusChangeListener(areFocusChangeListener)

        activeARE = binding.are1
    }
}
