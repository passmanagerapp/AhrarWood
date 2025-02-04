package com.dev0029.ahrarwood.network

import com.dev0029.ahrarwood.network.model.SearchResult
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.URLProtocol
import io.ktor.http.headers
import io.ktor.http.path

object ApiService {
    private val OPEN_LIBRARY_BASE_URL =  "openlibrary.org"
    val client = ApiClient.client

    suspend fun fetchBooks(author: String) : Result<ApiResponse<SearchResult>>{
        return try {
            val response: HttpResponse = client.get {
                url {
                    protocol = URLProtocol.HTTPS
                    host = OPEN_LIBRARY_BASE_URL
                    path("search.json")
                    parameters.append("author",author)
                    parameters.append("fields","author_name,first_publish_year,isbn,publisher,title")
                    headers {
                        append(HttpHeaders.UserAgent, "AhrarWood/1.0 (passmanagerapplication@gmail.com)")
                    }
                }
            }
            if (response.status == HttpStatusCode.OK) {
                val responseBody =  response.body<SearchResult>()
                Result.success(ApiResponse(success = true, data = responseBody, error = null))
            } else {
                Result.success(ApiResponse(success = false, data = null, error = "Not found"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


}