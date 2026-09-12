# EvalEx agent guide

## Purpose and scope

This file is the primary repository instruction file for AI coding agents. It applies to the
entire repository unless a more specific `AGENTS.md` exists below the directory being changed.
Tool-specific instruction files, such as `CLAUDE.md`, should reference this file instead of
duplicating it.

Use this guide together with the issue, `README.md`, `SECURITY.md`, the documentation under
`docs/`, the Maven configuration, and relevant tests. Do not make this file the only source of a
rule that human contributors also need to follow. When a durable project practice changes, update
the relevant human-facing documentation and this guide together.

EvalEx is a compact Java library for parsing and evaluating expressions. It supports numbers,
booleans, strings, date and time values, durations, arrays, structures, `NULL`, variables, custom
functions and operators, implicit multiplication, and lazy function parameters. Numerical
calculations use `BigDecimal`. The minimum supported Java version is 11, and the library has no
runtime dependencies.

## Human responsibility and AI transparency

- AI assistance does not reduce the contributor's responsibility. A human contributor must
  understand, review, and take responsibility for every submitted change.
- Do not publish, merge, approve, release, or push changes unless a maintainer explicitly asks for
  that exact action. Prepare local changes for human review by default.
- Do not sign commits, add a `Signed-off-by` or `Co-authored-by` trailer for an AI tool, or claim
  that generated output is original or free of third-party material.
- Disclose material AI assistance in the pull request description. Name the tool and model when
  known, and summarize what it produced or reviewed. The human contributor remains the author and
  decision maker.
- Never submit output that the contributor cannot explain. Treat AI-generated code, tests,
  documentation, citations, and analysis as untrusted until checked against the repository and
  authoritative sources.
- Do not send source code, issue details, logs, credentials, personal data, embargoed
  vulnerabilities, or other non-public repository information to an external service unless the
  contributor has confirmed that the service and its terms are approved for that data.

## Repository layout

- `src/main/java/com/ezylang/evalex/`: public entry points and implementation.
- `src/test/java/com/ezylang/evalex/`: JUnit 5 tests, generally mirroring production packages.
- `docs/`: user documentation published through GitHub Pages.
- `pom.xml`: Java level, dependencies, build plugins, formatting, coverage, and publishing setup.
- `spotless/header.txt`: required license header template for Java files.
- `.github/workflows/build.yml`: required CI build and consumer POM checks.
- `.github/workflows/publish.yml`: maintainer-only release workflow.
- `SECURITY.md`: vulnerability reporting process and security scope.

Do not edit generated content under `target/` or `.flattened-pom.xml`. Regenerate it through Maven
when it is needed for verification.

## Working method

1. Read the complete issue or task and inspect `git status` before editing.
2. Read the affected production code, its tests, and relevant documentation. Search for existing
   abstractions and conventions before adding new ones.
3. Keep the change limited to the requested behavior. Avoid speculative refactoring, unrelated
   cleanup, broad reformatting, or generated churn.
4. Preserve all pre-existing changes. Do not overwrite, revert, stage, or commit work that is not
   part of the task.
5. For a bug fix, first add or identify a focused regression test and observe the failure when
   practical. Then implement the smallest complete fix.
6. Run focused checks while developing and the complete required checks before handing off.
7. Review the final diff for correctness, unintended API changes, missing tests, documentation,
   security, licensing, and unrelated changes.
8. Report changed files, verification performed, and any check that could not be run. Never imply
   that a check passed when it was not run successfully.

Ask before making a destructive change, installing system software, changing repository or
account settings, or performing a remote write. Do not create commits or branches unless the task
requests them. If the task conflicts with this guide or lacks a decision that would materially
change the public contract, stop and ask the maintainer.

## Build and verification

### Prerequisites

- Use JDK 11 compatibility. A newer JDK may run Maven, but production code must compile with
  `--release 11` and must not use APIs introduced after Java 11.
- Use Maven from the repository root. This repository currently has no Maven wrapper.

### Commands

