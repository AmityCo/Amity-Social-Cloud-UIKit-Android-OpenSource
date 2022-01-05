package com.ekoapp.ekosdk.uikit.community.home.fragments;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000x\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\b\u0018\u00002\u00020\u00012\u00020\u0002:\u00014B\u0007\b\u0000\u00a2\u0006\u0002\u0010\u0003J\b\u0010\u0018\u001a\u00020\u0019H\u0002J\b\u0010\u001a\u001a\u00020\u0001H\u0002J\b\u0010\u001b\u001a\u00020\u0001H\u0002J\b\u0010\u001c\u001a\u00020\u0019H\u0002J\b\u0010\u001d\u001a\u00020\u0019H\u0002J\u0012\u0010\u001e\u001a\u00020\u00192\b\u0010\u001f\u001a\u0004\u0018\u00010 H\u0016J\u0012\u0010!\u001a\u00020\u00192\b\u0010\"\u001a\u0004\u0018\u00010#H\u0016J\u0018\u0010$\u001a\u00020\u00192\u0006\u0010%\u001a\u00020&2\u0006\u0010\'\u001a\u00020(H\u0016J&\u0010)\u001a\u0004\u0018\u00010*2\u0006\u0010\'\u001a\u00020+2\b\u0010,\u001a\u0004\u0018\u00010-2\b\u0010\"\u001a\u0004\u0018\u00010#H\u0016J\b\u0010.\u001a\u00020\u0019H\u0016J\u001a\u0010/\u001a\u00020\u00192\u0006\u00100\u001a\u00020*2\b\u0010\"\u001a\u0004\u0018\u00010#H\u0016J\u0010\u00101\u001a\u00020\u00192\u0006\u00102\u001a\u00020\u0017H\u0002J\b\u00103\u001a\u00020\u0019H\u0002R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082.\u00a2\u0006\u0002\n\u0000R\u001a\u0010\n\u001a\u00020\u000bX\u0086.\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\f\u0010\r\"\u0004\b\u000e\u0010\u000fR\u000e\u0010\u0010\u001a\u00020\u0011X\u0082.\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0012\u001a\u0004\u0018\u00010\u0013X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0014\u001a\u0004\u0018\u00010\u0013X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0015\u001a\b\u0012\u0004\u0012\u00020\u00170\u0016X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u00065"}, d2 = {"Lcom/ekoapp/ekosdk/uikit/community/home/fragments/EkoCommunityHomePageFragment;", "Landroidx/fragment/app/Fragment;", "Lcom/ekoapp/ekosdk/uikit/community/mycommunity/listener/IMyCommunityItemClickListener;", "()V", "fragmentStateAdapter", "Lcom/ekoapp/ekosdk/uikit/base/EkoFragmentStateAdapter;", "mBinding", "Lcom/ekoapp/ekosdk/uikit/community/databinding/AmityFragmentCommunityHomePageBinding;", "mSearchAdapter", "Lcom/ekoapp/ekosdk/uikit/community/mycommunity/adapter/EkoMyCommunitiesAdapter;", "mViewModel", "Lcom/ekoapp/ekosdk/uikit/community/home/fragments/EkoCommunityHomeViewModel;", "getMViewModel", "()Lcom/ekoapp/ekosdk/uikit/community/home/fragments/EkoCommunityHomeViewModel;", "setMViewModel", "(Lcom/ekoapp/ekosdk/uikit/community/home/fragments/EkoCommunityHomeViewModel;)V", "searchMenuItem", "Landroid/view/MenuItem;", "searchResultDisposable", "Lio/reactivex/disposables/Disposable;", "textChangeDisposable", "textChangeSubject", "Lio/reactivex/subjects/PublishSubject;", "", "addViewModelListeners", "", "getExploreFragment", "getNewsFeedFragment", "initSearchRecyclerview", "initTabLayout", "onCommunitySelected", "ekoCommunity", "Lcom/ekoapp/ekosdk/community/EkoCommunity;", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "onCreateOptionsMenu", "menu", "Landroid/view/Menu;", "inflater", "Landroid/view/MenuInflater;", "onCreateView", "Landroid/view/View;", "Landroid/view/LayoutInflater;", "container", "Landroid/view/ViewGroup;", "onDestroyView", "onViewCreated", "view", "searchCommunity", "newText", "subscribeTextChangeEvents", "Builder", "community_debug"})
public final class EkoCommunityHomePageFragment extends androidx.fragment.app.Fragment implements com.ekoapp.ekosdk.uikit.community.mycommunity.listener.IMyCommunityItemClickListener {
    private com.ekoapp.ekosdk.uikit.base.EkoFragmentStateAdapter fragmentStateAdapter;
    private android.view.MenuItem searchMenuItem;
    @org.jetbrains.annotations.NotNull()
    public com.ekoapp.ekosdk.uikit.community.home.fragments.EkoCommunityHomeViewModel mViewModel;
    private com.ekoapp.ekosdk.uikit.community.databinding.AmityFragmentCommunityHomePageBinding mBinding;
    private com.ekoapp.ekosdk.uikit.community.mycommunity.adapter.EkoMyCommunitiesAdapter mSearchAdapter;
    private io.reactivex.disposables.Disposable searchResultDisposable;
    private io.reactivex.disposables.Disposable textChangeDisposable;
    private final io.reactivex.subjects.PublishSubject<java.lang.String> textChangeSubject = null;
    private java.util.HashMap _$_findViewCache;
    
