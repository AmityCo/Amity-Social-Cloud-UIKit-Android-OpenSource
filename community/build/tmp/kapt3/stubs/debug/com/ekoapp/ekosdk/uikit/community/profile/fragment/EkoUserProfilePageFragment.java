package com.ekoapp.ekosdk.uikit.community.profile.fragment;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000h\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\b\u0018\u00002\u00020\u00012\u00020\u0002:\u0001,B\u0007\b\u0000\u00a2\u0006\u0002\u0010\u0003J\b\u0010\u0013\u001a\u00020\u0014H\u0002J\b\u0010\u0015\u001a\u00020\u0016H\u0002J\b\u0010\u0017\u001a\u00020\u0016H\u0002J\u0012\u0010\u0018\u001a\u00020\u00162\b\u0010\u0019\u001a\u0004\u0018\u00010\u001aH\u0016J&\u0010\u001b\u001a\u0004\u0018\u00010\u001c2\u0006\u0010\u001d\u001a\u00020\u001e2\b\u0010\u001f\u001a\u0004\u0018\u00010 2\b\u0010\u0019\u001a\u0004\u0018\u00010\u001aH\u0016J\u001a\u0010!\u001a\u00020\u00162\b\u0010\"\u001a\u0004\u0018\u00010#2\u0006\u0010$\u001a\u00020%H\u0016J\b\u0010&\u001a\u00020\u0016H\u0016J\b\u0010\'\u001a\u00020\u0016H\u0016J\b\u0010(\u001a\u00020\u0016H\u0016J\u001a\u0010)\u001a\u00020\u00162\u0006\u0010*\u001a\u00020\u001c2\b\u0010\u0019\u001a\u0004\u0018\u00010\u001aH\u0016J\b\u0010+\u001a\u00020\u0016H\u0002R\u0016\u0010\u0004\u001a\n \u0006*\u0004\u0018\u00010\u00050\u0005X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u0082.\u00a2\u0006\u0002\n\u0000R\u001a\u0010\r\u001a\u00020\u000eX\u0086.\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u000f\u0010\u0010\"\u0004\b\u0011\u0010\u0012\u00a8\u0006-"}, d2 = {"Lcom/ekoapp/ekosdk/uikit/community/profile/fragment/EkoUserProfilePageFragment;", "Lcom/ekoapp/ekosdk/uikit/base/EkoBaseFragment;", "Lcom/google/android/material/appbar/AppBarLayout$OnOffsetChangedListener;", "()V", "TAG", "", "kotlin.jvm.PlatformType", "fragmentStateAdapter", "Lcom/ekoapp/ekosdk/uikit/base/EkoFragmentStateAdapter;", "isRefreshing", "", "mBinding", "Lcom/ekoapp/ekosdk/uikit/community/databinding/AmityFragmentUserProfilePageBinding;", "mViewModel", "Lcom/ekoapp/ekosdk/uikit/community/profile/viewmodel/EkoUserProfileViewModel;", "getMViewModel", "()Lcom/ekoapp/ekosdk/uikit/community/profile/viewmodel/EkoUserProfileViewModel;", "setMViewModel", "(Lcom/ekoapp/ekosdk/uikit/community/profile/viewmodel/EkoUserProfileViewModel;)V", "getTimeLineFragment", "Landroidx/fragment/app/Fragment;", "getUserDetails", "", "initTabLayout", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "onCreateView", "Landroid/view/View;", "inflater", "Landroid/view/LayoutInflater;", "container", "Landroid/view/ViewGroup;", "onOffsetChanged", "appBarLayout", "Lcom/google/android/material/appbar/AppBarLayout;", "verticalOffset", "", "onPause", "onResume", "onStart", "onViewCreated", "view", "refreshFeed", "Builder", "community_debug"})
public final class EkoUserProfilePageFragment extends com.ekoapp.ekosdk.uikit.base.EkoBaseFragment implements com.google.android.material.appbar.AppBarLayout.OnOffsetChangedListener {
    private java.lang.String TAG;
    @org.jetbrains.annotations.NotNull()
    public com.ekoapp.ekosdk.uikit.community.profile.viewmodel.EkoUserProfileViewModel mViewModel;
    private com.ekoapp.ekosdk.uikit.base.EkoFragmentStateAdapter fragmentStateAdapter;
    private com.ekoapp.ekosdk.uikit.community.databinding.AmityFragmentUserProfilePageBinding mBinding;
    private boolean isRefreshing = false;
    private java.util.HashMap _$_findViewCache;
    
    @org.jetbrains.annotations.NotNull()
    public final com.ekoapp.ekosdk.uikit.community.profile.viewmodel.EkoUserProfileViewModel getMViewModel() {
        return null;
    }
    
