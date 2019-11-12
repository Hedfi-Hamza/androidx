/*
 * Copyright 2019 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package androidx.ui.test.android

import android.app.Activity
import androidx.ui.test.ComposeTestCase
import androidx.ui.test.ComposeExecutionControl
import androidx.ui.test.ComposeTestCaseSetup
import androidx.ui.test.ComposeTestRule

class AndroidComposeTestCaseSetup(
    private val testRule: ComposeTestRule,
    private val testCase: ComposeTestCase,
    private val activity: Activity
) : ComposeTestCaseSetup {
    override fun performTestWithEventsControl(block: ComposeExecutionControl.() -> Unit) {
        testRule.runOnUiThread {
            // TODO: Ensure that no composition exists at this stage!
            val runner = AndroidComposeTestCaseRunner(testCase, activity)
            try {
                runner.setupContent()
                block.invoke(runner)
            } finally {
                runner.disposeContent()
            }
        }
    }
}