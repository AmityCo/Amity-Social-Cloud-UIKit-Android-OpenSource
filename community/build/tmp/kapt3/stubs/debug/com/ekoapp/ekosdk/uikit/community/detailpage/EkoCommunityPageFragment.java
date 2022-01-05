package com.ekoapp.ekosdk.uikit.community.detailpage;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\u0082\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0010\u0018\u00002\u00020\u00012\u00020\u00022\u00020\u0003:\u0001?B\u0005\u00a2\u0006\u0002\u0010\u0004J\b\u0010\u0015\u001a\u00020\u0016H\u0002J\b\u0010\u0017\u001a\u00020\u0016H\u0002J\u0012\u0010\u0018\u001a\u00020\u00012\b\u0010\u0019\u001a\u0004\u0018\u00010\u001aH\u0002J\b\u0010\u001b\u001a\u00020\u0016H\u0002J\b\u0010\u001c\u001a\u00020\u0016H\u0016J\b\u0010\u001d\u001a\u00020\u0016H\u0002J\u0012\u0010\u001e\u001a\u00020\u00162\b\u0010\u001f\u001a\u0004\u0018\u00010 H\u0016J\u0018\u0010!\u001a\u00020\u00162\u0006\u0010\"\u001a\u00020#2\u0006\u0010$\u001a\u00020%H\u0016J&\u0010&\u001a\u0004\u0018\u00010\'2\u0006\u0010$\u001a\u00020(2\b\u0010)\u001a\u0004\u0018\u00010*2\b\u0010\u001f\u001a\u0004\u0018\u00010 H\u0016J\b\u0010+\u001a\u00020\u0016H\u0016J\u001a\u0010,\u001a\u00020\u00162\b\u0010-\u001a\u0004\u0018\u00010.2\u0006\u0010/\u001a\u000200H\u0016J\u0010\u00101\u001a\u00020\r2\u0006\u00102\u001a\u00020\u0013H\u0016J\b\u00103\u001a\u00020\u0016H\u0016J\b\u00104\u001a\u00020\u0016H\u0016J\b\u00105\u001a\u00020\u0016H\u0016J\u001a\u00106\u001a\u00020\u00162\u0006\u00107\u001a\u00020\'2\b\u0010\u001f\u001a\u0004\u0018\u00010 H\u0016J\b\u00108\u001a\u00020\u0016H\u0002J\b\u00109\u001a\u00020\u0016H\u0002J\b\u0010:\u001a\u00020\u0016H\u0016J\u0012\u0010;\u001a\u00020\u00162\b\u0010\u0019\u001a\u0004\u0018\u00010\u001aH\u0002J\b\u0010<\u001a\u00020\u0016H\u0002J\b\u0010=\u001a\u00020\u0016H\u0002J\b\u0010>\u001a\u00020\u0016H\u0002R\u0016\u0010\u0005\u001a\n \u0007*\u0004\u0018\u00010\u00060\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\rX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\rX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\rX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u0011X\u0082.\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0012\u001a\u0004\u0018\u00010\u0013X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0014\u001a\u00020\tX\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006@"}, d2 = {"Lcom/ekoapp/ekosdk/uikit/community/detailpage/EkoCommunityPageFragment;", "Landroidx/fragment/app/Fragment;", "Lcom/ekoapp/ekosdk/uikit/components/EkoToolBarClickListener;", "Lcom/google/android/material/appbar/AppBarLayout$OnOffsetChangedListener;", "()V", "TAG", "", "kotlin.jvm.PlatformType", "disposable", "Lio/reactivex/disposables/CompositeDisposable;", "fragmentStateAdapter", "Lcom/ekoapp/ekosdk/uikit/base/EkoFragmentStateAdapter;", "isCreateCommunity", "", "isFirstLoad", "isRefreshing", "mViewModel", "Lcom/ekoapp/ekosdk/uikit/community/detailpage/EkoCommunityDetailViewModel;", "menuItem", "Landroid/view/MenuItem;", "refreshDisposable", "assignModeratorRole", "", "getCommunityDetail", "getFeedFragment", "community", "Lcom/ekoapp/ekosdk/community/EkoCommunity;", "joinCommunity", "leftIconClick", "navigateToMembersPage", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "onCreateOptionsMenu", "menu", "Landroid/view/Menu;", "inflater", "Landroid/view/MenuInflater;", "onCreateView", "Landroid/view/View;", "Landroid/view/LayoutInflater;", "container", "Landroid/view/ViewGroup;", "onDestroy", "onOffsetChanged", "appBarLayout", "Lcom/google/android/material/appbar/AppBarLayout;", "verticalOffset", "", "onOptionsItemSelected", "item", "onPause", "onResume", "onStart", "onViewCreated", "view", "refreshCommunity", "refreshDetails", "rightIconClick", "setUpTabLayout", "setUpToolbar", "showCommunitySuccessMessage", "subscribeObservers", "Builder", "community_debug"})
public final class EkoCommunityPageFragment extends androidx.fragment.app.Fragment implements com.ekoapp.ekosdk.uikit.components.EkoToolBarClickListener, com.google.android.material.appbar.AppBarLayout.OnOffsetChangedListener {
    private boolean isCreateCommunity = false;
    private final java.lang.String TAG = null;
    private com.ekoapp.ekosdk.uikit.community.detailpage.EkoCommunityDetailViewModel mViewModel;
    private com.ekoapp.ekosdk.uikit.base.EkoFragmentStateAdapter fragmentStateAdapter;
    private io.reactivex.disposables.CompositeDisposable refreshDisposable;
    private io.reactivex.disposables.CompositeDisposable disposable;
    private android.view.MenuItem menuItem;
    private boolean isFirstLoad = true;
    private boolean isRefreshing = false;
    private java.util.HashMap _$_findViewCache;
    
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
    
