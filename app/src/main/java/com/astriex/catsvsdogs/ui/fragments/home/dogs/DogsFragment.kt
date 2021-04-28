package com.astriex.catsvsdogs.ui.fragments.home.dogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.astriex.catsvsdogs.R
import com.astriex.catsvsdogs.databinding.FragmentDogsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DogsFragment : Fragment(R.layout.fragment_dogs) {
    private val viewModel by viewModels<DogsViewModel>()
    private var _binding: FragmentDogsBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dogs, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentDogsBinding.bind(view)

        val adapter = DogsPhotoAdapter()
        binding.recyclerView.adapter = adapter

        viewModel.getAllDogs().observe(viewLifecycleOwner) {
            adapter.submitData(viewLifecycleOwner.lifecycle, it)
        }
    }
}