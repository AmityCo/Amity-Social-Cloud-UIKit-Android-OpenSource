package com.amity.socialcloud.uikit.sample.liveChat

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.amity.socialcloud.sdk.api.chat.AmityChatClient
import com.amity.socialcloud.sdk.helper.core.coroutines.asFlow
import com.amity.socialcloud.sdk.model.chat.channel.AmityChannel
import com.amity.socialcloud.uikit.chat.compose.R
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.flow.Flow

@Composable
fun AmityLiveChatList(
    onChannelClick: (AmityChannel) -> Unit,
    modifier: Modifier = Modifier
) {
    val channels = getLiveChatList().collectAsLazyPagingItems()
    var openDialog by remember { mutableStateOf(false) }
    var chatDisplayName by remember { mutableStateOf(TextFieldValue("")) }
    var members by remember { mutableStateOf(TextFieldValue("")) }
    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .padding(0.dp, 8.dp),
    ) {
        items(
            count = channels.itemCount,
            key = channels.itemKey { it.getChannelId() }
        ) {index ->
            channels.get(index)?.let { channel ->
                ChannelItemView(channel, onChannelClick)
            }
        }
        
        // Handle loading state
        channels.apply {
            when {
                loadState.refresh is LoadState.Loading -> {
                    item { LoadingIndicator() }
                }
                loadState.append is LoadState.Loading -> {
                    item { LoadingIndicator() }
                }
            }
        }
    }
    Box(
        modifier = Modifier.fillMaxSize()
            .padding(16.dp)
    ) {
        Column(
        modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.End
        ){
            FloatingActionButtonCompose(
                onClick = {
                    openDialog = true
                }
            )
        }
    }
    if (openDialog) {
        AlertDialog(
            onDismissRequest = { openDialog = false },
            title = { Text(
                text = "Create live chat",
                color = Color.Black
            ) },
            text = {
                Column {
                    TextField(
                        value = chatDisplayName,
                        onValueChange = { chatDisplayName = it },
                        label = {
                            Text(
                                text = "Chat Display Name",
                                color = Color.Black,
                            )
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    TextField(
                        value = members,
                        onValueChange = { members = it },
                        label = {
                            Text(
                                text = "Members (eg. userId1,userId2,userId3)",
                                color = Color.Black
                            )
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(onClick = {
                    openDialog = false
                    val memberList = members.text.trim().split(",")
                    val channelName = chatDisplayName.text.trim().ifEmpty { memberList.joinToString(" & ") }
                    if (memberList.isNotEmpty()) {
                        AmityChatClient.newChannelRepository()
                            .createChannel(displayName = channelName)
                            .live()
                            .userIds(memberList)
                            .build()
                            .create()
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .doFinally {
                                chatDisplayName = TextFieldValue("")
                                members = TextFieldValue("")
                            }
                            .subscribe()
                    }
                    
                }) {
                    Text(
                        text = "Create",
                        color = Color.White
                    )
                }
            },
            dismissButton = {
                Button(onClick = { openDialog = false }) {
                    Text(
                        text = "Cancel",
                        color = Color.White
                    )
                }
            }
        )
    }
}


@Composable
fun ChannelItemView(channel: AmityChannel, onChannelClick: (AmityChannel) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp, 12.dp)
            .clickable {
                onChannelClick(channel)
            },
        horizontalArrangement = Arrangement.Start
    ) {
        val nightModeFlags = LocalContext.current.resources.configuration.uiMode and
            Configuration.UI_MODE_NIGHT_MASK
        // Display avatar
        AvatarImage(avatarUrl = channel.getAvatar()?.getUrl() ?: "")
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = channel.getDisplayName(),
            modifier = Modifier.padding(8.dp),
            fontSize = 18.sp,
            style = MaterialTheme.typography.bodySmall,
            color = when (nightModeFlags) {
                Configuration.UI_MODE_NIGHT_YES -> Color.White
                else -> Color.Black
            }
        )
    }
}

@Composable
fun AvatarImage(avatarUrl: String) {
    val url = avatarUrl.ifEmpty { null }
    AsyncImage(
        model = ImageRequest
            .Builder(LocalContext.current)
            .data(url)
            .fallback(R.drawable.amity_ic_user)
            .crossfade(true)
            .networkCachePolicy(CachePolicy.ENABLED)
            .diskCachePolicy(CachePolicy.ENABLED)
            .memoryCachePolicy(CachePolicy.ENABLED)
            .build(),
        placeholder = painterResource(R.drawable.amity_ic_user),
        contentDescription = "User avatar",
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .size(32.dp)
            .background(Color.LightGray, CircleShape)
            .clip(CircleShape)
    )
}

@Composable
fun LoadingIndicator() {
    CircularProgressIndicator(Modifier
        .size(48.dp)
        .padding(8.dp))
}

fun getLiveChatList(): Flow<PagingData<AmityChannel>> {
    return AmityChatClient.newChannelRepository()
        .getChannels()
         .types(listOf(AmityChannel.Type.LIVE))
        .includeDeleted(false)
        .build()
        .query()
        .subscribeOn(Schedulers.io())
        .asFlow()
}

@Composable
fun FloatingActionButtonCompose(onClick: () -> Unit = {}) {
    FloatingActionButton(
        shape = MaterialTheme.shapes.large.copy(CornerSize(percent = 50)),
        containerColor = Color.LightGray,
        contentColor = Color.Black,
        onClick = {
          onClick()
        },
    ) {
        Icon(Icons.Default.Add, contentDescription = null)
    }
}