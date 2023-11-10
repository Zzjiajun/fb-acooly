
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
import com.acooly.showcase.daliy.dto.PerformanceDto;
import com.acooly.showcase.daliy.dto.TotalFromDto;
import com.acooly.showcase.daliy.dto.UserSpending;
import com.acooly.showcase.daliy.entity.Accounts;
import com.acooly.showcase.daliy.entity.Records;
import com.acooly.showcase.daliy.entity.Transactionss;
import com.acooly.showcase.daliy.service.AccountsService;
import com.acooly.showcase.daliy.service.PermissionsService;
import com.acooly.showcase.daliy.service.RecordsService;
import com.acooly.showcase.daliy.service.TransactionsService;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.Result;
import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping(value = "/manage/showcase/daily/accounts")
public class AccountsMangerController extends AbstractShowcaseController<Accounts, AccountsService> {


    {
        allowMapping = "*";
    }

    @Autowired
    private AccountsService accountsService;
    @Autowired
    private UserService userService;
    @Autowired
    private RecordsService recordsService;
    @Autowired
    private TransactionsService transactionsService;
    @Autowired
    private PermissionsService permissionsService;


    @Override
    protected PageInfo<Accounts> doList(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        Map<String, Object> searchParams = this.getSearchParams(request);
        User principal = (User) SecurityUtils.getSubject().getPrincipal();
        Map<String, Object> mapQuery = Maps.newHashMap();
        mapQuery.put("EQ_userName", principal.getUsername());
        if (!(permissionsService.query(mapQuery, null).size() > 0)) {
            searchParams.put("EQ_holder", principal.getUsername());
        }
        return this.getEntityService().query(this.getPageInfo(request), searchParams, this.getSortMap(request));
    }

    @Override
    protected void referenceData(HttpServletRequest request, Map<String, Object> model) {
        Map<Integer, String> map = Maps.newHashMap();
        map.put(1, "有效");
        map.put(0, "无效");
        model.put("statusList", map);
        Map<String, Object> mapQuery = Maps.newHashMap();
        mapQuery.put("EQ_userType", "2");
        List<User> query = userService.query(mapQuery, null);
        Map<String, String> mapName = query.stream().collect(Collectors.toMap(User::getUsername, User::getRealName));
        model.put("mapName", mapName);
    }


    @RequestMapping(value = "total")
    public String total(Model model, HttpServletRequest request, HttpServletResponse response) {
        ArrayList<String> list1 = new ArrayList<>();
        list1.add("欧洲股");
        list1.add("欧洲币");
        list1.add("美国股");
        list1.add("美国币");
        list1.add("日本");
        list1.add("韩国");
        list1.add("英国");
        list1.add("印度");
        Map<String, Object> map = Maps.newHashMap();
        map.put("regionList", list1);
        model.addAllAttributes(map);
        return "/manage/showcase/daily/totalForm";
    }

    @RequestMapping(value = "totalParse")
    public String totalParse(Model model, HttpServletRequest request, HttpServletResponse response) {
        return "/manage/showcase/daily/totalParse";
    }

    @RequestMapping(value = "outParse")
    public String outParse(Model model, HttpServletRequest request, HttpServletResponse response) {
        String accountName = request.getParameter("accountName");
        Map<String, Object> map = Maps.newHashMap();
        map.put("accountName", accountName);
        model.addAllAttributes(map);
        return "/manage/showcase/daily/outParse";
    }

    @RequestMapping(value = "inParse")
    public String inParse(Model model, HttpServletRequest request, HttpServletResponse response) {
        String accountName = request.getParameter("accountName");
        Map<String, Object> map = Maps.newHashMap();
        map.put("accountName", accountName);
        model.addAllAttributes(map);
        return "/manage/showcase/daily/inParse";
    }


