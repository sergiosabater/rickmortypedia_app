package dev.sergiosabater.rickmortypedia.core.common.error

sealed class DomainError(message: String) : Exception(message) {
    class NetworkError : DomainError("Connection error. Check your internet.")
    class ServerError : DomainError("Server error. Please try again later.")
    class CharacterNotFound(val id: Int) : DomainError("Character with ID $id not found")
    class NoCharactersFound : DomainError("No characters were found with those criteria")
    class UnknownError : DomainError("Unknown Error")
    class NoCachedData : DomainError("No data is stored locally")
    class SyncFailed : DomainError("Error synchronizing with the server")
    class DatabaseError : DomainError("Local database error")
}