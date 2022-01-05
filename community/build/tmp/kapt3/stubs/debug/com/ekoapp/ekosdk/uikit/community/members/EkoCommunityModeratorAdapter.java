package com.ekoapp.ekosdk.uikit.community.members;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u00008\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\u0018\u0000 \u00132\b\u0012\u0004\u0012\u00020\u00020\u0001:\u0002\u0013\u0014B\u001d\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u0012\u0006\u0010\u0007\u001a\u00020\b\u00a2\u0006\u0002\u0010\tJ\u001a\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\u000b2\b\u0010\r\u001a\u0004\u0018\u00010\u0002H\u0014J\u0018\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u000bH\u0016R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0015"}, d2 = {"Lcom/ekoapp/ekosdk/uikit/community/members/EkoCommunityModeratorAdapter;", "Lcom/ekoapp/ekosdk/uikit/base/EkoBaseRecyclerViewPagedAdapter;", "Lcom/ekoapp/ekosdk/community/membership/EkoCommunityMembership;", "context", "Landroid/content/Context;", "listener", "Lcom/ekoapp/ekosdk/uikit/community/members/IMemberClickListener;", "communityMemberViewModel", "Lcom/ekoapp/ekosdk/uikit/community/members/EkoCommunityMembersViewModel;", "(Landroid/content/Context;Lcom/ekoapp/ekosdk/uikit/community/members/IMemberClickListener;Lcom/ekoapp/ekosdk/uikit/community/members/EkoCommunityMembersViewModel;)V", "getLayoutId", "", "position", "obj", "getViewHolder", "Landroidx/recyclerview/widget/RecyclerView$ViewHolder;", "view", "Landroid/view/View;", "viewType", "Companion", "EkoModeratorViewHolder", "community_debug"})
public final class EkoCommunityModeratorAdapter extends com.ekoapp.ekosdk.uikit.base.EkoBaseRecyclerViewPagedAdapter<com.ekoapp.ekosdk.community.membership.EkoCommunityMembership> {
    private final android.content.Context context = null;
    private final com.ekoapp.ekosdk.uikit.community.members.IMemberClickListener listener = null;
    private final com.ekoapp.ekosdk.uikit.community.members.EkoCommunityMembersViewModel communityMemberViewModel = null;
    private static final androidx.recyclerview.widget.DiffUtil.ItemCallback<com.ekoapp.ekosdk.community.membership.EkoCommunityMembership> diffCallBack = null;
    public static final com.ekoapp.ekosdk.uikit.community.members.EkoCommunityModeratorAdapter.Companion Companion = null;
    
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
    
    public EkoCommunityModeratorAdapter(@org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.NotNull()
    com.ekoapp.ekosdk.uikit.community.members.IMemberClickListener listener, @org.jetbrains.annotations.NotNull()
    com.ekoapp.ekosdk.uikit.community.members.EkoCommunityMembersViewModel communityMemberViewModel) {
        super(null);
    }
    
    @kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000B\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0003\b\u0086\u0004\u0018\u00002\u00020\u00012\b\u0012\u0004\u0012\u00020\u00030\u0002B%\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u0012\u0006\u0010\n\u001a\u00020\u000b\u00a2\u0006\u0002\u0010\fJ\u001a\u0010\u000f\u001a\u00020\u00102\b\u0010\u0011\u001a\u0004\u0018\u00010\u00032\u0006\u0010\u0012\u001a\u00020\u0013H\u0016J\u0010\u0010\u0014\u001a\u00020\u00102\u0006\u0010\u0015\u001a\u00020\u0003H\u0002R\u0010\u0010\r\u001a\u0004\u0018\u00010\u000eX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0016"}, d2 = {"Lcom/ekoapp/ekosdk/uikit/community/members/EkoCommunityModeratorAdapter$EkoModeratorViewHolder;", "Lcom/ekoapp/ekosdk/uikit/community/members/EkoCommunityMembersBaseViewHolder;", "Lcom/ekoapp/ekosdk/uikit/base/EkoBaseRecyclerViewPagedAdapter$Binder;", "Lcom/ekoapp/ekosdk/community/membership/EkoCommunityMembership;", "itemView", "Landroid/view/View;", "context", "Landroid/content/Context;", "listener", "Lcom/ekoapp/ekosdk/uikit/community/members/IMemberClickListener;", "itemViewModel", "Lcom/ekoapp/ekosdk/uikit/community/members/EkoMembershipItemViewModel;", "(Lcom/ekoapp/ekosdk/uikit/community/members/EkoCommunityModeratorAdapter;Landroid/view/View;Landroid/content/Context;Lcom/ekoapp/ekosdk/uikit/community/members/IMemberClickListener;Lcom/ekoapp/ekosdk/uikit/community/members/EkoMembershipItemViewModel;)V", "binding", "Lcom/ekoapp/ekosdk/uikit/community/databinding/AmityItemCommunityMembershipBinding;", "bind", "", "data", "position", "", "showBottomSheet", "communityMembership", "community_debug"})
    public final class EkoModeratorViewHolder extends com.ekoapp.ekosdk.uikit.community.members.EkoCommunityMembersBaseViewHolder implements com.ekoapp.ekosdk.uikit.base.EkoBaseRecyclerViewPagedAdapter.Binder<com.ekoapp.ekosdk.community.membership.EkoCommunityMembership> {
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
        
        public EkoModeratorViewHolder(@org.jetbrains.annotations.NotNull()
        android.view.View itemView, @org.jetbrains.annotations.NotNull()
        android.content.Context context, @org.jetbrains.annotations.NotNull()
        com.ekoapp.ekosdk.uikit.community.members.IMemberClickListener listener, @org.jetbrains.annotations.NotNull()
        com.ekoapp.ekosdk.uikit.community.members.EkoMembershipItemViewModel itemViewModel) {
            super(null, null, null, null);
        }
    }
    
    @kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\u0016\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u0014\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0006"}, d2 = {"Lcom/ekoapp/ekosdk/uikit/community/members/EkoCommunityModeratorAdapter$Companion;", "", "()V", "diffCallBack", "Landroidx/recyclerview/widget/DiffUtil$ItemCallback;", "Lcom/ekoapp/ekosdk/community/membership/EkoCommunityMembership;", "community_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}