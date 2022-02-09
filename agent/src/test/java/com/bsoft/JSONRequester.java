//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.bsoft;

import com.alibaba.fastjson.JSON;
import ctd.account.UserRoleToken;
import ctd.mvc.controller.OutputSupportMVCController;
import ctd.mvc.controller.util.UserRoleTokenUtils;
import ctd.net.rpc.Client;
import ctd.net.rpc.balance.HostBalance;
import ctd.net.rpc.beans.ServiceBean;
import ctd.net.rpc.chain.support.RpcClientChainContext;
import ctd.net.rpc.desc.support.MethodDesc;
import ctd.net.rpc.desc.support.ServiceDesc;
import ctd.net.rpc.json.JSONRequestBean;
import ctd.net.rpc.json.JSONRequestParser;
import ctd.net.rpc.json.JSONResponseBean;
import ctd.net.rpc.logger.pm.ServerNode;
import ctd.net.rpc.util.ServiceAdapter;
import ctd.security.Repository;
import ctd.security.ResourceNode;
import ctd.security.exception.SecurityException;
import ctd.spring.AppDomainContext;
import ctd.util.AppContextHolder;
import ctd.util.JSONUtils;
import ctd.util.ServletUtils;
import ctd.util.context.ContextUtils;
import ctd.util.exception.CodedBase;
import ctd.util.exception.CodedBaseException;
import ctd.util.exception.CodedBaseRuntimeException;
import ctd.util.json.support.exception.JSONParseException;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ctd.util.store.StoreException;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

@Controller("mvcJSONRequester")
public class JSONRequester extends OutputSupportMVCController {
    private static final Logger logger = LoggerFactory.getLogger(JSONRequester.class);
    private static final String MVC_AUTHENTICATION = "mvc_authentication";
    private static final String MVC_AUTH_METHOD_EXCLUDE = "mvc_auth_method_exclude";
    private static final String MVC_EXPIRES = "mvc_expires";
    private static final String LOCAL_DOMAIN_PLACE_HOLDER = "$.";
    private static final String HEADER_SERVICE_ID = "X-Service-Id";
    private static final String HEADER_SERVICE_METHOD = "X-Service-Method";
    private static final String HEADER_ACTION_ID = "X-Action-Id";

    public JSONRequester() {
    }

    private Map<String, String> domainMapping = new HashMap<>();

    private Boolean hasFilter = false;

    @RequestMapping(
            value = {"/**/*.balance"},
            method = {RequestMethod.POST},
            headers = {"content-type=application/json"}
    )
    public void doBalance(@RequestBody HashMap<String,Object> map, HttpServletResponse response) {

        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> " +
                " change Balance " +
                "<<<<<<<<<<<<<<<<<<<<<<<<<<<<");

        HashMap<String,String> target = (HashMap<String,String>) map.getOrDefault("target",new HashMap<String,String>());

        LinkedHashMap o1 = (LinkedHashMap<String,Object>) map.get("source");

        String nodeName = (String) o1.get("nodeName");

        String domain = AppContextHolder.getName();

        String PATH =  "/ssdev/serverNodes/"+domain+"/"+nodeName;

        try {
            byte[] data =  AppDomainContext.getActiveStore().getData(PATH);

            ServerNode node = JSON.parseObject(data, ServerNode.class);

            if(target.size()==0){
                node.setDomainMapping(null);
            }

            domainMapping.clear();

            domainMapping.putAll(target);

            node.setDomainMapping(domainMapping);

            byte[] bytes = JSONUtils.toBytes(node);

            AppDomainContext.getActiveStore().setData(PATH,bytes);

        } catch (StoreException e) {
            e.printStackTrace();
        }

