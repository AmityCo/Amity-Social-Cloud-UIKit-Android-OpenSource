package com.ekoapp.ekosdk.uikit.community.newsfeed.listener;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\bf\u0018\u00002\u00020\u0001J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H&J\u0018\u0010\u0006\u001a\u00020\u00032\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\nH&\u00a8\u0006\u000b"}, d2 = {"Lcom/ekoapp/ekosdk/uikit/community/newsfeed/listener/IPostCommentItemClickListener;", "", "onClickAvatar", "", "user", "Lcom/ekoapp/ekosdk/user/EkoUser;", "onClickItem", "comment", "Lcom/ekoapp/ekosdk/comment/EkoComment;", "position", "", "community_debug"})
public abstract interface IPostCommentItemClickListener {
    
    public abstract void onClickItem(@org.jetbrains.annotations.NotNull()
    com.ekoapp.ekosdk.comment.EkoComment comment, int position);
    
    public abstract void onClickAvatar(@org.jetbrains.annotations.NotNull()
    com.ekoapp.ekosdk.user.EkoUser user);
}