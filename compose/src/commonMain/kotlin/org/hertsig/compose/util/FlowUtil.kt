package org.hertsig.compose.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import org.hertsig.logger.logger
import org.hertsig.util.throttle
import kotlin.time.Duration

@Composable
fun <F, T> MutableStateFlow<F>.mapAndCollect(mapping: (F) -> T): State<T> = map(mapping).collectAsState(mapping(value))

@Composable
fun <T: Any> MutableStateFlow<T>.debounceAndCollect(timeout: Duration) = throttle(timeout).collectAsState(value)
