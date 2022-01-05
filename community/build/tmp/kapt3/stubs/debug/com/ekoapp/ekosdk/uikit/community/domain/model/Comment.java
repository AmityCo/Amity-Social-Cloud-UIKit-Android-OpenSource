package com.ekoapp.ekosdk.uikit.community.domain.model;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u00006\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010 \n\u0002\b+\b\u0086\b\u0018\u00002\u00020\u0001Bm\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0007\u0012\b\b\u0002\u0010\b\u001a\u00020\u0007\u0012\u0006\u0010\t\u001a\u00020\n\u0012\u0006\u0010\u000b\u001a\u00020\u0003\u0012\u0006\u0010\f\u001a\u00020\r\u0012\u0006\u0010\u000e\u001a\u00020\r\u0012\u0006\u0010\u000f\u001a\u00020\u0005\u0012\u000e\u0010\u0010\u001a\n\u0012\u0004\u0012\u00020\u0000\u0018\u00010\u0011\u0012\n\b\u0002\u0010\u0012\u001a\u0004\u0018\u00010\u0003\u00a2\u0006\u0002\u0010\u0013J\t\u0010,\u001a\u00020\u0003H\u00c6\u0003J\u0011\u0010-\u001a\n\u0012\u0004\u0012\u00020\u0000\u0018\u00010\u0011H\u00c6\u0003J\u000b\u0010.\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\t\u0010/\u001a\u00020\u0005H\u00c6\u0003J\t\u00100\u001a\u00020\u0007H\u00c6\u0003J\t\u00101\u001a\u00020\u0007H\u00c6\u0003J\t\u00102\u001a\u00020\nH\u00c6\u0003J\t\u00103\u001a\u00020\u0003H\u00c6\u0003J\t\u00104\u001a\u00020\rH\u00c6\u0003J\t\u00105\u001a\u00020\rH\u00c6\u0003J\t\u00106\u001a\u00020\u0005H\u00c6\u0003J\u0081\u0001\u00107\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u00072\b\b\u0002\u0010\b\u001a\u00020\u00072\b\b\u0002\u0010\t\u001a\u00020\n2\b\b\u0002\u0010\u000b\u001a\u00020\u00032\b\b\u0002\u0010\f\u001a\u00020\r2\b\b\u0002\u0010\u000e\u001a\u00020\r2\b\b\u0002\u0010\u000f\u001a\u00020\u00052\u0010\b\u0002\u0010\u0010\u001a\n\u0012\u0004\u0012\u00020\u0000\u0018\u00010\u00112\n\b\u0002\u0010\u0012\u001a\u0004\u0018\u00010\u0003H\u00c6\u0001J\u0013\u00108\u001a\u00020\u00072\b\u00109\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010:\u001a\u00020\rH\u00d6\u0001J\t\u0010;\u001a\u00020\u0003H\u00d6\u0001R\u0011\u0010\u000e\u001a\u00020\r\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u0015R\u0011\u0010\u000f\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0016\u0010\u0017R\u001a\u0010\u000b\u001a\u00020\u0003X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0018\u0010\u0019\"\u0004\b\u001a\u0010\u001bR\u001a\u0010\u0006\u001a\u00020\u0007X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u001c\u0010\u001d\"\u0004\b\u001e\u0010\u001fR\u001a\u0010\b\u001a\u00020\u0007X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b \u0010\u001d\"\u0004\b!\u0010\u001fR\u001a\u0010\u0004\u001a\u00020\u0005X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\"\u0010\u0017\"\u0004\b#\u0010$R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b%\u0010\u0019R\u0013\u0010\u0012\u001a\u0004\u0018\u00010\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b&\u0010\u0019R\u0011\u0010\f\u001a\u00020\r\u00a2\u0006\b\n\u0000\u001a\u0004\b\'\u0010\u0015R\u0019\u0010\u0010\u001a\n\u0012\u0004\u0012\u00020\u0000\u0018\u00010\u0011\u00a2\u0006\b\n\u0000\u001a\u0004\b(\u0010)R\u0011\u0010\t\u001a\u00020\n\u00a2\u0006\b\n\u0000\u001a\u0004\b*\u0010+\u00a8\u0006<"}, d2 = {"Lcom/ekoapp/ekosdk/uikit/community/domain/model/Comment;", "", "id", "", "editedAt", "", "deleted", "", "edited", "user", "Lcom/ekoapp/ekosdk/uikit/community/domain/model/User;", "data", "reactionCount", "", "childrenNumber", "createdAt", "replies", "", "parentId", "(Ljava/lang/String;JZZLcom/ekoapp/ekosdk/uikit/community/domain/model/User;Ljava/lang/String;IIJLjava/util/List;Ljava/lang/String;)V", "getChildrenNumber", "()I", "getCreatedAt", "()J", "getData", "()Ljava/lang/String;", "setData", "(Ljava/lang/String;)V", "getDeleted", "()Z", "setDeleted", "(Z)V", "getEdited", "setEdited", "getEditedAt", "setEditedAt", "(J)V", "getId", "getParentId", "getReactionCount", "getReplies", "()Ljava/util/List;", "getUser", "()Lcom/ekoapp/ekosdk/uikit/community/domain/model/User;", "component1", "component10", "component11", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "component9", "copy", "equals", "other", "hashCode", "toString", "community_debug"})
public final class Comment {
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String id = null;
    private long editedAt;
    private boolean deleted;
    private boolean edited;
    @org.jetbrains.annotations.NotNull()
    private final com.ekoapp.ekosdk.uikit.community.domain.model.User user = null;
    @org.jetbrains.annotations.NotNull()
    private java.lang.String data;
    private final int reactionCount = 0;
    private final int childrenNumber = 0;
    private final long createdAt = 0L;
    @org.jetbrains.annotations.Nullable()
    private final java.util.List<com.ekoapp.ekosdk.uikit.community.domain.model.Comment> replies = null;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.String parentId = null;
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getId() {
        return null;
    }
    
