package com.shop.shop.common

data class ApiMessage<T>(val apiBody: ApiBody<T>,val apiHeader: ApiHeader= ApiHeader()){

}
