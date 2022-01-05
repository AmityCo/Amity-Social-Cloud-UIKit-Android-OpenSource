package com.ekoapp.ekosdk.uikit.community.domain.model;

import java.lang.System;

@kotlinx.android.parcel.Parcelize()
@kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000F\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\t\n\u0000\n\u0002\u0010 \n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b \n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0087\b\u0018\u00002\u00020\u0001B_\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0003\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\f\u0010\b\u001a\b\u0012\u0004\u0012\u00020\u00030\t\u0012\u0006\u0010\n\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u000b\u001a\u00020\f\u0012\b\b\u0002\u0010\r\u001a\u00020\f\u0012\u000e\b\u0002\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u00030\t\u00a2\u0006\u0002\u0010\u000fJ\t\u0010\"\u001a\u00020\u0003H\u00c6\u0003J\t\u0010#\u001a\u00020\u0003H\u00c6\u0003J\t\u0010$\u001a\u00020\u0003H\u00c6\u0003J\t\u0010%\u001a\u00020\u0007H\u00c6\u0003J\u000f\u0010&\u001a\b\u0012\u0004\u0012\u00020\u00030\tH\u00c6\u0003J\t\u0010\'\u001a\u00020\u0003H\u00c6\u0003J\t\u0010(\u001a\u00020\fH\u00c6\u0003J\t\u0010)\u001a\u00020\fH\u00c6\u0003J\u000f\u0010*\u001a\b\u0012\u0004\u0012\u00020\u00030\tH\u00c6\u0003Jo\u0010+\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00032\b\b\u0002\u0010\u0006\u001a\u00020\u00072\u000e\b\u0002\u0010\b\u001a\b\u0012\u0004\u0012\u00020\u00030\t2\b\b\u0002\u0010\n\u001a\u00020\u00032\b\b\u0002\u0010\u000b\u001a\u00020\f2\b\b\u0002\u0010\r\u001a\u00020\f2\u000e\b\u0002\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u00030\tH\u00c6\u0001J\t\u0010,\u001a\u00020-H\u00d6\u0001J\u0013\u0010.\u001a\u00020\f2\b\u0010/\u001a\u0004\u0018\u000100H\u00d6\u0003J\t\u00101\u001a\u00020-H\u00d6\u0001J\t\u00102\u001a\u00020\u0003H\u00d6\u0001J\u0019\u00103\u001a\u0002042\u0006\u00105\u001a\u0002062\u0006\u00107\u001a\u00020-H\u00d6\u0001R\u0011\u0010\u0005\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0011R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0011R\u0011\u0010\n\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0011R\u0017\u0010\b\u001a\b\u0012\u0004\u0012\u00020\u00030\t\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u0015R \u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u00030\tX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0016\u0010\u0015\"\u0004\b\u0017\u0010\u0018R\u0011\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0019\u0010\u001aR\u001a\u0010\r\u001a\u00020\fX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u001b\u0010\u001c\"\u0004\b\u001d\u0010\u001eR\u0011\u0010\u0004\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001f\u0010\u0011R\u001a\u0010\u000b\u001a\u00020\fX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b \u0010\u001c\"\u0004\b!\u0010\u001e\u00a8\u00068"}, d2 = {"Lcom/ekoapp/ekosdk/uikit/community/domain/model/Channel;", "Landroid/os/Parcelable;", "channelName", "", "profileImageUrl", "backdropImageUrl", "numberOfFollowers", "", "followersName", "", "description", "verified", "", "private", "moderators", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;JLjava/util/List;Ljava/lang/String;ZZLjava/util/List;)V", "getBackdropImageUrl", "()Ljava/lang/String;", "getChannelName", "getDescription", "getFollowersName", "()Ljava/util/List;", "getModerators", "setModerators", "(Ljava/util/List;)V", "getNumberOfFollowers", "()J", "getPrivate", "()Z", "setPrivate", "(Z)V", "getProfileImageUrl", "getVerified", "setVerified", "component1", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "component9", "copy", "describeContents", "", "equals", "other", "", "hashCode", "toString", "writeToParcel", "", "parcel", "Landroid/os/Parcel;", "flags", "community_debug"})
public final class Channel implements android.os.Parcelable {
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String channelName = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String profileImageUrl = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String backdropImageUrl = null;
    private final long numberOfFollowers = 0L;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<java.lang.String> followersName = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String description = null;
    private boolean verified;
    @org.jetbrains.annotations.NotNull()
    private java.util.List<java.lang.String> moderators;
    public static final android.os.Parcelable.Creator CREATOR = null;
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getChannelName() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getProfileImageUrl() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getBackdropImageUrl() {
        return null;
    }
    
    public final long getNumberOfFollowers() {
        return 0L;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<java.lang.String> getFollowersName() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getDescription() {
        return null;
    }
    
    public final boolean getVerified() {
        return false;
    }
    
    public final void setVerified(boolean p0) {
    }
    
    public final boolean getPrivate() {
        return false;
    }
    
    public final void setPrivate(boolean p0) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<java.lang.String> getModerators() {
        return null;
    }
    
    public final void setModerators(@org.jetbrains.annotations.NotNull()
    java.util.List<java.lang.String> p0) {
    }
    
    public Channel(@org.jetbrains.annotations.NotNull()
    java.lang.String channelName, @org.jetbrains.annotations.NotNull()
    java.lang.String profileImageUrl, @org.jetbrains.annotations.NotNull()
    java.lang.String backdropImageUrl, long numberOfFollowers, @org.jetbrains.annotations.NotNull()
    java.util.List<java.lang.String> followersName, @org.jetbrains.annotations.NotNull()
    java.lang.String description, boolean verified, boolean p7_1990234817, @org.jetbrains.annotations.NotNull()
    java.util.List<java.lang.String> moderators) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component1() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component2() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component3() {
        return null;
    }
    
    public final long component4() {
        return 0L;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<java.lang.String> component5() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component6() {
        return null;
    }
    
    public final boolean component7() {
        return false;
    }
    
    public final boolean component8() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<java.lang.String> component9() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.ekoapp.ekosdk.uikit.community.domain.model.Channel copy(@org.jetbrains.annotations.NotNull()
    java.lang.String channelName, @org.jetbrains.annotations.NotNull()
    java.lang.String profileImageUrl, @org.jetbrains.annotations.NotNull()
    java.lang.String backdropImageUrl, long numberOfFollowers, @org.jetbrains.annotations.NotNull()
    java.util.List<java.lang.String> followersName, @org.jetbrains.annotations.NotNull()
    java.lang.String description, boolean verified, boolean p7_1990234817, @org.jetbrains.annotations.NotNull()
    java.util.List<java.lang.String> moderators) {
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