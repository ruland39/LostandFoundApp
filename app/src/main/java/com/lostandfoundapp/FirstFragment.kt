package com.lostandfoundapp

import android.animation.ObjectAnimator
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.lostandfoundapp.databinding.FragmentFirstBinding

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private var isDetailsVisible = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // DROPDOWN FUNCTION
        _binding?.dropdownbutton?.setOnCheckedChangeListener { _, isChecked ->
            // Rotate the icon
            val rotationAngle = if (isChecked) 180f else 0f
            _binding!!.dropdownbutton.animate().rotation(rotationAngle).start()

            if (isChecked) {
                // Handle the toggle button when it is checked/on
//                Toast.makeText(context, "ON", Toast.LENGTH_SHORT).show()
                // Show details with fade-in animation
                fadeIn(_binding!!.detailsContainer)
            } else {
                // Handle the toggle button when it is unchecked/off
//                Toast.makeText(context, "OFF", Toast.LENGTH_SHORT).show()
                // Hide details with fade-out animation
                fadeOut(_binding!!.detailsContainer)
            }
        }

        binding.claimButton.setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }

    }

    private fun fadeIn(view: View) {
        if (!isDetailsVisible) {
            val animator = ObjectAnimator.ofFloat(view, "alpha", 0f, 1f)
            animator.duration = 300 // Adjust the duration of the fade-in animation as needed
            animator.start()
            view.visibility = View.VISIBLE
            isDetailsVisible = true
        }
    }

    private fun fadeOut(view: View) {
        if (isDetailsVisible) {
            val animator = ObjectAnimator.ofFloat(view, "alpha", 1f, 0f)
            animator.duration = 300 // Adjust the duration of the fade-out animation as needed
            animator.start()
            view.visibility = View.GONE
            isDetailsVisible = false
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}