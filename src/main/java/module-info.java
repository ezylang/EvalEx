module com.ezylang.evalex {
  requires static lombok;

  exports com.ezylang.evalex;
  exports com.ezylang.evalex.config;
  exports com.ezylang.evalex.data;
  exports com.ezylang.evalex.data.conversion;
  exports com.ezylang.evalex.functions;
  exports com.ezylang.evalex.functions.basic;
  exports com.ezylang.evalex.functions.datetime;
  exports com.ezylang.evalex.functions.string;
  exports com.ezylang.evalex.functions.trigonometric;
  exports com.ezylang.evalex.operators;
  exports com.ezylang.evalex.operators.arithmetic;
  exports com.ezylang.evalex.operators.booleans;
  exports com.ezylang.evalex.parser;

  // Tests + Frameworks
  opens com.ezylang.evalex;
  opens com.ezylang.evalex.config;
  opens com.ezylang.evalex.data;
  opens com.ezylang.evalex.data.conversion;
  opens com.ezylang.evalex.functions;
  opens com.ezylang.evalex.functions.basic;
  opens com.ezylang.evalex.functions.datetime;
  opens com.ezylang.evalex.functions.string;
  opens com.ezylang.evalex.functions.trigonometric;
  opens com.ezylang.evalex.operators;
  opens com.ezylang.evalex.operators.arithmetic;
  opens com.ezylang.evalex.operators.booleans;
  opens com.ezylang.evalex.parser;
}
