package com.shop.shop.token.dto

import com.shop.shop.token.domain.AccessToken
import com.shop.shop.token.domain.RefreshToken

class TokenResponseDto(val accessTokenCode: String,val refreshTokenCode: String) {
}