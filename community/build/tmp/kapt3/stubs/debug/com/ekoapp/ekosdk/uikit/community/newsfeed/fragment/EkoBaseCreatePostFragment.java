package com.ekoapp.ekosdk.uikit.community.newsfeed.fragment;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\u00c6\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0011\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u001b\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0015\n\u0002\b\u0014\n\u0002\u0010!\n\u0002\b\b\b&\u0018\u00002\u00020\u00012\u00020\u00022\u00020\u00032\u00020\u0004B\u0005\u00a2\u0006\u0002\u0010\u0005J\u0010\u0010)\u001a\u00020*2\u0006\u0010+\u001a\u00020,H\u0002J\u0010\u0010-\u001a\u00020*2\u0006\u0010.\u001a\u00020,H\u0002J\b\u0010/\u001a\u00020*H\u0002J\b\u00100\u001a\u00020*H\u0002J\u0006\u00101\u001a\u00020*J\b\u00102\u001a\u00020*H\u0002J\u0010\u00103\u001a\u00020\u00192\u0006\u00104\u001a\u000205H\u0002J\u0010\u00106\u001a\u0002072\u0006\u00108\u001a\u000209H\u0002J\b\u0010:\u001a\u00020\tH&J\b\u0010;\u001a\u00020*H\u0002J\b\u0010<\u001a\u00020*H\u0002J\b\u0010=\u001a\u00020*H\u0016J\b\u0010>\u001a\u00020*H\u0002J\b\u0010?\u001a\u00020*H\u0002J\b\u0010@\u001a\u00020*H&J\b\u0010A\u001a\u00020\u0019H\u0002J\b\u0010B\u001a\u00020*H\u0004J\u0006\u0010C\u001a\u00020*J\b\u0010D\u001a\u00020*H\u0002J\u0006\u0010E\u001a\u00020\u0019J\b\u0010F\u001a\u00020\u0019H\u0002J\b\u0010G\u001a\u00020\u0019H\u0002J\b\u0010H\u001a\u00020\u0019H\u0002J\b\u0010I\u001a\u00020\u0019H\u0002J\b\u0010J\u001a\u00020\u0019H\u0016J\u0010\u0010K\u001a\u00020\u00192\u0006\u0010+\u001a\u00020,H\u0002J\b\u0010L\u001a\u00020*H\u0002J\b\u0010M\u001a\u00020*H\u0002J\"\u0010N\u001a\u00020*2\u0006\u0010O\u001a\u00020\u00072\u0006\u0010P\u001a\u00020\u00072\b\u0010+\u001a\u0004\u0018\u00010,H\u0016J\b\u0010Q\u001a\u00020*H\u0016J\b\u0010R\u001a\u00020*H\u0016J\u0012\u0010S\u001a\u00020*2\b\u0010T\u001a\u0004\u0018\u00010UH\u0016J\u0018\u0010V\u001a\u00020*2\u0006\u0010W\u001a\u00020X2\u0006\u0010Y\u001a\u00020ZH\u0016J&\u0010[\u001a\u0004\u0018\u00010\\2\u0006\u0010Y\u001a\u00020]2\b\u0010^\u001a\u0004\u0018\u00010_2\b\u0010T\u001a\u0004\u0018\u00010UH\u0016J\b\u0010`\u001a\u00020*H\u0016J\u0010\u0010a\u001a\u00020\u00192\u0006\u0010b\u001a\u00020&H\u0016J\u0018\u0010c\u001a\u00020*2\u0006\u0010d\u001a\u0002072\u0006\u0010e\u001a\u00020\u0007H\u0016J\u0018\u0010f\u001a\u00020*2\u0006\u0010g\u001a\u00020h2\u0006\u0010e\u001a\u00020\u0007H\u0016J-\u0010i\u001a\u00020*2\u0006\u0010O\u001a\u00020\u00072\u000e\u0010j\u001a\n\u0012\u0006\b\u0001\u0012\u00020\t0\r2\u0006\u0010k\u001a\u00020lH\u0016\u00a2\u0006\u0002\u0010mJ\u001a\u0010n\u001a\u00020*2\u0006\u0010o\u001a\u00020\\2\b\u0010T\u001a\u0004\u0018\u00010UH\u0016J\b\u0010p\u001a\u00020*H\u0002J\b\u0010q\u001a\u00020*H\u0002J\r\u0010r\u001a\u00020*H\u0000\u00a2\u0006\u0002\bsJ\b\u0010t\u001a\u00020*H&J\b\u0010u\u001a\u00020*H\u0002J\b\u0010v\u001a\u00020*H\u0002J\b\u0010w\u001a\u00020*H\u0002J\b\u0010x\u001a\u00020*H\u0002J\b\u0010y\u001a\u00020*H\u0002J\u0012\u0010z\u001a\u00020*2\b\b\u0001\u0010{\u001a\u00020\u0007H\u0002J\u0012\u0010z\u001a\u00020*2\b\u0010{\u001a\u0004\u0018\u00010\tH\u0004J\b\u0010|\u001a\u00020*H\u0002J\b\u0010}\u001a\u00020*H\u0002J\u0006\u0010~\u001a\u00020*J\u0018\u0010\u007f\u001a\u00020*2\u000e\u0010\u0080\u0001\u001a\t\u0012\u0004\u0012\u0002070\u0081\u0001H\u0002J\t\u0010\u0082\u0001\u001a\u00020\u0019H\u0002J\t\u0010\u0083\u0001\u001a\u00020*H\u0002J\u0010\u0010\u0084\u0001\u001a\u00020*2\u0007\u0010\u0085\u0001\u001a\u00020\u0019J\u0019\u0010\u0086\u0001\u001a\u00020*2\u000e\u0010\u0080\u0001\u001a\t\u0012\u0004\u0012\u0002070\u0081\u0001H\u0002J\u0012\u0010\u0087\u0001\u001a\u00020*2\u0007\u0010\u0088\u0001\u001a\u00020hH\u0002R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082D\u00a2\u0006\u0002\n\u0000R\u0016\u0010\b\u001a\n \n*\u0004\u0018\u00010\t0\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\"\u0010\u000b\u001a\u0016\u0012\u0012\u0012\u0010\u0012\f\u0012\n \n*\u0004\u0018\u00010\t0\t0\r0\fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u000e\u001a\u00020\u000fX\u0084\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0010\u0010\u0011\"\u0004\b\u0012\u0010\u0013R\u0010\u0010\u0014\u001a\u0004\u0018\u00010\u0015X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0016\u001a\u0004\u0018\u00010\u0017X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u0018\u001a\u00020\u0019X\u0084\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0018\u0010\u001a\"\u0004\b\u001b\u0010\u001cR\u0010\u0010\u001d\u001a\u0004\u0018\u00010\u001eX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001b\u0010\u001f\u001a\u00020 8DX\u0084\u0084\u0002\u00a2\u0006\f\n\u0004\b#\u0010$\u001a\u0004\b!\u0010\"R\u0010\u0010%\u001a\u0004\u0018\u00010&X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\'\u001a\u0004\u0018\u00010(X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0089\u0001"}, d2 = {"Lcom/ekoapp/ekosdk/uikit/community/newsfeed/fragment/EkoBaseCreatePostFragment;", "Lcom/ekoapp/ekosdk/uikit/base/EkoBaseFragment;", "Lcom/ekoapp/ekosdk/uikit/community/newsfeed/listener/ICreatePostImageActionListener;", "Lcom/ekoapp/ekosdk/uikit/community/newsfeed/listener/ICreatePostFileActionListener;", "Lcom/ekoapp/ekosdk/uikit/common/views/dialog/EkoAlertDialogFragment$IAlertDialogActionListener;", "()V", "ID_MENU_ITEM_POST", "", "TAG", "", "kotlin.jvm.PlatformType", "cameraPermission", "Landroidx/activity/result/ActivityResultLauncher;", "", "compositeDisposable", "Lio/reactivex/disposables/CompositeDisposable;", "getCompositeDisposable", "()Lio/reactivex/disposables/CompositeDisposable;", "setCompositeDisposable", "(Lio/reactivex/disposables/CompositeDisposable;)V", "fileAdapter", "Lcom/ekoapp/ekosdk/uikit/community/newsfeed/adapter/EkoCreatePostFileAdapter;", "imageAdapter", "Lcom/ekoapp/ekosdk/uikit/community/newsfeed/adapter/EkoCreatePostImageAdapter;", "isLoading", "", "()Z", "setLoading", "(Z)V", "itemDecor", "Landroidx/recyclerview/widget/RecyclerView$ItemDecoration;", "mViewModel", "Lcom/ekoapp/ekosdk/uikit/community/newsfeed/viewmodel/EkoCreatePostViewModel;", "getMViewModel", "()Lcom/ekoapp/ekosdk/uikit/community/newsfeed/viewmodel/EkoCreatePostViewModel;", "mViewModel$delegate", "Lkotlin/Lazy;", "menuItemPost", "Landroid/view/MenuItem;", "photoFile", "Ljava/io/File;", "addFileAttachments", "", "data", "Landroid/content/Intent;", "addImages", "it", "addPostEditTextListener", "addViewModelListener", "clearHint", "dispatchTakePictureIntent", "exceedMaxFileSize", "size", "", "getFileAttachment", "Lcom/ekoapp/ekosdk/uikit/community/domain/model/FileAttachment;", "uri", "Landroid/net/Uri;", "getPostMenuText", "handleAddFiles", "handleAddPhotos", "handleBackPress", "handleButtonActiveInactiveBehavior", "handleCancelPost", "handlePostMenuItemClick", "hasDraft", "hideComposeBar", "hideLoading", "hidePostAsCommunity", "isEditMode", "isEmptyFileAttachments", "isEmptyImages", "isEmptyPostTest", "isModerator", "isRightButtonActive", "maxAttachmentCountExceed", "observeFileAttachments", "observeImageData", "onActivityResult", "requestCode", "resultCode", "onClickNegativeButton", "onClickPositiveButton", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "onCreateOptionsMenu", "menu", "Landroid/view/Menu;", "inflater", "Landroid/view/MenuInflater;", "onCreateView", "Landroid/view/View;", "Landroid/view/LayoutInflater;", "container", "Landroid/view/ViewGroup;", "onDestroy", "onOptionsItemSelected", "item", "onRemoveFile", "file", "position", "onRemoveImage", "feedImage", "Lcom/ekoapp/ekosdk/uikit/community/newsfeed/model/FeedImage;", "onRequestPermissionsResult", "permissions", "grantResults", "", "(I[Ljava/lang/String;[I)V", "onViewCreated", "view", "openFilePicker", "openImagePicker", "refresh", "refresh$community_debug", "setToolBarText", "setupComposeBar", "setupFileAttachmentAdapter", "setupImageAdapter", "showAttachmentUploadFailedDialog", "showDuplicateFilesMessage", "showErrorMessage", "error", "showExitConfirmationDialog", "showImageUploadFailedDialog", "showLoading", "showMaxLimitExceedError", "addedFiles", "", "showPostAsCommunity", "takePicture", "updatePostMenu", "enabled", "uploadFileAttachments", "uploadImage", "image", "community_debug"})
public abstract class EkoBaseCreatePostFragment extends com.ekoapp.ekosdk.uikit.base.EkoBaseFragment implements com.ekoapp.ekosdk.uikit.community.newsfeed.listener.ICreatePostImageActionListener, com.ekoapp.ekosdk.uikit.community.newsfeed.listener.ICreatePostFileActionListener, com.ekoapp.ekosdk.uikit.common.views.dialog.EkoAlertDialogFragment.IAlertDialogActionListener {
    private final int ID_MENU_ITEM_POST = 133;
    private android.view.MenuItem menuItemPost;
    @org.jetbrains.annotations.NotNull()
    private final kotlin.Lazy mViewModel$delegate = null;
    private final java.lang.String TAG = null;
    @org.jetbrains.annotations.NotNull()
    private io.reactivex.disposables.CompositeDisposable compositeDisposable;
    private com.ekoapp.ekosdk.uikit.community.newsfeed.adapter.EkoCreatePostImageAdapter imageAdapter;
    private androidx.recyclerview.widget.RecyclerView.ItemDecoration itemDecor;
    private com.ekoapp.ekosdk.uikit.community.newsfeed.adapter.EkoCreatePostFileAdapter fileAdapter;
    private java.io.File photoFile;
    private boolean isLoading = false;
    private final androidx.activity.result.ActivityResultLauncher<java.lang.String[]> cameraPermission = null;
    private java.util.HashMap _$_findViewCache;
    
