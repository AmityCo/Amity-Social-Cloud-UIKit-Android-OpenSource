package com.ekoapp.ekosdk.uikit.community.newsfeed.listener;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000D\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0004\bf\u0018\u00002\u00020\u0001J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H&J\u0018\u0010\u0006\u001a\u00020\u00032\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\nH&J \u0010\u000b\u001a\u00020\u00032\u0006\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\t\u001a\u00020\nH&J \u0010\u0010\u001a\u00020\u00032\u0006\u0010\f\u001a\u00020\r2\u0006\u0010\u0011\u001a\u00020\u00122\u0006\u0010\t\u001a\u00020\nH&J\u0018\u0010\u0013\u001a\u00020\u00032\u0006\u0010\f\u001a\u00020\r2\u0006\u0010\t\u001a\u00020\nH&J \u0010\u0014\u001a\u00020\u00032\u0006\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u0017\u001a\u00020\r2\u0006\u0010\t\u001a\u00020\nH&J\u0018\u0010\u0018\u001a\u00020\u00032\u0006\u0010\u0017\u001a\u00020\r2\u0006\u0010\t\u001a\u00020\nH&J \u0010\u0019\u001a\u00020\u00032\u0006\u0010\f\u001a\u00020\r2\u0006\u0010\u0011\u001a\u00020\u00122\u0006\u0010\t\u001a\u00020\nH&\u00a8\u0006\u001a"}, d2 = {"Lcom/ekoapp/ekosdk/uikit/community/newsfeed/listener/IPostItemActionListener;", "", "onClickCommunity", "", "community", "Lcom/ekoapp/ekosdk/community/EkoCommunity;", "onClickItem", "postId", "", "position", "", "onClickUserAvatar", "feed", "Lcom/ekoapp/ekosdk/feed/EkoPost;", "user", "Lcom/ekoapp/ekosdk/user/EkoUser;", "onCommentAction", "comment", "Lcom/ekoapp/ekosdk/comment/EkoComment;", "onFeedAction", "onLikeAction", "liked", "", "ekoPost", "onShareAction", "showAllReply", "community_debug"})
public abstract interface IPostItemActionListener {
    
    public abstract void onFeedAction(@org.jetbrains.annotations.NotNull()
    com.ekoapp.ekosdk.feed.EkoPost feed, int position);
    
    public abstract void onClickItem(@org.jetbrains.annotations.NotNull()
    java.lang.String postId, int position);
    
    public abstract void onCommentAction(@org.jetbrains.annotations.NotNull()
    com.ekoapp.ekosdk.feed.EkoPost feed, @org.jetbrains.annotations.NotNull()
    com.ekoapp.ekosdk.comment.EkoComment comment, int position);
    
    public abstract void showAllReply(@org.jetbrains.annotations.NotNull()
    com.ekoapp.ekosdk.feed.EkoPost feed, @org.jetbrains.annotations.NotNull()
    com.ekoapp.ekosdk.comment.EkoComment comment, int position);
    
    public abstract void onLikeAction(boolean liked, @org.jetbrains.annotations.NotNull()
    com.ekoapp.ekosdk.feed.EkoPost ekoPost, int position);
    
    public abstract void onShareAction(@org.jetbrains.annotations.NotNull()
    com.ekoapp.ekosdk.feed.EkoPost ekoPost, int position);
    
    public abstract void onClickUserAvatar(@org.jetbrains.annotations.NotNull()
    com.ekoapp.ekosdk.feed.EkoPost feed, @org.jetbrains.annotations.NotNull()
    com.ekoapp.ekosdk.user.EkoUser user, int position);
    
    public abstract void onClickCommunity(@org.jetbrains.annotations.NotNull()
    com.ekoapp.ekosdk.community.EkoCommunity community);
}