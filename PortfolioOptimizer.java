import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
 
public class PortfolioOptimizer {

    private static OptimalPortfolio bestPortfolio = new OptimalPortfolio();

    public static void main(String[] args) {
        String filePath = "assets.txt"; 
        List<Asset> availableAssets = new ArrayList<>();
        int totalInvestment = 0;
        double riskTolerance = 0.0;


        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            int lineNum = 0;
            while ((line = reader.readLine()) != null) {
                lineNum++;
                line = line.trim();
                if (line.isEmpty() || line.startsWith("//") || line.startsWith("#")) { 
                    continue;
                }

                if (line.toLowerCase().startsWith("total investment is")) {
                    String[] parts = line.split(" ");
                    totalInvestment = Integer.parseInt(parts[3]);
                } else if (line.toLowerCase().startsWith("risk tolerance level is")) {
                    String[] parts = line.split(" ");
                    riskTolerance = Double.parseDouble(parts[4]);
                } else {

                    String[] info = line.split("\\s*:\\s*");
                    if (info.length == 4) {
                        String id = info[0].trim();
                        double expectedReturn = Double.parseDouble(info[1].trim());
                        double riskLevel = Double.parseDouble(info[2].trim());
                        int quantity = Integer.parseInt(info[3].trim());
                        availableAssets.add(new Asset(id, expectedReturn, riskLevel, quantity));
                    } else {
                        System.err.println("Warning: Skipping malformed line " + lineNum + ": " + line);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading file '" + filePath + "': " + e.getMessage());
            return; 
        } catch (NumberFormatException e) {
            System.err.println("Error parsing number in file '" + filePath + "': " + e.getMessage());
            return; 
        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println("Error parsing line structure in file '" + filePath + "'. Check format.");
            return; 
        }


        if (availableAssets.isEmpty()) {
            System.err.println("No assets loaded from the file.");
            return;
        }
        if (totalInvestment <= 0) {
            System.err.println("Total investment must be positive.");
            return;
        }
         if (riskTolerance < 0) { 
            System.err.println("Risk tolerance cannot be negative.");
            return;
        }


        int totalAvailableQuantity = 0;
        for (Asset asset : availableAssets) {
            totalAvailableQuantity += asset.getMaxQuantity();
        }
        if (totalInvestment > totalAvailableQuantity) {
            System.out.println("Warning: Total investment (" + totalInvestment +
                               ") exceeds the total available quantity of all assets (" +
                               totalAvailableQuantity + "). Finding allocation within available limits.");
        }


        System.out.println("Starting optimization...");
        findOptimalAllocationRecursive(availableAssets, 0, totalInvestment, riskTolerance, new ArrayList<>());
        System.out.println("Optimization finished.");

        System.out.println("\n" + bestPortfolio.toString());

    }

    private static void findOptimalAllocationRecursive(
            List<Asset> assets,
            int assetIndex,
            int remainingInvestment,
            double riskTolerance,
            List<Asset> currentAllocation) {


        if (assetIndex == assets.size()) {

            int allocatedAmount = 0;
             for(Asset a : currentAllocation) {
                 allocatedAmount += a.getAllocatedQuantity();
             }

             if (allocatedAmount == (totalInvestment - remainingInvestment) && allocatedAmount > 0) { 
                 calculateAndEvaluatePortfolio(currentAllocation, riskTolerance, allocatedAmount);
             } else if (totalInvestment == 0 && allocatedAmount == 0) {

                 calculateAndEvaluatePortfolio(currentAllocation, riskTolerance, 0);
             }

            return;
        }


        Asset currentAsset = assets.get(assetIndex);
        for (int quantity = 0; quantity <= Math.min(currentAsset.getMaxQuantity(), remainingInvestment); quantity++) {

             currentAsset.setAllocatedQuantity(quantity);

             if (currentAllocation.size() <= assetIndex) { 
                 currentAllocation.add(currentAsset);
             } else { 
                 currentAllocation.set(assetIndex, currentAsset);
             }

            findOptimalAllocationRecursive(
                    assets,
                    assetIndex + 1,
                    remainingInvestment - quantity,
                    riskTolerance,
                    currentAllocation);
        }
    }

    private static void calculateAndEvaluatePortfolio(List<Asset> allocation, double riskTolerance, int totalAllocatedUnits) {

        if (totalAllocatedUnits <= 0) {

            if (bestPortfolio.getExpectedPortfolioReturn() < 0 && !bestPortfolio.isFoundFeasibleSolution()) {
        
                 bestPortfolio.updatePortfolio(new ArrayList<>(), 0.0, 0.0);
            }
            return;
        }


        double currentPortfolioReturn = 0;
        double currentPortfolioRisk = 0;

        for (Asset asset : allocation) {
             int units = asset.getAllocatedQuantity();
             if (units > 0) {
                double weight = (double) units / totalAllocatedUnits;
                currentPortfolioReturn += weight * asset.getExpectedReturn();
                currentPortfolioRisk += weight * asset.getRiskLevel();
             }
        }


        if (currentPortfolioRisk <= riskTolerance) {
            if (currentPortfolioReturn > bestPortfolio.getExpectedPortfolioReturn() || !bestPortfolio.isFoundFeasibleSolution()) {

                System.out.println("Found new best: Return=" + String.format("%.4f", currentPortfolioReturn) +
                                   ", Risk=" + String.format("%.4f", currentPortfolioRisk) );
                 bestPortfolio.updatePortfolio(allocation, currentPortfolioReturn, currentPortfolioRisk);
            } else if (currentPortfolioReturn == bestPortfolio.getExpectedPortfolioReturn() && currentPortfolioRisk < bestPortfolio.getPortfolioRiskLevel()) {

                System.out.println("Found lower risk for same return: Return=" + String.format("%.4f", currentPortfolioReturn) +
                                   ", Risk=" + String.format("%.4f", currentPortfolioRisk) );
                 bestPortfolio.updatePortfolio(allocation, currentPortfolioReturn, currentPortfolioRisk);
            }
        }
    }
}
