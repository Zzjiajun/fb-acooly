package com.acooly.showcase.daliy.web;

import com.acooly.core.common.dao.support.PageInfo;
import com.acooly.core.common.exception.BusinessException;
import com.acooly.core.common.web.support.JsonEntityResult;
import com.acooly.core.common.web.support.JsonListResult;
import com.acooly.core.common.web.support.JsonResult;
import com.acooly.module.security.domain.User;
import com.acooly.module.security.service.UserService;
import com.acooly.showcase.base.AbstractShowcaseController;
import com.acooly.showcase.daliy.Pagination;
import com.acooly.showcase.daliy.ParameterRequestWrapper;
import com.acooly.showcase.daliy.dto.PerformanceDto;
import com.acooly.showcase.daliy.dto.TotalFromDto;
import com.acooly.showcase.daliy.entity.Accounts;
import com.acooly.showcase.daliy.entity.Permissions;
import com.acooly.showcase.daliy.entity.Records;
import com.acooly.showcase.daliy.entity.Transactionss;
import com.acooly.showcase.daliy.service.AccountsService;
import com.acooly.showcase.daliy.service.PermissionsService;
import com.acooly.showcase.daliy.service.RecordsService;
import com.acooly.showcase.daliy.service.TransactionsService;
import com.google.common.collect.Maps;
import lombok.SneakyThrows;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;


@Controller
@RequestMapping(value = "/manage/showcase/daily/records")
public class RecordsMangerController extends AbstractShowcaseController<Records, RecordsService> {
    {
        allowMapping = "*";
    }

    @Autowired
    private RecordsService recordsService;
    @Autowired
    private AccountsService accountsService;
    @Autowired
    private PermissionsService permissionsService;
    @Autowired
    private UserService userService;
    @Autowired
    private TransactionsService transactionsService;

    @Override
    protected PageInfo<Records> doList(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        Map<String, Object> searchParams = this.getSearchParams(request);
        User principal = (User) SecurityUtils.getSubject().getPrincipal();
        Map<String, Object> mapQuery = Maps.newHashMap();
        mapQuery.put("EQ_userName",principal.getUsername());
        if (!(permissionsService.query(mapQuery,null).size() >0)){
            searchParams.put("EQ_userName",principal.getUsername());
        }
        return this.getEntityService().query(this.getPageInfo(request), searchParams, this.getSortMap(request));
    }

    @Override
    public String query(HttpServletRequest request, HttpServletResponse response, Model model) {
        return super.query(request, response, model);
    }

    @Override
    protected void referenceData(HttpServletRequest request, Map<String, Object> model) {
        ArrayList<String> list = new ArrayList<>();
        list.add("养户");
        list.add("欧洲股");
        list.add("欧洲币");
        list.add("美国股");
        list.add("美国币");
        list.add("日本");
        list.add("韩国");
        list.add("英国");
        list.add("印度");
        model.put("regionList",list);
        User principal = (User) SecurityUtils.getSubject().getPrincipal();
        Map<String, Object> mapQuery1 = Maps.newHashMap();
        mapQuery1.put("EQ_userType","2" );
        List<User> query1 = userService.query(mapQuery1, null);
        Map<String, String> mapName = query1.stream().collect(Collectors.toMap(User::getUsername, User::getRealName));
        model.put("mapName",mapName);
        Map<String, Object> map = Maps.newHashMap();
        map.put("EQ_holder", principal.getUsername());
        List<Accounts> query = accountsService.query(map, null);
        query = query.stream().filter(s -> s.getStatus() == 1).collect(Collectors.toList());
        Map<String, Object> mapQuery = query.stream().collect(Collectors.toMap(Accounts::getAccountName,accounts -> new DecimalFormat("#.##").format(accounts.getBalance())));
        model.put("mapQuery", mapQuery);
        model.put("userName", principal.getUsername());
    }


