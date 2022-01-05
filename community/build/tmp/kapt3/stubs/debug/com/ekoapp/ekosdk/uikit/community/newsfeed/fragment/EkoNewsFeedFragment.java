package com.ekoapp.ekosdk.uikit.community.newsfeed.fragment;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000Z\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0006\u0018\u00002\u00020\u00012\u00020\u0002:\u0001$B\u0007\b\u0000\u00a2\u0006\u0002\u0010\u0003J\b\u0010\u000e\u001a\u00020\u000fH\u0002J\b\u0010\u0010\u001a\u00020\u0011H\u0002J\b\u0010\u0012\u001a\u00020\u0011H\u0002J$\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u00162\b\u0010\u0017\u001a\u0004\u0018\u00010\u00182\b\u0010\u0019\u001a\u0004\u0018\u00010\u001aH\u0016J\u001a\u0010\u001b\u001a\u00020\u000f2\b\u0010\u001c\u001a\u0004\u0018\u00010\u001d2\u0006\u0010\u001e\u001a\u00020\u001fH\u0016J\b\u0010 \u001a\u00020\u000fH\u0016J\b\u0010!\u001a\u00020\u000fH\u0016J\u001a\u0010\"\u001a\u00020\u000f2\u0006\u0010#\u001a\u00020\u00142\b\u0010\u0019\u001a\u0004\u0018\u00010\u001aH\u0016R\u001b\u0010\u0004\u001a\u00020\u00058BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\b\u0010\t\u001a\u0004\b\u0006\u0010\u0007R\u000e\u0010\n\u001a\u00020\u000bX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\rX\u0082.\u00a2\u0006\u0002\n\u0000\u00a8\u0006%"}, d2 = {"Lcom/ekoapp/ekosdk/uikit/community/newsfeed/fragment/EkoNewsFeedFragment;", "Lcom/ekoapp/ekosdk/uikit/base/EkoBaseFragment;", "Lcom/google/android/material/appbar/AppBarLayout$OnOffsetChangedListener;", "()V", "communityHomeViewModel", "Lcom/ekoapp/ekosdk/uikit/community/home/fragments/EkoCommunityHomeViewModel;", "getCommunityHomeViewModel", "()Lcom/ekoapp/ekosdk/uikit/community/home/fragments/EkoCommunityHomeViewModel;", "communityHomeViewModel$delegate", "Lkotlin/Lazy;", "mBinding", "Lcom/ekoapp/ekosdk/uikit/community/databinding/AmityFragmentNewsFeedBinding;", "mViewModel", "Lcom/ekoapp/ekosdk/uikit/community/newsfeed/viewmodel/EkoNewsFeedViewModel;", "addViewModelListener", "", "getGlobalFeed", "Landroidx/fragment/app/Fragment;", "getMyCommunityPreviewFragment", "onCreateView", "Landroid/view/View;", "inflater", "Landroid/view/LayoutInflater;", "container", "Landroid/view/ViewGroup;", "savedInstanceState", "Landroid/os/Bundle;", "onOffsetChanged", "appBarLayout", "Lcom/google/android/material/appbar/AppBarLayout;", "verticalOffset", "", "onPause", "onResume", "onViewCreated", "view", "Builder", "community_debug"})
public final class EkoNewsFeedFragment extends com.ekoapp.ekosdk.uikit.base.EkoBaseFragment implements com.google.android.material.appbar.AppBarLayout.OnOffsetChangedListener {
    private com.ekoapp.ekosdk.uikit.community.databinding.AmityFragmentNewsFeedBinding mBinding;
    private com.ekoapp.ekosdk.uikit.community.newsfeed.viewmodel.EkoNewsFeedViewModel mViewModel;
    private final kotlin.Lazy communityHomeViewModel$delegate = null;
    private java.util.HashMap _$_findViewCache;
    
