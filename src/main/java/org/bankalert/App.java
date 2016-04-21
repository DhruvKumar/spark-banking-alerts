package org.bankalert;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.nifi.remote.client.SiteToSiteClient;
import org.apache.nifi.remote.client.SiteToSiteClientConfig;
import org.apache.nifi.spark.NiFiDataPacket;
import org.apache.nifi.spark.NiFiReceiver;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.storage.StorageLevel;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaReceiverInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.StatelessKieSession;

public class App {
  static StatelessKieSession kSession = null;
  static Connection conn = null;
  static Statement stmt = null;
  static Map<String, String> properties = new HashMap<String, String>();

  @SuppressWarnings("resource")
  public static void main(String[] args) {
//    if (args.length == 0) {
//      System.out
//          .println("Please pass properties file path from command line");
//      return;
//    }
//    String configFilePath = args[0];
//    loadProperties(configFilePath);
//    getDBConnection();

//    KieServices kieServices = KieServices.Factory.get();
//    KieContainer kContainer = kieServices.getKieClasspathContainer();
//    kSession = kContainer.newStatelessKieSession();
//
//    // set the lists as a global variable for use case 1
//    kSession.setGlobal("customerIdList", getCustomerIdList());
//    kSession.setGlobal("countryCodesList", getCountryCodesList());

//    String STREAM_DURATION_SECONDS = properties
//        .get("STREAM_DURATION_SECONDS");

    SiteToSiteClientConfig config = new SiteToSiteClient.Builder()
        .url("http://sandbox.hortonworks.com:9090/nifi")
        .portName("SparkData")
        .buildConfig();

    SparkConf conf = new SparkConf().setAppName("Bank Alerts").setMaster(
        "local[*]");

    JavaStreamingContext ssc = new JavaStreamingContext(conf,
        Durations.seconds(1000L));

    // Create a JavaReceiverInputDStream using a NiFi receiver so that we can pull data from
    // specified Port
    JavaReceiverInputDStream packetStream =
        ssc.receiverStream(new NiFiReceiver(config, StorageLevel.MEMORY_ONLY()));

    // Map the data from NiFi to text, ignoring the attributes
//    JavaDStream text = packetStream.map(new Function() {
//      public String call(final NiFiDataPacket dataPacket) throws Exception {
//        return new String(dataPacket.getContent(), StandardCharsets.UTF_8);
//      }
//    });


    JavaDStream text = packetStream.map(new Function() {
      @Override
      public Object call(Object o) throws Exception {
        String st = new String(((NiFiDataPacket)o).getContent(), StandardCharsets.UTF_8);
        System.out.println(st);
        return st;
      }
    });

    text.print();

    try {
      Class.forName("org.apache.phoenix.jdbc.PhoenixDriver");
      System.out.println("Connecting to database...");
      conn = DriverManager.getConnection("jdbc:phoenix:sandbox.hortonworks.com:2181","admin","admin");
      System.out.println("Creating statement...");
      stmt = conn.createStatement();
      String sql;
      sql = "SELECT * FROM TRANSACTIONS";
      ResultSet rs = stmt.executeQuery(sql);
      while(rs.next()){
        //Retrieve by column name
        int txnid  = rs.getInt("txnid");
        int customerid = rs.getInt("customerid");
        String first = rs.getString("countrycode");
        String last = rs.getString("last");

        //Display values
        System.out.print("ID: " + id);
        System.out.print(", Age: " + age);
        System.out.print(", First: " + first);
        System.out.println(", Last: " + last);
      }
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    } catch (SQLException e) {
      e.printStackTrace();
    }


//    String SOURCE_FOLDER_PATH = properties.get("SOURCE_FOLDER_PATH");
//    JavaDStream<String> inFileStream = ssc
//        .textFileStream(SOURCE_FOLDER_PATH);
//
//    JavaDStream<Transaction> txnStream = inFileStream.map(l -> {
//      String[] t = l.split(" ");
//      System.out.println(l);
//      Integer amount = 0;
//      if (t[7] != null && t[7] != "") {
//        amount = Integer.parseInt(t[7]);
//      }
//      Transaction transaction = new Transaction(t[0], t[1], t[2],
//          t[3], t[4], t[5], t[6], amount);
//      // System.out.println(transaction.toString());
//      transaction.setTxnDateTime(new Date());
//      return transaction;
//    });
//
//    // execute drools rules
//    JavaDStream<Transaction> txnStreamRulesExe = txnStream.map(t -> {
//      kSession.execute(t);
//      return t;
//    });
//
//    // Save transactions into database
//    JavaDStream<Transaction> txnStreamSaveDB = txnStreamRulesExe.map(t -> {
//      saveTransaction(t);
//      return t;
//    });
//    txnStreamSaveDB.print();

    ssc.start();
    ssc.awaitTermination();

//    try {
//      if (stmt != null)
//        stmt.close();
//    } catch (SQLException se) {
//    }
//    try {
//      if (conn != null)
//        conn.close();
//    } catch (SQLException se) {
//      se.printStackTrace();
//    }
  }

