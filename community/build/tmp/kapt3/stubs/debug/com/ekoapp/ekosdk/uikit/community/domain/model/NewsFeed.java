package com.ekoapp.ekosdk.uikit.community.domain.model;

import java.lang.System;

@kotlinx.android.parcel.Parcelize()
@kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\\\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0010\t\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b5\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0087\b\u0018\u00002\u00020\u0001B\u009d\u0001\u0012\b\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0003\u0012\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u0003\u0012\u0006\u0010\u0007\u001a\u00020\b\u0012\u0006\u0010\t\u001a\u00020\n\u0012\u0006\u0010\u000b\u001a\u00020\n\u0012\b\b\u0002\u0010\f\u001a\u00020\r\u0012\u0010\b\u0002\u0010\u000e\u001a\n\u0012\u0004\u0012\u00020\u0010\u0018\u00010\u000f\u0012\u0010\b\u0002\u0010\u0011\u001a\n\u0012\u0004\u0012\u00020\u0012\u0018\u00010\u000f\u0012\b\b\u0002\u0010\u0013\u001a\u00020\r\u0012\b\b\u0002\u0010\u0014\u001a\u00020\r\u0012\n\b\u0002\u0010\u0015\u001a\u0004\u0018\u00010\u0016\u0012\n\b\u0002\u0010\u0017\u001a\u0004\u0018\u00010\u0018\u00a2\u0006\u0002\u0010\u0019J\u000b\u0010<\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\u0011\u0010=\u001a\n\u0012\u0004\u0012\u00020\u0012\u0018\u00010\u000fH\u00c6\u0003J\t\u0010>\u001a\u00020\rH\u00c6\u0003J\t\u0010?\u001a\u00020\rH\u00c6\u0003J\u000b\u0010@\u001a\u0004\u0018\u00010\u0016H\u00c6\u0003J\u000b\u0010A\u001a\u0004\u0018\u00010\u0018H\u00c6\u0003J\t\u0010B\u001a\u00020\u0003H\u00c6\u0003J\t\u0010C\u001a\u00020\u0003H\u00c6\u0003J\u000b\u0010D\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\t\u0010E\u001a\u00020\bH\u00c6\u0003J\t\u0010F\u001a\u00020\nH\u00c6\u0003J\t\u0010G\u001a\u00020\nH\u00c6\u0003J\t\u0010H\u001a\u00020\rH\u00c6\u0003J\u0011\u0010I\u001a\n\u0012\u0004\u0012\u00020\u0010\u0018\u00010\u000fH\u00c6\u0003J\u00ad\u0001\u0010J\u001a\u00020\u00002\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00032\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u00032\b\b\u0002\u0010\u0007\u001a\u00020\b2\b\b\u0002\u0010\t\u001a\u00020\n2\b\b\u0002\u0010\u000b\u001a\u00020\n2\b\b\u0002\u0010\f\u001a\u00020\r2\u0010\b\u0002\u0010\u000e\u001a\n\u0012\u0004\u0012\u00020\u0010\u0018\u00010\u000f2\u0010\b\u0002\u0010\u0011\u001a\n\u0012\u0004\u0012\u00020\u0012\u0018\u00010\u000f2\b\b\u0002\u0010\u0013\u001a\u00020\r2\b\b\u0002\u0010\u0014\u001a\u00020\r2\n\b\u0002\u0010\u0015\u001a\u0004\u0018\u00010\u00162\n\b\u0002\u0010\u0017\u001a\u0004\u0018\u00010\u0018H\u00c6\u0001J\t\u0010K\u001a\u00020\nH\u00d6\u0001J\u0013\u0010L\u001a\u00020\r2\b\u0010M\u001a\u0004\u0018\u00010NH\u00d6\u0003J\t\u0010O\u001a\u00020\nH\u00d6\u0001J\t\u0010P\u001a\u00020\u0003H\u00d6\u0001J\u0019\u0010Q\u001a\u00020R2\u0006\u0010S\u001a\u00020T2\u0006\u0010U\u001a\u00020\nH\u00d6\u0001R\"\u0010\u0011\u001a\n\u0012\u0004\u0012\u00020\u0012\u0018\u00010\u000fX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u001a\u0010\u001b\"\u0004\b\u001c\u0010\u001dR\u0013\u0010\u0006\u001a\u0004\u0018\u00010\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001e\u0010\u001fR\u001c\u0010\u0017\u001a\u0004\u0018\u00010\u0018X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b \u0010!\"\u0004\b\"\u0010#R\u0011\u0010\u0005\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b$\u0010\u001fR\u001a\u0010\u0013\u001a\u00020\rX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b%\u0010&\"\u0004\b\'\u0010(R\u001c\u0010\u0002\u001a\u0004\u0018\u00010\u0003X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b)\u0010\u001f\"\u0004\b*\u0010+R\"\u0010\u000e\u001a\n\u0012\u0004\u0012\u00020\u0010\u0018\u00010\u000fX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b,\u0010\u001b\"\u0004\b-\u0010\u001dR\u0011\u0010\f\u001a\u00020\r\u00a2\u0006\b\n\u0000\u001a\u0004\b.\u0010&R\u0011\u0010\u000b\u001a\u00020\n\u00a2\u0006\b\n\u0000\u001a\u0004\b/\u00100R\u0011\u0010\t\u001a\u00020\n\u00a2\u0006\b\n\u0000\u001a\u0004\b1\u00100R\u0011\u0010\u0007\u001a\u00020\b\u00a2\u0006\b\n\u0000\u001a\u0004\b2\u00103R\u001a\u0010\u0014\u001a\u00020\rX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b4\u0010&\"\u0004\b5\u0010(R\u001a\u0010\u0004\u001a\u00020\u0003X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b6\u0010\u001f\"\u0004\b7\u0010+R\u001c\u0010\u0015\u001a\u0004\u0018\u00010\u0016X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b8\u00109\"\u0004\b:\u0010;\u00a8\u0006V"}, d2 = {"Lcom/ekoapp/ekosdk/uikit/community/domain/model/NewsFeed;", "Landroid/os/Parcelable;", "id", "", "text", "displayName", "avatarUrl", "postTime", "", "numLikes", "", "numComments", "liked", "", "images", "", "Lcom/ekoapp/ekosdk/uikit/community/newsfeed/model/FeedImage;", "attachments", "Lcom/ekoapp/ekosdk/uikit/community/domain/model/FileAttachment;", "edited", "postedByModerator", "user", "Lcom/ekoapp/ekosdk/uikit/community/domain/model/User;", "channel", "Lcom/ekoapp/ekosdk/uikit/community/domain/model/Channel;", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;JIIZLjava/util/List;Ljava/util/List;ZZLcom/ekoapp/ekosdk/uikit/community/domain/model/User;Lcom/ekoapp/ekosdk/uikit/community/domain/model/Channel;)V", "getAttachments", "()Ljava/util/List;", "setAttachments", "(Ljava/util/List;)V", "getAvatarUrl", "()Ljava/lang/String;", "getChannel", "()Lcom/ekoapp/ekosdk/uikit/community/domain/model/Channel;", "setChannel", "(Lcom/ekoapp/ekosdk/uikit/community/domain/model/Channel;)V", "getDisplayName", "getEdited", "()Z", "setEdited", "(Z)V", "getId", "setId", "(Ljava/lang/String;)V", "getImages", "setImages", "getLiked", "getNumComments", "()I", "getNumLikes", "getPostTime", "()J", "getPostedByModerator", "setPostedByModerator", "getText", "setText", "getUser", "()Lcom/ekoapp/ekosdk/uikit/community/domain/model/User;", "setUser", "(Lcom/ekoapp/ekosdk/uikit/community/domain/model/User;)V", "component1", "component10", "component11", "component12", "component13", "component14", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "component9", "copy", "describeContents", "equals", "other", "", "hashCode", "toString", "writeToParcel", "", "parcel", "Landroid/os/Parcel;", "flags", "community_debug"})
public final class NewsFeed implements android.os.Parcelable {
    @org.jetbrains.annotations.Nullable()
    private java.lang.String id;
    @org.jetbrains.annotations.NotNull()
    private java.lang.String text;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String displayName = null;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.String avatarUrl = null;
    private final long postTime = 0L;
    private final int numLikes = 0;
    private final int numComments = 0;
    private final boolean liked = false;
    @org.jetbrains.annotations.Nullable()
    private java.util.List<com.ekoapp.ekosdk.uikit.community.newsfeed.model.FeedImage> images;
    @org.jetbrains.annotations.Nullable()
    private java.util.List<com.ekoapp.ekosdk.uikit.community.domain.model.FileAttachment> attachments;
    private boolean edited;
    private boolean postedByModerator;
    @org.jetbrains.annotations.Nullable()
    private com.ekoapp.ekosdk.uikit.community.domain.model.User user;
    @org.jetbrains.annotations.Nullable()
    private com.ekoapp.ekosdk.uikit.community.domain.model.Channel channel;
    public static final android.os.Parcelable.Creator CREATOR = null;
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getId() {
        return null;
    }
    
