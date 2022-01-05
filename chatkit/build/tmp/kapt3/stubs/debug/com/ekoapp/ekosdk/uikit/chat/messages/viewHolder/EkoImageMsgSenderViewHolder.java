package com.ekoapp.ekosdk.uikit.chat.messages.viewHolder;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000B\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\u001d\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\u0002\u0010\bJ\b\u0010\r\u001a\u00020\u000eH\u0002J\u0010\u0010\u000f\u001a\u00020\u000e2\u0006\u0010\u0010\u001a\u00020\u0011H\u0002J\u0010\u0010\u0012\u001a\u00020\u000e2\u0006\u0010\u0013\u001a\u00020\u0014H\u0016J\b\u0010\u0015\u001a\u00020\u000eH\u0016R\u0010\u0010\t\u001a\u0004\u0018\u00010\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u000b\u001a\u0004\u0018\u00010\fX\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0016"}, d2 = {"Lcom/ekoapp/ekosdk/uikit/chat/messages/viewHolder/EkoImageMsgSenderViewHolder;", "Lcom/ekoapp/ekosdk/uikit/chat/messages/viewHolder/EkoSelectableMessageViewHolder;", "itemView", "Landroid/view/View;", "itemViewModel", "Lcom/ekoapp/ekosdk/uikit/chat/messages/viewModel/EkoImageMsgViewModel;", "context", "Landroid/content/Context;", "(Landroid/view/View;Lcom/ekoapp/ekosdk/uikit/chat/messages/viewModel/EkoImageMsgViewModel;Landroid/content/Context;)V", "binding", "Lcom/ekoapp/ekosdk/uikit/chat/databinding/AmityItemImageMsgSenderBinding;", "popUp", "Lcom/ekoapp/ekosdk/uikit/chat/messages/popUp/EkoPopUp;", "addViewModelListeners", "", "navigateToImagePreview", "imageUrl", "", "setMessageData", "item", "Lcom/ekoapp/ekosdk/message/EkoMessage;", "showPopUp", "chatkit_debug"})
public final class EkoImageMsgSenderViewHolder extends com.ekoapp.ekosdk.uikit.chat.messages.viewHolder.EkoSelectableMessageViewHolder {
    private final com.ekoapp.ekosdk.uikit.chat.databinding.AmityItemImageMsgSenderBinding binding = null;
    private com.ekoapp.ekosdk.uikit.chat.messages.popUp.EkoPopUp popUp;
    private final com.ekoapp.ekosdk.uikit.chat.messages.viewModel.EkoImageMsgViewModel itemViewModel = null;
    private final android.content.Context context = null;
    
    private final void addViewModelListeners() {
    }
    
    @java.lang.Override()
    public void setMessageData(@org.jetbrains.annotations.NotNull()
    com.ekoapp.ekosdk.message.EkoMessage item) {
    }
    
    private final void navigateToImagePreview(java.lang.String imageUrl) {
    }
    
    @java.lang.Override()
    public void showPopUp() {
    }
    
    public EkoImageMsgSenderViewHolder(@org.jetbrains.annotations.NotNull()
    android.view.View itemView, @org.jetbrains.annotations.NotNull()
    com.ekoapp.ekosdk.uikit.chat.messages.viewModel.EkoImageMsgViewModel itemViewModel, @org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        super(null, null, null);
    }
}