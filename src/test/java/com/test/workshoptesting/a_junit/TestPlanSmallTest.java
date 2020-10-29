package com.test.workshoptesting.a_junit;


import org.junit.platform.runner.JUnitPlatform;
import org.junit.platform.suite.api.IncludeTags;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.runner.RunWith;

import static com.test.workshoptesting.util.Constants.SMALL_TESTS;

@RunWith(JUnitPlatform.class)
@SelectPackages("com.test.workshoptesting")
@IncludeTags({SMALL_TESTS})
class TestPlanSmallTest {
}
