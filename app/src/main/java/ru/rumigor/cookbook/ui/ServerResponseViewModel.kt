package ru.rumigor.cookbook.ui

import ru.rumigor.cookbook.data.model.ServerResponse

class ServerResponseViewModel (
    val id: Long,
    val briefName: String,
    val name: String

        ) {
    object Mapper {
        fun map(serverResponse: ServerResponse) =
            ServerResponseViewModel(
                serverResponse.id,
                serverResponse.briefName,
                serverResponse.name
            )
    }
}