package org.bankalert;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Transaction implements Serializable {

	private static final long serialVersionUID = 1L;
	String txnID;
	String customerID;
	String countryCode;
	String ipAddress;
	String isp;
	String txnType;
	String beneficiaryAcctNum;
	Date txnDateTime;
	Integer txnAmount;
	Boolean isFraud;
	List<String> matchedUseCase = new ArrayList<String>();

	public Transaction() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Transaction(String txnID, String customerID, String countryCode,
			String ipAddress, String isp, String txnType,
			String beneficiaryAcctNum, Integer txnAmount) {
		this.txnID = txnID;
		this.customerID = customerID;
		this.countryCode = countryCode;
		this.ipAddress = ipAddress;
		this.isp = isp;
		this.txnType = txnType;
		this.beneficiaryAcctNum = beneficiaryAcctNum;
		this.txnAmount = txnAmount;
	}

	public List<String> getMatchedUseCase() {
		return matchedUseCase;
	}

	public void setMatchedUseCase(List<String> matchedUseCase) {
		this.matchedUseCase = matchedUseCase;
	}

	public Boolean getIsFraud() {
		return isFraud;
	}

	public void setIsFraud(Boolean isFraud) {
		this.isFraud = isFraud;
	}

	public String getTxnID() {
		return txnID;
	}

	public String getCustomerID() {
		return customerID;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public String getIsp() {
		return isp;
	}

	public String getTxnType() {
		return txnType;
	}

	public String getBeneficiaryAcctNum() {
		return beneficiaryAcctNum;
	}

	public void setTxnID(String txnID) {
		this.txnID = txnID;
	}

	public void setCustomerID(String customerID) {
		this.customerID = customerID;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public void setIsp(String isp) {
		this.isp = isp;
	}

	public void setTxnType(String txnType) {
		this.txnType = txnType;
	}

	public void setBeneficiaryAcctNum(String beneficiaryAcctNum) {
		this.beneficiaryAcctNum = beneficiaryAcctNum;
	}

	public Date getTxnDateTime() {
		return txnDateTime;
	}

	public void setTxnDateTime(Date txnDateTime) {
		this.txnDateTime = txnDateTime;
	}

	public Integer getTxnAmount() {
		return txnAmount;
	}

	public void setTxnAmount(Integer txnAmount) {
		this.txnAmount = txnAmount;
	}

	@Override
	public String toString() {
		return "Transaction{" + "txnID='" + txnID + '\'' + ", customerID='"
				+ customerID + '\'' + ", countryCode='" + countryCode + '\''
				+ ", ipAddress='" + ipAddress + '\'' + ", isp='" + isp + '\''
				+ ", txnType='" + txnType + '\'' + ", beneficiaryAcctNum='"
				+ beneficiaryAcctNum + '\'' + ", txnDateTime='" + txnDateTime
				+ '\'' + ", txnAmount='" + txnAmount + '\'' + '}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof Transaction))
			return false;

		Transaction that = (Transaction) o;

		if (!txnID.equals(that.txnID))
			return false;
		if (!customerID.equals(that.customerID))
			return false;
		if (!countryCode.equals(that.countryCode))
			return false;
		if (!ipAddress.equals(that.ipAddress))
			return false;
		if (!isp.equals(that.isp))
			return false;
		if (!txnType.equals(that.txnType))
			return false;
		if (beneficiaryAcctNum != null ? !beneficiaryAcctNum
				.equals(that.beneficiaryAcctNum)
				: that.beneficiaryAcctNum != null)
			return false;
		return txnDateTime.equals(that.txnDateTime);

	}

	@Override
	public int hashCode() {
		int result = txnID.hashCode();
		result = 31 * result + customerID.hashCode();
		result = 31 * result + countryCode.hashCode();
		result = 31 * result + ipAddress.hashCode();
		result = 31 * result + isp.hashCode();
		result = 31 * result + txnType.hashCode();
		result = 31
				* result
				+ (beneficiaryAcctNum != null ? beneficiaryAcctNum.hashCode()
						: 0);
		result = 31 * result + txnDateTime.hashCode();
		return result;
	}
}
