# Security Policy

Thank you for helping to improve the security of EvalEx.

## Supported Versions

Security fixes are generally provided for the latest released version of EvalEx. Older versions may not receive security updates.

## Reporting a Vulnerability

If you believe you have found a security vulnerability in EvalEx, please use GitHub's **Private Vulnerability Reporting** feature.

When reporting an issue, please include as much information as possible, for example:

- the affected EvalEx version
- a description of the issue
- steps to reproduce
- expected and actual behavior
- a proof of concept, if available

Please do not disclose the vulnerability publicly until it has been reviewed.

## Scope

EvalEx is a library for parsing and evaluating expressions. It is **not intended to be a security sandbox**.

Applications embedding EvalEx are responsible for deciding whether untrusted users may submit expressions and for configuring EvalEx according to their security requirements. This includes, for example:

- enabling only the required functions and operators
- replacing or removing functions that are not appropriate for the application's security model
- validating user input where appropriate
- applying resource limits suitable for the application's environment

EvalEx provides configuration mechanisms, such as configurable function dictionaries, to support these use cases.

Security reports should describe vulnerabilities in EvalEx itself. Issues that arise solely from insecure application configuration or misuse of the library may not be considered vulnerabilities in EvalEx.

## Disclosure Process

Every report is reviewed individually.

If a reported issue is confirmed as a vulnerability in EvalEx, we will work on an appropriate fix and publish a security advisory when appropriate.

If the issue is determined to be expected behavior, an application-level concern, or outside the scope of EvalEx, we will explain our reasoning.

Thank you for helping to make EvalEx better and more secure.