    public final long getEditedAt() {
        return 0L;
    }
    
    public final void setEditedAt(long p0) {
    }
    
    public final boolean getDeleted() {
        return false;
    }
    
    public final void setDeleted(boolean p0) {
    }
    
    public final boolean getEdited() {
        return false;
    }
    
    public final void setEdited(boolean p0) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.ekoapp.ekosdk.uikit.community.domain.model.User getUser() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getData() {
        return null;
    }
    
    public final void setData(@org.jetbrains.annotations.NotNull()
    java.lang.String p0) {
    }
    
    public final int getReactionCount() {
        return 0;
    }
    
    public final int getChildrenNumber() {
        return 0;
    }
    
    public final long getCreatedAt() {
        return 0L;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.util.List<com.ekoapp.ekosdk.uikit.community.domain.model.Comment> getReplies() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getParentId() {
        return null;
    }
    
    public Comment(@org.jetbrains.annotations.NotNull()
    java.lang.String id, long editedAt, boolean deleted, boolean edited, @org.jetbrains.annotations.NotNull()
    com.ekoapp.ekosdk.uikit.community.domain.model.User user, @org.jetbrains.annotations.NotNull()
    java.lang.String data, int reactionCount, int childrenNumber, long createdAt, @org.jetbrains.annotations.Nullable()
    java.util.List<com.ekoapp.ekosdk.uikit.community.domain.model.Comment> replies, @org.jetbrains.annotations.Nullable()
    java.lang.String parentId) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component1() {
        return null;
    }
    
    public final long component2() {
        return 0L;
    }
    
    public final boolean component3() {
        return false;
    }
    
    public final boolean component4() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.ekoapp.ekosdk.uikit.community.domain.model.User component5() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component6() {
        return null;
    }
    
    public final int component7() {
        return 0;
    }
    
    public final int component8() {
        return 0;
    }
    
    public final long component9() {
        return 0L;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.util.List<com.ekoapp.ekosdk.uikit.community.domain.model.Comment> component10() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String component11() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.ekoapp.ekosdk.uikit.community.domain.model.Comment copy(@org.jetbrains.annotations.NotNull()
    java.lang.String id, long editedAt, boolean deleted, boolean edited, @org.jetbrains.annotations.NotNull()
    com.ekoapp.ekosdk.uikit.community.domain.model.User user, @org.jetbrains.annotations.NotNull()
    java.lang.String data, int reactionCount, int childrenNumber, long createdAt, @org.jetbrains.annotations.Nullable()
    java.util.List<com.ekoapp.ekosdk.uikit.community.domain.model.Comment> replies, @org.jetbrains.annotations.Nullable()
    java.lang.String parentId) {
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
}