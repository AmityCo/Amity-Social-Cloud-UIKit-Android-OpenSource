package com.ekoapp.ekosdk.uikit.chat.messages.adapter;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\u008a\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\f\b\u0016\u0018\u0000 L2\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u00030\u00012\u00020\u0004:\u0002LMB\u001f\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u0012\b\u0010\u0007\u001a\u0004\u0018\u00010\b\u0012\u0006\u0010\t\u001a\u00020\n\u00a2\u0006\u0002\u0010\u000bJ\u0010\u00108\u001a\u00020\u001c2\u0006\u00109\u001a\u00020\u001cH\u0016J\u0010\u0010:\u001a\u00020;2\u0006\u0010<\u001a\u00020\u0003H\u0002J\u0010\u0010=\u001a\u00020;2\u0006\u0010>\u001a\u00020\rH\u0016J\u0018\u0010?\u001a\u00020;2\u0006\u0010<\u001a\u00020\u00032\u0006\u00109\u001a\u00020\u001cH\u0016J\u0018\u0010@\u001a\u00020\u00032\u0006\u0010A\u001a\u00020B2\u0006\u0010C\u001a\u00020\u001cH\u0016J\u0010\u0010D\u001a\u00020;2\u0006\u0010<\u001a\u00020\u0003H\u0016J\u0006\u0010E\u001a\u00020;J\u0010\u0010F\u001a\u00020;2\u0006\u0010G\u001a\u00020)H\u0016J\u0006\u0010H\u001a\u00020;J\b\u0010I\u001a\u00020;H\u0002J\b\u0010J\u001a\u00020;H\u0002J\b\u0010K\u001a\u00020;H\u0002R\u000e\u0010\f\u001a\u00020\rX\u0082D\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001b\u0010\u000e\u001a\u00020\u000f8BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\u0012\u0010\u0013\u001a\u0004\b\u0010\u0010\u0011R\u001b\u0010\u0014\u001a\u00020\u00158BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\u0018\u0010\u0013\u001a\u0004\b\u0016\u0010\u0017R\u000e\u0010\u0019\u001a\u00020\u001aX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u001b\u001a\u00020\u001cX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u001d\u0010\u001e\"\u0004\b\u001f\u0010 R\u0010\u0010\u0007\u001a\u0004\u0018\u00010\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010!\u001a\u00020\"X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001b\u0010#\u001a\u00020$8BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\'\u0010\u0013\u001a\u0004\b%\u0010&R\u0010\u0010(\u001a\u0004\u0018\u00010)X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001a\u0010*\u001a\u00020\rX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b+\u0010,\"\u0004\b-\u0010.R\u000e\u0010/\u001a\u000200X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001b\u00101\u001a\u0002028BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b5\u0010\u0013\u001a\u0004\b3\u00104R\u000e\u00106\u001a\u000207X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006N"}, d2 = {"Lcom/ekoapp/ekosdk/uikit/chat/messages/adapter/EkoMessageListAdapter;", "Landroidx/paging/PagedListAdapter;", "Lcom/ekoapp/ekosdk/message/EkoMessage;", "Lcom/ekoapp/ekosdk/uikit/chat/messages/viewHolder/EkoChatMessageBaseViewHolder;", "Lcom/ekoapp/ekosdk/uikit/chat/messages/viewHolder/IAudioPlayCallback;", "vmChat", "Lcom/ekoapp/ekosdk/uikit/chat/messages/viewModel/EkoMessageListViewModel;", "iViewHolder", "Lcom/ekoapp/ekosdk/uikit/chat/messages/adapter/EkoMessageListAdapter$ICustomViewHolder;", "context", "Landroid/content/Context;", "(Lcom/ekoapp/ekosdk/uikit/chat/messages/viewModel/EkoMessageListViewModel;Lcom/ekoapp/ekosdk/uikit/chat/messages/adapter/EkoMessageListAdapter$ICustomViewHolder;Landroid/content/Context;)V", "TAG", "", "ekoClient", "Lokhttp3/OkHttpClient;", "getEkoClient", "()Lokhttp3/OkHttpClient;", "ekoClient$delegate", "Lkotlin/Lazy;", "exoPlayer", "Lcom/google/android/exoplayer2/SimpleExoPlayer;", "getExoPlayer", "()Lcom/google/android/exoplayer2/SimpleExoPlayer;", "exoPlayer$delegate", "exoPlayerListener", "Lcom/google/android/exoplayer2/Player$EventListener;", "firstCompletelyVisibleItem", "", "getFirstCompletelyVisibleItem", "()I", "setFirstCompletelyVisibleItem", "(I)V", "messageUtil", "Lcom/ekoapp/ekosdk/uikit/chat/messages/adapter/EkoMessageItemUtil;", "okHttpDataSourceFactory", "Lcom/google/android/exoplayer2/ext/okhttp/OkHttpDataSourceFactory;", "getOkHttpDataSourceFactory", "()Lcom/google/android/exoplayer2/ext/okhttp/OkHttpDataSourceFactory;", "okHttpDataSourceFactory$delegate", "playingAudioHolder", "Lcom/ekoapp/ekosdk/uikit/chat/messages/viewHolder/AudioMsgBaseViewHolder;", "playingMsgId", "getPlayingMsgId", "()Ljava/lang/String;", "setPlayingMsgId", "(Ljava/lang/String;)V", "uAmpAudioAttributes", "Lcom/google/android/exoplayer2/audio/AudioAttributes;", "uiUpdateHandler", "Landroid/os/Handler;", "getUiUpdateHandler", "()Landroid/os/Handler;", "uiUpdateHandler$delegate", "updateSeekBar", "Ljava/lang/Runnable;", "getItemViewType", "position", "handleDateAndSenderVisibility", "", "holder", "messageDeleted", "msgId", "onBindViewHolder", "onCreateViewHolder", "parent", "Landroid/view/ViewGroup;", "viewType", "onViewRecycled", "pauseAndResetPlayer", "playAudio", "vh", "releaseMediaPlayer", "resetMediaPlayer", "updateNotPlayingState", "updatePlayingState", "Companion", "ICustomViewHolder", "chatkit_debug"})
public class EkoMessageListAdapter extends androidx.paging.PagedListAdapter<com.ekoapp.ekosdk.message.EkoMessage, com.ekoapp.ekosdk.uikit.chat.messages.viewHolder.EkoChatMessageBaseViewHolder> implements com.ekoapp.ekosdk.uikit.chat.messages.viewHolder.IAudioPlayCallback {
    private final java.lang.String TAG = "EkoMessageListAdapter";
    private final com.ekoapp.ekosdk.uikit.chat.messages.adapter.EkoMessageItemUtil messageUtil = null;
    private int firstCompletelyVisibleItem = 0;
    @org.jetbrains.annotations.NotNull()
    private java.lang.String playingMsgId = "-1";
    private final com.google.android.exoplayer2.audio.AudioAttributes uAmpAudioAttributes = null;
    private final kotlin.Lazy exoPlayer$delegate = null;
    private final kotlin.Lazy ekoClient$delegate = null;
    private final kotlin.Lazy okHttpDataSourceFactory$delegate = null;
    private com.ekoapp.ekosdk.uikit.chat.messages.viewHolder.AudioMsgBaseViewHolder playingAudioHolder;
    private final kotlin.Lazy uiUpdateHandler$delegate = null;
    private final com.google.android.exoplayer2.Player.EventListener exoPlayerListener = null;
    private final java.lang.Runnable updateSeekBar = null;
    private final com.ekoapp.ekosdk.uikit.chat.messages.viewModel.EkoMessageListViewModel vmChat = null;
    private final com.ekoapp.ekosdk.uikit.chat.messages.adapter.EkoMessageListAdapter.ICustomViewHolder iViewHolder = null;
    private final android.content.Context context = null;
    private static final androidx.recyclerview.widget.DiffUtil.ItemCallback<com.ekoapp.ekosdk.message.EkoMessage> MESSAGE_DIFF_CALLBACK = null;
    public static final com.ekoapp.ekosdk.uikit.chat.messages.adapter.EkoMessageListAdapter.Companion Companion = null;
    
