package com.ekoapp.ekosdk.uikit.community.newsfeed.viewmodel;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\u00a2\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010!\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\r\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010 \n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u001f\n\u0002\u0018\u0002\n\u0002\b\b\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u001a\u0010/\u001a\b\u0012\u0004\u0012\u00020\u00150\r2\f\u00100\u001a\b\u0012\u0004\u0012\u00020\u00150\rJ\u001a\u00101\u001a\b\u0012\u0004\u0012\u00020\u0017022\f\u00103\u001a\b\u0012\u0004\u0012\u00020402J\u0012\u00105\u001a\u0002062\b\u00107\u001a\u0004\u0018\u00010\u0004H\u0002J\u0014\u00108\u001a\b\u0012\u0004\u0012\u00020\u001c092\u0006\u0010\"\u001a\u00020\u0004J\u0016\u0010:\u001a\b\u0012\u0004\u0012\u00020\u001c092\u0006\u0010\"\u001a\u00020\u0004H\u0002J$\u0010;\u001a\b\u0012\u0004\u0012\u00020\u001c092\u0006\u0010\"\u001a\u00020\u00042\f\u0010<\u001a\b\u0012\u0004\u0012\u00020,02H\u0002J$\u0010=\u001a\b\u0012\u0004\u0012\u00020\u001c092\u0006\u0010\"\u001a\u00020\u00042\f\u00103\u001a\b\u0012\u0004\u0012\u00020.02H\u0002J\u0006\u0010>\u001a\u00020?J\u0006\u0010@\u001a\u000206J\u0012\u0010A\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00150\r0\u0019J\u0012\u0010B\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00170\r0\u0019J\b\u0010C\u001a\u0004\u0018\u00010\u001cJ\u0014\u0010D\u001a\b\u0012\u0004\u0012\u00020\u001c0E2\u0006\u0010F\u001a\u00020\u0004J\u0012\u0010G\u001a\u0004\u0018\u00010,2\u0006\u0010\u001b\u001a\u00020\u001cH\u0002J\u0012\u0010H\u001a\u0004\u0018\u00010.2\u0006\u0010\u001b\u001a\u00020\u001cH\u0002J\u0012\u0010$\u001a\u0004\u0018\u00010#2\u0006\u0010\u001b\u001a\u00020\u001cH\u0002J\u0006\u0010I\u001a\u00020)J\u0006\u0010J\u001a\u00020)J\u0006\u0010K\u001a\u00020)J\u0006\u0010L\u001a\u00020)J\b\u0010M\u001a\u00020)H\u0002J\b\u0010N\u001a\u00020)H\u0002J\u0006\u0010O\u001a\u00020)J\u0006\u0010P\u001a\u00020)J\u0006\u0010Q\u001a\u00020)J\u000e\u0010R\u001a\u00020)2\u0006\u0010\"\u001a\u00020\u0004J\u0010\u0010S\u001a\u00020)2\u0006\u0010T\u001a\u00020\u0015H\u0002J\u0010\u0010U\u001a\u00020\u00152\u0006\u0010V\u001a\u00020,H\u0002J\u0018\u0010U\u001a\u00020\u00152\u0006\u0010T\u001a\u00020\u00152\u0006\u0010V\u001a\u00020,H\u0002J\u0010\u0010W\u001a\u00020\u00172\u0006\u0010X\u001a\u00020.H\u0002J\u000e\u0010Y\u001a\u0002062\u0006\u0010Z\u001a\u00020\u0015J\u000e\u0010[\u001a\u0002062\u0006\u0010\\\u001a\u00020\u0017J\u000e\u0010]\u001a\u0002062\u0006\u0010\u001b\u001a\u00020\u001cJ\u0010\u0010^\u001a\u0002062\u0006\u0010_\u001a\u00020\u001cH\u0002J\b\u0010`\u001a\u000206H\u0002J\b\u0010a\u001a\u000206H\u0002J\b\u0010b\u001a\u000206H\u0002J\u001c\u0010c\u001a\u0002062\u0006\u0010T\u001a\u00020\u00152\f\u0010d\u001a\b\u0012\u0004\u0012\u00020,0eJ\u001c\u0010f\u001a\u0002062\u0006\u0010\\\u001a\u00020\u00172\f\u0010g\u001a\b\u0012\u0004\u0012\u00020.0eJ\u0010\u0010h\u001a\u0002062\u0006\u0010T\u001a\u00020\u0015H\u0002J\u0010\u0010h\u001a\u0002062\u0006\u0010\\\u001a\u00020\u0017H\u0002J\u000e\u0010i\u001a\u00020?2\u0006\u0010\"\u001a\u00020\u0004J\u001a\u0010j\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020,0e0E2\u0006\u0010k\u001a\u00020\u0015J\u001a\u0010l\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020.0e0E2\u0006\u0010\\\u001a\u00020\u0017R\u0016\u0010\u0003\u001a\n \u0005*\u0004\u0018\u00010\u00040\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001c\u0010\u0006\u001a\u0004\u0018\u00010\u0007X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\b\u0010\t\"\u0004\b\n\u0010\u000bR\u0014\u0010\f\u001a\b\u0012\u0004\u0012\u00020\u00040\rX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u00040\rX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0010X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u0012X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u0013\u001a\u000e\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u00150\u0014X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u0016\u001a\u000e\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u00170\u0014X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u0018\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00150\r0\u0019X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u001a\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00170\r0\u0019X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u001b\u001a\u0004\u0018\u00010\u001cX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001c\u0010\u001d\u001a\u0004\u0018\u00010\u0004X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u001e\u0010\u001f\"\u0004\b \u0010!R\u001c\u0010\"\u001a\u0004\u0018\u00010#X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b$\u0010%\"\u0004\b&\u0010\'R\u001a\u0010(\u001a\u000e\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020)0\u0014X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010*\u001a\u000e\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020)0\u0014X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010+\u001a\u000e\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020,0\u0014X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010-\u001a\u000e\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020.0\u0014X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006m"}, d2 = {"Lcom/ekoapp/ekosdk/uikit/community/newsfeed/viewmodel/EkoCreatePostViewModel;", "Lcom/ekoapp/ekosdk/uikit/base/EkoBaseViewModel;", "()V", "TAG", "", "kotlin.jvm.PlatformType", "community", "Lcom/ekoapp/ekosdk/community/EkoCommunity;", "getCommunity", "()Lcom/ekoapp/ekosdk/community/EkoCommunity;", "setCommunity", "(Lcom/ekoapp/ekosdk/community/EkoCommunity;)V", "deletedFileIds", "", "deletedImageIds", "feedRepository", "Lcom/ekoapp/ekosdk/EkoFeedRepository;", "fileRepository", "Lcom/ekoapp/ekosdk/EkoFileRepository;", "filesMap", "Ljava/util/HashMap;", "Lcom/ekoapp/ekosdk/uikit/community/domain/model/FileAttachment;", "imageMap", "Lcom/ekoapp/ekosdk/uikit/community/newsfeed/model/FeedImage;", "liveDataFiles", "Landroidx/lifecycle/MutableLiveData;", "liveDataImage", "newsFeed", "Lcom/ekoapp/ekosdk/feed/EkoPost;", "postId", "getPostId", "()Ljava/lang/String;", "setPostId", "(Ljava/lang/String;)V", "postText", "", "getPostText", "()Ljava/lang/CharSequence;", "setPostText", "(Ljava/lang/CharSequence;)V", "uploadFailedFile", "", "uploadFailedImages", "uploadedFilesMap", "Lcom/ekoapp/ekosdk/file/EkoFile;", "uploadedImageMap", "Lcom/ekoapp/ekosdk/file/EkoImage;", "addFiles", "fileAttachments", "addImages", "", "images", "Landroid/net/Uri;", "cancelUpload", "", "uploadId", "createPost", "Lio/reactivex/Single;", "createPostText", "createPostTextAndFiles", "files", "createPostTextAndImages", "deleteImageOrFileInPost", "Lio/reactivex/Completable;", "discardPost", "getFiles", "getImages", "getNewsFeed", "getPostDetails", "Lio/reactivex/Flowable;", "id", "getPostFile", "getPostImage", "hasAdminAccess", "hasAttachments", "hasFailedToUploadFiles", "hasFailedToUploadImages", "hasFirstTimeFailedToUploadFiles", "hasFirstTimeFailedToUploadImages", "hasImages", "hasPendingFileToUpload", "hasPendingImageToUpload", "hasUpdateOnPost", "isDuplicateFile", "fileAttachment", "mapEkoFileToFileAttachment", "ekoFile", "mapEkoImageToFeedImage", "ekoImage", "removeFile", "file", "removeImage", "feedImage", "setNewsFeed", "setUpPostTextWithImagesOrFiles", "ekoPost", "triggerFileUploadFailedEvent", "triggerImageRemovedEvent", "triggerImageUploadFailedEvent", "updateFileUploadStatus", "fileUpload", "Lcom/ekoapp/ekosdk/file/upload/EkoUploadResult;", "updateImageUploadStatus", "ekoImageUpload", "updateList", "updatePostText", "uploadFile", "attachment", "uploadImage", "community_debug"})
public final class EkoCreatePostViewModel extends com.ekoapp.ekosdk.uikit.base.EkoBaseViewModel {
    private final java.lang.String TAG = null;
    private final com.ekoapp.ekosdk.EkoFeedRepository feedRepository = null;
    private final com.ekoapp.ekosdk.EkoFileRepository fileRepository = null;
    @org.jetbrains.annotations.Nullable()
    private java.lang.String postId;
    private com.ekoapp.ekosdk.feed.EkoPost newsFeed;
    @org.jetbrains.annotations.Nullable()
    private com.ekoapp.ekosdk.community.EkoCommunity community;
    @org.jetbrains.annotations.Nullable()
    private java.lang.CharSequence postText;
    private final androidx.lifecycle.MutableLiveData<java.util.List<com.ekoapp.ekosdk.uikit.community.newsfeed.model.FeedImage>> liveDataImage = null;
    private final java.util.HashMap<java.lang.String, com.ekoapp.ekosdk.uikit.community.newsfeed.model.FeedImage> imageMap = null;
    private final java.util.HashMap<java.lang.String, com.ekoapp.ekosdk.file.EkoImage> uploadedImageMap = null;
    private final java.util.List<java.lang.String> deletedImageIds = null;
    private final java.util.HashMap<java.lang.String, java.lang.Boolean> uploadFailedImages = null;
    private final androidx.lifecycle.MutableLiveData<java.util.List<com.ekoapp.ekosdk.uikit.community.domain.model.FileAttachment>> liveDataFiles = null;
    private final java.util.HashMap<java.lang.String, com.ekoapp.ekosdk.uikit.community.domain.model.FileAttachment> filesMap = null;
    private final java.util.HashMap<java.lang.String, com.ekoapp.ekosdk.file.EkoFile> uploadedFilesMap = null;
    private final java.util.List<java.lang.String> deletedFileIds = null;
    private final java.util.HashMap<java.lang.String, java.lang.Boolean> uploadFailedFile = null;
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getPostId() {
        return null;
    }
    
