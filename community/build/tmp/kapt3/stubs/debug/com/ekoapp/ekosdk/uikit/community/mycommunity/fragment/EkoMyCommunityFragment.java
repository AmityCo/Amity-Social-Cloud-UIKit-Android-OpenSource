package com.ekoapp.ekosdk.uikit.community.mycommunity.fragment;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000n\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0007\u0018\u00002\u00020\u00012\u00020\u0002:\u0001.B\u0007\b\u0000\u00a2\u0006\u0002\u0010\u0003J\b\u0010\u0011\u001a\u00020\u0012H\u0002J\b\u0010\u0013\u001a\u00020\u0012H\u0002J\u0012\u0010\u0014\u001a\u00020\u00122\b\u0010\u0015\u001a\u0004\u0018\u00010\u0016H\u0002J\u0012\u0010\u0017\u001a\u00020\u00122\b\u0010\u0015\u001a\u0004\u0018\u00010\u0016H\u0016J\u0012\u0010\u0018\u001a\u00020\u00122\b\u0010\u0019\u001a\u0004\u0018\u00010\u001aH\u0016J\u0018\u0010\u001b\u001a\u00020\u00122\u0006\u0010\u001c\u001a\u00020\u001d2\u0006\u0010\u001e\u001a\u00020\u001fH\u0016J&\u0010 \u001a\u0004\u0018\u00010!2\u0006\u0010\u001e\u001a\u00020\"2\b\u0010#\u001a\u0004\u0018\u00010$2\b\u0010\u0019\u001a\u0004\u0018\u00010\u001aH\u0016J\u0010\u0010%\u001a\u00020&2\u0006\u0010\'\u001a\u00020(H\u0016J\u001a\u0010)\u001a\u00020\u00122\u0006\u0010*\u001a\u00020!2\b\u0010\u0019\u001a\u0004\u0018\u00010\u001aH\u0016J\b\u0010+\u001a\u00020\u0012H\u0002J\b\u0010,\u001a\u00020\u0012H\u0002J\b\u0010-\u001a\u00020\u0012H\u0002R\u0016\u0010\u0004\u001a\n \u0006*\u0004\u0018\u00010\u00050\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082.\u00a2\u0006\u0002\n\u0000R\u001a\u0010\t\u001a\u00020\nX\u0086.\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u000b\u0010\f\"\u0004\b\r\u0010\u000eR\u000e\u0010\u000f\u001a\u00020\u0010X\u0082.\u00a2\u0006\u0002\n\u0000\u00a8\u0006/"}, d2 = {"Lcom/ekoapp/ekosdk/uikit/community/mycommunity/fragment/EkoMyCommunityFragment;", "Lcom/ekoapp/ekosdk/uikit/base/EkoBaseFragment;", "Lcom/ekoapp/ekosdk/uikit/community/mycommunity/listener/IMyCommunityItemClickListener;", "()V", "TAG", "", "kotlin.jvm.PlatformType", "mAdapter", "Lcom/ekoapp/ekosdk/uikit/community/mycommunity/adapter/EkoMyCommunityListAdapter;", "mBinding", "Lcom/ekoapp/ekosdk/uikit/community/databinding/AmityFragmentMyCommunityBinding;", "getMBinding", "()Lcom/ekoapp/ekosdk/uikit/community/databinding/AmityFragmentMyCommunityBinding;", "setMBinding", "(Lcom/ekoapp/ekosdk/uikit/community/databinding/AmityFragmentMyCommunityBinding;)V", "mViewModel", "Lcom/ekoapp/ekosdk/uikit/community/mycommunity/viewmodel/EkoMyCommunityListViewModel;", "handleEditTextInput", "", "initRecyclerView", "navigateToCommunityDetails", "ekoCommunity", "Lcom/ekoapp/ekosdk/community/EkoCommunity;", "onCommunitySelected", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "onCreateOptionsMenu", "menu", "Landroid/view/Menu;", "inflater", "Landroid/view/MenuInflater;", "onCreateView", "Landroid/view/View;", "Landroid/view/LayoutInflater;", "container", "Landroid/view/ViewGroup;", "onOptionsItemSelected", "", "item", "Landroid/view/MenuItem;", "onViewCreated", "view", "searchCommunity", "setUpToolBar", "subscribeObservers", "Builder", "community_debug"})
public final class EkoMyCommunityFragment extends com.ekoapp.ekosdk.uikit.base.EkoBaseFragment implements com.ekoapp.ekosdk.uikit.community.mycommunity.listener.IMyCommunityItemClickListener {
    private final java.lang.String TAG = null;
    private com.ekoapp.ekosdk.uikit.community.mycommunity.viewmodel.EkoMyCommunityListViewModel mViewModel;
    @org.jetbrains.annotations.NotNull()
    public com.ekoapp.ekosdk.uikit.community.databinding.AmityFragmentMyCommunityBinding mBinding;
    private com.ekoapp.ekosdk.uikit.community.mycommunity.adapter.EkoMyCommunityListAdapter mAdapter;
    private java.util.HashMap _$_findViewCache;
    
