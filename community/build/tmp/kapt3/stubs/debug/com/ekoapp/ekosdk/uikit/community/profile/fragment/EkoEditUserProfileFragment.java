package com.ekoapp.ekosdk.uikit.community.profile.fragment;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000|\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\b\u0018\u00002\u00020\u0001:\u0001AB\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0018\u001a\u00020\u0019H\u0002J\b\u0010\u001a\u001a\u00020\u0019H\u0002J\b\u0010\u001b\u001a\u00020\u0019H\u0002J\u0010\u0010\u001c\u001a\u00020\u00192\u0006\u0010\u001d\u001a\u00020\u0015H\u0002J\b\u0010\u001e\u001a\u00020\u0019H\u0002J\b\u0010\u001f\u001a\u00020\u0019H\u0002J\u0012\u0010 \u001a\u00020\u00192\b\u0010!\u001a\u0004\u0018\u00010\"H\u0016J\u0018\u0010#\u001a\u00020\u00192\u0006\u0010$\u001a\u00020%2\u0006\u0010&\u001a\u00020\'H\u0016J&\u0010(\u001a\u0004\u0018\u00010)2\u0006\u0010&\u001a\u00020*2\b\u0010+\u001a\u0004\u0018\u00010,2\b\u0010!\u001a\u0004\u0018\u00010\"H\u0016J\u0012\u0010-\u001a\u00020\u00192\b\u0010.\u001a\u0004\u0018\u00010\u0017H\u0016J\u0012\u0010/\u001a\u00020\u00192\b\u0010.\u001a\u0004\u0018\u00010\u0017H\u0016J\u0010\u00100\u001a\u0002012\u0006\u0010\u001d\u001a\u00020\u0015H\u0016J\u0012\u00102\u001a\u00020\u00192\b\u00103\u001a\u0004\u0018\u000104H\u0016J\u001a\u00105\u001a\u00020\u00192\u0006\u00106\u001a\u00020)2\b\u0010!\u001a\u0004\u0018\u00010\"H\u0016J\u0012\u00107\u001a\u00020\u00192\b\u0010\u0016\u001a\u0004\u0018\u00010\u0017H\u0002J\u0010\u00108\u001a\u00020\u00192\u0006\u00109\u001a\u00020:H\u0002J\b\u0010;\u001a\u00020\u0019H\u0002J\u0010\u0010<\u001a\u00020\u00192\u0006\u0010=\u001a\u000201H\u0002J\b\u0010>\u001a\u00020\u0019H\u0002J\u0010\u0010?\u001a\u00020\u00192\u0006\u0010@\u001a\u00020\u0017H\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082D\u00a2\u0006\u0002\n\u0000R\u0016\u0010\u0005\u001a\n \u0007*\u0004\u0018\u00010\u00060\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\b\u001a\u00020\tX\u0086.\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\n\u0010\u000b\"\u0004\b\f\u0010\rR\u001b\u0010\u000e\u001a\u00020\u000f8BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\u0012\u0010\u0013\u001a\u0004\b\u0010\u0010\u0011R\u0010\u0010\u0014\u001a\u0004\u0018\u00010\u0015X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0016\u001a\u0004\u0018\u00010\u0017X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006B"}, d2 = {"Lcom/ekoapp/ekosdk/uikit/community/profile/fragment/EkoEditUserProfileFragment;", "Lcom/ekoapp/ekosdk/uikit/base/EkoPickerFragment;", "()V", "ID_MENU_ITEM_SAVE_PROFILE", "", "TAG", "", "kotlin.jvm.PlatformType", "mBinding", "Lcom/ekoapp/ekosdk/uikit/community/databinding/AmityFragmentEditUserProfileBinding;", "getMBinding", "()Lcom/ekoapp/ekosdk/uikit/community/databinding/AmityFragmentEditUserProfileBinding;", "setMBinding", "(Lcom/ekoapp/ekosdk/uikit/community/databinding/AmityFragmentEditUserProfileBinding;)V", "mViewModel", "Lcom/ekoapp/ekosdk/uikit/community/profile/viewmodel/EkoEditUserProfileViewModel;", "getMViewModel", "()Lcom/ekoapp/ekosdk/uikit/community/profile/viewmodel/EkoEditUserProfileViewModel;", "mViewModel$delegate", "Lkotlin/Lazy;", "menuItemSaveProfile", "Landroid/view/MenuItem;", "profileUri", "Landroid/net/Uri;", "addViewModelListener", "", "getUserDetails", "handleErrorProfilePictureUpload", "handleUploadPhotoOption", "item", "initToolBar", "observeProfileUpdate", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "onCreateOptionsMenu", "menu", "Landroid/view/Menu;", "inflater", "Landroid/view/MenuInflater;", "onCreateView", "Landroid/view/View;", "Landroid/view/LayoutInflater;", "container", "Landroid/view/ViewGroup;", "onFilePicked", "data", "onImagePicked", "onOptionsItemSelected", "", "onPhotoClicked", "file", "Ljava/io/File;", "onViewCreated", "view", "setProfilePicture", "setupUserData", "user", "Lcom/ekoapp/ekosdk/user/EkoUser;", "showOptionTakePhoto", "updateSaveProfileMenu", "enabled", "updateUser", "uploadProfilePicture", "uri", "Builder", "community_debug"})
public final class EkoEditUserProfileFragment extends com.ekoapp.ekosdk.uikit.base.EkoPickerFragment {
    private android.view.MenuItem menuItemSaveProfile;
    private final int ID_MENU_ITEM_SAVE_PROFILE = 111;
    private final java.lang.String TAG = null;
    private final kotlin.Lazy mViewModel$delegate = null;
    @org.jetbrains.annotations.NotNull()
    public com.ekoapp.ekosdk.uikit.community.databinding.AmityFragmentEditUserProfileBinding mBinding;
    private android.net.Uri profileUri;
    private java.util.HashMap _$_findViewCache;
    
