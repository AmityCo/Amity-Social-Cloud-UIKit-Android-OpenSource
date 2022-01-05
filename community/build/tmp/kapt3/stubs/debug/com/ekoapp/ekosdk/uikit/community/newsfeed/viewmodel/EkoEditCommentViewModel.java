package com.ekoapp.ekosdk.uikit.community.newsfeed.viewmodel;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000J\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u000b\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0016\u0010\u0012\u001a\n\u0012\u0004\u0012\u00020\u000b\u0018\u00010\u00132\u0006\u0010\u0014\u001a\u00020\u0007J\u0006\u0010\u0015\u001a\u00020\u0016J\u000e\u0010\u0017\u001a\u00020\u00182\u0006\u0010\u0014\u001a\u00020\u0007J\u0006\u0010\u0019\u001a\u00020\u0010J\b\u0010\u001a\u001a\u0004\u0018\u00010\u000bJ\b\u0010\u001b\u001a\u0004\u0018\u00010\u000bJ\u0010\u0010\u001c\u001a\u00020\u00162\b\u0010\u001d\u001a\u0004\u0018\u00010\u000bJ\u0010\u0010\u001e\u001a\u00020\u00162\b\u0010\u0005\u001a\u0004\u0018\u00010\u0007J\u0010\u0010\u001f\u001a\u00020\u00162\b\u0010\f\u001a\u0004\u0018\u00010\rJ\u0010\u0010 \u001a\u00020\u00162\b\u0010!\u001a\u0004\u0018\u00010\u000bJ\u000e\u0010\"\u001a\n\u0012\u0004\u0012\u00020\u000b\u0018\u00010\u0013R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\b\u0010\tR\u0010\u0010\n\u001a\u0004\u0018\u00010\u000bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\f\u001a\u0004\u0018\u00010\rX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u000e\u001a\u0004\u0018\u00010\u000bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0017\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u00100\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\t\u00a8\u0006#"}, d2 = {"Lcom/ekoapp/ekosdk/uikit/community/newsfeed/viewmodel/EkoEditCommentViewModel;", "Lcom/ekoapp/ekosdk/uikit/base/EkoBaseViewModel;", "()V", "commentRepository", "Lcom/ekoapp/ekosdk/EkoCommentRepository;", "commentText", "Landroidx/lifecycle/MutableLiveData;", "", "getCommentText", "()Landroidx/lifecycle/MutableLiveData;", "ekoComment", "Lcom/ekoapp/ekosdk/comment/EkoComment;", "ekoPost", "Lcom/ekoapp/ekosdk/feed/EkoPost;", "ekoReply", "hasCommentUpdate", "", "getHasCommentUpdate", "addComment", "Lio/reactivex/Single;", "commentId", "checkForCommentUpdate", "", "deleteComment", "Lio/reactivex/Completable;", "editMode", "getComment", "getReply", "setComment", "comment", "setCommentData", "setPost", "setReplyTo", "reply", "updateComment", "community_debug"})
public final class EkoEditCommentViewModel extends com.ekoapp.ekosdk.uikit.base.EkoBaseViewModel {
    private com.ekoapp.ekosdk.comment.EkoComment ekoComment;
    private com.ekoapp.ekosdk.comment.EkoComment ekoReply;
    private com.ekoapp.ekosdk.feed.EkoPost ekoPost;
    @org.jetbrains.annotations.NotNull()
    private final androidx.lifecycle.MutableLiveData<java.lang.String> commentText = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.lifecycle.MutableLiveData<java.lang.Boolean> hasCommentUpdate = null;
    private final com.ekoapp.ekosdk.EkoCommentRepository commentRepository = null;
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.lifecycle.MutableLiveData<java.lang.String> getCommentText() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.lifecycle.MutableLiveData<java.lang.Boolean> getHasCommentUpdate() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final io.reactivex.Single<com.ekoapp.ekosdk.comment.EkoComment> updateComment() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final io.reactivex.Single<com.ekoapp.ekosdk.comment.EkoComment> addComment(@org.jetbrains.annotations.NotNull()
    java.lang.String commentId) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final io.reactivex.Completable deleteComment(@org.jetbrains.annotations.NotNull()
    java.lang.String commentId) {
        return null;
    }
    
    public final void checkForCommentUpdate() {
    }
    
    public final void setPost(@org.jetbrains.annotations.Nullable()
    com.ekoapp.ekosdk.feed.EkoPost ekoPost) {
    }
    
    public final void setComment(@org.jetbrains.annotations.Nullable()
    com.ekoapp.ekosdk.comment.EkoComment comment) {
    }
    
    public final void setReplyTo(@org.jetbrains.annotations.Nullable()
    com.ekoapp.ekosdk.comment.EkoComment reply) {
    }
    
    public final boolean editMode() {
        return false;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.ekoapp.ekosdk.comment.EkoComment getComment() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.ekoapp.ekosdk.comment.EkoComment getReply() {
        return null;
    }
    
    public final void setCommentData(@org.jetbrains.annotations.Nullable()
    java.lang.String commentText) {
    }
    
    public EkoEditCommentViewModel() {
        super();
    }
}