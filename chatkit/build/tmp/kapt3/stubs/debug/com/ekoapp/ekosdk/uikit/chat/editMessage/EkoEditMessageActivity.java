package com.ekoapp.ekosdk.uikit.chat.editMessage;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u00004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\u0018\u0000 \u00192\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u00030\u0001:\u0001\u0019B\u0005\u00a2\u0006\u0002\u0010\u0004J\b\u0010\r\u001a\u00020\u000eH\u0016J\b\u0010\u000f\u001a\u00020\u000eH\u0016J\b\u0010\u0010\u001a\u00020\u0011H\u0002J\b\u0010\u0012\u001a\u00020\u0003H\u0016J\u0012\u0010\u0013\u001a\u00020\u00112\b\u0010\u0014\u001a\u0004\u0018\u00010\u0015H\u0014J\b\u0010\u0016\u001a\u00020\u0011H\u0014J\b\u0010\u0017\u001a\u00020\u0011H\u0002J\b\u0010\u0018\u001a\u00020\u0011H\u0002R\u0010\u0010\u0005\u001a\u0004\u0018\u00010\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001b\u0010\u0007\u001a\u00020\u00038BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\n\u0010\u000b\u001a\u0004\b\b\u0010\tR\u0010\u0010\f\u001a\u0004\u0018\u00010\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001a"}, d2 = {"Lcom/ekoapp/ekosdk/uikit/chat/editMessage/EkoEditMessageActivity;", "Lcom/ekoapp/ekosdk/uikit/base/EkoBaseActivity;", "Lcom/ekoapp/ekosdk/uikit/chat/databinding/AmityActivityEditMessageBinding;", "Lcom/ekoapp/ekosdk/uikit/chat/editMessage/EkoEditMessageViewModel;", "()V", "editMessageDisposable", "Lio/reactivex/disposables/Disposable;", "editMessageViewModel", "getEditMessageViewModel", "()Lcom/ekoapp/ekosdk/uikit/chat/editMessage/EkoEditMessageViewModel;", "editMessageViewModel$delegate", "Lkotlin/Lazy;", "messageDisposable", "getBindingVariable", "", "getLayoutId", "getMessage", "", "getViewModel", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "onDestroy", "requestFocus", "setUpToolbar", "Companion", "chatkit_debug"})
public final class EkoEditMessageActivity extends com.ekoapp.ekosdk.uikit.base.EkoBaseActivity<com.ekoapp.ekosdk.uikit.chat.databinding.AmityActivityEditMessageBinding, com.ekoapp.ekosdk.uikit.chat.editMessage.EkoEditMessageViewModel> {
    private final kotlin.Lazy editMessageViewModel$delegate = null;
    private io.reactivex.disposables.Disposable messageDisposable;
    private io.reactivex.disposables.Disposable editMessageDisposable;
    private static final java.lang.String INTENT_MESSAGE_ID = "messageId";
    public static final com.ekoapp.ekosdk.uikit.chat.editMessage.EkoEditMessageActivity.Companion Companion = null;
    private java.util.HashMap _$_findViewCache;
    
    private final com.ekoapp.ekosdk.uikit.chat.editMessage.EkoEditMessageViewModel getEditMessageViewModel() {
        return null;
    }
    
    @java.lang.Override()
    protected void onCreate(@org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    private final void setUpToolbar() {
    }
    
    private final void getMessage() {
    }
    
    @java.lang.Override()
    protected void onDestroy() {
    }
    
    @java.lang.Override()
    public int getLayoutId() {
        return 0;
    }
    
    @org.jetbrains.annotations.NotNull()
    @java.lang.Override()
    public com.ekoapp.ekosdk.uikit.chat.editMessage.EkoEditMessageViewModel getViewModel() {
        return null;
    }
    
    @java.lang.Override()
    public int getBindingVariable() {
        return 0;
    }
    
    private final void requestFocus() {
    }
    
    public EkoEditMessageActivity() {
        super();
    }
    
    @kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0016\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\u0004R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\n"}, d2 = {"Lcom/ekoapp/ekosdk/uikit/chat/editMessage/EkoEditMessageActivity$Companion;", "", "()V", "INTENT_MESSAGE_ID", "", "newIntent", "Landroid/content/Intent;", "context", "Landroid/content/Context;", "messageId", "chatkit_debug"})
    public static final class Companion {
        
        @org.jetbrains.annotations.NotNull()
        public final android.content.Intent newIntent(@org.jetbrains.annotations.NotNull()
        android.content.Context context, @org.jetbrains.annotations.NotNull()
        java.lang.String messageId) {
            return null;
        }
        
        private Companion() {
            super();
        }
    }
}