- Focused test class: `mvn --batch-mode -Dtest=ClassName test`
- Focused test method: `mvn --batch-mode -Dtest=ClassName#methodName test`
- Apply Java formatting and license headers: `mvn --batch-mode spotless:apply`
- Check formatting: `mvn --batch-mode spotless:check`
- Complete clean verification: `mvn --batch-mode clean verify`

Run focused tests first for fast feedback. Before review, run `mvn --batch-mode clean verify`.
Spotless runs during Maven validation, and JaCoCo checks coverage during the build. The current
JaCoCo gate requires 100 percent instruction coverage and no uncovered production class. Do not
lower, exclude, suppress, or bypass a quality gate to make a change pass.

When `pom.xml` affects the published POM, also perform the consumer POM checks from
`.github/workflows/build.yml`. Do not run `deploy`, create tags or releases, use the `publish`
profile, or handle signing credentials unless a maintainer explicitly requests a release action.

If a full check fails because of the task, fix the cause. If it fails for an unrelated or
environmental reason, record the exact command and failure in the handoff.

## Design and compatibility

- Prefer correct, clear, maintainable code over clever code.
- Preserve source and behavioral compatibility for public classes, interfaces, methods,
  constructors, constants, configuration defaults, expression syntax, evaluation behavior, and
  exception behavior unless the issue explicitly approves a breaking change.
- Treat everything exposed from production packages as public API unless clearly documented
  otherwise. Consider custom function, operator, converter, dictionary, and data-access
  implementations as extension points.
- Keep parser and evaluator changes deliberate. Check precedence, associativity, unary and binary
  ambiguity, implicit multiplication, lazy evaluation, `NULL` propagation, conversions, and error
  positions where relevant.
- Preserve `BigDecimal`, `MathContext`, scale, and rounding semantics. Do not introduce binary
  floating-point behavior into numerical evaluation without an explicit design decision.
- Preserve thread-safety expectations. Tests and documentation state that each thread should use
  its own expression copy; do not silently broaden or weaken that contract.
- Prefer existing configuration and extension mechanisms over special cases.
- Consider pathological expression size, nesting, recursion, regular expressions, allocation, and
  execution time. Add a focused performance or limit test when a change creates a credible
  resource-consumption risk.
- Do not add a runtime dependency. A new provided, test, plugin, or build dependency still requires
  a clear need, a compatible license, and maintainer approval.

## Java conventions

- Follow Google Java Style and let Spotless apply it. Do not manually fight the formatter.
- Use clear names, small focused methods, and the style of adjacent code.
- Prefer immutable state and explicit behavior where practical.
- Avoid wildcard imports in new or changed code. Do not rewrite unrelated existing imports only to
  enforce this rule.
- Use comments for intent, constraints, and non-obvious decisions. Do not narrate the code.
- Keep warnings and static-analysis findings actionable. Use a narrow suppression only when the
  reason is documented next to it.
- Add the project license header to every new Java source file. Use Spotless to insert or update it.

## Tests

- Add or update tests for every behavior change and bug fix. Test public behavior and boundary
  cases, not private implementation details.
- Use JUnit 5 and AssertJ consistently with nearby tests. Use parameterized tests when several
  inputs exercise the same behavior.
- Keep tests deterministic and independent of network access, local time, default locale, default
  time zone, execution order, and developer-specific paths. If time, locale, or zone behavior is
  the subject of a test, control it explicitly.
- Cover success, failure, `NULL`, type conversion, and numeric precision cases that are relevant to
  the change.
- Assert meaningful results and exception details. Do not weaken assertions, delete tests, catch
  unexpected failures, or add sleeps merely to obtain a passing build.
- Maintain the coverage gate for production code. Do not add trivial tests whose only purpose is to
  execute lines without verifying behavior.

## Security and privacy

- Read `SECURITY.md` before investigating or changing security-sensitive behavior.
- EvalEx is an expression evaluator, not a security sandbox. Do not describe it as safe for
  arbitrary untrusted input.
- Treat denial of service through recursion, deep nesting, large inputs, expensive operations, or
  regular expressions as a security concern. Preserve and test resource limits and timeouts.
