package com.ekoapp.ekosdk.uikit.chat.messages.fragment;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\u0096\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0011\n\u0002\u0010\u000e\n\u0002\b\n\n\u0002\u0010\u0002\n\u0002\b\t\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0012\u0018\u0000 U2\u00020\u00012\u00020\u0002:\u0002TUB\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0003J\u000e\u0010\u001f\u001a\u00020 2\u0006\u0010!\u001a\u00020\u0005J\b\u0010\"\u001a\u00020 H\u0002J\b\u0010#\u001a\u00020 H\u0002J\b\u0010$\u001a\u00020 H\u0002J\b\u0010%\u001a\u00020\u0011H\u0002J\b\u0010&\u001a\u00020 H\u0002J\b\u0010\'\u001a\u00020 H\u0002J\"\u0010(\u001a\u00020 2\u0006\u0010)\u001a\u00020*2\u0006\u0010+\u001a\u00020*2\b\u0010,\u001a\u0004\u0018\u00010-H\u0016J\u0012\u0010.\u001a\u00020 2\b\u0010/\u001a\u0004\u0018\u000100H\u0016J\u0018\u00101\u001a\u00020 2\u0006\u00102\u001a\u0002032\u0006\u00104\u001a\u000205H\u0016J&\u00106\u001a\u0004\u0018\u0001072\u0006\u00104\u001a\u0002082\b\u00109\u001a\u0004\u0018\u00010:2\b\u0010/\u001a\u0004\u0018\u000100H\u0016J\b\u0010;\u001a\u00020 H\u0016J\u0012\u0010<\u001a\u00020 2\b\u0010,\u001a\u0004\u0018\u00010=H\u0016J\u0012\u0010>\u001a\u00020 2\b\u0010?\u001a\u0004\u0018\u00010@H\u0016J\u0012\u0010A\u001a\u00020 2\b\u0010,\u001a\u0004\u0018\u00010=H\u0016J\u0010\u0010B\u001a\u00020\u00112\u0006\u0010C\u001a\u00020DH\u0016J\b\u0010E\u001a\u00020 H\u0016J\u0012\u0010F\u001a\u00020 2\b\u0010G\u001a\u0004\u0018\u00010@H\u0016J\b\u0010H\u001a\u00020 H\u0016J\u001a\u0010I\u001a\u00020 2\u0006\u0010J\u001a\u0002072\b\u0010/\u001a\u0004\u0018\u000100H\u0016J\b\u0010K\u001a\u00020 H\u0002J\b\u0010L\u001a\u00020 H\u0002J\u0010\u0010M\u001a\u00020 2\u0006\u0010N\u001a\u00020*H\u0002J\b\u0010O\u001a\u00020 H\u0003J\b\u0010P\u001a\u00020 H\u0002J\b\u0010Q\u001a\u00020 H\u0002J\b\u0010R\u001a\u00020 H\u0016J\b\u0010S\u001a\u00020 H\u0002R\u0010\u0010\u0004\u001a\u0004\u0018\u00010\u0005X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082.\u00a2\u0006\u0002\n\u0000R\u001b\u0010\n\u001a\u00020\u000b8BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\u000e\u0010\u000f\u001a\u0004\b\f\u0010\rR\u000e\u0010\u0010\u001a\u00020\u0011X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\"\u0010\u0012\u001a\u0016\u0012\u0012\u0012\u0010\u0012\f\u0012\n \u0016*\u0004\u0018\u00010\u00150\u00150\u00140\u0013X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u0017\u001a\u00020\u0011X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0018\u0010\u0019\"\u0004\b\u001a\u0010\u001bR\u0016\u0010\u001c\u001a\b\u0012\u0004\u0012\u00020\u00150\u0014X\u0082\u0004\u00a2\u0006\u0004\n\u0002\u0010\u001dR\u000e\u0010\u001e\u001a\u00020\u0011X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006V"}, d2 = {"Lcom/ekoapp/ekosdk/uikit/chat/messages/fragment/EkoMessageListFragment;", "Lcom/ekoapp/ekosdk/uikit/base/EkoPickerFragment;", "Lcom/ekoapp/ekosdk/uikit/components/IAudioRecorderListener;", "()V", "iCustomViewHolder", "Lcom/ekoapp/ekosdk/uikit/chat/messages/adapter/EkoMessageListAdapter$ICustomViewHolder;", "mAdapter", "Lcom/ekoapp/ekosdk/uikit/chat/messages/adapter/EkoMessageListAdapter;", "mBinding", "Lcom/ekoapp/ekosdk/uikit/chat/databinding/AmityFragmentChatBinding;", "messageListViewModel", "Lcom/ekoapp/ekosdk/uikit/chat/messages/viewModel/EkoMessageListViewModel;", "getMessageListViewModel", "()Lcom/ekoapp/ekosdk/uikit/chat/messages/viewModel/EkoMessageListViewModel;", "messageListViewModel$delegate", "Lkotlin/Lazy;", "msgSent", "", "recordPermission", "Landroidx/activity/result/ActivityResultLauncher;", "", "", "kotlin.jvm.PlatformType", "recordPermissionGranted", "getRecordPermissionGranted", "()Z", "setRecordPermissionGranted", "(Z)V", "requiredPermissions", "[Ljava/lang/String;", "scrollRequired", "addCustomView", "", "listener", "getChannelType", "initRecyclerView", "initToolBar", "isRecorderPermissionGranted", "joinChannel", "observeViewModelEvents", "onActivityResult", "requestCode", "", "resultCode", "data", "Landroid/content/Intent;", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "onCreateOptionsMenu", "menu", "Landroid/view/Menu;", "inflater", "Landroid/view/MenuInflater;", "onCreateView", "Landroid/view/View;", "Landroid/view/LayoutInflater;", "container", "Landroid/view/ViewGroup;", "onDestroyView", "onFilePicked", "Landroid/net/Uri;", "onFileRecorded", "audioFile", "Ljava/io/File;", "onImagePicked", "onOptionsItemSelected", "item", "Landroid/view/MenuItem;", "onPause", "onPhotoClicked", "file", "onResume", "onViewCreated", "view", "pickMultipleImages", "requestRecorderPermission", "scrollToEnd", "position", "setRecorderTouchListener", "setUpBackPress", "showAudioRecordUi", "showMessage", "toggleSoftKeyboard", "Builder", "Companion", "chatkit_debug"})
public final class EkoMessageListFragment extends com.ekoapp.ekosdk.uikit.base.EkoPickerFragment implements com.ekoapp.ekosdk.uikit.components.IAudioRecorderListener {
    private final kotlin.Lazy messageListViewModel$delegate = null;
    private com.ekoapp.ekosdk.uikit.chat.messages.adapter.EkoMessageListAdapter mAdapter;
    private com.ekoapp.ekosdk.uikit.chat.databinding.AmityFragmentChatBinding mBinding;
    private boolean scrollRequired = true;
    private boolean msgSent = false;
    private com.ekoapp.ekosdk.uikit.chat.messages.adapter.EkoMessageListAdapter.ICustomViewHolder iCustomViewHolder;
    private boolean recordPermissionGranted = false;
    private final java.lang.String[] requiredPermissions = {"android.permission.RECORD_AUDIO", "android.permission.WRITE_EXTERNAL_STORAGE"};
    private final androidx.activity.result.ActivityResultLauncher<java.lang.String[]> recordPermission = null;
    private static final long SCROLL_DELAY = 200L;
    public static final com.ekoapp.ekosdk.uikit.chat.messages.fragment.EkoMessageListFragment.Companion Companion = null;
    private java.util.HashMap _$_findViewCache;
    
