package com.ekoapp.ekosdk.uikit.community.ui.adapter;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u00008\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010 \n\u0000\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B\r\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u00a2\u0006\u0002\u0010\u0005J\b\u0010\u0006\u001a\u00020\u0007H\u0016J\u001a\u0010\b\u001a\u00020\u00072\u0006\u0010\t\u001a\u00020\u00072\b\u0010\n\u001a\u0004\u0018\u00010\u0002H\u0014J\u0018\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u0007H\u0016J\u0014\u0010\u0010\u001a\u00020\u00112\f\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\u00020\u0013R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0014"}, d2 = {"Lcom/ekoapp/ekosdk/uikit/community/ui/adapter/EkoAddedMembersAdapter;", "Lcom/ekoapp/ekosdk/uikit/base/EkoBaseRecyclerViewAdapter;", "Lcom/ekoapp/ekosdk/uikit/community/data/SelectMemberItem;", "listener", "Lcom/ekoapp/ekosdk/uikit/community/ui/clickListener/EkoAddedMemberClickListener;", "(Lcom/ekoapp/ekosdk/uikit/community/ui/clickListener/EkoAddedMemberClickListener;)V", "getItemCount", "", "getLayoutId", "position", "obj", "getViewHolder", "Landroidx/recyclerview/widget/RecyclerView$ViewHolder;", "view", "Landroid/view/View;", "viewType", "submitList", "", "newList", "", "community_debug"})
public final class EkoAddedMembersAdapter extends com.ekoapp.ekosdk.uikit.base.EkoBaseRecyclerViewAdapter<com.ekoapp.ekosdk.uikit.community.data.SelectMemberItem> {
    private final com.ekoapp.ekosdk.uikit.community.ui.clickListener.EkoAddedMemberClickListener listener = null;
    
    @java.lang.Override()
    protected int getLayoutId(int position, @org.jetbrains.annotations.Nullable()
    com.ekoapp.ekosdk.uikit.community.data.SelectMemberItem obj) {
        return 0;
    }
    
    @org.jetbrains.annotations.NotNull()
    @java.lang.Override()
    public androidx.recyclerview.widget.RecyclerView.ViewHolder getViewHolder(@org.jetbrains.annotations.NotNull()
    android.view.View view, int viewType) {
        return null;
    }
    
    @java.lang.Override()
    public int getItemCount() {
        return 0;
    }
    
    public final void submitList(@org.jetbrains.annotations.NotNull()
    java.util.List<com.ekoapp.ekosdk.uikit.community.data.SelectMemberItem> newList) {
    }
    
    public EkoAddedMembersAdapter(@org.jetbrains.annotations.NotNull()
    com.ekoapp.ekosdk.uikit.community.ui.clickListener.EkoAddedMemberClickListener listener) {
        super();
    }
}