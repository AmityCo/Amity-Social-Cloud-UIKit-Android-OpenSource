package com.ekoapp.ekosdk.uikit.chat.messages.adapter;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000B\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\n\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0018\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0002J\u0010\u0010\t\u001a\u00020\u00042\b\u0010\u0005\u001a\u0004\u0018\u00010\u0006J2\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u00042\b\u0010\u0011\u001a\u0004\u0018\u00010\u00122\u0006\u0010\u0013\u001a\u00020\u0014H\u0002J*\u0010\u0015\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u00042\b\u0010\u0011\u001a\u0004\u0018\u00010\u0012H\u0002J*\u0010\u0016\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u00042\b\u0010\u0011\u001a\u0004\u0018\u00010\u0012H\u0002J*\u0010\u0017\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u00042\b\u0010\u0011\u001a\u0004\u0018\u00010\u0012H\u0002J2\u0010\u0018\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u00042\b\u0010\u0011\u001a\u0004\u0018\u00010\u00122\u0006\u0010\u0013\u001a\u00020\u0014H\u0002J*\u0010\u0019\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u00042\b\u0010\u0011\u001a\u0004\u0018\u00010\u0012H\u0002J*\u0010\u001a\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u00042\b\u0010\u0011\u001a\u0004\u0018\u00010\u0012H\u0002J*\u0010\u001b\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u00042\b\u0010\u0011\u001a\u0004\u0018\u00010\u0012H\u0002J\u0018\u0010\u001c\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000fH\u0002J0\u0010\u001d\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u00042\b\u0010\u0011\u001a\u0004\u0018\u00010\u00122\u0006\u0010\u0013\u001a\u00020\u0014\u00a8\u0006\u001e"}, d2 = {"Lcom/ekoapp/ekosdk/uikit/chat/messages/adapter/EkoMessageItemUtil;", "", "()V", "getContentType", "", "message", "Lcom/ekoapp/ekosdk/message/EkoMessage;", "isSelf", "", "getMessageType", "getReceiverAudioMsgViewHolder", "Lcom/ekoapp/ekosdk/uikit/chat/messages/viewHolder/EkoChatMessageBaseViewHolder;", "inflater", "Landroid/view/LayoutInflater;", "parent", "Landroid/view/ViewGroup;", "itemType", "iViewHolder", "Lcom/ekoapp/ekosdk/uikit/chat/messages/adapter/EkoMessageListAdapter$ICustomViewHolder;", "listener", "Lcom/ekoapp/ekosdk/uikit/chat/messages/viewHolder/IAudioPlayCallback;", "getReceiverCustomMessageViewHolder", "getReceiverImageMsgViewHolder", "getReceiverTextMsgViewHolder", "getSenderAudioMsgViewHolder", "getSenderCustomMessageViewHolder", "getSenderImageMsgViewHolder", "getSenderTextMsgViewHolder", "getUnknownMessageViewHolder", "getViewHolder", "chatkit_debug"})
public final class EkoMessageItemUtil {
    
    @org.jetbrains.annotations.NotNull()
    public final com.ekoapp.ekosdk.uikit.chat.messages.viewHolder.EkoChatMessageBaseViewHolder getViewHolder(@org.jetbrains.annotations.NotNull()
    android.view.LayoutInflater inflater, @org.jetbrains.annotations.NotNull()
    android.view.ViewGroup parent, int itemType, @org.jetbrains.annotations.Nullable()
    com.ekoapp.ekosdk.uikit.chat.messages.adapter.EkoMessageListAdapter.ICustomViewHolder iViewHolder, @org.jetbrains.annotations.NotNull()
    com.ekoapp.ekosdk.uikit.chat.messages.viewHolder.IAudioPlayCallback listener) {
        return null;
    }
    
    private final com.ekoapp.ekosdk.uikit.chat.messages.viewHolder.EkoChatMessageBaseViewHolder getReceiverTextMsgViewHolder(android.view.LayoutInflater inflater, android.view.ViewGroup parent, int itemType, com.ekoapp.ekosdk.uikit.chat.messages.adapter.EkoMessageListAdapter.ICustomViewHolder iViewHolder) {
        return null;
    }
    
