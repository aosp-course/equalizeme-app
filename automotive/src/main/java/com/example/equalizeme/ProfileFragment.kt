package com.example.equalizeme

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.equalizeme.databinding.FragmentProfileBinding
import com.example.equalizeme.databinding.ItemPerfilBinding
import com.example.equalizeme.model.EqualizerInfo
import com.example.equalizeme.model.UserProfile
import com.example.equalizeme.viewmodel.MainViewModel


class ProfileFragment : Fragment() {

    private var numPerfis = 0
    private lateinit var perfilItem1: ItemPerfilBinding
    private lateinit var perfilItem2: ItemPerfilBinding
    private lateinit var perfilItem3: ItemPerfilBinding
    private lateinit var mViewModel: MainViewModel

    private var _binding: FragmentProfileBinding? = null
    // This property is only valid between onCreateView and
// onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val view = binding.root

        mViewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]

        perfilItem1 = binding.profileItem1
        perfilItem1.name.text = "Profile 1"
        perfilItem1.icon.visibility = View.GONE

        perfilItem2 = binding.profileItem2
        binding.addButton.root.setOnClickListener { adicionarPerfil() }

        binding.profileItem1.root.setOnClickListener {
            mViewModel.selectProfile(profile1)
            findNavController().navigate(R.id.action_firstFragment_to_secondFragment)
        }

        return view
    }

    private fun adicionarPerfil() {
        numPerfis++
        when (numPerfis) {
            1 -> {
                perfilItem2.root.visibility = View.VISIBLE
                perfilItem2.name.text = "Profile 2"
                perfilItem2.icon.visibility = View.GONE

                val profile2 = Profile(2, "Profile 2", Equalizer())
                mViewModel.addProfile(profile2)
            }
            2 -> {
                //val addButtonTextView = addButton.findViewById<TextView>(R.id.text_nome_perfil)
                binding.addButton.name.text = "Profile 3"
                binding.addButton.icon.visibility = View.GONE
                binding.addButton.root.visibility = View.VISIBLE
                //addButton.findViewById<ImageView>(R.id.addIcon).visibility = View.GONE
                binding.addButton.root.setOnClickListener(null) // Remove o listener

                val profile3 = Profile(3, "Profile 3", Equalizer())
                mViewModel.addProfile(profile3)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
