package com.ekoapp.ekosdk.uikit.community.mycommunity.adapter;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B\r\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u00a2\u0006\u0002\u0010\u0005J\u001a\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\u00072\b\u0010\t\u001a\u0004\u0018\u00010\u0002H\u0014J\u0018\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u0007H\u0016R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u000f"}, d2 = {"Lcom/ekoapp/ekosdk/uikit/community/mycommunity/adapter/EkoMyCommunitiesAdapter;", "Lcom/ekoapp/ekosdk/uikit/base/EkoBaseRecyclerViewPagedAdapter;", "Lcom/ekoapp/ekosdk/community/EkoCommunity;", "listener", "Lcom/ekoapp/ekosdk/uikit/community/mycommunity/listener/IMyCommunityItemClickListener;", "(Lcom/ekoapp/ekosdk/uikit/community/mycommunity/listener/IMyCommunityItemClickListener;)V", "getLayoutId", "", "position", "obj", "getViewHolder", "Landroidx/recyclerview/widget/RecyclerView$ViewHolder;", "view", "Landroid/view/View;", "viewType", "community_debug"})
public final class EkoMyCommunitiesAdapter extends com.ekoapp.ekosdk.uikit.base.EkoBaseRecyclerViewPagedAdapter<com.ekoapp.ekosdk.community.EkoCommunity> {
    private final com.ekoapp.ekosdk.uikit.community.mycommunity.listener.IMyCommunityItemClickListener listener = null;
    
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
    
    public EkoMyCommunitiesAdapter(@org.jetbrains.annotations.NotNull()
    com.ekoapp.ekosdk.uikit.community.mycommunity.listener.IMyCommunityItemClickListener listener) {
        super(null);
    }
}