package com.ekoapp.ekosdk.uikit.community.newsfeed.fragment;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000^\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0002\b\u0006\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\u0018\u00002\u00020\u00012\u00020\u0002:\u0001\'B\u0007\b\u0000\u00a2\u0006\u0002\u0010\u0003J\b\u0010\u0014\u001a\u00020\u0015H\u0002J\b\u0010\u0016\u001a\u00020\u0015H\u0002J\b\u0010\u0017\u001a\u00020\u0015H\u0002J\u0012\u0010\u0018\u001a\u00020\u00152\b\u0010\u0019\u001a\u0004\u0018\u00010\tH\u0002J\u0018\u0010\u001a\u001a\u00020\u00152\u0006\u0010\u0019\u001a\u00020\t2\u0006\u0010\u001b\u001a\u00020\u001cH\u0016J&\u0010\u001d\u001a\u0004\u0018\u00010\u001e2\u0006\u0010\u001f\u001a\u00020 2\b\u0010!\u001a\u0004\u0018\u00010\"2\b\u0010#\u001a\u0004\u0018\u00010$H\u0016J\u001a\u0010%\u001a\u00020\u00152\u0006\u0010&\u001a\u00020\u001e2\b\u0010#\u001a\u0004\u0018\u00010$H\u0016R\u0016\u0010\u0004\u001a\n \u0006*\u0004\u0018\u00010\u00050\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0016\u0010\u0007\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\t0\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\rX\u0082.\u00a2\u0006\u0002\n\u0000R\u001b\u0010\u000e\u001a\u00020\u000f8BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\u0012\u0010\u0013\u001a\u0004\b\u0010\u0010\u0011\u00a8\u0006("}, d2 = {"Lcom/ekoapp/ekosdk/uikit/community/newsfeed/fragment/EkoPostTargetSelectionFragment;", "Lcom/ekoapp/ekosdk/uikit/base/EkoBaseFragment;", "Lcom/ekoapp/ekosdk/uikit/community/newsfeed/listener/ICreatePostCommunitySelectionListener;", "()V", "TAG", "", "kotlin.jvm.PlatformType", "createPost", "Landroidx/activity/result/ActivityResultLauncher;", "Lcom/ekoapp/ekosdk/community/EkoCommunity;", "mAdapter", "Lcom/ekoapp/ekosdk/uikit/community/newsfeed/adapter/EkoCreatePostCommunitySelectionAdapter;", "mBinding", "Lcom/ekoapp/ekosdk/uikit/community/databinding/AmityFragmentPostTargetSelectionBinding;", "mViewModel", "Lcom/ekoapp/ekosdk/uikit/community/newsfeed/viewmodel/EkoCreatePostRoleSelectionViewModel;", "getMViewModel", "()Lcom/ekoapp/ekosdk/uikit/community/newsfeed/viewmodel/EkoCreatePostRoleSelectionViewModel;", "mViewModel$delegate", "Lkotlin/Lazy;", "handleCommunitySectionVisibility", "", "initProfileImage", "initRecyclerView", "navigateToCreatePost", "community", "onClickCommunity", "position", "", "onCreateView", "Landroid/view/View;", "inflater", "Landroid/view/LayoutInflater;", "container", "Landroid/view/ViewGroup;", "savedInstanceState", "Landroid/os/Bundle;", "onViewCreated", "view", "Builder", "community_debug"})
public final class EkoPostTargetSelectionFragment extends com.ekoapp.ekosdk.uikit.base.EkoBaseFragment implements com.ekoapp.ekosdk.uikit.community.newsfeed.listener.ICreatePostCommunitySelectionListener {
    private final java.lang.String TAG = null;
    private final kotlin.Lazy mViewModel$delegate = null;
    private com.ekoapp.ekosdk.uikit.community.newsfeed.adapter.EkoCreatePostCommunitySelectionAdapter mAdapter;
    private com.ekoapp.ekosdk.uikit.community.databinding.AmityFragmentPostTargetSelectionBinding mBinding;
    private final androidx.activity.result.ActivityResultLauncher<com.ekoapp.ekosdk.community.EkoCommunity> createPost = null;
    private java.util.HashMap _$_findViewCache;
    
    private final com.ekoapp.ekosdk.uikit.community.newsfeed.viewmodel.EkoCreatePostRoleSelectionViewModel getMViewModel() {
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
    
    private final void initProfileImage() {
    }
    
    private final void initRecyclerView() {
    }
    
    private final void handleCommunitySectionVisibility() {
    }
    
    @java.lang.Override()
    public void onClickCommunity(@org.jetbrains.annotations.NotNull()
    com.ekoapp.ekosdk.community.EkoCommunity community, int position) {
    }
    
    private final void navigateToCreatePost(com.ekoapp.ekosdk.community.EkoCommunity community) {
    }
    
    public EkoPostTargetSelectionFragment() {
        super();
    }
    
    @kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006\u00a8\u0006\u0007"}, d2 = {"Lcom/ekoapp/ekosdk/uikit/community/newsfeed/fragment/EkoPostTargetSelectionFragment$Builder;", "", "()V", "build", "Lcom/ekoapp/ekosdk/uikit/community/newsfeed/fragment/EkoPostTargetSelectionFragment;", "activity", "Landroidx/appcompat/app/AppCompatActivity;", "community_debug"})
    public static final class Builder {
        
        @org.jetbrains.annotations.NotNull()
        public final com.ekoapp.ekosdk.uikit.community.newsfeed.fragment.EkoPostTargetSelectionFragment build(@org.jetbrains.annotations.NotNull()
        androidx.appcompat.app.AppCompatActivity activity) {
            return null;
        }
        
        public Builder() {
            super();
        }
    }
}