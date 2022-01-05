package com.ekoapp.ekosdk.uikit.community.newsfeed.fragment;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\u00cc\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0002\b\u0015\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u001d\n\u0002\u0018\u0002\n\u0002\b\u0007\u0018\u00002\u00020\u00012\u00020\u00022\u00020\u00032\u00020\u00042\u00020\u00052\u00020\u00062\u00020\u00072\u00020\b:\u0002\u0085\u0001B\u0007\b\u0000\u00a2\u0006\u0002\u0010\tJ\u0010\u00103\u001a\u0002042\u0006\u00105\u001a\u00020\u0018H\u0002J\b\u00106\u001a\u000204H\u0002J\u0012\u00107\u001a\u0002042\b\u00108\u001a\u0004\u0018\u00010\rH\u0002J\u0018\u00109\u001a\u0002042\u0006\u0010:\u001a\u00020#2\u0006\u0010;\u001a\u00020\u0018H\u0002J\u0010\u0010<\u001a\u0002042\u0006\u0010:\u001a\u00020#H\u0002J\b\u0010=\u001a\u000204H\u0002J\b\u0010>\u001a\u000204H\u0002J\b\u0010?\u001a\u000204H\u0002J\b\u0010@\u001a\u000204H\u0002J\u0010\u0010A\u001a\u0002042\u0006\u0010B\u001a\u00020!H\u0002J\b\u0010C\u001a\u000204H\u0002J\b\u0010D\u001a\u000204H\u0002J\b\u0010E\u001a\u000204H\u0002J\b\u0010F\u001a\u000204H\u0002J\u0010\u0010G\u001a\u00020!2\u0006\u00105\u001a\u00020\u0018H\u0002J\u0010\u0010H\u001a\u0002042\u0006\u0010I\u001a\u00020JH\u0016J\u0018\u0010K\u001a\u0002042\u0006\u00105\u001a\u00020\u00182\u0006\u0010L\u001a\u00020\u000bH\u0016J\u0010\u0010M\u001a\u0002042\u0006\u0010N\u001a\u00020OH\u0016J\u001e\u0010P\u001a\u0002042\f\u0010Q\u001a\b\u0012\u0004\u0012\u00020S0R2\u0006\u0010L\u001a\u00020\u000bH\u0016J\u0018\u0010T\u001a\u0002042\u0006\u00105\u001a\u00020\u00182\u0006\u0010L\u001a\u00020\u000bH\u0016J\u0018\u0010U\u001a\u0002042\u0006\u00105\u001a\u00020\u00182\u0006\u0010L\u001a\u00020\u000bH\u0016J\u0012\u0010V\u001a\u0002042\b\u0010W\u001a\u0004\u0018\u00010\u001eH\u0016J\u0018\u0010X\u001a\u0002042\u0006\u0010Y\u001a\u00020Z2\u0006\u0010[\u001a\u00020\\H\u0016J$\u0010]\u001a\u00020^2\u0006\u0010[\u001a\u00020_2\b\u0010`\u001a\u0004\u0018\u00010a2\b\u0010W\u001a\u0004\u0018\u00010\u001eH\u0016J\b\u0010b\u001a\u000204H\u0016J\u0010\u0010c\u001a\u0002042\u0006\u0010d\u001a\u00020!H\u0016J\u0010\u0010e\u001a\u00020!2\u0006\u0010:\u001a\u00020#H\u0016J\b\u0010f\u001a\u000204H\u0016J\u001a\u0010g\u001a\u0002042\u0006\u0010h\u001a\u00020^2\b\u0010W\u001a\u0004\u0018\u00010\u001eH\u0016J\b\u0010i\u001a\u000204H\u0002J\u0018\u0010j\u001a\u0002042\u0006\u00105\u001a\u00020\u00182\u0006\u0010k\u001a\u00020!H\u0002J\u0018\u0010l\u001a\u0002042\u0006\u0010m\u001a\u00020%2\u0006\u0010k\u001a\u00020!H\u0002J\u0010\u0010n\u001a\u00020\u000b2\u0006\u0010;\u001a\u00020\u0018H\u0002J\u0010\u0010o\u001a\u00020\u000b2\u0006\u0010;\u001a\u00020\u0018H\u0002J\u0010\u0010p\u001a\u0002042\u0006\u0010I\u001a\u00020JH\u0002J\b\u0010q\u001a\u000204H\u0002J\u0010\u0010r\u001a\u0002042\u0006\u0010;\u001a\u00020\u0018H\u0002J\u0010\u0010s\u001a\u0002042\u0006\u0010;\u001a\u00020\u0018H\u0002J\u0010\u0010t\u001a\u0002042\u0006\u0010;\u001a\u00020\u0018H\u0002J\u0010\u0010u\u001a\u0002042\u0006\u00105\u001a\u00020\u0018H\u0002J\b\u0010v\u001a\u000204H\u0002J\u0012\u0010w\u001a\u0002042\b\u0010x\u001a\u0004\u0018\u00010\rH\u0002J\b\u0010y\u001a\u000204H\u0002J\u0010\u0010z\u001a\u0002042\u0006\u0010m\u001a\u00020%H\u0002J\u0010\u0010{\u001a\u0002042\u0006\u0010m\u001a\u00020%H\u0002J\u0010\u0010|\u001a\u0002042\u0006\u0010m\u001a\u00020%H\u0002J\b\u0010B\u001a\u00020!H\u0002J\u0016\u0010}\u001a\u0002042\f\u0010~\u001a\b\u0012\u0004\u0012\u00020\u00180\u007fH\u0002J\t\u0010\u0080\u0001\u001a\u000204H\u0002J\u0011\u0010\u0081\u0001\u001a\u0002042\u0006\u0010k\u001a\u00020!H\u0002J\t\u0010\u0082\u0001\u001a\u000204H\u0002J\u0012\u0010\u0083\u0001\u001a\u0002042\u0007\u0010\u0084\u0001\u001a\u00020\u000bH\u0002R\u000e\u0010\n\u001a\u00020\u000bX\u0082D\u00a2\u0006\u0002\n\u0000R\u0016\u0010\f\u001a\n \u000e*\u0004\u0018\u00010\r0\rX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u000f\u001a\u00020\u0010X\u0086.\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0011\u0010\u0012\"\u0004\b\u0013\u0010\u0014R\u0012\u0010\u0015\u001a\u0004\u0018\u00010\u000bX\u0082\u000e\u00a2\u0006\u0004\n\u0002\u0010\u0016R\u0010\u0010\u0017\u001a\u0004\u0018\u00010\u0018X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0019\u001a\u00020\u001aX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001c\u0010\u001b\u001a\u0010\u0012\f\u0012\n \u000e*\u0004\u0018\u00010\u00180\u00180\u001cX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001c\u0010\u001d\u001a\u0010\u0012\f\u0012\n \u000e*\u0004\u0018\u00010\u001e0\u001e0\u001cX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u001f\u001a\u0004\u0018\u00010\rX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010 \u001a\u00020!X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\"\u001a\u0004\u0018\u00010#X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010$\u001a\u00020%X\u0082.\u00a2\u0006\u0002\n\u0000R\u0010\u0010&\u001a\u0004\u0018\u00010\'X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001c\u0010(\u001a\u0010\u0012\f\u0012\n \u000e*\u0004\u0018\u00010%0%0\u001cX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010)\u001a\u0004\u0018\u00010*X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010+\u001a\u0004\u0018\u00010\u0018X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010,\u001a\u00020!X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001a\u0010-\u001a\u00020.X\u0086.\u00a2\u0006\u000e\n\u0000\u001a\u0004\b/\u00100\"\u0004\b1\u00102\u00a8\u0006\u0086\u0001"}, d2 = {"Lcom/ekoapp/ekosdk/uikit/community/newsfeed/fragment/EkoPostDetailFragment;", "Lcom/ekoapp/ekosdk/uikit/base/EkoBaseFragment;", "Lcom/ekoapp/ekosdk/uikit/community/newsfeed/listener/IPostImageClickListener;", "Lcom/ekoapp/ekosdk/uikit/community/newsfeed/listener/IPostCommentShowMoreActionListener;", "Lcom/ekoapp/ekosdk/uikit/community/newsfeed/listener/IPostActionShareListener;", "Lcom/ekoapp/ekosdk/uikit/community/newsfeed/listener/IPostCommentItemClickListener;", "Lcom/ekoapp/ekosdk/uikit/community/newsfeed/listener/IPostActionLikeListener;", "Lcom/ekoapp/ekosdk/uikit/community/newsfeed/listener/IPostFileItemClickListener;", "Lcom/ekoapp/ekosdk/uikit/community/newsfeed/listener/IPostCommentReplyClickListener;", "()V", "ID_MENU_ITEM", "", "TAG", "", "kotlin.jvm.PlatformType", "binding", "Lcom/ekoapp/ekosdk/uikit/community/databinding/AmityFragmentPostDetailBinding;", "getBinding", "()Lcom/ekoapp/ekosdk/uikit/community/databinding/AmityFragmentPostDetailBinding;", "setBinding", "(Lcom/ekoapp/ekosdk/uikit/community/databinding/AmityFragmentPostDetailBinding;)V", "commentActionIndex", "Ljava/lang/Integer;", "commentToExpand", "Lcom/ekoapp/ekosdk/comment/EkoComment;", "disposal", "Lio/reactivex/disposables/CompositeDisposable;", "editCommentContact", "Landroidx/activity/result/ActivityResultLauncher;", "ekoAddCommentContract", "Landroid/os/Bundle;", "feedId", "isViewInit", "", "menuItem", "Landroid/view/MenuItem;", "newsFeed", "Lcom/ekoapp/ekosdk/feed/EkoPost;", "postDetailAdapter", "Lcom/ekoapp/ekosdk/uikit/community/newsfeed/adapter/EkoPostDetailAdapter;", "postEditContact", "progressDialog", "Landroid/app/ProgressDialog;", "replyClicked", "scrollRequired", "viewModel", "Lcom/ekoapp/ekosdk/uikit/community/newsfeed/viewmodel/EkoPostDetailsViewModel;", "getViewModel", "()Lcom/ekoapp/ekosdk/uikit/community/newsfeed/viewmodel/EkoPostDetailsViewModel;", "setViewModel", "(Lcom/ekoapp/ekosdk/uikit/community/newsfeed/viewmodel/EkoPostDetailsViewModel;)V", "deleteComment", "", "comment", "deletePost", "getPostDetails", "id", "handleCommentActionItemClick", "item", "ekoComment", "handleFeedActionItemClick", "hideProgress", "hideReplyTo", "initCommentView", "initEkoPostCommentRecyclerview", "initFeedDetails", "showFooter", "initPostDetailsViewButton", "initUserData", "initView", "initialListener", "isReplyComment", "onClickAvatar", "user", "Lcom/ekoapp/ekosdk/user/EkoUser;", "onClickCommentReply", "position", "onClickFileItem", "file", "Lcom/ekoapp/ekosdk/uikit/community/domain/model/FileAttachment;", "onClickImage", "images", "", "Lcom/ekoapp/ekosdk/file/EkoImage;", "onClickItem", "onClickNewsFeedCommentShowMoreAction", "onCreate", "savedInstanceState", "onCreateOptionsMenu", "menu", "Landroid/view/Menu;", "inflater", "Landroid/view/MenuInflater;", "onCreateView", "Landroid/view/View;", "Landroid/view/LayoutInflater;", "container", "Landroid/view/ViewGroup;", "onDestroy", "onLikeAction", "liked", "onOptionsItemSelected", "onShareAction", "onViewCreated", "view", "openKeyBoardToAddComment", "sendReportComment", "isReport", "sendReportPost", "feed", "setCommentActionMenuAdmin", "setCommentActionMenuByOtherUser", "setImageUserCommentComposeBar", "setupViewReadOnlyMode", "showCommentActionAdmin", "showCommentActionByOtherUser", "showCommentActionCommentOwner", "showDeleteCommentWarning", "showDeletePostWarning", "showErrorMessage", "message", "showFeedAction", "showFeedActionByAdmin", "showFeedActionByOtherUser", "showFeedActionByOwner", "showLoadingComment", "commentList", "Landroidx/paging/PagedList;", "showReplyTo", "showReportSentMessage", "subscribeUiEvent", "updateProgress", "progress", "Builder", "community_debug"})
public final class EkoPostDetailFragment extends com.ekoapp.ekosdk.uikit.base.EkoBaseFragment implements com.ekoapp.ekosdk.uikit.community.newsfeed.listener.IPostImageClickListener, com.ekoapp.ekosdk.uikit.community.newsfeed.listener.IPostCommentShowMoreActionListener, com.ekoapp.ekosdk.uikit.community.newsfeed.listener.IPostActionShareListener, com.ekoapp.ekosdk.uikit.community.newsfeed.listener.IPostCommentItemClickListener, com.ekoapp.ekosdk.uikit.community.newsfeed.listener.IPostActionLikeListener, com.ekoapp.ekosdk.uikit.community.newsfeed.listener.IPostFileItemClickListener, com.ekoapp.ekosdk.uikit.community.newsfeed.listener.IPostCommentReplyClickListener {
    private final java.lang.String TAG = null;
    private final int ID_MENU_ITEM = 222;
    private com.ekoapp.ekosdk.feed.EkoPost newsFeed;
    @org.jetbrains.annotations.NotNull()
    public com.ekoapp.ekosdk.uikit.community.newsfeed.viewmodel.EkoPostDetailsViewModel viewModel;
    @org.jetbrains.annotations.NotNull()
    public com.ekoapp.ekosdk.uikit.community.databinding.AmityFragmentPostDetailBinding binding;
    private io.reactivex.disposables.CompositeDisposable disposal;
    private com.ekoapp.ekosdk.comment.EkoComment commentToExpand;
    private java.lang.String feedId;
    private java.lang.Integer commentActionIndex;
    private android.view.MenuItem menuItem;
    private com.ekoapp.ekosdk.comment.EkoComment replyClicked;
    private com.ekoapp.ekosdk.uikit.community.newsfeed.adapter.EkoPostDetailAdapter postDetailAdapter;
    private boolean isViewInit = false;
    private boolean scrollRequired = false;
    private android.app.ProgressDialog progressDialog;
    private androidx.activity.result.ActivityResultLauncher<com.ekoapp.ekosdk.feed.EkoPost> postEditContact;
    private androidx.activity.result.ActivityResultLauncher<com.ekoapp.ekosdk.comment.EkoComment> editCommentContact;
    private androidx.activity.result.ActivityResultLauncher<android.os.Bundle> ekoAddCommentContract;
    private java.util.HashMap _$_findViewCache;
    