    @RequestMapping(value = "outParseList")
    @ResponseBody
    public JsonListResult<Transactionss> outParseList(HttpServletRequest request, HttpServletResponse response) {
        Integer page = Integer.valueOf(request.getParameter("page"));
        Integer rows = Integer.valueOf(request.getParameter("rows"));
        JsonListResult<Transactionss> result = new JsonListResult<>();
        String accountName = request.getParameter("accountName");
        Map<String, Object> map = Maps.newHashMap();
        map.put("EQ_inAccount",accountName);
        List<Transactionss> query = transactionsService.query(map, null);
        Pagination pagination = new Pagination(query.size(), page, rows, query);
        pagination.calculatePagination();
        result.setPageNo(pagination.getCurrentPage());
        result.setTotal(Long.valueOf(pagination.getTotalItems()));
        result.setPageSize(pagination.getItemsPerPage());
        result.setRows(pagination.getCurrentPageItems());
        return result;
    }


    @RequestMapping(value = "inParseList")
    @ResponseBody
    public JsonListResult<Transactionss> inParseList(HttpServletRequest request, HttpServletResponse response) {
        Integer page = Integer.valueOf(request.getParameter("page"));
        Integer rows = Integer.valueOf(request.getParameter("rows"));
        JsonListResult<Transactionss> result = new JsonListResult<>();
        String accountName = request.getParameter("accountName");
        Map<String, Object> map = Maps.newHashMap();
        map.put("EQ_outAccount",accountName);
        List<Transactionss> query = transactionsService.query(map, null);
        Pagination pagination = new Pagination(query.size(), page, rows, query);
        pagination.calculatePagination();
        result.setPageNo(pagination.getCurrentPage());
        result.setTotal(Long.valueOf(pagination.getTotalItems()));
        result.setPageSize(pagination.getItemsPerPage());
        result.setRows(pagination.getCurrentPageItems());
        return result;
    }




    @RequestMapping(value = "transferOut")
    public String transferOut(Model model, HttpServletRequest request, HttpServletResponse response) {
        String id = request.getParameter("id");
        List<Accounts> all = this.getEntityService().getAll();
        Accounts accounts = this.getEntityService().get(Long.valueOf(id));
        all.remove(accounts);
        List<String> listHolder = all.stream()
                .map(Accounts::getHolder)
                .distinct()
                .collect(Collectors.toList());
        Map<String, List<String>> mapHolder = all.stream()
                .collect(Collectors.groupingBy(Accounts::getHolder,
                        Collectors.mapping(Accounts::getAccountName, Collectors.toList())));
        DecimalFormat decimalFormat = new DecimalFormat("#.00");
        Float balance = accounts.getBalance();
        String format = decimalFormat.format(balance);
        Map<String, Object> map = Maps.newHashMap();
        map.put("accountsId", id);
        map.put("mapHolder", mapHolder);
        map.put("listHolder", listHolder);
        map.put("balance",format);
        model.addAllAttributes(map);
        return "/manage/showcase/daily/transferOut";
    }


    @RequestMapping(value = "totalParseList")
    @ResponseBody
    public JsonResult totalParseList(HttpServletRequest request, HttpServletResponse response) {
        JsonResult jsonResult = new JsonResult();
        String phoneNumber = request.getParameter("phoneNumber");
        List<String> resultList = new ArrayList<>();
        String[] parts = phoneNumber.split("\\+");
        for (String part : parts) {
            part = StringEscapeUtils.unescapeHtml4(part);
            String cleanedPart = part.replace("(", "").replace(")", "").replace("-", "").replace(" ", "");
            ;
            resultList.add(cleanedPart);
        }
        Map<Object, Object> map = Maps.newHashMap();
        map.put("list", resultList.subList(1, resultList.size()));
        jsonResult.setData(map);
        return jsonResult;
    }

