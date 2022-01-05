package com.ekoapp.ekosdk.uikit.community.explore.adapter;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u00006\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\b\u0016\u0018\u00002\u00020\u00012\b\u0012\u0004\u0012\u00020\u00030\u0002B\u0017\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\b\u0010\u0006\u001a\u0004\u0018\u00010\u0007\u00a2\u0006\u0002\u0010\bJ\u001a\u0010\r\u001a\u00020\u000e2\b\u0010\u000f\u001a\u0004\u0018\u00010\u00032\u0006\u0010\u0010\u001a\u00020\u0011H\u0016R\u000e\u0010\t\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0013\u0010\u0006\u001a\u0004\u0018\u00010\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\f\u00a8\u0006\u0012"}, d2 = {"Lcom/ekoapp/ekosdk/uikit/community/explore/adapter/EkoCategoryItemViewHolder;", "Landroidx/recyclerview/widget/RecyclerView$ViewHolder;", "Lcom/ekoapp/ekosdk/uikit/base/EkoBaseRecyclerViewPagedAdapter$Binder;", "Lcom/ekoapp/ekosdk/community/category/EkoCommunityCategory;", "itemView", "Landroid/view/View;", "itemClickListener", "Lcom/ekoapp/ekosdk/uikit/community/explore/listener/IEkoCategoryItemClickListener;", "(Landroid/view/View;Lcom/ekoapp/ekosdk/uikit/community/explore/listener/IEkoCategoryItemClickListener;)V", "categoryView", "Lcom/ekoapp/ekosdk/uikit/community/views/communitycategory/EkoCommunityCategoryView;", "getItemClickListener", "()Lcom/ekoapp/ekosdk/uikit/community/explore/listener/IEkoCategoryItemClickListener;", "bind", "", "data", "position", "", "community_debug"})
public class EkoCategoryItemViewHolder extends androidx.recyclerview.widget.RecyclerView.ViewHolder implements com.ekoapp.ekosdk.uikit.base.EkoBaseRecyclerViewPagedAdapter.Binder<com.ekoapp.ekosdk.community.category.EkoCommunityCategory> {
    private final com.ekoapp.ekosdk.uikit.community.views.communitycategory.EkoCommunityCategoryView categoryView = null;
    @org.jetbrains.annotations.Nullable()
    private final com.ekoapp.ekosdk.uikit.community.explore.listener.IEkoCategoryItemClickListener itemClickListener = null;
    
    @java.lang.Override()
    public void bind(@org.jetbrains.annotations.Nullable()
    com.ekoapp.ekosdk.community.category.EkoCommunityCategory data, int position) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.ekoapp.ekosdk.uikit.community.explore.listener.IEkoCategoryItemClickListener getItemClickListener() {
        return null;
    }
    
    public EkoCategoryItemViewHolder(@org.jetbrains.annotations.NotNull()
    android.view.View itemView, @org.jetbrains.annotations.Nullable()
    com.ekoapp.ekosdk.uikit.community.explore.listener.IEkoCategoryItemClickListener itemClickListener) {
        super(null);
    }
}