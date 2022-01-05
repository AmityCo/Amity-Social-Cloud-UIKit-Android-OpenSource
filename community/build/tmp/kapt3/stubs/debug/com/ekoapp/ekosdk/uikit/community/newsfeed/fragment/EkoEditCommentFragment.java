package com.ekoapp.ekosdk.uikit.community.newsfeed.fragment;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000v\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u000b\u0018\u00002\u00020\u00012\u00020\u0002:\u0001=B/\b\u0000\u0012\b\u0010\u0003\u001a\u0004\u0018\u00010\u0004\u0012\b\u0010\u0005\u001a\u0004\u0018\u00010\u0006\u0012\b\u0010\u0007\u001a\u0004\u0018\u00010\u0006\u0012\b\u0010\b\u001a\u0004\u0018\u00010\t\u00a2\u0006\u0002\u0010\nJ\b\u0010\u001d\u001a\u00020\u001eH\u0002J\b\u0010\u001f\u001a\u00020\u001eH\u0002J\b\u0010 \u001a\u00020\tH\u0002J\b\u0010!\u001a\u00020\u001eH\u0016J\b\u0010\"\u001a\u00020\u001eH\u0002J\b\u0010#\u001a\u00020\u001eH\u0016J\b\u0010$\u001a\u00020\u001eH\u0016J\u0012\u0010%\u001a\u00020\u001e2\b\u0010&\u001a\u0004\u0018\u00010\'H\u0016J\u0018\u0010(\u001a\u00020\u001e2\u0006\u0010)\u001a\u00020*2\u0006\u0010+\u001a\u00020,H\u0016J&\u0010-\u001a\u0004\u0018\u00010.2\u0006\u0010+\u001a\u00020/2\b\u00100\u001a\u0004\u0018\u0001012\b\u0010&\u001a\u0004\u0018\u00010\'H\u0016J\u0010\u00102\u001a\u0002032\u0006\u00104\u001a\u00020\u001cH\u0016J\u001a\u00105\u001a\u00020\u001e2\u0006\u00106\u001a\u00020.2\b\u0010&\u001a\u0004\u0018\u00010\'H\u0016J\b\u00107\u001a\u00020\u001eH\u0002J\b\u00108\u001a\u00020\u001eH\u0002J\b\u00109\u001a\u00020\u001eH\u0002J\b\u0010:\u001a\u00020\u001eH\u0002J\u0010\u0010;\u001a\u00020\u001e2\u0006\u0010<\u001a\u000203H\u0002R\u000e\u0010\u000b\u001a\u00020\fX\u0082D\u00a2\u0006\u0002\n\u0000R\u0016\u0010\r\u001a\n \u000e*\u0004\u0018\u00010\t0\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\b\u001a\u0004\u0018\u00010\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0005\u001a\u0004\u0018\u00010\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u000f\u001a\u00020\u0010X\u0086.\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0011\u0010\u0012\"\u0004\b\u0013\u0010\u0014R\u001b\u0010\u0015\u001a\u00020\u00168BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\u0019\u0010\u001a\u001a\u0004\b\u0017\u0010\u0018R\u0010\u0010\u001b\u001a\u0004\u0018\u00010\u001cX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0007\u001a\u0004\u0018\u00010\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006>"}, d2 = {"Lcom/ekoapp/ekosdk/uikit/community/newsfeed/fragment/EkoEditCommentFragment;", "Lcom/ekoapp/ekosdk/uikit/base/EkoBaseFragment;", "Lcom/ekoapp/ekosdk/uikit/common/views/dialog/EkoAlertDialogFragment$IAlertDialogActionListener;", "ekoPost", "Lcom/ekoapp/ekosdk/feed/EkoPost;", "ekoComment", "Lcom/ekoapp/ekosdk/comment/EkoComment;", "reply", "commentText", "", "(Lcom/ekoapp/ekosdk/feed/EkoPost;Lcom/ekoapp/ekosdk/comment/EkoComment;Lcom/ekoapp/ekosdk/comment/EkoComment;Ljava/lang/String;)V", "ID_MENU_ITEM_ADD_COMMENT", "", "TAG", "kotlin.jvm.PlatformType", "mBinding", "Lcom/ekoapp/ekosdk/uikit/community/databinding/AmityFragmentEditCommentBinding;", "getMBinding", "()Lcom/ekoapp/ekosdk/uikit/community/databinding/AmityFragmentEditCommentBinding;", "setMBinding", "(Lcom/ekoapp/ekosdk/uikit/community/databinding/AmityFragmentEditCommentBinding;)V", "mViewModel", "Lcom/ekoapp/ekosdk/uikit/community/newsfeed/viewmodel/EkoEditCommentViewModel;", "getMViewModel", "()Lcom/ekoapp/ekosdk/uikit/community/newsfeed/viewmodel/EkoEditCommentViewModel;", "mViewModel$delegate", "Lkotlin/Lazy;", "menuItemComment", "Landroid/view/MenuItem;", "addComment", "", "addEditCommentViewTextWatcher", "getMenuItemCommentTitle", "handleBackPress", "handleCancelPost", "onClickNegativeButton", "onClickPositiveButton", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "onCreateOptionsMenu", "menu", "Landroid/view/Menu;", "inflater", "Landroid/view/MenuInflater;", "onCreateView", "Landroid/view/View;", "Landroid/view/LayoutInflater;", "container", "Landroid/view/ViewGroup;", "onOptionsItemSelected", "", "item", "onViewCreated", "view", "setupInitialData", "setupToolbar", "showExitConfirmationDialog", "updateComment", "updateCommentMenu", "enabled", "Builder", "community_debug"})
public final class EkoEditCommentFragment extends com.ekoapp.ekosdk.uikit.base.EkoBaseFragment implements com.ekoapp.ekosdk.uikit.common.views.dialog.EkoAlertDialogFragment.IAlertDialogActionListener {
    private final int ID_MENU_ITEM_ADD_COMMENT = 144;
    private android.view.MenuItem menuItemComment;
    private final java.lang.String TAG = null;
    private final kotlin.Lazy mViewModel$delegate = null;
    @org.jetbrains.annotations.NotNull()
    public com.ekoapp.ekosdk.uikit.community.databinding.AmityFragmentEditCommentBinding mBinding;
    private final com.ekoapp.ekosdk.feed.EkoPost ekoPost = null;
    private final com.ekoapp.ekosdk.comment.EkoComment ekoComment = null;
    private final com.ekoapp.ekosdk.comment.EkoComment reply = null;
    private final java.lang.String commentText = null;
    private java.util.HashMap _$_findViewCache;
    
