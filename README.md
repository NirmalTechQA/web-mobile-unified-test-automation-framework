# web-mobile-unified-test-automation-framework

## Overview

This repository provides a unified automation framework for testing both web and mobile applications. It leverages Selenium for web automation and Appium for mobile automation, supporting Android, iOS, and cross-browser web testing. The framework is designed for scalability, maintainability, and ease of integration with CI/CD pipelines.

## Features

- **Unified Test Automation:** Write and execute tests for web and mobile platforms using a single codebase.
- **BDD Support:** Cucumber integration for behavior-driven development with feature files.
- **Parallel Execution:** Supports parallel test execution using TestNG.
- **Cross-Platform:** Automate tests on Android, iOS, and major web browsers.
- **Cloud Integration:** Ready for LambdaTest cloud execution for both web and mobile.
- **Reporting:** Generates unified automation reports using ExtentReports.
- **Reusable Wrappers:** Includes wrapper classes for common actions (native, web, Android, iOS).

## Project Structure

```
src/
  main/
    java/
      pages/                # Page Object Model classes for web and mobile
      utils/                # Wrapper utilities for Selenium/Appium
      hooks/                # Cucumber hooks for setup/teardown
  test/
    java/
      testrunner/           # TestNG/Cucumber runners
    resources/
      features/             # Cucumber feature files
      extent.properties     # ExtentReports configuration
      extent-config.xml     # ExtentReports UI config
testng_*.xml                # TestNG suite files for different environments
bitbucket-pipelines.yml     # CI/CD pipeline configuration
pom.xml                     # Maven dependencies and build config
README.md                   # Project documentation
```

## Getting Started

1. **Clone the repository:**
   ```sh
   git clone https://bitbucket.org/your-org/web-mobile-unified-test-automation-framework.git
   cd web-mobile-unified-test-automation-framework
   ```

2. **Install dependencies:**
   ```sh
   mvn clean install
   ```

3. **Configure environment:**
   - Set `LT_USERNAME`, `LT_ACCESS_KEY`, and `LT_GRID_URL` for LambdaTest integration.

4. **Run tests:**
   - **Web:**  
     ```sh
     mvn clean test -Dsurefire.suiteXmlFiles=testng_lambdatest_web.xml -Dcucumber.filter.tags="@login1" -DautomationType="web"
     ```
   - **Android:**  
     ```sh
     mvn clean test -Dsurefire.suiteXmlFiles=testng_lambdatest_android.xml -Dcucumber.filter.tags="@loginandroid1" -DautomationType="mobile"
     ```
   - **iOS:**  
     ```sh
     mvn clean test -Dsurefire.suiteXmlFiles=testng_lambdatest_ios.xml -Dcucumber.filter.tags="@loginiOS1" -DautomationType="mobile"
     ```

## Customization

- **Add new tests:**  
  Create new feature files in `src/test/resources/features` and implement step definitions in `pages/`.
- **Update device/browser configurations:**  
  Modify the respective TestNG XML files or environment variables.

## Reporting

Test execution generates unified reports in the `UnifiedAutomation_Report/ExtentReports` directory. Configuration for ExtentReports is managed via `src/test/resources/extent.properties` and `src/test/resources/extent-config.xml`.

## CI/CD Integration

The framework is ready for Bitbucket Pipelines. See `bitbucket-pipelines.yml` for sample pipeline steps.

## Contributing

Feel free to fork the repository, create pull requests, and open issues for enhancements or bug fixes.

## License

This project is licensed under the MIT License.

---

For more details, refer to the documentation in the `src/main/java/utils` and `src/main/java/pages` directories.