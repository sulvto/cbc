package sysdep;

/**
 * Created by sulvto on 16-12-4.
 */
public class CodeGeneratorOptions {
    protected int optimizeLevel;
    protected boolean generatePIC;
    protected boolean generatePIE;
    protected boolean verboseAsm;

    public CodeGeneratorOptions() {
        optimizeLevel = 0;
        generatePIC = false;
        generatePIE = false;
        verboseAsm = false;
    }

    public void setOptimizeLevel(int optimizeLevel) {
        this.optimizeLevel = optimizeLevel;
    }

    public int optimizeLevel() {
        return optimizeLevel;
    }

    public boolean isGeneratePIC() {
        return generatePIC;
    }

    public boolean isGeneratePIE() {
        return generatePIE;
    }

    public void generateVerboseAsm() {
        verboseAsm = true;
    }

    public boolean isVerboseAsm() {
        return verboseAsm;
    }

    public boolean isPositionIndependent() {
        return generatePIC || generatePIE;
    }

    public void generatePIC() {
        this.generatePIC = true;
    }

    public boolean isPICRequired() {
        return generatePIC;
    }

    public void generatePIE() {
        this.generatePIE = true;
    }

    public boolean isPIERequired() {
        return generatePIE;
    }

}
