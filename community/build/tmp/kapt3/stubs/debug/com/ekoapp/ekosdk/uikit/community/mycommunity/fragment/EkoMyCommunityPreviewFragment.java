package com.ekoapp.ekosdk.uikit.community.mycommunity.fragment;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000V\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\b\u0018\u00002\u00020\u00012\u00020\u0002:\u0001*B\u0007\b\u0000\u00a2\u0006\u0002\u0010\u0003J\b\u0010\u0015\u001a\u00020\u0016H\u0002J\b\u0010\u0017\u001a\u00020\u0016H\u0002J\b\u0010\u0018\u001a\u00020\u0016H\u0002J\u0012\u0010\u0019\u001a\u00020\u00162\b\u0010\u001a\u001a\u0004\u0018\u00010\u001bH\u0016J&\u0010\u001c\u001a\u0004\u0018\u00010\u001d2\u0006\u0010\u001e\u001a\u00020\u001f2\b\u0010 \u001a\u0004\u0018\u00010!2\b\u0010\"\u001a\u0004\u0018\u00010#H\u0016J\b\u0010$\u001a\u00020\u0016H\u0016J\b\u0010%\u001a\u00020\u0016H\u0016J\u001a\u0010&\u001a\u00020\u00162\u0006\u0010\'\u001a\u00020\u001d2\b\u0010\"\u001a\u0004\u0018\u00010#H\u0016J\r\u0010(\u001a\u00020\u0016H\u0000\u00a2\u0006\u0002\b)R\u0016\u0010\u0004\u001a\n \u0006*\u0004\u0018\u00010\u00050\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082.\u00a2\u0006\u0002\n\u0000R\u001a\u0010\t\u001a\u00020\nX\u0086.\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u000b\u0010\f\"\u0004\b\r\u0010\u000eR\u001b\u0010\u000f\u001a\u00020\u00108BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\u0013\u0010\u0014\u001a\u0004\b\u0011\u0010\u0012\u00a8\u0006+"}, d2 = {"Lcom/ekoapp/ekosdk/uikit/community/mycommunity/fragment/EkoMyCommunityPreviewFragment;", "Lcom/ekoapp/ekosdk/uikit/base/EkoBaseFragment;", "Lcom/ekoapp/ekosdk/uikit/community/mycommunity/listener/IMyCommunityItemClickListener;", "()V", "TAG", "", "kotlin.jvm.PlatformType", "mAdapter", "Lcom/ekoapp/ekosdk/uikit/community/mycommunity/adapter/EkoMyCommunityPreviewAdapter;", "mViewModel", "Lcom/ekoapp/ekosdk/uikit/community/mycommunity/viewmodel/EkoMyCommunityListViewModel;", "getMViewModel", "()Lcom/ekoapp/ekosdk/uikit/community/mycommunity/viewmodel/EkoMyCommunityListViewModel;", "setMViewModel", "(Lcom/ekoapp/ekosdk/uikit/community/mycommunity/viewmodel/EkoMyCommunityListViewModel;)V", "newFeedViewModel", "Lcom/ekoapp/ekosdk/uikit/community/newsfeed/viewmodel/EkoNewsFeedViewModel;", "getNewFeedViewModel", "()Lcom/ekoapp/ekosdk/uikit/community/newsfeed/viewmodel/EkoNewsFeedViewModel;", "newFeedViewModel$delegate", "Lkotlin/Lazy;", "addItemTouchListener", "", "getCommunityList", "initRecyclerView", "onCommunitySelected", "ekoCommunity", "Lcom/ekoapp/ekosdk/community/EkoCommunity;", "onCreateView", "Landroid/view/View;", "inflater", "Landroid/view/LayoutInflater;", "container", "Landroid/view/ViewGroup;", "savedInstanceState", "Landroid/os/Bundle;", "onResume", "onStart", "onViewCreated", "view", "refresh", "refresh$community_debug", "Builder", "community_debug"})
public final class EkoMyCommunityPreviewFragment extends com.ekoapp.ekosdk.uikit.base.EkoBaseFragment implements com.ekoapp.ekosdk.uikit.community.mycommunity.listener.IMyCommunityItemClickListener {
    @org.jetbrains.annotations.NotNull()
    public com.ekoapp.ekosdk.uikit.community.mycommunity.viewmodel.EkoMyCommunityListViewModel mViewModel;
    private final kotlin.Lazy newFeedViewModel$delegate = null;
    private com.ekoapp.ekosdk.uikit.community.mycommunity.adapter.EkoMyCommunityPreviewAdapter mAdapter;
    private final java.lang.String TAG = null;
    private java.util.HashMap _$_findViewCache;
    
    @org.jetbrains.annotations.NotNull()
    public final com.ekoapp.ekosdk.uikit.community.mycommunity.viewmodel.EkoMyCommunityListViewModel getMViewModel() {
        return null;
    }
    
    public final void setMViewModel(@org.jetbrains.annotations.NotNull()
    com.ekoapp.ekosdk.uikit.community.mycommunity.viewmodel.EkoMyCommunityListViewModel p0) {
    }
    
    private final com.ekoapp.ekosdk.uikit.community.newsfeed.viewmodel.EkoNewsFeedViewModel getNewFeedViewModel() {
        return null;
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
    public void onStart() {
    }
    
    @java.lang.Override()
    public void onResume() {
    }
    
    public final void refresh$community_debug() {
    }
    
    private final void initRecyclerView() {
    }
    
    private final void getCommunityList() {
    }
    
    private final void addItemTouchListener() {
    }
    
    @java.lang.Override()
    public void onCommunitySelected(@org.jetbrains.annotations.Nullable()
    com.ekoapp.ekosdk.community.EkoCommunity ekoCommunity) {
    }
    
    public EkoMyCommunityPreviewFragment() {
        super();
    }
    
    @kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bJ\u0010\u0010\u0003\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0004H\u0002R\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\n"}, d2 = {"Lcom/ekoapp/ekosdk/uikit/community/mycommunity/fragment/EkoMyCommunityPreviewFragment$Builder;", "", "()V", "myCommunityItemClickListener", "Lcom/ekoapp/ekosdk/uikit/community/mycommunity/listener/IMyCommunityItemClickListener;", "build", "Lcom/ekoapp/ekosdk/uikit/community/mycommunity/fragment/EkoMyCommunityPreviewFragment;", "activity", "Landroidx/appcompat/app/AppCompatActivity;", "listener", "community_debug"})
    public static final class Builder {
        private com.ekoapp.ekosdk.uikit.community.mycommunity.listener.IMyCommunityItemClickListener myCommunityItemClickListener;
        
        @org.jetbrains.annotations.NotNull()
        public final com.ekoapp.ekosdk.uikit.community.mycommunity.fragment.EkoMyCommunityPreviewFragment build(@org.jetbrains.annotations.NotNull()
        androidx.appcompat.app.AppCompatActivity activity) {
            return null;
        }
        
        private final com.ekoapp.ekosdk.uikit.community.mycommunity.fragment.EkoMyCommunityPreviewFragment.Builder myCommunityItemClickListener(com.ekoapp.ekosdk.uikit.community.mycommunity.listener.IMyCommunityItemClickListener listener) {
            return null;
        }
        
        public Builder() {
            super();
        }
    }
}