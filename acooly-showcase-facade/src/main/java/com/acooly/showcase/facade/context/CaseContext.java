//package com.acooly.showcase.facade.context;
//
//import com.acooly.core.common.facade.InfoBase;
//import com.acooly.core.utils.Ids;
//import com.acooly.showcase.facade.annotation.CaseApiService;
//import lombok.Getter;
//import lombok.Setter;
//import org.slf4j.MDC;
//
//@Getter
//@Setter
//public class CaseContext extends InfoBase {
//    //HttpServletRequest 原始请求对象：存储原始的 HTTP 请求。
//    private HttpServletRequest orignalRequest;
//    //HttpServletResponse 原始响应对象：存储原始的 HTTP 响应。
//    private HttpServletResponse orignalResponse;
//    //String gid：用于存储全局唯一标识符，用于标识当前测试用例的唯一性。
//    private String gid;
//    //CaseApiService 接口服务对象：用于存储当前测试用例的接口服务对象。用于处理测试用例的 API 服务。
//    private CaseApiService caseApiService;
//
//    //CaseService 接口服务对象：用于存储当前测试用例的接口服务对象。用于处理测试用例的业务逻辑。
//    @SuppressWarnings("rawtypes")
//    private CaseService caseService;
//
//    //TestCase testCase：存储测试用例实例。
//    private TestCase testCase;
//
//    /**
//     * 用例编码
//     */
//    private String caseNo;
//
//    /**
//     * 项目编码
//     */
//    private String projectNo;
//
//    /**
//     * 接口编码
//     */
//    private String interfaceNo;
//
//    /**
//     * 参数编码
//     */
//    private String parametersNo;
//    //CaseBaseRequest 请求对象：存储当前测试用例的请求信息。
//    private CaseBaseRequest request;
//    //CaseBaseResponse 响应对象：存储当前测试用例的响应信息。
//    private CaseBaseResponse response;
//    // String requestBody：存储 HTTP 请求的主体内容。
//    private String requestBody;
//    //String responseBody：存储 HTTP 响应的主体内容
//    private String responseBody;
//    //String gatewayUrl：存储当前测试用例的网关地址。
//    private String gatewayUrl;
//
//    public CaseApiService getCaseApiService() {
//        return caseApiService;
//    }
//
//    public void setCaseApiService(CaseApiService caseApiService) {
//        this.caseApiService = caseApiService;
//        setCaseNo(caseApiService.caseNo());
//    }
//
//    public void init() {
//        this.gid = Ids.gid();
//        MDC.put(CaseConstants.GID, gid);
//        MDC.put("caseNo", caseNo);
//    }
//
//    //这段代码的作用是在运行时获取 caseService 对象所属类上的 CaseApiService 注解，
//    // 并将其保存在 caseApiService 属性中。这样可以在其他地方使用 caseApiService 属性
//    public void setCaseService(CaseService caseService) {
//        this.setCaseApiService(caseService.getClass().getAnnotation(CaseApiService.class));
//        this.caseService = caseService;
//    }
//
//    public void destory() {
//
//    }
//}
