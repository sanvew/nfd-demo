package xyz.sanvew.infrastructure.http.routing

import io.ktor.http.*

open class ApiException(val httpStatus: HttpStatusCode = HttpStatusCode.BadRequest) : Exception()

class ValidationException : ApiException(httpStatus = HttpStatusCode.BadRequest)
