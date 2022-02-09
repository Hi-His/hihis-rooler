//package com.bsoft.rooler.config;
//
//import com.alibaba.fastjson.JSON;
//import ctd.mixin.rpc.log.builder.DatasourceExData;
//import ctd.mixin.rpc.log.builder.DatasourceInfo;
//import ctd.net.rpc.Client;
//import ctd.net.rpc.balance.HostBalance;
//import ctd.net.rpc.chain.ChainContext;
//import ctd.net.rpc.chain.ChainFilter;
//import ctd.net.rpc.chain.support.RpcClientChainContext;
//import ctd.net.rpc.logger.pm.ServerNode;
//import ctd.spring.AppDomainContext;
//import ctd.util.AppContextHolder;
//import ctd.util.JSONUtils;
//import ctd.util.annotation.RpcService;
//import ctd.util.store.StoreException;
//import org.apache.commons.lang3.StringUtils;
//import org.springframework.beans.BeansException;
//import org.springframework.beans.MutablePropertyValues;
//import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
//import org.springframework.beans.factory.config.RuntimeBeanReference;
//import org.springframework.beans.factory.support.BeanDefinitionRegistry;
//import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
//import org.springframework.beans.factory.support.RootBeanDefinition;
//import org.springframework.stereotype.Component;
//import org.springframework.util.ObjectUtils;
//
//import java.util.*;
//
///**
// * @author: 何胜豪
// * @Title: TODO
// * @Package: hi.apptOp.base
// * @Description:
// * @date : 2021/11/27 21:33
// */
//@Component
//public class BalanceRpc implements BeanDefinitionRegistryPostProcessor   {
//
//    private Map<String, Object> domainMapping = new HashMap<>();
//
//    public BalanceRpc() {
//
//    }
//
//    @RpcService
//    public void ChangeBalacne(HashMap<String,Object> map){
//
////        domainMapping.put("his","127.0.0.1");
////        domainMapping.put("opdoc","127.0.0.1");
//
//        LinkedHashMap o1 = (LinkedHashMap<String,Object>) map.get("source");
//        String o =(String) o1.get("ip");
//        HashMap<String,String> target = (HashMap<String, String>) map.getOrDefault("target",new HashMap<String,String>());
//
//        domainMapping.clear();
//        domainMapping.putAll(target);
//
//        try {
//            String domain = AppContextHolder.getName();
//            String path = "/ssdev/serverNodes/"+domain+"/"+o;
//            byte[] data =  AppDomainContext.getActiveStore().getData(path);
//
//            ServerNode node =   JSON.parseObject(data, ServerNode.class);
//
//            DatasourceExData datasourceExData = new DatasourceExData();
//
//            for (String s : domainMapping.keySet()) {
//                DatasourceInfo datasourceInfo = new DatasourceInfo();
//                Map<String,String> d = (Map<String,String>)   domainMapping.get(s);
//                datasourceInfo.setURL(d.get("ip"));
//                datasourceInfo.setUserName(d.get("domain"));
//                datasourceExData.addDatasourceInfo(datasourceInfo);
//            }
//
//
//            node.setExDatas(Arrays.asList(datasourceExData));
//
//
//            byte[] bytes = JSONUtils.toBytes(node);
//            AppDomainContext.getActiveStore().delete(path);
//            AppDomainContext.getActiveStore().createTempPath(path,bytes);
//        } catch (StoreException e) {
//            e.printStackTrace();
//        }
//
//        Iterator<ChainFilter> iterator = Client.getChainPipeLine().iterator();
//
//        while (iterator.hasNext()){
//            ChainFilter next = iterator.next();
//            if(next instanceof HostFilter){
//                return;
//            }
//        }
//
//        Client.getChainPipeLine().addFirst(new HostFilter());
//    }
//
//    @Override
//    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
//
//        RootBeanDefinition def = new RootBeanDefinition();
//        def.setBeanClassName("ctd.net.rpc.beans.ServiceBean");
//        def.setInitMethodName("init");
//
//        MutablePropertyValues pv = def.getPropertyValues();
//        RuntimeBeanReference reference = new RuntimeBeanReference("balanceRpc");;
//        String domain = AppContextHolder.getName();
//        // his.beanName
//        String beanName = domain + ".balanceRpc" ;
//        pv.add("id", beanName);
//        pv.add("appDomain", domain);
//        pv.add("ref", reference);
//
//        HashMap<String, Object> map = new HashMap<>();
//        map.put("no_version_control", true);
//        map.put("mvc_authentication", false);
//        pv.add("properties", map);
//
//        registry.registerBeanDefinition(beanName,def);
//    }
//
//    @Override
//    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {
//
//    }
//
//
//
//    class HostFilter implements ChainFilter {
//
//        @Override
//        public void doFilter(ChainContext context) throws Exception {
//            String domain = StringUtils.substringBefore(((RpcClientChainContext) context).getServiceDesc().getId(), ".");
//            if (!ObjectUtils.isEmpty(BalanceRpc.this.domainMapping) && BalanceRpc.this.domainMapping.containsKey(domain) && !ObjectUtils.isEmpty(BalanceRpc.this.domainMapping.get(domain))) {
//                ((RpcClientChainContext) context).setBalance(new HostBalance(((Map<String,String>)BalanceRpc.this.domainMapping.get(domain)).get("ip")));
//            }
//
//            context.doNext();
//        }
//    }
//
//
//
//}
//
//
