@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.myapplication

import Conversation
import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.myapplication.ui.theme.DetailScreen
import kotlinx.coroutines.*



data class Pokemon(val name: String, val url: String, val id:Int)
data class PokemonDetail(val name: String, val url: String, val id:Int,val weight:String,val height:String,val types:List<String>)

@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : AppCompatActivity() {
    private val apiService = ApiService()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fetchType()
    }

    private suspend fun fetchDataType(id:Int): List<Pokemon> {
        return lifecycleScope.async {
            val result: ApiResult = try {
                apiService.getTypePokemon(id)
            } catch (e: Exception) {
                // Handle exception as needed
                ApiResult.Error("An error occurred")
            }

            when (result) {
                is ApiResult.Success -> result.desc // Assuming desc is a List<Pokemon>
                is ApiResult.Error -> TODO()
                is ApiResult.SuccessDetail -> TODO()
            }
        }.await()
    }


    private suspend fun fetchDataAll(): List<Pokemon> {
        return lifecycleScope.async {
            val result: ApiResult = try {
                apiService.getAllPokemon()
            } catch (e: Exception) {
                // Handle exception as needed
                ApiResult.Error("An error occurred")
            }

            when (result) {
                is ApiResult.Success -> result.desc // Assuming desc is a List<Pokemon>
                is ApiResult.Error -> TODO()
                is ApiResult.SuccessDetail -> TODO()
            }
        }.await()
    }

    private suspend fun fetchDataDetailScreen(id:Int): Any {
        return lifecycleScope.async {
            val result: ApiResult = try {
                apiService.getPokemon(id)
            } catch (e: Exception) {
                // Handle exception as needed
                ApiResult.Error("An error occurred")
            }

            when (result) {
                is ApiResult.SuccessDetail -> result.desc // Assuming desc is a List<Pokemon>
                is ApiResult.Error -> Log.e("err",result.message)
                is ApiResult.Success -> TODO()
            }
        }.await()
    }


    @Composable
    fun PokemonType(id:Int,navController: NavController) {
        var completedData by remember { mutableStateOf<List<Pokemon>>(emptyList()) }

        DisposableEffect(Unit) {
            val job = lifecycleScope.launch {
                try {
                    val data = fetchDataType(id) // Replace with your asynchronous call
                    completedData = data
                    Log.d("dane", completedData.toString())
                } catch (e: Exception) {
                    // Handle exceptions
                }
            }

            onDispose {
                job.cancel()
            }
        }
        Conversation(messages = completedData, navController = navController,Modifier)
        // Use 'completedData' in your Compose UI
    }

    @Composable
    fun DataDetailScreen(id:Int,navController: NavController) {
        var completedData by remember { mutableStateOf(PokemonDetail("1","1",1,"1","1",listOf("1"))) }

        DisposableEffect(Unit) {
            val job = lifecycleScope.launch {
                try {
                    val data = fetchDataDetailScreen(id) // Replace with your asynchronous call
                    completedData = data as PokemonDetail
                    Log.d("dane", completedData.toString())
                } catch (e: Exception) {
                    // Handle exceptions
                }
            }

            onDispose {
                job.cancel()
            }
        }
        DetailScreen(
            id=id,
            names = completedData
        )
    }

    @Composable
    fun PokemonAll(id:Int,navController: NavController) {
        var completedData by remember { mutableStateOf<List<Pokemon>>(emptyList()) }

        DisposableEffect(Unit) {
            val job = lifecycleScope.launch {
                try {
                    val data = fetchDataAll() // Replace with your asynchronous call
                    completedData = data
                    Log.d("dane", completedData.toString())
                } catch (e: Exception) {
                    // Handle exceptions
                }
            }

            onDispose {
                job.cancel()
            }
        }
        Conversation(messages = completedData, navController = navController,Modifier)
    }




    @SuppressLint("CoroutineCreationDuringComposition")
    private fun fetchType() {
        lifecycleScope.launch {
            when (val result = apiService.getType()) {
                is ApiResult.Success -> {
                    setContent {
                        val navController = rememberNavController()

                        NavHost(
                            navController = navController,
                            startDestination = "List"
                        ) {
                            composable(route = "List") {
                                List(
                                    result = result.desc,
                                    navController = navController,
                                    x=0,
                                )
                            }
                            composable(
                                route = "List/{index}",
                                arguments = listOf(navArgument(name = "index") {
                                    type = NavType.IntType
                                }),
                            ) { index ->
                                List(result = result.desc, navController = navController,x=1,id=index.arguments?.getInt("index")!!)
                            }

                            composable(route = "Conversation") {
                                List(
                                    result = result.desc,
                                    navController = navController,
                                    x = 3
                                )
                            }
                            composable(
                                route = "DetailScreen/{index}",
                                arguments = listOf(navArgument(name = "index") {
                                    type = NavType.IntType
                                }),
                            ) { index ->
                                List(
                                    result = result.desc,
                                    navController = navController,
                                    x = 2,
                                    id = index.arguments?.getInt("index")!!
                                )
                            }
                        }
                    }
                }
                is ApiResult.Error -> TODO()
                is ApiResult.SuccessDetail -> TODO()
            }
        }
    }

    @Composable
    fun List(result: List<Pokemon>, navController: NavController,x:Int,id:Int=-1) {
        val drawerState = rememberDrawerState(DrawerValue.Closed)

        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                ModalDrawerSheet() {
                    Text("Pokemon type", modifier = Modifier.padding(16.dp))
                    Divider()
                    LazyColumn() {
                        items(result) { item ->
                        val id = item.id
                        NavigationDrawerItem(
                            label = { Text(text = item.name) },
                            onClick = { navController.navigate(route = "List/$id") },
                            selected = false,
                        )
                    }
                    }
                }
            },

            ) {
            when (x) {
                0 -> {PokemonAll(id = id, navController = navController)}
                1 -> {
                    PokemonType(id = id, navController = navController)
                }

                2 -> { DataDetailScreen(id = id, navController = navController) }
            3 ->  { Conversation(messages = result, navController = navController, modifier = Modifier)}
        }
    }
        }

}



