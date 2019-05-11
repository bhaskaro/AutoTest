package com.oggu.auto.core.model;

public class OatsServer {

	private String name;
	private String host;
	private String homedir;
	private String adminUser;
	private String adminPwd;
	private String vmUser;
	private String vmPwd;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getHomedir() {
		return homedir;
	}

	public void setHomedir(String homedir) {
		this.homedir = homedir;
	}

	public String getAdminUser() {
		return adminUser;
	}

	public void setAdminUser(String adminUser) {
		this.adminUser = adminUser;
	}

	public String getAdminPwd() {
		return adminPwd;
	}

	public void setAdminPwd(String adminPwd) {
		this.adminPwd = adminPwd;
	}

	public String getVmUser() {
		return vmUser;
	}

	public void setVmUser(String vmUser) {
		this.vmUser = vmUser;
	}

	public String getVmPwd() {
		return vmPwd;
	}

	public void setVmPwd(String vmPwd) {
		this.vmPwd = vmPwd;
	}

	@Override
	public String toString() {
		return "OatsServer [name=" + name + ", host=" + host + ", homedir=" + homedir + ", adminUser=" + adminUser
				+ ", adminPwd=" + adminPwd + ", vmUser=" + vmUser + ", vmPwd=" + vmPwd + "]";
	}

}
