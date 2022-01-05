package com.ekoapp.ekosdk.uikit.community.ui.viewModel;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000r\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0010\b\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0005\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0012\u0010&\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020)0(0\'J\u0016\u0010*\u001a\u00020+2\u0006\u0010,\u001a\u00020\"2\u0006\u0010-\u001a\u00020\u0012J\u0012\u0010.\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020)0(0\'J\u0006\u0010/\u001a\u00020+R\u0011\u0010\u0003\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0003\u0010\u0005R\u0017\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\b0\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\t\u0010\nR-\u0010\u000b\u001a\u001e\u0012\u0004\u0012\u00020\b\u0012\u0004\u0012\u00020\r0\fj\u000e\u0012\u0004\u0012\u00020\b\u0012\u0004\u0012\u00020\r`\u000e\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u0010R\u0017\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u00120\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\nR-\u0010\u0014\u001a\u001e\u0012\u0004\u0012\u00020\b\u0012\u0004\u0012\u00020\r0\fj\u000e\u0012\u0004\u0012\u00020\b\u0012\u0004\u0012\u00020\r`\u000e\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u0010R\u001f\u0010\u0016\u001a\u0010\u0012\f\u0012\n \u0018*\u0004\u0018\u00010\b0\b0\u0017\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0019\u0010\u001aR!\u0010\u001b\u001a\u0012\u0012\u0004\u0012\u00020\b0\u001cj\b\u0012\u0004\u0012\u00020\b`\u001d\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001e\u0010\u001fR!\u0010 \u001a\u0012\u0012\u0004\u0012\u00020\"0!j\b\u0012\u0004\u0012\u00020\"`#\u00a2\u0006\b\n\u0000\u001a\u0004\b$\u0010%\u00a8\u00060"}, d2 = {"Lcom/ekoapp/ekosdk/uikit/community/ui/viewModel/EkoSelectMembersViewModel;", "Lcom/ekoapp/ekosdk/uikit/base/EkoBaseViewModel;", "()V", "isSearchUser", "Landroidx/databinding/ObservableBoolean;", "()Landroidx/databinding/ObservableBoolean;", "leftString", "Landroidx/lifecycle/MutableLiveData;", "", "getLeftString", "()Landroidx/lifecycle/MutableLiveData;", "memberMap", "Ljava/util/HashMap;", "", "Lkotlin/collections/HashMap;", "getMemberMap", "()Ljava/util/HashMap;", "rightStringActive", "", "getRightStringActive", "searchMemberMap", "getSearchMemberMap", "searchString", "Landroidx/databinding/ObservableField;", "kotlin.jvm.PlatformType", "getSearchString", "()Landroidx/databinding/ObservableField;", "selectedMemberSet", "Ljava/util/HashSet;", "Lkotlin/collections/HashSet;", "getSelectedMemberSet", "()Ljava/util/HashSet;", "selectedMembersList", "Ljava/util/ArrayList;", "Lcom/ekoapp/ekosdk/uikit/community/data/SelectMemberItem;", "Lkotlin/collections/ArrayList;", "getSelectedMembersList", "()Ljava/util/ArrayList;", "getAllUsers", "Lio/reactivex/Flowable;", "Landroidx/paging/PagedList;", "Lcom/ekoapp/ekosdk/user/EkoUser;", "prepareSelectedMembersList", "", "member", "isSelected", "searchUser", "setPropertyChangeCallback", "community_debug"})
public final class EkoSelectMembersViewModel extends com.ekoapp.ekosdk.uikit.base.EkoBaseViewModel {
    @org.jetbrains.annotations.NotNull()
    private final androidx.databinding.ObservableField<java.lang.String> searchString = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.ArrayList<com.ekoapp.ekosdk.uikit.community.data.SelectMemberItem> selectedMembersList = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.HashSet<java.lang.String> selectedMemberSet = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.HashMap<java.lang.String, java.lang.Integer> memberMap = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.HashMap<java.lang.String, java.lang.Integer> searchMemberMap = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.databinding.ObservableBoolean isSearchUser = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.lifecycle.MutableLiveData<java.lang.String> leftString = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.lifecycle.MutableLiveData<java.lang.Boolean> rightStringActive = null;
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.databinding.ObservableField<java.lang.String> getSearchString() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.ArrayList<com.ekoapp.ekosdk.uikit.community.data.SelectMemberItem> getSelectedMembersList() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.HashSet<java.lang.String> getSelectedMemberSet() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.HashMap<java.lang.String, java.lang.Integer> getMemberMap() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.HashMap<java.lang.String, java.lang.Integer> getSearchMemberMap() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.databinding.ObservableBoolean isSearchUser() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.lifecycle.MutableLiveData<java.lang.String> getLeftString() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.lifecycle.MutableLiveData<java.lang.Boolean> getRightStringActive() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final io.reactivex.Flowable<androidx.paging.PagedList<com.ekoapp.ekosdk.user.EkoUser>> getAllUsers() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final io.reactivex.Flowable<androidx.paging.PagedList<com.ekoapp.ekosdk.user.EkoUser>> searchUser() {
        return null;
    }
    
    public final void prepareSelectedMembersList(@org.jetbrains.annotations.NotNull()
    com.ekoapp.ekosdk.uikit.community.data.SelectMemberItem member, boolean isSelected) {
    }
    
    public final void setPropertyChangeCallback() {
    }
    
    public EkoSelectMembersViewModel() {
        super();
    }
}