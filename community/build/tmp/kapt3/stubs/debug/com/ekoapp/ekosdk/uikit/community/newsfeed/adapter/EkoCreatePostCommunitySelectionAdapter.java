package com.ekoapp.ekosdk.uikit.community.newsfeed.adapter;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\u0018\u0000 \u000f2\b\u0012\u0004\u0012\u00020\u00020\u0001:\u0002\u000f\u0010B\r\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u00a2\u0006\u0002\u0010\u0005J\u001a\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\u00072\b\u0010\t\u001a\u0004\u0018\u00010\u0002H\u0014J\u0018\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u0007H\u0016R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0011"}, d2 = {"Lcom/ekoapp/ekosdk/uikit/community/newsfeed/adapter/EkoCreatePostCommunitySelectionAdapter;", "Lcom/ekoapp/ekosdk/uikit/base/EkoBaseRecyclerViewPagedAdapter;", "Lcom/ekoapp/ekosdk/community/EkoCommunity;", "listener", "Lcom/ekoapp/ekosdk/uikit/community/newsfeed/listener/ICreatePostCommunitySelectionListener;", "(Lcom/ekoapp/ekosdk/uikit/community/newsfeed/listener/ICreatePostCommunitySelectionListener;)V", "getLayoutId", "", "position", "obj", "getViewHolder", "Landroidx/recyclerview/widget/RecyclerView$ViewHolder;", "view", "Landroid/view/View;", "viewType", "Companion", "EkoCommunityViewHolder", "community_debug"})
public final class EkoCreatePostCommunitySelectionAdapter extends com.ekoapp.ekosdk.uikit.base.EkoBaseRecyclerViewPagedAdapter<com.ekoapp.ekosdk.community.EkoCommunity> {
    private final com.ekoapp.ekosdk.uikit.community.newsfeed.listener.ICreatePostCommunitySelectionListener listener = null;
    private static final androidx.recyclerview.widget.DiffUtil.ItemCallback<com.ekoapp.ekosdk.community.EkoCommunity> diffCallBack = null;
    public static final com.ekoapp.ekosdk.uikit.community.newsfeed.adapter.EkoCreatePostCommunitySelectionAdapter.Companion Companion = null;
    
    @java.lang.Override()
    protected int getLayoutId(int position, @org.jetbrains.annotations.Nullable()
    com.ekoapp.ekosdk.community.EkoCommunity obj) {
        return 0;
    }
    
    @org.jetbrains.annotations.NotNull()
    @java.lang.Override()
    public androidx.recyclerview.widget.RecyclerView.ViewHolder getViewHolder(@org.jetbrains.annotations.NotNull()
    android.view.View view, int viewType) {
        return null;
    }
    
    public EkoCreatePostCommunitySelectionAdapter(@org.jetbrains.annotations.NotNull()
    com.ekoapp.ekosdk.uikit.community.newsfeed.listener.ICreatePostCommunitySelectionListener listener) {
        super(null);
    }
    
    @kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u00006\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0003\u0018\u00002\u00020\u00012\b\u0012\u0004\u0012\u00020\u00030\u0002B\u0017\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\b\u0010\u0006\u001a\u0004\u0018\u00010\u0007\u00a2\u0006\u0002\u0010\bJ\u001a\u0010\u000b\u001a\u00020\f2\b\u0010\r\u001a\u0004\u0018\u00010\u00032\u0006\u0010\u000e\u001a\u00020\u000fH\u0016J\u0010\u0010\u0010\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u0003H\u0002J\u0010\u0010\u0011\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u0003H\u0002R\u0010\u0010\t\u001a\u0004\u0018\u00010\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0006\u001a\u0004\u0018\u00010\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0012"}, d2 = {"Lcom/ekoapp/ekosdk/uikit/community/newsfeed/adapter/EkoCreatePostCommunitySelectionAdapter$EkoCommunityViewHolder;", "Landroidx/recyclerview/widget/RecyclerView$ViewHolder;", "Lcom/ekoapp/ekosdk/uikit/base/EkoBaseRecyclerViewPagedAdapter$Binder;", "Lcom/ekoapp/ekosdk/community/EkoCommunity;", "itemView", "Landroid/view/View;", "listener", "Lcom/ekoapp/ekosdk/uikit/community/newsfeed/listener/ICreatePostCommunitySelectionListener;", "(Landroid/view/View;Lcom/ekoapp/ekosdk/uikit/community/newsfeed/listener/ICreatePostCommunitySelectionListener;)V", "binding", "Lcom/ekoapp/ekosdk/uikit/community/databinding/AmityItemCommunitySelectionListBinding;", "bind", "", "data", "position", "", "setupCommunityImageView", "setupCommunityNameView", "community_debug"})
    public static final class EkoCommunityViewHolder extends androidx.recyclerview.widget.RecyclerView.ViewHolder implements com.ekoapp.ekosdk.uikit.base.EkoBaseRecyclerViewPagedAdapter.Binder<com.ekoapp.ekosdk.community.EkoCommunity> {
        private final com.ekoapp.ekosdk.uikit.community.databinding.AmityItemCommunitySelectionListBinding binding = null;
        private final com.ekoapp.ekosdk.uikit.community.newsfeed.listener.ICreatePostCommunitySelectionListener listener = null;
        
        @java.lang.Override()
        public void bind(@org.jetbrains.annotations.Nullable()
        com.ekoapp.ekosdk.community.EkoCommunity data, int position) {
        }
        
        private final void setupCommunityNameView(com.ekoapp.ekosdk.community.EkoCommunity data) {
        }
        
        private final void setupCommunityImageView(com.ekoapp.ekosdk.community.EkoCommunity data) {
        }
        
        public EkoCommunityViewHolder(@org.jetbrains.annotations.NotNull()
        android.view.View itemView, @org.jetbrains.annotations.Nullable()
        com.ekoapp.ekosdk.uikit.community.newsfeed.listener.ICreatePostCommunitySelectionListener listener) {
            super(null);
        }
    }
    
    @kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\u0016\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u0014\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0006"}, d2 = {"Lcom/ekoapp/ekosdk/uikit/community/newsfeed/adapter/EkoCreatePostCommunitySelectionAdapter$Companion;", "", "()V", "diffCallBack", "Landroidx/recyclerview/widget/DiffUtil$ItemCallback;", "Lcom/ekoapp/ekosdk/community/EkoCommunity;", "community_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}