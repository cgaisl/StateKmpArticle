package at.cgaisl.kmparticle.common

import com.rickclephas.kmm.viewmodel.KMMViewModel
import com.rickclephas.kmm.viewmodel.MutableStateFlow
import com.rickclephas.kmm.viewmodel.coroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

data class PhoneDialerScreenState(
    val username: String = "",
    val phoneNumber: String = "",
)


interface PhoneDialerScreenActions {
    fun inputPhoneNumber(number: String)
    fun onDialButtonPress()
}

sealed class PhoneDialerScreenSideEffect {
    data class Dial(val phoneNumber: String) : PhoneDialerScreenSideEffect()
}

class PhoneDialerScreenViewModel : KMMViewModel(), PhoneDialerScreenActions {
    val state = MutableStateFlow(viewModelScope, PhoneDialerScreenState())
    val sideEffects = MutableSharedFlow<PhoneDialerScreenSideEffect>()

    fun loadUsername() {
        // pretend we're loading the username from a database
        state.value = state.value.copy(username = "Christian")
    }

    override fun inputPhoneNumber(number: String) {
        state.value = state.value.copy(phoneNumber = number)
    }

    override fun onDialButtonPress() {
        viewModelScope.coroutineScope.launch {
            sideEffects.emit(PhoneDialerScreenSideEffect.Dial(state.value.phoneNumber))
        }
    }
}
