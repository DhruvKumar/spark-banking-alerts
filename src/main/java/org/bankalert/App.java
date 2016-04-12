package org.bankalert;

import org.apache.spark.SparkConf;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.StatelessKieSession;

public class App {

  public static void main(String[] args) {

    KieServices kieServices = KieServices.Factory.get();
    KieContainer kContainer = kieServices.getKieClasspathContainer();
    StatelessKieSession kSession = kContainer.newStatelessKieSession();

    Transaction txn = new Transaction( "1000", "dkumar", "US", "127.0.0.1", "ATT", "WITHDRAWAL", "",  "2016-04-11");

    // evaluate business rules
    kSession.execute( txn );

    SparkConf conf = new SparkConf().setAppName("Bank Alerts").setMaster("local[*]");
    JavaStreamingContext ssc = new JavaStreamingContext(conf, Durations.seconds(5));

    JavaDStream<String> inFileStream = ssc.textFileStream(args[0]);
    JavaDStream<Transaction> txnStream = inFileStream.map(l -> {
      String[] t = l.split(" ");
      return new Transaction(t[0], t[1], t[2], t[3], t[4], t[5], t[6], t[7]);
    });

    txnStream.map(t -> {
      // look up hbase via phoenix in .drl file
      kSession.execute(t);
      return null; // implement me
    });

  }

}