    public final int getFirstCompletelyVisibleItem() {
        return 0;
    }
    
    public final void setFirstCompletelyVisibleItem(int p0) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getPlayingMsgId() {
        return null;
    }
    
    public final void setPlayingMsgId(@org.jetbrains.annotations.NotNull()
    java.lang.String p0) {
    }
    
    private final com.google.android.exoplayer2.SimpleExoPlayer getExoPlayer() {
        return null;
    }
    
    private final okhttp3.OkHttpClient getEkoClient() {
        return null;
    }
    
    private final com.google.android.exoplayer2.ext.okhttp.OkHttpDataSourceFactory getOkHttpDataSourceFactory() {
        return null;
    }
    
    private final android.os.Handler getUiUpdateHandler() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    @java.lang.Override()
    public com.ekoapp.ekosdk.uikit.chat.messages.viewHolder.EkoChatMessageBaseViewHolder onCreateViewHolder(@org.jetbrains.annotations.NotNull()
    android.view.ViewGroup parent, int viewType) {
        return null;
    }
    
    @java.lang.Override()
    public void onBindViewHolder(@org.jetbrains.annotations.NotNull()
    com.ekoapp.ekosdk.uikit.chat.messages.viewHolder.EkoChatMessageBaseViewHolder holder, int position) {
    }
    
