package org.bankalert;


import java.io.Serializable;

public class Transaction implements Serializable {

  String txnID;
  String customerID;
  String countryCode;
  String ipAddress;
  String isp;
  String txnType;
  String beneficiaryAcctNum;
  String timestamp;

  public Transaction(String txnID, String customerID, String countryCode, String ipAddress, String isp, String
      txnType, String beneficiaryAcctNum, String timestamp) {
    this.txnID = txnID;
    this.customerID = customerID;
    this.countryCode = countryCode;
    this.ipAddress = ipAddress;
    this.isp = isp;
    this.txnType = txnType;
    this.beneficiaryAcctNum = beneficiaryAcctNum;
    this.timestamp = timestamp;
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

  public String getTimestamp() {
    return timestamp;
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

  public void setTimestamp(String timestamp) {
    this.timestamp = timestamp;
  }

  @Override
  public String toString() {
    return "Transaction{" +
        "txnID='" + txnID + '\'' +
        ", customerID='" + customerID + '\'' +
        ", countryCode='" + countryCode + '\'' +
        ", ipAddress='" + ipAddress + '\'' +
        ", isp='" + isp + '\'' +
        ", txnType='" + txnType + '\'' +
        ", beneficiaryAcctNum='" + beneficiaryAcctNum + '\'' +
        ", timestamp='" + timestamp + '\'' +
        '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Transaction)) return false;

    Transaction that = (Transaction) o;

    if (!txnID.equals(that.txnID)) return false;
    if (!customerID.equals(that.customerID)) return false;
    if (!countryCode.equals(that.countryCode)) return false;
    if (!ipAddress.equals(that.ipAddress)) return false;
    if (!isp.equals(that.isp)) return false;
    if (!txnType.equals(that.txnType)) return false;
    if (beneficiaryAcctNum != null ? !beneficiaryAcctNum.equals(that.beneficiaryAcctNum) : that.beneficiaryAcctNum
        != null)
      return false;
    return timestamp.equals(that.timestamp);

  }

  @Override
  public int hashCode() {
    int result = txnID.hashCode();
    result = 31 * result + customerID.hashCode();
    result = 31 * result + countryCode.hashCode();
    result = 31 * result + ipAddress.hashCode();
    result = 31 * result + isp.hashCode();
    result = 31 * result + txnType.hashCode();
    result = 31 * result + (beneficiaryAcctNum != null ? beneficiaryAcctNum.hashCode() : 0);
    result = 31 * result + timestamp.hashCode();
    return result;
  }
}