    private final com.ekoapp.ekosdk.uikit.chat.messages.viewModel.EkoMessageListViewModel getMessageListViewModel() {
        return null;
    }
    
    public final boolean getRecordPermissionGranted() {
        return false;
    }
    
    public final void setRecordPermissionGranted(boolean p0) {
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
    public void onResume() {
    }
    
    private final void setUpBackPress() {
    }
    
    private final void getChannelType() {
    }
    
    private final void initToolBar() {
    }
    
    private final void initRecyclerView() {
    }
    
    private final void joinChannel() {
    }
    
    @android.annotation.SuppressLint(value = {"ClickableViewAccessibility"})
    private final void setRecorderTouchListener() {
    }
    
    private final void requestRecorderPermission() {
    }
    
    private final boolean isRecorderPermissionGranted() {
        return false;
    }
    
    private final void scrollToEnd(int position) {
    }
    
    private final void observeViewModelEvents() {
    }
    
    private final void showAudioRecordUi() {
    }
    
    private final void toggleSoftKeyboard() {
    }
    
    private final void pickMultipleImages() {
    }
    
    @java.lang.Override()
    public void onFilePicked(@org.jetbrains.annotations.Nullable()
    android.net.Uri data) {
    }
    
    @java.lang.Override()
    public void onImagePicked(@org.jetbrains.annotations.Nullable()
    android.net.Uri data) {
    }
    
    @java.lang.Override()
    public void onPhotoClicked(@org.jetbrains.annotations.Nullable()
    java.io.File file) {
    }
    
    @java.lang.Override()
    public void onFileRecorded(@org.jetbrains.annotations.Nullable()
    java.io.File audioFile) {
    }
    
    @java.lang.Override()
    public void showMessage() {
    }
    
    public final void addCustomView(@org.jetbrains.annotations.NotNull()
    com.ekoapp.ekosdk.uikit.chat.messages.adapter.EkoMessageListAdapter.ICustomViewHolder listener) {
    }
    
    @java.lang.Override()
    public void onActivityResult(int requestCode, int resultCode, @org.jetbrains.annotations.Nullable()
    android.content.Intent data) {
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
    
    @java.lang.Override()
    public void onPause() {
    }
    
    @java.lang.Override()
    public void onDestroyView() {
    }
    
    private EkoMessageListFragment() {
        super();
    }
    
    @kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0006\u0010\u0005\u001a\u00020\u0006R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0007"}, d2 = {"Lcom/ekoapp/ekosdk/uikit/chat/messages/fragment/EkoMessageListFragment$Builder;", "", "channelId", "", "(Ljava/lang/String;)V", "build", "Lcom/ekoapp/ekosdk/uikit/chat/messages/fragment/EkoMessageListFragment;", "chatkit_debug"})
    public static final class Builder {
        private final java.lang.String channelId = null;
        
        @org.jetbrains.annotations.NotNull()
        public final com.ekoapp.ekosdk.uikit.chat.messages.fragment.EkoMessageListFragment build() {
            return null;
        }
        
        public Builder(@org.jetbrains.annotations.NotNull()
        java.lang.String channelId) {
            super();
        }
    }
    
    @kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\t\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0005"}, d2 = {"Lcom/ekoapp/ekosdk/uikit/chat/messages/fragment/EkoMessageListFragment$Companion;", "", "()V", "SCROLL_DELAY", "", "chatkit_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}