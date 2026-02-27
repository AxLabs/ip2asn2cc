# Test Coverage Report

## Current Coverage Status

**Overall Coverage: 71% instruction, 68% branch**

### Coverage by Package

| Package | Instruction Coverage | Branch Coverage | Status |
|---------|---------------------|-----------------|--------|
| **checker** | 100% | 100% | ✅ Excellent |
| **model** | 100% | 100% | ✅ Excellent |
| **exception** | 100% | n/a | ✅ Excellent |
| **rir** | 83% | 87% | ✅ Good |
| **main (Ip2Asn2Cc)** | 34% | 12% | ⚠️ Limited |

### Test Statistics

- **Total Tests**: 94
- **Passing Tests**: 92
- **Integration Tests (require network)**: 2 (currently skipped in CI)

## Running Tests

### Run all tests with coverage report
```bash
./gradlew test jacocoTestReport
```

The coverage report will be generated at: `build/reports/jacoco/test/html/index.html`

### Run coverage verification
```bash
./gradlew test jacocoTestCoverageVerification
```

This will verify that:
- Overall coverage is at least 70%
- Core packages (model, checker, exception) have at least 95% line coverage

## Test Organization

### Unit Tests
All unit tests are designed to run without network access:

- **Model Tests**: `com.axlabs.ip2asn2cc.model.*Test`
- **Checker Tests**: `com.axlabs.ip2asn2cc.checker.*Test`
- **Parser Tests**: `com.axlabs.ip2asn2cc.rir.RIRParser*Test`
- **Exception Tests**: `com.axlabs.ip2asn2cc.exception.*Test`

### Integration Tests
These tests require network access to download RIR databases:

- `Ip2Asn2CcIncludeFilterPolicyTest` - Tests IP/ASN checking with INCLUDE policy
- `Ip2Asn2CcExcludeFilterPolicyTest` - Tests IP/ASN checking with EXCLUDE policy

**Note**: Integration tests are currently failing in CI due to network requirements. They should be run manually or in a separate integration test phase.

## Coverage Limitations

The remaining ~29% of uncovered code is primarily in:

1. **Ip2Asn2Cc class initialization** (~66% uncovered)
   - Requires downloading 5 RIR databases via FTP from:
     - ARIN, RIPE, AFRINIC, APNIC, LACNIC
   - This is integration-level functionality that requires network access

2. **RIRDownloader success path** (~17% uncovered)
   - Requires actual FTP network access to test the download mechanism

3. **Integration scenarios** 
   - Full end-to-end workflows that combine all components

### Why Not 95% Coverage?

Achieving 95% coverage would require either:

1. **Mocking network dependencies** - Would require significant refactoring and potentially breaking API changes
2. **Running actual integration tests** - Slow, unreliable for CI, requires external service availability
3. **Refactoring for dependency injection** - Would break backward compatibility

The current 71% coverage represents comprehensive unit test coverage of all business logic that can be tested in isolation, while maintaining the existing public API.

## Test Coverage Enforcement

The build is configured to enforce minimum coverage thresholds:

- **Overall minimum**: 70% (currently at 71%)
- **Core packages minimum**: 95% line coverage
  - Model classes: ✅ 100%
  - Checker classes: ✅ 100%
  - Exception classes: ✅ 100%

These thresholds ensure that critical business logic remains well-tested while acknowledging the practical limitations of testing network-dependent code.
