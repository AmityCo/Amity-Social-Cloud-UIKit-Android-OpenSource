package com.ekoapp.ekosdk.uikit.community.profile.viewmodel;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u00004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\f\u0010\u0015\u001a\b\u0012\u0004\u0012\u00020\u00170\u0016J\u0006\u0010\u0018\u001a\u00020\u0019R\u001c\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\bR\u001c\u0010\t\u001a\u0004\u0018\u00010\nX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u000b\u0010\f\"\u0004\b\r\u0010\u000eR\u001c\u0010\u000f\u001a\u0004\u0018\u00010\u0010X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0011\u0010\u0012\"\u0004\b\u0013\u0010\u0014\u00a8\u0006\u001a"}, d2 = {"Lcom/ekoapp/ekosdk/uikit/community/profile/viewmodel/EkoUserProfileViewModel;", "Lcom/ekoapp/ekosdk/uikit/base/EkoBaseViewModel;", "()V", "editUserProfileClickListener", "Lcom/ekoapp/ekosdk/uikit/community/profile/listener/IEditUserProfileClickListener;", "getEditUserProfileClickListener", "()Lcom/ekoapp/ekosdk/uikit/community/profile/listener/IEditUserProfileClickListener;", "setEditUserProfileClickListener", "(Lcom/ekoapp/ekosdk/uikit/community/profile/listener/IEditUserProfileClickListener;)V", "feedFragmentDelegate", "Lcom/ekoapp/ekosdk/uikit/community/profile/listener/IFeedFragmentDelegate;", "getFeedFragmentDelegate", "()Lcom/ekoapp/ekosdk/uikit/community/profile/listener/IFeedFragmentDelegate;", "setFeedFragmentDelegate", "(Lcom/ekoapp/ekosdk/uikit/community/profile/listener/IFeedFragmentDelegate;)V", "userId", "", "getUserId", "()Ljava/lang/String;", "setUserId", "(Ljava/lang/String;)V", "getUser", "Lio/reactivex/Flowable;", "Lcom/ekoapp/ekosdk/user/EkoUser;", "isLoggedInUser", "", "community_debug"})
public final class EkoUserProfileViewModel extends com.ekoapp.ekosdk.uikit.base.EkoBaseViewModel {
    @org.jetbrains.annotations.Nullable()
    private com.ekoapp.ekosdk.uikit.community.profile.listener.IFeedFragmentDelegate feedFragmentDelegate;
    @org.jetbrains.annotations.Nullable()
    private com.ekoapp.ekosdk.uikit.community.profile.listener.IEditUserProfileClickListener editUserProfileClickListener;
    @org.jetbrains.annotations.Nullable()
    private java.lang.String userId;
    
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
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getUserId() {
        return null;
    }
    
    public final void setUserId(@org.jetbrains.annotations.Nullable()
    java.lang.String p0) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final io.reactivex.Flowable<com.ekoapp.ekosdk.user.EkoUser> getUser() {
        return null;
    }
    
    public final boolean isLoggedInUser() {
        return false;
    }
    
    public EkoUserProfileViewModel() {
        super();
    }
}