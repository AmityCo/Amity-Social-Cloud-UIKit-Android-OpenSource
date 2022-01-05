package com.ekoapp.ekosdk.uikit.community.ui.view;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\u008c\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010\u000e\n\u0002\b\u0002\u0018\u00002\u00020\u00012\u00020\u00022\u00020\u0003:\u0001=B\u0007\b\u0000\u00a2\u0006\u0002\u0010\u0004J\u000e\u0010\u001b\u001a\u00020\u001c2\u0006\u0010\u001d\u001a\u00020\u001eJ\b\u0010\u001f\u001a\u00020\u001cH\u0002J\b\u0010 \u001a\u00020\u001cH\u0002J&\u0010!\u001a\u0004\u0018\u00010\"2\u0006\u0010#\u001a\u00020$2\b\u0010%\u001a\u0004\u0018\u00010&2\b\u0010\'\u001a\u0004\u0018\u00010(H\u0016J\b\u0010)\u001a\u00020\u001cH\u0016J\u0018\u0010*\u001a\u00020\u001c2\u0006\u0010+\u001a\u00020,2\u0006\u0010-\u001a\u00020.H\u0016J\u0010\u0010/\u001a\u00020\u001c2\u0006\u0010+\u001a\u00020\u001aH\u0016J\u001a\u00100\u001a\u00020\u001c2\u0006\u00101\u001a\u00020\"2\b\u0010\'\u001a\u0004\u0018\u00010(H\u0016J\u0016\u00102\u001a\u00020\u001c2\f\u00103\u001a\b\u0012\u0004\u0012\u00020,04H\u0002J\b\u00105\u001a\u00020\u001cH\u0002J\b\u00106\u001a\u00020\u001cH\u0002J\b\u00107\u001a\u00020\u001cH\u0002J\b\u00108\u001a\u00020\u001cH\u0002J\b\u00109\u001a\u00020\u001cH\u0002J\u0010\u0010:\u001a\u00020\u001c2\u0006\u0010;\u001a\u00020<H\u0002R\u001b\u0010\u0005\u001a\u00020\u00068BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\t\u0010\n\u001a\u0004\b\u0007\u0010\bR\u000e\u0010\u000b\u001a\u00020\fX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u000eX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0010X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u0012X\u0082.\u00a2\u0006\u0002\n\u0000R\u001b\u0010\u0013\u001a\u00020\u00148BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\u0017\u0010\n\u001a\u0004\b\u0015\u0010\u0016R\u0014\u0010\u0018\u001a\b\u0012\u0004\u0012\u00020\u001a0\u0019X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006>"}, d2 = {"Lcom/ekoapp/ekosdk/uikit/community/ui/view/EkoSelectMembersListFragment;", "Landroidx/fragment/app/Fragment;", "Lcom/ekoapp/ekosdk/uikit/community/ui/clickListener/EkoSelectMemberListener;", "Lcom/ekoapp/ekosdk/uikit/community/ui/clickListener/EkoSelectedMemberListener;", "()V", "disposable", "Lio/reactivex/disposables/CompositeDisposable;", "getDisposable", "()Lio/reactivex/disposables/CompositeDisposable;", "disposable$delegate", "Lkotlin/Lazy;", "mBinding", "Lcom/ekoapp/ekosdk/uikit/community/databinding/AmityFragmentSelectMembersListBinding;", "mMemberListAdapter", "Lcom/ekoapp/ekosdk/uikit/community/ui/adapter/EkoMembersListAdapter;", "mSearchResultAdapter", "Lcom/ekoapp/ekosdk/uikit/community/ui/adapter/EkoSearchResultAdapter;", "mSelectedMembersAdapter", "Lcom/ekoapp/ekosdk/uikit/community/ui/adapter/EkoSelectedMemberAdapter;", "mViewModel", "Lcom/ekoapp/ekosdk/uikit/community/ui/viewModel/EkoSelectMembersViewModel;", "getMViewModel", "()Lcom/ekoapp/ekosdk/uikit/community/ui/viewModel/EkoSelectMembersViewModel;", "mViewModel$delegate", "receivedMembersList", "Ljava/util/ArrayList;", "Lcom/ekoapp/ekosdk/uikit/community/data/SelectMemberItem;", "finishActivity", "", "isCancel", "", "handleSelectedMembers", "loadFilteredList", "onCreateView", "Landroid/view/View;", "inflater", "Landroid/view/LayoutInflater;", "container", "Landroid/view/ViewGroup;", "savedInstanceState", "Landroid/os/Bundle;", "onDestroy", "onMemberClicked", "member", "Lcom/ekoapp/ekosdk/user/EkoUser;", "position", "", "onMemberRemoved", "onViewCreated", "view", "prepareSearchMap", "list", "Landroidx/paging/PagedList;", "setToolBarState", "setUpMembersListRecyclerView", "setUpSearchRecyclerView", "setUpSelectedMemberRecyclerView", "subscribeObservers", "updateListOnSelection", "id", "", "Builder", "community_debug"})
public final class EkoSelectMembersListFragment extends androidx.fragment.app.Fragment implements com.ekoapp.ekosdk.uikit.community.ui.clickListener.EkoSelectMemberListener, com.ekoapp.ekosdk.uikit.community.ui.clickListener.EkoSelectedMemberListener {
    private final kotlin.Lazy mViewModel$delegate = null;
    private com.ekoapp.ekosdk.uikit.community.ui.adapter.EkoSelectedMemberAdapter mSelectedMembersAdapter;
    private com.ekoapp.ekosdk.uikit.community.ui.adapter.EkoMembersListAdapter mMemberListAdapter;
    private com.ekoapp.ekosdk.uikit.community.ui.adapter.EkoSearchResultAdapter mSearchResultAdapter;
    private final java.util.ArrayList<com.ekoapp.ekosdk.uikit.community.data.SelectMemberItem> receivedMembersList = null;
    private final kotlin.Lazy disposable$delegate = null;
    private com.ekoapp.ekosdk.uikit.community.databinding.AmityFragmentSelectMembersListBinding mBinding;
    private java.util.HashMap _$_findViewCache;
    
