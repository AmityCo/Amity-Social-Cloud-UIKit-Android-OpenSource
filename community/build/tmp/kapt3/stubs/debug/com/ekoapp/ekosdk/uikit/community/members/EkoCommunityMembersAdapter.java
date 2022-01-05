package com.ekoapp.ekosdk.uikit.community.members;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000H\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\u0018\u0000 \u00192\b\u0012\u0004\u0012\u00020\u00020\u0001:\u0002\u0019\u001aB+\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u0012\u0006\u0010\u0007\u001a\u00020\b\u0012\f\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u000b0\n\u00a2\u0006\u0002\u0010\fJ\u001a\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u00102\b\u0010\u0012\u001a\u0004\u0018\u00010\u0002H\u0014J\u0018\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u0017\u001a\u00020\u0010H\u0016J\u000e\u0010\u0018\u001a\u00020\u000b2\u0006\u0010\r\u001a\u00020\u000eR\u000e\u0010\u0007\u001a\u00020\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u000eX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u000b0\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001b"}, d2 = {"Lcom/ekoapp/ekosdk/uikit/community/members/EkoCommunityMembersAdapter;", "Lcom/ekoapp/ekosdk/uikit/base/EkoBaseRecyclerViewPagedAdapter;", "Lcom/ekoapp/ekosdk/community/membership/EkoCommunityMembership;", "context", "Landroid/content/Context;", "listener", "Lcom/ekoapp/ekosdk/uikit/community/members/IMemberClickListener;", "communityMemberViewModel", "Lcom/ekoapp/ekosdk/uikit/community/members/EkoCommunityMembersViewModel;", "onUserPromoted", "Lkotlin/Function0;", "", "(Landroid/content/Context;Lcom/ekoapp/ekosdk/uikit/community/members/IMemberClickListener;Lcom/ekoapp/ekosdk/uikit/community/members/EkoCommunityMembersViewModel;Lkotlin/jvm/functions/Function0;)V", "isJoined", "", "getLayoutId", "", "position", "obj", "getViewHolder", "Landroidx/recyclerview/widget/RecyclerView$ViewHolder;", "view", "Landroid/view/View;", "viewType", "setUserIsJoined", "Companion", "EkoMembershipViewHolder", "community_debug"})
public final class EkoCommunityMembersAdapter extends com.ekoapp.ekosdk.uikit.base.EkoBaseRecyclerViewPagedAdapter<com.ekoapp.ekosdk.community.membership.EkoCommunityMembership> {
    private boolean isJoined = false;
    private final android.content.Context context = null;
    private final com.ekoapp.ekosdk.uikit.community.members.IMemberClickListener listener = null;
    private final com.ekoapp.ekosdk.uikit.community.members.EkoCommunityMembersViewModel communityMemberViewModel = null;
    private final kotlin.jvm.functions.Function0<kotlin.Unit> onUserPromoted = null;
    private static final androidx.recyclerview.widget.DiffUtil.ItemCallback<com.ekoapp.ekosdk.community.membership.EkoCommunityMembership> diffCallBack = null;
    public static final com.ekoapp.ekosdk.uikit.community.members.EkoCommunityMembersAdapter.Companion Companion = null;
    
    @java.lang.Override()
    protected int getLayoutId(int position, @org.jetbrains.annotations.Nullable()
    com.ekoapp.ekosdk.community.membership.EkoCommunityMembership obj) {
        return 0;
    }
    
    @org.jetbrains.annotations.NotNull()
    @java.lang.Override()
    public androidx.recyclerview.widget.RecyclerView.ViewHolder getViewHolder(@org.jetbrains.annotations.NotNull()
    android.view.View view, int viewType) {
        return null;
    }
    
    public final void setUserIsJoined(boolean isJoined) {
    }
    
    public EkoCommunityMembersAdapter(@org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.NotNull()
    com.ekoapp.ekosdk.uikit.community.members.IMemberClickListener listener, @org.jetbrains.annotations.NotNull()
    com.ekoapp.ekosdk.uikit.community.members.EkoCommunityMembersViewModel communityMemberViewModel, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onUserPromoted) {
        super(null);
    }
    