    @org.jetbrains.annotations.NotNull()
    public final com.ekoapp.ekosdk.uikit.community.databinding.AmityFragmentMyCommunityBinding getMBinding() {
        return null;
    }
    
    public final void setMBinding(@org.jetbrains.annotations.NotNull()
    com.ekoapp.ekosdk.uikit.community.databinding.AmityFragmentMyCommunityBinding p0) {
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
    
    private final void handleEditTextInput() {
    }
    
    private final void setUpToolBar() {
    }
    
    private final void subscribeObservers() {
    }
    
    private final void searchCommunity() {
    }
    
    private final void initRecyclerView() {
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
    
    @java.lang.Override()
    public void onCommunitySelected(@org.jetbrains.annotations.Nullable()
    com.ekoapp.ekosdk.community.EkoCommunity ekoCommunity) {
    }
    
    private final void navigateToCommunityDetails(com.ekoapp.ekosdk.community.EkoCommunity ekoCommunity) {
    }
    
    public EkoMyCommunityFragment() {
        super();
    }
    
    @kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u000bJ\u0010\u0010\u0003\u001a\u00020\u00002\u0006\u0010\f\u001a\u00020\u0004H\u0002J\u000e\u0010\u0005\u001a\u00020\u00002\u0006\u0010\r\u001a\u00020\u0006J\u000e\u0010\u0007\u001a\u00020\u00002\u0006\u0010\r\u001a\u00020\u0006R\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u000e"}, d2 = {"Lcom/ekoapp/ekosdk/uikit/community/mycommunity/fragment/EkoMyCommunityFragment$Builder;", "", "()V", "myCommunityItemClickListener", "Lcom/ekoapp/ekosdk/uikit/community/mycommunity/listener/IMyCommunityItemClickListener;", "showOptionsMenu", "", "showSearch", "build", "Lcom/ekoapp/ekosdk/uikit/community/mycommunity/fragment/EkoMyCommunityFragment;", "activity", "Landroidx/appcompat/app/AppCompatActivity;", "listener", "value", "community_debug"})
    public static final class Builder {
        private com.ekoapp.ekosdk.uikit.community.mycommunity.listener.IMyCommunityItemClickListener myCommunityItemClickListener;
        private boolean showSearch = true;
        private boolean showOptionsMenu = true;
        
        @org.jetbrains.annotations.NotNull()
        public final com.ekoapp.ekosdk.uikit.community.mycommunity.fragment.EkoMyCommunityFragment build(@org.jetbrains.annotations.NotNull()
        androidx.appcompat.app.AppCompatActivity activity) {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.ekoapp.ekosdk.uikit.community.mycommunity.fragment.EkoMyCommunityFragment.Builder showSearch(boolean value) {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.ekoapp.ekosdk.uikit.community.mycommunity.fragment.EkoMyCommunityFragment.Builder showOptionsMenu(boolean value) {
            return null;
        }
        
        private final com.ekoapp.ekosdk.uikit.community.mycommunity.fragment.EkoMyCommunityFragment.Builder myCommunityItemClickListener(com.ekoapp.ekosdk.uikit.community.mycommunity.listener.IMyCommunityItemClickListener listener) {
            return null;
        }
        
        public Builder() {
            super();
        }
    }
}