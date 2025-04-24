package com.lorenzorogers.atmosphere.network.request;

import com.lorenzorogers.atmosphere.network.RequestUtils;

import java.util.ArrayList;

public class RequestBuilder {
    private final ArrayList<QueryParameter> params = new ArrayList<>();
    private final String urlBase;

    public RequestBuilder(String base) {
        this.urlBase = base;
    }

    public RequestBuilder addParameter(String key, Object value) {
        params.add(new QueryParameter(key, String.valueOf(value)));
        return this;
    }

    public String build() {
        StringBuilder parameters = new StringBuilder();

        for (QueryParameter param : params) {
            parameters
                    .append(params.indexOf(param) == 0 ? "?" : "&")
                    .append(param.key)
                    .append("=")
                    .append(RequestUtils.urlPrepare(param.value));
        }
        return urlBase + parameters;
    }

    public record QueryParameter(String key, String value) {}
}
