/*
* acooly.cn Inc.
* Copyright (c) 2025 All Rights Reserved.
* create by acooly
* date:2025-02-15
*/
package com.acooly.showcase.stemp.web;

import java.awt.*;
import java.awt.Color;
import java.io.Closeable;
import java.io.OutputStream;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.math.BigInteger;
import java.util.*;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.acooly.core.common.dao.support.PageInfo;
import com.acooly.core.common.exception.BusinessException;
import com.acooly.core.common.web.AbstractStandardEntityController;
import com.acooly.core.common.web.MappingMethod;
import com.acooly.core.common.web.support.JsonListResult;
import com.acooly.core.common.web.support.JsonResult;
import com.acooly.core.utils.Strings;
import com.acooly.core.utils.ie.ExportModelMeta;
import com.acooly.core.utils.ie.ExportStyleMeta;
import com.acooly.core.utils.io.Streams;
import com.acooly.showcase.stemp.enums.FieldEnum;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.acooly.core.common.web.AbstractJsonEntityController;
import com.acooly.showcase.stemp.entity.EmSumdata;
import com.acooly.showcase.stemp.service.EmSumdataService;

import com.google.common.collect.Maps;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * em_sumdata 管理控制器
 *
 * @author acooly
 * @date 2025-02-15 14:50:19
 */
@Controller
@RequestMapping(value = "/manage/stemp/emSumdata")
public class EmSumdataManagerController extends AbstractJsonEntityController<EmSumdata, EmSumdataService> {

	private static final Logger logger = LoggerFactory.getLogger(AbstractStandardEntityController.class);
	private static ThreadLocal<ExportModelMeta> exportResultLocal1 = new ThreadLocal();

	{
		allowMapping = "*";
	}

	@SuppressWarnings("unused")
	@Autowired
	private EmSumdataService emSumdataService;






	@Override
	public String create(HttpServletRequest request, HttpServletResponse response, Model model) {


		this.allow(request, response, MappingMethod.create);

		try {
			model.addAllAttributes(this.referenceData(request));
			this.onCreate(request, response, model);
			String gather = request.getParameter("gather");
			String stampId= request.getParameter("stampId");

			// 将 gather 字符串以逗号分割形成列表
			List<String> gatherList = Arrays.asList(gather.split(","));
			// 将列表添加到模型中以供视图使用
			model.addAttribute("gatherList", gatherList);
			model.addAttribute("stampId", stampId);
			model.addAttribute("action", "create");
		} catch (Exception var5) {
			logger.warn(this.getExceptionMessage("create", var5), var5);
			this.handleException("新建", var5, request);
		}
		return this.getEditView();
	}


	@Override
	public String edit(HttpServletRequest request, HttpServletResponse response, Model model) {
		this.allow(request, response, MappingMethod.update);

		try {
			model.addAllAttributes(this.referenceData(request));
			EmSumdata entity = this.loadEntity(request);

			String gather = request.getParameter("gather");
			String stampId= request.getParameter("stampId");
			List<String> gatherList = gather != null ? Arrays.asList(gather.split(",")) : new ArrayList<>();
			model.addAttribute("gatherList", gatherList);
			model.addAttribute("action", "edit");
			model.addAttribute("stampId", stampId);
			model.addAttribute(this.getEntityName(), entity);
			this.onEdit(request, response, model, entity);
		} catch (Exception var5) {
			logger.warn(this.getExceptionMessage("edit", var5), var5);
			this.handleException("编辑", var5, request);
		}

		return this.getEditView();
	}

	@Override
	public String importView(Model model, HttpServletRequest request, HttpServletResponse response) {
		try {
			Map<String, Object> map = this.referenceData(request);
			map.put("stampId", request.getParameter("stampId"));
			model.addAllAttributes(map);
		} catch (Exception var5) {
			logger.warn(this.getExceptionMessage("importView", var5), var5);
			this.handleException("导入界面", var5, request);
		}

		return this.getImportView();
	}


	//表格导出

