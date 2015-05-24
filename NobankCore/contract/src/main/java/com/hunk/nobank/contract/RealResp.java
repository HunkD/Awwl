package com.hunk.nobank.contract;

public class RealResp<Resp> {
    public int Code;
    public Resp Response;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RealResp<?> realResp = (RealResp<?>) o;

        if (Code != realResp.Code) return false;
        return !(Response != null ? !Response.equals(realResp.Response) : realResp.Response != null);

    }

    @Override
    public int hashCode() {
        int result = Code;
        result = 31 * result + (Response != null ? Response.hashCode() : 0);
        return result;
    }
}
