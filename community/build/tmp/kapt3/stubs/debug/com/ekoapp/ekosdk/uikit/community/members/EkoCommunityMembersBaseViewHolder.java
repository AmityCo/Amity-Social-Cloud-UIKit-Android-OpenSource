package com.ekoapp.ekosdk.uikit.community.members;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000D\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\u0003\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0003\b&\u0018\u00002\u00020\u0001B%\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u00a2\u0006\u0002\u0010\nJ\b\u0010\u000b\u001a\u00020\fH\u0002J\u000e\u0010\r\u001a\u00020\f2\u0006\u0010\u000e\u001a\u00020\u000fJ\u000e\u0010\u0010\u001a\u00020\f2\u0006\u0010\u0011\u001a\u00020\u0012J\u0010\u0010\u0013\u001a\u00020\f2\u0006\u0010\u0011\u001a\u00020\u0012H\u0002J\u0016\u0010\u0014\u001a\u00020\f2\u0006\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0015\u001a\u00020\u0016J\u0010\u0010\u0017\u001a\u00020\f2\u0006\u0010\u0015\u001a\u00020\u0016H\u0002J\u000e\u0010\u0018\u001a\u00020\f2\u0006\u0010\u0011\u001a\u00020\u0012R\u000e\u0010\b\u001a\u00020\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0019"}, d2 = {"Lcom/ekoapp/ekosdk/uikit/community/members/EkoCommunityMembersBaseViewHolder;", "Landroidx/recyclerview/widget/RecyclerView$ViewHolder;", "itemView", "Landroid/view/View;", "context", "Landroid/content/Context;", "itemViewModel", "Lcom/ekoapp/ekosdk/uikit/community/members/EkoMembershipItemViewModel;", "communityMemberViewModel", "Lcom/ekoapp/ekosdk/uikit/community/members/EkoCommunityMembersViewModel;", "(Landroid/view/View;Landroid/content/Context;Lcom/ekoapp/ekosdk/uikit/community/members/EkoMembershipItemViewModel;Lcom/ekoapp/ekosdk/uikit/community/members/EkoCommunityMembersViewModel;)V", "checkUserRole", "", "handleNoPermissionError", "exception", "", "removeModerator", "ekoUser", "Lcom/ekoapp/ekosdk/user/EkoUser;", "removeUser", "sendReportUser", "isReport", "", "showDialogSentReportMessage", "showRemoveUserDialog", "community_debug"})
public abstract class EkoCommunityMembersBaseViewHolder extends androidx.recyclerview.widget.RecyclerView.ViewHolder {
    private final android.content.Context context = null;
    private final com.ekoapp.ekosdk.uikit.community.members.EkoMembershipItemViewModel itemViewModel = null;
    private final com.ekoapp.ekosdk.uikit.community.members.EkoCommunityMembersViewModel communityMemberViewModel = null;
    
    public final void sendReportUser(@org.jetbrains.annotations.NotNull()
    com.ekoapp.ekosdk.user.EkoUser ekoUser, boolean isReport) {
    }
    
    private final void showDialogSentReportMessage(boolean isReport) {
    }
    
    public final void showRemoveUserDialog(@org.jetbrains.annotations.NotNull()
    com.ekoapp.ekosdk.user.EkoUser ekoUser) {
    }
    
    private final void removeUser(com.ekoapp.ekosdk.user.EkoUser ekoUser) {
    }
    
    public final void removeModerator(@org.jetbrains.annotations.NotNull()
    com.ekoapp.ekosdk.user.EkoUser ekoUser) {
    }
    
    public final void handleNoPermissionError(@org.jetbrains.annotations.NotNull()
    java.lang.Throwable exception) {
    }
    
    private final void checkUserRole() {
    }
    
    public EkoCommunityMembersBaseViewHolder(@org.jetbrains.annotations.NotNull()
    android.view.View itemView, @org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.NotNull()
    com.ekoapp.ekosdk.uikit.community.members.EkoMembershipItemViewModel itemViewModel, @org.jetbrains.annotations.NotNull()
    com.ekoapp.ekosdk.uikit.community.members.EkoCommunityMembersViewModel communityMemberViewModel) {
        super(null);
    }
}