package com.hunk.astub.dispatcher.method;

import com.hunk.nobank.contract.ContractGson;
import com.hunk.nobank.contract.RealResp;
import com.hunk.nobank.contract.TransactionCategory;
import com.hunk.nobank.contract.TransactionFields;
import com.hunk.nobank.contract.TransactionType;

import java.util.ArrayList;
import java.util.List;

import fi.iki.elonen.NanoHTTPD;

/**
 *
 */
public class TransListMethodHandler implements MethodHandler {
    @Override
    public String handle(NanoHTTPD.IHTTPSession session) {
        RealResp<List<TransactionFields>> realResp = new RealResp<>();
        realResp.Response = new ArrayList<>();
        realResp.Response.addAll(getData());

        return ContractGson.getInstance().toJson(realResp);
    }

    private List<TransactionFields> getData() {
        List<TransactionFields> list = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            list.add(new TransactionFields("Move to vault" + i, 15.5, TransactionType.VAULT, 1000));
            list.add(new TransactionFields("Pay to Hunk" + i, 19.5, TransactionType.PAY, 1000));
            list.add(new TransactionFields("Deposit from check" + i, 25.5, TransactionType.DEPOSIT, 1000, TransactionCategory.Credit));
        }
        return list;
    }
}
