//import java.util.Date;
//
// enum PaymentStatus{
//     INITIATED,
//    SUCCESS,
//     FAILED,
//     TIMEOUT
//}
//
//public class paymentRetry {
//
//    String makePayment(Double amount, String payeeId, String payerId){
//        UUID transactionID = idGen.getTransactionId();
//        Date timestamp = Date.getTimestamp();
//        synchronized{
//            try{
//                Transaction newTransaction = new Transaction(transactionID, amount, payeeId, payerId, PaymentStatus.INITIATED);
//                PaymentStatus status =  PaymentGateway.initiatePayment(newTransaction);
//
//                if(status == PaymentStatus.SUCCESS){
//                    newTransaction.setStatus(PaymentStatus.SUCCESS); //handle exceptions
//                }
//                if(status == PaymentStatus.FAILED){
//                    newTransaction.setStatus(PaymentStatus.FAILED); //handle exceptions
//                }
//
//            }
//            catch(Exception e){
//                // raise exception
//            }
//        }
//    }
//
//    void checkStatus(UUID transactionID){ //agreed
//
//    }
//}
//
//// TRANSACTION
//// transaction_id - UUID
//// amount
//// (timestamp)
//// payee id (upi id)
//// payer id (upi id)
//// status
//
//
