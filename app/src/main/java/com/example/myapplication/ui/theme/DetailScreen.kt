package com.example.myapplication.ui.theme

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.myapplication.PokemonDetail

@Composable
fun DetailScreen(
    modifier: Modifier = Modifier,
    names: PokemonDetail,
    id: Int
) {
    Column(
        modifier
            .fillMaxSize()
            .padding(25.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        AsyncImage(
            model = names.url,
            contentDescription = "null",
        )
            Text(text = names.name, fontSize = 30.sp, fontWeight = FontWeight.Bold)
            var types=""
            for(i in names.types)
                types += "$i/"
            Text(text = types.removeSuffix("/"), fontSize = 30.sp, fontWeight = FontWeight.Bold)


            Text(text = "id: "+names.id.toString(), fontSize = 30.sp, fontWeight = FontWeight.Bold)
        if (names.height.toDouble()<10){
            Text(text = "Wzrost: "+(names.height.toDouble()*10).toString()+" cm", fontSize = 30.sp, fontWeight = FontWeight.Bold)
        } else {
            Text(
                text = "Wzrost: " + (names.height.toDouble()/10).toString() + " m",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold
            )
        }
            Text(text = "Waga: "+(names.weight.toDouble()/10).toString()+" kg", fontSize = 30.sp, fontWeight = FontWeight.Bold)
    }
}

