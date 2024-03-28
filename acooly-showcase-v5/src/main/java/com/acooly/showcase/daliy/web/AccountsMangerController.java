
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
import com.acooly.showcase.daliy.Utils.FTPTools;
import com.acooly.showcase.daliy.Utils.FTPUploader;
import com.acooly.showcase.daliy.Utils.FtpUtils;
import com.acooly.showcase.daliy.dto.PerformanceDto;
import com.acooly.showcase.daliy.dto.TotalFromDto;
import com.acooly.showcase.daliy.dto.UserSpending;
import com.acooly.showcase.daliy.entity.*;
import com.acooly.showcase.daliy.service.*;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baidu.aip.util.Base64Util;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.Result;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Paths;
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
    @Autowired
    private RegnameService regnameService;
    @Autowired
    private LinkService linkService;


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
        list1.add("养户");
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


    @RequestMapping("file")
    public JsonResult upload(HttpServletRequest request, MultipartFile file) throws Exception {
        JsonResult jsonResult = new JsonResult();
        String domain = request.getParameter("domain");
        User principal = (User) SecurityUtils.getSubject().getPrincipal();
        Map<String, Object> map = Maps.newHashMap();
        map.put("EQ_userId", principal.getId());
        map.put("EQ_name", principal.getUsername());
        List<Regname> query = regnameService.query(map, null);
        String regionName = query.get(0).getRegionName();
        regionName = regionName + "/" + domain;
        String ftpUp = FTPUploader.ftpUp(file.getInputStream(), regionName);
        if (ftpUp.equals("文件上传成功")) {
            jsonResult.setSuccess(true);
        } else {
            jsonResult.setSuccess(false);
        }
        jsonResult.setMessage(ftpUp);
        return jsonResult;
    }

    @RequestMapping("fileUpdate")
    @ResponseBody
    public JsonResult fileUpdate(HttpServletRequest request, MultipartFile file)  {
        JsonResult result = new JsonResult();
        String domain = request.getParameter("domain1");
        if (StringUtils.isEmpty(domain)) {
            result.setSuccess(false);
            result.setMessage("域名不能为空");
            return result;
        }
        String regionName = request.getParameter("regname1");
        if (StringUtils.isEmpty(regionName)) {
            result.setSuccess(false);
            result.setMessage("域名不能为空");
            return result;
        }
        String ftpUrl = regionName + "/" + domain;
        if (FTPUploader.updateLinkNotLocal(request.getParameter("oldLink"), request.getParameter("newLink"), ftpUrl)){
            result.setMessage("链接修改完成");
        }else {
            result.setMessage("链接修改失败");
            result.setSuccess(false);
        }
        return result;
    }

    @RequestMapping("filePh")
    @ResponseBody
    public JsonResult filePh(HttpServletRequest request, MultipartFile file) throws Exception {
        JsonResult jsonResult = new JsonResult();
        try {
            byte[] bytes = file.getBytes();
            String imgStr = Base64Util.encode(bytes);
            // 请求url
            String url = "https://aip.baidubce.com/rest/2.0/ocr/v1/numbers";
            String imgParam = URLEncoder.encode(imgStr, "UTF-8");
            String param = "image=" + imgParam;
            // 注意这里仅为了简化编码每一次请求都去获取access_token，线上环境access_token有过期时间， 客户端可自行缓存，过期后重新获取。
            String accessToken = "24.a35efca5336cbe04e8a8009375183fea.2592000.1712137983.282335-55093663";
            String result = com.acooly.showcase.daliy.Utils.HttpUtil.post(url, accessToken, param);
            JSONObject jsonObject = JSON.parseObject(result);
            JSONArray wordsResult = jsonObject.getJSONArray("words_result");
            List<String> wordsList = wordsResult.stream()
                    .map(obj -> ((JSONObject) obj).getString("words"))
                    .collect(Collectors.toList());
            Map<Object, Object> map = Maps.newHashMap();
            map.put("list",wordsList);
            jsonResult.setData(map);
            jsonResult.setMessage("识别成功");
        } catch (Exception e) {
            jsonResult.setSuccess(false);
            jsonResult.setMessage(e.getMessage());
            jsonResult.setCode("500");
        }
        return jsonResult;
    }

    @RequestMapping("file1")
    @ResponseBody
    public JsonResult upload1(HttpServletRequest request, MultipartFile file) throws Exception {
        JsonResult jsonResult = new JsonResult();
        String domain = request.getParameter("domain");
        if (StringUtils.isEmpty(domain)) {
            jsonResult.setSuccess(false);
            jsonResult.setMessage("域名不能为空");
            return jsonResult;
        }
        String regionName = request.getParameter("regname");
        if (StringUtils.isEmpty(regionName)) {
            jsonResult.setSuccess(false);
            jsonResult.setMessage("域名不能为空");
            return jsonResult;
        }
        String ftpUrl = regionName + "/" + domain;
        try (InputStream inputStream = file.getInputStream()) {
            String ftpUp = FTPUploader.ftpUp(inputStream, ftpUrl);
            if (ftpUp.equals("文件上传成功")) {
                jsonResult.setSuccess(true);
            } else {
                jsonResult.setSuccess(false);
            }
            jsonResult.setMessage(ftpUp);
        } catch (IOException e) {
            jsonResult.setSuccess(false);
            jsonResult.setMessage("文件上传失败：" + e.getMessage());
        }
        return jsonResult;
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
        map.put("EQ_inAccount", accountName);
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
        map.put("EQ_outAccount", accountName);
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
        map.put("balance", format);
        model.addAllAttributes(map);
        return "/manage/showcase/daily/transferOut";
    }

    @RequestMapping(value = "linkFile")
    public String linkFile(Model model, HttpServletRequest request, HttpServletResponse response) {
        List<Link> query;
        Map<String, Object> map = Maps.newHashMap();
        User principal = (User) SecurityUtils.getSubject().getPrincipal();
        Map<String, Object> mapQuery1 = Maps.newHashMap();
        mapQuery1.put("EQ_userName", principal.getUsername());
        if (!(permissionsService.query(mapQuery1, null).size() > 0)) {
            Map<String, Object> map1 = Maps.newHashMap();
            map1.put("EQ_userId", principal.getId());
            map1.put("EQ_holder", principal.getUsername());
            query = linkService.query(map1, null);
        } else {
            query = linkService.getAll();
        }
        List<String> listRegionName = query.stream().map(Link::getRegionName).distinct().collect(Collectors.toList());
        Map<String, List<String>> mapDomain = query.stream()
                .collect(Collectors.groupingBy(Link::getRegionName,
                        Collectors.mapping(Link::getDomain, Collectors.toList())));
        map.put("domainList", listRegionName);
        map.put("mapDomain", mapDomain);
        model.addAllAttributes(map);
        return "/manage/showcase/daily/linkFile";
    }


    @RequestMapping(value = "linkUpdate")
    public String linkUpdate(Model model, HttpServletRequest request, HttpServletResponse response) {
        List<Link> query;
        Map<String, Object> map = Maps.newHashMap();
        User principal = (User) SecurityUtils.getSubject().getPrincipal();
        Map<String, Object> mapQuery1 = Maps.newHashMap();
        mapQuery1.put("EQ_userName", principal.getUsername());
        if (!(permissionsService.query(mapQuery1, null).size() > 0)) {
            Map<String, Object> map1 = Maps.newHashMap();
            map1.put("EQ_userId", principal.getId());
            map1.put("EQ_holder", principal.getUsername());
            query = linkService.query(map1, null);
        } else {
            query = linkService.getAll();
        }
        List<String> listRegionName = query.stream().map(Link::getRegionName).distinct().collect(Collectors.toList());
        Map<String, List<String>> mapDomain = query.stream()
                .collect(Collectors.groupingBy(Link::getRegionName,
                        Collectors.mapping(Link::getDomain, Collectors.toList())));
        map.put("domainList1", listRegionName);
        map.put("mapDomain1", mapDomain);
        model.addAllAttributes(map);
        return "/manage/showcase/daily/linkUpdate";
    }




    @RequestMapping(value = "totalParseList")
    @ResponseBody
    public JsonResult totalParseList(HttpServletRequest request, HttpServletResponse response) {
        JsonResult jsonResult = new JsonResult();
        List<String> resultList = null;
        String phoneNumber = request.getParameter("phoneNumber");
        if (!phoneNumber.startsWith("+")){
            jsonResult.setSuccess(false);
            jsonResult.setMessage("输入的电话号格式错误");
            return jsonResult;
        }
        resultList = new ArrayList<>();
        try {
            String[] parts = phoneNumber.split("\\+");
            for (String part : parts) {
                part = StringEscapeUtils.unescapeHtml4(part);
                String cleanedPart = part.replace("(", "").replace(")", "").replace("-", "").replace(" ", "");
                ;
                resultList.add(cleanedPart);
            }
        } catch (Exception e) {
            e.printStackTrace();
            jsonResult.setSuccess(false);
            jsonResult.setMessage("输入的电话号格式错误");
            return jsonResult;
        }
        Map<Object, Object> map = Maps.newHashMap();
        map.put("list", resultList.subList(1, resultList.size()));
        jsonResult.setData(map);
        return jsonResult;
    }

    @RequestMapping(value = "totalParseList1")
    @ResponseBody
    public JsonResult totalParseList1(HttpServletRequest request, HttpServletResponse response) {
        JsonResult jsonResult = new JsonResult();
        String phoneNumber = request.getParameter("phoneNumber");
        if (phoneNumber.startsWith("+")){
            jsonResult.setSuccess(false);
            jsonResult.setMessage("输入的电话号格式错误");
            return jsonResult;
        }
        List<String> lines = null;
        try {
            phoneNumber= phoneNumber.replace("\r\n", "");
            int step = 11;
            int length = phoneNumber.length();
            int count = (length + step - 1) / step;
            lines = new ArrayList<>();
            for (int i = 0; i < count; i++) {
                int startIndex = i * step;
                int endIndex = Math.min(startIndex + step, length);
                StringBuilder sb = new StringBuilder();
                for (int j = startIndex; j < endIndex; j++) {
                    sb.append(phoneNumber.charAt(j));
                }
                lines.add(FTPUploader.formatPhoneNumber(sb.toString()));
            }
        } catch (Exception e) {
            jsonResult.setSuccess(false);
            jsonResult.setMessage("输入的电话号格式错误");
            return jsonResult;
        }
        Map<Object, Object> map = Maps.newHashMap();
        map.put("list",lines);
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
            mapRecords.put("EQ_userName", user.getUsername());
            List<Records> query1 = recordsService.query(mapRecords, null);
            float spendingReduce = getSpendingList(query1);
            List<Transactionss> transactionsList = transactionsService.query(mapRecords, null);
            for (Transactionss entity : transactionsList) {
                spendingReduce += entity.getAmount();
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

    private float getSpendingList(List<Records> listMap) {
        float reduce = 0;
        for (Records records : listMap) {
            Map<String, Float> rechargeToMap = stringRechargeToMap(records.getRecharge());
            reduce += rechargeToMap.values().stream().reduce(0f, Float::sum);
        }
        return reduce;
    }

    private Map<String, Float> stringRechargeToMap(String s) {
        List<String> accountName = Arrays.asList(s.split(","));
        Map<String, Float> map = Maps.newHashMap();
        for (String str : accountName) {
            int i = str.indexOf(':');
            if (i != -1) {
                int leftBracket = str.indexOf("[");
                int rightBracket = str.indexOf("]");
                String numStr = str.substring(leftBracket + 1, rightBracket);
                map.put(str.substring(0, i - 2), Float.parseFloat(numStr));
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
