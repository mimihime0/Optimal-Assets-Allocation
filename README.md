# Optimal Asset Allocation (Brute Force)

## Description

This project provides a Java program to determine an optimal allocation of assets within an investment portfolio based on expected return and risk levels. Given a list of assets with their individual expected returns, risk levels, and maximum available quantities, along with a total investment amount and a maximum risk tolerance for the overall portfolio, this program aims to find the specific allocation (number of units of each asset) that maximizes the total expected portfolio return without exceeding the specified risk tolerance.

This implementation uses a **brute-force recursive approach** to explore all valid combinations of asset quantities that sum up to the total investment amount.

*Disclaimer: This project is designed for educational purposes to demonstrate a brute-force optimization approach and may not be suitable for real-world financial decisions.*

## Features

* Determines optimal asset allocation based on maximizing return within a risk tolerance.
* Reads asset data (ID, expected return, risk, max quantity), total investment, and risk tolerance from a configuration file (`assets.txt`).
* Handles a variable number of assets defined in the input file.
* Uses a brute-force recursive algorithm to evaluate potential allocations.
* Prints the optimal allocation (units per asset), the resulting expected portfolio return, and the portfolio risk level.
* Includes basic input validation and error handling for file reading.

## Requirements

* Java Development Kit (JDK) 8 or higher (for compiling and running).
* No external libraries are required beyond the standard Java SE platform.

## Input File Configuration (`assets.txt`)

The program reads its configuration from a text file named `assets.txt` (by default) located in the same directory. The file must follow this format:

1.  **Asset Lines:** Each line defining an asset should be formatted as:
    `AssetID : ExpectedReturn : RiskLevel : MaxQuantity`
    * `AssetID`: A unique identifier for the asset (e.g., `AAPL`, `GOOGL`, `ASSET1`).
    * `ExpectedReturn`: The expected return for this asset (e.g., `0.08` for 8%).
    * `RiskLevel`: The risk level associated with this asset (e.g., `0.03`).
    * `MaxQuantity`: The maximum number of units of this asset available or considered for investment (e.g., `500`).
    * Whitespace around the colons (`:`) is ignored.

2.  **Total Investment Line:** One line must specify the total investment amount:
    `Total investment is <Amount> units`
    * Replace `<Amount>` with the total integer number of units to be invested (e.g., `1000`).

3.  **Risk Tolerance Line:** One line must specify the maximum acceptable portfolio risk:
    `Risk tolerance level is <Tolerance>`
    * Replace `<Tolerance>` with the maximum weighted average risk level allowed for the portfolio (e.g., `0.024`).

**Example `assets.txt`:**

```
AAPL : 0.05 : 0.02 : 1000
GOOGL : 0.08 : 0.03 : 500
MSFT : 0.04 : 0.015 : 800
Total investment is 1000 units
Risk tolerance level is 0.024
```

*Note: You can modify the ``filePath`` variable inside ``PortfolioOptimizer.java`` if you want to use a different input filename.*

## Usage
1. **Prepare Files:** Ensure you have the following Java source files in a directory:
* Asset.java
* OptimalPortfolio.java
* PortfolioOptimizer.java And the input file (assets.txt or your custom file) in the same directory.
2. **Compile:** Open a terminal or command prompt, navigate to the directory containing the files, and compile all Java source files:
  ```
  javac *.java
  ```
  *(Use a semicolon ; instead of && if using PowerShell)*
3. **Run:** Execute the main class:
  ```
  java PortfolioOptimizer
  ```
  *(If you encounter `ClassNotFoundException`, try: `java -cp . PortfolioOptimizer`)*
4. **Output:** The program will print the optimal allocation found (units per asset), the expected portfolio return, and the portfolio risk level to the console. If no allocation meets the risk tolerance, it will indicate that no feasible solution was found.

## Notes & Limitation
* **Brute-Force Approach:** The core algorithm explores all possible valid combinations. This can become very slow as the number of assets (`N`) or the `totalInvestment` amount increases, due to the exponential nature of the search space. This implementation is not practical for a large number of assets or very large investment sums.