    @kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000J\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0086\u0004\u0018\u00002\u00020\u00012\b\u0012\u0004\u0012\u00020\u00030\u0002B%\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u0012\u0006\u0010\n\u001a\u00020\u000b\u00a2\u0006\u0002\u0010\fJ\u001a\u0010\u000f\u001a\u00020\u00102\b\u0010\u0011\u001a\u0004\u0018\u00010\u00032\u0006\u0010\u0012\u001a\u00020\u0013H\u0016J\u0010\u0010\u0014\u001a\u00020\u00102\u0006\u0010\u0015\u001a\u00020\u0016H\u0002J\u0010\u0010\u0017\u001a\u00020\u00102\u0006\u0010\u0018\u001a\u00020\u0003H\u0002R\u0010\u0010\r\u001a\u0004\u0018\u00010\u000eX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0019"}, d2 = {"Lcom/ekoapp/ekosdk/uikit/community/members/EkoCommunityMembersAdapter$EkoMembershipViewHolder;", "Lcom/ekoapp/ekosdk/uikit/community/members/EkoCommunityMembersBaseViewHolder;", "Lcom/ekoapp/ekosdk/uikit/base/EkoBaseRecyclerViewPagedAdapter$Binder;", "Lcom/ekoapp/ekosdk/community/membership/EkoCommunityMembership;", "itemView", "Landroid/view/View;", "context", "Landroid/content/Context;", "listener", "Lcom/ekoapp/ekosdk/uikit/community/members/IMemberClickListener;", "itemViewModel", "Lcom/ekoapp/ekosdk/uikit/community/members/EkoMembershipItemViewModel;", "(Lcom/ekoapp/ekosdk/uikit/community/members/EkoCommunityMembersAdapter;Landroid/view/View;Landroid/content/Context;Lcom/ekoapp/ekosdk/uikit/community/members/IMemberClickListener;Lcom/ekoapp/ekosdk/uikit/community/members/EkoMembershipItemViewModel;)V", "binding", "Lcom/ekoapp/ekosdk/uikit/community/databinding/AmityItemCommunityMembershipBinding;", "bind", "", "data", "position", "", "promoteModerator", "user", "Lcom/ekoapp/ekosdk/user/EkoUser;", "showBottomSheet", "communityMembership", "community_debug"})
    public final class EkoMembershipViewHolder extends com.ekoapp.ekosdk.uikit.community.members.EkoCommunityMembersBaseViewHolder implements com.ekoapp.ekosdk.uikit.base.EkoBaseRecyclerViewPagedAdapter.Binder<com.ekoapp.ekosdk.community.membership.EkoCommunityMembership> {
        private final com.ekoapp.ekosdk.uikit.community.databinding.AmityItemCommunityMembershipBinding binding = null;
        private final android.content.Context context = null;
        private final com.ekoapp.ekosdk.uikit.community.members.IMemberClickListener listener = null;
        private final com.ekoapp.ekosdk.uikit.community.members.EkoMembershipItemViewModel itemViewModel = null;
        
        @java.lang.Override()
        public void bind(@org.jetbrains.annotations.Nullable()
        com.ekoapp.ekosdk.community.membership.EkoCommunityMembership data, int position) {
        }
        
        private final void showBottomSheet(com.ekoapp.ekosdk.community.membership.EkoCommunityMembership communityMembership) {
        }
        
        private final void promoteModerator(com.ekoapp.ekosdk.user.EkoUser user) {
        }
        
        public EkoMembershipViewHolder(@org.jetbrains.annotations.NotNull()
        android.view.View itemView, @org.jetbrains.annotations.NotNull()
        android.content.Context context, @org.jetbrains.annotations.NotNull()
        com.ekoapp.ekosdk.uikit.community.members.IMemberClickListener listener, @org.jetbrains.annotations.NotNull()
        com.ekoapp.ekosdk.uikit.community.members.EkoMembershipItemViewModel itemViewModel) {
            super(null, null, null, null);
        }
    }
    
    @kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\u0016\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u0014\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0006"}, d2 = {"Lcom/ekoapp/ekosdk/uikit/community/members/EkoCommunityMembersAdapter$Companion;", "", "()V", "diffCallBack", "Landroidx/recyclerview/widget/DiffUtil$ItemCallback;", "Lcom/ekoapp/ekosdk/community/membership/EkoCommunityMembership;", "community_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}