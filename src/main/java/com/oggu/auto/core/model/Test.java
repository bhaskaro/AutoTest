/**
 * 
 */
package com.oggu.auto.core.model;

import java.util.Map;

/**
 * @author bhaskaro
 *
 */
public class Test {

	private String name;
	private String dir;
	private String usecaseName;
	private String sessionName;
	private int threads;
	private int iterations;
	private int duration;
	private Map<String, String> scnProps;
	private Map<String, String> scriptProps;
	private double baselineTps;
	private int baselineFlrs;
	private double baselineFlrPerc;
	private String defaultOatsSrvr;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDir() {
		return dir;
	}

	public void setDir(String dir) {
		this.dir = dir;
	}

	public String getUsecaseName() {
		return usecaseName;
	}

	public void setUsecaseName(String usecaseName) {
		this.usecaseName = usecaseName;
	}

	public String getSessionName() {
		return sessionName;
	}

	public void setSessionName(String sessionName) {
		this.sessionName = sessionName;
	}

	public int getThreads() {
		return threads;
	}

	public void setThreads(int threads) {
		this.threads = threads;
	}

	public int getIterations() {
		return iterations;
	}

	public void setIterations(int iterations) {
		this.iterations = iterations;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public Map<String, String> getScnProps() {
		return scnProps;
	}

	public void setScnProps(Map<String, String> scnProps) {
		this.scnProps = scnProps;
	}

	public Map<String, String> getScriptProps() {
		return scriptProps;
	}

	public void setScriptProps(Map<String, String> scriptProps) {
		this.scriptProps = scriptProps;
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

	public double getBaselineFlrPerc() {
		return baselineFlrPerc;
	}

	public void setBaselineFlrPerc(double baselineFlrPerc) {
		this.baselineFlrPerc = baselineFlrPerc;
	}

	public String getDefaultOatsSrvr() {
		return defaultOatsSrvr;
	}

	public void setDefaultOatsSrvr(String defaultOatsSrvr) {
		this.defaultOatsSrvr = defaultOatsSrvr;
	}

	@Override
	public String toString() {
		return "Test [name=" + name + ", dir=" + dir + ", usecaseName=" + usecaseName + ", sessionName=" + sessionName
				+ ", threads=" + threads + ", iterations=" + iterations + ", duration=" + duration + ", scnProps="
				+ scnProps + ", scriptProps=" + scriptProps + ", baselineTps=" + baselineTps + ", baselineFlrs="
				+ baselineFlrs + ", baselineFlrPerc=" + baselineFlrPerc + ", defaultOatsSrvr=" + defaultOatsSrvr + "]";
	}

}
