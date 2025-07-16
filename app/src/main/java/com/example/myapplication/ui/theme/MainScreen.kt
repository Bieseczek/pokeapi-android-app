
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.myapplication.Pokemon

@Composable
fun Conversation(messages: List<Pokemon>, navController: NavController,modifier:Modifier) {

    LazyColumn() {
        items(messages) { message ->
            var itemIndex = message.id+1
            Row(modifier = Modifier
                .clickable(onClick = {navController.navigate(route= "DetailScreen/$itemIndex")}),
            ) {
                AsyncImage(
                    model = message.url,
                    contentDescription = "null",
                )

                Column {
                    Text(
                        text = message.name,
                    )
                    Text(
                        text = message.url,
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun categoryList(result: List<Pokemon>, navController: NavController) {

    ModalNavigationDrawer(drawerContent = {
        ModalDrawerSheet {
            Text("Pokemon type", modifier = Modifier.padding(16.dp))
            Divider()
            LazyColumn(modifier=Modifier.size(1.dp)) {
                items(result) { item ->
                    val id = result.indexOf(item)
                    NavigationDrawerItem(
                        label = { Text(text = item.name, fontSize = 1.sp) },
                        selected = false,
                        onClick =  {navController.navigate(route = "List/$id")}
                    )

                }
            }
        }
    }
    )
    {}
}

