
import java.io.*;
import java.util.*;

public class demo {

    public static void main(String[] args) {

        //variables
        String id;
        double expectedReturn, riskLevel, toleranceLevel = 0;
        int quantity, totalInvestment = 0, assetLine = 0;

        //assets list
        LinkedList<Asset> assets = new LinkedList<Asset>();

        //saving the assets text file location
        String path = "assets.txt";

        try {
            //accessing the text file
            FileReader fReader = new FileReader(path);
            BufferedReader bReader = new BufferedReader(fReader);

            String line; 
            while ((line = bReader.readLine()) != null) {
                if (line.startsWith("Total investment is")) totalInvestment = Integer.parseInt(line.split(" ")[3]);
                else if (line.startsWith("Risk tolerance level is")) toleranceLevel = Double.parseDouble(line.split(" ")[4]);
                else {
                    String[] info = line.split("\\s*:\\s*");
                    id = info[0];
                    expectedReturn = Double.parseDouble(info[1].trim());
                    riskLevel = Double.parseDouble(info[2].trim());
                    quantity = Integer.parseInt(info[3].trim());
                    assetLine++;

                    Asset asset = new Asset(id, expectedReturn,riskLevel, quantity);
                    if(!(assets.add(asset))) {
                        System.out.println("Failed to add asset at line: " + assetLine);
                    }
                }

                
            }

            if (totalInvestment > calcAllQuantity(assets))
                System.out.println("can't give you an optimal allocation.");
            else {
                Optimalportfolio result = FindOptimalAllocation(assets, totalInvestment, toleranceLevel);
                System.out.println("Optimal Allocation:");
                for (Asset asset : result.getAsset()) {
                    System.out.println(asset.id + ": " + asset.quantity + " units");
                }

                System.out.println("Expected Portfolio Return: " + String.format("%.3f", result.getEPR()));
                System.out.println("Portfolio Risk Level: " + String.format("%.3f", result.getPRL()));
            }
            // closing the file
            // bReader.close();
        } catch (Exception e) {
            System.out.println("Couldn't access file: " + e.getMessage());
        }

    }// end main

    // calculate risk for each asset
    public static double calcRisk(double risk, double Punit, double total) {
        return (Punit / total) * risk;
    }

    // calculate Expected return for each asset
    public static double calcExReturn(double ExReturn, double Punit, double total) {
        return (Punit / total) * ExReturn;
    }

    // case if the investment may exeed the available quantity
    public static int calcAllQuantity(List<Asset> assets) {
        int numOfAsset = assets.size();
        int size = 0;
        for (int i = 0; i < numOfAsset; i++)
            size += assets.get(i).getQuantity();
        return size;

    }

    // brute force algorithm to find Optimal Allocation
    public static Optimalportfolio FindOptimalAllocation(LinkedList<Asset> assets, int totalInvestment, double risktoleranceLevel) {
        List<Asset> OptimaSolutions = new ArrayList<>();
        double maxExpectedReturn = 0;
        double RiskLevel = Double.MAX_VALUE;

        for (int i = 0; i <= assets.get(0).quantity; i++) {
            for (int j = 0; j <= Math.min(assets.get(1).quantity, totalInvestment - i); j++) {
                int k = totalInvestment - i - j;

                if (k > assets.get(2).quantity) {
                    continue;// move to the next
                }

                double expectedReturn = calcExReturn(assets.get(0).expectedReturn, i, totalInvestment)
                        + calcExReturn(assets.get(1).expectedReturn, j, totalInvestment)
                        + calcExReturn(assets.get(2).expectedReturn, k, totalInvestment);

                double riskLevel = calcRisk(assets.get(0).riskLevel, i, totalInvestment)
                        + calcRisk(assets.get(1).riskLevel, j, totalInvestment)
                        + calcRisk(assets.get(2).riskLevel, k, totalInvestment);

                // Check if the cureent allocation < or = to the risk tolerance level and has
                // higher expected return than the pre
                if (riskLevel <= risktoleranceLevel && expectedReturn > maxExpectedReturn) {
                    maxExpectedReturn = expectedReturn;
                    RiskLevel = riskLevel;
                    OptimaSolutions.clear();
                    OptimaSolutions
                            .add(new Asset(assets.get(0).id, assets.get(0).expectedReturn, assets.get(0).riskLevel, i));
                    OptimaSolutions
                            .add(new Asset(assets.get(1).id, assets.get(1).expectedReturn, assets.get(1).riskLevel, j));
                    OptimaSolutions
                            .add(new Asset(assets.get(2).id, assets.get(2).expectedReturn, assets.get(2).riskLevel, k));
                }
            }
        }
        return new Optimalportfolio(OptimaSolutions, maxExpectedReturn, RiskLevel);
    }
}// end class