    private final void navigateToMembersPage() {
    }
    
    @java.lang.Override()
    public void onOffsetChanged(@org.jetbrains.annotations.Nullable()
    com.google.android.material.appbar.AppBarLayout appBarLayout, int verticalOffset) {
    }
    
    @java.lang.Override()
    public void onPause() {
    }
    
    @java.lang.Override()
    public void onResume() {
    }
    
    @java.lang.Override()
    public void onStart() {
    }
    
    private final void refreshCommunity() {
    }
    
    private final void refreshDetails() {
    }
    
    private final void setUpToolbar() {
    }
    
    private final void getCommunityDetail() {
    }
    
    private final void showCommunitySuccessMessage() {
    }
    
    private final void assignModeratorRole() {
    }
    
    private final void setUpTabLayout(com.ekoapp.ekosdk.community.EkoCommunity community) {
    }
    
    private final androidx.fragment.app.Fragment getFeedFragment(com.ekoapp.ekosdk.community.EkoCommunity community) {
        return null;
    }
    
    @java.lang.Override()
    public void onCreateOptionsMenu(@org.jetbrains.annotations.NotNull()
    android.view.Menu menu, @org.jetbrains.annotations.NotNull()
    android.view.MenuInflater inflater) {
    }
    
    @java.lang.Override()
    public boolean onOptionsItemSelected(@org.jetbrains.annotations.NotNull()
    android.view.MenuItem item) {
        return false;
    }
    
    private final void subscribeObservers() {
    }
    
    private final void joinCommunity() {
    }
    
    @java.lang.Override()
    public void leftIconClick() {
    }
    
    @java.lang.Override()
    public void rightIconClick() {
    }
    
    @java.lang.Override()
    public void onDestroy() {
    }
    
    public EkoCommunityPageFragment() {
        super();
    }
    
