package com.ekoapp.ekosdk.uikit.chat.messages.viewHolder;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000@\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u00002\u00020\u0001B%\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u00a2\u0006\u0002\u0010\nJ\b\u0010\u000f\u001a\u00020\u0010H\u0002J\b\u0010\u0011\u001a\u00020\u0001H\u0016J\u0010\u0010\u0012\u001a\u00020\u00102\u0006\u0010\u0013\u001a\u00020\u0014H\u0016J\b\u0010\u0015\u001a\u00020\u0010H\u0016R\u0010\u0010\u000b\u001a\u0004\u0018\u00010\fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\r\u001a\u0004\u0018\u00010\u000eX\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0016"}, d2 = {"Lcom/ekoapp/ekosdk/uikit/chat/messages/viewHolder/EkoAudioMsgSenderViewHolder;", "Lcom/ekoapp/ekosdk/uikit/chat/messages/viewHolder/AudioMsgBaseViewHolder;", "itemView", "Landroid/view/View;", "itemViewModel", "Lcom/ekoapp/ekosdk/uikit/chat/messages/viewModel/EkoAudioMsgViewModel;", "context", "Landroid/content/Context;", "audioPlayListener", "Lcom/ekoapp/ekosdk/uikit/chat/messages/viewHolder/IAudioPlayCallback;", "(Landroid/view/View;Lcom/ekoapp/ekosdk/uikit/chat/messages/viewModel/EkoAudioMsgViewModel;Landroid/content/Context;Lcom/ekoapp/ekosdk/uikit/chat/messages/viewHolder/IAudioPlayCallback;)V", "binding", "Lcom/ekoapp/ekosdk/uikit/chat/databinding/AmityItemAudioMessageSenderBinding;", "popUp", "Lcom/ekoapp/ekosdk/uikit/chat/messages/popUp/EkoPopUp;", "addViewModelListeners", "", "getAudioViewHolder", "setMessageData", "item", "Lcom/ekoapp/ekosdk/message/EkoMessage;", "showPopUp", "chatkit_debug"})
public final class EkoAudioMsgSenderViewHolder extends com.ekoapp.ekosdk.uikit.chat.messages.viewHolder.AudioMsgBaseViewHolder {
    private final com.ekoapp.ekosdk.uikit.chat.databinding.AmityItemAudioMessageSenderBinding binding = null;
    private com.ekoapp.ekosdk.uikit.chat.messages.popUp.EkoPopUp popUp;
    private final com.ekoapp.ekosdk.uikit.chat.messages.viewModel.EkoAudioMsgViewModel itemViewModel = null;
    
    @org.jetbrains.annotations.NotNull()
    @java.lang.Override()
    public com.ekoapp.ekosdk.uikit.chat.messages.viewHolder.AudioMsgBaseViewHolder getAudioViewHolder() {
        return null;
    }
    
    private final void addViewModelListeners() {
    }
    
    @java.lang.Override()
    public void setMessageData(@org.jetbrains.annotations.NotNull()
    com.ekoapp.ekosdk.message.EkoMessage item) {
    }
    
    @java.lang.Override()
    public void showPopUp() {
    }
    
    public EkoAudioMsgSenderViewHolder(@org.jetbrains.annotations.NotNull()
    android.view.View itemView, @org.jetbrains.annotations.NotNull()
    com.ekoapp.ekosdk.uikit.chat.messages.viewModel.EkoAudioMsgViewModel itemViewModel, @org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.NotNull()
    com.ekoapp.ekosdk.uikit.chat.messages.viewHolder.IAudioPlayCallback audioPlayListener) {
        super(null, null, null, null);
    }
}