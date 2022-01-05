package com.ekoapp.ekosdk.uikit.chat.messages.viewModel;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u00006\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\r\n\u0002\u0018\u0002\n\u0000\b\u0016\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010 \u001a\u0004\u0018\u00010!R\u0017\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0007R\u001f\u0010\b\u001a\u0010\u0012\f\u0012\n \n*\u0004\u0018\u00010\t0\t0\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\u0007R\u001c\u0010\f\u001a\u0004\u0018\u00010\rX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u000e\u0010\u000f\"\u0004\b\u0010\u0010\u0011R\u0011\u0010\u0012\u001a\u00020\u0013\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0014R\u0011\u0010\u0015\u001a\u00020\u0013\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u0014R\u0011\u0010\u0016\u001a\u00020\u0013\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0016\u0010\u0014R\u0011\u0010\u0017\u001a\u00020\u0013\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0017\u0010\u0014R\u0011\u0010\u0018\u001a\u00020\u0013\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0018\u0010\u0014R\u0011\u0010\u0019\u001a\u00020\u0013\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0019\u0010\u0014R\u001f\u0010\u001a\u001a\u0010\u0012\f\u0012\n \n*\u0004\u0018\u00010\t0\t0\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001b\u0010\u0007R\u001f\u0010\u001c\u001a\u0010\u0012\f\u0012\n \n*\u0004\u0018\u00010\t0\t0\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001d\u0010\u0007R\u001f\u0010\u001e\u001a\u0010\u0012\f\u0012\n \n*\u0004\u0018\u00010\t0\t0\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001f\u0010\u0007\u00a8\u0006\""}, d2 = {"Lcom/ekoapp/ekosdk/uikit/chat/messages/viewModel/EkoChatMessageBaseViewModel;", "Lcom/ekoapp/ekosdk/uikit/base/EkoBaseViewModel;", "()V", "dateFillColor", "Landroidx/databinding/ObservableField;", "", "getDateFillColor", "()Landroidx/databinding/ObservableField;", "editedAt", "", "kotlin.jvm.PlatformType", "getEditedAt", "ekoMessage", "Lcom/ekoapp/ekosdk/message/EkoMessage;", "getEkoMessage", "()Lcom/ekoapp/ekosdk/message/EkoMessage;", "setEkoMessage", "(Lcom/ekoapp/ekosdk/message/EkoMessage;)V", "isDateVisible", "Landroidx/databinding/ObservableBoolean;", "()Landroidx/databinding/ObservableBoolean;", "isDeleted", "isEdited", "isFailed", "isSelf", "isSenderVisible", "msgDate", "getMsgDate", "msgTime", "getMsgTime", "sender", "getSender", "deleteMessage", "Lio/reactivex/Completable;", "chatkit_debug"})
public class EkoChatMessageBaseViewModel extends com.ekoapp.ekosdk.uikit.base.EkoBaseViewModel {
    @org.jetbrains.annotations.NotNull()
    private final androidx.databinding.ObservableBoolean isSelf = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.databinding.ObservableField<java.lang.String> sender = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.databinding.ObservableField<java.lang.String> msgTime = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.databinding.ObservableField<java.lang.String> msgDate = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.databinding.ObservableBoolean isDateVisible = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.databinding.ObservableBoolean isSenderVisible = null;
    @org.jetbrains.annotations.Nullable()
    private com.ekoapp.ekosdk.message.EkoMessage ekoMessage;
    @org.jetbrains.annotations.NotNull()
    private final androidx.databinding.ObservableBoolean isDeleted = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.databinding.ObservableField<java.lang.String> editedAt = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.databinding.ObservableBoolean isEdited = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.databinding.ObservableField<java.lang.Integer> dateFillColor = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.databinding.ObservableBoolean isFailed = null;
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.databinding.ObservableBoolean isSelf() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.databinding.ObservableField<java.lang.String> getSender() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.databinding.ObservableField<java.lang.String> getMsgTime() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.databinding.ObservableField<java.lang.String> getMsgDate() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.databinding.ObservableBoolean isDateVisible() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.databinding.ObservableBoolean isSenderVisible() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.ekoapp.ekosdk.message.EkoMessage getEkoMessage() {
        return null;
    }
    
    public final void setEkoMessage(@org.jetbrains.annotations.Nullable()
    com.ekoapp.ekosdk.message.EkoMessage p0) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.databinding.ObservableBoolean isDeleted() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.databinding.ObservableField<java.lang.String> getEditedAt() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.databinding.ObservableBoolean isEdited() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.databinding.ObservableField<java.lang.Integer> getDateFillColor() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.databinding.ObservableBoolean isFailed() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final io.reactivex.Completable deleteMessage() {
        return null;
    }
    
    public EkoChatMessageBaseViewModel() {
        super();
    }
}