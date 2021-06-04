package com.github.dtcan.weatherapp.api;

public interface ResponseHandler<T> {

    void onResponse(T response);
    void onError(int statusCode, String error);

}
