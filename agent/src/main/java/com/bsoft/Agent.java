package com.bsoft;

import java.lang.instrument.Instrumentation;

/**
 * @author: 何胜豪
 * @Title: TODO
 * @Package: com.bsoft
 * @Description:
 * @date : 2021/12/2 15:34
 */
public class Agent {



    public static void premain(String agentArgs, Instrumentation inst){
        inst.addTransformer(new Transformer());
    }
}
