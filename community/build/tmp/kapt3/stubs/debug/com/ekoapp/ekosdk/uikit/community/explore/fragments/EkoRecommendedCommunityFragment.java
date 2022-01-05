package com.ekoapp.ekosdk.uikit.community.explore.fragments;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000P\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0007\u0018\u00002\u00020\u00012\u00020\u0002:\u0001$B\u0005\u00a2\u0006\u0002\u0010\u0003J\b\u0010\u000f\u001a\u00020\u0010H\u0002J\b\u0010\u0011\u001a\u00020\u0010H\u0002J\b\u0010\u0012\u001a\u00020\u0010H\u0002J\u0012\u0010\u0013\u001a\u00020\u00102\b\u0010\u0014\u001a\u0004\u0018\u00010\u0015H\u0002J\u0012\u0010\u0016\u001a\u00020\u00102\b\u0010\u0014\u001a\u0004\u0018\u00010\u0015H\u0016J&\u0010\u0017\u001a\u0004\u0018\u00010\u00182\u0006\u0010\u0019\u001a\u00020\u001a2\b\u0010\u001b\u001a\u0004\u0018\u00010\u001c2\b\u0010\u001d\u001a\u0004\u0018\u00010\u001eH\u0016J\b\u0010\u001f\u001a\u00020\u0010H\u0016J\u001a\u0010 \u001a\u00020\u00102\u0006\u0010!\u001a\u00020\u00182\b\u0010\u001d\u001a\u0004\u0018\u00010\u001eH\u0016J\r\u0010\"\u001a\u00020\u0010H\u0000\u00a2\u0006\u0002\b#R\u0016\u0010\u0004\u001a\n \u0006*\u0004\u0018\u00010\u00050\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082.\u00a2\u0006\u0002\n\u0000R\u001a\u0010\t\u001a\u00020\nX\u0086.\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u000b\u0010\f\"\u0004\b\r\u0010\u000e\u00a8\u0006%"}, d2 = {"Lcom/ekoapp/ekosdk/uikit/community/explore/fragments/EkoRecommendedCommunityFragment;", "Lcom/ekoapp/ekosdk/uikit/base/EkoBaseFragment;", "Lcom/ekoapp/ekosdk/uikit/community/mycommunity/listener/IMyCommunityItemClickListener;", "()V", "TAG", "", "kotlin.jvm.PlatformType", "mAdapter", "Lcom/ekoapp/ekosdk/uikit/community/explore/adapter/EkoRecommendedCommunitiesAdapter;", "mViewModel", "Lcom/ekoapp/ekosdk/uikit/community/explore/viewmodel/EkoExploreCommunityViewModel;", "getMViewModel", "()Lcom/ekoapp/ekosdk/uikit/community/explore/viewmodel/EkoExploreCommunityViewModel;", "setMViewModel", "(Lcom/ekoapp/ekosdk/uikit/community/explore/viewmodel/EkoExploreCommunityViewModel;)V", "addItemTouchListener", "", "getRecommendedCommunity", "initializeRecyclerView", "navigateToCommunityDetails", "ekoCommunity", "Lcom/ekoapp/ekosdk/community/EkoCommunity;", "onCommunitySelected", "onCreateView", "Landroid/view/View;", "inflater", "Landroid/view/LayoutInflater;", "container", "Landroid/view/ViewGroup;", "savedInstanceState", "Landroid/os/Bundle;", "onResume", "onViewCreated", "view", "refresh", "refresh$community_debug", "Builder", "community_debug"})
public final class EkoRecommendedCommunityFragment extends com.ekoapp.ekosdk.uikit.base.EkoBaseFragment implements com.ekoapp.ekosdk.uikit.community.mycommunity.listener.IMyCommunityItemClickListener {
    @org.jetbrains.annotations.NotNull()
    public com.ekoapp.ekosdk.uikit.community.explore.viewmodel.EkoExploreCommunityViewModel mViewModel;
    private com.ekoapp.ekosdk.uikit.community.explore.adapter.EkoRecommendedCommunitiesAdapter mAdapter;
    private final java.lang.String TAG = null;
    private java.util.HashMap _$_findViewCache;
    
    @org.jetbrains.annotations.NotNull()
    public final com.ekoapp.ekosdk.uikit.community.explore.viewmodel.EkoExploreCommunityViewModel getMViewModel() {
        return null;
    }
    
    public final void setMViewModel(@org.jetbrains.annotations.NotNull()
    com.ekoapp.ekosdk.uikit.community.explore.viewmodel.EkoExploreCommunityViewModel p0) {
    }
    
    @org.jetbrains.annotations.Nullable()
    @java.lang.Override()
    public android.view.View onCreateView(@org.jetbrains.annotations.NotNull()
    android.view.LayoutInflater inflater, @org.jetbrains.annotations.Nullable()
    android.view.ViewGroup container, @org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
        return null;
    }
    
    @java.lang.Override()
    public void onViewCreated(@org.jetbrains.annotations.NotNull()
    android.view.View view, @org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    @java.lang.Override()
    public void onResume() {
    }
    
    public final void refresh$community_debug() {
    }
    
    private final void initializeRecyclerView() {
    }
    
    private final void getRecommendedCommunity() {
    }
    
    private final void addItemTouchListener() {
    }
    
    @java.lang.Override()
    public void onCommunitySelected(@org.jetbrains.annotations.Nullable()
    com.ekoapp.ekosdk.community.EkoCommunity ekoCommunity) {
    }
    
    private final void navigateToCommunityDetails(com.ekoapp.ekosdk.community.EkoCommunity ekoCommunity) {
    }
    
    public EkoRecommendedCommunityFragment() {
        super();
    }
    
    @kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bJ\u0010\u0010\u0003\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u0004H\u0002R\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u000b"}, d2 = {"Lcom/ekoapp/ekosdk/uikit/community/explore/fragments/EkoRecommendedCommunityFragment$Builder;", "", "()V", "communityItemClickListener", "Lcom/ekoapp/ekosdk/uikit/community/mycommunity/listener/IMyCommunityItemClickListener;", "build", "Lcom/ekoapp/ekosdk/uikit/community/explore/fragments/EkoRecommendedCommunityFragment;", "activity", "Landroidx/appcompat/app/AppCompatActivity;", "", "listener", "community_debug"})
    public static final class Builder {
        private com.ekoapp.ekosdk.uikit.community.mycommunity.listener.IMyCommunityItemClickListener communityItemClickListener;
        
        @org.jetbrains.annotations.NotNull()
        public final com.ekoapp.ekosdk.uikit.community.explore.fragments.EkoRecommendedCommunityFragment build(@org.jetbrains.annotations.NotNull()
        androidx.appcompat.app.AppCompatActivity activity) {
            return null;
        }
        
        private final void communityItemClickListener(com.ekoapp.ekosdk.uikit.community.mycommunity.listener.IMyCommunityItemClickListener listener) {
        }
        
        public Builder() {
            super();
        }
    }
}