package vo;

public class RecommandVO {

	private String campAddress;
	private String campingName;
	private String campType;
	private String environment;
	private int inputCount;

	public RecommandVO() {
		;
	}

	public String getCampAddress() {
		return campAddress;
	}

	public void setCampAddress(String campAddress) {
		this.campAddress = campAddress;
	}

	public String getCampingName() {
		return campingName;
	}

	public void setCampingName(String campingName) {
		this.campingName = campingName;
	}

	public String getCampType() {
		return campType;
	}

	public void setCampType(String campType) {
		this.campType = campType;
	}

	public String getEnvironment() {
		return environment;
	}

	public void setEnvironment(String environment) {
		this.environment = environment;
	}

	public int getInputCount() {
		return inputCount;
	}

	public void setInputCount(int inputCount) {
		this.inputCount = inputCount;
	}

	@Override
	public String toString() {
		return "RecommandVO [campAddress=" + campAddress + ", campingName=" + campingName + ", campType=" + campType
				+ ", environment=" + environment + ", inputCount=" + inputCount + "]";
	}

}