package ru.rumigor.cookbook.ui

import ru.rumigor.cookbook.data.model.ServerResponse

class ServerResponseViewModel (
    val id: Long,
    val timestamp: Long
        ) {
    object Mapper {
        fun map(serverResponse: ServerResponse) =
            ServerResponseViewModel(
                serverResponse.id,
                serverResponse.timestamp
            )
    }
}