    @org.jetbrains.annotations.NotNull()
    public final com.ekoapp.ekosdk.uikit.community.newsfeed.viewmodel.EkoPostDetailsViewModel getViewModel() {
        return null;
    }
    
    public final void setViewModel(@org.jetbrains.annotations.NotNull()
    com.ekoapp.ekosdk.uikit.community.newsfeed.viewmodel.EkoPostDetailsViewModel p0) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.ekoapp.ekosdk.uikit.community.databinding.AmityFragmentPostDetailBinding getBinding() {
        return null;
    }
    
    public final void setBinding(@org.jetbrains.annotations.NotNull()
    com.ekoapp.ekosdk.uikit.community.databinding.AmityFragmentPostDetailBinding p0) {
    }
    
    @java.lang.Override()
    public void onCreate(@org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    @org.jetbrains.annotations.NotNull()
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
    
    @java.lang.Override()
    public void onDestroy() {
    }
    
    private final void initialListener() {
    }
    
    private final void initEkoPostCommentRecyclerview() {
    }
    
    private final void showLoadingComment(androidx.paging.PagedList<com.ekoapp.ekosdk.comment.EkoComment> commentList) {
    }
    
    private final void getPostDetails(java.lang.String id) {
    }
    
    private final void showErrorMessage(java.lang.String message) {
    }
    
    private final void initCommentView() {
    }
    
    private final boolean showFooter() {
        return false;
    }
    
    private final void initView() {
    }
    
    private final void hideReplyTo() {
    }
    
    private final void showReplyTo() {
    }
    
    private final void setupViewReadOnlyMode() {
    }
    
    private final void initUserData() {
    }
    
    private final void setImageUserCommentComposeBar(com.ekoapp.ekosdk.user.EkoUser user) {
    }
    
    private final void initPostDetailsViewButton() {
    }
    
    private final void initFeedDetails(boolean showFooter) {
    }
    
    private final void openKeyBoardToAddComment() {
    }
    
    private final void showFeedAction() {
    }
    
    private final void handleFeedActionItemClick(android.view.MenuItem item) {
    }
    
    private final void showDeletePostWarning() {
    }
    
    private final void showDeleteCommentWarning(com.ekoapp.ekosdk.comment.EkoComment comment) {
    }
    
    private final void deleteComment(com.ekoapp.ekosdk.comment.EkoComment comment) {
    }
    
    private final void deletePost() {
    }
    
    private final void sendReportComment(com.ekoapp.ekosdk.comment.EkoComment comment, boolean isReport) {
    }
    
    private final void sendReportPost(com.ekoapp.ekosdk.feed.EkoPost feed, boolean isReport) {
    }
    
    private final void showReportSentMessage(boolean isReport) {
    }
    
    @java.lang.Override()
    public void onClickImage(@org.jetbrains.annotations.NotNull()
    java.util.List<com.ekoapp.ekosdk.file.EkoImage> images, int position) {
    }
    
    @java.lang.Override()
    public void onClickItem(@org.jetbrains.annotations.NotNull()
    com.ekoapp.ekosdk.comment.EkoComment comment, int position) {
    }
    
    @java.lang.Override()
    public void onClickAvatar(@org.jetbrains.annotations.NotNull()
    com.ekoapp.ekosdk.user.EkoUser user) {
    }
    
    @java.lang.Override()
    public void onClickNewsFeedCommentShowMoreAction(@org.jetbrains.annotations.NotNull()
    com.ekoapp.ekosdk.comment.EkoComment comment, int position) {
    }
    
    @java.lang.Override()
    public void onClickCommentReply(@org.jetbrains.annotations.NotNull()
    com.ekoapp.ekosdk.comment.EkoComment comment, int position) {
    }
    
    private final void subscribeUiEvent() {
    }
    
    private final void showCommentActionAdmin(com.ekoapp.ekosdk.comment.EkoComment ekoComment) {
    }
    
    private final int setCommentActionMenuAdmin(com.ekoapp.ekosdk.comment.EkoComment ekoComment) {
        return 0;
    }
    
    private final void showFeedActionByOwner(com.ekoapp.ekosdk.feed.EkoPost feed) {
    }
    
    private final void showFeedActionByOtherUser(com.ekoapp.ekosdk.feed.EkoPost feed) {
    }
    
    private final void showFeedActionByAdmin(com.ekoapp.ekosdk.feed.EkoPost feed) {
    }
    
    private final void showCommentActionCommentOwner(com.ekoapp.ekosdk.comment.EkoComment ekoComment) {
    }
    
    private final void showCommentActionByOtherUser(com.ekoapp.ekosdk.comment.EkoComment ekoComment) {
    }
    
    private final int setCommentActionMenuByOtherUser(com.ekoapp.ekosdk.comment.EkoComment ekoComment) {
        return 0;
    }
    
    private final boolean isReplyComment(com.ekoapp.ekosdk.comment.EkoComment comment) {
        return false;
    }
    
    private final void handleCommentActionItemClick(android.view.MenuItem item, com.ekoapp.ekosdk.comment.EkoComment ekoComment) {
    }
    
    @java.lang.Override()
    public void onLikeAction(boolean liked) {
    }
    
    @java.lang.Override()
    public void onShareAction() {
    }
    
    @java.lang.Override()
    public void onClickFileItem(@org.jetbrains.annotations.NotNull()
    com.ekoapp.ekosdk.uikit.community.domain.model.FileAttachment file) {
    }
    
    @java.lang.Override()
    public void onCreateOptionsMenu(@org.jetbrains.annotations.NotNull()
    android.view.Menu menu, @org.jetbrains.annotations.NotNull()
    android.view.MenuInflater inflater) {
    }
    
    @java.lang.Override()
    public boolean onOptionsItemSelected(@org.jetbrains.annotations.NotNull()
    android.view.MenuItem item) {
        return false;
    }
    
    private final void hideProgress() {
    }
    
    private final void updateProgress(int progress) {
    }
    
    public EkoPostDetailFragment() {
        super();
    }
    
    @kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000eJ\u0010\u0010\u000f\u001a\u00020\u00002\u0006\u0010\u0005\u001a\u00020\u0006H\u0002J\u000e\u0010\u0010\u001a\u00020\u00002\u0006\u0010\u0011\u001a\u00020\u0004J\u000e\u0010\u0012\u001a\u00020\u00002\u0006\u0010\u0012\u001a\u00020\u0013J\u000e\u0010\u0007\u001a\u00020\u00002\u0006\u0010\u0007\u001a\u00020\bJ\u000e\u0010\t\u001a\u00020\u00002\u0006\u0010\u0014\u001a\u00020\nR\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0005\u001a\u0004\u0018\u00010\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0007\u001a\u0004\u0018\u00010\bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\t\u001a\u0004\u0018\u00010\nX\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0015"}, d2 = {"Lcom/ekoapp/ekosdk/uikit/community/newsfeed/fragment/EkoPostDetailFragment$Builder;", "", "()V", "avatarClickListener", "Lcom/ekoapp/ekosdk/uikit/community/newsfeed/listener/IAvatarClickListener;", "comment", "Lcom/ekoapp/ekosdk/comment/EkoComment;", "postId", "", "postShareClickListener", "Lcom/ekoapp/ekosdk/uikit/feed/settings/IPostShareClickListener;", "build", "Lcom/ekoapp/ekosdk/uikit/community/newsfeed/fragment/EkoPostDetailFragment;", "activity", "Landroidx/appcompat/app/AppCompatActivity;", "commentToExpand", "onClickUserAvatar", "onAvatarClickListener", "post", "Lcom/ekoapp/ekosdk/feed/EkoPost;", "onPostShareClickListener", "community_debug"})
    public static final class Builder {
        private java.lang.String postId;
        private com.ekoapp.ekosdk.comment.EkoComment comment;
        private com.ekoapp.ekosdk.uikit.community.newsfeed.listener.IAvatarClickListener avatarClickListener;
        private com.ekoapp.ekosdk.uikit.feed.settings.IPostShareClickListener postShareClickListener;
        
        @org.jetbrains.annotations.NotNull()
        public final com.ekoapp.ekosdk.uikit.community.newsfeed.fragment.EkoPostDetailFragment build(@org.jetbrains.annotations.NotNull()
        androidx.appcompat.app.AppCompatActivity activity) {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.ekoapp.ekosdk.uikit.community.newsfeed.fragment.EkoPostDetailFragment.Builder postId(@org.jetbrains.annotations.NotNull()
        java.lang.String postId) {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.ekoapp.ekosdk.uikit.community.newsfeed.fragment.EkoPostDetailFragment.Builder post(@org.jetbrains.annotations.NotNull()
        com.ekoapp.ekosdk.feed.EkoPost post) {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.ekoapp.ekosdk.uikit.community.newsfeed.fragment.EkoPostDetailFragment.Builder onClickUserAvatar(@org.jetbrains.annotations.NotNull()
        com.ekoapp.ekosdk.uikit.community.newsfeed.listener.IAvatarClickListener onAvatarClickListener) {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.ekoapp.ekosdk.uikit.community.newsfeed.fragment.EkoPostDetailFragment.Builder postShareClickListener(@org.jetbrains.annotations.NotNull()
        com.ekoapp.ekosdk.uikit.feed.settings.IPostShareClickListener onPostShareClickListener) {
            return null;
        }
        
        private final com.ekoapp.ekosdk.uikit.community.newsfeed.fragment.EkoPostDetailFragment.Builder commentToExpand(com.ekoapp.ekosdk.comment.EkoComment comment) {
            return null;
        }
        
        public Builder() {
            super();
        }
    }
}