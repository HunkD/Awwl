package com.hunk.astub.dispatcher.method;

import com.hunk.nobank.contract.ContractGson;
import com.hunk.nobank.contract.RealResp;
import com.hunk.nobank.contract.TransactionCategory;
import com.hunk.nobank.contract.TransactionFields;
import com.hunk.nobank.contract.TransactionType;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import fi.iki.elonen.NanoHTTPD;

/**
 *
 */
public class TransListMethodHandler implements MethodHandler {

    private static final int NEXT_INT_RANGE = 1000;
    SecureRandom random = new SecureRandom();
    @Override
    public String handle(NanoHTTPD.IHTTPSession session) {
        RealResp<List<TransactionFields>> realResp = new RealResp<>();
        realResp.Response = new ArrayList<>();
        realResp.Response.addAll(getData());

        return ContractGson.getInstance().toJson(realResp);
    }

    private List<TransactionFields> getData() {
        long timestamp = new Date().getTime();
        List<TransactionFields> list = new ArrayList<>();
        list.add(new TransactionFields("Move to vault 1", 15.5,
                TransactionType.VAULT, getRandomTimestamp(timestamp), null, "1.PNG"));
        list.add(new TransactionFields("Pay to Fancy 3", 19.5,
                TransactionType.PAY, getRandomTimestamp(timestamp), TransactionCategory.Debit, "Fancy", "3.PNG"));
        list.add(new TransactionFields("Deposit from check 4", 25.5,
                TransactionType.DEPOSIT, getRandomTimestamp(timestamp), TransactionCategory.Credit, "Check", "4.PNG"));
        list.add(new TransactionFields("Deposit from cash 2", 92.3,
                TransactionType.DEPOSIT, getRandomTimestamp(timestamp), TransactionCategory.Credit, "Cash", "2.PNG"));
        list.add(new TransactionFields("Pay to Hunk 3", 119.5,
                TransactionType.PAY, getRandomTimestamp(timestamp), TransactionCategory.Debit, "Fancy", "3.PNG"));
        list.add(new TransactionFields("Move to vault 10", 5.2,
                TransactionType.VAULT, getRandomTimestamp(timestamp), null, "10.PNG"));
        list.add(new TransactionFields("Deposit from check 9", 47.9,
                TransactionType.DEPOSIT, getRandomTimestamp(timestamp), TransactionCategory.Credit, "Check", "9.PNG"));
        list.add(new TransactionFields("Deposit from cash 6", 92.3,
                TransactionType.DEPOSIT, getRandomTimestamp(timestamp), TransactionCategory.Credit, "Cash", "6.PNG"));
        list.add(new TransactionFields("Deposit from cash 8", 92.3,
                TransactionType.DEPOSIT, getRandomTimestamp(timestamp), TransactionCategory.Credit, "Cash", "8.PNG"));
        list.add(new TransactionFields("Move to vault 7", 12.2,
                TransactionType.VAULT, getRandomTimestamp(timestamp), null, "7.PNG"));
        list.add(new TransactionFields("Pay to Fancy 5", 119.5,
                TransactionType.PAY, getRandomTimestamp(timestamp), TransactionCategory.Debit, "Fancy", "5.PNG"));
        return list;
    }

    private long getRandomTimestamp(long timestamp) {
        return timestamp + random.nextInt(NEXT_INT_RANGE);
    }
}
