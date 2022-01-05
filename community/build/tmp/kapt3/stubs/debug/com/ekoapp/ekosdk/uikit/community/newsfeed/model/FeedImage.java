package com.ekoapp.ekosdk.uikit.community.newsfeed.model;

import java.lang.System;

@kotlinx.android.parcel.Parcelize()
@kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000B\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0019\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0087\b\u0018\u00002\u00020\u0001B5\u0012\b\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u0012\b\u0010\u0004\u001a\u0004\u0018\u00010\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u0012\b\b\u0002\u0010\u0007\u001a\u00020\b\u0012\b\b\u0002\u0010\t\u001a\u00020\n\u00a2\u0006\u0002\u0010\u000bJ\u000b\u0010\u001c\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\u000b\u0010\u001d\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\t\u0010\u001e\u001a\u00020\u0006H\u00c6\u0003J\t\u0010\u001f\u001a\u00020\bH\u00c6\u0003J\t\u0010 \u001a\u00020\nH\u00c6\u0003J?\u0010!\u001a\u00020\u00002\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00062\b\b\u0002\u0010\u0007\u001a\u00020\b2\b\b\u0002\u0010\t\u001a\u00020\nH\u00c6\u0001J\t\u0010\"\u001a\u00020\nH\u00d6\u0001J\u0013\u0010#\u001a\u00020$2\b\u0010%\u001a\u0004\u0018\u00010&H\u00d6\u0003J\t\u0010\'\u001a\u00020\nH\u00d6\u0001J\t\u0010(\u001a\u00020\u0003H\u00d6\u0001J\u0019\u0010)\u001a\u00020*2\u0006\u0010+\u001a\u00020,2\u0006\u0010-\u001a\u00020\nH\u00d6\u0001R\u001a\u0010\t\u001a\u00020\nX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\f\u0010\r\"\u0004\b\u000e\u0010\u000fR\u001c\u0010\u0002\u001a\u0004\u0018\u00010\u0003X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0010\u0010\u0011\"\u0004\b\u0012\u0010\u0013R\u001c\u0010\u0004\u001a\u0004\u0018\u00010\u0003X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0014\u0010\u0011\"\u0004\b\u0015\u0010\u0013R\u001a\u0010\u0007\u001a\u00020\bX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0016\u0010\u0017\"\u0004\b\u0018\u0010\u0019R\u0011\u0010\u0005\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001a\u0010\u001b\u00a8\u0006."}, d2 = {"Lcom/ekoapp/ekosdk/uikit/community/newsfeed/model/FeedImage;", "Landroid/os/Parcelable;", "id", "", "uploadId", "url", "Landroid/net/Uri;", "uploadState", "Lcom/ekoapp/ekosdk/uikit/community/newsfeed/model/FileUploadState;", "currentProgress", "", "(Ljava/lang/String;Ljava/lang/String;Landroid/net/Uri;Lcom/ekoapp/ekosdk/uikit/community/newsfeed/model/FileUploadState;I)V", "getCurrentProgress", "()I", "setCurrentProgress", "(I)V", "getId", "()Ljava/lang/String;", "setId", "(Ljava/lang/String;)V", "getUploadId", "setUploadId", "getUploadState", "()Lcom/ekoapp/ekosdk/uikit/community/newsfeed/model/FileUploadState;", "setUploadState", "(Lcom/ekoapp/ekosdk/uikit/community/newsfeed/model/FileUploadState;)V", "getUrl", "()Landroid/net/Uri;", "component1", "component2", "component3", "component4", "component5", "copy", "describeContents", "equals", "", "other", "", "hashCode", "toString", "writeToParcel", "", "parcel", "Landroid/os/Parcel;", "flags", "community_debug"})
public final class FeedImage implements android.os.Parcelable {
    @org.jetbrains.annotations.Nullable()
    private java.lang.String id;
    @org.jetbrains.annotations.Nullable()
    private java.lang.String uploadId;
    @org.jetbrains.annotations.NotNull()
    private final android.net.Uri url = null;
    @org.jetbrains.annotations.NotNull()
    private com.ekoapp.ekosdk.uikit.community.newsfeed.model.FileUploadState uploadState;
    private int currentProgress;
    public static final android.os.Parcelable.Creator CREATOR = null;
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getId() {
        return null;
    }
    
    public final void setId(@org.jetbrains.annotations.Nullable()
    java.lang.String p0) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getUploadId() {
        return null;
    }
    
    public final void setUploadId(@org.jetbrains.annotations.Nullable()
    java.lang.String p0) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final android.net.Uri getUrl() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.ekoapp.ekosdk.uikit.community.newsfeed.model.FileUploadState getUploadState() {
        return null;
    }
    
    public final void setUploadState(@org.jetbrains.annotations.NotNull()
    com.ekoapp.ekosdk.uikit.community.newsfeed.model.FileUploadState p0) {
    }
    
    public final int getCurrentProgress() {
        return 0;
    }
    
    public final void setCurrentProgress(int p0) {
    }
    
    public FeedImage(@org.jetbrains.annotations.Nullable()
    java.lang.String id, @org.jetbrains.annotations.Nullable()
    java.lang.String uploadId, @org.jetbrains.annotations.NotNull()
    android.net.Uri url, @org.jetbrains.annotations.NotNull()
    com.ekoapp.ekosdk.uikit.community.newsfeed.model.FileUploadState uploadState, int currentProgress) {
        super();
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String component1() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String component2() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final android.net.Uri component3() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.ekoapp.ekosdk.uikit.community.newsfeed.model.FileUploadState component4() {
        return null;
    }
    
    public final int component5() {
        return 0;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.ekoapp.ekosdk.uikit.community.newsfeed.model.FeedImage copy(@org.jetbrains.annotations.Nullable()
    java.lang.String id, @org.jetbrains.annotations.Nullable()
    java.lang.String uploadId, @org.jetbrains.annotations.NotNull()
    android.net.Uri url, @org.jetbrains.annotations.NotNull()
    com.ekoapp.ekosdk.uikit.community.newsfeed.model.FileUploadState uploadState, int currentProgress) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    @java.lang.Override()
    public java.lang.String toString() {
        return null;
    }
    
    @java.lang.Override()
    public int hashCode() {
        return 0;
    }
    
    @java.lang.Override()
    public boolean equals(@org.jetbrains.annotations.Nullable()
    java.lang.Object p0) {
        return false;
    }
    
    @java.lang.Override()
    public int describeContents() {
        return 0;
    }
    
    @java.lang.Override()
    public void writeToParcel(@org.jetbrains.annotations.NotNull()
    android.os.Parcel parcel, int flags) {
    }
    
    @kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 3)
    public static final class Creator implements android.os.Parcelable.Creator {
        
        public Creator() {
            super();
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.lang.Object[] newArray(int size) {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.lang.Object createFromParcel(@org.jetbrains.annotations.NotNull()
        android.os.Parcel in) {
            return null;
        }
    }
}