package com.monstarlab.features.login

import androidx.lifecycle.*
import com.monstarlab.arch.extensions.LoadingAware
import com.monstarlab.arch.extensions.ViewErrorAware
import com.monstarlab.arch.extensions.onSuccess
import com.monstarlab.arch.extensions.savedstate.getStateFlow
import com.monstarlab.core.usecases.user.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel(), ViewErrorAware, LoadingAware {

    val loginResultFlow = savedStateHandle.getStateFlow(viewModelScope, "loginResult", false)

    fun login(email: String, password: String) {
        loginUseCase
            .login(email, password)
            .bindLoading(this)
            .bindError(this)
            .onSuccess {
                loginResultFlow.emit(true)
            }
            .launchIn(viewModelScope)
    }
}
