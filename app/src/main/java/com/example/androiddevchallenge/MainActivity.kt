/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.androiddevchallenge

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigate
import androidx.navigation.compose.rememberNavController
import com.example.androiddevchallenge.ui.theme.MyTheme
import com.example.androiddevchallenge.ui.theme.typography
import java.io.Serializable


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyTheme {
                MyApp()
            }
        }
    }
}

@Composable
fun ComposeNavigation() {

}

class Puppy(val id: Int, val name: String, val age: Int, val image: Int) : Serializable

val listPuppies = listOf<Puppy>(
    Puppy(1, "one", 3, R.drawable.one),
    Puppy(2, "two", 4, R.drawable.two),
    Puppy(3, "three", 8, R.drawable.three),
    Puppy(4, "four", 2, R.drawable.four),
    Puppy(5, "five", 2, R.drawable.five),
    Puppy(6, "six", 8, R.drawable.six),
    Puppy(7, "seven", 7, R.drawable.seven),
    Puppy(8, "eight", 8, R.drawable.eight),
    Puppy(9, "eight", 8, R.drawable.nine),

    )

// Start building your app here!
@Preview(showBackground = true)
@Composable
fun MyApp() {

    val navController = rememberNavController()
    NavHost(navController, startDestination = "home") {
        composable("home") { Home(navController) }
        composable("details") { Details(navController) }
    }

}

@Composable
fun Home(navController: NavController) {
    Surface(color = MaterialTheme.colors.background) {
        LazyColumn(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            items(
                items = listPuppies,
            ) {
                PuppyRow(it, navController)
            }


        }
    }
}

@Composable
fun PuppyRow(puppy: Puppy, navController: NavController) {
    Card(
        modifier = Modifier
            .height(100.dp)
            .padding(10.dp)
            .fillMaxWidth()
            .clickable {
                navController.currentBackStackEntry?.arguments?.putSerializable("puppy", puppy)
                navController.navigate("details")
            },
        elevation = 8.dp,
        backgroundColor = Color.White
    ) {
        Row(
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            val image: Painter = painterResource(id = puppy.image)
            Image(
                modifier = Modifier.size(100.dp),
                painter = image, contentDescription = "",
                contentScale = ContentScale.Crop
            )
            Column(Modifier.padding(10.dp)) {
                Text(
                    text = "Name: ${puppy.name}",
                    style = typography.body1
                )
                Text(
                    text = "Age: ${puppy.age} months",
                    style = typography.body2,
                    color = Color.LightGray
                )
            }

        }
    }

}

@Preview("Light Theme", widthDp = 360, heightDp = 640)
@Composable
fun LightPreview() {
    MyTheme {
        MyApp()
    }
}

@Preview("Dark Theme", widthDp = 360, heightDp = 640)
@Composable
fun DarkPreview() {
    MyTheme(darkTheme = true) {
        MyApp()
    }
}

// Start building your app here!
@Composable
fun Details(navController: NavHostController) {
    val dog = navController.previousBackStackEntry?.arguments?.get("puppy") as Puppy
    Surface(color = MaterialTheme.colors.background) {
        Column(Modifier.padding(
                20.dp
            )
        ) {
            val image: Painter = painterResource(id = dog.image)
            Image(
                modifier = Modifier
                    .padding(top = 20.dp)
                    .height(200.dp)
                    .fillMaxWidth(),
                painter = image, contentDescription = "",
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.padding(vertical = 20.dp))
            Text(
                text = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. " +
                        "Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, " +
                        "when an unknown printer took a galley of type and scrambled it to make a type specimen book." +
                        " It has survived not only five centuries, " +
                        "but also the leap into electronic typesetting, " +
                        "remaining essentially unchanged. "
            )
            Spacer(modifier = Modifier.padding(vertical = 20.dp))
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = { /* Do something! */ }

            ) {
                Text("Adopt")
            }
        }

    }
}