    private final com.ekoapp.ekosdk.uikit.community.ui.viewModel.EkoSelectMembersViewModel getMViewModel() {
        return null;
    }
    
    private final io.reactivex.disposables.CompositeDisposable getDisposable() {
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
    
    private final void handleSelectedMembers() {
    }
    
    private final void setToolBarState() {
    }
    
    private final void subscribeObservers() {
    }
    
    private final void setUpSelectedMemberRecyclerView() {
    }
    
    private final void setUpMembersListRecyclerView() {
    }
    
    private final void setUpSearchRecyclerView() {
    }
    
    @java.lang.Override()
    public void onMemberClicked(@org.jetbrains.annotations.NotNull()
    com.ekoapp.ekosdk.user.EkoUser member, int position) {
    }
    
    @java.lang.Override()
    public void onMemberRemoved(@org.jetbrains.annotations.NotNull()
    com.ekoapp.ekosdk.uikit.community.data.SelectMemberItem member) {
    }
    
    private final void updateListOnSelection(java.lang.String id) {
    }
    
    private final void loadFilteredList() {
    }
    
    private final void prepareSearchMap(androidx.paging.PagedList<com.ekoapp.ekosdk.user.EkoUser> list) {
    }
    
    public final void finishActivity(boolean isCancel) {
    }
    
    @java.lang.Override()
    public void onDestroy() {
    }
    
    public EkoSelectMembersListFragment() {
        super();
    }
    
    @kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\nJ\"\u0010\u000b\u001a\u00020\u00002\u001a\u0010\f\u001a\u0016\u0012\u0004\u0012\u00020\u0005\u0018\u00010\u0004j\n\u0012\u0004\u0012\u00020\u0005\u0018\u0001`\u0006R\"\u0010\u0003\u001a\u0016\u0012\u0004\u0012\u00020\u0005\u0018\u00010\u0004j\n\u0012\u0004\u0012\u00020\u0005\u0018\u0001`\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\r"}, d2 = {"Lcom/ekoapp/ekosdk/uikit/community/ui/view/EkoSelectMembersListFragment$Builder;", "", "()V", "selectedMembersList", "Ljava/util/ArrayList;", "Lcom/ekoapp/ekosdk/uikit/community/data/SelectMemberItem;", "Lkotlin/collections/ArrayList;", "build", "Lcom/ekoapp/ekosdk/uikit/community/ui/view/EkoSelectMembersListFragment;", "activity", "Landroidx/appcompat/app/AppCompatActivity;", "selectedMembers", "list", "community_debug"})
    public static final class Builder {
        private java.util.ArrayList<com.ekoapp.ekosdk.uikit.community.data.SelectMemberItem> selectedMembersList;
        
        @org.jetbrains.annotations.NotNull()
        public final com.ekoapp.ekosdk.uikit.community.ui.view.EkoSelectMembersListFragment build(@org.jetbrains.annotations.NotNull()
        androidx.appcompat.app.AppCompatActivity activity) {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.ekoapp.ekosdk.uikit.community.ui.view.EkoSelectMembersListFragment.Builder selectedMembers(@org.jetbrains.annotations.Nullable()
        java.util.ArrayList<com.ekoapp.ekosdk.uikit.community.data.SelectMemberItem> list) {
            return null;
        }
        
        public Builder() {
            super();
        }
    }
}