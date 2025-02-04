package com.acooly.showcase.daliy.web;


import com.acooly.core.common.exception.BusinessException;
import com.acooly.core.common.web.support.JsonResult;
import com.acooly.showcase.base.AbstractShowcaseController;
import com.acooly.showcase.daliy.entity.Phone;
import com.acooly.showcase.daliy.service.PhoneService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/manage/showcase/daily/phone")
public class PhoneMangerController extends AbstractShowcaseController<Phone, PhoneService> {
    {
        allowMapping = "*";
    }

    @RequestMapping({"createTest"})
    public String create(HttpServletRequest request, HttpServletResponse response, Model model) {
        return StringUtils.isNotBlank(this.editView) ? this.editView : this.getRequestMapperValue() + "File";
    }

    @Override
    protected List<Object> doExportRow(Phone entity) {
        return Lists.newArrayList(entity.getGroupName(),entity.getPhoneNumber(),entity.getRepeatCount());
    }


    @Override
    public List<String> getExportTitles() {
        return Lists.newArrayList( "粉丝群", "电话号","出现数");
    }

    @Override
    protected void referenceData(HttpServletRequest request, Map<String, Object> model) {
        List<String> groupName = this.getEntityService().getGroupName();
        model.put("groupName",groupName);
    }


    @RequestMapping(value = "phoneFile")
    @ResponseBody
    @Transactional(rollbackFor = Exception.class)
    public JsonResult phoneFile(HttpServletRequest request, HttpServletResponse response){
        JsonResult jsonResult = new JsonResult();
        String phoneNumber = request.getParameter("commentsFile");
        String groupNumber = request.getParameter("groupNumber");
        List<Phone> phoneList = null;
        try {
            String[] parts = phoneNumber.split(",");
            List<String> list1 = new ArrayList<>(Arrays.asList(parts));
            phoneList = new ArrayList<>();
            Map<String, Object> mapQuery = Maps.newHashMap();
            List<String> list = new ArrayList<>();
            for (String number : parts) {
                String deleteWhitespace = StringUtils.deleteWhitespace(number);
                mapQuery.put("EQ_phoneNumber",deleteWhitespace);
                list.add(deleteWhitespace);
                List<Phone> query = this.getEntityService().query(mapQuery, null);
                int i = 1;
                Phone phone1 = new Phone();
                if (!query.isEmpty()){
                    for (Phone phone : query) {
                        i = phone.getRepeatCount() + 1;
                        phone.setRepeatCount(i);
                        this.getEntityService().update(phone);
                    }
                    phone1.setRepeatCount(i);
                }else {
                    phone1.setRepeatCount(1);
                }
                phone1.setGroupName(groupNumber);
                phone1.setPhoneNumber(deleteWhitespace);
                phoneList.add(phone1);
            }
            if (!(phoneList.size()>0)){
                jsonResult.setSuccess(false);
            }
        } catch (BusinessException e) {
            String message = e.getMessage();
            jsonResult.setMessage(message);
            jsonResult.setSuccess(false);
        }
        this.getEntityService().saves(phoneList);
        return jsonResult;
    }



    @RequestMapping(value = "phoneParse")
    @ResponseBody
    @Transactional(rollbackFor = Exception.class)
    public JsonResult phoneParse(HttpServletRequest request, HttpServletResponse response){
        JsonResult jsonResult = new JsonResult();
        String phoneNumber = request.getParameter("phoneNumber");
        String groupNumber = request.getParameter("groupNumber");
        List<Phone> phoneList = new ArrayList<>();
        Map<String, Object> mapQuery = Maps.newHashMap();
        String[] parts = phoneNumber.split("\\+");
        boolean isFirst = true;
        try {
            for (String part : parts) {
                if (isFirst) {
                    isFirst = false;
                    continue;
                }
                part= StringEscapeUtils.unescapeHtml4(part);
                String cleanedPart = part.replace("(", "").replace(")", "").replace("-", "").replace(" ", "");
                mapQuery.put("EQ_phoneNumber",cleanedPart);
                List<Phone> query = this.getEntityService().query(mapQuery, null);
                int i = 1;
                Phone phone1 = new Phone();
                if (query.size()>0){
                    for (Phone phone : query) {
                        i = phone.getRepeatCount() + 1;
                        phone.setRepeatCount(i);
                        this.getEntityService().update(phone);
                    }
                    phone1.setRepeatCount(i);
                }else {
                    phone1.setRepeatCount(1);
                }
                phone1.setGroupName(groupNumber);
                phone1.setPhoneNumber(cleanedPart);
                phoneList.add(phone1);
            }
            if (!(phoneList.size()>0)){
                jsonResult.setSuccess(false);
            }
            this.getEntityService().saves(phoneList);
        } catch (Exception e) {
            String message = e.getMessage();
            jsonResult.setMessage(message);
            jsonResult.setSuccess(false);
        }
        return jsonResult;
    }
}
