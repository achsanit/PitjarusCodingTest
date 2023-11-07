package com.achsanit.pitjaruscodingtest.util.mapper

import com.achsanit.pitjaruscodingtest.util.Resource
import com.achsanit.pitjaruscodingtest.util.statics.CodeError
import retrofit2.HttpException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

suspend inline fun <T, RequestType> resourceMapper(
    crossinline code: suspend () -> RequestType,
    crossinline convertData: (RequestType) -> T,
    noinline saveResult: suspend (RequestType) -> Unit = { }
): Resource<T> {
    return try {
        // fetch api
        val fetchApi = code.invoke()

        // save result to db
        saveResult(fetchApi)

        // return T as resource success
        Resource.Success(convertData(fetchApi))
    } catch (httpException: HttpException) {
        when (httpException.code()) {
            in 400..499 -> {
                Resource.Error(
                    httpException.message.toString() + "-${httpException.code()}",
                    httpException.code(),
                )
            }
            in 500..599 -> {
                Resource.ServerError(
                    httpException.message.toString() + "-${httpException.code()}",
                    httpException.code()
                )
            }
            else -> {
                Resource.Error(
                    httpException.message.toString() + "-${httpException.code()}",
                    httpException.code(),
                )
            }
        }
    } catch (e: UnknownHostException) {
        e.printStackTrace()
        Resource.Error(e.message.toString(), CodeError.NO_INTERNET_CONNECTION)
    } catch (e: SocketTimeoutException) {
        e.printStackTrace()
        Resource.Error(e.message.toString(), CodeError.REQUEST_TIME_OUT)
    } catch (e: Exception) {
        Resource.Error(e.message.toString(), CodeError.SOMETHING_WENT_WRONG)
    }
}
