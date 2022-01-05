package com.ekoapp.ekosdk.uikit.chat.messages.viewModel;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000F\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0007\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010 \u001a\u00020#2\u0006\u0010$\u001a\u00020%J\u0006\u0010&\u001a\u00020#R\u001c\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\bR\u001f\u0010\t\u001a\u0010\u0012\f\u0012\n \f*\u0004\u0018\u00010\u000b0\u000b0\n\u00a2\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000eR\u0011\u0010\u000f\u001a\u00020\u0010\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u0012R\u001f\u0010\u0013\u001a\u0010\u0012\f\u0012\n \f*\u0004\u0018\u00010\u000b0\u000b0\n\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u000eR\u0011\u0010\u0015\u001a\u00020\u0010\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u0012R\u0011\u0010\u0016\u001a\u00020\u0017\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0018\u0010\u0019R\u001f\u0010\u001a\u001a\u0010\u0012\f\u0012\n \f*\u0004\u0018\u00010\u001b0\u001b0\n\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001c\u0010\u000eR\u001f\u0010\u001d\u001a\u0010\u0012\f\u0012\n \f*\u0004\u0018\u00010\u001b0\u001b0\n\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001e\u0010\u000eR\u0017\u0010\u001f\u001a\b\u0012\u0004\u0012\u00020\u001b0\n\u00a2\u0006\b\n\u0000\u001a\u0004\b \u0010\u000eR\u0011\u0010!\u001a\u00020\u0010\u00a2\u0006\b\n\u0000\u001a\u0004\b\"\u0010\u0012\u00a8\u0006\'"}, d2 = {"Lcom/ekoapp/ekosdk/uikit/chat/messages/viewModel/EkoAudioMsgViewModel;", "Lcom/ekoapp/ekosdk/uikit/chat/messages/viewModel/EkoSelectableMessageViewModel;", "()V", "audioUri", "Landroid/net/Uri;", "getAudioUri", "()Landroid/net/Uri;", "setAudioUri", "(Landroid/net/Uri;)V", "audioUrl", "Landroidx/databinding/ObservableField;", "", "kotlin.jvm.PlatformType", "getAudioUrl", "()Landroidx/databinding/ObservableField;", "buffering", "Landroidx/databinding/ObservableBoolean;", "getBuffering", "()Landroidx/databinding/ObservableBoolean;", "duration", "getDuration", "isPlaying", "progressMax", "Landroidx/databinding/ObservableInt;", "getProgressMax", "()Landroidx/databinding/ObservableInt;", "receiverFillColor", "", "getReceiverFillColor", "senderFillColor", "getSenderFillColor", "uploadProgress", "getUploadProgress", "uploading", "getUploading", "", "ekoMessage", "Lcom/ekoapp/ekosdk/message/EkoMessage;", "playButtonClicked", "chatkit_debug"})
public final class EkoAudioMsgViewModel extends com.ekoapp.ekosdk.uikit.chat.messages.viewModel.EkoSelectableMessageViewModel {
    @org.jetbrains.annotations.NotNull()
    private final androidx.databinding.ObservableField<java.lang.String> audioUrl = null;
    @org.jetbrains.annotations.Nullable()
    private android.net.Uri audioUri;
    @org.jetbrains.annotations.NotNull()
    private final androidx.databinding.ObservableBoolean isPlaying = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.databinding.ObservableField<java.lang.String> duration = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.databinding.ObservableInt progressMax = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.databinding.ObservableField<java.lang.Integer> senderFillColor = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.databinding.ObservableField<java.lang.Integer> receiverFillColor = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.databinding.ObservableBoolean uploading = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.databinding.ObservableField<java.lang.Integer> uploadProgress = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.databinding.ObservableBoolean buffering = null;
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.databinding.ObservableField<java.lang.String> getAudioUrl() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final android.net.Uri getAudioUri() {
        return null;
    }
    
    public final void setAudioUri(@org.jetbrains.annotations.Nullable()
    android.net.Uri p0) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.databinding.ObservableBoolean isPlaying() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.databinding.ObservableField<java.lang.String> getDuration() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.databinding.ObservableInt getProgressMax() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.databinding.ObservableField<java.lang.Integer> getSenderFillColor() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.databinding.ObservableField<java.lang.Integer> getReceiverFillColor() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.databinding.ObservableBoolean getUploading() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.databinding.ObservableField<java.lang.Integer> getUploadProgress() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.databinding.ObservableBoolean getBuffering() {
        return null;
    }
    
    public final void playButtonClicked() {
    }
    
    public final void getUploadProgress(@org.jetbrains.annotations.NotNull()
    com.ekoapp.ekosdk.message.EkoMessage ekoMessage) {
    }
    
    public EkoAudioMsgViewModel() {
        super();
    }
}