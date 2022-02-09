//package com.bsoft;
//
//import com.alibaba.fastjson.JSON;
//import ctd.mixin.rpc.log.builder.DatasourceExData;
//import ctd.mixin.rpc.log.builder.DatasourceInfo;
//import ctd.mvc.resource.service.ResourceLocateService;
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
//import org.springframework.context.ApplicationListener;
//import org.springframework.context.annotation.PropertySource;
//import org.springframework.context.event.ContextRefreshedEvent;
//import org.springframework.core.env.ConfigurableEnvironment;
//
//
//import org.springframework.stereotype.Component;
//import org.springframework.util.ObjectUtils;
//
//import java.util.*;
//
///**
// * @author: 何
// * @Title: TODO
// * @Package:
// * @Description:
// * @date : 2021/11/27 21:33
// */
//@Component
////@PropertySource(value = "classpath:./package/development.properties",ignoreResourceNotFound = true)
////@PropertySource(value = "classpath:./package/test.properties",ignoreResourceNotFound = true)
////@PropertySource(value = "classpath:./package/local.properties",ignoreResourceNotFound = true)
//public class BalanceRpc  implements BeanDefinitionRegistryPostProcessor, ApplicationListener<ContextRefreshedEvent>  {
//
////    private Map<String, String> domainMapping = new HashMap<>();
//
//    private static String PATH = "";
//
//    private static Boolean  IS_OPEN;
//
////    @RpcService
////    public void changeBalance(HashMap<String,Object> map){
////        if(!IS_OPEN){
////            return;
////        }
////        HashMap<String,HashMap<String,String>> target = (HashMap<String, HashMap<String,String>>) map.getOrDefault("target",new HashMap<String,HashMap<String,String>>());
////
////        domainMapping.clear();
////
////        try {
////            byte[] data =  AppDomainContext.getActiveStore().getData(PATH);
////
////            ServerNode node =   JSON.parseObject(data, ServerNode.class);
////
////            DatasourceExData datasourceExData = new DatasourceExData();
////
////            for (String service : target.keySet()) {
////                DatasourceInfo datasourceInfo = new DatasourceInfo();
////                HashMap<String,String> innerMap = target.get(service);
////                String domain = innerMap.get("domain");
////                String nodeName = innerMap.get("nodeName");
////                String ip = getHost(nodeName);
////
////                domainMapping.put(domain,ip);
////                datasourceInfo.setURL(ip);
////                datasourceInfo.setUserName(domain);
////                datasourceExData.addDatasourceInfo(datasourceInfo);
////            }
////
////            node.setExDatas(Arrays.asList(datasourceExData));
////
////            byte[] bytes = JSONUtils.toBytes(node);
////            AppDomainContext.getActiveStore().delete(PATH);
////            AppDomainContext.getActiveStore().createTempPath(PATH,bytes);
////        } catch (StoreException e) {
////            e.printStackTrace();
////        }
////
////        Iterator<ChainFilter> iterator = Client.getChainPipeLine().iterator();
////
////        while (iterator.hasNext()){
////            ChainFilter next = iterator.next();
////            if(next instanceof HostFilter){
////                return;
////            }
////        }
////
////        Client.getChainPipeLine().addFirst(new HostFilter());
////    }
//
//    private String getHost(String path) {
//        String[] split = path.split("@");
//        path = split[1];
//        split = path.split(":");
//        return  split[0];
//    }
//
//
//    @Override
//    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
//
////        RootBeanDefinition def = new RootBeanDefinition();
////        def.setBeanClassName("ctd.net.rpc.beans.ServiceBean");
////        def.setInitMethodName("init");
////
////        MutablePropertyValues pv = def.getPropertyValues();
////        RuntimeBeanReference reference = new RuntimeBeanReference("balanceRpc");;
////        String domain = AppContextHolder.getName();
////        // his.beanName
////        String beanName = domain + ".balanceRpc" ;
////        pv.add("id", beanName);
////        pv.add("appDomain", domain);
////        pv.add("ref", reference);
////
////        HashMap<String, Object> map = new HashMap<>();
////        map.put("no_version_control", true);
////        map.put("mvc_authentication", false);
////        pv.add("properties", map);
////
////        registry.registerBeanDefinition(beanName,def);
//    }
//
//    @Override
//    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {
//
//    }
//
//
//
//
//
//    @Override
//    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
//
//        // 应用初始化时在zk节点上添加http端口
//        ResourceLocateService resourceLocator = AppDomainContext.getBean("resourceLocator", ResourceLocateService.class);
//        String httpContextPath = resourceLocator.getHttpContextPath();
//        String replace = httpContextPath.replace("http://", "");
//        String[] httpPath = replace.split(":");
//        String port = httpPath[1];
//
//        String rpcServerWorkUrl = AppDomainContext.getRpcServerWorkUrl();
//         replace = rpcServerWorkUrl.replace("tcp://", "");
//        String[] tpcPath = replace.split(":");
//        String ip = tpcPath[0];
//
//        String domain = AppContextHolder.getName();
//
//        String[] split = rpcServerWorkUrl.replace("tcp://", "").split("\\?");
//        String url = split[0];
//        String serverId = AppDomainContext.getServerId();
//
//
//        String domainPath = "/ssdev/serverNodes/"+domain+"/"+serverId+"@"+url;
//
//        try {
//            // TODO 把线上的改掉了
//            List<String> children = AppDomainContext.getActiveStore().getChildren(domainPath);
//            String s = children.get(0);
//            domainPath = domainPath + "/" +s;
//
//            ServerNode node = ServerNode.instance();
//
//            node.setIpAddress("http://"+ip+":"+port);
//
//            byte[] bytes = JSONUtils.toBytes(node);
//            AppDomainContext.getActiveStore().delete(PATH);
//            AppDomainContext.getActiveStore().createTempPath(PATH,bytes);
//
//        } catch (StoreException e) {
//            e.printStackTrace();
//        }
//
//    }
//
////    class HostFilter implements ChainFilter {
////
////        @Override
////        public void doFilter(ChainContext context) throws Exception {
////            String domain = StringUtils.substringBefore(((RpcClientChainContext) context).getServiceDesc().getId(), ".");
////            if (!ObjectUtils.isEmpty(BalanceRpc.this.domainMapping) && BalanceRpc.this.domainMapping.containsKey(domain) && !ObjectUtils.isEmpty(BalanceRpc.this.domainMapping.get(domain))) {
////                ((RpcClientChainContext) context).setBalance(new HostBalance(BalanceRpc.this.domainMapping.get(domain)));
////            }
////
////            context.doNext();
////        }
////    }
//
//
//
//}
//
//
