package com.amity.socialcloud.uikit.community.compose.community.setup

import android.app.Activity
import android.content.Context
import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rxjava3.subscribeAsState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.compose.collectAsLazyPagingItems
import coil3.compose.AsyncImage
import com.amity.socialcloud.sdk.helper.core.coroutines.asFlow
import com.amity.socialcloud.sdk.model.chat.settings.AmityMembershipAcceptanceType
import com.amity.socialcloud.sdk.model.core.file.AmityImage
import com.amity.socialcloud.sdk.model.core.user.AmityUser
import com.amity.socialcloud.sdk.model.social.community.AmityJoinRequestStatus
import com.amity.socialcloud.uikit.common.common.toDp
import com.amity.socialcloud.uikit.common.common.views.AmityColorShade
import com.amity.socialcloud.uikit.common.config.AmityUIKitConfigController
import com.amity.socialcloud.uikit.common.eventbus.AmityUIKitSnackbar
import com.amity.socialcloud.uikit.common.ui.base.AmityBaseElement
import com.amity.socialcloud.uikit.common.ui.base.AmityBasePage
import com.amity.socialcloud.uikit.common.ui.elements.AmityAlertDialog
import com.amity.socialcloud.uikit.common.ui.elements.AmityTextField
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposePageScope
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.AmityCameraUtil
import com.amity.socialcloud.uikit.common.utils.amityStringResource
import com.amity.socialcloud.uikit.common.utils.clickableWithoutRipple
import com.amity.socialcloud.uikit.common.utils.closePage
import com.amity.socialcloud.uikit.common.utils.closePageWithResult
import com.amity.socialcloud.uikit.common.utils.getIcon
import com.amity.socialcloud.uikit.common.utils.getText
import com.amity.socialcloud.uikit.common.utils.shade
import com.amity.socialcloud.uikit.community.compose.AmitySocialBehaviorHelper
import com.amity.socialcloud.uikit.community.compose.R
import com.amity.socialcloud.uikit.community.compose.community.category.AmityCommunityAddCategoryPageActivity
import com.amity.socialcloud.uikit.community.compose.community.category.element.AmityCommunityCategoryList
import com.amity.socialcloud.uikit.community.compose.community.membership.add.AmityCommunityAddMemberPageActivity
import com.amity.socialcloud.uikit.community.compose.community.membership.element.AmityCommunityAddMemberList
import com.amity.socialcloud.uikit.community.compose.community.membership.element.AmityCommunityInviteMemberList
import com.amity.socialcloud.uikit.community.compose.community.membership.invite.AmityCommunityInviteMemberPageActivity
import com.amity.socialcloud.uikit.community.compose.community.setup.elements.AmityMediaImageSelectionSheet
import com.amity.socialcloud.uikit.community.compose.community.setup.elements.AmityMediaImageSelectionType

