package com.ekoapp.ekosdk.uikit.community.databinding;
import com.ekoapp.ekosdk.uikit.community.R;
import com.ekoapp.ekosdk.uikit.community.BR;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class AmityItemMyCommunityBindingImpl extends AmityItemMyCommunityBinding implements com.ekoapp.ekosdk.uikit.community.generated.callback.OnClickListener.Listener {

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
    // variables
    @Nullable
    private final android.view.View.OnClickListener mCallback1;
    // values
    // listeners
    // Inverse Binding Event Handlers

    public AmityItemMyCommunityBindingImpl(@Nullable androidx.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        this(bindingComponent, root, mapBindings(bindingComponent, root, 4, sIncludes, sViewsWithIds));
    }
    private AmityItemMyCommunityBindingImpl(androidx.databinding.DataBindingComponent bindingComponent, View root, Object[] bindings) {
        super(bindingComponent, root, 0
            , (com.google.android.material.imageview.ShapeableImageView) bindings[1]
            , (android.widget.ImageView) bindings[3]
            , (android.widget.TextView) bindings[2]
            );
        this.ivAvatar.setTag(null);
        this.ivOfficial.setTag(null);
        this.mboundView0 = (androidx.constraintlayout.widget.ConstraintLayout) bindings[0];
        this.mboundView0.setTag(null);
        this.tvName.setTag(null);
        setRootTag(root);
        // listeners
        mCallback1 = new com.ekoapp.ekosdk.uikit.community.generated.callback.OnClickListener(this, 1);
        invalidateAll();
    }

    @Override
    public void invalidateAll() {
        synchronized(this) {
                mDirtyFlags = 0x4L;
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
        if (BR.ekoCommunity == variableId) {
            setEkoCommunity((com.ekoapp.ekosdk.community.EkoCommunity) variable);
        }
        else if (BR.listener == variableId) {
            setListener((com.ekoapp.ekosdk.uikit.community.mycommunity.listener.IMyCommunityItemClickListener) variable);
        }
        else {
            variableSet = false;
        }
            return variableSet;
    }

    public void setEkoCommunity(@Nullable com.ekoapp.ekosdk.community.EkoCommunity EkoCommunity) {
        this.mEkoCommunity = EkoCommunity;
        synchronized(this) {
            mDirtyFlags |= 0x1L;
        }
        notifyPropertyChanged(BR.ekoCommunity);
        super.requestRebind();
    }
    public void setListener(@Nullable com.ekoapp.ekosdk.uikit.community.mycommunity.listener.IMyCommunityItemClickListener Listener) {
        this.mListener = Listener;
        synchronized(this) {
            mDirtyFlags |= 0x2L;
        }
        notifyPropertyChanged(BR.listener);
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
        com.ekoapp.ekosdk.community.EkoCommunity ekoCommunity = mEkoCommunity;
        java.lang.String ekoCommunityDisplayName = null;
        boolean ekoCommunityOfficial = false;
        int ekoCommunityOfficialViewVISIBLEViewGONE = 0;
        com.ekoapp.ekosdk.uikit.community.mycommunity.listener.IMyCommunityItemClickListener listener = mListener;

        if ((dirtyFlags & 0x5L) != 0) {



                if (ekoCommunity != null) {
                    // read ekoCommunity.displayName
                    ekoCommunityDisplayName = ekoCommunity.getDisplayName();
                    // read ekoCommunity.official
                    ekoCommunityOfficial = ekoCommunity.isOfficial();
                }
            if((dirtyFlags & 0x5L) != 0) {
                if(ekoCommunityOfficial) {
                        dirtyFlags |= 0x10L;
                }
                else {
                        dirtyFlags |= 0x8L;
                }
            }


                // read ekoCommunity.official ? View.VISIBLE : View.GONE
                ekoCommunityOfficialViewVISIBLEViewGONE = ((ekoCommunityOfficial) ? (android.view.View.VISIBLE) : (android.view.View.GONE));
        }
        // batch finished
        if ((dirtyFlags & 0x4L) != 0) {
            // api target 1

            com.ekoapp.ekosdk.uikit.components.BindingUtilityKt.setEkoViewBackgroundColor(this.ivAvatar, getColorFromResource(ivAvatar, R.color.amityColorPrimary), com.ekoapp.ekosdk.uikit.common.views.ColorShade.SHADE3);
            this.mboundView0.setOnClickListener(mCallback1);
        }
        if ((dirtyFlags & 0x5L) != 0) {
            // api target 1

            this.ivOfficial.setVisibility(ekoCommunityOfficialViewVISIBLEViewGONE);
            androidx.databinding.adapters.TextViewBindingAdapter.setText(this.tvName, ekoCommunityDisplayName);
        }
    }
    // Listener Stub Implementations
    // callback impls
    public final void _internalCallbackOnClick(int sourceId , android.view.View callbackArg_0) {
        // localize variables for thread safety
        // ekoCommunity
        com.ekoapp.ekosdk.community.EkoCommunity ekoCommunity = mEkoCommunity;
        // listener != null
        boolean listenerJavaLangObjectNull = false;
        // listener
        com.ekoapp.ekosdk.uikit.community.mycommunity.listener.IMyCommunityItemClickListener listener = mListener;



        listenerJavaLangObjectNull = (listener) != (null);
        if (listenerJavaLangObjectNull) {



            listener.onCommunitySelected(ekoCommunity);
        }
    }
    // dirty flag
    private  long mDirtyFlags = 0xffffffffffffffffL;
    /* flag mapping
        flag 0 (0x1L): ekoCommunity
        flag 1 (0x2L): listener
        flag 2 (0x3L): null
        flag 3 (0x4L): ekoCommunity.official ? View.VISIBLE : View.GONE
        flag 4 (0x5L): ekoCommunity.official ? View.VISIBLE : View.GONE
    flag mapping end*/
    //end
}