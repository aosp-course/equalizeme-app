package com.example.equalizeme

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.SeekBar
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.equalizeme.databinding.FragmentEqualizerBinding
import com.example.equalizeme.databinding.FragmentProfileBinding
import com.example.equalizeme.model.EqualizerInfo
import com.example.equalizeme.viewmodel.MainViewModel
import kotlinx.coroutines.launch
import java.util.function.Consumer

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [EqualizerFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EqualizerFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var _binding: FragmentEqualizerBinding? = null
    private val binding get() = _binding!!
    private val mViewModel by activityViewModels<MainViewModel>()
    private val TAG = "EqualizerFragment"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEqualizerBinding.inflate(inflater, container, false)
        val bassSeekBar = binding.seekBar1
        val midSeekBar = binding.seekBar2
        val trebleSeekBar = binding.seekBar3
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                mViewModel.currentEqualizer.collect {
                    val currentBass = it?.bass ?: 5
                    val currentMid = it?.mid ?: 5
                    val currentTreble = it?.treble ?: 5
                    Log.e(TAG, "Current bass: $currentBass")
                    binding.seekBar1.progress = currentBass
                    // Apply BMT equalizer values to sliders:
                    bassSeekBar.apply {
                        progress = currentBass
                        Log.d(TAG, "Bass initial progress: $progress")
                        setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                                Log.d(TAG, "Bass - onProgressChanged")
                                //Do nothing
                            }

                            override fun onStartTrackingTouch(p0: SeekBar?) {
                                //Do nothing
                            }

                            override fun onStopTrackingTouch(p0: SeekBar?) {
                                Log.d(TAG, "Bass - onStopTrackingTouch: "+(p0?.progress ?: 5))
                                mViewModel.setBass(p0?.progress ?: 5)
                            }

                        })
                    }
                    midSeekBar.apply {
                        progress = currentMid
                        setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                                //Do nothing
                            }

                            override fun onStartTrackingTouch(p0: SeekBar?) {
                                //Do nothing
                            }

                            override fun onStopTrackingTouch(p0: SeekBar?) {
                                mViewModel.setMid(p0?.progress ?: 5)
                            }

                        })
                    }
                    trebleSeekBar.apply {
                        progress = currentTreble
                        setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                                //Do nothing
                            }

                            override fun onStartTrackingTouch(p0: SeekBar?) {
                                //Do nothing
                            }

                            override fun onStopTrackingTouch(p0: SeekBar?) {
                                mViewModel.setBass(p0?.progress ?: 5)
                            }

                        })
                    }
                }
            }
        }


        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_equalizer, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment EqualizerFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            EqualizerFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
