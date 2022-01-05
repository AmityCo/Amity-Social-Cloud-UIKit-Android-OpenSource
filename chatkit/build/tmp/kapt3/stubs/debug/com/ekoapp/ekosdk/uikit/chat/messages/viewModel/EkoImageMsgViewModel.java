package com.ekoapp.ekosdk.uikit.chat.messages.viewModel;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u00004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u0012R\u0017\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0007R\u0017\u0010\b\u001a\b\u0012\u0004\u0012\u00020\t0\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u0007R\u0011\u0010\u000b\u001a\u00020\f\u00a2\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000e\u00a8\u0006\u0013"}, d2 = {"Lcom/ekoapp/ekosdk/uikit/chat/messages/viewModel/EkoImageMsgViewModel;", "Lcom/ekoapp/ekosdk/uikit/chat/messages/viewModel/EkoSelectableMessageViewModel;", "()V", "imageUrl", "Landroidx/databinding/ObservableField;", "", "getImageUrl", "()Landroidx/databinding/ObservableField;", "uploadProgress", "", "getUploadProgress", "uploading", "Landroidx/databinding/ObservableBoolean;", "getUploading", "()Landroidx/databinding/ObservableBoolean;", "getImageUploadProgress", "", "ekoMessage", "Lcom/ekoapp/ekosdk/message/EkoMessage;", "chatkit_debug"})
public final class EkoImageMsgViewModel extends com.ekoapp.ekosdk.uikit.chat.messages.viewModel.EkoSelectableMessageViewModel {
    @org.jetbrains.annotations.NotNull()
    private final androidx.databinding.ObservableField<java.lang.String> imageUrl = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.databinding.ObservableBoolean uploading = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.databinding.ObservableField<java.lang.Integer> uploadProgress = null;
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.databinding.ObservableField<java.lang.String> getImageUrl() {
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
    
    public final void getImageUploadProgress(@org.jetbrains.annotations.NotNull()
    com.ekoapp.ekosdk.message.EkoMessage ekoMessage) {
    }
    
    public EkoImageMsgViewModel() {
        super();
    }
}