@Composable
fun AmityCommunitySetupPage(
    modifier: Modifier = Modifier,
    mode: AmityCommunitySetupPageMode,
) {
    val context = LocalContext.current
    val behavior by lazy {
        AmitySocialBehaviorHelper.communitySetupPageBehavior
    }

    val isInEditMode by remember(mode) {
        mutableStateOf(mode is AmityCommunitySetupPageMode.Edit)
    }

    val communityToEdit by remember(isInEditMode) {
        derivedStateOf {
            if (isInEditMode) {
                (mode as AmityCommunitySetupPageMode.Edit).community
            } else {
                null
            }
        }
    }

    var name by remember(isInEditMode) {
        mutableStateOf(if (isInEditMode) communityToEdit?.getDisplayName() ?: "" else "")
    }
    var description by remember(isInEditMode) {
        mutableStateOf(if (isInEditMode) communityToEdit?.getDescription() ?: "" else "")
    }

    var privacyMode by remember(isInEditMode) {
        mutableStateOf(
            if (isInEditMode) {
                val isPublic = communityToEdit?.isPublic() == true
                val isPrivateVisible =
                    communityToEdit?.isPublic() == false && communityToEdit?.isDiscoverable() == true
                val isPrivateHidden =
                    communityToEdit?.isPublic() == false && communityToEdit?.isDiscoverable() == false

                when {
                    isPublic -> AmityCommunitySetupPrivacyMode.PUBLIC
                    isPrivateVisible -> AmityCommunitySetupPrivacyMode.PRIVATE_VISIBLE
                    isPrivateHidden -> AmityCommunitySetupPrivacyMode.PRIVATE_HIDDEN
                    else -> AmityCommunitySetupPrivacyMode.PUBLIC
                }
            } else {
                AmityCommunitySetupPrivacyMode.PUBLIC
            }
        )
    }

    val isPublic by remember(privacyMode) {
        derivedStateOf { privacyMode == AmityCommunitySetupPrivacyMode.PUBLIC }
    }

    var isMembershipEnabled by remember(isInEditMode, privacyMode, communityToEdit) {
        mutableStateOf(
            if (isInEditMode) {
                communityToEdit?.requiresJoinApproval() == true
            } else {
                privacyMode != AmityCommunitySetupPrivacyMode.PUBLIC
            }
        )
    }

    var isDiscoverable by remember(privacyMode) {
        mutableStateOf(
            when (privacyMode) {
                AmityCommunitySetupPrivacyMode.PUBLIC,
                AmityCommunitySetupPrivacyMode.PRIVATE_VISIBLE,
                    -> true

                AmityCommunitySetupPrivacyMode.PRIVATE_HIDDEN -> false
            }
        )
    }

    LaunchedEffect(privacyMode) {
        isDiscoverable = when (privacyMode) {
            AmityCommunitySetupPrivacyMode.PUBLIC,
            AmityCommunitySetupPrivacyMode.PRIVATE_VISIBLE,
                -> true

            AmityCommunitySetupPrivacyMode.PRIVATE_HIDDEN -> false
        }
    }

    val selectedCategories = remember(isInEditMode) {
        val categories = if (isInEditMode) {
            communityToEdit?.getCategories() ?: emptyList()
        } else {
            emptyList()
        }
        mutableStateListOf(*categories.toTypedArray())
    }

    val selectedUsers = remember {
        mutableStateListOf<AmityUser>()
    }

    val addCategoriesLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.let {
                val categories = it.let(AmityCommunityAddCategoryPageActivity::getCategories)

                selectedCategories.clear()
                selectedCategories.addAll(categories)
            }
        }
    }

    val addMembersLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.let {
                val users = it.let(AmityCommunityAddMemberPageActivity::getUsers)

                selectedUsers.clear()
                selectedUsers.addAll(users)
            }
        }
    }

    val inviteMembersLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.let {
                val users = it.let(AmityCommunityInviteMemberPageActivity::getUsers)
                selectedUsers.clear()
                selectedUsers.addAll(users)
            }
        }
    }

    val viewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current) {
        "No ViewModelStoreOwner was provided via LocalViewModelStoreOwner"
    }
    val viewModel =
        viewModel<AmityCommunitySetupPageViewModel>(viewModelStoreOwner = viewModelStoreOwner)

    var showLeaveConfirmDialog by remember { mutableStateOf(false) }
    var showPrivacyConfirmDialog by remember { mutableStateOf(false) }
    var showMediaCameraSelectionSheet by remember { mutableStateOf(false) }
    var isCameraPermissionGranted by remember { mutableStateOf(false) }

    var isCapturedImageReady by remember { mutableStateOf(false) }
    var avatarUri by remember { mutableStateOf(Uri.EMPTY) }

    val joinRequestFlow = remember(communityToEdit) {
        if (isInEditMode) {
            communityToEdit?.getJoinRequests(AmityJoinRequestStatus.PENDING)?.asFlow()
        } else {
            null
        }
    }
    val joinRequest = joinRequestFlow?.collectAsLazyPagingItems()

    val hasJoinRequest by remember(joinRequest) {
        derivedStateOf { (joinRequest?.itemCount ?: 0) > 0 }
    }

    var showJoinRequestNotEmptyDialog by remember { mutableStateOf(false) }

    val scrollState = rememberScrollState()
    val hasScrolled by remember {
        derivedStateOf { scrollState.value > 0 }
    }

    val imagePickerLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            avatarUri = uri
            isCapturedImageReady = true
        }

    val imageCaptureLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
            isCapturedImageReady = isSuccess
        }

    val cameraPermissionLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            isCapturedImageReady = false
            isCameraPermissionGranted = permissions.entries.all { it.value }
            if (!isCameraPermissionGranted) {
                AmityUIKitSnackbar.publishSnackbarErrorMessage("Camera permission not granted")
            } else {
                val imageFile = AmityCameraUtil.createImageFile(context)
                if (imageFile == null) {
                    AmityUIKitSnackbar.publishSnackbarErrorMessage("Failed to create image file")

                } else {
                    avatarUri = AmityCameraUtil.createPhotoUri(context, imageFile)
                    imageCaptureLauncher.launch(avatarUri)
                }
            }
        }

    val shouldActionButtonEnable by remember(
        communityToEdit,
        isInEditMode,
        privacyMode,
        isMembershipEnabled
    ) {
        derivedStateOf {
            if (isInEditMode) {
                val sc = selectedCategories.sortedWith(compareBy {
                    it.getCategoryId()
                }).map { it.getCategoryId() }

                val cc = communityToEdit?.getCategories()?.sortedWith(compareBy {
                    it.getCategoryId()
                })?.map { it.getCategoryId() } ?: emptyList()

                val isPublic = communityToEdit?.isPublic() == true
                val isPrivateVisible =
                    communityToEdit?.isPublic() == false && communityToEdit?.isDiscoverable() == true
                val isPrivateHidden =
                    communityToEdit?.isPublic() == false && communityToEdit?.isDiscoverable() == false

                val hasPrivacyModeChanged = privacyMode != when {
                    isPublic -> AmityCommunitySetupPrivacyMode.PUBLIC
                    isPrivateVisible -> AmityCommunitySetupPrivacyMode.PRIVATE_VISIBLE
                    isPrivateHidden -> AmityCommunitySetupPrivacyMode.PRIVATE_HIDDEN
                    else -> AmityCommunitySetupPrivacyMode.PUBLIC
                }

                val hasMemberShipChanged =
                    isMembershipEnabled != communityToEdit?.requiresJoinApproval()

                name != communityToEdit?.getDisplayName() ||
                        description != communityToEdit?.getDescription() ||
                        hasPrivacyModeChanged ||
                        hasMemberShipChanged ||
                        sc != cc || avatarUri != Uri.EMPTY
            } else {
                name.isNotBlank()
            }
        }
    }

    val membershipAcceptance = viewModel.getMembershipAcceptanceType()
        .subscribeAsState(initial = AmityMembershipAcceptanceType.AUTOMATIC)

    LaunchedEffect(Unit) {
        if (mode is AmityCommunitySetupPageMode.Edit) {
            viewModel.observeGlobalFeaturedPost(communityToEdit!!.getCommunityId())
        }
    }

    BackHandler {
        showLeaveConfirmDialog = true
    }

    var stickyHeaderHeight by remember { mutableStateOf(0.dp) }
    val localDensity = LocalDensity.current

    AmityBasePage(pageId = "community_setup_page") {
        Box(
            modifier = modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.safeDrawing)
        ) {
            // Fixed toolbar at the top
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopCenter)
                    .background(AmityTheme.colors.background)
                    .onGloballyPositioned { coordinates ->
                        // Convert pixels to dp
                        stickyHeaderHeight = with(localDensity) {
                            coordinates.size.height.toDp()
                        }
                    }
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp)
                ) {
                    AmityBaseElement(
                        pageScope = getPageScope(),
                        elementId = "close_button"
                    ) {
                        Icon(
                            painter = painterResource(id = getConfig().getIcon()),
                            contentDescription = "Close Button",
                            tint = AmityTheme.colors.base,
                            modifier = modifier
                                .size(16.dp)
                                .clickableWithoutRipple {
                                    showLeaveConfirmDialog = true
                                }
                                .testTag(getAccessibilityId())
                        )
                    }
                    AmityBaseElement(
                        pageScope = getPageScope(),
                        elementId = if (isInEditMode) "community_edit_title" else "title"
                    ) {
                        Text(
                            text = amityStringResource(
                                configString = getConfig().getText(),
                                id = if (isInEditMode) R.string.amity_v4_community_setup_edit_title
                                else R.string.amity_v4_community_setup_create_title,
                            ),
                            style = AmityTheme.typography.titleLegacy,
                            modifier = modifier
                                .fillMaxWidth()
                                .padding(vertical = 16.dp)
                                .testTag(getAccessibilityId())
                        )
                    }
                }
                if (hasScrolled) {
                    HorizontalDivider(
                        color = AmityTheme.colors.baseShade4,
                    )
                }
            }

            // Content area with scroll
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = stickyHeaderHeight)
                    .verticalScroll(scrollState)
                    .background(AmityTheme.colors.background)
            ) {
                Box(
                    modifier = modifier
                        .aspectRatio(2f)
                        .background(AmityTheme.colors.baseShade3)
                        .background(Color.Black.copy(alpha = 0.5f))
                        .clickableWithoutRipple {
                            showMediaCameraSelectionSheet = true
                        }
                ) {
                    val avatar = when {
                        avatarUri != Uri.EMPTY && isCapturedImageReady -> avatarUri
                        isInEditMode -> communityToEdit?.getAvatar()?.getUrl(AmityImage.Size.MEDIUM)
                        else -> null
                    }

                    AsyncImage(
                        model = avatar,
                        contentDescription = "Avatar",
                        contentScale = ContentScale.Crop,
                        modifier = modifier.fillMaxWidth(),
                    )
                    Box(
                        modifier = modifier
                            .fillMaxSize()
                            .background(Color.Black.copy(alpha = 0.4f))
                    )
                    Icon(
                        painter = painterResource(R.drawable.amity_ic_camera2),
                        contentDescription = "Upload avatar",
                        tint = Color.White,
                        modifier = modifier
                            .size(28.dp)
                            .align(Alignment.Center)
                    )
                }

                Spacer(modifier = modifier.height(24.dp))
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    AmityBaseElement(
                        pageScope = getPageScope(),
                        elementId = "community_name_title"
                    ) {
                        Text(
                            text = amityStringResource(
                                configString = getConfig().getText(),
                                id = R.string.amity_v4_community_setup_name_title
                            ),
                            style = AmityTheme.typography.titleLegacy,
                            modifier = modifier.testTag(getAccessibilityId())
                        )
                    }

                    Text(
                        text = "${name.length}/30",
                        style = AmityTheme.typography.captionLegacy.copy(
                            fontWeight = FontWeight.Normal,
                            color = AmityTheme.colors.baseShade1,
                        )
                    )
                }

                AmityTextField(
                    maxCharacters = 30,
                    text = name,
                    hint = amityStringResource(id = R.string.amity_v4_community_setup_name_description),
                    onValueChange = { name = it },
                    innerPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp),
                )

                HorizontalDivider(
                    color = AmityTheme.colors.divider,
                    modifier = modifier.padding(horizontal = 16.dp)
                )

                Spacer(modifier = modifier.height(24.dp))
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        AmityBaseElement(
                            pageScope = getPageScope(),
                            elementId = "community_about_title"
                        ) {
                            Text(
                                text = amityStringResource(
                                    configString = getConfig().getText(),
                                    id = R.string.amity_v4_community_setup_about_title,
                                ),
                                style = AmityTheme.typography.titleLegacy,
                                modifier = modifier.testTag(getAccessibilityId())
                            )
                        }
                        Text(
                            text = " " + amityStringResource(
                                id = R.string.amity_v4_community_setup_about_optional_title,
                            ),
                            style = AmityTheme.typography.bodyLegacy.copy(
                                color = AmityTheme.colors.baseShade3,
                            )
                        )
                    }

                    Text(
                        text = "${description.length}/180",
                        style = AmityTheme.typography.captionLegacy.copy(
                            fontWeight = FontWeight.Normal,
                            color = AmityTheme.colors.baseShade1,
                        )
                    )
                }

                AmityTextField(
                    maxCharacters = 180,
                    text = description,
                    hint = amityStringResource(id = R.string.amity_v4_community_setup_about_description),
                    onValueChange = { description = it },
                    innerPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp),
                )

                HorizontalDivider(
                    color = AmityTheme.colors.divider,
                    modifier = modifier.padding(horizontal = 16.dp)
                )

                Spacer(modifier = modifier.height(24.dp))
                AmityBaseElement(
                    pageScope = getPageScope(),
                    elementId = "community_category_title"
                ) {
                    Text(
                        text = amityStringResource(
                            configString = getConfig().getText(),
                            id = R.string.amity_v4_community_setup_categories_title,
                        ),
                        style = AmityTheme.typography.titleLegacy,
                        modifier = modifier
                            .padding(horizontal = 16.dp)
                            .testTag(getAccessibilityId())
                    )
                }
                Spacer(modifier = modifier.height(18.dp))
                Box(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    if (selectedCategories.isEmpty()) {
                        Text(
                            text = context.getString(R.string.amity_v4_community_setup_categories_description),
                            style = AmityTheme.typography.bodyLegacy.copy(
                                color = AmityTheme.colors.baseShade3,
                            ),
                            modifier = modifier.clickableWithoutRipple {
                                behavior.goToAddCategoryPage(
                                    AmityCommunitySetupPageBehavior.Context(
                                        pageContext = context,
                                        launcher = addCategoriesLauncher,
                                        categories = selectedCategories,
                                    )
                                )
                            }
                        )
                    } else {
                        AmityCommunityCategoryList(
                            modifier = modifier.padding(end = 24.dp),
                            categories = selectedCategories,
                            onRemove = {
                                selectedCategories.remove(it)
                            }
                        )
                    }
                    Icon(
                        painter = painterResource(R.drawable.amity_ic_chevron_right),
                        contentDescription = "Select category",
                        tint = AmityTheme.colors.baseShade2,
                        modifier = modifier
                            .size(16.dp)
                            .align(Alignment.TopEnd)
                            .offset(
                                y = if (selectedCategories.isNotEmpty()) 8.dp else 0.dp
                            )
                            .clickableWithoutRipple {
                                behavior.goToAddCategoryPage(
                                    AmityCommunitySetupPageBehavior.Context(
                                        pageContext = context,
                                        launcher = addCategoriesLauncher,
                                        categories = selectedCategories,
                                    )
                                )
                            }
                    )
                }
                Spacer(modifier = modifier.height(16.dp))
                HorizontalDivider(
                    color = AmityTheme.colors.divider,
                    modifier = modifier.padding(horizontal = 16.dp)
                )

                Spacer(modifier = modifier.height(24.dp))
                AmityBaseElement(
                    pageScope = getPageScope(),
                    elementId = "community_privacy_title"
                ) {
                    Text(
                        text = amityStringResource(
                            configString = getConfig().getText(),
                            id = R.string.amity_v4_community_setup_privacy_title,
                        ),
                        style = AmityTheme.typography.titleLegacy,
                        modifier = modifier
                            .padding(horizontal = 16.dp)
                            .testTag(getAccessibilityId())
                    )
                }

                //Privacy Options
                //Privacy Public
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Box(
                        modifier = modifier
                            .clip(CircleShape)
                            .background(AmityTheme.colors.baseShade4)
                            .size(40.dp)
                    ) {
                        AmityBaseElement(
                            pageScope = getPageScope(),
                            elementId = "community_privacy_public_icon"
                        ) {
                            Icon(
                                painter = painterResource(getConfig().getIcon()),
                                contentDescription = "Public",
                                modifier = modifier
                                    .size(16.dp)
                                    .align(Alignment.Center)
                                    .testTag(getAccessibilityId())
                            )
                        }
                    }

                    Spacer(modifier.width(12.dp))
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .clickableWithoutRipple {
                                privacyMode = AmityCommunitySetupPrivacyMode.PUBLIC
                            }
                    ) {
                        AmityBaseElement(
                            pageScope = getPageScope(),
                            elementId = "community_privacy_public_title"
                        ) {
                            Text(
                                text = amityStringResource(
                                    configString = getConfig().getText(),
                                    id = R.string.amity_v4_community_setup_privacy_public_title,
                                ),
                                style = AmityTheme.typography.bodyLegacy.copy(
                                    fontWeight = FontWeight.SemiBold
                                ),
                                modifier = modifier.testTag(getAccessibilityId())
                            )
                        }
                        Spacer(modifier.height(2.dp))
                        AmityBaseElement(
                            pageScope = getPageScope(),
                            elementId = "community_privacy_public_description"
                        ) {
                            Text(
                                text = amityStringResource(
                                    configString = getConfig().getText(),
                                    id = R.string.amity_v4_community_setup_privacy_public_description,
                                ),
                                style = AmityTheme.typography.captionLegacy.copy(
                                    color = AmityTheme.colors.baseShade1,
                                    fontWeight = FontWeight.Normal,
                                ),
                                modifier = modifier.testTag(getAccessibilityId())
                            )
                        }
                    }
                    RadioButton(
                        selected = privacyMode == AmityCommunitySetupPrivacyMode.PUBLIC,
                        colors = RadioButtonDefaults.colors(
                            selectedColor = AmityTheme.colors.highlight,
                            unselectedColor = AmityTheme.colors.baseShade2,
                        ),
                        onClick = {
                            privacyMode = AmityCommunitySetupPrivacyMode.PUBLIC
                        }
                    )
                }
                //Privacy Private and visible
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Box(
                        modifier = modifier
                            .clip(CircleShape)
                            .background(AmityTheme.colors.baseShade4)
                            .size(40.dp)
                    ) {
                        AmityBaseElement(
                            pageScope = getPageScope(),
                            elementId = "community_privacy_private_and_visible_icon"
                        ) {
                            Icon(
                                painter = painterResource(getConfig().getIcon()),
                                contentDescription = "Private and visible",
                                modifier = modifier
                                    .size(16.dp)
                                    .align(Alignment.Center)
                                    .testTag(getAccessibilityId())
                            )
                        }
                    }

                    Spacer(modifier.width(12.dp))
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .clickableWithoutRipple {
                                privacyMode = AmityCommunitySetupPrivacyMode.PRIVATE_VISIBLE
                            }
                    ) {
                        AmityBaseElement(
                            pageScope = getPageScope(),
                            elementId = "community_privacy_private_and_visible_title"
                        ) {
                            Text(
                                text = amityStringResource(
                                    configString = getConfig().getText(),
                                    id = R.string.amity_v4_community_setup_privacy_private_and_visible_title,
                                ),
                                style = AmityTheme.typography.bodyLegacy.copy(
                                    fontWeight = FontWeight.SemiBold
                                ),
                                modifier = modifier.testTag(getAccessibilityId())
                            )
                        }
                        Spacer(modifier.height(2.dp))
                        AmityBaseElement(
                            pageScope = getPageScope(),
                            elementId = "community_privacy_private_and_visible_description"
                        ) {
                            Text(
                                text = amityStringResource(
                                    configString = getConfig().getText(),
                                    id = R.string.amity_v4_community_setup_privacy_private_and_visible_description,
                                ),
                                style = AmityTheme.typography.captionLegacy.copy(
                                    color = AmityTheme.colors.baseShade1,
                                    fontWeight = FontWeight.Normal,
                                ),
                                modifier = modifier.testTag(getAccessibilityId())
                            )
                        }
                    }
                    RadioButton(
                        selected = privacyMode == AmityCommunitySetupPrivacyMode.PRIVATE_VISIBLE,
                        colors = RadioButtonDefaults.colors(
                            selectedColor = AmityTheme.colors.highlight,
                            unselectedColor = AmityTheme.colors.baseShade2,
                        ),
                        onClick = {
                            privacyMode = AmityCommunitySetupPrivacyMode.PRIVATE_VISIBLE
                        }
                    )
                }
                //Privacy Private and hidden
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Box(
                        modifier = modifier
                            .clip(CircleShape)
                            .background(AmityTheme.colors.baseShade4)
                            .size(40.dp)
                    ) {
                        AmityBaseElement(
                            pageScope = getPageScope(),
                            elementId = "community_privacy_private_and_hidden_icon"
                        ) {
                            Icon(
                                painter = painterResource(getConfig().getIcon()),
                                contentDescription = "Private and hidden",
                                modifier = modifier
                                    .size(16.dp)
                                    .align(Alignment.Center)
                                    .testTag(getAccessibilityId())
                            )
                        }
                    }

                    Spacer(modifier.width(12.dp))
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .clickableWithoutRipple {
                                privacyMode = AmityCommunitySetupPrivacyMode.PRIVATE_HIDDEN
                            }
                    ) {
                        AmityBaseElement(
                            pageScope = getPageScope(),
                            elementId = "community_privacy_private_and_hidden_title"
                        ) {
                            Text(
                                text = amityStringResource(
                                    configString = getConfig().getText(),
                                    id = R.string.amity_v4_community_setup_privacy_private_and_hidden_title,
                                ),
                                style = AmityTheme.typography.bodyLegacy.copy(
                                    fontWeight = FontWeight.SemiBold
                                ),
                                modifier = modifier.testTag(getAccessibilityId())
                            )
                        }
                        Spacer(modifier.height(2.dp))
                        AmityBaseElement(
                            pageScope = getPageScope(),
                            elementId = "community_privacy_private_and_hidden_description"
                        ) {
                            Text(
                                text = amityStringResource(
                                    configString = getConfig().getText(),
                                    id = R.string.amity_v4_community_setup_privacy_private_and_hidden_description,
                                ),
                                style = AmityTheme.typography.captionLegacy.copy(
                                    color = AmityTheme.colors.baseShade1,
                                    fontWeight = FontWeight.Normal,
                                ),
                                modifier = modifier.testTag(getAccessibilityId())
                            )
                        }
                    }
                    RadioButton(
                        selected = privacyMode == AmityCommunitySetupPrivacyMode.PRIVATE_HIDDEN,
                        colors = RadioButtonDefaults.colors(
                            selectedColor = AmityTheme.colors.highlight,
                            unselectedColor = AmityTheme.colors.baseShade2,
                        ),
                        onClick = {
                            privacyMode = AmityCommunitySetupPrivacyMode.PRIVATE_HIDDEN
                        }
                    )
                }

                HorizontalDivider(
                    color = AmityTheme.colors.divider,
                    modifier = modifier.padding(horizontal = 16.dp)
                )

                Spacer(modifier = modifier.height(24.dp))

                //MemberShip
                AmityBaseElement(
                    pageScope = getPageScope(),
                    elementId = "community_membership_title"
                ) {
                    Text(
                        text = amityStringResource(
                            configString = getConfig().getText(),
                            id = R.string.amity_v4_community_setup_privacy_title,
                        ),
                        style = AmityTheme.typography.titleLegacy,
                        modifier = modifier
                            .padding(horizontal = 16.dp)
                            .testTag(getAccessibilityId())
                    )
                }

                //MemberShip Options
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .clickableWithoutRipple {
                                privacyMode = AmityCommunitySetupPrivacyMode.PRIVATE_VISIBLE
                            }
                    ) {
                        AmityBaseElement(
                            pageScope = getPageScope(),
                            elementId = "community_membership_description"
                        ) {
                            Text(
                                text = amityStringResource(
                                    configString = getConfig().getText(),
                                    id = R.string.amity_v4_community_setup_membership_desc,
                                ),
                                style = AmityTheme.typography.bodyBold,
                                modifier = modifier.testTag(getAccessibilityId())
                            )
                        }
                        Spacer(modifier.height(2.dp))
                        AmityBaseElement(
                            pageScope = getPageScope(),
                            elementId = "community_membership_sub_description"
                        ) {
                            Text(
                                text = amityStringResource(
                                    configString = getConfig().getText(),
                                    id = R.string.amity_v4_community_setup_membership_sub_desc,
                                ),
                                style = AmityTheme.typography.caption,
                                color = AmityTheme.colors.baseShade1,
                                modifier = modifier.testTag(getAccessibilityId())
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Switch(
                        checked = isMembershipEnabled,
                        onCheckedChange = {
                            isMembershipEnabled = it
                        },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            uncheckedThumbColor = Color.White,
                            uncheckedBorderColor = AmityTheme.colors.baseShade3,
                            checkedTrackColor = AmityTheme.colors.primary,
                            uncheckedTrackColor = AmityTheme.colors.baseShade3,
                            disabledCheckedThumbColor = Color.White,
                            disabledCheckedTrackColor = AmityTheme.colors.primaryShade3,
                        ),
                        modifier = Modifier
                    )
                }

                if (!isPublic && !isInEditMode) {
                    Spacer(modifier = modifier.height(16.dp))
                    HorizontalDivider(
                        color = AmityTheme.colors.divider,
                        modifier = modifier.padding(horizontal = 16.dp)
                    )
                    if (membershipAcceptance.value == AmityMembershipAcceptanceType.INVITATION) {
                        Spacer(modifier = modifier.height(24.dp))
                        AmityBaseElement(
                            pageScope = getPageScope(),
                            elementId = "community_invite_member_title"
                        ) {
                            Text(
                                text = amityStringResource(
                                    configString = getConfig().getText(),
                                    id = R.string.amity_v4_community_setup_invite_members_title,
                                ),
                                style = AmityTheme.typography.titleLegacy,
                                modifier = modifier
                                    .padding(horizontal = 16.dp)
                                    .testTag(getAccessibilityId())
                            )
                        }
                        AmityBaseElement(
                            pageScope = getPageScope(),
                            elementId = "community_invite_member_description"
                        ) {
                            Text(
                                text = amityStringResource(
                                    configString = getConfig().getText(),
                                    id = R.string.amity_v4_community_setup_invite_members_description,
                                ),
                                style = AmityTheme.typography.caption,
                                modifier = modifier
                                    .padding(horizontal = 16.dp)
                                    .testTag(getAccessibilityId())
                            )
                        }
                        Spacer(modifier = modifier.height(16.dp))
                        AmityCommunityInviteMemberList(
                            pageScope = getPageScope(),
                            users = selectedUsers,
                            modifier = modifier.padding(horizontal = 16.dp),
                            onAddAction = {
                                behavior.goToPendingInvitationsPage(
                                    AmityCommunitySetupPageBehavior.Context(
                                        pageContext = context,
                                        launcher = inviteMembersLauncher,
                                        users = selectedUsers,
                                    )
                                )
                            },
                            onRemoveAction = {
                                selectedUsers.find {
                                    it.getUserId() == it.getUserId()
                                }.let(selectedUsers::remove)
                            }
                        )
                        Spacer(modifier = modifier.height(16.dp))
                    } else {
                        Spacer(modifier = modifier.height(24.dp))
                        AmityBaseElement(
                            pageScope = getPageScope(),
                            elementId = "community_add_member_title"
                        ) {
                            Text(
                                text = amityStringResource(
                                    configString = getConfig().getText(),
                                    id = R.string.amity_v4_community_setup_create_button,
                                ),
                                style = AmityTheme.typography.titleLegacy,
                                modifier = modifier
                                    .padding(horizontal = 16.dp)
                                    .testTag(getAccessibilityId())
                            )
                        }
                        Spacer(modifier = modifier.height(16.dp))
                        AmityCommunityAddMemberList(
                            pageScope = getPageScope(),
                            users = selectedUsers,
                            modifier = modifier.padding(horizontal = 16.dp),
                            onAddAction = {
                                behavior.goToAddMemberPage(
                                    AmityCommunitySetupPageBehavior.Context(
                                        pageContext = context,
                                        launcher = addMembersLauncher,
                                        users = selectedUsers,
                                    )
                                )
                            },
                            onRemoveAction = {
                                selectedUsers.find {
                                    it.getUserId() == it.getUserId()
                                }.let(selectedUsers::remove)
                            }
                        )
                        Spacer(modifier = modifier.height(16.dp))
                    }
                }
                Spacer(modifier.height(96.dp))
            }

            Column(
                modifier = modifier
                    .align(Alignment.BottomCenter)
                    .background(AmityTheme.colors.background)
            ) {
                HorizontalDivider(
                    color = AmityTheme.colors.divider,
                    modifier = modifier
                )

                Spacer(modifier = modifier.height(16.dp))
                Button(
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AmityTheme.colors.highlight,
                        disabledContainerColor = AmityTheme.colors.highlight.shade(AmityColorShade.SHADE2),
                    ),
                    shape = RoundedCornerShape(4.dp),
                    contentPadding = ButtonDefaults.ButtonWithIconContentPadding,
                    enabled = shouldActionButtonEnable,
                    modifier = Modifier
                        .height(40.dp)
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    onClick = {
                        if (isInEditMode) {
                            val initial = communityToEdit?.requiresJoinApproval() == true
                            val changeNotToRequireApproval = initial && !isMembershipEnabled
                            if (changeNotToRequireApproval && hasJoinRequest) {
                                showJoinRequestNotEmptyDialog = true
                                return@Button
                            }
                            val wasPublic = communityToEdit?.isPublic() == true
                            if (!isPublic && wasPublic && viewModel.hasGlobalFeaturedPost) {
                                showPrivacyConfirmDialog = true
                            } else {
                                updateCommunity(
                                    communityId = communityToEdit?.getCommunityId() ?: "",
                                    avatarUri = avatarUri,
                                    displayName = name,
                                    description = description,
                                    isPublic = isPublic,
                                    isDiscoverable = isDiscoverable,
                                    requiresJoinApproval = isMembershipEnabled,
                                    categoryIds = selectedCategories.map { it.getCategoryId() },
                                    viewModel = viewModel,
                                    pageScope = getPageScope(),
                                    context = context,
                                )
                            }
                        } else {
                            viewModel.createCommunity(
                                avatarUri = avatarUri,
                                displayName = name,
                                description = description,
                                isPublic = isPublic,
                                isDiscoverable = isDiscoverable,
                                requiresJoinApproval = isMembershipEnabled,
                                categoryIds = selectedCategories.map { it.getCategoryId() },
                                userIds = selectedUsers.map { it.getUserId() },
                                membershipAcceptanceType = membershipAcceptance.value,
                                onSuccess = {
                                    context.closePageWithResult(Activity.RESULT_OK)
                                    behavior.goToCommunityProfilePage(
                                        AmityCommunitySetupPageBehavior.Context(
                                            pageContext = context,
                                            communityId = it.getCommunityId(),
                                            isCommunityJustCreated = true,
                                        )
                                    )
                                },
                                onError = {
                                    AmityUIKitSnackbar.publishSnackbarMessage(context.getString(R.string.amity_v4_community_setup_toast_create_failed))
                                }
                            )
                        }
                    }
                ) {
                    AmityBaseElement(
                        pageScope = getPageScope(),
                        elementId = if (isInEditMode) "community_edit_button" else "community_create_button"
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.SpaceAround,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            if (!isInEditMode) {
                                Icon(
                                    painter = painterResource(getConfig().getIcon()),
                                    contentDescription = "Create",
                                    tint = Color.White,
                                    modifier = modifier.size(16.dp)
                                )
                                Spacer(modifier = modifier.width(8.dp))
                            }
                            Text(
                                text = context.amityStringResource(
                                    configString = getConfig().getText(),
                                    id = if (isInEditMode) R.string.amity_v4_community_setup_edit_button
                                    else R.string.amity_v4_community_setup_create_button
                                ),
                                style = AmityTheme.typography.captionLegacy.copy(
                                    color = Color.White,
                                ),
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .testTag(getAccessibilityId())
                                    .fillMaxWidth()
                            )
                        }
                    }
                }
                Spacer(modifier = modifier.height(16.dp))
            }
        }

        if (showPrivacyConfirmDialog) {
            AmityAlertDialog(
                dialogTitle = context.amityStringResource(id = R.string.amity_v4_community_setup_dialog_change_privacy_title),
                dialogText = context.amityStringResource(id = R.string.amity_v4_community_setup_dialog_change_privacy_message),
                confirmText = context.amityStringResource(id = R.string.amity_v4_dialog_confirm_button),
                dismissText = context.amityStringResource(id = R.string.amity_v4_dialog_cancel_button),
                onConfirmation = {
                    showPrivacyConfirmDialog = false
                    updateCommunity(
                        communityId = communityToEdit?.getCommunityId() ?: "",
                        avatarUri = avatarUri,
                        displayName = name,
                        description = description,
                        isPublic = isPublic,
                        isDiscoverable = isDiscoverable,
                        requiresJoinApproval = isMembershipEnabled,
                        categoryIds = selectedCategories.map { it.getCategoryId() },
                        viewModel = viewModel,
                        pageScope = getPageScope(),
                        context = context,
                    )
                },
                onDismissRequest = {
                    showPrivacyConfirmDialog = false
                }
            )
        }

        if (showMediaCameraSelectionSheet) {
            AmityMediaImageSelectionSheet(
                modifier = modifier,
                pageScope = getPageScope(),
            ) { type ->
                showMediaCameraSelectionSheet = false

                type?.let {
                    when (type) {
                        AmityMediaImageSelectionType.CAMERA -> {
                            val permissions = arrayOf(
                                android.Manifest.permission.CAMERA,
                            )
                            cameraPermissionLauncher.launch(permissions)
                        }

                        AmityMediaImageSelectionType.IMAGE -> {
                            imagePickerLauncher.launch(PickVisualMediaRequest(mediaType = ActivityResultContracts.PickVisualMedia.ImageOnly))
                        }
                    }
                }
            }
        }

        if (showLeaveConfirmDialog) {
            val hasEnteredContent = name.isNotBlank() || description.isNotBlank() || selectedCategories.isNotEmpty() || !isPublic
            when {
                // Case 1: User has made edits in edit mode
                shouldActionButtonEnable && isInEditMode -> {
                    AmityAlertDialog(
                        dialogTitle = context.amityStringResource(id = R.string.amity_v4_community_setup_dialog_leave_edit_title),
                        dialogText = context.amityStringResource(id = R.string.amity_v4_community_setup_dialog_leave_edit_description),
                        confirmText = context.amityStringResource(id = R.string.amity_v4_dialog_discard_button),
                        dismissText = context.amityStringResource(id = R.string.amity_v4_dialog_cancel_button),
                        confirmTextColor = AmityTheme.colors.alert,
                        onConfirmation = { context.closePage() },
                        onDismissRequest = { showLeaveConfirmDialog = false }
                    )
                }
                // Case 2: User has made progress in create mode
                shouldActionButtonEnable || (!isInEditMode && hasEnteredContent) -> {
                    AmityAlertDialog(
                        dialogTitle = context.amityStringResource(id = R.string.amity_v4_community_setup_dialog_leave_title),
                        dialogText = context.amityStringResource(id = R.string.amity_v4_community_setup_dialog_leave_description),
                        confirmText = context.amityStringResource(id = R.string.amity_v4_dialog_leave_button),
                        dismissText = context.amityStringResource(id = R.string.amity_v4_dialog_cancel_button),
                        confirmTextColor = AmityTheme.colors.alert,
                        onConfirmation = { context.closePage() },
                        onDismissRequest = { showLeaveConfirmDialog = false }
                    )
                }
                // Case 3: No meaningful changes - just close
                else -> context.closePage()
            }
        }

        if (showJoinRequestNotEmptyDialog) {
            AmityAlertDialog(
                dialogTitle = "You have pending join requests",
                dialogText = "Please address these requests before switching off membership approval.",
                dismissText = "OK",
                onDismissRequest = { showJoinRequestNotEmptyDialog = false },
            )
        }
    }
}