    private final com.ekoapp.ekosdk.uikit.community.profile.viewmodel.EkoEditUserProfileViewModel getMViewModel() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.ekoapp.ekosdk.uikit.community.databinding.AmityFragmentEditUserProfileBinding getMBinding() {
        return null;
    }
    
    public final void setMBinding(@org.jetbrains.annotations.NotNull()
    com.ekoapp.ekosdk.uikit.community.databinding.AmityFragmentEditUserProfileBinding p0) {
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
    
    private final void observeProfileUpdate() {
    }
    
    @java.lang.Override()
    public void onImagePicked(@org.jetbrains.annotations.Nullable()
    android.net.Uri data) {
    }
    
    private final void setProfilePicture(android.net.Uri profileUri) {
    }
    
    @java.lang.Override()
    public void onFilePicked(@org.jetbrains.annotations.Nullable()
    android.net.Uri data) {
    }
    
    @java.lang.Override()
    public void onPhotoClicked(@org.jetbrains.annotations.Nullable()
    java.io.File file) {
    }
    
    private final void getUserDetails() {
    }
    
    private final void setupUserData(com.ekoapp.ekosdk.user.EkoUser user) {
    }
    
    private final void initToolBar() {
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
    
    private final void updateSaveProfileMenu(boolean enabled) {
    }
    
    private final void addViewModelListener() {
    }
    
    private final void handleErrorProfilePictureUpload() {
    }
    
    private final void uploadProfilePicture(android.net.Uri uri) {
    }
    
    private final void showOptionTakePhoto() {
    }
    
    private final void handleUploadPhotoOption(android.view.MenuItem item) {
    }
    
    private final void updateUser() {
    }
    
    public EkoEditUserProfileFragment() {
        super();
    }
    
    @kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006\u00a8\u0006\u0007"}, d2 = {"Lcom/ekoapp/ekosdk/uikit/community/profile/fragment/EkoEditUserProfileFragment$Builder;", "", "()V", "build", "Lcom/ekoapp/ekosdk/uikit/community/profile/fragment/EkoEditUserProfileFragment;", "activity", "Landroidx/appcompat/app/AppCompatActivity;", "community_debug"})
    public static final class Builder {
        
        @org.jetbrains.annotations.NotNull()
        public final com.ekoapp.ekosdk.uikit.community.profile.fragment.EkoEditUserProfileFragment build(@org.jetbrains.annotations.NotNull()
        androidx.appcompat.app.AppCompatActivity activity) {
            return null;
        }
        
        public Builder() {
            super();
        }
    }
}