package at.cgaisl.kmparticle.android

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import at.cgaisl.kmparticle.common.PhoneDialerScreenActions
import at.cgaisl.kmparticle.common.PhoneDialerScreenSideEffect
import at.cgaisl.kmparticle.common.PhoneDialerScreenState
import at.cgaisl.kmparticle.common.PhoneDialerScreenViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@Composable
fun PhoneDialerScreen() {
    val viewModel = viewModel<PhoneDialerScreenViewModel>()
    val state by viewModel.state.collectAsState()

    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.loadUsername()

        viewModel.sideEffects.onEach { sideEffect ->
            when (sideEffect) {
                is PhoneDialerScreenSideEffect.Dial -> {
                    context.dial(sideEffect.phoneNumber)
                }
            }
        }.launchIn(this)
    }

    PhoneDialerScreenContent(
        state = state,
        actions = viewModel,
    )
}

private fun Context.dial(phoneNumber: String) {
    startActivity(Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phoneNumber")))
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhoneDialerScreenContent(
    state: PhoneDialerScreenState,
    actions: PhoneDialerScreenActions,
) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = "Hello, ${state.username}"
        )
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = state.phoneNumber,
                onValueChange = actions::inputPhoneNumber
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = actions::onDialButtonPress,
            ) {
                Text("Call")
            }
        }
    }
}