    public final void setId(@org.jetbrains.annotations.Nullable()
    java.lang.String p0) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getText() {
        return null;
    }
    
    public final void setText(@org.jetbrains.annotations.NotNull()
    java.lang.String p0) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getDisplayName() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getAvatarUrl() {
        return null;
    }
    
    public final long getPostTime() {
        return 0L;
    }
    
    public final int getNumLikes() {
        return 0;
    }
    
    public final int getNumComments() {
        return 0;
    }
    
    public final boolean getLiked() {
        return false;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.util.List<com.ekoapp.ekosdk.uikit.community.newsfeed.model.FeedImage> getImages() {
        return null;
    }
    
    public final void setImages(@org.jetbrains.annotations.Nullable()
    java.util.List<com.ekoapp.ekosdk.uikit.community.newsfeed.model.FeedImage> p0) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.util.List<com.ekoapp.ekosdk.uikit.community.domain.model.FileAttachment> getAttachments() {
        return null;
    }
    
    public final void setAttachments(@org.jetbrains.annotations.Nullable()
    java.util.List<com.ekoapp.ekosdk.uikit.community.domain.model.FileAttachment> p0) {
    }
    
    public final boolean getEdited() {
        return false;
    }
    
    public final void setEdited(boolean p0) {
    }
    
    public final boolean getPostedByModerator() {
        return false;
    }
    
    public final void setPostedByModerator(boolean p0) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.ekoapp.ekosdk.uikit.community.domain.model.User getUser() {
        return null;
    }
    
    public final void setUser(@org.jetbrains.annotations.Nullable()
    com.ekoapp.ekosdk.uikit.community.domain.model.User p0) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.ekoapp.ekosdk.uikit.community.domain.model.Channel getChannel() {
        return null;
    }
    
    public final void setChannel(@org.jetbrains.annotations.Nullable()
    com.ekoapp.ekosdk.uikit.community.domain.model.Channel p0) {
    }
    
    public NewsFeed(@org.jetbrains.annotations.Nullable()
    java.lang.String id, @org.jetbrains.annotations.NotNull()
    java.lang.String text, @org.jetbrains.annotations.NotNull()
    java.lang.String displayName, @org.jetbrains.annotations.Nullable()
    java.lang.String avatarUrl, long postTime, int numLikes, int numComments, boolean liked, @org.jetbrains.annotations.Nullable()
    java.util.List<com.ekoapp.ekosdk.uikit.community.newsfeed.model.FeedImage> images, @org.jetbrains.annotations.Nullable()
    java.util.List<com.ekoapp.ekosdk.uikit.community.domain.model.FileAttachment> attachments, boolean edited, boolean postedByModerator, @org.jetbrains.annotations.Nullable()
    com.ekoapp.ekosdk.uikit.community.domain.model.User user, @org.jetbrains.annotations.Nullable()
    com.ekoapp.ekosdk.uikit.community.domain.model.Channel channel) {
        super();
    }
    
    @org.jetbrains.annotations.Nullable()
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
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String component4() {
        return null;
    }
    
    public final long component5() {
        return 0L;
    }
    
    public final int component6() {
        return 0;
    }
    
    public final int component7() {
        return 0;
    }
    
    public final boolean component8() {
        return false;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.util.List<com.ekoapp.ekosdk.uikit.community.newsfeed.model.FeedImage> component9() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.util.List<com.ekoapp.ekosdk.uikit.community.domain.model.FileAttachment> component10() {
        return null;
    }
    
    public final boolean component11() {
        return false;
    }
    
    public final boolean component12() {
        return false;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.ekoapp.ekosdk.uikit.community.domain.model.User component13() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.ekoapp.ekosdk.uikit.community.domain.model.Channel component14() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.ekoapp.ekosdk.uikit.community.domain.model.NewsFeed copy(@org.jetbrains.annotations.Nullable()
    java.lang.String id, @org.jetbrains.annotations.NotNull()
    java.lang.String text, @org.jetbrains.annotations.NotNull()
    java.lang.String displayName, @org.jetbrains.annotations.Nullable()
    java.lang.String avatarUrl, long postTime, int numLikes, int numComments, boolean liked, @org.jetbrains.annotations.Nullable()
    java.util.List<com.ekoapp.ekosdk.uikit.community.newsfeed.model.FeedImage> images, @org.jetbrains.annotations.Nullable()
    java.util.List<com.ekoapp.ekosdk.uikit.community.domain.model.FileAttachment> attachments, boolean edited, boolean postedByModerator, @org.jetbrains.annotations.Nullable()
    com.ekoapp.ekosdk.uikit.community.domain.model.User user, @org.jetbrains.annotations.Nullable()
    com.ekoapp.ekosdk.uikit.community.domain.model.Channel channel) {
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