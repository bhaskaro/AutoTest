/**
 * 
 */
package com.oggu.auto.core.model;

/**
 * @author bhaskaro
 *
 */
public class RunTests {

	private String[] testNames;
	private boolean sequential;
	private int testsDuration;
	private String testProperties;
	private double baselineFlrPerc;
	private double baselineTps;
	private int baselineFlrs;

	public String[] getTestNames() {
		return testNames;
	}

	public void setTestNames(String[] testNames) {
		this.testNames = testNames;
	}

	public boolean isSequential() {
		return sequential;
	}

	public void setSequential(boolean sequential) {
		this.sequential = sequential;
	}

	public int getTestsDuration() {
		return testsDuration;
	}

	public void setTestsDuration(int testsDuration) {
		this.testsDuration = testsDuration;
	}

	public String getTestProperties() {
		return testProperties;
	}

	public void setTestProperties(String testProperties) {
		this.testProperties = testProperties;
	}

	public double getBaselineFlrPerc() {
		return baselineFlrPerc;
	}

	public void setBaselineFlrPerc(double baselineFlrPerc) {
		this.baselineFlrPerc = baselineFlrPerc;
	}

	public double getBaselineTps() {
		return baselineTps;
	}

	public void setBaselineTps(double baselineTps) {
		this.baselineTps = baselineTps;
	}

	public int getBaselineFlrs() {
		return baselineFlrs;
	}

	public void setBaselineFlrs(int baselineFlrs) {
		this.baselineFlrs = baselineFlrs;
	}

	@Override
	public String toString() {
		return "RunTests [testNames=" + testNames + ", sequential=" + sequential + ", testsDuration=" + testsDuration
				+ ", testProperties=" + testProperties + ", baselineFlrPerc=" + baselineFlrPerc + ", baselineTps="
				+ baselineTps + ", baselineFlrs=" + baselineFlrs + "]";
	}

}
