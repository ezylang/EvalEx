/*
  Copyright 2012-2026 Udo Klimaschewski

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
*/
package com.ezylang.evalex.functions.basic;

import com.ezylang.evalex.EvaluationException;
import com.ezylang.evalex.Expression;
import com.ezylang.evalex.data.EvaluationValue;
import com.ezylang.evalex.functions.AbstractFunction;
import com.ezylang.evalex.functions.FunctionParameter;
import com.ezylang.evalex.parser.Token;
import java.math.BigDecimal;
import java.math.MathContext;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.util.Locale;

/**
 * A function that converts a string representation of a number into a {@link java.math.BigDecimal},
 * allowing to convert both raw unformatted numbers and heavily-formatted string values (containing
 * localized thousands and decimal separators) into numeric values ready for mathematical execution.
 *
 * <p>To preserve mathematical consistency, any parsed value, whether extracted directly or through
 * a formatting pattern, is strictly aligned with the expression's global {@link
 * java.math.MathContext} rules.
 *
 * <p>This function follows a strict positional argument pattern for its optional parameters. If you
 * wish to skip the {@code format} parameter but specify a custom {@code locale}, you must pass an
 * explicit {@code NULL} as the second argument.
 *
 * <h3>Syntax</h3>
 *
 * {@code NUMBER_PARSE(stringValue [, format [, locale]])}
 *
 * <h3>Function Parameters</h3>
 *
 * <ul>
 *   <li><b>string</b> - The {@link java.lang.String} text containing the digits to convert. Must
 *       not be null.
 *   <li><b>format</b> - <i>(Optional)</i> A custom {@link java.text.DecimalFormat} pattern string
 *       (e.g., {@code "###,##0.00"}). If omitted or set to {@code NULL}, the string is assumed to
 *       be an unformatted decimal string compatible with the default {@link
 *       java.math.BigDecimal#BigDecimal(String)} constructor.
 *   <li><b>locale</b> - <i>(Optional)</i> An IETF BCP 47 language tag string specifying localized
 *       number formatting symbols (e.g., {@code "de-DE"}, {@code "en-US"}, {@code "pt-BR"}).
 *       Defaults to the system's current default locale if omitted or set to {@code NULL}.
 * </ul>
 *
 * <h3>Examples</h3>
 *
 * Assuming the default system configuration, here is how various invocation combinations behave:
 *
 * <pre>
 *   NUMBER_PARSE("123.45")                          -&gt; 123.45 (Standard fallback unformatted string parse)
 *   NUMBER_PARSE("1.234,56", "###,##0.00", "de")    -&gt; 1234.56 (Parses German dot-thousands and comma-decimals)
 *   NUMBER_PARSE("1,234.56", "###,##0.00", "en-US") -&gt; 1234.56 (Parses US comma-thousands and dot-decimals)
 *   NUMBER_PARSE("1234,56", "###,##0.00", "pt-BR")  -&gt; 1234.56 (Parses Brazilian comma-decimals)
 *   NUMBER_PARSE("1.234567", "###.000")             -&gt; 1.235 (Enforces pattern with default locale)
 * </pre>
 *
 * <h3>Advanced Examples</h3>
 *
 * <pre>
 *   // Scientific/Engineering notation support (when format is omitted)
 *   NUMBER_PARSE("1.23E4")                          -&gt; 12300 (Parses standard exponential string formats)
 *
 *   // Accounting/Financial negative number pattern handling
 *   NUMBER_PARSE("(1,250.50)", "###,##0.00;(###,##0.00)", "en-US") -&gt; -1250.50 (Correctly parses parentheses as negative)
 *
 *   // Percentage string parsing with pattern configuration
 *   NUMBER_PARSE("85.5%", "###.0%", "en-US")        -&gt; 0.855 (Parses percentage signs automatically)
 * </pre>
 *
 * @author oswaldo.bapvic.jr
 * @since 3.8.0
 */
@FunctionParameter(name = "string")
@FunctionParameter(name = "optionalParams", isVarArg = true) // format and locale
public class NumberParseFunction extends AbstractFunction {

  @Override
  public EvaluationValue evaluate(
      Expression expression, Token functionToken, EvaluationValue... parameterValues)
      throws EvaluationException {

    EvaluationValue string = parameterValues[0];
    if (string.isNullValue()) {
      throw new EvaluationException(functionToken, "The input string cannot be null");
    }

    String sanitized = string.getStringValue().trim();

    // Retrieve the official MathContext from the EvalEx expression configuration
    MathContext mathContext = expression.getConfiguration().getMathContext();

    // Resolve the Locale parameter (defaulting to expression configuration if skipped or null)
    Locale locale = determineLocale(expression, parameterValues);

    try {
      BigDecimal parsedNumber;

      // Branching execution based on whether a formatting pattern was passed
      if (parameterValues.length > 1 && !parameterValues[1].isNullValue()) {
        String pattern = parameterValues[1].getStringValue().trim();
        DecimalFormat decimalFormat = getDecimalFormat(locale, pattern);
        BigDecimal rawNumber = (BigDecimal) decimalFormat.parse(sanitized);

        // Align the newly parsed number with the global MathContext rules
        parsedNumber = rawNumber.round(mathContext);
      } else {
        // Direct fallback: parse raw unformatted text utilizing the MathContext on creation
        parsedNumber = new BigDecimal(sanitized, mathContext);
      }

      return EvaluationValue.numberValue(parsedNumber);

    } catch (ParseException e) {
      throw new EvaluationException(
          functionToken,
          String.format("Value '%s' does not match the specified format pattern.", sanitized));
    } catch (NumberFormatException e) {
      throw new EvaluationException(
          functionToken,
          String.format("Value '%s' cannot be safely parsed into a valid number.", sanitized));
    }
  }

  private Locale determineLocale(Expression expression, EvaluationValue... parameterValues) {
    if (parameterValues.length > 2 && !parameterValues[2].isNullValue()) {
      // A specific locale was passed
      String localeString = parameterValues[2].getStringValue().trim();
      return Locale.forLanguageTag(localeString); // e.g.: "en-US", "pt-BR", "de"
    }
    return expression.getConfiguration().getLocale();
  }

  private DecimalFormat getDecimalFormat(Locale locale, String pattern) {
    DecimalFormatSymbols symbols = DecimalFormatSymbols.getInstance(locale);
    DecimalFormat decimalFormat = new DecimalFormat(pattern, symbols);
    // Direct Java to produce a raw BigDecimal instance from the text format
    decimalFormat.setParseBigDecimal(true);
    return decimalFormat;
  }

  @Override
  public void validatePreEvaluation(Token token, EvaluationValue... parameterValues)
      throws EvaluationException {
    super.validatePreEvaluation(token, parameterValues);
    if (parameterValues.length > 3) {
      throw new EvaluationException(token, "Too many parameters");
    }
  }
}
