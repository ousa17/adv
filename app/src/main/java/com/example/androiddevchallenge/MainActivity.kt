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
import android.os.CountDownTimer
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.androiddevchallenge.ui.theme.MyTheme


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

// Start building your app here!
@Composable
fun MyApp() {
    Surface(color = MaterialTheme.colors.background) {
        HelloScreen()
    }
}

class HelloViewModel : ViewModel() {

    private var countDownTimer: CountDownTimer? = null

    // LiveData holds state which is observed by the UI
    // (state flows down from ViewModel)
    private val _name = MutableLiveData(0)
    val name: LiveData<Int> = _name

    // onNameChanged is an event we're defining that the UI can invoke
    // (events flow up from UI)
    fun onNameChanged(newName: Int) {
        _name.value = newName
    }

    private val _startOrReset = MutableLiveData(true)
    val startOrReset: LiveData<Boolean> = _startOrReset

    // onNameChanged is an event we're defining that the UI can invoke
    // (events flow up from UI)
    fun onToggle(toggle: Boolean, input: Long = 0) {
        _startOrReset.value = toggle
        if (toggle) {
            countDownTimer?.cancel()
            countDownTimer = object : CountDownTimer(input * 1000, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    val s = (millisUntilFinished / 1000)
                    onNameChanged(s.toInt())
                }

                override fun onFinish() {
                }
            }

            countDownTimer?.start()
        } else {
            onNameChanged(0)
            countDownTimer?.cancel()
        }
    }


}

@Composable
fun HelloScreen(helloViewModel: HelloViewModel = HelloViewModel()) {
    val progress: Int by helloViewModel.name.observeAsState(0)
    HelloContent(currentProgress = progress, helloViewModel)
}

@Composable
fun HelloContent(currentProgress: Int, viewmodel: HelloViewModel) {

    var input by rememberSaveable { mutableStateOf("") }
    var progress: Float = 0f

    Column(modifier = Modifier.padding(16.dp)) {
        if (input.toLongOrNull() != null)
            progress = 1 - Math.round(100.0f * currentProgress / input.toFloat()) / 100f
        Column(
            Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(contentAlignment = Alignment.Center) {
                CircularProgressIndicator(
                    progress = progress,
                    Modifier.size(200.dp)
                )
                Text(
                    text = "$currentProgress",
                    modifier = Modifier.padding(bottom = 8.dp),
                    style = MaterialTheme.typography.h2,
                )

            }

            Row(Modifier.padding(20.dp)) {
                Button(onClick = {
                    if (input.toLongOrNull() != null) {
                        viewmodel.onToggle(true, input.toLong())
                    }
                }) {
                    Text(
                        text = "Start",
                    )
                }

                Spacer(modifier = Modifier.padding(20.dp))
                Button(onClick = { viewmodel.onToggle(false) }) {
                    Text(
                        text = "Reset",
                    )
                }
            }
            OutlinedTextField(
                value = input,
                onValueChange = { input = it },
                label = { Text("Seconds") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)

            )


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
