package com.seo4d696b75.compose.material3.picker.sample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.seo4d696b75.compose.material3.picker.sample.ui.SampleScreen
import com.seo4d696b75.compose.material3.picker.sample.ui.theme.AppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                SampleScreen()
            }
        }
    }
}
