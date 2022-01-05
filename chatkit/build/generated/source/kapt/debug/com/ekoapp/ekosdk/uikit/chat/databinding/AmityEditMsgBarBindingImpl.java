package com.ekoapp.ekosdk.uikit.chat.databinding;
import com.ekoapp.ekosdk.uikit.chat.R;
import com.ekoapp.ekosdk.uikit.chat.BR;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class AmityEditMsgBarBindingImpl extends AmityEditMsgBarBinding implements com.ekoapp.ekosdk.uikit.chat.generated.callback.OnClickListener.Listener {

    @Nullable
    private static final androidx.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.icCross, 2);
    }
    // views
    @NonNull
    private final com.google.android.material.appbar.MaterialToolbar mboundView0;
    // variables
    @Nullable
    private final android.view.View.OnClickListener mCallback4;
    // values
    // listeners
    // Inverse Binding Event Handlers

    public AmityEditMsgBarBindingImpl(@Nullable androidx.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        this(bindingComponent, root, mapBindings(bindingComponent, root, 3, sIncludes, sViewsWithIds));
    }
    private AmityEditMsgBarBindingImpl(androidx.databinding.DataBindingComponent bindingComponent, View root, Object[] bindings) {
        super(bindingComponent, root, 2
            , (android.widget.ImageView) bindings[2]
            , (android.widget.TextView) bindings[1]
            );
        this.mboundView0 = (com.google.android.material.appbar.MaterialToolbar) bindings[0];
        this.mboundView0.setTag(null);
        this.tvSave.setTag(null);
        setRootTag(root);
        // listeners
        mCallback4 = new com.ekoapp.ekosdk.uikit.chat.generated.callback.OnClickListener(this, 1);
        invalidateAll();
    }

    @Override
    public void invalidateAll() {
        synchronized(this) {
                mDirtyFlags = 0x8L;
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
        if (BR.viewModel == variableId) {
            setViewModel((com.ekoapp.ekosdk.uikit.chat.editMessage.EkoEditMessageViewModel) variable);
        }
        else {
            variableSet = false;
        }
            return variableSet;
    }

    public void setViewModel(@Nullable com.ekoapp.ekosdk.uikit.chat.editMessage.EkoEditMessageViewModel ViewModel) {
        this.mViewModel = ViewModel;
        synchronized(this) {
            mDirtyFlags |= 0x4L;
        }
        notifyPropertyChanged(BR.viewModel);
        super.requestRebind();
    }

    @Override
    protected boolean onFieldChange(int localFieldId, Object object, int fieldId) {
        switch (localFieldId) {
            case 0 :
                return onChangeViewModelIsSaveEnabled((androidx.databinding.ObservableBoolean) object, fieldId);
            case 1 :
                return onChangeViewModelSaveColor((androidx.databinding.ObservableField<java.lang.Integer>) object, fieldId);
        }
        return false;
    }
    private boolean onChangeViewModelIsSaveEnabled(androidx.databinding.ObservableBoolean ViewModelIsSaveEnabled, int fieldId) {
        if (fieldId == BR._all) {
            synchronized(this) {
                    mDirtyFlags |= 0x1L;
            }
            return true;
        }
        return false;
    }
    private boolean onChangeViewModelSaveColor(androidx.databinding.ObservableField<java.lang.Integer> ViewModelSaveColor, int fieldId) {
        if (fieldId == BR._all) {
            synchronized(this) {
                    mDirtyFlags |= 0x2L;
            }
            return true;
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
        int androidxDatabindingViewDataBindingSafeUnboxViewModelIsSaveEnabledViewModelSaveColorColorPaletteUtilINSTANCEGetColorViewModelSaveColorColorShadeSHADE2 = 0;
        boolean viewModelIsSaveEnabledGet = false;
        int androidxDatabindingViewDataBindingSafeUnboxViewModelSaveColorGet = 0;
        int colorPaletteUtilINSTANCEGetColorViewModelSaveColorColorShadeSHADE2 = 0;
        androidx.databinding.ObservableBoolean viewModelIsSaveEnabled = null;
        androidx.databinding.ObservableField<java.lang.Integer> viewModelSaveColor = null;
        com.ekoapp.ekosdk.uikit.chat.editMessage.EkoEditMessageViewModel viewModel = mViewModel;
        java.lang.Integer viewModelIsSaveEnabledViewModelSaveColorColorPaletteUtilINSTANCEGetColorViewModelSaveColorColorShadeSHADE2 = null;
        java.lang.Integer viewModelSaveColorGet = null;

        if ((dirtyFlags & 0xfL) != 0) {



                if (viewModel != null) {
                    // read viewModel.isSaveEnabled()
                    viewModelIsSaveEnabled = viewModel.isSaveEnabled();
                }
                updateRegistration(0, viewModelIsSaveEnabled);


                if (viewModelIsSaveEnabled != null) {
                    // read viewModel.isSaveEnabled().get()
                    viewModelIsSaveEnabledGet = viewModelIsSaveEnabled.get();
                }
            if((dirtyFlags & 0xfL) != 0) {
                if(viewModelIsSaveEnabledGet) {
                        dirtyFlags |= 0x20L;
                }
                else {
                        dirtyFlags |= 0x10L;
                }
            }
        }
        // batch finished

        if ((dirtyFlags & 0x30L) != 0) {

                if (viewModel != null) {
                    // read viewModel.saveColor
                    viewModelSaveColor = viewModel.getSaveColor();
                }
                updateRegistration(1, viewModelSaveColor);


                if (viewModelSaveColor != null) {
                    // read viewModel.saveColor.get()
                    viewModelSaveColorGet = viewModelSaveColor.get();
                }

            if ((dirtyFlags & 0x10L) != 0) {

                    // read androidx.databinding.ViewDataBinding.safeUnbox(viewModel.saveColor.get())
                    androidxDatabindingViewDataBindingSafeUnboxViewModelSaveColorGet = androidx.databinding.ViewDataBinding.safeUnbox(viewModelSaveColorGet);


                    // read ColorPaletteUtil.INSTANCE.getColor(androidx.databinding.ViewDataBinding.safeUnbox(viewModel.saveColor.get()), ColorShade.SHADE2)
                    colorPaletteUtilINSTANCEGetColorViewModelSaveColorColorShadeSHADE2 = com.ekoapp.ekosdk.uikit.common.views.ColorPaletteUtil.INSTANCE.getColor(androidxDatabindingViewDataBindingSafeUnboxViewModelSaveColorGet, com.ekoapp.ekosdk.uikit.common.views.ColorShade.SHADE2);
            }
        }

        if ((dirtyFlags & 0xfL) != 0) {

                // read viewModel.isSaveEnabled().get() ? viewModel.saveColor.get() : ColorPaletteUtil.INSTANCE.getColor(androidx.databinding.ViewDataBinding.safeUnbox(viewModel.saveColor.get()), ColorShade.SHADE2)
                viewModelIsSaveEnabledViewModelSaveColorColorPaletteUtilINSTANCEGetColorViewModelSaveColorColorShadeSHADE2 = ((viewModelIsSaveEnabledGet) ? (viewModelSaveColorGet) : (colorPaletteUtilINSTANCEGetColorViewModelSaveColorColorShadeSHADE2));


                // read androidx.databinding.ViewDataBinding.safeUnbox(viewModel.isSaveEnabled().get() ? viewModel.saveColor.get() : ColorPaletteUtil.INSTANCE.getColor(androidx.databinding.ViewDataBinding.safeUnbox(viewModel.saveColor.get()), ColorShade.SHADE2))
                androidxDatabindingViewDataBindingSafeUnboxViewModelIsSaveEnabledViewModelSaveColorColorPaletteUtilINSTANCEGetColorViewModelSaveColorColorShadeSHADE2 = androidx.databinding.ViewDataBinding.safeUnbox(viewModelIsSaveEnabledViewModelSaveColorColorPaletteUtilINSTANCEGetColorViewModelSaveColorColorShadeSHADE2);
        }
        // batch finished
        if ((dirtyFlags & 0xdL) != 0) {
            // api target 1

            this.tvSave.setEnabled(viewModelIsSaveEnabledGet);
        }
        if ((dirtyFlags & 0x8L) != 0) {
            // api target 1

            this.tvSave.setOnClickListener(mCallback4);
        }
        if ((dirtyFlags & 0xfL) != 0) {
            // api target 1

            this.tvSave.setTextColor(androidxDatabindingViewDataBindingSafeUnboxViewModelIsSaveEnabledViewModelSaveColorColorPaletteUtilINSTANCEGetColorViewModelSaveColorColorShadeSHADE2);
        }
    }
    // Listener Stub Implementations
    // callback impls
    public final void _internalCallbackOnClick(int sourceId , android.view.View callbackArg_0) {
        // localize variables for thread safety
        // viewModel.saveMessage()
        io.reactivex.Completable viewModelSaveMessage = null;
        // viewModel
        com.ekoapp.ekosdk.uikit.chat.editMessage.EkoEditMessageViewModel viewModel = mViewModel;
        // viewModel != null
        boolean viewModelJavaLangObjectNull = false;



        viewModelJavaLangObjectNull = (viewModel) != (null);
        if (viewModelJavaLangObjectNull) {


            viewModelSaveMessage = viewModel.saveMessage();
        }
    }
    // dirty flag
    private  long mDirtyFlags = 0xffffffffffffffffL;
    /* flag mapping
        flag 0 (0x1L): viewModel.isSaveEnabled()
        flag 1 (0x2L): viewModel.saveColor
        flag 2 (0x3L): viewModel
        flag 3 (0x4L): null
        flag 4 (0x5L): viewModel.isSaveEnabled().get() ? viewModel.saveColor.get() : ColorPaletteUtil.INSTANCE.getColor(androidx.databinding.ViewDataBinding.safeUnbox(viewModel.saveColor.get()), ColorShade.SHADE2)
        flag 5 (0x6L): viewModel.isSaveEnabled().get() ? viewModel.saveColor.get() : ColorPaletteUtil.INSTANCE.getColor(androidx.databinding.ViewDataBinding.safeUnbox(viewModel.saveColor.get()), ColorShade.SHADE2)
    flag mapping end*/
    //end
}