	@Override
	protected void doExportExcelBody(HttpServletRequest request, HttpServletResponse response, int batchSize) {
		SXSSFWorkbook workbook = null;
		OutputStream out = null;

		try {
			List<String> headerNames = this.getExportTitles();
			workbook = new SXSSFWorkbook(batchSize);
			workbook.setCompressTempFiles(true);
			ExportModelMeta exportModelMeta = this.getExportTResult();
			Sheet sheet = workbook.createSheet();
			int rowNum = 0;
			Row row = sheet.createRow(rowNum);
			if (exportModelMeta != null && exportModelMeta.getHeaderStyleMeta() != null && exportModelMeta.getHeaderStyleMeta().getRowHeight() != -1) {
				row.setHeight(exportModelMeta.getHeaderStyleMeta().getRowHeight());
			}
			PageInfo<EmSumdata> pageInfo = new PageInfo(batchSize, 1);
			pageInfo = this.getEntityService().query(pageInfo, this.getSearchParams(request), this.getSortMap(request));
			pageInfo.getPageResults().removeIf(emSumdata -> emSumdata.getIsDelete() == 1);
			List<Integer> validColumns = new ArrayList<>();
			if (headerNames != null) {
				commonExcelTCellStyle(sheet);
				CellStyle headerStyle = excelHeaderCellRStyle(sheet);

				// 动态生成表头，并记录需要保留的列索引
				for (int cellnum = 0; cellnum < headerNames.size(); ++cellnum) {
					// 跳过 id 和 stampId 列
					if (headerNames.get(cellnum).equalsIgnoreCase("id") ||
							headerNames.get(cellnum).equalsIgnoreCase("stampId") ||
							headerNames.get(cellnum).equalsIgnoreCase("类型表ID")) {
						continue;
					}

					// 检查该列是否有值
					boolean hasValue = false;//过滤IsDelete=1的数据
					for (EmSumdata entity : pageInfo.getPageResults()) {
						List<Object> rowData = this.doExportRow(entity);
						if (rowData.get(cellnum) != null && !rowData.get(cellnum).toString().isEmpty()) {
							hasValue = true;
							break;
						}
					}

					if (hasValue) {
						row.createCell(validColumns.size()).setCellValue(headerNames.get(cellnum));
						if (headerStyle != null) {
							row.getCell(validColumns.size()).setCellStyle(headerStyle);
						}
						updateExcelColumnWidthR(validColumns.size(), headerNames.get(cellnum), sheet);
						validColumns.add(cellnum);
					}
				}

				++rowNum;
			}

//			PageInfo<EmSumdata> pageInfo = new PageInfo(batchSize, 1);
//			pageInfo = this.getEntityService().query(pageInfo, this.getSearchParams(request), this.getSortMap(request));
			rowNum = this.doExportExcelPage1(pageInfo.getPageResults(), rowNum, sheet, validColumns);
			long totalPage = pageInfo.getTotalPage();
			if (totalPage > 1L) {
				for (int i = 2; (long) i <= totalPage; ++i) {
					pageInfo.setCurrentPage(i);
					pageInfo = this.getEntityService().query(pageInfo, this.getSearchParams(request), this.getSortMap(request));
					rowNum = this.doExportExcelPage1(pageInfo.getPageResults(), rowNum, sheet, validColumns);
				}
			}

			out = response.getOutputStream();
			workbook.write(out);
			out.flush();
		} catch (Exception var18) {
			logger.warn("do export excel failure -> " + var18.getMessage(), var18);
			throw new BusinessException("FILE_EXPORT_EXCEL_FAIL", "EXCEL文件输出失败", var18.getMessage());
		} finally {
			Streams.close(new Closeable[]{out});
			Streams.close(new Closeable[]{workbook});
			workbook.dispose();
		}
	}


	// 修改后的 doExportExcelPage 方法，只导出 validColumns 中的列
	private int doExportExcelPage1(List<EmSumdata> pageResults, int rowNum, Sheet sheet, List<Integer> validColumns) {
		for (EmSumdata entity : pageResults) {
			Row row = sheet.createRow(rowNum++);
			List<Object> rowData = this.doExportRow(entity);
			for (int i = 0; i < validColumns.size(); i++) {
				int colIndex = validColumns.get(i);
				Object value = rowData.get(colIndex);
				row.createCell(i).setCellValue(value != null ? value.toString() : "");
			}
		}
		return rowNum;
	}
	private ExportModelMeta getExportTResult() {
		return (ExportModelMeta)exportResultLocal1.get();
	}


	private void updateExcelColumnWidthR(int colNum, String value, Sheet sheet) throws Exception {
		int cellColumnWidth = (value.getBytes("UTF-8").length + 1) * 256;
		if (cellColumnWidth > sheet.getColumnWidth(colNum)) {
			if (cellColumnWidth <= 65280) {
				sheet.setColumnWidth(colNum, cellColumnWidth);
			} else {
				sheet.setColumnWidth(colNum, 25500);
			}
		}

	}


