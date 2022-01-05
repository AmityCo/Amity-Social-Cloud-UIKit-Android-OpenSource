package com.ekoapp.ekosdk.uikit.community.newsfeed.fragment;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\u00c0\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b!\b&\u0018\u00002\u00020\u00012\u00020\u00022\u00020\u00032\u00020\u00042\u00020\u0005B\u0005\u00a2\u0006\u0002\u0010\u0006J\u0010\u0010\u001a\u001a\u00020\u001b2\u0006\u0010\u001c\u001a\u00020\u000eH\u0002J\u0010\u0010\u001d\u001a\u00020\u001b2\u0006\u0010\u001e\u001a\u00020\u001fH\u0002J\b\u0010 \u001a\u00020!H&J\b\u0010\"\u001a\u00020#H&J\b\u0010$\u001a\u00020\u001bH\u0002J\u0006\u0010%\u001a\u00020&J\b\u0010\'\u001a\u00020(H&J\u0018\u0010)\u001a\u00020\u001b2\u0006\u0010*\u001a\u00020+2\u0006\u0010,\u001a\u00020\u000eH\u0002J\u0010\u0010-\u001a\u00020\u001b2\u0006\u0010.\u001a\u00020\u0012H\u0016J\u0018\u0010/\u001a\u00020\u001b2\u0006\u0010*\u001a\u00020+2\u0006\u00100\u001a\u00020\u001fH\u0002J\u0010\u00101\u001a\u00020\u001b2\u0006\u00102\u001a\u000203H\u0002J\b\u00104\u001a\u00020\u001bH\u0002J\b\u00105\u001a\u00020\u001bH\u0002J\b\u00106\u001a\u00020\u001bH\u0002J\b\u00107\u001a\u00020\u001bH\u0002J\u0010\u00108\u001a\u00020\u001b2\u0006\u0010\u001e\u001a\u00020\u001fH\u0016J\u0010\u00109\u001a\u00020\u001b2\u0006\u0010:\u001a\u00020;H\u0016J\u0010\u0010<\u001a\u00020\u001b2\u0006\u00102\u001a\u000203H\u0016J\u001e\u0010=\u001a\u00020\u001b2\f\u0010>\u001a\b\u0012\u0004\u0012\u00020@0?2\u0006\u0010A\u001a\u00020BH\u0016J\u0018\u0010C\u001a\u00020\u001b2\u0006\u0010D\u001a\u00020\b2\u0006\u0010A\u001a\u00020BH\u0016J \u0010E\u001a\u00020\u001b2\u0006\u00100\u001a\u00020\u001f2\u0006\u0010\u001c\u001a\u00020\u000e2\u0006\u0010A\u001a\u00020BH\u0016J&\u0010F\u001a\u0004\u0018\u00010!2\u0006\u0010G\u001a\u00020H2\b\u0010I\u001a\u0004\u0018\u00010&2\b\u0010J\u001a\u0004\u0018\u00010KH\u0016J\u0018\u0010L\u001a\u00020\u001b2\u0006\u00100\u001a\u00020\u001f2\u0006\u0010A\u001a\u00020BH\u0016J\u0016\u0010M\u001a\u00020\u001b2\f\u0010N\u001a\b\u0012\u0004\u0012\u00020\u001f0OH\u0002J \u0010P\u001a\u00020\u001b2\u0006\u0010Q\u001a\u00020\u00122\u0006\u0010R\u001a\u00020\u001f2\u0006\u0010A\u001a\u00020BH\u0016J\u0018\u0010S\u001a\u00020\u001b2\u0006\u0010R\u001a\u00020\u001f2\u0006\u0010A\u001a\u00020BH\u0016J\u001a\u0010T\u001a\u00020\u001b2\u0006\u0010U\u001a\u00020!2\b\u0010J\u001a\u0004\u0018\u00010KH\u0016J\r\u0010V\u001a\u00020\u001bH\u0000\u00a2\u0006\u0002\bWJ\b\u0010X\u001a\u00020\u001bH\u0002J\u0018\u0010Y\u001a\u00020\u001b2\u0006\u0010\u001c\u001a\u00020\u000e2\u0006\u0010Z\u001a\u00020\u0012H\u0002J\u0018\u0010[\u001a\u00020\u001b2\u0006\u00100\u001a\u00020\u001f2\u0006\u0010Z\u001a\u00020\u0012H\u0002J \u0010\\\u001a\u00020\u001b2\u0006\u00100\u001a\u00020\u001f2\u0006\u0010\u001c\u001a\u00020\u000e2\u0006\u0010A\u001a\u00020BH\u0016J\u0010\u0010]\u001a\u00020\u001b2\u0006\u0010,\u001a\u00020\u000eH\u0002J\u0010\u0010^\u001a\u00020\u001b2\u0006\u0010,\u001a\u00020\u000eH\u0002J\u0010\u0010_\u001a\u00020\u001b2\u0006\u0010,\u001a\u00020\u000eH\u0002J\b\u0010`\u001a\u00020\u001bH\u0002J\u0010\u0010a\u001a\u00020\u001b2\u0006\u0010\u001c\u001a\u00020\u000eH\u0002J\u0010\u0010b\u001a\u00020\u001b2\u0006\u0010\u001e\u001a\u00020\u001fH\u0002J\u0012\u0010c\u001a\u00020\u001b2\b\u0010d\u001a\u0004\u0018\u00010\bH\u0002J\u0010\u0010e\u001a\u00020\u001b2\u0006\u00100\u001a\u00020\u001fH\u0002J\u0010\u0010f\u001a\u00020\u001b2\u0006\u00100\u001a\u00020\u001fH\u0002J\u0010\u0010g\u001a\u00020\u001b2\u0006\u00100\u001a\u00020\u001fH\u0002J\u0010\u0010h\u001a\u00020\u001b2\u0006\u0010\u001e\u001a\u00020\u001fH\u0002J\u0012\u0010i\u001a\u00020\u001b2\b\u0010d\u001a\u0004\u0018\u00010\bH\u0002J\u0010\u0010j\u001a\u00020\u001b2\u0006\u0010Z\u001a\u00020\u0012H\u0002J\b\u0010k\u001a\u00020\u001bH\u0002J\b\u0010l\u001a\u00020\u001bH\u0002J\b\u0010m\u001a\u00020\u001bH\u0002J\u0010\u0010n\u001a\u00020\u001b2\u0006\u0010o\u001a\u00020BH\u0002R\u0016\u0010\u0007\u001a\n \t*\u0004\u0018\u00010\b0\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0082.\u00a2\u0006\u0002\n\u0000R\u001c\u0010\f\u001a\u0010\u0012\f\u0012\n \t*\u0004\u0018\u00010\u000e0\u000e0\rX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u000f\u001a\u0004\u0018\u00010\u0010X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u0012X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\u0014X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0015\u001a\u0004\u0018\u00010\u0010X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0016\u001a\u00020\u0017X\u0082.\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0018\u001a\u0004\u0018\u00010\u0019X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006p"}, d2 = {"Lcom/ekoapp/ekosdk/uikit/community/newsfeed/fragment/EkoBaseFeedFragment;", "Lcom/ekoapp/ekosdk/uikit/base/EkoBaseFragment;", "Lcom/ekoapp/ekosdk/uikit/community/newsfeed/listener/IPostImageClickListener;", "Lcom/ekoapp/ekosdk/uikit/community/newsfeed/listener/IPostItemActionListener;", "Lcom/ekoapp/ekosdk/uikit/community/newsfeed/adapter/EkoPostViewFileAdapter$ILoadMoreFilesClickListener;", "Lcom/ekoapp/ekosdk/uikit/community/newsfeed/listener/IPostFileItemClickListener;", "()V", "TAG", "", "kotlin.jvm.PlatformType", "adapter", "Lcom/ekoapp/ekosdk/uikit/community/newsfeed/adapter/EkoNewsFeedAdapter;", "editCommentContact", "Landroidx/activity/result/ActivityResultLauncher;", "Lcom/ekoapp/ekosdk/comment/EkoComment;", "feedDisposable", "Lio/reactivex/disposables/Disposable;", "isLoaded", "", "loadingDuration", "", "loadingTimerDisposable", "mBinding", "Lcom/ekoapp/ekosdk/uikit/community/databinding/AmityFragmentBaseFeedBinding;", "progressDialog", "Landroid/app/ProgressDialog;", "deleteComment", "", "comment", "deletePost", "post", "Lcom/ekoapp/ekosdk/feed/EkoPost;", "getEmptyView", "Landroid/view/View;", "getFeedType", "Lcom/ekoapp/ekosdk/uikit/community/newsfeed/util/EkoTimelineType;", "getFeeds", "getRootView", "Landroid/view/ViewGroup;", "getViewModel", "Lcom/ekoapp/ekosdk/uikit/community/newsfeed/viewmodel/EkoBaseFeedViewModel;", "handleCommentActionItemClick", "item", "Landroid/view/MenuItem;", "ekoComment", "handleEmptyList", "isListEmpty", "handleFeedActionItemClick", "feed", "handleOpenFile", "file", "Lcom/ekoapp/ekosdk/uikit/community/domain/model/FileAttachment;", "hideProgress", "initCreateFeed", "initNewsFeedAdapter", "initNewsFeedRecyclerView", "loadMoreFiles", "onClickCommunity", "community", "Lcom/ekoapp/ekosdk/community/EkoCommunity;", "onClickFileItem", "onClickImage", "images", "", "Lcom/ekoapp/ekosdk/file/EkoImage;", "position", "", "onClickItem", "postId", "onCommentAction", "onCreateView", "inflater", "Landroid/view/LayoutInflater;", "container", "savedInstanceState", "Landroid/os/Bundle;", "onFeedAction", "onFeedsLoaded", "result", "Landroidx/paging/PagedList;", "onLikeAction", "liked", "ekoPost", "onShareAction", "onViewCreated", "view", "refresh", "refresh$community_debug", "refreshGlobalFeed", "sendReportComment", "isReport", "sendReportPost", "showAllReply", "showCommentActionAdmin", "showCommentActionByOtherUser", "showCommentActionCommentOwner", "showConnectivityIssue", "showDeleteCommentWarning", "showDeletePostWarning", "showFailedToDeleteMessage", "message", "showFeedActionByAdmin", "showFeedActionByOtherUser", "showFeedActionByOwner", "showFeedShareAction", "showGetFeedErrorMessage", "showReportSentMessage", "startLoadingTimer", "stopLoadingTimer", "subscribeUiEvent", "updateProgress", "progress", "community_debug"})
public abstract class EkoBaseFeedFragment extends com.ekoapp.ekosdk.uikit.base.EkoBaseFragment implements com.ekoapp.ekosdk.uikit.community.newsfeed.listener.IPostImageClickListener, com.ekoapp.ekosdk.uikit.community.newsfeed.listener.IPostItemActionListener, com.ekoapp.ekosdk.uikit.community.newsfeed.adapter.EkoPostViewFileAdapter.ILoadMoreFilesClickListener, com.ekoapp.ekosdk.uikit.community.newsfeed.listener.IPostFileItemClickListener {
    private final java.lang.String TAG = null;
    private com.ekoapp.ekosdk.uikit.community.newsfeed.adapter.EkoNewsFeedAdapter adapter;
    private com.ekoapp.ekosdk.uikit.community.databinding.AmityFragmentBaseFeedBinding mBinding;
    private io.reactivex.disposables.Disposable feedDisposable;
    private boolean isLoaded = false;
    private io.reactivex.disposables.Disposable loadingTimerDisposable;
    private long loadingDuration = 0L;
    private android.app.ProgressDialog progressDialog;
    private androidx.activity.result.ActivityResultLauncher<com.ekoapp.ekosdk.comment.EkoComment> editCommentContact;
    private java.util.HashMap _$_findViewCache;
    
