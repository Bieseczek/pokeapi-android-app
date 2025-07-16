package com.example.myapplication
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.io.IOException

class ApiService {

    suspend fun getAllPokemon(): ApiResult = withContext(Dispatchers.IO) {
        val apiUrl = "https://pokeapi.co/api/v2/pokemon?limit=100000&offset=0"


        val request = Request.Builder()
            .url(apiUrl)
            .build()

        val client = OkHttpClient()

        try {
            val response = client.newCall(request).execute()
            val responseBody = response.body?.string()

            if (response.isSuccessful && responseBody != null) {
                val json = JSONObject(responseBody)
                val count = json.getInt("count")
                var i = 0
                var pokemonlist: List<Pokemon> = emptyList()
                while (i < count) {
                    val name = json.getJSONArray("results").getJSONObject(i).getString("name")
                    val url =
                        "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/"
                    val png = ".png"
                    var id = json.getJSONArray("results").getJSONObject(i).getString("url")
                    id = id.removePrefix("https://pokeapi.co/api/v2/pokemon/")
                    id = id.removeSuffix("/")
                    i++
                    pokemonlist += Pokemon(name = name, url = "$url$id$png", id = id.toInt() - 1)
                }
                ApiResult.Success(pokemonlist)
            } else {
                ApiResult.Error("Błąd API: ${response.message}")
            }
        } catch (e: IOException) {
            ApiResult.Error("Wystąpił błąd: ${e.message}")
        }
    }


    suspend fun getTypePokemon(param: Int): ApiResult = withContext(Dispatchers.IO) {
        val apiUrl = "https://pokeapi.co/api/v2/type/"
        val request = Request.Builder()
            .url("$apiUrl$param")
            .build()

        val client = OkHttpClient()
        Log.d("JD", request.toString())
        try {
            val response = client.newCall(request).execute()
            val responseBody = response.body?.string()

            if (response.isSuccessful && responseBody != null) {
                val json = JSONObject(responseBody)
                val count = json.getJSONArray("pokemon").length()
                var i = 0
                var pokemonlist: List<Pokemon> = emptyList()
                while (i < count) {
                    val name =
                        json.getJSONArray("pokemon").getJSONObject(i).getJSONObject("pokemon")
                            .getString("name")
                    val url =
                        "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/"
                    val png = ".png"
                    var id = json.getJSONArray("pokemon").getJSONObject(i).getJSONObject("pokemon")
                        .getString("url")
                    id = id.removePrefix("https://pokeapi.co/api/v2/pokemon/")
                    id = id.removeSuffix("/")
                    i++
                    pokemonlist += Pokemon(name = name, url = "$url$id$png", id = id.toInt() - 1)
                }
                ApiResult.Success(pokemonlist)

            } else {
                ApiResult.Error("Błąd API: ${response.message}")
            }
        } catch (e: IOException) {
            ApiResult.Error("Wystąpił błąd: ${e.message}")
        }
    }

    suspend fun getPokemon(param: Int): ApiResult = withContext(Dispatchers.IO) {
        val apiUrl = "https://pokeapi.co/api/v2/pokemon/"
        val request = Request.Builder()
            .url("$apiUrl$param")
            .build()

        val client = OkHttpClient()
        Log.d("JD", request.toString())
        try {
            val response = client.newCall(request).execute()
            val responseBody = response.body?.string()
            val PokemonDetail: PokemonDetail
            if (response.isSuccessful && responseBody != null) {
                val json = JSONObject(responseBody)
                val name = json.getString("name")
                val url =
                    "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/"
                val png = ".png"
                var id = json.getString("id")
                val weight = json.getString("weight")
                val height = json.getString("height")
                val count = json.getJSONArray("types").length()
                var i=0
                var types : List<String> = emptyList()
                while (i < count) {
                    types += json.getJSONArray("types").getJSONObject(i).getJSONObject("type").getString("name")
                    i++
                }
                PokemonDetail = PokemonDetail(
                    name = name,
                    url = "$url$id$png",
                    id = id.toInt(),
                    weight = weight,
                    height=height,
                    types = types
                )
                Log.d("dane",types.toString())
            ApiResult.SuccessDetail(PokemonDetail)

        } else {
        ApiResult.Error("Błąd API: ${response.message}")
    }
    } catch (e: IOException)
    {
        ApiResult.Error("Wystąpił błąd: ${e.message}")
    }
}


    suspend fun getType(): ApiResult = withContext(Dispatchers.IO) {
        val apiUrl = "https://pokeapi.co/api/v2/type/"

        val request = Request.Builder()
            .url(apiUrl)
            .build()

        val client = OkHttpClient()

        try {
            val response = client.newCall(request).execute()
            val responseBody = response.body?.string()

            if (response.isSuccessful && responseBody != null) {
                val json = JSONObject(responseBody)
                val count = json.getJSONArray("results").length()
                var i = 0
                var pokemonlist: List<Pokemon> = emptyList()
                while (i < count) {
                    val name = json.getJSONArray("results").getJSONObject(i).getString("name")
                    val url = json.getJSONArray("results").getJSONObject(i).getString("url")
                    var id = json.getJSONArray("results").getJSONObject(i).getString("url")
                    id = id.removePrefix("https://pokeapi.co/api/v2/type/")
                    id = id.removeSuffix("/")
                    i++
                    pokemonlist += Pokemon(name = name, url = url, id = id.toInt())
                }
                ApiResult.Success(pokemonlist)
            } else {
                ApiResult.Error("Błąd API: ${response.message}")
            }
        } catch (e: IOException) {
            ApiResult.Error("Wystąpił błąd: ${e.message}")
        }
    }


}

sealed class ApiResult {

    data class Success(val desc: List<Pokemon>) : ApiResult()
    data class SuccessDetail(val desc: PokemonDetail) : ApiResult()
    data class Error(val message: String) : ApiResult()
}
