package com.ekoapp.ekosdk.uikit.community.home.fragments;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u00008\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u001a\u0010\u0018\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u001b0\u001a0\u00192\u0006\u0010\u001c\u001a\u00020\u001dR\u0011\u0010\u0003\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006R\u0011\u0010\u0007\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\b\u0010\u0006R\u001c\u0010\t\u001a\u0004\u0018\u00010\nX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u000b\u0010\f\"\u0004\b\r\u0010\u000eR\u001a\u0010\u000f\u001a\u00020\u0004X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u000f\u0010\u0006\"\u0004\b\u0010\u0010\u0011R\u001c\u0010\u0012\u001a\u0004\u0018\u00010\u0013X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0014\u0010\u0015\"\u0004\b\u0016\u0010\u0017\u00a8\u0006\u001e"}, d2 = {"Lcom/ekoapp/ekosdk/uikit/community/home/fragments/EkoCommunityHomeViewModel;", "Lcom/ekoapp/ekosdk/uikit/base/EkoBaseViewModel;", "()V", "emptySearch", "Landroidx/databinding/ObservableBoolean;", "getEmptySearch", "()Landroidx/databinding/ObservableBoolean;", "emptySearchString", "getEmptySearchString", "exploreFragmentDelegate", "Lcom/ekoapp/ekosdk/uikit/community/home/listener/IExploreFragmentFragmentDelegate;", "getExploreFragmentDelegate", "()Lcom/ekoapp/ekosdk/uikit/community/home/listener/IExploreFragmentFragmentDelegate;", "setExploreFragmentDelegate", "(Lcom/ekoapp/ekosdk/uikit/community/home/listener/IExploreFragmentFragmentDelegate;)V", "isSearchMode", "setSearchMode", "(Landroidx/databinding/ObservableBoolean;)V", "newsFeedFragmentDelegate", "Lcom/ekoapp/ekosdk/uikit/community/home/listener/INewsFeedFragmentDelegate;", "getNewsFeedFragmentDelegate", "()Lcom/ekoapp/ekosdk/uikit/community/home/listener/INewsFeedFragmentDelegate;", "setNewsFeedFragmentDelegate", "(Lcom/ekoapp/ekosdk/uikit/community/home/listener/INewsFeedFragmentDelegate;)V", "searchCommunity", "Lio/reactivex/Flowable;", "Landroidx/paging/PagedList;", "Lcom/ekoapp/ekosdk/community/EkoCommunity;", "searchString", "", "community_debug"})
public final class EkoCommunityHomeViewModel extends com.ekoapp.ekosdk.uikit.base.EkoBaseViewModel {
    @org.jetbrains.annotations.Nullable()
    private com.ekoapp.ekosdk.uikit.community.home.listener.IExploreFragmentFragmentDelegate exploreFragmentDelegate;
    @org.jetbrains.annotations.Nullable()
    private com.ekoapp.ekosdk.uikit.community.home.listener.INewsFeedFragmentDelegate newsFeedFragmentDelegate;
    @org.jetbrains.annotations.NotNull()
    private androidx.databinding.ObservableBoolean isSearchMode;
    @org.jetbrains.annotations.NotNull()
    private final androidx.databinding.ObservableBoolean emptySearch = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.databinding.ObservableBoolean emptySearchString = null;
    
    @org.jetbrains.annotations.Nullable()
    public final com.ekoapp.ekosdk.uikit.community.home.listener.IExploreFragmentFragmentDelegate getExploreFragmentDelegate() {
        return null;
    }
    
    public final void setExploreFragmentDelegate(@org.jetbrains.annotations.Nullable()
    com.ekoapp.ekosdk.uikit.community.home.listener.IExploreFragmentFragmentDelegate p0) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.ekoapp.ekosdk.uikit.community.home.listener.INewsFeedFragmentDelegate getNewsFeedFragmentDelegate() {
        return null;
    }
    
    public final void setNewsFeedFragmentDelegate(@org.jetbrains.annotations.Nullable()
    com.ekoapp.ekosdk.uikit.community.home.listener.INewsFeedFragmentDelegate p0) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.databinding.ObservableBoolean isSearchMode() {
        return null;
    }
    
    public final void setSearchMode(@org.jetbrains.annotations.NotNull()
    androidx.databinding.ObservableBoolean p0) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.databinding.ObservableBoolean getEmptySearch() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.databinding.ObservableBoolean getEmptySearchString() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final io.reactivex.Flowable<androidx.paging.PagedList<com.ekoapp.ekosdk.community.EkoCommunity>> searchCommunity(@org.jetbrains.annotations.NotNull()
    java.lang.String searchString) {
        return null;
    }
    
    public EkoCommunityHomeViewModel() {
        super();
    }
}