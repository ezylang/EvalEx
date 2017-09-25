package com.udojava.evalex;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ TestTokenizer.class, TestRPN.class, TestEval.class,
		TestVariables.class, TestBooleans.class, TestCustoms.class,
		TestNested.class, TestVarArgs.class, TestSciNotation.class,
		TestExposedComponents.class, TestCaseInsensitive.class,
		TestLazyIf.class, TestGetUsedVariables.class, TestVariableCharacters.class })
public class AllTests {
}
