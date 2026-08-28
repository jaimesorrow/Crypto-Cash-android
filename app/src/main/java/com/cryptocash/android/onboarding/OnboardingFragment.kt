package com.cryptocash.android.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.cryptocash.android.databinding.FragmentOnboardingPageBinding

class OnboardingFragment : Fragment() {

    private var _binding: FragmentOnboardingPageBinding? = null
    private val binding get() = _binding!!

    companion object {
        private const val ARG_TITLE = "title"
        private const val ARG_DESC = "desc"
        private const val ARG_IMAGE = "image"

        fun newInstance(page: OnboardingPage) = OnboardingFragment().apply {
            arguments = Bundle().apply {
                putInt(ARG_TITLE, page.titleRes)
                putInt(ARG_DESC, page.descRes)
                putInt(ARG_IMAGE, page.imageRes)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOnboardingPageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let { args ->
            binding.imgOnboarding.setImageResource(args.getInt(ARG_IMAGE))
            binding.tvTitle.setText(args.getInt(ARG_TITLE))
            binding.tvDescription.setText(args.getInt(ARG_DESC))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
