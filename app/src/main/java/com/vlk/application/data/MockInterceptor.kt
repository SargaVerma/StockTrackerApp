
package com.vlk.application.data

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import android.content.Context
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Protocol
import okhttp3.ResponseBody.Companion.toResponseBody

class MockInterceptor(private val context: Context) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {

        return   try {
            val request: Request = chain.request()
            // Load mock JSON from assets
            val assetManager = context.assets
            val inputStream = assetManager.open("stock_list.json")
            val mockResponse = readStream(inputStream)

            val mediaType = "application/json".toMediaTypeOrNull()
            // Build a mock response
            return Response.Builder()
                .code(200) // HTTP status code
                .message("OK")
                .request(request)
                .protocol(Protocol.HTTP_1_1)
                .body(ResponseBody.create(mediaType, mockResponse))
                .build()
        }catch (e: IOException) {
            e.printStackTrace()
            Response.Builder()
                .code(500) // Internal Server Error or appropriate error code
                .message("Error loading mock response")
                .request(chain.request())
                .protocol(Protocol.HTTP_1_1)
                .body("".toResponseBody("text/plain".toMediaTypeOrNull()))
                .build()
        }
    }

    @Throws(IOException::class)
    private fun readStream(inputStream: InputStream): String {
        val reader = BufferedReader(InputStreamReader(inputStream, "UTF-8"))
        val builder = StringBuilder()
        var line: String?
        while ((reader.readLine().also { line = it }) != null) {
            builder.append(line).append('\n')
        }
        reader.close()
        return builder.toString()
    }
}