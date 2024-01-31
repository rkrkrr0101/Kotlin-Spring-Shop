package com.shop.shop.exception

import com.auth0.jwt.exceptions.JWTDecodeException
import com.auth0.jwt.exceptions.TokenExpiredException
import com.shop.shop.common.ApiHeader
import com.shop.shop.common.ErrorResult
import com.shop.shop.common.Result
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.security.SignatureException


@RestControllerAdvice
class ExceptionResponseHandler {
    @ExceptionHandler(JWTDecodeException::class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    fun handleSignatureException():Result<ErrorResult> {
        val apiHeader = ApiHeader(401, "토큰이 없거나 유효하지 않습니다.")
        return Result(ErrorResult(apiHeader))
    }

    @ExceptionHandler(TokenExpiredException::class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    fun handleExpiredJwtException():Result<ErrorResult> {
        val apiHeader = ApiHeader(401, "토큰이 만료되었습니다. 다시 로그인해주세요.")
        return Result(ErrorResult(apiHeader))
    }
}