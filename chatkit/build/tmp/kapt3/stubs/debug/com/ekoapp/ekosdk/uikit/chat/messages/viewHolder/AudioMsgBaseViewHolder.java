package com.ekoapp.ekosdk.uikit.chat.messages.viewHolder;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0002\b\u0004\b&\u0018\u00002\u00020\u0001B%\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u00a2\u0006\u0002\u0010\nJ\b\u0010\r\u001a\u00020\u000eH\u0002J\b\u0010\u000f\u001a\u00020\u000eH\u0002J\b\u0010\u0010\u001a\u00020\u0000H&J\b\u0010\u0011\u001a\u00020\u000eH\u0002R\u0011\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\fR\u000e\u0010\b\u001a\u00020\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0012"}, d2 = {"Lcom/ekoapp/ekosdk/uikit/chat/messages/viewHolder/AudioMsgBaseViewHolder;", "Lcom/ekoapp/ekosdk/uikit/chat/messages/viewHolder/EkoSelectableMessageViewHolder;", "itemView", "Landroid/view/View;", "audioMsgBaseViewModel", "Lcom/ekoapp/ekosdk/uikit/chat/messages/viewModel/EkoAudioMsgViewModel;", "context", "Landroid/content/Context;", "audioPlayListener", "Lcom/ekoapp/ekosdk/uikit/chat/messages/viewHolder/IAudioPlayCallback;", "(Landroid/view/View;Lcom/ekoapp/ekosdk/uikit/chat/messages/viewModel/EkoAudioMsgViewModel;Landroid/content/Context;Lcom/ekoapp/ekosdk/uikit/chat/messages/viewHolder/IAudioPlayCallback;)V", "getAudioMsgBaseViewModel", "()Lcom/ekoapp/ekosdk/uikit/chat/messages/viewModel/EkoAudioMsgViewModel;", "addViewModelListener", "", "downloadAudioFile", "getAudioViewHolder", "setAudioFileProperties", "chatkit_debug"})
public abstract class AudioMsgBaseViewHolder extends com.ekoapp.ekosdk.uikit.chat.messages.viewHolder.EkoSelectableMessageViewHolder {
    @org.jetbrains.annotations.NotNull()
    private final com.ekoapp.ekosdk.uikit.chat.messages.viewModel.EkoAudioMsgViewModel audioMsgBaseViewModel = null;
    private final android.content.Context context = null;
    private final com.ekoapp.ekosdk.uikit.chat.messages.viewHolder.IAudioPlayCallback audioPlayListener = null;
    
    @org.jetbrains.annotations.NotNull()
    public abstract com.ekoapp.ekosdk.uikit.chat.messages.viewHolder.AudioMsgBaseViewHolder getAudioViewHolder();
    
    private final void addViewModelListener() {
    }
    
    /**
     * Not using now will be used when we'll start downloading Audio Files
     * @author sumitlakra
     * @date 12/01/2020
     */
    private final void setAudioFileProperties() {
    }
    
    /**
     * Not using now will be used when we'll start downloading Audio Files
     * @author sumitlakra
     * @date 12/01/2020
     */
    private final void downloadAudioFile() {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.ekoapp.ekosdk.uikit.chat.messages.viewModel.EkoAudioMsgViewModel getAudioMsgBaseViewModel() {
        return null;
    }
    
    public AudioMsgBaseViewHolder(@org.jetbrains.annotations.NotNull()
    android.view.View itemView, @org.jetbrains.annotations.NotNull()
    com.ekoapp.ekosdk.uikit.chat.messages.viewModel.EkoAudioMsgViewModel audioMsgBaseViewModel, @org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.NotNull()
    com.ekoapp.ekosdk.uikit.chat.messages.viewHolder.IAudioPlayCallback audioPlayListener) {
        super(null, null, null);
    }
}