- Do not add file, process, environment, reflection, class-loading, or network access to expression
  evaluation without an explicit, reviewed security design.
- Never commit secrets or print them in commands, logs, tests, fixtures, or handoff text.
- Do not open a public issue for a suspected vulnerability. Follow the private reporting process in
  `SECURITY.md` and avoid exposing proof-of-concept details in public artifacts.

## Documentation and user-facing text

- Update `docs/` and examples in the same change when public behavior, syntax, configuration,
  compatibility, or limitations change.
- Write direct, concise English that is clear to non-native speakers. Avoid filler, marketing
  language, AI-style phrasing, and repeated explanations.
- Do not use em dashes in repository content, including code comments, tests, documentation,
  commits, issues, and pull requests.
- Keep examples small and runnable. Verify names, links, versions, and code against the repository.
- Do not invent release notes, compatibility claims, benchmarks, citations, or security guarantees.

## Licensing and provenance

- All repository content must be compatible with the Apache License, Version 2.0. Preserve the
  root `LICENSE`, copyright notices, and required attribution.
- Do not copy code, tests, documentation, or assets from another project merely because an AI tool
  suggested them. Record the source and license of any third-party material and obtain maintainer
  approval before adding it.
- Check generated output for suspicious similarity, embedded notices, fabricated references, and
  incompatible terms. When provenance is uncertain, do not submit the material.

## Issues, branches, and commits

- Work on a feature branch, never directly on the default branch. When the work has an issue,
  derive the branch name from its number and title:
  `<issue-number>-<short-kebab-case-title>`. Otherwise, use a short kebab-case description.
- Replace unsuitable branch-name characters with hyphens, collapse repeated hyphens, and remove
  leading or trailing hyphens.
- Keep one issue per branch and move unrelated changes to a separate issue and branch.
- When an issue or pull request ID exists, prefix commit subjects with it and a concise imperative
  summary. For example: `Issue-123: Add fixed-format lexer` or
  `PR-123: Address review feedback`. Otherwise, use a concise imperative subject.
- Do not rewrite shared history, force-push, squash distinct concerns together, or commit generated
  build output. Squash fixup and incidental commits before review only when it is safe and the
  maintainer requests it.

## Pull requests

- When an issue exists, prefix the title with its ID, for example
  `Issue-123: Add fixed-format lexer`, and link it in the description. Otherwise, use a concise
  imperative title.
- Keep the pull request focused and reviewable. State what changed, important design decisions,
  verification performed, compatibility impact, AI assistance, and known follow-up work.
- Put motivation and broader discussion in the issue. Do not hide failures or unresolved risks.
- Do not request review until required checks pass. An AI agent must not approve or merge its own
  output.

## Review priorities

Review changes in this order:

1. Correctness and the issue's acceptance criteria.
2. Public API, expression-language, and behavioral compatibility.
3. Test completeness and the configured coverage gate.
4. Security, privacy, and resource-consumption risks.
5. Maintainability, performance, and dependency impact.
6. Documentation, licensing, provenance, and repository hygiene.

## Definition of done

A change is complete only when:

- Its behavior and acceptance criteria are satisfied with no unrelated changes.
- Relevant tests are added or updated and pass.
- `mvn --batch-mode clean verify` passes, or an exact external blocker is documented.
- Production-code coverage still satisfies the configured JaCoCo gate.
- Public compatibility, performance, and security implications have been considered.
- User-facing behavior and limitations are documented.
- The final diff has been reviewed for generated artifacts, secrets, licenses, and third-party
  material.
- The branch, commits, pull request, and AI disclosure follow this guide when those artifacts are
  part of the task.

## Maintaining this guide

- Keep these rules concrete, current, testable, and consistent with the repository.
- Prefer links to detailed human-facing documentation over duplicating long instructions.
- Add a nested `AGENTS.md` only when a subtree needs materially different commands or conventions.
- Keep `CLAUDE.md` and other tool-specific files as references to this guide so agents receive the
  same policy.
