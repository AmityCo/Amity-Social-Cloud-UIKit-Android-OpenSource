package com.ekoapp.ekosdk.uikit.community.newsfeed.viewmodel;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000F\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0006\u0010\u001b\u001a\u00020\u0016J\u0016\u0010\u000b\u001a\n\u0012\u0004\u0012\u00020\n\u0018\u00010\u001c2\u0006\u0010\u000f\u001a\u00020\u0010J\u0016\u0010\u001d\u001a\u0010\u0012\n\u0012\b\u0012\u0004\u0012\u00020 0\u001f\u0018\u00010\u001eH\u0016J\u0006\u0010!\u001a\u00020\"R\u001c\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\bR\u001c\u0010\t\u001a\u0004\u0018\u00010\nX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u000b\u0010\f\"\u0004\b\r\u0010\u000eR\u001c\u0010\u000f\u001a\u0004\u0018\u00010\u0010X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0011\u0010\u0012\"\u0004\b\u0013\u0010\u0014R\u001a\u0010\u0015\u001a\u00020\u0016X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0017\u0010\u0018\"\u0004\b\u0019\u0010\u001a\u00a8\u0006#"}, d2 = {"Lcom/ekoapp/ekosdk/uikit/community/newsfeed/viewmodel/EkoCommunityTimelineViewModel;", "Lcom/ekoapp/ekosdk/uikit/community/newsfeed/viewmodel/EkoBaseFeedViewModel;", "()V", "avatarClickListener", "Lcom/ekoapp/ekosdk/uikit/community/newsfeed/listener/IAvatarClickListener;", "getAvatarClickListener", "()Lcom/ekoapp/ekosdk/uikit/community/newsfeed/listener/IAvatarClickListener;", "setAvatarClickListener", "(Lcom/ekoapp/ekosdk/uikit/community/newsfeed/listener/IAvatarClickListener;)V", "community", "Lcom/ekoapp/ekosdk/community/EkoCommunity;", "getCommunity", "()Lcom/ekoapp/ekosdk/community/EkoCommunity;", "setCommunity", "(Lcom/ekoapp/ekosdk/community/EkoCommunity;)V", "communityId", "", "getCommunityId", "()Ljava/lang/String;", "setCommunityId", "(Ljava/lang/String;)V", "hasAdminAccess", "", "getHasAdminAccess", "()Z", "setHasAdminAccess", "(Z)V", "canCreatePost", "Lio/reactivex/Single;", "getFeed", "Lio/reactivex/Flowable;", "Landroidx/paging/PagedList;", "Lcom/ekoapp/ekosdk/feed/EkoPost;", "updateAdminAccess", "", "community_debug"})
public final class EkoCommunityTimelineViewModel extends com.ekoapp.ekosdk.uikit.community.newsfeed.viewmodel.EkoBaseFeedViewModel {
    @org.jetbrains.annotations.Nullable()
    private java.lang.String communityId;
    @org.jetbrains.annotations.Nullable()
    private com.ekoapp.ekosdk.community.EkoCommunity community;
    private boolean hasAdminAccess = false;
    @org.jetbrains.annotations.Nullable()
    private com.ekoapp.ekosdk.uikit.community.newsfeed.listener.IAvatarClickListener avatarClickListener;
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getCommunityId() {
        return null;
    }
    
    public final void setCommunityId(@org.jetbrains.annotations.Nullable()
    java.lang.String p0) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.ekoapp.ekosdk.community.EkoCommunity getCommunity() {
        return null;
    }
    
    public final void setCommunity(@org.jetbrains.annotations.Nullable()
    com.ekoapp.ekosdk.community.EkoCommunity p0) {
    }
    
    public final boolean getHasAdminAccess() {
        return false;
    }
    
    public final void setHasAdminAccess(boolean p0) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.ekoapp.ekosdk.uikit.community.newsfeed.listener.IAvatarClickListener getAvatarClickListener() {
        return null;
    }
    
    public final void setAvatarClickListener(@org.jetbrains.annotations.Nullable()
    com.ekoapp.ekosdk.uikit.community.newsfeed.listener.IAvatarClickListener p0) {
    }
    
    @org.jetbrains.annotations.Nullable()
    @java.lang.Override()
    public io.reactivex.Flowable<androidx.paging.PagedList<com.ekoapp.ekosdk.feed.EkoPost>> getFeed() {
        return null;
    }
    
    public final boolean canCreatePost() {
        return false;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final io.reactivex.Single<com.ekoapp.ekosdk.community.EkoCommunity> getCommunity(@org.jetbrains.annotations.NotNull()
    java.lang.String communityId) {
        return null;
    }
    
    public final void updateAdminAccess() {
    }
    
    public EkoCommunityTimelineViewModel() {
        super();
    }
}