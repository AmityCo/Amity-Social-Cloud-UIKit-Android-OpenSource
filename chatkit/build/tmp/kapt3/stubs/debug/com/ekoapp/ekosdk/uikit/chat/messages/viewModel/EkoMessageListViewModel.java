package com.ekoapp.ekosdk.uikit.chat.messages.viewModel;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000x\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\b\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0012\u0010%\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020(0\'0&J\f\u0010)\u001a\b\u0012\u0004\u0012\u00020*0&J\u0012\u0010+\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020,0\'0&J\u0006\u0010-\u001a\u00020.J\u0016\u0010/\u001a\u0002002\u0006\u00101\u001a\u0002022\u0006\u00103\u001a\u000204J\u000e\u00105\u001a\u00020.2\u0006\u00106\u001a\u000207J\u000e\u00108\u001a\u00020.2\u0006\u00109\u001a\u000207J\u0006\u0010:\u001a\u000200J\u0006\u0010;\u001a\u000200J\u0006\u0010<\u001a\u000200J\u0006\u0010=\u001a\u000200J\u0006\u0010>\u001a\u000200R\u001a\u0010\u0003\u001a\u00020\u0004X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\bR\u0011\u0010\t\u001a\u00020\n\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\fR\u001a\u0010\r\u001a\u00020\u000eX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\r\u0010\u000f\"\u0004\b\u0010\u0010\u0011R\u0011\u0010\u0012\u001a\u00020\u0013\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0014R\u0011\u0010\u0015\u001a\u00020\u0013\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u0014R\u0011\u0010\u0016\u001a\u00020\u0013\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0016\u0010\u0014R\u0011\u0010\u0017\u001a\u00020\u0018\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0019\u0010\u001aR\u0011\u0010\u001b\u001a\u00020\u0013\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001c\u0010\u0014R\u0017\u0010\u001d\u001a\b\u0012\u0004\u0012\u00020\u00040\u001e\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001f\u0010 R\u0017\u0010!\u001a\b\u0012\u0004\u0012\u00020\u00040\u001e\u00a2\u0006\b\n\u0000\u001a\u0004\b\"\u0010 R\u0017\u0010#\u001a\b\u0012\u0004\u0012\u00020\u00040\u001e\u00a2\u0006\b\n\u0000\u001a\u0004\b$\u0010 \u00a8\u0006?"}, d2 = {"Lcom/ekoapp/ekosdk/uikit/chat/messages/viewModel/EkoMessageListViewModel;", "Lcom/ekoapp/ekosdk/uikit/chat/messages/viewModel/EkoChatMessageBaseViewModel;", "()V", "channelID", "", "getChannelID", "()Ljava/lang/String;", "setChannelID", "(Ljava/lang/String;)V", "composeBarClickListener", "Lcom/ekoapp/ekosdk/uikit/components/EkoChatComposeBarClickListener;", "getComposeBarClickListener", "()Lcom/ekoapp/ekosdk/uikit/components/EkoChatComposeBarClickListener;", "isRVScrolling", "", "()Z", "setRVScrolling", "(Z)V", "isRecording", "Landroidx/databinding/ObservableBoolean;", "()Landroidx/databinding/ObservableBoolean;", "isScrollable", "isVoiceMsgUi", "keyboardHeight", "Landroidx/databinding/ObservableInt;", "getKeyboardHeight", "()Landroidx/databinding/ObservableInt;", "showComposeBar", "getShowComposeBar", "stickyDate", "Landroidx/databinding/ObservableField;", "getStickyDate", "()Landroidx/databinding/ObservableField;", "text", "getText", "title", "getTitle", "getAllMessages", "Lio/reactivex/Flowable;", "Landroidx/paging/PagedList;", "Lcom/ekoapp/ekosdk/message/EkoMessage;", "getChannelType", "Lcom/ekoapp/ekosdk/channel/EkoChannel;", "getDisplayName", "Lcom/ekoapp/ekosdk/channel/membership/EkoChannelMembership;", "joinChannel", "Lio/reactivex/Completable;", "onRVScrollStateChanged", "", "rv", "Landroidx/recyclerview/widget/RecyclerView;", "newState", "", "sendAudioMessage", "audioFileUri", "Landroid/net/Uri;", "sendImageMessage", "imageUri", "sendMessage", "startReading", "stopReading", "toggleComposeBar", "toggleRecordingView", "chatkit_debug"})
public final class EkoMessageListViewModel extends com.ekoapp.ekosdk.uikit.chat.messages.viewModel.EkoChatMessageBaseViewModel {
    @org.jetbrains.annotations.NotNull()
    private final androidx.databinding.ObservableField<java.lang.String> text = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.databinding.ObservableField<java.lang.String> title = null;
    @org.jetbrains.annotations.NotNull()
    private java.lang.String channelID = "";
    private boolean isRVScrolling = false;
    @org.jetbrains.annotations.NotNull()
    private final androidx.databinding.ObservableBoolean isScrollable = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.databinding.ObservableField<java.lang.String> stickyDate = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.databinding.ObservableBoolean showComposeBar = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.databinding.ObservableInt keyboardHeight = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.databinding.ObservableBoolean isVoiceMsgUi = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.databinding.ObservableBoolean isRecording = null;
    @org.jetbrains.annotations.NotNull()
    private final com.ekoapp.ekosdk.uikit.components.EkoChatComposeBarClickListener composeBarClickListener = null;
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.databinding.ObservableField<java.lang.String> getText() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.databinding.ObservableField<java.lang.String> getTitle() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getChannelID() {
        return null;
    }
    
    public final void setChannelID(@org.jetbrains.annotations.NotNull()
    java.lang.String p0) {
    }
    
    public final boolean isRVScrolling() {
        return false;
    }
    
    public final void setRVScrolling(boolean p0) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.databinding.ObservableBoolean isScrollable() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.databinding.ObservableField<java.lang.String> getStickyDate() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.databinding.ObservableBoolean getShowComposeBar() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.databinding.ObservableInt getKeyboardHeight() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.databinding.ObservableBoolean isVoiceMsgUi() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.databinding.ObservableBoolean isRecording() {
        return null;
    }
    
    public final void toggleRecordingView() {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final io.reactivex.Flowable<com.ekoapp.ekosdk.channel.EkoChannel> getChannelType() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final io.reactivex.Flowable<androidx.paging.PagedList<com.ekoapp.ekosdk.channel.membership.EkoChannelMembership>> getDisplayName() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final io.reactivex.Completable joinChannel() {
        return null;
    }
    
    public final void startReading() {
    }
    
    public final void stopReading() {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final io.reactivex.Flowable<androidx.paging.PagedList<com.ekoapp.ekosdk.message.EkoMessage>> getAllMessages() {
        return null;
    }
    
    public final void sendMessage() {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final io.reactivex.Completable sendImageMessage(@org.jetbrains.annotations.NotNull()
    android.net.Uri imageUri) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final io.reactivex.Completable sendAudioMessage(@org.jetbrains.annotations.NotNull()
    android.net.Uri audioFileUri) {
        return null;
    }
    
    public final void toggleComposeBar() {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.ekoapp.ekosdk.uikit.components.EkoChatComposeBarClickListener getComposeBarClickListener() {
        return null;
    }
    
    public final void onRVScrollStateChanged(@org.jetbrains.annotations.NotNull()
    androidx.recyclerview.widget.RecyclerView rv, int newState) {
    }
    
    public EkoMessageListViewModel() {
        super();
    }
}