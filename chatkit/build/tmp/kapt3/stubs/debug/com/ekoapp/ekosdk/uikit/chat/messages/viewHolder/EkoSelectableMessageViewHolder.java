package com.ekoapp.ekosdk.uikit.chat.messages.viewHolder;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0007\b&\u0018\u00002\u00020\u0001B\u001d\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\u0002\u0010\bJ\b\u0010\t\u001a\u00020\nH\u0002J\b\u0010\u000b\u001a\u00020\nH\u0002J\b\u0010\f\u001a\u00020\nH\u0002J\u0010\u0010\r\u001a\u00020\n2\u0006\u0010\u000e\u001a\u00020\u000fH\u0016J\u0010\u0010\u0010\u001a\u00020\n2\u0006\u0010\u0011\u001a\u00020\u000fH&J\b\u0010\u0012\u001a\u00020\nH\u0002J\b\u0010\u0013\u001a\u00020\nH\u0002J\b\u0010\u0014\u001a\u00020\nH\u0002J\b\u0010\u0015\u001a\u00020\nH&R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0016"}, d2 = {"Lcom/ekoapp/ekosdk/uikit/chat/messages/viewHolder/EkoSelectableMessageViewHolder;", "Lcom/ekoapp/ekosdk/uikit/chat/messages/viewHolder/EkoChatMessageBaseViewHolder;", "itemView", "Landroid/view/View;", "itemViewModel", "Lcom/ekoapp/ekosdk/uikit/chat/messages/viewModel/EkoSelectableMessageViewModel;", "context", "Landroid/content/Context;", "(Landroid/view/View;Lcom/ekoapp/ekosdk/uikit/chat/messages/viewModel/EkoSelectableMessageViewModel;Landroid/content/Context;)V", "addViewModelListener", "", "deleteMessage", "reportMessage", "setMessage", "message", "Lcom/ekoapp/ekosdk/message/EkoMessage;", "setMessageData", "item", "showDeleteDialog", "showDeleteFailedDialog", "showFailedMessageDialog", "showPopUp", "chatkit_debug"})
public abstract class EkoSelectableMessageViewHolder extends com.ekoapp.ekosdk.uikit.chat.messages.viewHolder.EkoChatMessageBaseViewHolder {
    private final com.ekoapp.ekosdk.uikit.chat.messages.viewModel.EkoSelectableMessageViewModel itemViewModel = null;
    private final android.content.Context context = null;
    
    private final void addViewModelListener() {
    }
    
    public abstract void showPopUp();
    
    public abstract void setMessageData(@org.jetbrains.annotations.NotNull()
    com.ekoapp.ekosdk.message.EkoMessage item);
    
    @java.lang.Override()
    public void setMessage(@org.jetbrains.annotations.NotNull()
    com.ekoapp.ekosdk.message.EkoMessage message) {
    }
    
    private final void showDeleteDialog() {
    }
    
    private final void showFailedMessageDialog() {
    }
    
    private final void deleteMessage() {
    }
    
    private final void showDeleteFailedDialog() {
    }
    
    private final void reportMessage() {
    }
    
    public EkoSelectableMessageViewHolder(@org.jetbrains.annotations.NotNull()
    android.view.View itemView, @org.jetbrains.annotations.NotNull()
    com.ekoapp.ekosdk.uikit.chat.messages.viewModel.EkoSelectableMessageViewModel itemViewModel, @org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        super(null, null);
    }
}