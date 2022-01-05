package com.ekoapp.ekosdk.uikit.community.members;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\\\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\b\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u001c\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\u00042\f\u0010\f\u001a\b\u0012\u0004\u0012\u00020\u00040\rJ\u0016\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u00100\u000f2\u0006\u0010\u0011\u001a\u00020\u0012H\u0002J,\u0010\u0013\u001a\u0018\u0012\u0014\u0012\u0012\u0012\u0004\u0012\u00020\u00150\u0014j\b\u0012\u0004\u0012\u00020\u0015`\u00160\u000f2\u0006\u0010\u0017\u001a\u00020\u00182\u0006\u0010\u0019\u001a\u00020\u001aJ,\u0010\u001b\u001a\u0018\u0012\u0014\u0012\u0012\u0012\u0004\u0012\u00020\u00150\u0014j\b\u0012\u0004\u0012\u00020\u0015`\u00160\u000f2\u0006\u0010\u0017\u001a\u00020\u00182\u0006\u0010\u0019\u001a\u00020\u001aJ\f\u0010\u001c\u001a\b\u0012\u0004\u0012\u00020\u001d0\u000fJ\u0014\u0010\u001e\u001a\b\u0012\u0004\u0012\u00020\u001f0\u000f2\u0006\u0010 \u001a\u00020\u0004J\u001c\u0010!\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\u00042\f\u0010\f\u001a\b\u0012\u0004\u0012\u00020\u00040\rJ\u0014\u0010\"\u001a\u00020\n2\f\u0010#\u001a\b\u0012\u0004\u0012\u00020\u00040\rJ\u000e\u0010$\u001a\u00020\n2\u0006\u0010%\u001a\u00020\u001fJ\u000e\u0010&\u001a\u00020\n2\u0006\u0010%\u001a\u00020\u001fR\u001a\u0010\u0003\u001a\u00020\u0004X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\b\u00a8\u0006\'"}, d2 = {"Lcom/ekoapp/ekosdk/uikit/community/members/EkoMembershipItemViewModel;", "Lcom/ekoapp/ekosdk/uikit/base/EkoBaseViewModel;", "()V", "communityId", "", "getCommunityId", "()Ljava/lang/String;", "setCommunityId", "(Ljava/lang/String;)V", "assignRole", "Lio/reactivex/Completable;", "role", "userIdList", "", "checkModeratorPermission", "Lio/reactivex/Flowable;", "", "permission", "Lcom/ekoapp/ekosdk/permission/EkoPermission;", "getBottomSheetMemberTab", "Ljava/util/ArrayList;", "Lcom/ekoapp/ekosdk/uikit/model/EkoMenuItem;", "Lkotlin/collections/ArrayList;", "context", "Landroid/content/Context;", "communityMembership", "Lcom/ekoapp/ekosdk/community/membership/EkoCommunityMembership;", "getBottomSheetModeratorTab", "getCommunityDetail", "Lcom/ekoapp/ekosdk/community/EkoCommunity;", "getUser", "Lcom/ekoapp/ekosdk/user/EkoUser;", "userId", "removeRole", "removeUser", "list", "reportUser", "ekoUser", "unReportUser", "community_debug"})
public final class EkoMembershipItemViewModel extends com.ekoapp.ekosdk.uikit.base.EkoBaseViewModel {
    @org.jetbrains.annotations.NotNull()
    private java.lang.String communityId = "";
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getCommunityId() {
        return null;
    }
    
    public final void setCommunityId(@org.jetbrains.annotations.NotNull()
    java.lang.String p0) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final io.reactivex.Flowable<java.util.ArrayList<com.ekoapp.ekosdk.uikit.model.EkoMenuItem>> getBottomSheetMemberTab(@org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.NotNull()
    com.ekoapp.ekosdk.community.membership.EkoCommunityMembership communityMembership) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final io.reactivex.Flowable<java.util.ArrayList<com.ekoapp.ekosdk.uikit.model.EkoMenuItem>> getBottomSheetModeratorTab(@org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.NotNull()
    com.ekoapp.ekosdk.community.membership.EkoCommunityMembership communityMembership) {
        return null;
    }
    
    private final io.reactivex.Flowable<java.lang.Boolean> checkModeratorPermission(com.ekoapp.ekosdk.permission.EkoPermission permission) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final io.reactivex.Completable reportUser(@org.jetbrains.annotations.NotNull()
    com.ekoapp.ekosdk.user.EkoUser ekoUser) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final io.reactivex.Completable unReportUser(@org.jetbrains.annotations.NotNull()
    com.ekoapp.ekosdk.user.EkoUser ekoUser) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final io.reactivex.Flowable<com.ekoapp.ekosdk.user.EkoUser> getUser(@org.jetbrains.annotations.NotNull()
    java.lang.String userId) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final io.reactivex.Completable removeUser(@org.jetbrains.annotations.NotNull()
    java.util.List<java.lang.String> list) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final io.reactivex.Completable assignRole(@org.jetbrains.annotations.NotNull()
    java.lang.String role, @org.jetbrains.annotations.NotNull()
    java.util.List<java.lang.String> userIdList) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final io.reactivex.Completable removeRole(@org.jetbrains.annotations.NotNull()
    java.lang.String role, @org.jetbrains.annotations.NotNull()
    java.util.List<java.lang.String> userIdList) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final io.reactivex.Flowable<com.ekoapp.ekosdk.community.EkoCommunity> getCommunityDetail() {
        return null;
    }
    
    public EkoMembershipItemViewModel() {
        super();
    }
}