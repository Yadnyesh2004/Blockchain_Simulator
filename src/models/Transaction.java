package models;

import utils.StringUtil;
import java.util.Date;
public class Transaction {

    private String sender;
    private String recipient;
    private String transactionId;
    private double amount;
    private long timeStamp;

    public Transaction(String sender,String recipient,double amount){
        this.sender=sender;
        this.amount=amount;
        this.recipient=recipient;
        this.timeStamp=new Date().getTime();
        this.transactionId=calculateHash();
    }

    private String calculateHash() {
        String data = sender+recipient+Double.toString(amount)+Long.toString(timeStamp);
        return StringUtil.applySha256(data);

    }
    public String getTransactionId() {
        return transactionId;
    }

    public double getAmount() {
        return amount;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public String getSender() {
        return sender;
    }
    public String getRecipient(){
        return recipient;
    }

   public String  toString(){
        return "Transaction{" +
                "id='" + transactionId.substring(0, 10) + "...'" +
                ", " + sender + " -> " + recipient +
                ", amount=" + amount + " BTC" +
                '}';
   }
}