    private final com.ekoapp.ekosdk.uikit.community.newsfeed.viewmodel.EkoEditCommentViewModel getMViewModel() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.ekoapp.ekosdk.uikit.community.databinding.AmityFragmentEditCommentBinding getMBinding() {
        return null;
    }
    
    public final void setMBinding(@org.jetbrains.annotations.NotNull()
    com.ekoapp.ekosdk.uikit.community.databinding.AmityFragmentEditCommentBinding p0) {
    }
    
    @java.lang.Override()
    public void onCreate(@org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    @org.jetbrains.annotations.Nullable()
    @java.lang.Override()
    public android.view.View onCreateView(@org.jetbrains.annotations.NotNull()
    android.view.LayoutInflater inflater, @org.jetbrains.annotations.Nullable()
    android.view.ViewGroup container, @org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
        return null;
    }
    
    @java.lang.Override()
    public void onViewCreated(@org.jetbrains.annotations.NotNull()
    android.view.View view, @org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    private final void setupInitialData() {
    }
    
    private final void addEditCommentViewTextWatcher() {
    }
    
    private final void setupToolbar() {
    }
    
    @java.lang.Override()
    public void onCreateOptionsMenu(@org.jetbrains.annotations.NotNull()
    android.view.Menu menu, @org.jetbrains.annotations.NotNull()
    android.view.MenuInflater inflater) {
    }
    
    private final java.lang.String getMenuItemCommentTitle() {
        return null;
    }
    
    @java.lang.Override()
    public boolean onOptionsItemSelected(@org.jetbrains.annotations.NotNull()
    android.view.MenuItem item) {
        return false;
    }
    
    private final void updateCommentMenu(boolean enabled) {
    }
    
    @java.lang.Override()
    public void handleBackPress() {
    }
    
    private final void handleCancelPost() {
    }
    
    private final void showExitConfirmationDialog() {
    }
    
    private final void updateComment() {
    }
    
    private final void addComment() {
    }
    
    @java.lang.Override()
    public void onClickPositiveButton() {
    }
    
    @java.lang.Override()
    public void onClickNegativeButton() {
    }
    
    public EkoEditCommentFragment(@org.jetbrains.annotations.Nullable()
    com.ekoapp.ekosdk.feed.EkoPost ekoPost, @org.jetbrains.annotations.Nullable()
    com.ekoapp.ekosdk.comment.EkoComment ekoComment, @org.jetbrains.annotations.Nullable()
    com.ekoapp.ekosdk.comment.EkoComment reply, @org.jetbrains.annotations.Nullable()
    java.lang.String commentText) {
        super();
    }
    
    @kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\rJ\u0010\u0010\u000e\u001a\u00020\u00002\b\u0010\u000f\u001a\u0004\u0018\u00010\u0006J\u0010\u0010\u0010\u001a\u00020\u00002\b\u0010\u000f\u001a\u0004\u0018\u00010\u0004J\u0010\u0010\u0011\u001a\u00020\u00002\b\u0010\u0012\u001a\u0004\u0018\u00010\bJ\u0010\u0010\u0013\u001a\u00020\u00002\b\u0010\t\u001a\u0004\u0018\u00010\u0006R\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0005\u001a\u0004\u0018\u00010\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0007\u001a\u0004\u0018\u00010\bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\t\u001a\u0004\u0018\u00010\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0014"}, d2 = {"Lcom/ekoapp/ekosdk/uikit/community/newsfeed/fragment/EkoEditCommentFragment$Builder;", "", "()V", "commentText", "", "ekoComment", "Lcom/ekoapp/ekosdk/comment/EkoComment;", "ekoPost", "Lcom/ekoapp/ekosdk/feed/EkoPost;", "reply", "build", "Lcom/ekoapp/ekosdk/uikit/community/newsfeed/fragment/EkoEditCommentFragment;", "activity", "Landroidx/appcompat/app/AppCompatActivity;", "setComment", "comment", "setCommentText", "setNewsFeed", "post", "setReplyTo", "community_debug"})
    public static final class Builder {
        private com.ekoapp.ekosdk.comment.EkoComment ekoComment;
        private com.ekoapp.ekosdk.feed.EkoPost ekoPost;
        private java.lang.String commentText;
        private com.ekoapp.ekosdk.comment.EkoComment reply;
        
        @org.jetbrains.annotations.NotNull()
        public final com.ekoapp.ekosdk.uikit.community.newsfeed.fragment.EkoEditCommentFragment build(@org.jetbrains.annotations.NotNull()
        androidx.appcompat.app.AppCompatActivity activity) {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.ekoapp.ekosdk.uikit.community.newsfeed.fragment.EkoEditCommentFragment.Builder setNewsFeed(@org.jetbrains.annotations.Nullable()
        com.ekoapp.ekosdk.feed.EkoPost post) {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.ekoapp.ekosdk.uikit.community.newsfeed.fragment.EkoEditCommentFragment.Builder setComment(@org.jetbrains.annotations.Nullable()
        com.ekoapp.ekosdk.comment.EkoComment comment) {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.ekoapp.ekosdk.uikit.community.newsfeed.fragment.EkoEditCommentFragment.Builder setReplyTo(@org.jetbrains.annotations.Nullable()
        com.ekoapp.ekosdk.comment.EkoComment reply) {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.ekoapp.ekosdk.uikit.community.newsfeed.fragment.EkoEditCommentFragment.Builder setCommentText(@org.jetbrains.annotations.Nullable()
        java.lang.String comment) {
            return null;
        }
        
        public Builder() {
            super();
        }
    }
}