fun updateCommunity(
    communityId: String,
    avatarUri: Uri,
    displayName: String,
    description: String,
    isPublic: Boolean,
    isDiscoverable: Boolean,
    requiresJoinApproval: Boolean,
    categoryIds: List<String>,
    viewModel: AmityCommunitySetupPageViewModel,
    pageScope: AmityComposePageScope,
    context: Context,
) {
    pageScope.showProgressSnackbar(context.amityStringResource(id = R.string.amity_v4_community_setup_toast_updating))
    viewModel.editCommunity(
        communityId = communityId,
        avatarUri = avatarUri,
        displayName = displayName,
        description = description,
        isPublic = isPublic,
        isDiscoverable = isDiscoverable,
        requiresJoinApproval = requiresJoinApproval,
        categoryIds = categoryIds,
        onSuccess = {
            AmityUIKitSnackbar.publishSnackbarMessage(context.getString(R.string.amity_v4_community_setup_toast_update_success))
            context.closePageWithResult(Activity.RESULT_OK)
        },
        onError = {
            AmityUIKitSnackbar.publishSnackbarMessage(context.getString(R.string.amity_v4_community_setup_toast_update_failed))
            context.closePageWithResult(Activity.RESULT_OK)
        }
    )
}

@Preview(showBackground = true)
@Composable
fun AmityCommunitySetupPagePreview() {
    AmityUIKitConfigController.setup(LocalContext.current)
    AmityCommunitySetupPage(
        mode = AmityCommunitySetupPageMode.Create
    )
}
