package com.openfaas.function;

import com.openfaas.model.IHandler;
import com.openfaas.model.IResponse;
import com.openfaas.model.IRequest;
import com.openfaas.model.Response;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;

import java.util.Random;


public class Handler extends com.openfaas.model.AbstractHandler {
    HazelcastInstance client;

    public Handler(){
        super();
        ClientConfig config = new ClientConfig();
        config.getConnectionStrategyConfig().getConnectionRetryConfig().setClusterConnectTimeoutMillis(Long.MAX_VALUE); 
        config.getNetworkConfig().addAddress("hz-hazelcast.default");
        this.client = HazelcastClient.newHazelcastClient(config);

        System.out.println("Connection Successful!");
        System.out.println("Now the map named 'map' will be filled with random entries.");
    }

    public IResponse Handle(IRequest req) {
        Response res = new Response();
        IMap<String, String> map = this.client.getMap("map");
        Random random = new Random();
        int randomKey = random.nextInt(100_000);
        map.put("key-" + randomKey, "value-" + randomKey);
	    res.setBody(String.valueOf(map.size()));
	    return res;
    }
}
