package com.ekoapp.ekosdk.uikit.community.explore.viewmodel;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000R\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0012\u0010,\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020/0.0-J\u0012\u00100\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u0002010.0-J\u0012\u00102\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u0002010.0-R\u001c\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\bR\u001c\u0010\t\u001a\u0004\u0018\u00010\nX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u000b\u0010\f\"\u0004\b\r\u0010\u000eR\u0011\u0010\u000f\u001a\u00020\u0010\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u0012R\u0011\u0010\u0013\u001a\u00020\u0010\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u0012R\u0011\u0010\u0015\u001a\u00020\u0010\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0016\u0010\u0012R\u001c\u0010\u0017\u001a\u0004\u0018\u00010\u0018X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0019\u0010\u001a\"\u0004\b\u001b\u0010\u001cR\u001c\u0010\u001d\u001a\u0004\u0018\u00010\u001eX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u001f\u0010 \"\u0004\b!\u0010\"R\u001c\u0010#\u001a\u0004\u0018\u00010\u0018X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b$\u0010\u001a\"\u0004\b%\u0010\u001cR\u001c\u0010&\u001a\u0004\u0018\u00010\'X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b(\u0010)\"\u0004\b*\u0010+\u00a8\u00063"}, d2 = {"Lcom/ekoapp/ekosdk/uikit/community/explore/viewmodel/EkoExploreCommunityViewModel;", "Lcom/ekoapp/ekosdk/uikit/base/EkoBaseViewModel;", "()V", "categoryItemClickListener", "Lcom/ekoapp/ekosdk/uikit/community/explore/listener/IEkoCategoryItemClickListener;", "getCategoryItemClickListener", "()Lcom/ekoapp/ekosdk/uikit/community/explore/listener/IEkoCategoryItemClickListener;", "setCategoryItemClickListener", "(Lcom/ekoapp/ekosdk/uikit/community/explore/listener/IEkoCategoryItemClickListener;)V", "categoryPreviewFragmentDelegate", "Lcom/ekoapp/ekosdk/uikit/community/explore/listener/ICategoryPreviewFragmentDelegate;", "getCategoryPreviewFragmentDelegate", "()Lcom/ekoapp/ekosdk/uikit/community/explore/listener/ICategoryPreviewFragmentDelegate;", "setCategoryPreviewFragmentDelegate", "(Lcom/ekoapp/ekosdk/uikit/community/explore/listener/ICategoryPreviewFragmentDelegate;)V", "emptyCategoryList", "Landroidx/databinding/ObservableBoolean;", "getEmptyCategoryList", "()Landroidx/databinding/ObservableBoolean;", "emptyRecommendedList", "getEmptyRecommendedList", "emptyTrendingList", "getEmptyTrendingList", "recommendedCommunityItemClickListener", "Lcom/ekoapp/ekosdk/uikit/community/mycommunity/listener/IMyCommunityItemClickListener;", "getRecommendedCommunityItemClickListener", "()Lcom/ekoapp/ekosdk/uikit/community/mycommunity/listener/IMyCommunityItemClickListener;", "setRecommendedCommunityItemClickListener", "(Lcom/ekoapp/ekosdk/uikit/community/mycommunity/listener/IMyCommunityItemClickListener;)V", "recommendedFragmentDelegate", "Lcom/ekoapp/ekosdk/uikit/community/explore/listener/IRecommendedCommunityFragmentDelegate;", "getRecommendedFragmentDelegate", "()Lcom/ekoapp/ekosdk/uikit/community/explore/listener/IRecommendedCommunityFragmentDelegate;", "setRecommendedFragmentDelegate", "(Lcom/ekoapp/ekosdk/uikit/community/explore/listener/IRecommendedCommunityFragmentDelegate;)V", "trendingCommunityItemClickListener", "getTrendingCommunityItemClickListener", "setTrendingCommunityItemClickListener", "trendingFragmentDelegate", "Lcom/ekoapp/ekosdk/uikit/community/explore/listener/ITrendingCommunityFragmentDelegate;", "getTrendingFragmentDelegate", "()Lcom/ekoapp/ekosdk/uikit/community/explore/listener/ITrendingCommunityFragmentDelegate;", "setTrendingFragmentDelegate", "(Lcom/ekoapp/ekosdk/uikit/community/explore/listener/ITrendingCommunityFragmentDelegate;)V", "getCommunityCategory", "Lio/reactivex/Flowable;", "Landroidx/paging/PagedList;", "Lcom/ekoapp/ekosdk/community/category/EkoCommunityCategory;", "getRecommendedCommunity", "Lcom/ekoapp/ekosdk/community/EkoCommunity;", "getTrendingCommunity", "community_debug"})
public final class EkoExploreCommunityViewModel extends com.ekoapp.ekosdk.uikit.base.EkoBaseViewModel {
    @org.jetbrains.annotations.Nullable()
    private com.ekoapp.ekosdk.uikit.community.explore.listener.IEkoCategoryItemClickListener categoryItemClickListener;
    @org.jetbrains.annotations.Nullable()
    private com.ekoapp.ekosdk.uikit.community.mycommunity.listener.IMyCommunityItemClickListener trendingCommunityItemClickListener;
    @org.jetbrains.annotations.Nullable()
    private com.ekoapp.ekosdk.uikit.community.mycommunity.listener.IMyCommunityItemClickListener recommendedCommunityItemClickListener;
    @org.jetbrains.annotations.Nullable()
    private com.ekoapp.ekosdk.uikit.community.explore.listener.ICategoryPreviewFragmentDelegate categoryPreviewFragmentDelegate;
    @org.jetbrains.annotations.Nullable()
    private com.ekoapp.ekosdk.uikit.community.explore.listener.ITrendingCommunityFragmentDelegate trendingFragmentDelegate;
    @org.jetbrains.annotations.Nullable()
    private com.ekoapp.ekosdk.uikit.community.explore.listener.IRecommendedCommunityFragmentDelegate recommendedFragmentDelegate;
    @org.jetbrains.annotations.NotNull()
    private final androidx.databinding.ObservableBoolean emptyRecommendedList = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.databinding.ObservableBoolean emptyTrendingList = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.databinding.ObservableBoolean emptyCategoryList = null;
    