    private final com.ekoapp.ekosdk.uikit.chat.messages.viewHolder.EkoChatMessageBaseViewHolder getSenderTextMsgViewHolder(android.view.LayoutInflater inflater, android.view.ViewGroup parent, int itemType, com.ekoapp.ekosdk.uikit.chat.messages.adapter.EkoMessageListAdapter.ICustomViewHolder iViewHolder) {
        return null;
    }
    
    private final com.ekoapp.ekosdk.uikit.chat.messages.viewHolder.EkoChatMessageBaseViewHolder getReceiverImageMsgViewHolder(android.view.LayoutInflater inflater, android.view.ViewGroup parent, int itemType, com.ekoapp.ekosdk.uikit.chat.messages.adapter.EkoMessageListAdapter.ICustomViewHolder iViewHolder) {
        return null;
    }
    
    private final com.ekoapp.ekosdk.uikit.chat.messages.viewHolder.EkoChatMessageBaseViewHolder getSenderImageMsgViewHolder(android.view.LayoutInflater inflater, android.view.ViewGroup parent, int itemType, com.ekoapp.ekosdk.uikit.chat.messages.adapter.EkoMessageListAdapter.ICustomViewHolder iViewHolder) {
        return null;
    }
    
    private final com.ekoapp.ekosdk.uikit.chat.messages.viewHolder.EkoChatMessageBaseViewHolder getReceiverAudioMsgViewHolder(android.view.LayoutInflater inflater, android.view.ViewGroup parent, int itemType, com.ekoapp.ekosdk.uikit.chat.messages.adapter.EkoMessageListAdapter.ICustomViewHolder iViewHolder, com.ekoapp.ekosdk.uikit.chat.messages.viewHolder.IAudioPlayCallback listener) {
        return null;
    }
    
    private final com.ekoapp.ekosdk.uikit.chat.messages.viewHolder.EkoChatMessageBaseViewHolder getSenderAudioMsgViewHolder(android.view.LayoutInflater inflater, android.view.ViewGroup parent, int itemType, com.ekoapp.ekosdk.uikit.chat.messages.adapter.EkoMessageListAdapter.ICustomViewHolder iViewHolder, com.ekoapp.ekosdk.uikit.chat.messages.viewHolder.IAudioPlayCallback listener) {
        return null;
    }
    
    private final com.ekoapp.ekosdk.uikit.chat.messages.viewHolder.EkoChatMessageBaseViewHolder getUnknownMessageViewHolder(android.view.LayoutInflater inflater, android.view.ViewGroup parent) {
        return null;
    }
    
    private final com.ekoapp.ekosdk.uikit.chat.messages.viewHolder.EkoChatMessageBaseViewHolder getSenderCustomMessageViewHolder(android.view.LayoutInflater inflater, android.view.ViewGroup parent, int itemType, com.ekoapp.ekosdk.uikit.chat.messages.adapter.EkoMessageListAdapter.ICustomViewHolder iViewHolder) {
        return null;
    }
    
    private final com.ekoapp.ekosdk.uikit.chat.messages.viewHolder.EkoChatMessageBaseViewHolder getReceiverCustomMessageViewHolder(android.view.LayoutInflater inflater, android.view.ViewGroup parent, int itemType, com.ekoapp.ekosdk.uikit.chat.messages.adapter.EkoMessageListAdapter.ICustomViewHolder iViewHolder) {
        return null;
    }
    
    public final int getMessageType(@org.jetbrains.annotations.Nullable()
    com.ekoapp.ekosdk.message.EkoMessage message) {
        return 0;
    }
    
    private final int getContentType(com.ekoapp.ekosdk.message.EkoMessage message, boolean isSelf) {
        return 0;
    }
    
    public EkoMessageItemUtil() {
        super();
    }
}