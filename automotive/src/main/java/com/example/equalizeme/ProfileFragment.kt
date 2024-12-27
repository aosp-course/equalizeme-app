package com.example.equalizeme

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.equalizeme.databinding.FragmentProfileBinding
import com.example.equalizeme.databinding.ItemPerfilBinding
import com.example.equalizeme.viewmodel.MainViewModel
import kotlinx.coroutines.launch


class ProfileFragment : Fragment() {

    private lateinit var perfilItem1: ItemPerfilBinding
    private lateinit var perfilItem2: ItemPerfilBinding
    private lateinit var perfilItem3: ItemPerfilBinding
    private val mViewModel by activityViewModels<MainViewModel>()

    private var _binding: FragmentProfileBinding? = null
    // This property is only valid between onCreateView and
// onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val view = binding.root

        perfilItem1 = binding.profileItem1
        perfilItem2 = binding.profileItem2
        perfilItem3 = binding.profileItem3

        val profileViews = listOf(perfilItem1, perfilItem2, perfilItem3)

        // Observe profiles flow
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                Log.i("ProfileFrag", "Starting launch")
                mViewModel.profilesFlow.collect { profiles ->
                    profileViews.forEachIndexed { index, view->
                        val isAddProfileBtn = index == profiles.size
                        val profile = profiles.elementAtOrNull(index)

                        view.root.visibility = if(profile == null && !isAddProfileBtn) View.GONE else View.VISIBLE
                        view.name.text = profile?.name ?: ""
                        view.icon.visibility = if(isAddProfileBtn) View.VISIBLE else View.INVISIBLE

                        if(isAddProfileBtn) {
                            view.root.setOnClickListener {
                                mViewModel.addProfile()
                            }
                        } else if (profile != null) {
                            view.root.setOnClickListener {
                                mViewModel.selectProfile(index)
                                findNavController().navigate(R.id.action_firstFragment_to_secondFragment)
                            }
                        } else {
                            view.root.setOnClickListener {  }
                        }
                    }
                }
            }
        }

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