    private final com.ekoapp.ekosdk.uikit.community.home.fragments.EkoCommunityHomeViewModel getCommunityHomeViewModel() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
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
    public void onOffsetChanged(@org.jetbrains.annotations.Nullable()
    com.google.android.material.appbar.AppBarLayout appBarLayout, int verticalOffset) {
    }
    
    @java.lang.Override()
    public void onResume() {
    }
    
    @java.lang.Override()
    public void onPause() {
    }
    
    private final androidx.fragment.app.Fragment getMyCommunityPreviewFragment() {
        return null;
    }
    
    private final androidx.fragment.app.Fragment getGlobalFeed() {
        return null;
    }
    
    private final void addViewModelListener() {
    }
    
    public EkoNewsFeedFragment() {
        super();
    }
    
    @kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\fJ\u0010\u0010\u0003\u001a\u00020\u00002\u0006\u0010\r\u001a\u00020\u0004H\u0002J\u000e\u0010\u0005\u001a\u00020\u00002\u0006\u0010\u000e\u001a\u00020\u0006J\u0010\u0010\u000f\u001a\u00020\u00002\u0006\u0010\u000e\u001a\u00020\bH\u0002R\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0005\u001a\u0004\u0018\u00010\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0007\u001a\u0004\u0018\u00010\bX\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0010"}, d2 = {"Lcom/ekoapp/ekosdk/uikit/community/newsfeed/fragment/EkoNewsFeedFragment$Builder;", "", "()V", "createPostButtonClickListener", "Lcom/ekoapp/ekosdk/uikit/community/newsfeed/listener/ICreatePostButtonClickListener;", "globalFeedFragmentDelegate", "Lcom/ekoapp/ekosdk/uikit/community/home/listener/IGlobalFeedFragmentDelegate;", "myCommunityListPreviewFragmentDelegate", "Lcom/ekoapp/ekosdk/uikit/community/home/listener/IMyCommunityListPreviewFragmentDelegate;", "build", "Lcom/ekoapp/ekosdk/uikit/community/newsfeed/fragment/EkoNewsFeedFragment;", "activity", "Landroidx/appcompat/app/AppCompatActivity;", "listener", "delegate", "myCommunityPreviewFragmentDelegate", "community_debug"})
    public static final class Builder {
        private com.ekoapp.ekosdk.uikit.community.home.listener.IMyCommunityListPreviewFragmentDelegate myCommunityListPreviewFragmentDelegate;
        private com.ekoapp.ekosdk.uikit.community.home.listener.IGlobalFeedFragmentDelegate globalFeedFragmentDelegate;
        private com.ekoapp.ekosdk.uikit.community.newsfeed.listener.ICreatePostButtonClickListener createPostButtonClickListener;
        
        @org.jetbrains.annotations.NotNull()
        public final com.ekoapp.ekosdk.uikit.community.newsfeed.fragment.EkoNewsFeedFragment build(@org.jetbrains.annotations.NotNull()
        androidx.appcompat.app.AppCompatActivity activity) {
            return null;
        }
        
        private final com.ekoapp.ekosdk.uikit.community.newsfeed.fragment.EkoNewsFeedFragment.Builder createPostButtonClickListener(com.ekoapp.ekosdk.uikit.community.newsfeed.listener.ICreatePostButtonClickListener listener) {
            return null;
        }
        
        private final com.ekoapp.ekosdk.uikit.community.newsfeed.fragment.EkoNewsFeedFragment.Builder myCommunityPreviewFragmentDelegate(com.ekoapp.ekosdk.uikit.community.home.listener.IMyCommunityListPreviewFragmentDelegate delegate) {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.ekoapp.ekosdk.uikit.community.newsfeed.fragment.EkoNewsFeedFragment.Builder globalFeedFragmentDelegate(@org.jetbrains.annotations.NotNull()
        com.ekoapp.ekosdk.uikit.community.home.listener.IGlobalFeedFragmentDelegate delegate) {
            return null;
        }
        
        public Builder() {
            super();
        }
    }
}