    public final void setPostId(@org.jetbrains.annotations.Nullable()
    java.lang.String p0) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.ekoapp.ekosdk.community.EkoCommunity getCommunity() {
        return null;
    }
    
    public final void setCommunity(@org.jetbrains.annotations.Nullable()
    com.ekoapp.ekosdk.community.EkoCommunity p0) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.CharSequence getPostText() {
        return null;
    }
    
    public final void setPostText(@org.jetbrains.annotations.Nullable()
    java.lang.CharSequence p0) {
    }
    
    public final void setNewsFeed(@org.jetbrains.annotations.NotNull()
    com.ekoapp.ekosdk.feed.EkoPost newsFeed) {
    }
    
    private final void setUpPostTextWithImagesOrFiles(com.ekoapp.ekosdk.feed.EkoPost ekoPost) {
    }
    
    private final com.ekoapp.ekosdk.uikit.community.domain.model.FileAttachment mapEkoFileToFileAttachment(com.ekoapp.ekosdk.file.EkoFile ekoFile) {
        return null;
    }
    
    private final com.ekoapp.ekosdk.uikit.community.domain.model.FileAttachment mapEkoFileToFileAttachment(com.ekoapp.ekosdk.uikit.community.domain.model.FileAttachment fileAttachment, com.ekoapp.ekosdk.file.EkoFile ekoFile) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.ekoapp.ekosdk.feed.EkoPost getNewsFeed() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final io.reactivex.Flowable<com.ekoapp.ekosdk.feed.EkoPost> getPostDetails(@org.jetbrains.annotations.NotNull()
    java.lang.String id) {
        return null;
    }
    