    @java.lang.Override()
    public void onViewRecycled(@org.jetbrains.annotations.NotNull()
    com.ekoapp.ekosdk.uikit.chat.messages.viewHolder.EkoChatMessageBaseViewHolder holder) {
    }
    
    @java.lang.Override()
    public int getItemViewType(int position) {
        return 0;
    }
    
    private final void handleDateAndSenderVisibility(com.ekoapp.ekosdk.uikit.chat.messages.viewHolder.EkoChatMessageBaseViewHolder holder) {
    }
    
    @java.lang.Override()
    public void playAudio(@org.jetbrains.annotations.NotNull()
    com.ekoapp.ekosdk.uikit.chat.messages.viewHolder.AudioMsgBaseViewHolder vh) {
    }
    
    @java.lang.Override()
    public void messageDeleted(@org.jetbrains.annotations.NotNull()
    java.lang.String msgId) {
    }
    
    private final void updatePlayingState() {
    }
    
    private final void updateNotPlayingState() {
    }
    
    private final void resetMediaPlayer() {
    }
    
    public final void pauseAndResetPlayer() {
    }
    
    public final void releaseMediaPlayer() {
    }
    
    public EkoMessageListAdapter(@org.jetbrains.annotations.NotNull()
    com.ekoapp.ekosdk.uikit.chat.messages.viewModel.EkoMessageListViewModel vmChat, @org.jetbrains.annotations.Nullable()
    com.ekoapp.ekosdk.uikit.chat.messages.adapter.EkoMessageListAdapter.ICustomViewHolder iViewHolder, @org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        super(null);
    }
    
    @kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\bf\u0018\u00002\u00020\u0001J\"\u0010\u0002\u001a\u0004\u0018\u00010\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\tH&\u00a8\u0006\n"}, d2 = {"Lcom/ekoapp/ekosdk/uikit/chat/messages/adapter/EkoMessageListAdapter$ICustomViewHolder;", "", "getViewHolder", "Lcom/ekoapp/ekosdk/uikit/chat/messages/viewHolder/EkoChatMessageBaseViewHolder;", "inflater", "Landroid/view/LayoutInflater;", "parent", "Landroid/view/ViewGroup;", "viewType", "", "chatkit_debug"})
    public static abstract interface ICustomViewHolder {
        
        @org.jetbrains.annotations.Nullable()
        public abstract com.ekoapp.ekosdk.uikit.chat.messages.viewHolder.EkoChatMessageBaseViewHolder getViewHolder(@org.jetbrains.annotations.NotNull()
        android.view.LayoutInflater inflater, @org.jetbrains.annotations.NotNull()
        android.view.ViewGroup parent, int viewType);
    }
    
    @kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\u0016\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u0014\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0006"}, d2 = {"Lcom/ekoapp/ekosdk/uikit/chat/messages/adapter/EkoMessageListAdapter$Companion;", "", "()V", "MESSAGE_DIFF_CALLBACK", "Landroidx/recyclerview/widget/DiffUtil$ItemCallback;", "Lcom/ekoapp/ekosdk/message/EkoMessage;", "chatkit_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}