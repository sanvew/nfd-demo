package xyz.sanvew.web

enum class ApiErrorStatus {
    Error, Success
}

class ApiError(message: String, status: ApiErrorStatus = ApiErrorStatus.Error)