import java.util.List;
import java.util.ArrayList;

public class OptimalPortfolio {
    private List<Asset> optimalAllocation; 
    private double expectedPortfolioReturn;
    private double portfolioRiskLevel;
    private boolean foundFeasibleSolution; 

    public OptimalPortfolio() {
        this.optimalAllocation = new ArrayList<>();
        this.expectedPortfolioReturn = -Double.MAX_VALUE; 
        this.portfolioRiskLevel = Double.MAX_VALUE; 
        this.foundFeasibleSolution = false;
    }

     public OptimalPortfolio(List<Asset> allocation, double expectedReturn, double riskLevel) {
        this.optimalAllocation = allocation;
        this.expectedPortfolioReturn = expectedReturn;
        this.portfolioRiskLevel = riskLevel;
        this.foundFeasibleSolution = true; 
    }


    public List<Asset> getOptimalAllocation() {
        return optimalAllocation;
    }

    public double getExpectedPortfolioReturn() {
        return expectedPortfolioReturn;
    }

    public double getPortfolioRiskLevel() {
        return portfolioRiskLevel;
    }

     public boolean isFoundFeasibleSolution() {
        return foundFeasibleSolution;
    }

 
    public void updatePortfolio(List<Asset> currentAllocation, double currentReturn, double currentRisk) {

        List<Asset> allocationCopy = new ArrayList<>();
        for (Asset asset : currentAllocation) {
            allocationCopy.add(new Asset(asset, asset.getAllocatedQuantity()));
        }

        this.optimalAllocation = allocationCopy;
        this.expectedPortfolioReturn = currentReturn;
        this.portfolioRiskLevel = currentRisk;
        this.foundFeasibleSolution = true; 
    }

    @Override
    public String toString() {
        if (!foundFeasibleSolution) {
            return "No feasible solution found within the specified risk tolerance.";
        }
        StringBuilder sb = new StringBuilder("Optimal Allocation:\n");
        for (Asset asset : optimalAllocation) {
            sb.append("  ").append(asset.toString()).append("\n");
        }
        sb.append("Expected Portfolio Return: ").append(String.format("%.3f", expectedPortfolioReturn)).append("\n");
        sb.append("Portfolio Risk Level: ").append(String.format("%.3f", portfolioRiskLevel));
        return sb.toString();
    }
}