    @org.jetbrains.annotations.NotNull()
    public final com.ekoapp.ekosdk.uikit.community.home.fragments.EkoCommunityHomeViewModel getMViewModel() {
        return null;
    }
    
    public final void setMViewModel(@org.jetbrains.annotations.NotNull()
    com.ekoapp.ekosdk.uikit.community.home.fragments.EkoCommunityHomeViewModel p0) {
    }
    
    @java.lang.Override()
    public void onCreate(@org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
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
    public void onDestroyView() {
    }
    
    private final void initTabLayout() {
    }
    
    private final androidx.fragment.app.Fragment getExploreFragment() {
        return null;
    }
    
    private final androidx.fragment.app.Fragment getNewsFeedFragment() {
        return null;
    }
    
    private final void addViewModelListeners() {
    }
    
    private final void initSearchRecyclerview() {
    }
    
    private final void subscribeTextChangeEvents() {
    }
    
    private final void searchCommunity(java.lang.String newText) {
    }
    
    @java.lang.Override()
    public void onCreateOptionsMenu(@org.jetbrains.annotations.NotNull()
    android.view.Menu menu, @org.jetbrains.annotations.NotNull()
    android.view.MenuInflater inflater) {
    }
    
    @java.lang.Override()
    public void onCommunitySelected(@org.jetbrains.annotations.Nullable()
    com.ekoapp.ekosdk.community.EkoCommunity ekoCommunity) {
    }
    
    public EkoCommunityHomePageFragment() {
        super();
    }
    
    @kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\nJ\u000e\u0010\u0003\u001a\u00020\u00002\u0006\u0010\u000b\u001a\u00020\u0004J\u000e\u0010\u0005\u001a\u00020\u00002\u0006\u0010\u000b\u001a\u00020\u0006R\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0005\u001a\u0004\u0018\u00010\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\f"}, d2 = {"Lcom/ekoapp/ekosdk/uikit/community/home/fragments/EkoCommunityHomePageFragment$Builder;", "", "()V", "exploreFragmentDelegate", "Lcom/ekoapp/ekosdk/uikit/community/home/listener/IExploreFragmentFragmentDelegate;", "newsFeedFragmentDelegate", "Lcom/ekoapp/ekosdk/uikit/community/home/listener/INewsFeedFragmentDelegate;", "build", "Lcom/ekoapp/ekosdk/uikit/community/home/fragments/EkoCommunityHomePageFragment;", "activity", "Landroidx/appcompat/app/AppCompatActivity;", "delegate", "community_debug"})
    public static final class Builder {
        private com.ekoapp.ekosdk.uikit.community.home.listener.INewsFeedFragmentDelegate newsFeedFragmentDelegate;
        private com.ekoapp.ekosdk.uikit.community.home.listener.IExploreFragmentFragmentDelegate exploreFragmentDelegate;
        
        @org.jetbrains.annotations.NotNull()
        public final com.ekoapp.ekosdk.uikit.community.home.fragments.EkoCommunityHomePageFragment build(@org.jetbrains.annotations.NotNull()
        androidx.appcompat.app.AppCompatActivity activity) {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.ekoapp.ekosdk.uikit.community.home.fragments.EkoCommunityHomePageFragment.Builder newsFeedFragmentDelegate(@org.jetbrains.annotations.NotNull()
        com.ekoapp.ekosdk.uikit.community.home.listener.INewsFeedFragmentDelegate delegate) {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.ekoapp.ekosdk.uikit.community.home.fragments.EkoCommunityHomePageFragment.Builder exploreFragmentDelegate(@org.jetbrains.annotations.NotNull()
        com.ekoapp.ekosdk.uikit.community.home.listener.IExploreFragmentFragmentDelegate delegate) {
            return null;
        }
        
        public Builder() {
            super();
        }
    }
}