  /**
   * Save transaction into 'transaction' table
   *
   * @param transaction
   */
  private static void saveTransaction(Transaction transaction) {
    try {
      java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(
          "yyyy-MM-dd HH:mm:ss");
      String transactionDateTimeStr = sdf.format(transaction
          .getTxnDateTime());
      String sql = "INSERT INTO  transaction (t_number, t_date, t_type, t_amount, country_code, ip_address, isp, beneficiary_acc_no, customer_id)"
          + "VALUES ('"
          + transaction.getTxnID()
          + "','"
          + transactionDateTimeStr
          + "','"
          + transaction.getTxnType()
          + "',"
          + transaction.getTxnAmount()
          + ",'"
          + transaction.getCountryCode()
          + "','"
          + transaction.getIpAddress()
          + "','"
          + transaction.getIsp()
          + "','"
          + transaction.getBeneficiaryAcctNum()
          + "','"
          + transaction.getCustomerID() + "')";
      // System.out.println(sql);
      stmt.executeUpdate(sql);

      String matchedUseCasesStr = null;
      if (transaction.getIsFraud() != null && transaction.getIsFraud()) {
        List<String> matchedUseCases = transaction.getMatchedUseCase();
        if (matchedUseCases != null && !matchedUseCases.isEmpty()) {
          matchedUseCasesStr = "";
          for (String s : matchedUseCases) {
            if (matchedUseCasesStr != "") {
              matchedUseCasesStr = matchedUseCasesStr + ", ";
            }
            matchedUseCasesStr = matchedUseCasesStr + s;
          }
        }

        String sqlFraud = "INSERT INTO  transaction_fraud (t_number, t_date, t_type, t_amount, country_code, ip_address, isp, beneficiary_acc_no, customer_id, matched_use_cases)"
            + "VALUES ('"
            + transaction.getTxnID()
            + "','"
            + transactionDateTimeStr
            + "','"
            + transaction.getTxnType()
            + "',"
            + transaction.getTxnAmount()
            + ",'"
            + transaction.getCountryCode()
            + "','"
            + transaction.getIpAddress()
            + "','"
            + transaction.getIsp()
            + "','"
            + transaction.getBeneficiaryAcctNum()
            + "','"
            + transaction.getCustomerID()
            + "','"
            + matchedUseCasesStr + "')";

        stmt.executeUpdate(sqlFraud);
      }
    } catch (SQLException se) {
      se.printStackTrace();
    } catch (Exception e) {
      e.printStackTrace();
    } finally {

    }
  }

  /**
   * Load properties from file system
   *
   *
   */
  private static void loadProperties(String configFilePath) {
    Properties props = new Properties();

    try {
      props.load(new FileInputStream(configFilePath));
      for (String key : props.stringPropertyNames()) {
        String value = props.getProperty(key);
        properties.put(key, value);
      }
    } catch (IOException e) {
      System.out.println("Exception Occurred" + e.getMessage());
    }
  }

  /**
   * Get the database connection to save data into database
   */
  private static void getDBConnection() {
    String DB_URL = properties.get("DB_URL");
    String DB_USERNAME = properties.get("DB_USERNAME");
    String DB_PASSWORD = properties.get("DB_PASSWORD");

    try {
      Class.forName("com.mysql.jdbc.Driver");

      conn = DriverManager
          .getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
      stmt = conn.createStatement();
    } catch (SQLException | ClassNotFoundException e) {
      e.printStackTrace();
    }
  }

  /**
   * high risk customer id list which will come from database or file system
   *
   * @return
   */
  private static List<String> getCustomerIdList() {
    List<String> customerIdList = new ArrayList<String>();
    customerIdList.add("aaa");
    customerIdList.add("bbb");
    customerIdList.add("ccc");
    customerIdList.add("ddd");
    customerIdList.add("eee");
    customerIdList.add("fff");
    customerIdList.add("ggg");
    customerIdList.add("hhh");
    customerIdList.add("iii");
    customerIdList.add("jjj");

    return customerIdList;
  }

  /**
   * high risk country codes list which will come from database or file system
   *
   * @return
   */
  private static List<String> getCountryCodesList() {
    List<String> countryCodesList = new ArrayList<String>();
    countryCodesList.add("AFG");
    countryCodesList.add("CHI");
    countryCodesList.add("ALB");
    countryCodesList.add("BRA");
    countryCodesList.add("KHM");
    countryCodesList.add("CAF");
    countryCodesList.add("EGY");
    countryCodesList.add("FRA");
    countryCodesList.add("RSA");
    countryCodesList.add("GAB");

    return countryCodesList;
  }
}
