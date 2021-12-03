package com.narayanagroup.assiginment.api

import com.narayanagroup.assiginment.models.Repos
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query
import retrofit2.http.Url

interface githubApi {

    @Headers(

        "user-agent: com.narayanagroup.assiginment",
        "x-github-request-id: F76F:7F9C:3D5FD2:44849F:61AA037B"

    )
    @GET
    suspend fun getPlayers(@Url url: String?,
        @Query("per_page") per_page: Int?,
        @Query("page") page: Int?,
    ): Repos

}