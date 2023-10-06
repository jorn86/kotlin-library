package org.hertsig.compose.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import org.hertsig.util.throttle
import kotlin.time.Duration

@Composable
fun <F, T> StateFlow<F>.mapAndCollect(mapping: (F) -> T): State<T> = map(mapping).collectAsState(mapping(value))

@Composable
fun <T: Any> StateFlow<T>.debounceAndCollect(timeout: Duration) = throttle(timeout).collectAsState(value)