    @org.jetbrains.annotations.NotNull()
    protected final com.ekoapp.ekosdk.uikit.community.newsfeed.viewmodel.EkoCreatePostViewModel getMViewModel() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    protected final io.reactivex.disposables.CompositeDisposable getCompositeDisposable() {
        return null;
    }
    
    protected final void setCompositeDisposable(@org.jetbrains.annotations.NotNull()
    io.reactivex.disposables.CompositeDisposable p0) {
    }
    
    protected final boolean isLoading() {
        return false;
    }
    
    protected final void setLoading(boolean p0) {
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
    
    public abstract void handlePostMenuItemClick();
    
    private final void observeImageData() {
    }
    
    private final void observeFileAttachments() {
    }
    
    private final boolean showPostAsCommunity() {
        return false;
    }
    
    @java.lang.Override()
    public void onDestroy() {
    }
    
    protected final void showErrorMessage(@org.jetbrains.annotations.Nullable()
    java.lang.String error) {
    }
    
    private final void setupComposeBar() {
    }
    
    private final void handleAddFiles() {
    }
    
    private final void openFilePicker() {
    }
    
    private final void handleAddPhotos() {
    }
    
    protected final void hideComposeBar() {
    }
    
    private final void hidePostAsCommunity() {
    }
    
    private final void setupImageAdapter() {
    }
    
    private final void setupFileAttachmentAdapter() {
    }
    
    private final void addPostEditTextListener() {
    }
    
    public abstract void setToolBarText();
    
    @org.jetbrains.annotations.NotNull()
    public abstract java.lang.String getPostMenuText();
    
    public final boolean isEditMode() {
        return false;
    }
    
    public final void showLoading() {
    }
    
    public final void hideLoading() {
    }
    
    public final void clearHint() {
    }
    
    public boolean isRightButtonActive() {
        return false;
    }
    
    private final void handleButtonActiveInactiveBehavior() {
    }
    
    public final void updatePostMenu(boolean enabled) {
    }
    
    private final boolean isEmptyFileAttachments() {
        return false;
    }
    
    private final boolean isEmptyImages() {
        return false;
    }
    
    private final boolean isEmptyPostTest() {
        return false;
    }
    
    @java.lang.Override()
    public void handleBackPress() {
    }
    
    private final void handleCancelPost() {
    }
    
    private final void showExitConfirmationDialog() {
    }
    
    private final boolean hasDraft() {
        return false;
    }
    
    private final void openImagePicker() {
    }
    
    @java.lang.Override()
    public void onRequestPermissionsResult(int requestCode, @org.jetbrains.annotations.NotNull()
    java.lang.String[] permissions, @org.jetbrains.annotations.NotNull()
    int[] grantResults) {
    }
    
    @java.lang.Override()
    public void onActivityResult(int requestCode, int resultCode, @org.jetbrains.annotations.Nullable()
    android.content.Intent data) {
    }
    
    private final void addViewModelListener() {
    }
    
    private final void showImageUploadFailedDialog() {
    }
    
    private final void showAttachmentUploadFailedDialog() {
    }
    
    private final boolean maxAttachmentCountExceed(android.content.Intent data) {
        return false;
    }
    
    private final void addImages(android.content.Intent it) {
    }
    
    private final void uploadImage(com.ekoapp.ekosdk.uikit.community.newsfeed.model.FeedImage image) {
    }
    
    private final void addFileAttachments(android.content.Intent data) {
    }
    
    private final void showMaxLimitExceedError(java.util.List<com.ekoapp.ekosdk.uikit.community.domain.model.FileAttachment> addedFiles) {
    }
    
    private final void uploadFileAttachments(java.util.List<com.ekoapp.ekosdk.uikit.community.domain.model.FileAttachment> addedFiles) {
    }
    
    private final void showDuplicateFilesMessage() {
    }
    
    private final void showErrorMessage(@androidx.annotation.StringRes()
    int error) {
    }
    
    private final boolean exceedMaxFileSize(long size) {
        return false;
    }
    
    private final void takePicture() {
    }
    
    private final void dispatchTakePictureIntent() {
    }
    
    private final com.ekoapp.ekosdk.uikit.community.domain.model.FileAttachment getFileAttachment(android.net.Uri uri) {
        return null;
    }
    
    @java.lang.Override()
    public void onRemoveImage(@org.jetbrains.annotations.NotNull()
    com.ekoapp.ekosdk.uikit.community.newsfeed.model.FeedImage feedImage, int position) {
    }
    
    @java.lang.Override()
    public void onRemoveFile(@org.jetbrains.annotations.NotNull()
    com.ekoapp.ekosdk.uikit.community.domain.model.FileAttachment file, int position) {
    }
    
    @java.lang.Override()
    public void onClickPositiveButton() {
    }
    
    public final void refresh$community_debug() {
    }
    
    private final boolean isModerator() {
        return false;
    }
    
    @java.lang.Override()
    public void onClickNegativeButton() {
    }
    
    public EkoBaseCreatePostFragment() {
        super();
    }
}