package com.pricesurvey.exception

class ResourceNotFoundException(message: String) : RuntimeException(message)

class AuthenticationException(message: String) : RuntimeException(message)