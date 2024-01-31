package com.shop.shop.token.dto

import com.shop.shop.token.domain.AccessToken
import com.shop.shop.token.domain.RefreshToken

class TokenResponseDto(val accessToken: String,val refreshToken: String) {
}