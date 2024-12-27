package com.example.equalizeme

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.equalizeme.databinding.FragmentEqualizerBinding
import com.example.equalizeme.viewmodel.MainViewModel
import kotlinx.coroutines.launch



class EqualizerFragment : Fragment() {
    private var _binding: FragmentEqualizerBinding? = null
    private val binding get() = _binding!!
    private val mViewModel by activityViewModels<MainViewModel>()
    private val TAG = "EqualizerFragment"

    /**
     * Listener for sliders changes
     */
    private val seekListener: SeekBar.OnSeekBarChangeListener = object :
        SeekBar.OnSeekBarChangeListener {
        override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
            Log.i(TAG, "progress change progress: $progress")
            when (seekBar.id) {
                R.id.seekBar1 -> {
                    mViewModel.setBass(progress)
                }

                R.id.seekBar2 -> {
                    mViewModel.setMid(progress)
                }

                R.id.seekBar3 -> {
                    mViewModel.setTreble(progress)
                }

                else -> {
                    Log.i(TAG, "unknown bar percent: $progress")
                }
            }
        }

        override fun onStartTrackingTouch(p0: SeekBar?) {
            //Do nothing
        }

        override fun onStopTrackingTouch(p0: SeekBar?) {
            //Do nothing
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEqualizerBinding.inflate(inflater, container, false)
        val bassSeekBar = binding.seekBar1
        val midSeekBar = binding.seekBar2
        val trebleSeekBar = binding.seekBar3
        val view = binding.root

        binding.backButton.setOnClickListener {
            findNavController().popBackStack()
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {

                mViewModel.currentEqualizer.collect {
                    val currentBass = it?.bass ?: 5
                    val currentMid = it?.mid ?: 5
                    val currentTreble = it?.treble ?: 5
                    Log.d(TAG, "Current progress for bass: $currentBass | mid: $currentMid | treble: $currentTreble")
                    binding.seekBar1.progress = currentBass
                    // Apply BMT equalizer values to sliders:
                    bassSeekBar.apply {
                        progress = currentBass
                        setOnSeekBarChangeListener(seekListener)
                    }
                    midSeekBar.apply {
                        progress = currentMid
                        setOnSeekBarChangeListener(seekListener)
                    }
                    trebleSeekBar.apply {
                        progress = currentTreble
                        setOnSeekBarChangeListener(seekListener)
                    }
                }

            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            mViewModel.currentProfileFlow.collect {
                Log.d(TAG, "Current name is: ${it?.name}")
                binding.selectedProfile.text = it?.name
            }
        }


        // Inflate the layout for this fragment
        return view
    }
}