    private final java.lang.CharSequence getPostText(com.ekoapp.ekosdk.feed.EkoPost newsFeed) {
        return null;
    }
    
    private final com.ekoapp.ekosdk.file.EkoImage getPostImage(com.ekoapp.ekosdk.feed.EkoPost newsFeed) {
        return null;
    }
    
    private final com.ekoapp.ekosdk.file.EkoFile getPostFile(com.ekoapp.ekosdk.feed.EkoPost newsFeed) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.lifecycle.MutableLiveData<java.util.List<com.ekoapp.ekosdk.uikit.community.newsfeed.model.FeedImage>> getImages() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final io.reactivex.Completable updatePostText(@org.jetbrains.annotations.NotNull()
    java.lang.String postText) {
        return null;
    }
    
    private final io.reactivex.Single<com.ekoapp.ekosdk.feed.EkoPost> createPostText(java.lang.String postText) {
        return null;
    }
    
    private final io.reactivex.Single<com.ekoapp.ekosdk.feed.EkoPost> createPostTextAndImages(java.lang.String postText, java.util.List<com.ekoapp.ekosdk.file.EkoImage> images) {
        return null;
    }
    
    private final io.reactivex.Single<com.ekoapp.ekosdk.feed.EkoPost> createPostTextAndFiles(java.lang.String postText, java.util.List<com.ekoapp.ekosdk.file.EkoFile> files) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final io.reactivex.Flowable<com.ekoapp.ekosdk.file.upload.EkoUploadResult<com.ekoapp.ekosdk.file.EkoImage>> uploadImage(@org.jetbrains.annotations.NotNull()
    com.ekoapp.ekosdk.uikit.community.newsfeed.model.FeedImage feedImage) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final io.reactivex.Flowable<com.ekoapp.ekosdk.file.upload.EkoUploadResult<com.ekoapp.ekosdk.file.EkoFile>> uploadFile(@org.jetbrains.annotations.NotNull()
    com.ekoapp.ekosdk.uikit.community.domain.model.FileAttachment attachment) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final io.reactivex.Completable deleteImageOrFileInPost() {
        return null;
    }
    
