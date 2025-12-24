package com.amity.socialcloud.uikit.community.compose.event.setup

import android.content.Context

/**
 * Usage Examples for AmityEventSetupPage
 * 
 * This file demonstrates how to launch the Event Setup Page from different contexts.
 */

// Example 1: Launch Event Setup Page to create a new event
fun Context.launchEventCreation() {
    val intent = AmityEventSetupPageActivity.newIntent(
        context = this,
        mode = AmityEventSetupPageMode.Create()
    )
    startActivity(intent)
}

// Example 2: Launch with startActivityForResult to handle the result
fun androidx.fragment.app.Fragment.launchEventCreationForResult() {
    val intent = AmityEventSetupPageActivity.newIntent(
        context = requireContext(),
        mode = AmityEventSetupPageMode.Create()
    )
    startActivityForResult(intent, REQUEST_CREATE_EVENT)
}

// Example 3: Launch with Activity Result API (recommended for modern Android)
fun androidx.fragment.app.Fragment.launchEventCreationWithResultContract() {
    val launcher = registerForActivityResult(
        androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == android.app.Activity.RESULT_OK) {
            // Event was created successfully
            // Handle the result here
            // For example: refresh the events list, show a success message, etc.
        }
    }
    
    val intent = AmityEventSetupPageActivity.newIntent(
        context = requireContext(),
        mode = AmityEventSetupPageMode.Create()
    )
    launcher.launch(intent)
}

// Example 4: Simple usage from an Activity
fun android.app.Activity.openEventSetupPage() {
    val intent = AmityEventSetupPageActivity.newIntent(this)
    startActivity(intent)
}

// Example 5: Launch from a Composable using Activity context
@androidx.compose.runtime.Composable
fun LaunchEventSetupExample() {
    val context = androidx.compose.ui.platform.LocalContext.current
    
    androidx.compose.material3.Button(
        onClick = {
            val intent = AmityEventSetupPageActivity.newIntent(context)
            context.startActivity(intent)
        }
    ) {
        androidx.compose.material3.Text("Create Event")
    }
}

// Example 6: Launch with Activity Result API from Composable (recommended)
@androidx.compose.runtime.Composable
fun LaunchEventSetupWithResultExample() {
    val context = androidx.compose.ui.platform.LocalContext.current
    
    val launcher = androidx.activity.compose.rememberLauncherForActivityResult(
        contract = androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == android.app.Activity.RESULT_OK) {
            // Event was created successfully
            // Handle the result here
        }
    }
    
    androidx.compose.material3.Button(
        onClick = {
            val intent = AmityEventSetupPageActivity.newIntent(context)
            launcher.launch(intent)
        }
    ) {
        androidx.compose.material3.Text("Create Event")
    }
}

// Constants for request codes (if using startActivityForResult)
const val REQUEST_CREATE_EVENT = 1001
const val REQUEST_EDIT_EVENT = 1002

/**
 * Future usage example when Edit mode is implemented:
 * 
 * fun Context.launchEventEdit(event: AmityEvent) {
 *     val intent = AmityEventSetupPageActivity.newIntent(
 *         context = this,
 *         mode = AmityEventSetupPageMode.Edit(event)
 *     )
 *     startActivity(intent)
 * }
 */