    @org.jetbrains.annotations.Nullable()
    public final com.ekoapp.ekosdk.uikit.community.explore.listener.IEkoCategoryItemClickListener getCategoryItemClickListener() {
        return null;
    }
    
    public final void setCategoryItemClickListener(@org.jetbrains.annotations.Nullable()
    com.ekoapp.ekosdk.uikit.community.explore.listener.IEkoCategoryItemClickListener p0) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.ekoapp.ekosdk.uikit.community.mycommunity.listener.IMyCommunityItemClickListener getTrendingCommunityItemClickListener() {
        return null;
    }
    
    public final void setTrendingCommunityItemClickListener(@org.jetbrains.annotations.Nullable()
    com.ekoapp.ekosdk.uikit.community.mycommunity.listener.IMyCommunityItemClickListener p0) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.ekoapp.ekosdk.uikit.community.mycommunity.listener.IMyCommunityItemClickListener getRecommendedCommunityItemClickListener() {
        return null;
    }
    
    public final void setRecommendedCommunityItemClickListener(@org.jetbrains.annotations.Nullable()
    com.ekoapp.ekosdk.uikit.community.mycommunity.listener.IMyCommunityItemClickListener p0) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.ekoapp.ekosdk.uikit.community.explore.listener.ICategoryPreviewFragmentDelegate getCategoryPreviewFragmentDelegate() {
        return null;
    }
    
    public final void setCategoryPreviewFragmentDelegate(@org.jetbrains.annotations.Nullable()
    com.ekoapp.ekosdk.uikit.community.explore.listener.ICategoryPreviewFragmentDelegate p0) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.ekoapp.ekosdk.uikit.community.explore.listener.ITrendingCommunityFragmentDelegate getTrendingFragmentDelegate() {
        return null;
    }
    
    public final void setTrendingFragmentDelegate(@org.jetbrains.annotations.Nullable()
    com.ekoapp.ekosdk.uikit.community.explore.listener.ITrendingCommunityFragmentDelegate p0) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.ekoapp.ekosdk.uikit.community.explore.listener.IRecommendedCommunityFragmentDelegate getRecommendedFragmentDelegate() {
        return null;
    }
    
    public final void setRecommendedFragmentDelegate(@org.jetbrains.annotations.Nullable()
    com.ekoapp.ekosdk.uikit.community.explore.listener.IRecommendedCommunityFragmentDelegate p0) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.databinding.ObservableBoolean getEmptyRecommendedList() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.databinding.ObservableBoolean getEmptyTrendingList() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.databinding.ObservableBoolean getEmptyCategoryList() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final io.reactivex.Flowable<androidx.paging.PagedList<com.ekoapp.ekosdk.community.EkoCommunity>> getRecommendedCommunity() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final io.reactivex.Flowable<androidx.paging.PagedList<com.ekoapp.ekosdk.community.EkoCommunity>> getTrendingCommunity() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final io.reactivex.Flowable<androidx.paging.PagedList<com.ekoapp.ekosdk.community.category.EkoCommunityCategory>> getCommunityCategory() {
        return null;
    }
    
    public EkoExploreCommunityViewModel() {
        super();
    }
}