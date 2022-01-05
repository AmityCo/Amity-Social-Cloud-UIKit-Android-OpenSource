package com.ekoapp.ekosdk.uikit.community.members;

import java.lang.System;

/**
 * A simple [Fragment] subclass.
 * Use the [EkoModeratorsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000V\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0005\u0018\u0000 $2\u00020\u00012\u00020\u0002:\u0001$B\u0005\u00a2\u0006\u0002\u0010\u0003J\b\u0010\u000f\u001a\u00020\u0010H\u0002J\u0010\u0010\u0011\u001a\u00020\u00102\u0006\u0010\u0012\u001a\u00020\u0013H\u0016J&\u0010\u0014\u001a\u0004\u0018\u00010\u00152\u0006\u0010\u0016\u001a\u00020\u00172\b\u0010\u0018\u001a\u0004\u0018\u00010\u00192\b\u0010\u001a\u001a\u0004\u0018\u00010\u001bH\u0016J\u001a\u0010\u001c\u001a\u00020\u00102\u0006\u0010\u001d\u001a\u00020\u00152\b\u0010\u001a\u001a\u0004\u0018\u00010\u001bH\u0016J\u0016\u0010\u001e\u001a\u00020\u00102\f\u0010\u001f\u001a\b\u0012\u0004\u0012\u00020\u00130 H\u0002J\b\u0010!\u001a\u00020\u0010H\u0002J\b\u0010\"\u001a\u00020\u0010H\u0002J\b\u0010#\u001a\u00020\u0010H\u0002R\u0016\u0010\u0004\u001a\n \u0006*\u0004\u0018\u00010\u00050\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001b\u0010\u0007\u001a\u00020\b8BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\u000b\u0010\f\u001a\u0004\b\t\u0010\nR\u000e\u0010\r\u001a\u00020\u000eX\u0082.\u00a2\u0006\u0002\n\u0000\u00a8\u0006%"}, d2 = {"Lcom/ekoapp/ekosdk/uikit/community/members/EkoModeratorsFragment;", "Lcom/ekoapp/ekosdk/uikit/base/EkoBaseFragment;", "Lcom/ekoapp/ekosdk/uikit/community/members/IMemberClickListener;", "()V", "TAG", "", "kotlin.jvm.PlatformType", "mViewModel", "Lcom/ekoapp/ekosdk/uikit/community/members/EkoCommunityMembersViewModel;", "getMViewModel", "()Lcom/ekoapp/ekosdk/uikit/community/members/EkoCommunityMembersViewModel;", "mViewModel$delegate", "Lkotlin/Lazy;", "moderatorAdapter", "Lcom/ekoapp/ekosdk/uikit/community/members/EkoCommunityModeratorAdapter;", "initRecyclerView", "", "onCommunityMembershipSelected", "membership", "Lcom/ekoapp/ekosdk/community/membership/EkoCommunityMembership;", "onCreateView", "Landroid/view/View;", "inflater", "Landroid/view/LayoutInflater;", "container", "Landroid/view/ViewGroup;", "savedInstanceState", "Landroid/os/Bundle;", "onViewCreated", "view", "prepareSelectedMembersList", "list", "Landroidx/paging/PagedList;", "refresh", "searchMembers", "subscribeObservers", "Companion", "community_debug"})
public final class EkoModeratorsFragment extends com.ekoapp.ekosdk.uikit.base.EkoBaseFragment implements com.ekoapp.ekosdk.uikit.community.members.IMemberClickListener {
    private final kotlin.Lazy mViewModel$delegate = null;
    private final java.lang.String TAG = null;
    private com.ekoapp.ekosdk.uikit.community.members.EkoCommunityModeratorAdapter moderatorAdapter;
    public static final com.ekoapp.ekosdk.uikit.community.members.EkoModeratorsFragment.Companion Companion = null;
    private java.util.HashMap _$_findViewCache;
    
    private final com.ekoapp.ekosdk.uikit.community.members.EkoCommunityMembersViewModel getMViewModel() {
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
    
    private final void subscribeObservers() {
    }
    
    private final void initRecyclerView() {
    }
    
    private final void prepareSelectedMembersList(androidx.paging.PagedList<com.ekoapp.ekosdk.community.membership.EkoCommunityMembership> list) {
    }
    
    private final void searchMembers() {
    }
    
    private final void refresh() {
    }
    
    @java.lang.Override()
    public void onCommunityMembershipSelected(@org.jetbrains.annotations.NotNull()
    com.ekoapp.ekosdk.community.membership.EkoCommunityMembership membership) {
    }
    
    public EkoModeratorsFragment() {
        super();
    }
    
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment EkoModeratorsFragment.
     */
    @org.jetbrains.annotations.NotNull()
    public static final com.ekoapp.ekosdk.uikit.community.members.EkoModeratorsFragment newInstance() {
        return null;
    }
    
    @kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0003\u001a\u00020\u0004H\u0007\u00a8\u0006\u0005"}, d2 = {"Lcom/ekoapp/ekosdk/uikit/community/members/EkoModeratorsFragment$Companion;", "", "()V", "newInstance", "Lcom/ekoapp/ekosdk/uikit/community/members/EkoModeratorsFragment;", "community_debug"})
    public static final class Companion {
        
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         * @return A new instance of fragment EkoModeratorsFragment.
         */
        @org.jetbrains.annotations.NotNull()
        public final com.ekoapp.ekosdk.uikit.community.members.EkoModeratorsFragment newInstance() {
            return null;
        }
        
        private Companion() {
            super();
        }
    }
}