package com.ekoapp.ekosdk.uikit.chat.messages.viewHolder;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\b&\u0018\u00002\u00020\u0001B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006J\u0010\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\fH\u0002J\u0010\u0010\r\u001a\u00020\u000e2\b\u0010\u000b\u001a\u0004\u0018\u00010\fJ\u0010\u0010\u000f\u001a\u00020\u000e2\u0006\u0010\u0010\u001a\u00020\fH&R\u0011\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\b\u00a8\u0006\u0011"}, d2 = {"Lcom/ekoapp/ekosdk/uikit/chat/messages/viewHolder/EkoChatMessageBaseViewHolder;", "Landroidx/recyclerview/widget/RecyclerView$ViewHolder;", "itemView", "Landroid/view/View;", "itemBaseViewModel", "Lcom/ekoapp/ekosdk/uikit/chat/messages/viewModel/EkoChatMessageBaseViewModel;", "(Landroid/view/View;Lcom/ekoapp/ekosdk/uikit/chat/messages/viewModel/EkoChatMessageBaseViewModel;)V", "getItemBaseViewModel", "()Lcom/ekoapp/ekosdk/uikit/chat/messages/viewModel/EkoChatMessageBaseViewModel;", "getSenderName", "", "item", "Lcom/ekoapp/ekosdk/message/EkoMessage;", "setItem", "", "setMessage", "message", "chatkit_debug"})
public abstract class EkoChatMessageBaseViewHolder extends androidx.recyclerview.widget.RecyclerView.ViewHolder {
    @org.jetbrains.annotations.NotNull()
    private final com.ekoapp.ekosdk.uikit.chat.messages.viewModel.EkoChatMessageBaseViewModel itemBaseViewModel = null;
    
    public abstract void setMessage(@org.jetbrains.annotations.NotNull()
    com.ekoapp.ekosdk.message.EkoMessage message);
    
    public final void setItem(@org.jetbrains.annotations.Nullable()
    com.ekoapp.ekosdk.message.EkoMessage item) {
    }
    
    private final java.lang.String getSenderName(com.ekoapp.ekosdk.message.EkoMessage item) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.ekoapp.ekosdk.uikit.chat.messages.viewModel.EkoChatMessageBaseViewModel getItemBaseViewModel() {
        return null;
    }
    
    public EkoChatMessageBaseViewHolder(@org.jetbrains.annotations.NotNull()
    android.view.View itemView, @org.jetbrains.annotations.NotNull()
    com.ekoapp.ekosdk.uikit.chat.messages.viewModel.EkoChatMessageBaseViewModel itemBaseViewModel) {
        super(null);
    }
}