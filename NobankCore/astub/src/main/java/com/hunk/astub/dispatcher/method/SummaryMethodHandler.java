package com.hunk.astub.dispatcher.method;

import com.hunk.nobank.contract.AccountModel;
import com.hunk.nobank.contract.AccountSummary;
import com.hunk.nobank.contract.AccountType;
import com.hunk.nobank.contract.ContractGson;
import com.hunk.nobank.contract.Money;
import com.hunk.nobank.contract.RealResp;

import java.util.ArrayList;

import fi.iki.elonen.NanoHTTPD;

/**
 *
 */
public class SummaryMethodHandler implements MethodHandler {

    @Override
    public String handle(NanoHTTPD.IHTTPSession session) {
        RealResp<AccountSummary> realResp = new RealResp<>();
        AccountSummary accountSummary = new AccountSummary();
        accountSummary.Accounts = new ArrayList<>();

        AccountModel mainAccount = new AccountModel();
        mainAccount.Type = AccountType.Main;
        mainAccount.Balance = new Money("300.32");

        AccountModel vaultAccount = new AccountModel();
        vaultAccount.Type = AccountType.Vault;
        vaultAccount.Balance = new Money("20000.66");
        accountSummary.Accounts.add(mainAccount);
        accountSummary.Accounts.add(vaultAccount);

        realResp.Response = accountSummary;
        return ContractGson.getInstance().toJson(realResp);
    }
}
