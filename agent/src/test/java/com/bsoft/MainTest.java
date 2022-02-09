package com.bsoft;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.Query;
import java.lang.management.ManagementFactory;
import java.util.Set;

/**
 * Unit test for simple App.
 */
public class MainTest
{
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue()
    {
        //
//            try {
//
//            CtClass ct = cc.getCtClass("ctd.net.rpc.logger.pm.ServerNode");
//
//
//            CtField f = new CtField(CtClass.intType, "port", ct);
//            f.setModifiers(Modifier.PUBLIC);
//
//            String scheme = Context.get("scheme");
//
//            System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+scheme+"<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
//
//
//            ct.addField(f,Context.getPort());


//        private void init() {
//        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
//        Set<ObjectName> objs = null;
//        try {
//            objs = mbs.queryNames(new ObjectName("*:type=Connector,*"), Query.match(Query.attr("protocol"), Query.value("HTTP/*")));
//        } catch (MalformedObjectNameException e) {
//            e.printStackTrace();
//        }
//        ObjectName[] list;
//
//        String port = "";
//        if (!objs.isEmpty()) {
//            list = objs.toArray(new ObjectName[objs.size()]);
//            port = list[0].getKeyProperty("port");
//        }
//        Context.setPort(port);
//    }

        assertTrue( true );
    }
}