    @SneakyThrows
    @Override
    @Transactional(rollbackFor = Exception.class)
    public JsonEntityResult<Records> saveJson(HttpServletRequest request, HttpServletResponse response) {
        User principal = (User) SecurityUtils.getSubject().getPrincipal();
        Map<String, Object> map = Maps.newHashMap();
        map.put("EQ_holder", principal.getUsername());
        List<Accounts> query = accountsService.query(map, null);
        query = query.stream().filter(s -> s.getStatus() == 1).collect(Collectors.toList());
        List<String> list = query.stream().map(Accounts::getAccountName).collect(Collectors.toList());
        try {
            Map<String, Object> mapQuery = query.stream().collect(Collectors.toMap(Accounts::getAccountName, Accounts::getBalance));
            Map<String, Long> mapById = query.stream().collect(Collectors.toMap(Accounts::getAccountName, Accounts::getId));
            Map<String, String[]> parameterMap = new HashMap<String, String[]>(request.getParameterMap());
            Map<String, Map<String, String[]>> accountNameMap = getAccountNameMap(new HashMap<String, String[]>(request.getParameterMap()));
            float spending = 0;
            for (String s : list) {
                Accounts accounts = new Accounts();
                accounts.setId(mapById.get(s));
                accounts.setStatus(1);
                //花销
                float balance = Float.parseFloat(parameterMap.get(s)[0]);
                //充值
                float balanceRecharge = Float.parseFloat(parameterMap.get(s + "recharge")[0]);
                //之前的余额
                float balanceOld = (float) mapQuery.get(s);
                accounts.setBalance(balanceOld - balance + balanceRecharge);
                accountsService.update(accounts);
                spending += (float) balance;
    //            parameterMap.remove(s);
            }
            String accountInformation = mapToString(accountNameMap.get("accountInformation"));
            String recharge = mapToString(accountNameMap.get("recharge"));
            BigDecimal bigDecimal = new BigDecimal(Double.toString(spending));
            bigDecimal = bigDecimal.setScale(2, RoundingMode.HALF_UP);
            parameterMap.put("spending", new String[]{String.valueOf(bigDecimal.doubleValue())});
            parameterMap.put("accountInformation", new String[]{accountInformation});
            parameterMap.put("recharge", new String[]{recharge});
            request = new ParameterRequestWrapper(request, parameterMap);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return super.saveJson(request, response);

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public JsonEntityResult<Records> updateJson(HttpServletRequest request, HttpServletResponse response) {
        String id = request.getParameter("id");
        Map<String, String[]> parameterMap = new HashMap<String, String[]>(request.getParameterMap());
        Records records = this.getEntityService().get(Long.valueOf(id));
        Map<String, Object> map1 = Maps.newHashMap();
        map1.put("EQ_holder",records.getUserName());
        map1.put("EQ_status","0");
        List<Accounts> query = accountsService.query(map1, null);
        try {
            List<String> collect = query.stream().map(Accounts::getAccountName).collect(Collectors.toList());
            Map<String, Float> stringToMaps = getRemoveMap(collect,stringToMap(records.getAccountInformation()));
            Map<String, Float> stringFloatMap = getRemoveMap(collect,stringRechargeToMap(records.getRecharge()));
            Map<String, Map<String, String[]>> accountNameMap = getAccountNameMap(new HashMap<String, String[]>(request.getParameterMap()));
            String accountInformation = mapToString(accountNameMap.get("accountInformation"));
            String recharge = mapToString(accountNameMap.get("recharge"));
            Map<String, Float> stringToMapsNew = stringToMap(accountInformation);
            Map<String, Float> stringFloatMapNew = stringRechargeToMap(recharge);
            Map<String, Object> map = Maps.newHashMap();
            float spending = 0;
            map.put("EQ_holder", request.getParameter("userName"));
            for (Map.Entry<String, Float> entry : stringToMaps.entrySet()) {
                map.put("EQ_accountName",entry.getKey());
                Accounts accounts = accountsService.query(map, null).get(0);
                accounts.setBalance(accounts.getBalance()-stringFloatMap.get(entry.getKey())+entry.getValue()-stringToMapsNew.get(entry.getKey())+stringFloatMapNew.get(entry.getKey()));
                accountsService.update(accounts);
                spending +=stringToMapsNew.get(entry.getKey());
                parameterMap.remove(entry.getKey());
                parameterMap.remove(entry.getKey()+ "recharge");
            }
            BigDecimal bigDecimal = new BigDecimal(Double.toString(spending));
            bigDecimal = bigDecimal.setScale(2, RoundingMode.HALF_UP);
            parameterMap.put("spending", new String[]{String.valueOf(bigDecimal.doubleValue())});
            parameterMap.put("accountInformation", new String[]{accountInformation});
            parameterMap.put("recharge", new String[]{recharge});
            request = new ParameterRequestWrapper(request, parameterMap);
        } catch (BusinessException e) {
            e.printStackTrace();
        }
        return super.updateJson(request, response);
    }


    @RequestMapping(value = "totalForm")
    @ResponseBody
    public JsonListResult<TotalFromDto> totalForm(HttpServletRequest request, HttpServletResponse response){
        JsonListResult<TotalFromDto> result = new JsonListResult();
        String userName = request.getParameter("userName");
        Map<String, Object> mapRecords = Maps.newHashMap();
        Map<String, Object> mapRecords1 = Maps.newHashMap();
        Map<String, Object> mapRecords2 = Maps.newHashMap();
        mapRecords.put("EQ_userName",userName);
        List<Records> query1 = recordsService.query(mapRecords, null);
        Integer page = Integer.valueOf(request.getParameter("page"));
        Integer rows = Integer.valueOf(request.getParameter("rows"));
        Map<String, Object> map = Maps.newHashMap();
        map.put("EQ_holder",userName);
        Map<String, Map<String, Float>> recordsList = getRecordsList(query1);
        ArrayList<TotalFromDto> totalFromDtoList = new ArrayList<>();
        DecimalFormat decimalFormat = new DecimalFormat("#.00");
        if (query1.size()>0){
            Map<String, Float> map1 = recordsList.get("accountInformation");
            Map<String, Float> map2 = recordsList.get("recharge");
            List<Accounts> query = accountsService.query(map, null);
            for (Accounts accounts : query){
                TotalFromDto totalFromDto = new TotalFromDto();
                totalFromDto.setAccountName(accounts.getAccountName());
                totalFromDto.setStatus(accounts.getStatus());
                totalFromDto.setBalance(accounts.getBalance());
                if (map1.containsKey(accounts.getAccountName())) {
                    totalFromDto.setSpending(map1.get(accounts.getAccountName()));
                    totalFromDto.setRecharge(map2.get(accounts.getAccountName()));
                } else {
                    totalFromDto.setSpending((float) 0);
                    totalFromDto.setRecharge((float) 0);
                }
                mapRecords1.put("EQ_outAccount",accounts.getAccountName());
                mapRecords2.put("EQ_inAccount",accounts.getAccountName());
                List<Transactionss> list = transactionsService.query(mapRecords1, null);
                List<Transactionss> list1 = transactionsService.query(mapRecords2, null);
                if (!(list.size()>0)){
                    totalFromDto.setOutAmount("0.00");
                }else {
                    Float outFolat = transactionsService.getOutFolat(accounts.getAccountName());
                    totalFromDto.setOutAmount(String.valueOf(outFolat));
                }
                if (list1.size()>0){
                    Float inFolat = transactionsService.getInFolat(accounts.getAccountName());
                    totalFromDto.setInAmount(String.valueOf(inFolat));
                }else {
                    totalFromDto.setInAmount("0.00");
                }
                totalFromDto.setCreateTime(accounts.getCreateTime());
                totalFromDto.setDeprecatedTime(accounts.getDeprecatedTime());
                totalFromDto.setOutStr(totalFromDto.getAccountName());
                totalFromDto.setInStr(totalFromDto.getAccountName());
                totalFromDtoList.add(totalFromDto);
            }
        }
        Pagination pagination = new Pagination(totalFromDtoList.size(), page,rows,totalFromDtoList);
        pagination.calculatePagination();
        result.setPageNo(pagination.getCurrentPage());
        result.setTotal(Long.valueOf(pagination.getTotalItems()));
        result.setPageSize(pagination.getItemsPerPage());
        result.setRows(pagination.getCurrentPageItems());
        return result;
    }


    private  Map<String,Map<String,Float>> getRecordsList(List<Records> query){
        Map<String,Map<String,Float>> mapMap=Maps.newHashMap();
        Map<String, Float> hashMap = Maps.newHashMap();
        Map<String, Float> hashMapRecharge = Maps.newHashMap();
        query.forEach(s->{
            Map<String, Float> floatMap = stringToMap(s.getAccountInformation());
            Map<String, Float> rechargeToMap = stringRechargeToMap(s.getRecharge());
            for (Map.Entry<String, Float> entry : floatMap.entrySet()) {
                hashMap.put(entry.getKey(), hashMap.getOrDefault(entry.getKey(), 0f) + entry.getValue());
            }
            for (Map.Entry<String, Float> entry : rechargeToMap.entrySet()) {
                hashMapRecharge.put(entry.getKey(), hashMapRecharge.getOrDefault(entry.getKey(), 0f) + entry.getValue());
            }
        });
        mapMap.put("accountInformation",hashMap);
        mapMap.put("recharge",hashMapRecharge);
        return mapMap;
    }

    @RequestMapping(value = "totalPermissions")
    @ResponseBody
    public List<User> totalPermissions(HttpServletRequest request, HttpServletResponse response){
        User principal = (User) SecurityUtils.getSubject().getPrincipal();
        Map<String, Object> mapQuery = Maps.newHashMap();
        ArrayList<User> userArrayList = new ArrayList<>();
        mapQuery.put("EQ_userName",principal.getUsername());
        if (permissionsService.query(mapQuery,null).size() >0){
            userArrayList.add(principal);
        }
        return userArrayList;
    }


    @RequestMapping(value = "totalPerformance")
    @ResponseBody
    public JsonListResult<PerformanceDto> totalPerformance(HttpServletRequest request, HttpServletResponse response){
        Map<String, String[]> parameterMap = request.getParameterMap();
        JsonListResult<PerformanceDto> result = new JsonListResult();
        Integer page = Integer.valueOf(request.getParameter("page"));
        Integer rows = Integer.valueOf(request.getParameter("rows"));
        String userName = request.getParameter("userName");
        Map<String, Object> mapRecords = Maps.newHashMap();
        mapRecords.put("EQ_userName",userName);
        if (StringUtils.isNotEmpty(request.getParameter("timeSelection")) && "true".equals(request.getParameter("timeSelection"))){
            String createTimeGTE = request.getParameter("createTimeGTE");
            String createTimeLTE = request.getParameter("createTimeLTE");
            if (StringUtils.isEmpty(createTimeGTE)){
                Date date = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
               createTimeGTE = sdf.format(date);
            }
            if (StringUtils.isEmpty(createTimeLTE)){
                Date date = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                createTimeLTE = sdf.format(date);
            }
            mapRecords.put("GTE_buildTime",createTimeGTE+" 00:00:00");
            mapRecords.put("LTE_buildTime",createTimeLTE+" 00:00:00");
        }
        List<Records> query1 = recordsService.query(mapRecords, null);
        Map<String, List<Records>> listMap = query1.stream()
                .collect(Collectors.groupingBy(Records::getRegion));
        List<PerformanceDto> permissionsList = new ArrayList<>();
        for (Map.Entry<String, List<Records>> entry : listMap.entrySet()) {
            PerformanceDto performanceDto = getPerformanceList(entry.getValue());
            performanceDto.setRegion(entry.getKey());
            permissionsList.add(performanceDto);
        }
        permissionsList.forEach(s->{
            BigDecimal bigNum1 = new BigDecimal(s.getSpending());
            BigDecimal bigNum2 = new BigDecimal(String.valueOf(s.getGrades()));
            if (s.getGrades()==0){
                s.setPrice(s.getSpending());
            }else {
                BigDecimal divide = bigNum1.divide(bigNum2, 2, RoundingMode.HALF_UP);
                s.setPrice(String.valueOf(divide));
            }
            if ("养户".equals(s.getRegion())){
                s.setPrice("0.00");
            }
        });
        Pagination pagination = new Pagination(permissionsList.size(), page,rows,permissionsList);
        pagination.calculatePagination();
        result.setPageNo(pagination.getCurrentPage());
        result.setTotal(Long.valueOf(pagination.getTotalItems()));
        result.setPageSize(pagination.getItemsPerPage());
        result.setRows(pagination.getCurrentPageItems());
        return result;
    }

    private PerformanceDto getPerformanceList(List<Records> listMap){
        int grades = 0;
        float reduce = 0;
        float reduce1 = 0;
        for (Records records : listMap) {
            grades+=records.getGrades();
            Map<String, Float> floatMap = stringToMap(records.getAccountInformation());
            Map<String, Float> rechargeToMap = stringRechargeToMap(records.getRecharge());
            reduce += floatMap.values().stream().reduce(0f, Float::sum);
            reduce1 += rechargeToMap.values().stream().reduce(0f, Float::sum);
//            for (Map.Entry<String, Float> entry : floatMap.entrySet()) {
//                reduce+=entry.getValue();
//            }
//            for (Map.Entry<String, Float> entry :rechargeToMap.entrySet()) {
//                reduce1+=entry.getValue();
//            }
        }
        PerformanceDto performanceDto = new PerformanceDto();
        performanceDto.setGrades(grades);
        DecimalFormat decimalFormat = new DecimalFormat("#.00");
        performanceDto.setRecharge(decimalFormat.format(reduce1));
        performanceDto.setSpending(decimalFormat.format(reduce));
        return performanceDto;
    }



    @Override
    protected Records onSave(HttpServletRequest request, HttpServletResponse response, Model model, Records entity, boolean isCreate) throws Exception {
        String buildTime = request.getParameter("buildTime");
        Date date = convertFrontendDateToBackendDate(buildTime);
        entity.setBuildTime(date);
        return super.onSave(request, response, model, entity, isCreate);
    }

    @Override
    public JsonResult deleteJson(HttpServletRequest request, HttpServletResponse response) {
        Long id = Long.valueOf(request.getParameter("id"));
        Map<String, Object> map = Maps.newHashMap();
        Records records = recordsService.get(id);
        Map<String, Float> stringToMap = stringToMap(records.getAccountInformation());
        Map<String, Float> stringFloatMap = stringRechargeToMap(records.getRecharge());
        for (Map.Entry<String, Float> entry : stringToMap.entrySet()) {
            map.put("EQ_accountName",entry.getKey());
            Accounts accounts = accountsService.query(map, null).get(0);
            accounts.setBalance(accounts.getBalance()+entry.getValue()-stringFloatMap.get(entry.getKey()));
            accountsService.update(accounts);
        }
        return super.deleteJson(request, response);
    }

    private Map<String,Map<String, String[]>> getAccountNameMap(Map<String, String[]> parameterMap) {
        Map<String,Map<String, String[]>> map=Maps.newHashMap();
        parameterMap.remove("id");
        parameterMap.remove("grades");
        parameterMap.remove("region");
        parameterMap.remove("userName");
        parameterMap.remove("buildTime");
        Map<String, String[]> accountInformationKeys = new HashMap<>();
        Map<String, String[]> rechargeKeys = new HashMap<>();

        for (String key : parameterMap.keySet()) {
            if (key.endsWith("recharge")) {
                rechargeKeys.put(key,parameterMap.get(key));
            } else { // 如果键长度等于3，说明无尾明reg
                accountInformationKeys.put(key,parameterMap.get(key));
            }
        }
        map.put("accountInformation",accountInformationKeys);
        map.put("recharge",rechargeKeys);

        return map;
    }

    public String mapToString(Map<String, String[]> map) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String[]> entry : map.entrySet()) {
            if (entry.getKey().endsWith("recharge")){
                sb.append(entry.getKey().substring(0, entry.getKey().length() - "recharge".length()))
                        .append("充值")
                        .append(": ")
                        .append(Arrays.toString(entry.getValue()))
                        .append(" ");
                sb.append(",");
            }else {
                sb.append(entry.getKey()).append(": ").append(Arrays.toString(entry.getValue())).append(" ");
                sb.append(",");
            }

        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }
    private  Map<String,Float> stringToMap(String s){
        List<String> accountName = Arrays.asList(s.split(","));
        Map<String,Float> map =Maps.newHashMap();
        for (String str :accountName){
            int i = str.indexOf(':');
            if (i != -1) {
                int leftBracket = str.indexOf("[");
                int rightBracket = str.indexOf("]");
                String numStr = str.substring(leftBracket + 1, rightBracket);
                map.put(str.substring(0, i),Float.parseFloat(numStr));
            }
        }
        return map;
    }
    private  Map<String,Float> stringRechargeToMap(String s){
        List<String> accountName = Arrays.asList(s.split(","));
        Map<String,Float> map =Maps.newHashMap();
        for (String str :accountName){
            int i = str.indexOf(':');
            if (i != -1) {
                int leftBracket = str.indexOf("[");
                int rightBracket = str.indexOf("]");
                String numStr = str.substring(leftBracket + 1, rightBracket);
                map.put(str.substring(0, i-2),Float.parseFloat(numStr));
            }
        }
        return map;
    }



    private static Date convertFrontendDateToBackendDate(String frontendDate) {
        SimpleDateFormat frontendDateFormat = new SimpleDateFormat("MM/dd/yyyy");
        SimpleDateFormat backendDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = frontendDateFormat.parse(frontendDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    private Map<String, Float> getRemoveMap(List<String> list,Map<String, Float> map){
        for (String key : list) {
            if (map.containsKey(key)) {
                map.remove(key);
            }
        }
        return map;
    }

//    private Map<String, String[]> UpdateOrSave(HttpServletRequest request,boolean b){
//
//    }
}
//        String accountInformation = JSONObject.toJSONString(accountNameMap);
//        ObjectMapper objectMapper = new ObjectMapper();
//        String accountInformation = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(accountNameMap.get("accountInformation"));
//        String recharge = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(accountNameMap.get("recharge"));