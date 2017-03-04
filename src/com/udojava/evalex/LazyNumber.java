/*
    Created By : uklimaschewski
    Modified By : iamsubhranil
    Date : 4/3/17
    Time : 10:53 PM
    Package : com.udojava.evalex
*/
package com.udojava.evalex;

import java.math.BigDecimal;

/**
 * LazyNumber interface created for lazily evaluated functions
 */
interface LazyNumber {
    BigDecimal eval();
}
