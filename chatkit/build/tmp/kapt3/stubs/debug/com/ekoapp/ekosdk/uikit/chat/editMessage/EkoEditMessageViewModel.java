package com.ekoapp.ekosdk.uikit.chat.editMessage;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000F\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0014\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u00170\u00162\u0006\u0010\u0018\u001a\u00020\bJ\u0006\u0010\u0019\u001a\u00020\u001aJ\u0006\u0010\u001b\u001a\u00020\u001cR\u0011\u0010\u0003\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0003\u0010\u0005R\u0017\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\b0\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\t\u0010\nR\u001a\u0010\u000b\u001a\u00020\fX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\r\u0010\u000e\"\u0004\b\u000f\u0010\u0010R\u0017\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\f0\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\nR\u0017\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\u00140\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\n\u00a8\u0006\u001d"}, d2 = {"Lcom/ekoapp/ekosdk/uikit/chat/editMessage/EkoEditMessageViewModel;", "Lcom/ekoapp/ekosdk/uikit/base/EkoBaseViewModel;", "()V", "isSaveEnabled", "Landroidx/databinding/ObservableBoolean;", "()Landroidx/databinding/ObservableBoolean;", "message", "Landroidx/databinding/ObservableField;", "", "getMessage", "()Landroidx/databinding/ObservableField;", "messageLength", "", "getMessageLength", "()I", "setMessageLength", "(I)V", "saveColor", "getSaveColor", "textData", "Lcom/ekoapp/ekosdk/message/EkoMessage$Data$TEXT;", "getTextData", "Lio/reactivex/Flowable;", "Lcom/ekoapp/ekosdk/message/EkoMessage;", "messageId", "observeMessageChange", "", "saveMessage", "Lio/reactivex/Completable;", "chatkit_debug"})
public final class EkoEditMessageViewModel extends com.ekoapp.ekosdk.uikit.base.EkoBaseViewModel {
    @org.jetbrains.annotations.NotNull()
    private final androidx.databinding.ObservableField<java.lang.String> message = null;
    private int messageLength = 0;
    @org.jetbrains.annotations.NotNull()
    private final androidx.databinding.ObservableBoolean isSaveEnabled = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.databinding.ObservableField<com.ekoapp.ekosdk.message.EkoMessage.Data.TEXT> textData = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.databinding.ObservableField<java.lang.Integer> saveColor = null;
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.databinding.ObservableField<java.lang.String> getMessage() {
        return null;
    }
    
    public final int getMessageLength() {
        return 0;
    }
    
    public final void setMessageLength(int p0) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.databinding.ObservableBoolean isSaveEnabled() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.databinding.ObservableField<com.ekoapp.ekosdk.message.EkoMessage.Data.TEXT> getTextData() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.databinding.ObservableField<java.lang.Integer> getSaveColor() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final io.reactivex.Flowable<com.ekoapp.ekosdk.message.EkoMessage> getMessage(@org.jetbrains.annotations.NotNull()
    java.lang.String messageId) {
        return null;
    }
    
    public final void observeMessageChange() {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final io.reactivex.Completable saveMessage() {
        return null;
    }
    
    public EkoEditMessageViewModel() {
        super();
    }
}