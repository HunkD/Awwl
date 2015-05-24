package com.hunk.nobank.contract;

import java.util.List;

public class LoginResp {

    public boolean NeedSecurityQuestionCheck;
    public List<String> AllAccountIds;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LoginResp loginResp = (LoginResp) o;

        if (NeedSecurityQuestionCheck != loginResp.NeedSecurityQuestionCheck) return false;
        return !(AllAccountIds != null ? !AllAccountIds.equals(loginResp.AllAccountIds) : loginResp.AllAccountIds != null);

    }

    @Override
    public int hashCode() {
        int result = (NeedSecurityQuestionCheck ? 1 : 0);
        result = 31 * result + (AllAccountIds != null ? AllAccountIds.hashCode() : 0);
        return result;
    }
}
