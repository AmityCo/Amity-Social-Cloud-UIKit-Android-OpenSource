package com.ekoapp.ekosdk.uikit.community.newsfeed.viewmodel;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u00006\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0014\u0010\u000f\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00120\u00110\u0010H\u0016J\u000e\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u0016R\u001c\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\bR\u001a\u0010\t\u001a\u00020\nX\u0086.\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u000b\u0010\f\"\u0004\b\r\u0010\u000e\u00a8\u0006\u0017"}, d2 = {"Lcom/ekoapp/ekosdk/uikit/community/newsfeed/viewmodel/EkoUserTimelineViewModel;", "Lcom/ekoapp/ekosdk/uikit/community/newsfeed/viewmodel/EkoBaseFeedViewModel;", "()V", "avatarClickListener", "Lcom/ekoapp/ekosdk/uikit/community/newsfeed/listener/IAvatarClickListener;", "getAvatarClickListener", "()Lcom/ekoapp/ekosdk/uikit/community/newsfeed/listener/IAvatarClickListener;", "setAvatarClickListener", "(Lcom/ekoapp/ekosdk/uikit/community/newsfeed/listener/IAvatarClickListener;)V", "userId", "", "getUserId", "()Ljava/lang/String;", "setUserId", "(Ljava/lang/String;)V", "getFeed", "Lio/reactivex/Flowable;", "Landroidx/paging/PagedList;", "Lcom/ekoapp/ekosdk/feed/EkoPost;", "otherUser", "", "user", "Lcom/ekoapp/ekosdk/user/EkoUser;", "community_debug"})
public final class EkoUserTimelineViewModel extends com.ekoapp.ekosdk.uikit.community.newsfeed.viewmodel.EkoBaseFeedViewModel {
    @org.jetbrains.annotations.NotNull()
    public java.lang.String userId;
    @org.jetbrains.annotations.Nullable()
    private com.ekoapp.ekosdk.uikit.community.newsfeed.listener.IAvatarClickListener avatarClickListener;
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getUserId() {
        return null;
    }
    
    public final void setUserId(@org.jetbrains.annotations.NotNull()
    java.lang.String p0) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.ekoapp.ekosdk.uikit.community.newsfeed.listener.IAvatarClickListener getAvatarClickListener() {
        return null;
    }
    
    public final void setAvatarClickListener(@org.jetbrains.annotations.Nullable()
    com.ekoapp.ekosdk.uikit.community.newsfeed.listener.IAvatarClickListener p0) {
    }
    
    @org.jetbrains.annotations.NotNull()
    @java.lang.Override()
    public io.reactivex.Flowable<androidx.paging.PagedList<com.ekoapp.ekosdk.feed.EkoPost>> getFeed() {
        return null;
    }
    
    public final boolean otherUser(@org.jetbrains.annotations.NotNull()
    com.ekoapp.ekosdk.user.EkoUser user) {
        return false;
    }
    
    public EkoUserTimelineViewModel() {
        super();
    }
}