    @org.jetbrains.annotations.Nullable()
    @java.lang.Override()
    public android.view.View onCreateView(@org.jetbrains.annotations.NotNull()
    android.view.LayoutInflater inflater, @org.jetbrains.annotations.Nullable()
    android.view.ViewGroup container, @org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final android.view.ViewGroup getRootView() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public abstract com.ekoapp.ekosdk.uikit.community.newsfeed.util.EkoTimelineType getFeedType();
    
    @java.lang.Override()
    public void onViewCreated(@org.jetbrains.annotations.NotNull()
    android.view.View view, @org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    private final void subscribeUiEvent() {
    }
    
    @org.jetbrains.annotations.NotNull()
    public abstract com.ekoapp.ekosdk.uikit.community.newsfeed.viewmodel.EkoBaseFeedViewModel getViewModel();
    
    public final void refresh$community_debug() {
    }
    
    private final void getFeeds() {
    }
    
    private final void startLoadingTimer() {
    }
    
    private final void stopLoadingTimer() {
    }
    
    private final void onFeedsLoaded(androidx.paging.PagedList<com.ekoapp.ekosdk.feed.EkoPost> result) {
    }
    
    private final void showGetFeedErrorMessage(java.lang.String message) {
    }
    
    public void handleEmptyList(boolean isListEmpty) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public abstract android.view.View getEmptyView();
    
    private final void initCreateFeed() {
    }
    
    private final void initNewsFeedAdapter() {
    }
    
    private final void initNewsFeedRecyclerView() {
    }
    
    @java.lang.Override()
    public void loadMoreFiles(@org.jetbrains.annotations.NotNull()
    com.ekoapp.ekosdk.feed.EkoPost post) {
    }
    
    @java.lang.Override()
    public void onClickImage(@org.jetbrains.annotations.NotNull()
    java.util.List<com.ekoapp.ekosdk.file.EkoImage> images, int position) {
    }
    
    @java.lang.Override()
    public void onFeedAction(@org.jetbrains.annotations.NotNull()
    com.ekoapp.ekosdk.feed.EkoPost feed, int position) {
    }
    
    @java.lang.Override()
    public void onCommentAction(@org.jetbrains.annotations.NotNull()
    com.ekoapp.ekosdk.feed.EkoPost feed, @org.jetbrains.annotations.NotNull()
    com.ekoapp.ekosdk.comment.EkoComment comment, int position) {
    }
    
    @java.lang.Override()
    public void onShareAction(@org.jetbrains.annotations.NotNull()
    com.ekoapp.ekosdk.feed.EkoPost ekoPost, int position) {
    }
    
    private final void showFeedShareAction(com.ekoapp.ekosdk.feed.EkoPost post) {
    }
    
    @java.lang.Override()
    public void showAllReply(@org.jetbrains.annotations.NotNull()
    com.ekoapp.ekosdk.feed.EkoPost feed, @org.jetbrains.annotations.NotNull()
    com.ekoapp.ekosdk.comment.EkoComment comment, int position) {
    }
    
    @java.lang.Override()
    public void onClickItem(@org.jetbrains.annotations.NotNull()
    java.lang.String postId, int position) {
    }
    
    @java.lang.Override()
    public void onClickFileItem(@org.jetbrains.annotations.NotNull()
    com.ekoapp.ekosdk.uikit.community.domain.model.FileAttachment file) {
    }
    
    @java.lang.Override()
    public void onClickCommunity(@org.jetbrains.annotations.NotNull()
    com.ekoapp.ekosdk.community.EkoCommunity community) {
    }
    
    private final void showFeedActionByOwner(com.ekoapp.ekosdk.feed.EkoPost feed) {
    }
    
    private final void showFeedActionByOtherUser(com.ekoapp.ekosdk.feed.EkoPost feed) {
    }
    
    private final void showFeedActionByAdmin(com.ekoapp.ekosdk.feed.EkoPost feed) {
    }
    
    private final void handleFeedActionItemClick(android.view.MenuItem item, com.ekoapp.ekosdk.feed.EkoPost feed) {
    }
    
    private final void sendReportPost(com.ekoapp.ekosdk.feed.EkoPost feed, boolean isReport) {
    }
    
    private final void showReportSentMessage(boolean isReport) {
    }
    
    private final void deletePost(com.ekoapp.ekosdk.feed.EkoPost post) {
    }
    
    private final void showConnectivityIssue() {
    }
    
    private final void refreshGlobalFeed() {
    }
    
    private final void showFailedToDeleteMessage(java.lang.String message) {
    }
    
    private final void showDeletePostWarning(com.ekoapp.ekosdk.feed.EkoPost post) {
    }
    
    private final void showCommentActionCommentOwner(com.ekoapp.ekosdk.comment.EkoComment ekoComment) {
    }
    
    private final void showCommentActionByOtherUser(com.ekoapp.ekosdk.comment.EkoComment ekoComment) {
    }
    
    private final void showCommentActionAdmin(com.ekoapp.ekosdk.comment.EkoComment ekoComment) {
    }
    
    private final void handleCommentActionItemClick(android.view.MenuItem item, com.ekoapp.ekosdk.comment.EkoComment ekoComment) {
    }
    
    private final void sendReportComment(com.ekoapp.ekosdk.comment.EkoComment comment, boolean isReport) {
    }
    
    private final void showDeleteCommentWarning(com.ekoapp.ekosdk.comment.EkoComment comment) {
    }
    
    private final void deleteComment(com.ekoapp.ekosdk.comment.EkoComment comment) {
    }
    
    private final void handleOpenFile(com.ekoapp.ekosdk.uikit.community.domain.model.FileAttachment file) {
    }
    
    @java.lang.Override()
    public void onLikeAction(boolean liked, @org.jetbrains.annotations.NotNull()
    com.ekoapp.ekosdk.feed.EkoPost ekoPost, int position) {
    }
    
    private final void hideProgress() {
    }
    
    private final void updateProgress(int progress) {
    }
    
    public EkoBaseFeedFragment() {
        super();
    }
}