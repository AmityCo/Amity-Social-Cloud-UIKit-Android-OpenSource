package com.ekoapp.ekosdk.uikit.community.explore.viewmodel;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u001a\u0010\u000b\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u000e0\r0\f2\u0006\u0010\u000f\u001a\u00020\u0010R\u001c\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\bR\u000e\u0010\t\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0011"}, d2 = {"Lcom/ekoapp/ekosdk/uikit/community/explore/viewmodel/EkoCategoryCommunityListViewModel;", "Lcom/ekoapp/ekosdk/uikit/base/EkoBaseViewModel;", "()V", "communityItemClickListener", "Lcom/ekoapp/ekosdk/uikit/community/explore/listener/IEkoCommunityItemClickListener;", "getCommunityItemClickListener", "()Lcom/ekoapp/ekosdk/uikit/community/explore/listener/IEkoCommunityItemClickListener;", "setCommunityItemClickListener", "(Lcom/ekoapp/ekosdk/uikit/community/explore/listener/IEkoCommunityItemClickListener;)V", "communityRepository", "Lcom/ekoapp/ekosdk/EkoCommunityRepository;", "getCommunityByCategory", "Lio/reactivex/Flowable;", "Landroidx/paging/PagedList;", "Lcom/ekoapp/ekosdk/community/EkoCommunity;", "parentCategoryId", "", "community_debug"})
public final class EkoCategoryCommunityListViewModel extends com.ekoapp.ekosdk.uikit.base.EkoBaseViewModel {
    @org.jetbrains.annotations.Nullable()
    private com.ekoapp.ekosdk.uikit.community.explore.listener.IEkoCommunityItemClickListener communityItemClickListener;
    private final com.ekoapp.ekosdk.EkoCommunityRepository communityRepository = null;
    
    @org.jetbrains.annotations.Nullable()
    public final com.ekoapp.ekosdk.uikit.community.explore.listener.IEkoCommunityItemClickListener getCommunityItemClickListener() {
        return null;
    }
    
    public final void setCommunityItemClickListener(@org.jetbrains.annotations.Nullable()
    com.ekoapp.ekosdk.uikit.community.explore.listener.IEkoCommunityItemClickListener p0) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final io.reactivex.Flowable<androidx.paging.PagedList<com.ekoapp.ekosdk.community.EkoCommunity>> getCommunityByCategory(@org.jetbrains.annotations.NotNull()
    java.lang.String parentCategoryId) {
        return null;
    }
    
    public EkoCategoryCommunityListViewModel() {
        super();
    }
}