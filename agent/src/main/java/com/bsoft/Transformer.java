package com.bsoft;


import javassist.ClassPool;
import javassist.LoaderClassPath;

import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;


/**
 * @author: 何胜豪
 * @Title: TODO
 * @Package: com.bsoft
 * @Description:
 * @date : 2021/12/2 15:37
 */
public class Transformer implements ClassFileTransformer {

    private static final String RESOURCE_LOCATE = "ctd/mvc/resource/service/ResourceLocateService";

    private static final String SERVER_NODE = "ctd/net/rpc/logger/pm/ServerNode";

    private static final String CONTROLLER = "ctd/mvc/controller/support/JSONRequester";

//    private static final String CLASS_NAME_INTERNAL = CLASS_NAME.replace('.', '/');

    private static ClassPool cc = ClassPool.getDefault();


    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {

        cc.appendClassPath(new LoaderClassPath(loader));
        if(RESOURCE_LOCATE.equals(className)){

        }
        else if (SERVER_NODE.equals(className)){

//            ServerNodeProxy asmProxyAction = new ServerNodeProxy();
//            byte[] cc = asmProxyAction.aop(classfileBuffer);

//            try {
//                FileOutputStream fos = new FileOutputStream("D:\\plugin\\hihis-zooler\\agent\\src\\main\\java\\com\\bsoft\\node.class");
//                fos.write(cc);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }

            return null;
        }   else if (CONTROLLER.equals(className)){

            ControllerProxy controllerProxy = new ControllerProxy();
            byte[] cc = controllerProxy.aop(classfileBuffer);

//            try {
//                FileOutputStream fos = new FileOutputStream("D:\\plugin\\hihis-zooler\\agent\\src\\main\\java\\com\\bsoft\\cc.class");
//                fos.write(cc);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }

            return cc;
        }

        return null;
    }


}
