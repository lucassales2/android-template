package com.monstarlab.features.login

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.loadingFlow
import androidx.lifecycle.viewErrorFlow
import androidx.navigation.fragment.findNavController
import androidx.transition.TransitionManager
import com.monstarlab.R
import com.monstarlab.arch.extensions.collectFlow
import com.monstarlab.arch.extensions.onClick
import com.monstarlab.arch.extensions.snackErrorFlow
import com.monstarlab.arch.extensions.viewbinding.viewBinding
import com.monstarlab.arch.extensions.visibilityFlow
import com.monstarlab.databinding.FragmentLoginBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment(R.layout.fragment_login) {

    private val viewModel by viewModels<LoginViewModel>()
    private val binding by viewBinding(FragmentLoginBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.loginButton.onClick {
            viewModel.login(
                binding.loginEmailEditText.text.toString(),
                binding.loginPasswordEditText.text.toString()
            )
        }

        collectFlow(viewModel.loginResultFlow) {
            findNavController().navigate(R.id.resourceFragment)
        }

        snackErrorFlow(viewModel.viewErrorFlow, view)
        visibilityFlow(viewModel.loadingFlow, binding.loginProgressBar)

        collectFlow(viewModel.loadingFlow) { loading ->
            TransitionManager.beginDelayedTransition(binding.root)
            binding.loginEmailEditText.isEnabled = !loading
            binding.loginPasswordEditText.isEnabled = !loading
            binding.loginButton.isVisible = !loading
        }
    }
}
