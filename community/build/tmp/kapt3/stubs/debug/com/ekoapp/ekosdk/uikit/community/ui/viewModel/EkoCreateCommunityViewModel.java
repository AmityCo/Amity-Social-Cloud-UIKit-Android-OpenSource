package com.ekoapp.ekosdk.uikit.community.ui.viewModel;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000v\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0018\u0002\n\u0002\b\f\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0006\u00105\u001a\u000206J\u000e\u00107\u001a\u0002062\u0006\u00108\u001a\u00020&J\f\u00109\u001a\b\u0012\u0004\u0012\u00020;0:J\u0006\u0010<\u001a\u000206J\f\u0010=\u001a\b\u0012\u0004\u0012\u00020;0:J\f\u0010>\u001a\b\u0012\u0004\u0012\u00020;0?J\b\u0010@\u001a\u000206H\u0002J\u000e\u0010A\u001a\u0002062\u0006\u0010\r\u001a\u00020\u000fJ\u000e\u0010B\u001a\u0002062\u0006\u0010C\u001a\u00020;J\u0006\u0010D\u001a\u000206J\u001a\u0010E\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00190F0?2\u0006\u0010G\u001a\u00020HR\u0011\u0010\u0003\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006R\u001f\u0010\u0007\u001a\u0010\u0012\f\u0012\n \n*\u0004\u0018\u00010\t0\t0\b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\fR\u0017\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u000f0\u000e\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0011R\u0017\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\t0\b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\fR\u0017\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\t0\b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\fR\u001f\u0010\u0016\u001a\u0010\u0012\f\u0012\n \n*\u0004\u0018\u00010\t0\t0\b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0017\u0010\fR\u001c\u0010\u0018\u001a\u0004\u0018\u00010\u0019X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u001a\u0010\u001b\"\u0004\b\u001c\u0010\u001dR\u001a\u0010\u001e\u001a\u00020\tX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u001f\u0010 \"\u0004\b!\u0010\"R\u000e\u0010#\u001a\u00020\tX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010$\u001a\u00020\tX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010%\u001a\u00020&X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0017\u0010\'\u001a\b\u0012\u0004\u0012\u00020&0(\u00a2\u0006\b\n\u0000\u001a\u0004\b)\u0010*R\u0011\u0010+\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b+\u0010\u0006R\u0011\u0010,\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b,\u0010\u0006R\u0011\u0010-\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b.\u0010\u0006R\u0017\u0010/\u001a\b\u0012\u0004\u0012\u00020100\u00a2\u0006\b\n\u0000\u001a\u0004\b2\u00103R\u0014\u00104\u001a\b\u0012\u0004\u0012\u00020\t00X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006I"}, d2 = {"Lcom/ekoapp/ekosdk/uikit/community/ui/viewModel/EkoCreateCommunityViewModel;", "Lcom/ekoapp/ekosdk/uikit/base/EkoBaseViewModel;", "()V", "addMemberVisible", "Landroidx/databinding/ObservableBoolean;", "getAddMemberVisible", "()Landroidx/databinding/ObservableBoolean;", "avatarUrl", "Landroidx/databinding/ObservableField;", "", "kotlin.jvm.PlatformType", "getAvatarUrl", "()Landroidx/databinding/ObservableField;", "category", "Landroidx/databinding/ObservableParcelable;", "Lcom/ekoapp/ekosdk/uikit/community/data/SelectCategoryItem;", "getCategory", "()Landroidx/databinding/ObservableParcelable;", "communityId", "getCommunityId", "communityName", "getCommunityName", "description", "getDescription", "ekoImage", "Lcom/ekoapp/ekosdk/file/EkoImage;", "getEkoImage", "()Lcom/ekoapp/ekosdk/file/EkoImage;", "setEkoImage", "(Lcom/ekoapp/ekosdk/file/EkoImage;)V", "initialCategory", "getInitialCategory", "()Ljava/lang/String;", "setInitialCategory", "(Ljava/lang/String;)V", "initialCommunityDescription", "initialCommunityName", "initialIsPublic", "", "initialStateChanged", "Landroidx/lifecycle/MutableLiveData;", "getInitialStateChanged", "()Landroidx/lifecycle/MutableLiveData;", "isAdmin", "isPublic", "nameError", "getNameError", "selectedMembersList", "Ljava/util/ArrayList;", "Lcom/ekoapp/ekosdk/uikit/community/data/SelectMemberItem;", "getSelectedMembersList", "()Ljava/util/ArrayList;", "userIdList", "changeAdminPost", "", "changePostType", "value", "createCommunity", "Lio/reactivex/Single;", "Lcom/ekoapp/ekosdk/community/EkoCommunity;", "createIdList", "editCommunity", "getCommunityDetail", "Lio/reactivex/Flowable;", "resetError", "setCategory", "setCommunityDetails", "ekoCommunity", "setPropertyChangeCallback", "uploadProfilePicture", "Lcom/ekoapp/ekosdk/file/upload/EkoUploadResult;", "uri", "Landroid/net/Uri;", "community_debug"})
public final class EkoCreateCommunityViewModel extends com.ekoapp.ekosdk.uikit.base.EkoBaseViewModel {
    @org.jetbrains.annotations.NotNull()
    private final androidx.lifecycle.MutableLiveData<java.lang.Boolean> initialStateChanged = null;
    private java.lang.String initialCommunityName = "";
    private java.lang.String initialCommunityDescription = "";
    private boolean initialIsPublic = true;
    @org.jetbrains.annotations.NotNull()
    private java.lang.String initialCategory = "";
    @org.jetbrains.annotations.NotNull()
    private final androidx.databinding.ObservableField<java.lang.String> avatarUrl = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.databinding.ObservableField<java.lang.String> communityId = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.databinding.ObservableField<java.lang.String> communityName = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.databinding.ObservableField<java.lang.String> description = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.databinding.ObservableBoolean isPublic = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.databinding.ObservableBoolean isAdmin = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.databinding.ObservableBoolean addMemberVisible = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.databinding.ObservableParcelable<com.ekoapp.ekosdk.uikit.community.data.SelectCategoryItem> category = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.databinding.ObservableBoolean nameError = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.ArrayList<com.ekoapp.ekosdk.uikit.community.data.SelectMemberItem> selectedMembersList = null;
    private final java.util.ArrayList<java.lang.String> userIdList = null;
    @org.jetbrains.annotations.Nullable()
    private com.ekoapp.ekosdk.file.EkoImage ekoImage;
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.lifecycle.MutableLiveData<java.lang.Boolean> getInitialStateChanged() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getInitialCategory() {
        return null;
    }
    
