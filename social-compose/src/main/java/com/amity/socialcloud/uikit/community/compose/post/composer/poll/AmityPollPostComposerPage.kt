package com.amity.socialcloud.uikit.community.compose.post.composer.poll

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.waterfall
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.amity.socialcloud.sdk.api.social.post.review.AmityReviewStatus
import com.amity.socialcloud.sdk.helper.core.coroutines.await
import com.amity.socialcloud.sdk.helper.core.hashtag.AmityHashtag
import com.amity.socialcloud.sdk.helper.core.mention.AmityMentionMetadata
import com.amity.socialcloud.sdk.model.core.file.upload.AmityUploadResult
import com.amity.socialcloud.sdk.model.core.user.AmityUser
import com.amity.socialcloud.sdk.model.social.community.AmityCommunity
import com.amity.socialcloud.sdk.model.social.post.AmityPost
import com.amity.socialcloud.uikit.common.ui.base.AmityBaseElement
import com.amity.socialcloud.uikit.common.ui.base.AmityBasePage
import com.amity.socialcloud.uikit.common.ui.elements.AmityAlertDialog
import com.amity.socialcloud.uikit.common.ui.elements.AmityBasicTextField
import com.amity.socialcloud.uikit.common.ui.elements.AmityDatePickerDialog
import com.amity.socialcloud.uikit.common.ui.elements.AmityTextField
import com.amity.socialcloud.uikit.common.ui.elements.AmityTimePickerDialog
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposeComponentScope
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.clickableWithoutRipple
import com.amity.socialcloud.uikit.common.utils.closePageWithResult
import com.amity.socialcloud.uikit.community.compose.R
import com.amity.socialcloud.uikit.community.compose.post.composer.AmityPostComposerHelper
import com.amity.socialcloud.uikit.community.compose.post.composer.AmityPostComposerPageViewModel
import com.amity.socialcloud.uikit.community.compose.post.composer.RenderAltTextConfigSheet
import com.amity.socialcloud.uikit.community.compose.post.composer.components.AltTextMedia
import com.amity.socialcloud.uikit.community.compose.post.model.AmityFileUploadState
import com.amity.socialcloud.uikit.community.compose.ui.components.mentions.AmityMentionSuggestionView
import com.amity.socialcloud.uikit.community.compose.ui.components.mentions.AmityMentionTextField
import kotlinx.coroutines.launch
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import org.joda.time.format.DateTimeFormat

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun AmityPollPostComposerPage(
    modifier: Modifier = Modifier,
    targetId: String,
    targetType: AmityPost.TargetType,
    targetCommunity: AmityCommunity? = null,
    type: String? = "text",
) {
    val context = LocalContext.current
    var topBarHeight by remember { mutableStateOf(0.dp) }
    val density = LocalDensity.current
    var imagePollCardHeight by remember { mutableStateOf(0.dp) }
    val focusManager = LocalFocusManager.current

    val viewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current) {
        "No ViewModelStoreOwner was provided via LocalViewModelStoreOwner"
    }
    val viewModel =
        viewModel<AmityPollPostComposerViewModel>(viewModelStoreOwner = viewModelStoreOwner)

    val postComposerViewModel =
        viewModel<AmityPostComposerPageViewModel>(viewModelStoreOwner = viewModelStoreOwner)

    val coroutineScope = rememberCoroutineScope()

    val formatter = DateTimeFormat.forPattern("dd MMM 'at' h:mm a")
        .withLocale(context.resources.configuration.locale)
    val durationMap = mapOf(
        1 to "1 day",
        3 to "3 days",
        7 to "7 days",
        14 to "14 days",
        30 to "30 days"
    )
    val durationKeys = durationMap.keys.toList()
    val durationOptions = durationMap.values.toList()
    val QUESTION_MAX_CHAR = 500
    val OPTION_MAX_CHAR = 60
    val IMAGE_OPTION_MAX_CHAR = 20
    val DURATION_MAX_DAYS = 30
    val MIN_OPTIONS_REQUIRED = 2
    val maxTitleChar = 150

    val title = targetCommunity?.getDisplayName() ?: "My Timeline"
    var queryToken by remember { mutableStateOf("") }
    var shouldShowSuggestion by remember { mutableStateOf(false) }
    var selectedUserToMention by remember { mutableStateOf<AmityUser?>(null) }
    var mentionedUsers by remember { mutableStateOf<List<AmityMentionMetadata.USER>>(emptyList()) }
    var hashtags by remember { mutableStateOf<List<AmityHashtag>>(emptyList()) }
    var postTitle by remember { mutableStateOf("") }
    var question by remember { mutableStateOf("") }
    var textFields by remember {
        mutableStateOf(listOf(TextFieldValue(), TextFieldValue()))
    }

    val textAndImage by viewModel.imagePollUiState.collectAsState()
    var selectedImageIndex by remember { mutableStateOf<Int?>(null) }
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        selectedImageIndex?.let { index ->
            uri?.let { selectedUri ->
                viewModel.updateImagePollUiState(
                    index = index,
                    newState = textAndImage[index].copy(imageUri = selectedUri)
                )
                // Start upload immediately after image selection
                viewModel.uploadMediaForIndex(index, selectedUri)
            }
        }
        selectedImageIndex = null
    }

    var shouldShowUploadErrorBottomSheetDialog by remember { mutableStateOf(false) }

    var expanded by remember { mutableStateOf(false) }
    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }
    var selectedPollDurationIndex by remember { mutableIntStateOf(4) }
    var isChecked by remember {
        mutableStateOf(false)
    }
    var isCreating by remember { mutableStateOf(false) }
    var isEnabled by remember { mutableStateOf(false) }
    var showDiscardPostDialog by remember { mutableStateOf(false) }
    var showPendingPostDialog by remember { mutableStateOf(false) }
    val maxDate = DateTime.now().plusDays(DURATION_MAX_DAYS)
    var selectedDate = maxDate

    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = maxDate.millis,
        selectableDates =
            object : SelectableDates {
                override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                    val date = DateTime(utcTimeMillis, DateTimeZone.UTC)
                    return !date.isBefore(DateTime.now()) && !date.isAfter(maxDate)
                }
            }
    )
    val timePickerState = rememberTimePickerState(
        initialHour = selectedDate.toLocalTime().hourOfDay,
        initialMinute = selectedDate.toLocalTime().minuteOfHour
    )

    LaunchedEffect(postTitle, question, textFields, isCreating, textAndImage) {
        val isImageUploaded = if (type == "image") {
            textAndImage.filter { it.image != null && it.uploadError == null }.size >= 2
                    && textAndImage.none { it.answer.text.isNotBlank() && it.image == null }
                    && textAndImage.none { it.uploadError != null }
                    && textAndImage.none { it.uploadState == AmityFileUploadState.UPLOADING }
        } else {
            true
        }

        val isTextFilled = if (type == "text") {
            textFields.filter { it.text.trim().isNotEmpty() }.size >= MIN_OPTIONS_REQUIRED
                    && textFields.all { it.text.trim().length <= OPTION_MAX_CHAR }
        } else {
            true
        }

        isEnabled = question.trim().isNotEmpty()
                && isTextFilled
                && postTitle.trim().length <= maxTitleChar
                && question.trim().length <= QUESTION_MAX_CHAR
                && !isCreating && isImageUploaded
    }

    BackHandler {
        val hasNoInput = question.trim().isEmpty()
                && postTitle.trim().isEmpty()
                && textFields.none { it.text.trim().isNotEmpty() }
                && (type != "image" || textAndImage.none { it.image != null })
        if (hasNoInput) {
            context.closePageWithResult(Activity.RESULT_CANCELED)
        } else {
            showDiscardPostDialog = true
        }
    }

    val scrollState = rememberLazyGridState()
    val hasScrolled by remember {
        derivedStateOf { scrollState.firstVisibleItemScrollOffset > 0 }
    }

    AmityBasePage(pageId = "poll_post_composer_page") {
        Scaffold(
            containerColor = AmityTheme.colors.background,
            topBar = {
                Box(
                    modifier = modifier
                        .fillMaxWidth()
                        .background(AmityTheme.colors.background)
                        .padding(start = 12.dp, end = 16.dp)
                        .onGloballyPositioned { coordinates ->
                            topBarHeight = with(density) { coordinates.size.height.toDp() }
                        }
                ) {
                    AmityBaseElement(
                        pageScope = getPageScope(),
                        elementId = "close_button"
                    ) {
                        Icon(
                            //painter = painterResource(id = getConfig().getIcon()),
                            Icons.Default.Close,
                            contentDescription = "Close Button",
                            tint = AmityTheme.colors.base,
                            modifier = modifier
                                .align(Alignment.CenterStart)
                                .size(24.dp)
                                .padding(2.dp)
                                .clickableWithoutRipple {
                                    val hasNoInput = question.trim().isEmpty()
                                            && postTitle.trim().isEmpty()
                                            && textFields.none { it.text.trim().isNotEmpty() }
                                            && (type != "image" || textAndImage.none { it.image != null })
                                    if (hasNoInput) {
                                        context.closePageWithResult(Activity.RESULT_CANCELED)
                                    } else {
                                        showDiscardPostDialog = true
                                    }
                                }
                                .testTag(getAccessibilityId())
                        )
                    }

                    AmityBaseElement(
                        pageScope = getPageScope(),
                        elementId = "community_title"
                    ) {
                        Text(
                            text = title,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            style = AmityTheme.typography.titleLegacy,
                            modifier = modifier
                                .align(Alignment.Center)
                                .padding(vertical = 16.dp, horizontal = 48.dp)
                                .testTag(getAccessibilityId())
                        )
                    }

                    AmityBaseElement(
                        pageScope = getPageScope(),
                        elementId = "create_new_post_button"
                    ) {
                        Text(
                            text = "Post",
                            style = AmityTheme.typography.bodyLegacy.copy(
                                color = if (isEnabled) AmityTheme.colors.primary else AmityTheme.colors.primaryShade2
                            ),
                            modifier = modifier
                                .align(Alignment.CenterEnd)
                                .clickableWithoutRipple(enabled = isEnabled) {
                                    if (!isCreating) {
                                        isCreating = true
                                        coroutineScope.launch {
                                            getPageScope().showProgressSnackbar("Posting...")
                                            val durationInput =
                                                if (selectedPollDurationIndex == -1) {
                                                    selectedDate.millis - DateTime.now().millis
                                                } else {
                                                    durationKeys[selectedPollDurationIndex] * 24 * 60 * 60 * 1000L
                                                }
                                            try {
                                                val post = viewModel
                                                    .createPost(
                                                        question = question,
                                                        postTitle = postTitle.trim(),
                                                        options = textFields
                                                            .map { it.text }
                                                            .filter {
                                                                it.trim().isNotEmpty()
                                                            },
                                                        imagePoll = if (type == "image") textAndImage else null,
                                                        isMultipleChoice = isChecked,
                                                        duration = durationInput,
                                                        targetId = targetId,
                                                        targetType = targetType,
                                                        mentionedUsers = mentionedUsers,
                                                        hashtags = hashtags,
                                                    )
                                                    .await()
                                                if (post.getReviewStatus() == AmityReviewStatus.UNDER_REVIEW) {
                                                    showPendingPostDialog = true
                                                } else {
                                                    AmityPostComposerHelper.addNewPost(post)
                                                    context.closePageWithResult(Activity.RESULT_OK)
                                                }
                                            } catch (e: Exception) {
                                                val text =
                                                    "Failed to create post. Please try again."
                                                getPageScope().showSnackbar(
                                                    message = text,
                                                    drawableRes = R.drawable.amity_ic_snack_bar_warning,
                                                    additionalHeight = 52,
                                                )
                                            } finally {
                                                isCreating = false
                                            }
                                        }
                                    }
                                }
                                .testTag(getAccessibilityId()),
                        )
                    }
                }
            },
            contentWindowInsets = WindowInsets(0.dp, 0.dp, 0.dp, 0.dp),
        ) {
            it
            Box(
                modifier = modifier
                    .fillMaxSize()
                    .padding(top = topBarHeight)
                    .windowInsetsPadding(WindowInsets.safeDrawing)
                    .pointerInput(Unit) {
                        detectTapGestures(onTap = {
                            focusManager.clearFocus()
                        })
                    }
            ) {
                if (hasScrolled) {
                    HorizontalDivider(
                        color = AmityTheme.colors.baseShade4,
                        thickness = 1.dp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.TopCenter)
                    )
                }
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    state = scrollState,
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 12.dp),
                ) {
                    // Post title section
                    item(span = { GridItemSpan(maxLineSpan) }) {
                        var isFocused by remember { mutableStateOf(false) }
                        val isNotEmpty = postTitle.trim().isNotEmpty()

                        Column(
                            modifier = Modifier.padding(horizontal = 4.dp)
                        ) {
                            Spacer(modifier = modifier.height(24.dp))

                            // Post title section
                            Row(
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 4.dp)
                            ) {
                                AmityBaseElement(
                                    pageScope = getPageScope(),
                                    elementId = "post_title_label"
                                ) {
                                    Text(
                                        text = "Post title (Optional)",
                                        style = AmityTheme.typography.titleLegacy,
                                        modifier = modifier.testTag(getAccessibilityId())
                                    )
                                }

                                Spacer(modifier = modifier.height(4.dp))

                                Text(
                                    text = "${postTitle.length}/${maxTitleChar}",
                                    style = AmityTheme.typography.captionLegacy.copy(
                                        fontWeight = FontWeight.Normal,
                                        color = AmityTheme.colors.baseShade1,
                                    )
                                )
                            }

                            Spacer(modifier = modifier.height(4.dp))

                            // Post title text field
                            AmityMentionTextField(
                                value = postTitle,
                                onValueChange = {
                                    // Remove line breaks and convert them to spaces
                                    val cleanedText = it.replace(Regex("[\r\n]+"), " ")
                                    if (cleanedText.length <= maxTitleChar) {
                                        postTitle = cleanedText
                                    }
                                },
                                hintText = "Give your poll a headline",
                                hintColor = AmityTheme.colors.baseShade3,
                                textStyle = AmityTheme.typography.titleBold.copy(
                                    color = AmityTheme.colors.baseShade3,
                                    fontSize = 17.sp,
                                    textAlign = TextAlign.Start
                                ),
                                keyboardOptions = KeyboardOptions(
                                    capitalization = KeyboardCapitalization.Sentences,
                                    imeAction = ImeAction.Next
                                ),
                                isEnabled = !isCreating,
                                cursorColor = AmityTheme.colors.primary,
                                contentPadding = PaddingValues(horizontal = 4.dp, vertical = 16.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .onFocusChanged { focus ->
                                        isFocused = focus.isFocused
                                    }
                                    .background(
                                        color = Color.Transparent,
                                        shape = RoundedCornerShape(4.dp)
                                    )
                            )

                            HorizontalDivider(
                                color = if (postTitle.trim().length > maxTitleChar) AmityTheme.colors.alert else AmityTheme.colors.divider,
                                modifier = modifier.padding(horizontal = 4.dp)
                            )

                            if (postTitle.trim().length > maxTitleChar) {
                                Spacer(modifier = modifier.height(8.dp))

                                Text(
                                    modifier = Modifier.padding(horizontal = 16.dp),
                                    text = "Post title cannot exceed ${maxTitleChar} characters.",
                                    style = AmityTheme.typography.captionLegacy.copy(
                                        fontWeight = FontWeight.Normal,
                                        color = AmityTheme.colors.alert,
                                    )
                                )
                            }
                        }
                    }

                    //Poll Question Section
                    item(span = { GridItemSpan(maxLineSpan) }) {
                        val isNotEmpty = question.trim().isNotEmpty()
                        var isFocused by remember { mutableStateOf(false) }

                        Column(
                            modifier = Modifier.padding(horizontal = 4.dp)
                        ) {
                            Spacer(modifier = modifier.height(24.dp))

                            Row(
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 4.dp)
                            ) {
                                AmityBaseElement(
                                    pageScope = getPageScope(),
                                    elementId = "poll_question_title"
                                ) {
                                    Text(
                                        text = "Poll question",
                                        style = AmityTheme.typography.titleLegacy,
                                        modifier = modifier.testTag(getAccessibilityId())
                                    )
                                }

                                Spacer(modifier = modifier.height(4.dp))

                                Text(
                                    text = "${question.length}/${QUESTION_MAX_CHAR}",
                                    style = AmityTheme.typography.captionLegacy.copy(
                                        fontWeight = FontWeight.Normal,
                                        color = AmityTheme.colors.baseShade1,
                                    )
                                )
                            }

                            Spacer(modifier = modifier.height(4.dp))

                            AmityMentionTextField(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .onFocusChanged { focus ->
                                        isFocused = focus.isFocused
                                    }
                                    .heightIn(min = 112.dp)
                                    .background(
                                        color = Color.Transparent,
                                        shape = RoundedCornerShape(4.dp)
                                    )
                                    .padding(4.dp),
                                value = question,
                                hintText = "What's your poll question?",
                                maxLines = 30,
                                maxChar = QUESTION_MAX_CHAR,
                                mentionedUser = selectedUserToMention,
                                // Poll-specific styling
                                textStyle = AmityTheme.typography.body.copy(
                                    color = AmityTheme.colors.base,
                                    fontSize = 16.sp  // Match font size with post composer
                                ),
                                hintColor = AmityTheme.colors.baseShade3,
                                contentPadding = PaddingValues(vertical = 10.dp), // Minimal padding in the text field itself
                                verticalPadding = 0.dp,
                                horizontalPadding = 0.dp,
                                backgroundColor = Color.Transparent,
                                // Disable when creating poll
                                isEnabled = !isCreating,
                                onValueChange = {
                                    question = it
                                },
                                onMentionAdded = {
                                    selectedUserToMention = null
                                },
                                onQueryToken = {
                                    queryToken = it ?: ""
                                    shouldShowSuggestion = (it != null)
                                },
                                onUserMentions = {
                                    mentionedUsers = it
                                },
                                onHashtags = {
                                    hashtags = it
                                },
                            )

                            HorizontalDivider(
                                color = AmityTheme.colors.divider,
                                modifier = modifier.padding(horizontal = 4.dp)
                            )
                        }
                    }

                    //Poll Options title and description Section
                    item(span = { GridItemSpan(maxLineSpan) }) {
                        Column {
                            Spacer(modifier = modifier.height(24.dp))

                            Row(
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 4.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                ) {
                                    AmityBaseElement(
                                        pageScope = getPageScope(),
                                        elementId = "poll_options_title"
                                    ) {
                                        Text(
                                            text = "Options", //getConfig().getText(),
                                            style = AmityTheme.typography.titleLegacy,
                                            modifier = modifier.testTag(getAccessibilityId())
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = modifier.height(4.dp))

                            AmityBaseElement(
                                pageScope = getPageScope(),
                                elementId = "poll_options_description"
                            ) {
                                val text = if (type == "text") {
                                    "Poll must contain at least ${MIN_OPTIONS_REQUIRED} options."
                                } else {
                                    "Poll must contain at least ${MIN_OPTIONS_REQUIRED} options. and an image must be uploaded for every option."
                                }
                                Text(
                                    modifier = Modifier.padding(horizontal = 4.dp),
                                    text = text,
                                    style = AmityTheme.typography.captionLegacy.copy(
                                        fontWeight = FontWeight.Normal,
                                        color = AmityTheme.colors.baseShade1,
                                    )
                                )
                            }

                            Spacer(modifier = modifier.height(if (type == "text") 20.dp else 16.dp))
                        }
                    }

                    if (type == "text") {
                        // Poll Options Text Section
                        item(span = { GridItemSpan(maxLineSpan) }) {
                            Column(
                            ) {
                                textFields.forEachIndexed { index, textFieldValue ->
                                    var isFocused by remember { mutableStateOf(false) }
                                    val isNotEmpty = textFieldValue.text.trim().isNotEmpty()

                                    Row(
                                        modifier = Modifier
                                            .padding(horizontal = 4.dp)
                                            .fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                                    ) {

                                        AmityBasicTextField(
                                            value = textFieldValue,
                                            maxChar = OPTION_MAX_CHAR,
                                            onValueChange = { newValue: TextFieldValue ->
                                                val textValue =
                                                    if (newValue.text.contains("\n")) TextFieldValue(
                                                        text = newValue.text.replace(
                                                            "\n",
                                                            " "
                                                        )
                                                    ) else newValue
                                                textFields =
                                                    textFields.toMutableList().also { currentText ->
                                                        currentText[index] = textValue
                                                    }
                                            },
                                            textStyle = AmityTheme.typography.bodyLegacy.copy(
                                                color = AmityTheme.colors.base
                                            ),
                                            modifier = Modifier
                                                .weight(1f)
                                                .heightIn(max = 160.dp)
                                                .onFocusChanged { focus ->
                                                    isFocused = focus.isFocused
                                                }
                                                .border(
                                                    BorderStroke(
                                                        width = 1.dp,
                                                        color = AmityTheme.colors.baseShade4
                                                    ),
                                                    shape = RoundedCornerShape(8.dp)
                                                )
                                                .background(
                                                    color = AmityTheme.colors.baseShade4,
                                                    shape = RoundedCornerShape(8.dp)
                                                ),
                                            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                                            placeholder = {
                                                Text(
                                                    text = "Option ${index + 1}",
                                                    style = AmityTheme.typography.bodyLegacy.copy(
                                                        color = AmityTheme.colors.baseShade2
                                                    )
                                                )
                                            },
                                            readOnly = isCreating,
                                            colors = TextFieldDefaults.colors(
                                                unfocusedContainerColor = Color.Transparent,
                                                focusedContainerColor = Color.Transparent,
                                                unfocusedIndicatorColor = Color.Transparent,
                                                focusedIndicatorColor = Color.Transparent,
                                            ),
                                            singleLine = false,
                                            minHeight = 40.dp,
                                            contentPadding = PaddingValues(
                                                start = 12.dp,
                                                end = 12.dp,
                                                top = 12.dp,
                                                bottom = 12.dp
                                            ),
                                            maxLines = 5,
                                        )

                                        IconButton(
                                            modifier = Modifier
                                                .size(20.dp),
                                            enabled = !isCreating,
                                            onClick = {
                                                textFields = textFields
                                                    .toMutableList()
                                                    .also {
                                                        it.removeAt(index)
                                                    }
                                            }
                                        ) {
                                            Icon(
                                                painter = painterResource(R.drawable.amity_ic_delete_trash),
                                                contentDescription = "Remove TextField",
                                                tint = AmityTheme.colors.base
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(12.dp))
                                }

                                if (textFields.size < 10) {
                                    AmityBaseElement(
                                        pageScope = getPageScope(),
                                        elementId = "poll_add_option_button"
                                    ) {
                                        Row(
                                            modifier = Modifier
                                                .testTag("poll_add_option_button")
                                                .height(40.dp)
                                                .fillMaxWidth()
                                                .padding(start = 4.dp, end = 36.dp)
                                                .border(
                                                    BorderStroke(
                                                        1.dp,
                                                        AmityTheme.colors.baseShade3
                                                    ),
                                                    RoundedCornerShape(8.dp)
                                                )
                                                .clickableWithoutRipple {
                                                    if (isCreating) {
                                                        return@clickableWithoutRipple
                                                    }
                                                    textFields = textFields + TextFieldValue()
                                                },
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.Center
                                        ) {
                                            Icon(
                                                painter = painterResource(R.drawable.amity_ic_add),
                                                contentDescription = "Create",
                                                tint = AmityTheme.colors.secondary,
                                                modifier = modifier.size(16.dp)
                                            )

                                            Spacer(modifier = modifier.width(8.dp))

                                            Text(
                                                text = "Add option",
                                                style = AmityTheme.typography.captionLegacy.copy(
                                                    color = AmityTheme.colors.secondary,
                                                ),
                                                textAlign = TextAlign.Center,
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        itemsIndexed(textAndImage) { index, data ->
                            AmityPollImageTypeItemView(
                                uiState = data,
                                placeHolderText = "Option ${index + 1}",
                                onTextChange = { newValue ->
                                    if (!isCreating) {
                                        val textValue =
                                            if (newValue.text.contains("\n")) TextFieldValue(
                                                text = newValue.text.replace("\n", " ")
                                            ) else newValue

                                        viewModel.updateImagePollUiState(
                                            index = index,
                                            newState = data.copy(answer = textValue)
                                        )
                                    }
                                },
                                maxChar = IMAGE_OPTION_MAX_CHAR,
                                onSelectImageClick = {
                                    if (!isCreating) {
                                        selectedImageIndex = index
                                        imagePickerLauncher.launch(
                                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                        )
                                    }
                                },
                                onErrorUploadImageClick = {
                                    if (!isCreating) {
                                        selectedImageIndex = index
                                        shouldShowUploadErrorBottomSheetDialog = true
                                    }
                                },
                                onRemoveAnswerClick = {
                                    if (!isCreating) {
                                        viewModel.removeImagePollOption(data)
                                    }
                                },
                                onAltTextClick = { altText ->
                                    selectedImageIndex = index
                                    postComposerViewModel.setAltTextMedia(
                                        altText
                                    )
                                    postComposerViewModel.showAltTextConfigSheet()
                                },
                                onCardHeight = { cardHeightDp ->
                                    imagePollCardHeight = cardHeightDp
                                }
                            )
                        }

                        if (textAndImage.size < 10) {
                            item {
                                AmityPollImageTypeAddItemView(
                                    modifier = Modifier
                                        .height(imagePollCardHeight),
                                    onAddClick = {
                                        viewModel.addImagePollOption()
                                    }
                                )
                            }
                        }
                    }

                    // Poll Multiple Selection and Duration Section
                    item(span = { GridItemSpan(maxLineSpan) }) {
                        Column {
                            Spacer(modifier = modifier.height(24.dp))

                            HorizontalDivider(
                                color = AmityTheme.colors.divider,
                                modifier = modifier.padding(horizontal = 4.dp)
                            )

                            Spacer(modifier = modifier.height(24.dp))

                            Row(
                                modifier = Modifier
                                    .padding(horizontal = 4.dp),
                                verticalAlignment = Alignment.Top,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {

                                Column(
                                    modifier = Modifier.weight(1f)
                                ) {
                                    AmityBaseElement(
                                        pageScope = getPageScope(),
                                        elementId = "poll_multiple_selection_title"
                                    ) {
                                        Text(
                                            text = "Multiple selection",
                                            style = AmityTheme.typography.titleLegacy,
                                            modifier = modifier
                                                .testTag(getAccessibilityId())
                                        )
                                    }

                                    Spacer(modifier = modifier.height(4.dp))

                                    AmityBaseElement(
                                        pageScope = getPageScope(),
                                        elementId = "poll_multiple_selection_description"
                                    ) {
                                        Text(
                                            text = "Let participants vote more than one option.",
                                            style = AmityTheme.typography.captionLegacy.copy(
                                                fontWeight = FontWeight.Normal,
                                                color = AmityTheme.colors.baseShade1,
                                            )
                                        )
                                    }
                                }

                                Switch(
                                    modifier = Modifier.height(28.dp),
                                    checked = isChecked,
                                    colors = SwitchDefaults.colors(
                                        checkedTrackColor = AmityTheme.colors.primary,
                                        uncheckedBorderColor = AmityTheme.colors.baseShade3,
                                        uncheckedThumbColor = Color.White,
                                        uncheckedTrackColor = AmityTheme.colors.baseShade3,
                                    ),
                                    onCheckedChange = {
                                        if (isCreating) {
                                            return@Switch
                                        }
                                        isChecked = !isChecked
                                    }
                                )
                            }

                            Spacer(modifier = modifier.height(24.dp))

                            HorizontalDivider(
                                color = AmityTheme.colors.divider,
                                modifier = modifier.padding(horizontal = 4.dp)
                            )
                        }
                    }

                    // Poll Duration Section
                    item(span = { GridItemSpan(maxLineSpan) }) {
                        Column {
                            Spacer(modifier = modifier.height(24.dp))

                            AmityBaseElement(
                                pageScope = getPageScope(),
                                elementId = "poll_duration_title"
                            ) {
                                Text(
                                    text = "Poll duration",
                                    style = AmityTheme.typography.titleLegacy,
                                    modifier = modifier
                                        .padding(horizontal = 4.dp)
                                        .testTag(getAccessibilityId())
                                )
                            }

                            Spacer(modifier = modifier.height(4.dp))

                            Text(
                                modifier = Modifier.padding(horizontal = 4.dp),
                                text = "You can always close the poll before the set duration.",
                                style = AmityTheme.typography.captionLegacy.copy(
                                    fontWeight = FontWeight.Normal,
                                    color = AmityTheme.colors.baseShade1,
                                )
                            )

                            Spacer(modifier = Modifier.height(18.dp))

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 4.dp),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Text(
                                    text = if (selectedPollDurationIndex == -1) "Custom end date" else durationOptions[selectedPollDurationIndex],
                                    style = AmityTheme.typography.bodyLegacy.copy(
                                        color = AmityTheme.colors.base,
                                    ),
                                    modifier = Modifier
                                        .weight(1f)
                                        .clickableWithoutRipple {
                                            if (isCreating) {
                                                return@clickableWithoutRipple
                                            }
                                            expanded = true
                                        }
                                )
                                Icon(
                                    Icons.Filled.KeyboardArrowDown,
                                    contentDescription = "Select duration",
                                    tint = AmityTheme.colors.baseShade2,
                                    modifier = modifier
                                        .size(24.dp)
                                        .clickableWithoutRipple {
                                            if (isCreating) {
                                                return@clickableWithoutRipple
                                            }
                                            expanded = true
                                        }
                                )
                            }
                            Spacer(modifier = modifier.height(16.dp))

                            HorizontalDivider(
                                color = AmityTheme.colors.divider,
                                modifier = modifier.padding(horizontal = 4.dp)
                            )

                            if (selectedPollDurationIndex == -1) {
                                Row(
                                    modifier = Modifier
                                        .height(72.dp)
                                        .padding(horizontal = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    Text(
                                        modifier = Modifier.weight(1f),
                                        text = "Ends on",
                                        style = AmityTheme.typography.bodyLegacy.copy(
                                            fontWeight = FontWeight.Normal,
                                            color = AmityTheme.colors.base,
                                        )
                                    )
                                    val dayFormat = DateTimeFormat.forPattern("dd MMM yyyy")
                                    Text(
                                        modifier = Modifier
                                            .background(
                                                AmityTheme.colors.baseShade4,
                                                RoundedCornerShape(8.dp)
                                            )
                                            .padding(12.dp)
                                            .clickableWithoutRipple {
                                                if (isCreating) {
                                                    return@clickableWithoutRipple
                                                }
                                                showDatePicker = true
                                            },
                                        text = selectedDate.toString(dayFormat),
                                        style = AmityTheme.typography.bodyLegacy.copy(
                                            fontWeight = FontWeight.Normal,
                                            color = AmityTheme.colors.base,
                                        )
                                    )
                                    val timeFormat = DateTimeFormat.forPattern("hh:mm a")
                                    Text(
                                        modifier = Modifier
                                            .background(
                                                AmityTheme.colors.baseShade4,
                                                RoundedCornerShape(8.dp)
                                            )
                                            .padding(12.dp)
                                            .clickableWithoutRipple {
                                                if (isCreating) {
                                                    return@clickableWithoutRipple
                                                }
                                                showTimePicker = true
                                            },
                                        text = selectedDate.toString(timeFormat),
                                        style = AmityTheme.typography.bodyLegacy.copy(
                                            fontWeight = FontWeight.Normal,
                                            color = AmityTheme.colors.base,
                                        )
                                    )
                                }
                            } else {
                                Spacer(modifier = modifier.height(6.dp))

                                val endText = "Ends on " + DateTime.now().plusDays(
                                    durationKeys[selectedPollDurationIndex]
                                ).toString(formatter)

                                Text(
                                    modifier = Modifier.padding(horizontal = 4.dp),
                                    text = endText,
                                    style = AmityTheme.typography.captionLegacy.copy(
                                        fontWeight = FontWeight.Normal,
                                        color = AmityTheme.colors.baseShade1,
                                    )
                                )
                            }

                            Spacer(modifier = modifier.height(24.dp))
                        }
                    }

                }

                if (showDiscardPostDialog) {
                    AmityAlertDialog(
                        dialogTitle = "Discard this post?",
                        dialogText = "The post will be permanently discarded. It cannot be undone.",
                        confirmText = "Discard",
                        dismissText = "Keep editing",
                        confirmTextColor = AmityTheme.colors.alert,
                        dismissTextColor = AmityTheme.colors.highlight,
                        onConfirmation = {
                            context.closePageWithResult(Activity.RESULT_CANCELED)
                        },
                        onDismissRequest = {
                            showDiscardPostDialog = false
                        }
                    )
                }

                if (showPendingPostDialog) {
                    AmityAlertDialog(
                        dialogTitle = "Posts sent for review",
                        dialogText = "Your post has been submitted to the pending list. It will be published once approved by the community moderator.",
                        dismissText = "OK",
                    ) {
                        showPendingPostDialog = false
                        context.closePageWithResult(Activity.RESULT_OK)
                    }
                }

                if (showDatePicker) {
                    AmityDatePickerDialog(
                        datePickerState = datePickerState,
                        onDismissRequest = {
                            showDatePicker = false
                        },
                        confirmButton = {
                            TextButton(
                                onClick = {
                                    showDatePicker = false
                                    val date = DateTime(datePickerState.selectedDateMillis)
                                    selectedDate = DateTime(
                                        date.year,
                                        date.monthOfYear,
                                        date.dayOfMonth,
                                        timePickerState.hour,
                                        timePickerState.minute
                                    )
                                }
                            ) {
                                Text("OK", color = AmityTheme.colors.primary)
                            }
                        },
                        dismissButton = {
                            TextButton(
                                onClick = {
                                    showDatePicker = false
                                }
                            ) {
                                Text("CANCEL", color = AmityTheme.colors.primary)
                            }
                        }
                    )
                }

                if (showTimePicker) {
                    AmityTimePickerDialog(
                        timePickerState = timePickerState,
                        onDismissRequest = {
                            showTimePicker = false
                        },
                        confirmButton = {
                            TextButton(
                                onClick = {
                                    showTimePicker = false
                                    selectedDate = DateTime(
                                        selectedDate.year,
                                        selectedDate.monthOfYear,
                                        selectedDate.dayOfMonth,
                                        timePickerState.hour,
                                        timePickerState.minute
                                    )
                                }
                            ) {
                                Text("OK", color = AmityTheme.colors.primary)
                            }
                        },
                        dismissButton = {
                            TextButton(
                                onClick = {
                                    showTimePicker = false
                                }
                            ) {
                                Text("CANCEL", color = AmityTheme.colors.primary)
                            }
                        }
                    )
                }

                AmityPollDurationSelectionBottomSheet(
                    modifier = Modifier,
                    shouldShow = expanded,
                    durationOptions = durationOptions,
                    selectedIndex = selectedPollDurationIndex,
                    onSelected = {
                        selectedPollDurationIndex = it
                        expanded = false
                    },
                    onDismiss = { expanded = false },
                )

                if (shouldShowSuggestion) {
                    AmityMentionSuggestionView(
                        heightIn = 150.dp,  // Slightly smaller than post composer
                        shape = RoundedCornerShape(8.dp),
                        community = targetCommunity,
                        keyword = queryToken,
                        modifier = Modifier.align(Alignment.BottomStart),
                    ) {
                        selectedUserToMention = it
                        shouldShowSuggestion = false
                    }
                }

                if (shouldShowUploadErrorBottomSheetDialog) {
                    Dialog(
                        onDismissRequest = {
                            shouldShowUploadErrorBottomSheetDialog = false
                        },
                        DialogProperties(
                            usePlatformDefaultWidth = false
                        )
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize(),
                            contentAlignment = Alignment.BottomCenter
                        ) {
                            Column {
                                Column(
                                    modifier = Modifier
                                        .padding(horizontal = 8.dp)
                                        .clip(RoundedCornerShape(14.dp))
                                        .background(AmityTheme.colors.baseShade4)
                                        .fillMaxWidth()
                                ) {
                                    Column(
                                        modifier = Modifier.padding(vertical = 13.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Text(
                                            text = "Your image couldn’t be uploaded",
                                            fontSize = 13.sp,
                                            lineHeight = 18.sp,
                                            fontWeight = FontWeight(600),
                                            textAlign = TextAlign.Center,
                                            modifier = Modifier.fillMaxWidth(),
                                            color = AmityTheme.colors.secondary.copy(alpha = 0.5f),
                                        )
                                    }
                                    HorizontalDivider(
                                        thickness = 1.dp,
                                        color = AmityTheme.colors.secondaryShade1
                                    )
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                    ) {
                                        TextButton(
                                            onClick = {
                                                shouldShowUploadErrorBottomSheetDialog = false
                                                selectedImageIndex?.let { index ->
                                                    val imageUri = textAndImage[index].imageUri
                                                    imageUri?.let { uri ->
                                                        viewModel.uploadMediaForIndex(index, uri)
                                                    }
                                                }
                                            },
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            Text(
                                                text = "Retry",
                                                fontSize = 17.sp,
                                                lineHeight = 22.sp,
                                                fontWeight = FontWeight(600),
                                                color = Color(0xFF007AFF),
                                            )
                                        }
                                    }
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                    ) {
                                        TextButton(
                                            onClick = {
                                                shouldShowUploadErrorBottomSheetDialog = false
                                                imagePickerLauncher.launch(
                                                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                                )
                                            },
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            Text(
                                                text = "Upload new image",
                                                fontSize = 17.sp,
                                                lineHeight = 22.sp,
                                                fontWeight = FontWeight(600),
                                                color = Color(0xFF007AFF),
                                            )
                                        }
                                    }
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                Column(
                                    modifier = Modifier
                                        .padding(horizontal = 8.dp)
                                        .clip(RoundedCornerShape(14.dp))
                                        .background(AmityTheme.colors.baseShade4)
                                        .fillMaxWidth()
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                    ) {
                                        TextButton(
                                            onClick = {
                                                shouldShowUploadErrorBottomSheetDialog = false
                                                //Logic for clear image
//                                                selectedImageIndex?.let { index ->
//                                                    viewModel.updateImagePollUiState(
//                                                        index,
//                                                        textAndImage[index].copy(
//                                                            imageUri = null,
//                                                            uploadState = AmityFileUploadState.PENDING,
//                                                            uploadProgress = 0,
//                                                            uploadError = null
//                                                        )
//                                                    )
//                                                }
                                            },
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            Text(
                                                text = "Cancel",
                                                fontSize = 17.sp,
                                                lineHeight = 22.sp,
                                                fontWeight = FontWeight(600),
                                                color = AmityTheme.colors.primary,
                                            )
                                        }
                                    }
                                }
                                Spacer(modifier = Modifier.height(30.dp))
                            }
                        }
                    }
                }

                RenderAltTextConfigSheet(
                    pageScope = getPageScope(),
                    onSuccess = { imageWithAltText ->
                        selectedImageIndex?.let { index ->
                            val data = textAndImage[index]
                            viewModel.updateImagePollUiState(
                                index = index,
                                newState = data.copy(image = imageWithAltText)
                            )
                        }
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun AmityPollDurationSelectionBottomSheet(
    modifier: Modifier = Modifier,
    componentScope: AmityComposeComponentScope? = null,
    durationOptions: List<String>,
    selectedIndex: Int,
    shouldShow: Boolean,
    onSelected: (Int) -> Unit,
    onDismiss: () -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    if (shouldShow) {
        ModalBottomSheet(
            onDismissRequest = {
                onDismiss()
            },
            sheetState = sheetState,
            containerColor = AmityTheme.colors.background,
            contentWindowInsets = { WindowInsets.waterfall },
            modifier = modifier.semantics {
                testTagsAsResourceId = true
            }
        ) {
            AmityPollDurationOptionContainer(
                modifier = modifier,
                componentScope = componentScope,
                selectedIndex = selectedIndex,
                durationOptions = durationOptions,
            ) { selectedOption ->
                onSelected(selectedOption)
            }
        }
    }
}

@Composable
fun AmityPollDurationOptionContainer(
    modifier: Modifier = Modifier,
    componentScope: AmityComposeComponentScope? = null,
    durationOptions: List<String>,
    selectedIndex: Int,
    onSelected: (Int) -> Unit,
) {
    AmityBaseElement(
        componentScope = componentScope,
        elementId = "community_profile_actions"
    ) {
        Column(
            modifier = Modifier
                .background(AmityTheme.colors.background)
                .padding(bottom = 40.dp)
        ) {
            durationOptions.forEachIndexed { index, option ->
                AmityPollDurationOptionItem(
                    text = option,
                    isSelected = index == selectedIndex,
                    onSelected = { onSelected(index) },
                )
            }

            AmityPollDurationOptionItem(
                text = "Custom end date",
                isSelected = (-1 == selectedIndex),
                onSelected = { onSelected(-1) },
            )

            Spacer(modifier = Modifier.height(40.dp))


        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AmityPollDurationOptionItem(
    modifier: Modifier = Modifier,
    text: String = "",
    isSelected: Boolean,
    onSelected: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .height(56.dp)
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp)
            .clickableWithoutRipple { onSelected() },
    ) {
        Text(
            text = text,
            style = AmityTheme.typography.bodyLegacy.copy(
                fontWeight = FontWeight.SemiBold,
            ),
            modifier = modifier
                .weight(1f)
                .testTag(text)
        )
        CompositionLocalProvider(LocalMinimumInteractiveComponentSize provides 0.dp) {
            RadioButton(
                modifier = Modifier.testTag(text),
                selected = isSelected,
                colors = RadioButtonDefaults.colors(
                    selectedColor = AmityTheme.colors.highlight,
                    unselectedColor = AmityTheme.colors.baseShade2,
                ),
                onClick = onSelected,
            )
        }
    }
}



