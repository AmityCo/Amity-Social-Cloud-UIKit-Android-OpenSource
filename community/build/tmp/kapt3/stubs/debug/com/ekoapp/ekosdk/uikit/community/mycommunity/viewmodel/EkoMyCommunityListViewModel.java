package com.ekoapp.ekosdk.uikit.community.mycommunity.viewmodel;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000<\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0012\u0010\u0013\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00160\u00150\u0014J\u0006\u0010\u0017\u001a\u00020\u0018R\u0011\u0010\u0003\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006R\u001c\u0010\u0007\u001a\u0004\u0018\u00010\bX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\t\u0010\n\"\u0004\b\u000b\u0010\fR\u001f\u0010\r\u001a\u0010\u0012\f\u0012\n \u0010*\u0004\u0018\u00010\u000f0\u000f0\u000e\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u0012\u00a8\u0006\u0019"}, d2 = {"Lcom/ekoapp/ekosdk/uikit/community/mycommunity/viewmodel/EkoMyCommunityListViewModel;", "Lcom/ekoapp/ekosdk/uikit/base/EkoBaseViewModel;", "()V", "emptyCommunity", "Landroidx/databinding/ObservableBoolean;", "getEmptyCommunity", "()Landroidx/databinding/ObservableBoolean;", "myCommunityItemClickListener", "Lcom/ekoapp/ekosdk/uikit/community/mycommunity/listener/IMyCommunityItemClickListener;", "getMyCommunityItemClickListener", "()Lcom/ekoapp/ekosdk/uikit/community/mycommunity/listener/IMyCommunityItemClickListener;", "setMyCommunityItemClickListener", "(Lcom/ekoapp/ekosdk/uikit/community/mycommunity/listener/IMyCommunityItemClickListener;)V", "searchString", "Landroidx/databinding/ObservableField;", "", "kotlin.jvm.PlatformType", "getSearchString", "()Landroidx/databinding/ObservableField;", "getCommunityList", "Lio/reactivex/Flowable;", "Landroidx/paging/PagedList;", "Lcom/ekoapp/ekosdk/community/EkoCommunity;", "setPropertyChangeCallback", "", "community_debug"})
public final class EkoMyCommunityListViewModel extends com.ekoapp.ekosdk.uikit.base.EkoBaseViewModel {
    @org.jetbrains.annotations.Nullable()
    private com.ekoapp.ekosdk.uikit.community.mycommunity.listener.IMyCommunityItemClickListener myCommunityItemClickListener;
    @org.jetbrains.annotations.NotNull()
    private final androidx.databinding.ObservableField<java.lang.String> searchString = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.databinding.ObservableBoolean emptyCommunity = null;
    
    @org.jetbrains.annotations.Nullable()
    public final com.ekoapp.ekosdk.uikit.community.mycommunity.listener.IMyCommunityItemClickListener getMyCommunityItemClickListener() {
        return null;
    }
    
    public final void setMyCommunityItemClickListener(@org.jetbrains.annotations.Nullable()
    com.ekoapp.ekosdk.uikit.community.mycommunity.listener.IMyCommunityItemClickListener p0) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.databinding.ObservableField<java.lang.String> getSearchString() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.databinding.ObservableBoolean getEmptyCommunity() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final io.reactivex.Flowable<androidx.paging.PagedList<com.ekoapp.ekosdk.community.EkoCommunity>> getCommunityList() {
        return null;
    }
    
    public final void setPropertyChangeCallback() {
    }
    
    public EkoMyCommunityListViewModel() {
        super();
    }
}