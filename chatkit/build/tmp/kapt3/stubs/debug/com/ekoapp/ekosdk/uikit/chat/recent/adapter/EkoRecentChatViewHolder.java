package com.ekoapp.ekosdk.uikit.chat.recent.adapter;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000D\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0004\u0018\u00002\u00020\u00012\b\u0012\u0004\u0012\u00020\u00030\u0002B\u0017\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\b\u0010\u0006\u001a\u0004\u0018\u00010\u0007\u00a2\u0006\u0002\u0010\bJ\u001a\u0010\u0011\u001a\u00020\u00122\b\u0010\u0013\u001a\u0004\u0018\u00010\u00032\u0006\u0010\u0014\u001a\u00020\u0015H\u0016J\u0010\u0010\u0016\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u0003H\u0002J\u0010\u0010\u0017\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u0003H\u0002J\u0010\u0010\u0018\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u0003H\u0002R\u000e\u0010\t\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u000b\u001a\u0004\u0018\u00010\fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0006\u001a\u0004\u0018\u00010\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u000eX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u000eX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u000eX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0019"}, d2 = {"Lcom/ekoapp/ekosdk/uikit/chat/recent/adapter/EkoRecentChatViewHolder;", "Landroidx/recyclerview/widget/RecyclerView$ViewHolder;", "Lcom/ekoapp/ekosdk/uikit/base/EkoBaseRecyclerViewPagedAdapter$Binder;", "Lcom/ekoapp/ekosdk/channel/EkoChannel;", "itemView", "Landroid/view/View;", "listener", "Lcom/ekoapp/ekosdk/uikit/chat/home/callback/IRecentChatItemClickListener;", "(Landroid/view/View;Lcom/ekoapp/ekosdk/uikit/chat/home/callback/IRecentChatItemClickListener;)V", "avatar", "Lcom/google/android/material/imageview/ShapeableImageView;", "binding", "Lcom/ekoapp/ekosdk/uikit/chat/databinding/AmityItemRecentMessageBinding;", "memberCount", "Landroid/widget/TextView;", "name", "unreadCount", "bind", "", "data", "position", "", "setUpAvatarView", "setupNameView", "setupUnreadCount", "chatkit_debug"})
public final class EkoRecentChatViewHolder extends androidx.recyclerview.widget.RecyclerView.ViewHolder implements com.ekoapp.ekosdk.uikit.base.EkoBaseRecyclerViewPagedAdapter.Binder<com.ekoapp.ekosdk.channel.EkoChannel> {
    private final com.ekoapp.ekosdk.uikit.chat.databinding.AmityItemRecentMessageBinding binding = null;
    private final android.widget.TextView memberCount = null;
    private final android.widget.TextView name = null;
    private final com.google.android.material.imageview.ShapeableImageView avatar = null;
    private final android.widget.TextView unreadCount = null;
    private final com.ekoapp.ekosdk.uikit.chat.home.callback.IRecentChatItemClickListener listener = null;
    
    @java.lang.Override()
    public void bind(@org.jetbrains.annotations.Nullable()
    com.ekoapp.ekosdk.channel.EkoChannel data, int position) {
    }
    
    private final void setUpAvatarView(com.ekoapp.ekosdk.channel.EkoChannel data) {
    }
    
    private final void setupNameView(com.ekoapp.ekosdk.channel.EkoChannel data) {
    }
    
    private final void setupUnreadCount(com.ekoapp.ekosdk.channel.EkoChannel data) {
    }
    
    public EkoRecentChatViewHolder(@org.jetbrains.annotations.NotNull()
    android.view.View itemView, @org.jetbrains.annotations.Nullable()
    com.ekoapp.ekosdk.uikit.chat.home.callback.IRecentChatItemClickListener listener) {
        super(null);
    }
}