	private CellStyle excelHeaderCellRStyle(Sheet sheet) {
		ExportModelMeta exportModelMeta = getExportTResult();
		if (exportModelMeta == null) {
			return null;
		} else {
			XSSFCellStyle cellStyle = (XSSFCellStyle)sheet.getWorkbook().createCellStyle();
			cellStyle.setWrapText(true);
			if (exportModelMeta.isBorder()) {
				cellStyle.setBorderTop(BorderStyle.THIN);
				cellStyle.setBorderBottom(BorderStyle.THIN);
				cellStyle.setBorderLeft(BorderStyle.THIN);
				cellStyle.setBorderRight(BorderStyle.THIN);
			}

			ExportStyleMeta headerStyleMeta = exportModelMeta.getHeaderStyleMeta();
			if (headerStyleMeta != null) {
				if (headerStyleMeta.requireFont()) {
					XSSFFont font = (XSSFFont)sheet.getWorkbook().createFont();
					if (Strings.isNotBlank(headerStyleMeta.getFontName())) {
						font.setFontName(headerStyleMeta.getFontName());
					}

					if (headerStyleMeta.getFontSize() != -1) {
						font.setFontHeightInPoints(headerStyleMeta.getFontSize());
					}

					font.setBold(headerStyleMeta.isFontBold());
					cellStyle.setFont(font);
				}

				if (Strings.isNotBlank(headerStyleMeta.getBackgroundColor())) {
					cellStyle.setFillForegroundColor(new XSSFColor(Color.decode(headerStyleMeta.getBackgroundColor())));
					cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
				}
			}

			return cellStyle;
		}
	}

	private CellStyle commonExcelTCellStyle(Sheet sheet) {
		ExportModelMeta exportModelMeta = getExportTResult();
		if (exportModelMeta == null) {
			return null;
		} else {
			if (exportModelMeta.getStyle() == null) {
				CellStyle cellStyle = sheet.getWorkbook().createCellStyle();
				cellStyle.setWrapText(true);
				if (exportModelMeta.isBorder()) {
					cellStyle.setBorderTop(BorderStyle.THIN);
					cellStyle.setBorderBottom(BorderStyle.THIN);
					cellStyle.setBorderLeft(BorderStyle.THIN);
					cellStyle.setBorderRight(BorderStyle.THIN);
				}

				exportModelMeta.setStyle(cellStyle);
			}

			return (CellStyle)exportModelMeta.getStyle();
		}
	}




	//逻辑删除






	@Override
	public JsonResult deleteJson(HttpServletRequest request, HttpServletResponse response) {
		JsonResult result = new JsonResult();
		this.allow(request, response, MappingMethod.delete);

		try {
			Serializable[] ids = this.getRequestIds(request);
			for (Serializable id : ids) {
				EmSumdata emSumdata = this.getEntityService().get(id);
				emSumdata.setIsDelete(1);
				this.getEntityService().update(emSumdata);
			}
			result.setMessage("删除成功");
		} catch (Exception var5) {
			this.handleException(result, "删除", var5);
		}

		return result;
	}


	@Override
	protected EmSumdata onSave(HttpServletRequest request, HttpServletResponse response, Model model, EmSumdata entity, boolean isCreate) throws Exception {
		if (isCreate){
			entity.setIsDelete(0);
		}
		return super.onSave(request, response, model, entity, isCreate);
	}


	//动态导入

	@Override
	protected List<EmSumdata> unmarshal(List<List<String>> lines, HttpServletRequest request) {
		List< EmSumdata> entities = new LinkedList<>();

		if (lines.isEmpty()) return entities;

		// 获取表头行（中文名称）
		List<String> headers = lines.get(0);
		String stampId = request.getParameter("stampId");

		// 从第二行开始遍历数据行
		for (int i = 1; i < lines.size(); i++) {
			List<String> row = lines.get(i);

			// 1. 手动创建实体实例（绕过可能出问题的doImportEntity）
			EmSumdata entity = new EmSumdata();

			try {
				// 2. 设置stampId（需类型安全转换）
				if (stampId != null) {
					entity.setStampId(new BigInteger(stampId));
				}

				// 3. 动态匹配字段赋值
				for (int col = 0; col < headers.size() && col < row.size(); col++) {
					String chineseHeader = headers.get(col);
					String fieldName = FieldEnum.getFieldNameByChineseName(chineseHeader);

					if (fieldName != null) {
						Field field = entity.getClass().getDeclaredField(fieldName);
						field.setAccessible(true); // 突破私有限制

						// 4. 类型安全设置值（示例为String类型）
						String value = row.get(col);
						if (value != null) {
							field.set(entity, value);
						}
					}
				}
				entity.setIsDelete(0);
				entities.add(entity);
			} catch (Exception e) {
				// 5. 异常处理（记录错误行）
				logger.error("第{}行数据解析失败: {}", i+1, e.getMessage());
			}
		}
		return entities;
	}

}
