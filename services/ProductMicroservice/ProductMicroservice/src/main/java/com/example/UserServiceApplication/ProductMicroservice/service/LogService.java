package com.example.UserServiceApplication.ProductMicroservice.service;


public interface LogService {

    void sendLog(String serviceName, String logLevel, String message);
}
