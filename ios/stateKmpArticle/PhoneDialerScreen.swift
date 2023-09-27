import common
import Foundation
import KMMViewModelSwiftUI
import SwiftUI

struct PhoneDialerScreen: View {
    @StateViewModel var viewModel = PhoneDialerScreenViewModel()

    var body: some View {
        PhoneDialerScreenContent(
            state: viewModel.state.value,
            actions: viewModel
        )
        .task {
            viewModel.loadUsername()
            
            for await sideEffect in viewModel.sideEffects {
                switch onEnum(of: sideEffect) {
                case let .dial(data):
                    if let url = URL(string: "tel://\(data.phoneNumber)"), UIApplication.shared.canOpenURL(url) {
                        UIApplication.shared.open(url, options: [:], completionHandler: nil)
                    }
                }
            }
        }
    }
}

struct PhoneDialerScreenContent: View {
    let state: PhoneDialerScreenState
    let actions: PhoneDialerScreenActions

    var body: some View {
        VStack(alignment: .leading, spacing: 16) {
            Text("Hello, \(state.username)!")

            Spacer()

            TextField(
                "Phone number",
                text: Binding(
                    get: {
                        state.phoneNumber
                    },
                    set: {
                        actions.inputPhoneNumber(number: $0)
                    }
                )
            )
            .keyboardType(.phonePad)

            Button("Press here to dial") {
                actions.onDialButtonPress()
            }

            Spacer()
        }
        .padding()
    }
}

#Preview {
    class PhoneDialerScreenActionsMock: PhoneDialerScreenActions {
        func inputPhoneNumber(number _: String) {}
        func onDialButtonPress() {}
    }

    return PhoneDialerScreenContent(
        state: .init(
            username: "username",
            phoneNumber: "555-0123"
        ),
        actions: PhoneDialerScreenActionsMock()
    )
}