    public final boolean hasAdminAccess() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.ekoapp.ekosdk.uikit.community.newsfeed.model.FeedImage> addImages(@org.jetbrains.annotations.NotNull()
    java.util.List<? extends android.net.Uri> images) {
        return null;
    }
    
    public final void removeImage(@org.jetbrains.annotations.NotNull()
    com.ekoapp.ekosdk.uikit.community.newsfeed.model.FeedImage feedImage) {
    }
    
    private final void cancelUpload(java.lang.String uploadId) {
    }
    
    private final void triggerImageRemovedEvent() {
    }
    
    public final void updateImageUploadStatus(@org.jetbrains.annotations.NotNull()
    com.ekoapp.ekosdk.uikit.community.newsfeed.model.FeedImage feedImage, @org.jetbrains.annotations.NotNull()
    com.ekoapp.ekosdk.file.upload.EkoUploadResult<com.ekoapp.ekosdk.file.EkoImage> ekoImageUpload) {
    }
    
    private final void triggerImageUploadFailedEvent() {
    }
    
    private final void updateList(com.ekoapp.ekosdk.uikit.community.newsfeed.model.FeedImage feedImage) {
    }
    
    private final void updateList(com.ekoapp.ekosdk.uikit.community.domain.model.FileAttachment fileAttachment) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final io.reactivex.Single<com.ekoapp.ekosdk.feed.EkoPost> createPost(@org.jetbrains.annotations.NotNull()
    java.lang.String postText) {
        return null;
    }
    
    private final com.ekoapp.ekosdk.uikit.community.newsfeed.model.FeedImage mapEkoImageToFeedImage(com.ekoapp.ekosdk.file.EkoImage ekoImage) {
        return null;
    }
    
    public final boolean hasPendingImageToUpload() {
        return false;
    }
    
    public final boolean hasPendingFileToUpload() {
        return false;
    }
    
    public final boolean hasUpdateOnPost(@org.jetbrains.annotations.NotNull()
    java.lang.String postText) {
        return false;
    }
    
    public final boolean hasFailedToUploadImages() {
        return false;
    }
    
    public final boolean hasFailedToUploadFiles() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.lifecycle.MutableLiveData<java.util.List<com.ekoapp.ekosdk.uikit.community.domain.model.FileAttachment>> getFiles() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.ekoapp.ekosdk.uikit.community.domain.model.FileAttachment> addFiles(@org.jetbrains.annotations.NotNull()
    java.util.List<com.ekoapp.ekosdk.uikit.community.domain.model.FileAttachment> fileAttachments) {
        return null;
    }
    
    private final boolean isDuplicateFile(com.ekoapp.ekosdk.uikit.community.domain.model.FileAttachment fileAttachment) {
        return false;
    }
    
    public final void removeFile(@org.jetbrains.annotations.NotNull()
    com.ekoapp.ekosdk.uikit.community.domain.model.FileAttachment file) {
    }
    
    public final void updateFileUploadStatus(@org.jetbrains.annotations.NotNull()
    com.ekoapp.ekosdk.uikit.community.domain.model.FileAttachment fileAttachment, @org.jetbrains.annotations.NotNull()
    com.ekoapp.ekosdk.file.upload.EkoUploadResult<com.ekoapp.ekosdk.file.EkoFile> fileUpload) {
    }
    
    private final boolean hasFirstTimeFailedToUploadFiles() {
        return false;
    }
    
    private final boolean hasFirstTimeFailedToUploadImages() {
        return false;
    }
    
    private final void triggerFileUploadFailedEvent() {
    }
    
    public final boolean hasAttachments() {
        return false;
    }
    
    public final boolean hasImages() {
        return false;
    }
    
    public final void discardPost() {
    }
    
    public EkoCreatePostViewModel() {
        super();
    }
}