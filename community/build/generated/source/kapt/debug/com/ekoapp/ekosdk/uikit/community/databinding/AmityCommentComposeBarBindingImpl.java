package com.ekoapp.ekosdk.uikit.community.databinding;
import com.ekoapp.ekosdk.uikit.community.R;
import com.ekoapp.ekosdk.uikit.community.BR;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class AmityCommentComposeBarBindingImpl extends AmityCommentComposeBarBinding  {

    @Nullable
    private static final androidx.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.ivExpand, 4);
    }
    // views
    // variables
    // values
    // listeners
    // Inverse Binding Event Handlers

    public AmityCommentComposeBarBindingImpl(@Nullable androidx.databinding.DataBindingComponent bindingComponent, @NonNull View[] root) {
        this(bindingComponent, root, mapBindings(bindingComponent, root, 5, sIncludes, sViewsWithIds));
    }
    private AmityCommentComposeBarBindingImpl(androidx.databinding.DataBindingComponent bindingComponent, View[] root, Object[] bindings) {
        super(bindingComponent, root[0], 0
            , (com.google.android.material.imageview.ShapeableImageView) bindings[0]
            , (android.widget.Button) bindings[3]
            , (com.google.android.material.textfield.TextInputEditText) bindings[2]
            , (android.widget.ImageView) bindings[4]
            , (android.widget.RelativeLayout) bindings[1]
            );
        this.avProfile.setTag(null);
        this.btnPost.setTag(null);
        this.etPostComment.setTag(null);
        this.layoutPostComment.setTag(null);
        setRootTag(root);
        // listeners
        invalidateAll();
    }

    @Override
    public void invalidateAll() {
        synchronized(this) {
                mDirtyFlags = 0x2L;
        }
        requestRebind();
    }

    @Override
    public boolean hasPendingBindings() {
        synchronized(this) {
            if (mDirtyFlags != 0) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean setVariable(int variableId, @Nullable Object variable)  {
        boolean variableSet = true;
        if (BR.avatarUrl == variableId) {
            setAvatarUrl((java.lang.String) variable);
        }
        else {
            variableSet = false;
        }
            return variableSet;
    }

    public void setAvatarUrl(@Nullable java.lang.String AvatarUrl) {
        this.mAvatarUrl = AvatarUrl;
        synchronized(this) {
            mDirtyFlags |= 0x1L;
        }
        notifyPropertyChanged(BR.avatarUrl);
        super.requestRebind();
    }

    @Override
    protected boolean onFieldChange(int localFieldId, Object object, int fieldId) {
        switch (localFieldId) {
        }
        return false;
    }

    @Override
    protected void executeBindings() {
        long dirtyFlags = 0;
        synchronized(this) {
            dirtyFlags = mDirtyFlags;
            mDirtyFlags = 0;
        }
        java.lang.String avatarUrl = mAvatarUrl;
        int comEkoappEkosdkUikitRColorAmityColorBase = 0;

        if ((dirtyFlags & 0x3L) != 0) {
        }
        if ((dirtyFlags & 0x2L) != 0) {

                // read com.ekoapp.ekosdk.uikit.R.color.amityColorBase
                comEkoappEkosdkUikitRColorAmityColorBase = com.ekoapp.ekosdk.uikit.R.color.amityColorBase;
        }
        // batch finished
        if ((dirtyFlags & 0x2L) != 0) {
            // api target 1

            com.ekoapp.ekosdk.uikit.components.BindingUtilityKt.setEkoViewBackgroundColor(this.avProfile, getColorFromResource(avProfile, R.color.amityColorPrimary), com.ekoapp.ekosdk.uikit.common.views.ColorShade.SHADE3);
            com.ekoapp.ekosdk.uikit.components.BindingUtilityKt.setEkoButtonTextColor(this.btnPost, getColorFromResource(btnPost, R.color.amityColorHighlight), (com.ekoapp.ekosdk.uikit.common.views.ColorShade)null, getColorFromResource(btnPost, R.color.amityColorHighlight), com.ekoapp.ekosdk.uikit.common.views.ColorShade.SHADE2);
            com.ekoapp.ekosdk.uikit.components.BindingUtilityKt.setRoundedCorner(this.etPostComment, true, (java.lang.Float)null, (java.lang.Float)null, (java.lang.Float)null, (java.lang.Float)null, comEkoappEkosdkUikitRColorAmityColorBase, (java.lang.Integer)null, com.ekoapp.ekosdk.uikit.common.views.ColorShade.SHADE4);
            com.ekoapp.ekosdk.uikit.components.BindingUtilityKt.setEkoTextColor(this.etPostComment, (com.ekoapp.ekosdk.uikit.common.views.ColorShade)null, com.ekoapp.ekosdk.uikit.common.views.ColorShade.SHADE2);
        }
        if ((dirtyFlags & 0x3L) != 0) {
            // api target 1

            com.ekoapp.ekosdk.uikit.components.BindingUtilityKt.setImageUrl(this.avProfile, avatarUrl, (android.graphics.drawable.Drawable)null);
        }
    }
    // Listener Stub Implementations
    // callback impls
    // dirty flag
    private  long mDirtyFlags = 0xffffffffffffffffL;
    /* flag mapping
        flag 0 (0x1L): avatarUrl
        flag 1 (0x2L): null
    flag mapping end*/
    //end
}