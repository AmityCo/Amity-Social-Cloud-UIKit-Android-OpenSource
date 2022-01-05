package com.ekoapp.ekosdk.uikit.chat.databinding;
import com.ekoapp.ekosdk.uikit.chat.R;
import com.ekoapp.ekosdk.uikit.chat.BR;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class AmityViewMsgDeletedBindingImpl extends AmityViewMsgDeletedBinding  {

    @Nullable
    private static final androidx.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.ivRemove, 3);
    }
    // views
    @NonNull
    private final androidx.constraintlayout.widget.ConstraintLayout mboundView0;
    // variables
    // values
    // listeners
    // Inverse Binding Event Handlers

    public AmityViewMsgDeletedBindingImpl(@Nullable androidx.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        this(bindingComponent, root, mapBindings(bindingComponent, root, 4, sIncludes, sViewsWithIds));
    }
    private AmityViewMsgDeletedBindingImpl(androidx.databinding.DataBindingComponent bindingComponent, View root, Object[] bindings) {
        super(bindingComponent, root, 2
            , (android.widget.ImageView) bindings[3]
            , (android.widget.TextView) bindings[1]
            , (android.widget.TextView) bindings[2]
            );
        this.mboundView0 = (androidx.constraintlayout.widget.ConstraintLayout) bindings[0];
        this.mboundView0.setTag(null);
        this.tvDltMsg.setTag(null);
        this.tvDltTime.setTag(null);
        setRootTag(root);
        // listeners
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
            setViewModel((com.ekoapp.ekosdk.uikit.chat.messages.viewModel.EkoSelectableMessageViewModel) variable);
        }
        else {
            variableSet = false;
        }
            return variableSet;
    }

    public void setViewModel(@Nullable com.ekoapp.ekosdk.uikit.chat.messages.viewModel.EkoSelectableMessageViewModel ViewModel) {
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
                return onChangeViewModelEditedAt((androidx.databinding.ObservableField<java.lang.String>) object, fieldId);
            case 1 :
                return onChangeViewModelIsDeleted((androidx.databinding.ObservableBoolean) object, fieldId);
        }
        return false;
    }
    private boolean onChangeViewModelEditedAt(androidx.databinding.ObservableField<java.lang.String> ViewModelEditedAt, int fieldId) {
        if (fieldId == BR._all) {
            synchronized(this) {
                    mDirtyFlags |= 0x1L;
            }
            return true;
        }
        return false;
    }
    private boolean onChangeViewModelIsDeleted(androidx.databinding.ObservableBoolean ViewModelIsDeleted, int fieldId) {
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
        int viewModelIsDeletedViewVISIBLEViewGONE = 0;
        androidx.databinding.ObservableField<java.lang.String> viewModelEditedAt = null;
        java.lang.String viewModelEditedAtGet = null;
        com.ekoapp.ekosdk.uikit.chat.messages.viewModel.EkoSelectableMessageViewModel viewModel = mViewModel;
        boolean viewModelIsDeletedGet = false;
        androidx.databinding.ObservableBoolean viewModelIsDeleted = null;

        if ((dirtyFlags & 0xfL) != 0) {


            if ((dirtyFlags & 0xdL) != 0) {

                    if (viewModel != null) {
                        // read viewModel.editedAt
                        viewModelEditedAt = viewModel.getEditedAt();
                    }
                    updateRegistration(0, viewModelEditedAt);


                    if (viewModelEditedAt != null) {
                        // read viewModel.editedAt.get()
                        viewModelEditedAtGet = viewModelEditedAt.get();
                    }
            }
            if ((dirtyFlags & 0xeL) != 0) {

                    if (viewModel != null) {
                        // read viewModel.isDeleted()
                        viewModelIsDeleted = viewModel.isDeleted();
                    }
                    updateRegistration(1, viewModelIsDeleted);


                    if (viewModelIsDeleted != null) {
                        // read viewModel.isDeleted().get()
                        viewModelIsDeletedGet = viewModelIsDeleted.get();
                    }
                if((dirtyFlags & 0xeL) != 0) {
                    if(viewModelIsDeletedGet) {
                            dirtyFlags |= 0x20L;
                    }
                    else {
                            dirtyFlags |= 0x10L;
                    }
                }


                    // read viewModel.isDeleted().get() ? View.VISIBLE : View.GONE
                    viewModelIsDeletedViewVISIBLEViewGONE = ((viewModelIsDeletedGet) ? (android.view.View.VISIBLE) : (android.view.View.GONE));
            }
        }
        // batch finished
        if ((dirtyFlags & 0xeL) != 0) {
            // api target 1

            this.mboundView0.setVisibility(viewModelIsDeletedViewVISIBLEViewGONE);
        }
        if ((dirtyFlags & 0x8L) != 0) {
            // api target 1

            com.ekoapp.ekosdk.uikit.components.BindingUtilityKt.setEkoTextColor(this.tvDltMsg, com.ekoapp.ekosdk.uikit.common.views.ColorShade.SHADE2, (com.ekoapp.ekosdk.uikit.common.views.ColorShade)null);
            com.ekoapp.ekosdk.uikit.components.BindingUtilityKt.setEkoTextColor(this.tvDltTime, com.ekoapp.ekosdk.uikit.common.views.ColorShade.SHADE2, (com.ekoapp.ekosdk.uikit.common.views.ColorShade)null);
        }
        if ((dirtyFlags & 0xdL) != 0) {
            // api target 1

            androidx.databinding.adapters.TextViewBindingAdapter.setText(this.tvDltTime, viewModelEditedAtGet);
        }
    }
    // Listener Stub Implementations
    // callback impls
    // dirty flag
    private  long mDirtyFlags = 0xffffffffffffffffL;
    /* flag mapping
        flag 0 (0x1L): viewModel.editedAt
        flag 1 (0x2L): viewModel.isDeleted()
        flag 2 (0x3L): viewModel
        flag 3 (0x4L): null
        flag 4 (0x5L): viewModel.isDeleted().get() ? View.VISIBLE : View.GONE
        flag 5 (0x6L): viewModel.isDeleted().get() ? View.VISIBLE : View.GONE
    flag mapping end*/
    //end
}