    @RequestMapping(value = "userTotalList")
    @ResponseBody
    public JsonListResult<UserSpending> userTotalList(HttpServletRequest request, HttpServletResponse response) {
        Integer page = Integer.valueOf(request.getParameter("page"));
        Integer rows = Integer.valueOf(request.getParameter("rows"));
        Map<String, Object> mapRecords = Maps.newHashMap();
        JsonListResult<UserSpending> result = new JsonListResult();
        List<User> all = userService.getAll();
        List<User> filteredList = all.stream()
                .filter(s -> s.getUserType() != 1)
                .collect(Collectors.toList());
        ArrayList<UserSpending> userSpendingList = new ArrayList<>();
        for (User user : filteredList) {
            UserSpending userSpending = new UserSpending();
            userSpending.setId(user.getId());
            userSpending.setUsername(user.getUsername());
            userSpending.setRealName(user.getRealName());
            userSpending.setEmail(user.getEmail());
            mapRecords.put("EQ_userName",user.getUsername());
            List<Records> query1 = recordsService.query(mapRecords, null);
            float spendingReduce = getSpendingList(query1);
            List<Transactionss> transactionsList = transactionsService.query(mapRecords, null);
            for (Transactionss entity : transactionsList) {
                spendingReduce+=entity.getAmount();
            }
            DecimalFormat decimalFormat = new DecimalFormat("#.00");
            userSpending.setSpending(decimalFormat.format(spendingReduce));
            userSpendingList.add(userSpending);
        }
        Pagination pagination = new Pagination(userSpendingList.size(), page, rows, userSpendingList);
        pagination.calculatePagination();
        result.setPageNo(pagination.getCurrentPage());
        result.setTotal(Long.valueOf(pagination.getTotalItems()));
        result.setPageSize(pagination.getItemsPerPage());
        result.setRows(pagination.getCurrentPageItems());
        return result;
    }

    private float getSpendingList(List<Records> listMap){
        float reduce = 0;
        for (Records records : listMap) {
            Map<String, Float> rechargeToMap = stringRechargeToMap(records.getRecharge());
            reduce += rechargeToMap.values().stream().reduce(0f, Float::sum);
        }
        return reduce;
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


    @RequestMapping(value = "saveList")
    @ResponseBody
    public JsonResult transactionsSaveList(HttpServletRequest request, HttpServletResponse response) {
        JsonResult jsonResult = new JsonResult();
        try {
            String id = request.getParameter("id");
            String inAccount = request.getParameter("inAccount");
            String amount = request.getParameter("amount");
            String accountManager = request.getParameter("accountManager");
            Accounts accounts = accountsService.get(Long.valueOf(id));
            //减去老账户的金额
            accounts.setBalance(accounts.getBalance() - Float.parseFloat(amount));
            accountsService.update(accounts);
            //转入账户加上金额
            Map<String, Object> mapQuery = Maps.newHashMap();
            mapQuery.put("EQ_accountName", inAccount);
            Accounts accountsOut = accountsService.query(mapQuery, null).get(0);
            accountsOut.setBalance(accountsOut.getBalance() + Float.parseFloat(amount));
            accountsService.update(accountsOut);
            //添加记录
            User principal = (User) SecurityUtils.getSubject().getPrincipal();
            Transactionss transactions = new Transactionss();
            transactions.setOutAccount(accounts.getAccountName());
            transactions.setInAccount(inAccount);
            transactions.setAmount(Float.parseFloat(amount));
            transactions.setAccountManager(principal.getUsername());
            transactions.setUserName(accountManager);
            transactionsService.save(transactions);
        } catch (BusinessException e) {
            String message = e.message();
            jsonResult.setMessage(message);
            jsonResult.setSuccess(false);
        }
        return jsonResult;
    }






    @Override
    protected Accounts onSave(HttpServletRequest request, HttpServletResponse response, Model model, Accounts entity, boolean isCreate) throws Exception {
        if (isCreate) {
            String accountName = entity.getAccountName();
//            String replace = accountName.replace("-", "_");
            String replace = accountName.replaceAll("[\\+\\-]", "_");
            entity.setAccountName(replace);
        } else {
//            Accounts accounts = this.getEntityService().get(entity.getId());
//            Map<String, Object> mapRecords = Maps.newHashMap();
//            mapRecords.put("EQ_userName",accounts.getHolder());
//            List<Records> query1 = recordsService.query(mapRecords, null);
//            for (Records records : query1) {
//                records.setUserName(entity.getHolder());
//                recordsService.update(records);
//            }
            if (entity.getStatus() == 0) {
                entity.setDeprecatedTime(new Date());
            }
        }
        return super.onSave(request, response, model, entity, isCreate);
    }


    @Override
    protected Accounts doSave(HttpServletRequest request, HttpServletResponse response, Model model, boolean isCreate) throws Exception {
        return super.doSave(request, response, model, isCreate);
    }

}