        if(!hasFilter){
            Client.getChainPipeLine().addFirst((context) -> {
                RpcClientChainContext c = (RpcClientChainContext)  context;
                String domains = StringUtils.substringBefore(c.getServiceDesc().getId(), ".");
                if (!ObjectUtils.isEmpty(this.domainMapping) && this.domainMapping.containsKey(domains) && !ObjectUtils.isEmpty(this.domainMapping.get(domains))) {
                    c.setBalance(new HostBalance((String)this.domainMapping.get(domains)));
                }
                c.doNext();
            });
            hasFilter = true;
        }
        JSONResponseBean bean = new JSONResponseBean();
        bean.setBody("执行成功");
        bean.setCode(200);
        bean.setMsg("");
        try {
            this.jsonOutput(response, bean, false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(
            value = {"/**/*.jsonRequest"},
            method = {RequestMethod.POST},
            headers = {"content-type=application/json"}
    )
    public void doJSONRequest(HttpServletRequest request, HttpServletResponse response) {
        String beanName = request.getHeader("X-Service-Id");
        String methodName = request.getHeader("X-Service-Method");
        String actionId = request.getHeader("X-Action-Id");
        this.dpApiInvoke(beanName, methodName, actionId, request, response);
    }

    @RequestMapping(
            value = {"/api/{beanName}/{method}"},
            method = {RequestMethod.POST},
            headers = {"content-type=application/json"}
    )
    public void dpApiInvoke(@PathVariable("beanName") String beanName, @PathVariable("method") String methodName, @RequestParam(value = "ac",required = false) String actionId, HttpServletRequest request, HttpServletResponse response) {
        JSONResponseBean responseBean = new JSONResponseBean();
        boolean gzip = ServletUtils.isAcceptGzip(request);

        Throwable t;
        CodedBase c;
        try {
            ContextUtils.put("$httpRequest", request);
            ServiceDesc service;
            if (StringUtils.isNotEmpty(beanName)) {
                beanName = this.getRealBeanName(beanName);
                ServiceDesc remoteService = ServiceAdapter.getServiceDescFixed(beanName);
                if (!this.isAccessible(request, beanName, methodName, actionId, remoteService)) {
                    throw new SecurityException(SecurityException.ACCESS_DENIED);
                }

                service = ServiceAdapter.findLocalServiceDesc(beanName);
                if (service == null) {
                    byte[] bytes = IOUtils.toByteArray(request.getInputStream());
                    byte[] res = ServiceAdapter.invokeWithJsonBytes(beanName, methodName, bytes);

                    try {
                        this.jsonBytesOutput(response, res, gzip);
                    } catch (IOException var23) {
                        response.setStatus(500);
                        logger.error(var23.getMessage());
                    }

                    ContextUtils.clear();
                    return;
                }

                if (remoteService != null) {
                    service.setProperties(remoteService.getProperties());
                }

                MethodDesc method = service.getMethodByName(methodName);
                if (method == null) {
                    throw new JSONParseException(405, "service[" + beanName + "] method[" + methodName + "] not found.");
                }

                Object[] parameters = JSONRequestParser.parseParameters(method, request.getInputStream());
                Object result = ServiceAdapter.invokeLocalService((ServiceBean)service, method, parameters);
                responseBean.setBody(result);
            } else {
                JSONRequestBean requestBean = JSONRequestParser.parse(request.getInputStream());
                actionId = (String)requestBean.getProperty("actionId");
                beanName = requestBean.getServiceId();
                service = ServiceAdapter.getServiceDescFixed(beanName);
                if (service != null) {
                    Map<String, Object> confs = service.getProperties();
                    if (confs != null && confs.containsKey("track_log_level")) {
                        requestBean.setProperty("track_log_level", confs.get("track_log_level"));
                    }
                }

                if (!this.isAccessible(request, beanName, methodName, actionId, service)) {
                    throw new SecurityException(SecurityException.ACCESS_DENIED);
                }

                Object result = ServiceAdapter.invokeWithJsonRequest(requestBean);
                responseBean.setBody(result);
            }
        } catch (SecurityException var24) {
            this.outputAuthFailed(response, var24);
            ContextUtils.clear();
            return;
        } catch (CodedBaseException var25) {
            t = var25.getCause();
            if (t instanceof CodedBase) {
                c = (CodedBase)t;
                responseBean.setCode(c.getCode());
                responseBean.setMsg(c.getMessage());
            } else {
                responseBean.setCode(var25.getCode());
                responseBean.setMsg(var25.getMessage());
            }

            logger.error(String.format("jsonRequest[%s.%s(...)] failed.", beanName, methodName), var25);
        } catch (CodedBaseRuntimeException var26) {
            t = var26.getCause();
            if (t instanceof CodedBase) {
                c = (CodedBase)t;
                responseBean.setCode(c.getCode());
                responseBean.setMsg(c.getMessage());
            } else {
                responseBean.setCode(var26.getCode());
                responseBean.setMsg(var26.getMessage());
            }

            logger.error(String.format("jsonRequest[%s.%s(...)] failed.", beanName, methodName), var26);
        } catch (Exception var27) {
            t = var27.getCause();
            if (!(t instanceof CodedBase)) {
                logger.error(var27.getMessage(), var27);
                response.setStatus(500);
                ContextUtils.clear();
                return;
            }

            c = (CodedBase)t;
            responseBean.setCode(c.getCode());
            responseBean.setMsg(c.getMessage());
        }

        try {
            this.jsonOutput(response, responseBean, gzip);
        } catch (Exception var21) {
            response.setStatus(500);
            var21.printStackTrace();
            logger.error(var21.getMessage());
        } finally {
            ContextUtils.clear();
        }

    }

    private String getRealBeanName(String beanName) {
        if (beanName.startsWith("$.")) {
            beanName = StringUtils.replaceOnce(beanName, "$", AppDomainContext.getName());
        }

        return beanName;
    }

    private boolean isAccessible(HttpServletRequest request, String beanName, String method, String actionId, ServiceDesc service) throws CodedBaseException {
        if (service == null) {
            throw new CodedBaseException(404, "service[" + beanName + "] not found.");
        } else {
            Boolean authentication = (Boolean)service.getProperty("mvc_authentication", Boolean.class, true);
            boolean actionAccessible = this.isAccessibleAction(actionId);
            if (authentication && !actionAccessible) {
                return false;
            } else {
                boolean methodNeedAuthorize = this.methodNeedAuthorize(authentication, service, method);
                if (methodNeedAuthorize) {
                    UserRoleToken token = UserRoleTokenUtils.getUserRoleToken(request);
                    String currentDeptId = UserRoleTokenUtils.getCurrentDeptId(request);
                    Map<String, Object> headers = new HashMap();
                    headers.put("$uid", token.getUserId());
                    headers.put("$urt", token.getId());
                    headers.put("$tenantId", token.getTenantId());
                    headers.put("currentDeptId", currentDeptId);
                    headers.put("$clientIpAddress", ServletUtils.getIpAddress(request));
                    headers.put("$fromDomain", AppDomainContext.getName());
                    ContextUtils.put("$rpcInvokeHeaders", headers);
                    ContextUtils.put("$ur", token);
                    ContextUtils.put("currentDeptId", currentDeptId);
                    ContextUtils.put("$clientIpAddress", ServletUtils.getIpAddress(request));
                    ContextUtils.put("$fromDomain", AppDomainContext.getName());
                }

                return true;
            }
        }
    }

    private boolean isAccessibleAction(String actionId) {
        if (StringUtils.isEmpty(actionId)) {
            return true;
        } else {
            String[] paths = actionId.split("/");
            ResourceNode node = Repository.getNode(paths);
            return node.lookupPermissionMode().isAccessible();
        }
    }

    private boolean methodNeedAuthorize(boolean authentication, ServiceDesc service, String method) {
        String authProp = (String)service.getProperty("mvc_auth_method_exclude", String.class);
        if (StringUtils.isEmpty(authProp)) {
            return authentication;
        } else {
            String[] patterns = authProp.split(",");
            String[] var6 = patterns;
            int var7 = patterns.length;

            for(int var8 = 0; var8 < var7; ++var8) {
                String p = var6[var8];
                if (method.matches(p.trim())) {
                    return !authentication;
                }
            }

            return authentication;
        }
    }
}
