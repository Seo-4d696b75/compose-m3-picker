package com.seo4d696b75.compose.material3.picker.sample.ui

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.seo4d696b75.compose.material3.picker.Picker
import com.seo4d696b75.compose.material3.picker.rememberPickerState
import com.seo4d696b75.compose.material3.picker.sample.ui.theme.AppTheme
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.launch

@Composable
fun InfiniteScrollPickerSection(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        val range = remember { (0..20).toPersistentList() }

        val finiteState = rememberPickerState(range)
        val infiniteState = rememberPickerState(range, isInfiniteScrollable = true)

        LaunchedEffect(infiniteState) {
            snapshotFlow { infiniteState.targetIndex }.collect {
                Log.d("InfinitePicker", "targetIndex = $it")
            }
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(48.dp),
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = "Finite",
                    style = MaterialTheme.typography.titleMedium,
                )
                Picker(
                    state = finiteState,
                )
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = "Infinite",
                    style = MaterialTheme.typography.titleMedium,
                )
                Picker(
                    state = infiniteState,
                )
            }
        }

        val scope = rememberCoroutineScope()

        Spacer(modifier = Modifier.height(64.dp))

        Text(
            text = "animateScrollToIndex()",
            style = MaterialTheme.typography.titleMedium,
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(48.dp),
        ) {
            Button(
                onClick = {
                    scope.launch {
                        finiteState.animateScrollToIndex(finiteState.currentIndex - 10)
                    }
                    scope.launch {
                        infiniteState.animateScrollToIndex(infiniteState.currentIndex - 10)
                    }
                },
            ) {
                Text("-10")
            }
            Button(
                onClick = {
                    scope.launch {
                        finiteState.animateScrollToIndex(finiteState.currentIndex + 10)
                    }
                    scope.launch {
                        infiniteState.animateScrollToIndex(infiniteState.currentIndex + 10)
                    }
                },
            ) {
                Text("+10")
            }
        }
    }
}

@Composable
@Preview
private fun InfiniteScrollPickerSectionPreview() {
    AppTheme {
        Surface {
            InfiniteScrollPickerSection()
        }
    }
}
