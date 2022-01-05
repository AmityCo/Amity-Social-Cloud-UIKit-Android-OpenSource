package com.ekoapp.ekosdk.uikit.chat.databinding;
import com.ekoapp.ekosdk.uikit.chat.R;
import com.ekoapp.ekosdk.uikit.chat.BR;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class AmityChatComposeBarBindingImpl extends AmityChatComposeBarBinding implements com.ekoapp.ekosdk.uikit.chat.generated.callback.OnClickListener.Listener {

    @Nullable
    private static final androidx.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = null;
    }
    // views
    @NonNull
    private final androidx.constraintlayout.widget.ConstraintLayout mboundView0;
    @NonNull
    private final android.view.View mboundView1;
    // variables
    @Nullable
    private final android.view.View.OnClickListener mCallback8;
    @Nullable
    private final android.view.View.OnClickListener mCallback6;
    @Nullable
    private final android.view.View.OnClickListener mCallback7;
    @Nullable
    private final android.view.View.OnClickListener mCallback5;
    // values
    // listeners
    // Inverse Binding Event Handlers

    public AmityChatComposeBarBindingImpl(@Nullable androidx.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        this(bindingComponent, root, mapBindings(bindingComponent, root, 10, sIncludes, sViewsWithIds));
    }
    private AmityChatComposeBarBindingImpl(androidx.databinding.DataBindingComponent bindingComponent, View root, Object[] bindings) {
        super(bindingComponent, root, 0
            , (com.google.android.material.imageview.ShapeableImageView) bindings[4]
            , (com.google.android.material.imageview.ShapeableImageView) bindings[2]
            , (com.google.android.material.imageview.ShapeableImageView) bindings[6]
            , (com.google.android.material.imageview.ShapeableImageView) bindings[8]
            , (android.widget.TextView) bindings[5]
            , (android.widget.TextView) bindings[3]
            , (android.widget.TextView) bindings[7]
            , (android.widget.TextView) bindings[9]
            );
        this.ivAlbum.setTag(null);
        this.ivCamera.setTag(null);
        this.ivFile.setTag(null);
        this.ivLocation.setTag(null);
        this.mboundView0 = (androidx.constraintlayout.widget.ConstraintLayout) bindings[0];
        this.mboundView0.setTag(null);
        this.mboundView1 = (android.view.View) bindings[1];
        this.mboundView1.setTag(null);
        this.tvAlbum.setTag(null);
        this.tvCamera.setTag(null);
        this.tvFile.setTag(null);
        this.tvLocation.setTag(null);
        setRootTag(root);
        // listeners
        mCallback8 = new com.ekoapp.ekosdk.uikit.chat.generated.callback.OnClickListener(this, 4);
        mCallback6 = new com.ekoapp.ekosdk.uikit.chat.generated.callback.OnClickListener(this, 2);
        mCallback7 = new com.ekoapp.ekosdk.uikit.chat.generated.callback.OnClickListener(this, 3);
        mCallback5 = new com.ekoapp.ekosdk.uikit.chat.generated.callback.OnClickListener(this, 1);
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
        if (BR.clickListener == variableId) {
            setClickListener((com.ekoapp.ekosdk.uikit.components.EkoChatComposeBarClickListener) variable);
        }
        else {
            variableSet = false;
        }
            return variableSet;
    }

    public void setClickListener(@Nullable com.ekoapp.ekosdk.uikit.components.EkoChatComposeBarClickListener ClickListener) {
        this.mClickListener = ClickListener;
        synchronized(this) {
            mDirtyFlags |= 0x1L;
        }
        notifyPropertyChanged(BR.clickListener);
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
        com.ekoapp.ekosdk.uikit.components.EkoChatComposeBarClickListener clickListener = mClickListener;
        // batch finished
        if ((dirtyFlags & 0x2L) != 0) {
            // api target 1

            this.ivAlbum.setOnClickListener(mCallback6);
            com.ekoapp.ekosdk.uikit.components.BindingUtilityKt.setEkoViewBackgroundColor(this.ivAlbum, getColorFromResource(ivAlbum, R.color.amityColorBase), com.ekoapp.ekosdk.uikit.common.views.ColorShade.SHADE4);
            this.ivCamera.setOnClickListener(mCallback5);
            com.ekoapp.ekosdk.uikit.components.BindingUtilityKt.setEkoViewBackgroundColor(this.ivCamera, getColorFromResource(ivCamera, R.color.amityColorBase), com.ekoapp.ekosdk.uikit.common.views.ColorShade.SHADE4);
            this.ivFile.setOnClickListener(mCallback7);
            com.ekoapp.ekosdk.uikit.components.BindingUtilityKt.setEkoViewBackgroundColor(this.ivFile, getColorFromResource(ivFile, R.color.amityColorBase), com.ekoapp.ekosdk.uikit.common.views.ColorShade.SHADE4);
            this.ivLocation.setOnClickListener(mCallback8);
            com.ekoapp.ekosdk.uikit.components.BindingUtilityKt.setEkoViewBackgroundColor(this.ivLocation, getColorFromResource(ivLocation, R.color.amityColorBase), com.ekoapp.ekosdk.uikit.common.views.ColorShade.SHADE4);
            com.ekoapp.ekosdk.uikit.components.BindingUtilityKt.setEkoViewBackgroundColor(this.mboundView1, getColorFromResource(mboundView1, R.color.amityColorBase), com.ekoapp.ekosdk.uikit.common.views.ColorShade.SHADE4);
            com.ekoapp.ekosdk.uikit.components.BindingUtilityKt.setEkoTextColor(this.tvAlbum, com.ekoapp.ekosdk.uikit.common.views.ColorShade.SHADE1, (com.ekoapp.ekosdk.uikit.common.views.ColorShade)null);
            com.ekoapp.ekosdk.uikit.components.BindingUtilityKt.setEkoTextColor(this.tvCamera, com.ekoapp.ekosdk.uikit.common.views.ColorShade.SHADE1, (com.ekoapp.ekosdk.uikit.common.views.ColorShade)null);
            com.ekoapp.ekosdk.uikit.components.BindingUtilityKt.setEkoTextColor(this.tvFile, com.ekoapp.ekosdk.uikit.common.views.ColorShade.SHADE1, (com.ekoapp.ekosdk.uikit.common.views.ColorShade)null);
            com.ekoapp.ekosdk.uikit.components.BindingUtilityKt.setEkoTextColor(this.tvLocation, com.ekoapp.ekosdk.uikit.common.views.ColorShade.SHADE1, (com.ekoapp.ekosdk.uikit.common.views.ColorShade)null);
        }
    }
    // Listener Stub Implementations
    // callback impls
    public final void _internalCallbackOnClick(int sourceId , android.view.View callbackArg_0) {
        switch(sourceId) {
            case 4: {
                // localize variables for thread safety
                // clickListener
                com.ekoapp.ekosdk.uikit.components.EkoChatComposeBarClickListener clickListener = mClickListener;
                // clickListener != null
                boolean clickListenerJavaLangObjectNull = false;



                clickListenerJavaLangObjectNull = (clickListener) != (null);
                if (clickListenerJavaLangObjectNull) {


                    clickListener.onLocationCLicked();
                }
                break;
            }
            case 2: {
                // localize variables for thread safety
                // clickListener
                com.ekoapp.ekosdk.uikit.components.EkoChatComposeBarClickListener clickListener = mClickListener;
                // clickListener != null
                boolean clickListenerJavaLangObjectNull = false;



                clickListenerJavaLangObjectNull = (clickListener) != (null);
                if (clickListenerJavaLangObjectNull) {


                    clickListener.onAlbumClicked();
                }
                break;
            }
            case 3: {
                // localize variables for thread safety
                // clickListener
                com.ekoapp.ekosdk.uikit.components.EkoChatComposeBarClickListener clickListener = mClickListener;
                // clickListener != null
                boolean clickListenerJavaLangObjectNull = false;



                clickListenerJavaLangObjectNull = (clickListener) != (null);
                if (clickListenerJavaLangObjectNull) {


                    clickListener.onFileClicked();
                }
                break;
            }
            case 1: {
                // localize variables for thread safety
                // clickListener
                com.ekoapp.ekosdk.uikit.components.EkoChatComposeBarClickListener clickListener = mClickListener;
                // clickListener != null
                boolean clickListenerJavaLangObjectNull = false;



                clickListenerJavaLangObjectNull = (clickListener) != (null);
                if (clickListenerJavaLangObjectNull) {


                    clickListener.onCameraClicked();
                }
                break;
            }
        }
    }
    // dirty flag
    private  long mDirtyFlags = 0xffffffffffffffffL;
    /* flag mapping
        flag 0 (0x1L): clickListener
        flag 1 (0x2L): null
    flag mapping end*/
    //end
}