package com.example.hw2b11109041

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val names = resources.getStringArray(R.array.地點名稱)
            val intros = resources.getStringArray(R.array.地點介紹)
            val picture =arrayOf(R.drawable.a,R.drawable.b,R.drawable.c,R.drawable.d,R.drawable.e,R.drawable.f,R.drawable.g,R.drawable.h,R.drawable.i,R.drawable.j )
            val iTems :MutableList<iTem> = mutableListOf()
            for (index: Int in 0..9) {
                val temp = iTem(names[index],intros[index],picture[index],names[index])
                iTems.add(temp)
            }

val navController = rememberNavController()
NavHost(navController = navController, startDestination = "attractionList") {
    composable("attractionList") {
        ITemScreen(iTems) { iTem ->
            navController.navigate("iTemDetail/${iTem.name}")
        }
    }
    composable("iTemDetail/{name}") { backStackEntry ->
        val iTemName = backStackEntry.arguments?.getString("name")
        val iTem = iTems.find { it.name == iTemName }
        if (iTem != null) {
            ITemDetailScreen(iTem, navController)
        }
    }
}
}
}
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    val names = resources.getStringArray(R.array.地點名稱)
    val intros = resources.getStringArray(R.array.地點介紹)
    val picture =arrayOf(R.drawable.a,R.drawable.b,R.drawable.c,R.drawable.d,R.drawable.e,R.drawable.f,R.drawable.g,R.drawable.h,R.drawable.i,R.drawable.j )
    val iTems :MutableList<iTem> = mutableListOf()
    for (index: Int in 0..9) {
        val temp = iTem(names[index],intros[index],picture[index],names[index])
        iTems.add(temp)
    }
ITemScreen(iTems) { /* onClick handler */ }
}

@Composable
fun ITemScreen(iTems: List<iTem>, onClick: (iTem) -> Unit) {
LazyColumn {
items(iTems) { iTem ->
    ITemListItem(iTem, onClick)
        }
    }
}

@Composable
fun ITemListItem(i: iTem, onClick: (iTem) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ){
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .clickable(onClick = { onClick(i) }),
        )
        {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = i.imageResourceId),
                    contentDescription = null,
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = i.name,
                )
            }
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("QueryPermissionsNeeded")
@Composable
fun ITemDetailScreen(place: iTem, navController: NavController) {
    LazyColumn {
        item { Column(modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)) {
            Button(
                onClick = { navController.navigateUp() },
                modifier = Modifier.align(Alignment.Start)
            ){
                Text(text = "Back")
            }
            Spacer(modifier = Modifier.height(16.dp))
            Image(
                painter = painterResource(id = place.imageResourceId),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(450.dp)
                    .clip(RoundedCornerShape(8.dp))
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = place.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally),
                textAlign = TextAlign.Center,
                style = androidx.compose.ui.text.TextStyle(fontSize = 24.sp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = place.description,
            )
            Spacer(modifier = Modifier.height(16.dp))
            val context = LocalContext.current
            Button(
                onClick = {
                    val gmmIntentUri = Uri.parse("geo:0,0?q=${place.location}")
                    val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                    mapIntent.setPackage("com.google.android.apps.maps")
                    context.startActivity(mapIntent)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally),
            ) {
                Text(text = "View on Google Maps")
            }
        }
        } }

}

}
private fun showGoogleMapsUnavailableDialog(context: Context) {
AlertDialog.Builder(context)
.setTitle("Google Maps 不可用")
.setMessage("您的設備上未安裝Google Maps應用，無法顯示地圖。請安裝Google Maps或使用其他地圖應用。")
.setPositiveButton("確定") { dialog, _ ->
dialog.dismiss()
}
.show()
}