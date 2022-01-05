package com.ekoapp.ekosdk.uikit.community.newsfeed.adapter;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000D\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0012\u0010\u0013\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00030\u00070\u0014J\u0006\u0010\u0015\u001a\u00020\u0016J\b\u0010\u0017\u001a\u00020\nH\u0002J\f\u0010\u0018\u001a\b\u0012\u0004\u0012\u00020\n0\u0014R(\u0010\u0005\u001a\u001c\u0012\u0018\u0012\u0016\u0012\u0004\u0012\u00020\u0003 \b*\n\u0012\u0004\u0012\u00020\u0003\u0018\u00010\u00070\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\u00030\u0007X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\rX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u00030\u0007X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0010X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\nX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001c\u0010\u0012\u001a\u0010\u0012\f\u0012\n \b*\u0004\u0018\u00010\n0\n0\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0019"}, d2 = {"Lcom/ekoapp/ekosdk/uikit/community/newsfeed/adapter/EkoCommentReplyLoader;", "", "comment", "Lcom/ekoapp/ekosdk/comment/EkoComment;", "(Lcom/ekoapp/ekosdk/comment/EkoComment;)V", "commentsSubject", "Lio/reactivex/subjects/PublishSubject;", "", "kotlin.jvm.PlatformType", "isLoading", "", "loadedComments", "loader", "Lcom/ekoapp/ekosdk/comment/query/EkoCommentLoader;", "publishingComments", "publishingSize", "", "selfLoad", "showLoadMoreButtonSubject", "getComments", "Lio/reactivex/Flowable;", "load", "Lio/reactivex/Completable;", "shouldShowLoadMoreButton", "showLoadMoreButton", "community_debug"})
public final class EkoCommentReplyLoader {
    private com.ekoapp.ekosdk.comment.query.EkoCommentLoader loader;
    private final io.reactivex.subjects.PublishSubject<java.util.List<com.ekoapp.ekosdk.comment.EkoComment>> commentsSubject = null;
    private final io.reactivex.subjects.PublishSubject<java.lang.Boolean> showLoadMoreButtonSubject = null;
    private java.util.List<com.ekoapp.ekosdk.comment.EkoComment> loadedComments;
    private java.util.List<com.ekoapp.ekosdk.comment.EkoComment> publishingComments;
    private boolean isLoading = false;
    private int publishingSize = 3;
    private boolean selfLoad = false;
    
    @org.jetbrains.annotations.NotNull()
    public final io.reactivex.Flowable<java.lang.Boolean> showLoadMoreButton() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final io.reactivex.Flowable<java.util.List<com.ekoapp.ekosdk.comment.EkoComment>> getComments() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final io.reactivex.Completable load() {
        return null;
    }
    
    private final boolean shouldShowLoadMoreButton() {
        return false;
    }
    
    public EkoCommentReplyLoader(@org.jetbrains.annotations.NotNull()
    com.ekoapp.ekosdk.comment.EkoComment comment) {
        super();
    }
}