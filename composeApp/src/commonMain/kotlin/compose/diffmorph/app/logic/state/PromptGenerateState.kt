package compose.diffmorph.app.logic.state

import compose.diffmorph.app.network.response.PromptResponse

sealed class PromptGenerateState {
    data object Idle : PromptGenerateState()
    data object Loading : PromptGenerateState()
    data class Success(val response: PromptResponse) : PromptGenerateState()
    data class Error(val message: String) : PromptGenerateState()
}
