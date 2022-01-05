package com.ekoapp.ekosdk.uikit.chat.recent.fragment;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0012\u0010\t\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\f0\u000b0\nR\u001c\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\b\u00a8\u0006\r"}, d2 = {"Lcom/ekoapp/ekosdk/uikit/chat/recent/fragment/EkoRecentChatViewModel;", "Lcom/ekoapp/ekosdk/uikit/base/EkoBaseViewModel;", "()V", "recentChatItemClickListener", "Lcom/ekoapp/ekosdk/uikit/chat/home/callback/IRecentChatItemClickListener;", "getRecentChatItemClickListener", "()Lcom/ekoapp/ekosdk/uikit/chat/home/callback/IRecentChatItemClickListener;", "setRecentChatItemClickListener", "(Lcom/ekoapp/ekosdk/uikit/chat/home/callback/IRecentChatItemClickListener;)V", "getRecentChat", "Lio/reactivex/Flowable;", "Landroidx/paging/PagedList;", "Lcom/ekoapp/ekosdk/channel/EkoChannel;", "chatkit_debug"})
public final class EkoRecentChatViewModel extends com.ekoapp.ekosdk.uikit.base.EkoBaseViewModel {
    @org.jetbrains.annotations.Nullable()
    private com.ekoapp.ekosdk.uikit.chat.home.callback.IRecentChatItemClickListener recentChatItemClickListener;
    
    @org.jetbrains.annotations.Nullable()
    public final com.ekoapp.ekosdk.uikit.chat.home.callback.IRecentChatItemClickListener getRecentChatItemClickListener() {
        return null;
    }
    
    public final void setRecentChatItemClickListener(@org.jetbrains.annotations.Nullable()
    com.ekoapp.ekosdk.uikit.chat.home.callback.IRecentChatItemClickListener p0) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final io.reactivex.Flowable<androidx.paging.PagedList<com.ekoapp.ekosdk.channel.EkoChannel>> getRecentChat() {
        return null;
    }
    
    public EkoRecentChatViewModel() {
        super();
    }
}