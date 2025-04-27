import java.util.Objects;
 
public class Asset {

    private String id;
    private double expectedReturn;
    private double riskLevel;
    private int maxQuantity; 
    private int allocatedQuantity; 

    public Asset(String id, double expectedReturn, double riskLevel, int maxQuantity) {
        this.id = id;
        this.expectedReturn = expectedReturn;
        this.riskLevel = riskLevel;
        this.maxQuantity = maxQuantity;
        this.allocatedQuantity = 0; 
    }

    public Asset(Asset original, int allocatedQuantity) {
        this.id = original.id;
        this.expectedReturn = original.expectedReturn;
        this.riskLevel = original.riskLevel;
        this.maxQuantity = original.maxQuantity;
        this.allocatedQuantity = allocatedQuantity;
    }

    
    public String getId() {
        return id;
    }

    public double getExpectedReturn() {
        return expectedReturn;
    }

    public double getRiskLevel() {
        return riskLevel;
    }

    public int getMaxQuantity() {
        return maxQuantity;
    }

     public int getAllocatedQuantity() {
        return allocatedQuantity;
    }

     public void setAllocatedQuantity(int allocatedQuantity) {
        this.allocatedQuantity = allocatedQuantity;
    }

    @Override
    public String toString() {
        return id + ": " + allocatedQuantity + " units";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Asset asset = (Asset) o;
        return Double.compare(asset.expectedReturn, expectedReturn) == 0 &&
               Double.compare(asset.riskLevel, riskLevel) == 0 &&
               maxQuantity == asset.maxQuantity &&
               allocatedQuantity == asset.allocatedQuantity &&
               Objects.equals(id, asset.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, expectedReturn, riskLevel, maxQuantity, allocatedQuantity);
    }
}
