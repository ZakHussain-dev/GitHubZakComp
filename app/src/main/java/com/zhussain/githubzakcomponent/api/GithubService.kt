package com.zhussain.githubzakcomponent.api

import androidx.lifecycle.LiveData
import com.zhussain.githubzakcomponent.dataclass.Contributor
import com.zhussain.githubzakcomponent.dataclass.Repo
import com.zhussain.githubzakcomponent.dataclass.User
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GithubService {

    //Get a single user  GET /users/:username (https://developer.github.com/v3/users/)
    @GET("/users/{username}")
    fun getUser(@Path(value = "username") userName: String) : LiveData<ApiResponse<User>>

    //Lists public repositories for the specified user. GET /users/:username/repos (https://developer.github.com/v3/repos/)
    @GET("/users/{username}/repos")
    fun getRepos(@Path("username") username: String) : LiveData<ApiResponse<List<Repo>>>


    //Get a repository GET /repos/:owner/:repo Ex: /repos/zakir/zak-dev
    @GET("/repos/{owner}/{repo}")
    fun getRepo(
        @Path("owner") owner: String,
        @Path("repo") repo: String
    ) : LiveData<ApiResponse<Repo>>

    //Lists contributors to the specified repository and sorts them by the number of commits per contributor in descending order.
    //GET /repos/:owner/:repo/contributors Ex : /repos/zakir/zakhussain-dev/contributors
    @GET("/repos/{owner}/{repo}/contributors")
    fun getContributors(
        @Path("owner") owner: String,
        @Path("repo") repo: String
    ) : LiveData<ApiResponse<List<Contributor>>>

    //When searching for repositories, you can get text match metadata for the name and description fields when you pass the 'text-match' media type
    //Search repositories GET /search/repositories (https://developer.github.com/v3/search/)

    @GET("/search/repositories")
    fun searchRepos(@Query("q") query : String) : LiveData<ApiResponse<RepoSearchResponse>>

    // TODO left only one to add. searchRepo that directly return Call<T>
    @GET("search/repositories")
    fun searchRepos(@Query("q") query: String, @Query("page") page: Int): Call<RepoSearchResponse>
}