    public final void setMViewModel(@org.jetbrains.annotations.NotNull()
    com.ekoapp.ekosdk.uikit.community.profile.viewmodel.EkoUserProfileViewModel p0) {
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
    public void onStart() {
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
    
    private final void refreshFeed() {
    }
    
    private final void getUserDetails() {
    }
    
    private final void initTabLayout() {
    }
    
    private final androidx.fragment.app.Fragment getTimeLineFragment() {
        return null;
    }
    
    public EkoUserProfilePageFragment() {
        super();
    }
    
    @kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u00006\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u0014J\u000e\u0010\t\u001a\u00020\u00002\u0006\u0010\u0015\u001a\u00020\nJ\u000e\u0010\u0016\u001a\u00020\u00002\u0006\u0010\u0017\u001a\u00020\u0004J\u000e\u0010\u0018\u001a\u00020\u00002\u0006\u0010\u0018\u001a\u00020\u0019J\u000e\u0010\u000f\u001a\u00020\u00002\u0006\u0010\u000f\u001a\u00020\u0010R\u001c\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\bR\u001c\u0010\t\u001a\u0004\u0018\u00010\nX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u000b\u0010\f\"\u0004\b\r\u0010\u000eR\u0010\u0010\u000f\u001a\u0004\u0018\u00010\u0010X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001a"}, d2 = {"Lcom/ekoapp/ekosdk/uikit/community/profile/fragment/EkoUserProfilePageFragment$Builder;", "", "()V", "editUserProfileClickListener", "Lcom/ekoapp/ekosdk/uikit/community/profile/listener/IEditUserProfileClickListener;", "getEditUserProfileClickListener", "()Lcom/ekoapp/ekosdk/uikit/community/profile/listener/IEditUserProfileClickListener;", "setEditUserProfileClickListener", "(Lcom/ekoapp/ekosdk/uikit/community/profile/listener/IEditUserProfileClickListener;)V", "feedFragmentDelegate", "Lcom/ekoapp/ekosdk/uikit/community/profile/listener/IFeedFragmentDelegate;", "getFeedFragmentDelegate", "()Lcom/ekoapp/ekosdk/uikit/community/profile/listener/IFeedFragmentDelegate;", "setFeedFragmentDelegate", "(Lcom/ekoapp/ekosdk/uikit/community/profile/listener/IFeedFragmentDelegate;)V", "userId", "", "build", "Lcom/ekoapp/ekosdk/uikit/community/profile/fragment/EkoUserProfilePageFragment;", "activity", "Landroidx/appcompat/app/AppCompatActivity;", "delegate", "onClickEditUserProfile", "onEditUserProfileClickListener", "user", "Lcom/ekoapp/ekosdk/user/EkoUser;", "community_debug"})
    public static final class Builder {
        private java.lang.String userId;
        @org.jetbrains.annotations.Nullable()
        private com.ekoapp.ekosdk.uikit.community.profile.listener.IFeedFragmentDelegate feedFragmentDelegate;
        @org.jetbrains.annotations.Nullable()
        private com.ekoapp.ekosdk.uikit.community.profile.listener.IEditUserProfileClickListener editUserProfileClickListener;
        
        @org.jetbrains.annotations.Nullable()
        public final com.ekoapp.ekosdk.uikit.community.profile.listener.IFeedFragmentDelegate getFeedFragmentDelegate() {
            return null;
        }
        
        public final void setFeedFragmentDelegate(@org.jetbrains.annotations.Nullable()
        com.ekoapp.ekosdk.uikit.community.profile.listener.IFeedFragmentDelegate p0) {
        }
        
        @org.jetbrains.annotations.Nullable()
        public final com.ekoapp.ekosdk.uikit.community.profile.listener.IEditUserProfileClickListener getEditUserProfileClickListener() {
            return null;
        }
        
        public final void setEditUserProfileClickListener(@org.jetbrains.annotations.Nullable()
        com.ekoapp.ekosdk.uikit.community.profile.listener.IEditUserProfileClickListener p0) {
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.ekoapp.ekosdk.uikit.community.profile.fragment.EkoUserProfilePageFragment build(@org.jetbrains.annotations.NotNull()
        androidx.appcompat.app.AppCompatActivity activity) {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.ekoapp.ekosdk.uikit.community.profile.fragment.EkoUserProfilePageFragment.Builder userId(@org.jetbrains.annotations.NotNull()
        java.lang.String userId) {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.ekoapp.ekosdk.uikit.community.profile.fragment.EkoUserProfilePageFragment.Builder user(@org.jetbrains.annotations.NotNull()
        com.ekoapp.ekosdk.user.EkoUser user) {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.ekoapp.ekosdk.uikit.community.profile.fragment.EkoUserProfilePageFragment.Builder feedFragmentDelegate(@org.jetbrains.annotations.NotNull()
        com.ekoapp.ekosdk.uikit.community.profile.listener.IFeedFragmentDelegate delegate) {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.ekoapp.ekosdk.uikit.community.profile.fragment.EkoUserProfilePageFragment.Builder onClickEditUserProfile(@org.jetbrains.annotations.NotNull()
        com.ekoapp.ekosdk.uikit.community.profile.listener.IEditUserProfileClickListener onEditUserProfileClickListener) {
            return null;
        }
        
        public Builder() {
            super();
        }
    }
}