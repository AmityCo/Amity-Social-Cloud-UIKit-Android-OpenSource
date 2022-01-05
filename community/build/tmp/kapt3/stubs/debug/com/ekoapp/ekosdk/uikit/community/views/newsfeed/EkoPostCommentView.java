package com.ekoapp.ekosdk.uikit.community.views.newsfeed;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000V\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0010\u000e\n\u0002\b\u0002\u0018\u00002\u00020\u0001:\u0001$B\u000f\b\u0016\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004B\u0017\b\u0016\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u00a2\u0006\u0002\u0010\u0007B\u001f\b\u0016\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u0012\u0006\u0010\b\u001a\u00020\t\u00a2\u0006\u0002\u0010\nJ\u0006\u0010\u0012\u001a\u00020\u0013J\b\u0010\u0014\u001a\u00020\u0013H\u0002J\b\u0010\u0015\u001a\u00020\u0013H\u0002J\u000e\u0010\u0016\u001a\u00020\u00132\u0006\u0010\u0017\u001a\u00020\u0018J\u000e\u0010\u0019\u001a\u00020\u00132\u0006\u0010\u001a\u001a\u00020\fJ\u000e\u0010\u001b\u001a\u00020\u00132\u0006\u0010\u001a\u001a\u00020\u000eJ\u000e\u0010\u001c\u001a\u00020\u00132\u0006\u0010\u001d\u001a\u00020\u001eJ\u000e\u0010\u001f\u001a\u00020\u00132\u0006\u0010 \u001a\u00020\u001eJ\u000e\u0010!\u001a\u00020\u00132\u0006\u0010\"\u001a\u00020#R\u0010\u0010\u000b\u001a\u0004\u0018\u00010\fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\r\u001a\u0004\u0018\u00010\u000eX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0010X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\tX\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006%"}, d2 = {"Lcom/ekoapp/ekosdk/uikit/community/views/newsfeed/EkoPostCommentView;", "Landroidx/constraintlayout/widget/ConstraintLayout;", "context", "Landroid/content/Context;", "(Landroid/content/Context;)V", "attrs", "Landroid/util/AttributeSet;", "(Landroid/content/Context;Landroid/util/AttributeSet;)V", "defStyleAttr", "", "(Landroid/content/Context;Landroid/util/AttributeSet;I)V", "commentActionListener", "Lcom/ekoapp/ekosdk/uikit/community/views/newsfeed/EkoPostCommentView$ICommentActionListener;", "commentTextClickListener", "Landroid/view/View$OnClickListener;", "mBinding", "Lcom/ekoapp/ekosdk/uikit/community/databinding/AmityItemCommentNewsFeedBinding;", "reactionCount", "enableReadOnlyMode", "", "handleBottomSpace", "init", "setComment", "comment", "Lcom/ekoapp/ekosdk/comment/EkoComment;", "setCommentActionListener", "listener", "setOnExpandClickListener", "setReadOnlyMode", "readOnly", "", "setShowViewRepliesButton", "showRepliesComment", "setText", "text", "", "ICommentActionListener", "community_debug"})
public final class EkoPostCommentView extends androidx.constraintlayout.widget.ConstraintLayout {
    private com.ekoapp.ekosdk.uikit.community.databinding.AmityItemCommentNewsFeedBinding mBinding;
    private com.ekoapp.ekosdk.uikit.community.views.newsfeed.EkoPostCommentView.ICommentActionListener commentActionListener;
    private android.view.View.OnClickListener commentTextClickListener;
    private int reactionCount = 0;
    private java.util.HashMap _$_findViewCache;
    
    private final void init() {
    }
    
    public final void setOnExpandClickListener(@org.jetbrains.annotations.NotNull()
    android.view.View.OnClickListener listener) {
    }
    
    public final void setComment(@org.jetbrains.annotations.NotNull()
    com.ekoapp.ekosdk.comment.EkoComment comment) {
    }
    
    public final void setCommentActionListener(@org.jetbrains.annotations.NotNull()
    com.ekoapp.ekosdk.uikit.community.views.newsfeed.EkoPostCommentView.ICommentActionListener listener) {
    }
    
    private final void handleBottomSpace() {
    }
    
    public final void setReadOnlyMode(boolean readOnly) {
    }
    
    public final void setShowViewRepliesButton(boolean showRepliesComment) {
    }
    
    public final void enableReadOnlyMode() {
    }
    
    public final void setText(@org.jetbrains.annotations.NotNull()
    java.lang.String text) {
    }
    
    public EkoPostCommentView(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        super(null);
    }
    
    public EkoPostCommentView(@org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.NotNull()
    android.util.AttributeSet attrs) {
        super(null);
    }
    
    public EkoPostCommentView(@org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.NotNull()
    android.util.AttributeSet attrs, int defStyleAttr) {
        super(null);
    }
    
    @kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\bf\u0018\u00002\u00020\u0001J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H&J\b\u0010\u0006\u001a\u00020\u0003H&J\b\u0010\u0007\u001a\u00020\u0003H&\u00a8\u0006\b"}, d2 = {"Lcom/ekoapp/ekosdk/uikit/community/views/newsfeed/EkoPostCommentView$ICommentActionListener;", "", "onClickReply", "", "comment", "Lcom/ekoapp/ekosdk/comment/EkoComment;", "showAllReplies", "showMoreAction", "community_debug"})
    public static abstract interface ICommentActionListener {
        
        public abstract void showAllReplies();
        
        public abstract void showMoreAction();
        
        public abstract void onClickReply(@org.jetbrains.annotations.NotNull()
        com.ekoapp.ekosdk.comment.EkoComment comment);
    }
}