    public final void setInitialCategory(@org.jetbrains.annotations.NotNull()
    java.lang.String p0) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.databinding.ObservableField<java.lang.String> getAvatarUrl() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.databinding.ObservableField<java.lang.String> getCommunityId() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.databinding.ObservableField<java.lang.String> getCommunityName() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.databinding.ObservableField<java.lang.String> getDescription() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.databinding.ObservableBoolean isPublic() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.databinding.ObservableBoolean isAdmin() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.databinding.ObservableBoolean getAddMemberVisible() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.databinding.ObservableParcelable<com.ekoapp.ekosdk.uikit.community.data.SelectCategoryItem> getCategory() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.databinding.ObservableBoolean getNameError() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.ArrayList<com.ekoapp.ekosdk.uikit.community.data.SelectMemberItem> getSelectedMembersList() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.ekoapp.ekosdk.file.EkoImage getEkoImage() {
        return null;
    }
    
    public final void setEkoImage(@org.jetbrains.annotations.Nullable()
    com.ekoapp.ekosdk.file.EkoImage p0) {
    }
    
    public final void changePostType(boolean value) {
    }
    
    public final void changeAdminPost() {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final io.reactivex.Flowable<com.ekoapp.ekosdk.file.upload.EkoUploadResult<com.ekoapp.ekosdk.file.EkoImage>> uploadProfilePicture(@org.jetbrains.annotations.NotNull()
    android.net.Uri uri) {
        return null;
    }
    
    public final void setCategory(@org.jetbrains.annotations.NotNull()
    com.ekoapp.ekosdk.uikit.community.data.SelectCategoryItem category) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final io.reactivex.Single<com.ekoapp.ekosdk.community.EkoCommunity> createCommunity() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final io.reactivex.Single<com.ekoapp.ekosdk.community.EkoCommunity> editCommunity() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final io.reactivex.Flowable<com.ekoapp.ekosdk.community.EkoCommunity> getCommunityDetail() {
        return null;
    }
    
    public final void setCommunityDetails(@org.jetbrains.annotations.NotNull()
    com.ekoapp.ekosdk.community.EkoCommunity ekoCommunity) {
    }
    
    public final void createIdList() {
    }
    
    private final void resetError() {
    }
    
    public final void setPropertyChangeCallback() {
    }
    
    public EkoCreateCommunityViewModel() {
        super();
    }
}