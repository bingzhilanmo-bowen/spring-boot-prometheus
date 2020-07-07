package com.lanmo.sbp.rpc;

import com.google.common.collect.Maps;
import com.lanmo.sbp.config.RpcStaticConfig;
import com.lanmo.sbp.utils.OkHttp;
import io.micrometer.core.instrument.Metrics;
import io.micrometer.core.instrument.Timer;
import java.util.concurrent.Callable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Rpc {

    @Autowired
    private RpcStaticConfig rpcStaticConfig;

    private Timer t = Metrics.timer("RPC_REQUEST", "/API/LIST","GET");

    public String getList(){
        Callable<String> call = ()->{
            String obj = OkHttp
                .getMethod(rpcStaticConfig.getPath().get("url") + rpcStaticConfig.getPath().get("list"));
            return obj;
        };

        try {
            t.recordCallable(call);
            return "test timer";
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }


}