    @kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u000b\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u0011J\u000e\u0010\u0012\u001a\u00020\u00002\u0006\u0010\u0013\u001a\u00020\u0004J\u000e\u0010\t\u001a\u00020\u00002\u0006\u0010\u0014\u001a\u00020\nJ\u000e\u0010\u0015\u001a\u00020\u00002\u0006\u0010\u0016\u001a\u00020\bJ\u000e\u0010\u0017\u001a\u00020\u00002\u0006\u0010\u0018\u001a\u00020\rJ\u000e\u0010\u0019\u001a\u00020\u00002\u0006\u0010\u001a\u001a\u00020\u0006J\u000e\u0010\u001b\u001a\u00020\u00002\u0006\u0010\u0013\u001a\u00020\u0004R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0007\u001a\u0004\u0018\u00010\bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\t\u001a\u0004\u0018\u00010\nX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\f\u001a\u0004\u0018\u00010\rX\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001c"}, d2 = {"Lcom/ekoapp/ekosdk/uikit/community/detailpage/EkoCommunityPageFragment$Builder;", "", "()V", "communityCreated", "", "communityId", "", "editCommunityProfileClickListener", "Lcom/ekoapp/ekosdk/uikit/community/detailpage/listener/IEditCommunityProfileClickListener;", "feedFragmentDelegate", "Lcom/ekoapp/ekosdk/uikit/community/profile/listener/IFeedFragmentDelegate;", "isMessageVisible", "messageClickListener", "Lcom/ekoapp/ekosdk/uikit/community/detailpage/listener/IMessageClickListener;", "build", "Lcom/ekoapp/ekosdk/uikit/community/detailpage/EkoCommunityPageFragment;", "activity", "Landroidx/appcompat/app/AppCompatActivity;", "createCommunitySuccess", "value", "delegate", "onClickEditCommunityProfile", "onEditCommunityProfileClickListener", "onClickMessage", "onMessageClickListener", "setCommunityId", "id", "showMessageButton", "community_debug"})
    public static final class Builder {
        private java.lang.String communityId = "";
        private boolean communityCreated = false;
        private boolean isMessageVisible = false;
        private com.ekoapp.ekosdk.uikit.community.profile.listener.IFeedFragmentDelegate feedFragmentDelegate;
        private com.ekoapp.ekosdk.uikit.community.detailpage.listener.IMessageClickListener messageClickListener;
        private com.ekoapp.ekosdk.uikit.community.detailpage.listener.IEditCommunityProfileClickListener editCommunityProfileClickListener;
        
        @org.jetbrains.annotations.NotNull()
        public final com.ekoapp.ekosdk.uikit.community.detailpage.EkoCommunityPageFragment build(@org.jetbrains.annotations.NotNull()
        androidx.appcompat.app.AppCompatActivity activity) {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.ekoapp.ekosdk.uikit.community.detailpage.EkoCommunityPageFragment.Builder setCommunityId(@org.jetbrains.annotations.NotNull()
        java.lang.String id) {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.ekoapp.ekosdk.uikit.community.detailpage.EkoCommunityPageFragment.Builder createCommunitySuccess(boolean value) {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.ekoapp.ekosdk.uikit.community.detailpage.EkoCommunityPageFragment.Builder showMessageButton(boolean value) {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.ekoapp.ekosdk.uikit.community.detailpage.EkoCommunityPageFragment.Builder feedFragmentDelegate(@org.jetbrains.annotations.NotNull()
        com.ekoapp.ekosdk.uikit.community.profile.listener.IFeedFragmentDelegate delegate) {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.ekoapp.ekosdk.uikit.community.detailpage.EkoCommunityPageFragment.Builder onClickMessage(@org.jetbrains.annotations.NotNull()
        com.ekoapp.ekosdk.uikit.community.detailpage.listener.IMessageClickListener onMessageClickListener) {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.ekoapp.ekosdk.uikit.community.detailpage.EkoCommunityPageFragment.Builder onClickEditCommunityProfile(@org.jetbrains.annotations.NotNull()
        com.ekoapp.ekosdk.uikit.community.detailpage.listener.IEditCommunityProfileClickListener onEditCommunityProfileClickListener) {
            return null;
        }
        
        public Builder() {
            super();
        }
    }
}