package com.ekoapp.ekosdk.uikit.community.profile.viewmodel;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000h\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0007\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\n\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0006\u00101\u001a\u000202J\u0006\u00103\u001a\u000202J\b\u00104\u001a\u00020\u0005H\u0002J\u000e\u0010*\u001a\n\u0012\u0004\u0012\u00020)\u0018\u000105J\b\u00106\u001a\u00020\u0011H\u0002J\u0014\u00107\u001a\u0002022\f\u00108\u001a\b\u0012\u0004\u0012\u00020\u001809J\u0010\u0010:\u001a\u0002022\b\u0010\u001d\u001a\u0004\u0018\u00010\u001eJ\f\u0010;\u001a\b\u0012\u0004\u0012\u00020)05J\u001a\u0010<\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u0018090=2\u0006\u0010>\u001a\u00020\u001eR\u0017\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0007R\u001a\u0010\b\u001a\u00020\tX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\n\u0010\u000b\"\u0004\b\f\u0010\rR\u0017\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u0007R\u0017\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\u00110\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0007R\u0017\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\u00050\u0014\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u0016R\u001c\u0010\u0017\u001a\u0004\u0018\u00010\u0018X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0019\u0010\u001a\"\u0004\b\u001b\u0010\u001cR\u001c\u0010\u001d\u001a\u0004\u0018\u00010\u001eX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u001f\u0010 \"\u0004\b!\u0010\"R\u001a\u0010#\u001a\u00020\u0011X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b$\u0010%\"\u0004\b&\u0010\'R\u001c\u0010(\u001a\u0004\u0018\u00010)X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b*\u0010+\"\u0004\b,\u0010-R\u001a\u0010.\u001a\u00020\tX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b/\u0010\u000b\"\u0004\b0\u0010\r\u00a8\u0006?"}, d2 = {"Lcom/ekoapp/ekosdk/uikit/community/profile/viewmodel/EkoEditUserProfileViewModel;", "Lcom/ekoapp/ekosdk/uikit/base/EkoBaseViewModel;", "()V", "about", "Landroidx/lifecycle/MutableLiveData;", "", "getAbout", "()Landroidx/lifecycle/MutableLiveData;", "aboutMaxTextLength", "", "getAboutMaxTextLength", "()I", "setAboutMaxTextLength", "(I)V", "displayName", "getDisplayName", "hasProfileUpdate", "", "getHasProfileUpdate", "mediatorLiveData", "Landroidx/lifecycle/MediatorLiveData;", "getMediatorLiveData", "()Landroidx/lifecycle/MediatorLiveData;", "profileImage", "Lcom/ekoapp/ekosdk/file/EkoImage;", "getProfileImage", "()Lcom/ekoapp/ekosdk/file/EkoImage;", "setProfileImage", "(Lcom/ekoapp/ekosdk/file/EkoImage;)V", "profileUri", "Landroid/net/Uri;", "getProfileUri", "()Landroid/net/Uri;", "setProfileUri", "(Landroid/net/Uri;)V", "updating", "getUpdating", "()Z", "setUpdating", "(Z)V", "user", "Lcom/ekoapp/ekosdk/user/EkoUser;", "getUser", "()Lcom/ekoapp/ekosdk/user/EkoUser;", "setUser", "(Lcom/ekoapp/ekosdk/user/EkoUser;)V", "userNameMaxTextLength", "getUserNameMaxTextLength", "setUserNameMaxTextLength", "checkProfileUpdate", "", "errorOnUpdate", "getCurrentProfileUrl", "Lio/reactivex/Single;", "hasDraft", "updateImageUploadStatus", "ekoImageUpload", "Lcom/ekoapp/ekosdk/file/upload/EkoUploadResult;", "updateProfileUri", "updateUser", "uploadProfilePicture", "Lio/reactivex/Flowable;", "uri", "community_debug"})
public final class EkoEditUserProfileViewModel extends com.ekoapp.ekosdk.uikit.base.EkoBaseViewModel {
    @org.jetbrains.annotations.Nullable()
    private com.ekoapp.ekosdk.file.EkoImage profileImage;
    @org.jetbrains.annotations.Nullable()
    private android.net.Uri profileUri;
    private boolean updating = false;
    @org.jetbrains.annotations.Nullable()
    private com.ekoapp.ekosdk.user.EkoUser user;
    private int userNameMaxTextLength = 100;
    private int aboutMaxTextLength = 180;
    @org.jetbrains.annotations.NotNull()
    private final androidx.lifecycle.MutableLiveData<java.lang.String> displayName = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.lifecycle.MutableLiveData<java.lang.String> about = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.lifecycle.MutableLiveData<java.lang.Boolean> hasProfileUpdate = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.lifecycle.MediatorLiveData<java.lang.String> mediatorLiveData = null;
    
    @org.jetbrains.annotations.Nullable()
    public final com.ekoapp.ekosdk.file.EkoImage getProfileImage() {
        return null;
    }
    
    public final void setProfileImage(@org.jetbrains.annotations.Nullable()
    com.ekoapp.ekosdk.file.EkoImage p0) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final android.net.Uri getProfileUri() {
        return null;
    }
    
    public final void setProfileUri(@org.jetbrains.annotations.Nullable()
    android.net.Uri p0) {
    }
    
    public final boolean getUpdating() {
        return false;
    }
    
    public final void setUpdating(boolean p0) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.ekoapp.ekosdk.user.EkoUser getUser() {
        return null;
    }
    
    public final void setUser(@org.jetbrains.annotations.Nullable()
    com.ekoapp.ekosdk.user.EkoUser p0) {
    }
    
    public final int getUserNameMaxTextLength() {
        return 0;
    }
    
    public final void setUserNameMaxTextLength(int p0) {
    }
    
    public final int getAboutMaxTextLength() {
        return 0;
    }
    
    public final void setAboutMaxTextLength(int p0) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.lifecycle.MutableLiveData<java.lang.String> getDisplayName() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.lifecycle.MutableLiveData<java.lang.String> getAbout() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.lifecycle.MutableLiveData<java.lang.Boolean> getHasProfileUpdate() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.lifecycle.MediatorLiveData<java.lang.String> getMediatorLiveData() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final io.reactivex.Single<com.ekoapp.ekosdk.user.EkoUser> updateUser() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final io.reactivex.Flowable<com.ekoapp.ekosdk.file.upload.EkoUploadResult<com.ekoapp.ekosdk.file.EkoImage>> uploadProfilePicture(@org.jetbrains.annotations.NotNull()
    android.net.Uri uri) {
        return null;
    }
    
    public final void updateImageUploadStatus(@org.jetbrains.annotations.NotNull()
    com.ekoapp.ekosdk.file.upload.EkoUploadResult<com.ekoapp.ekosdk.file.EkoImage> ekoImageUpload) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final io.reactivex.Single<com.ekoapp.ekosdk.user.EkoUser> getUser() {
        return null;
    }
    
    public final void updateProfileUri(@org.jetbrains.annotations.Nullable()
    android.net.Uri profileUri) {
    }
    
    private final boolean hasDraft() {
        return false;
    }
    
    private final java.lang.String getCurrentProfileUrl() {
        return null;
    }
    
    public final void checkProfileUpdate() {
    }
    
    public final void errorOnUpdate() {
    }
    
    public EkoEditUserProfileViewModel() {
        super();
    }
}