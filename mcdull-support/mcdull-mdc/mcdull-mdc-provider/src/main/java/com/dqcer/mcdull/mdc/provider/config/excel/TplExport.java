package com.dqcer.mcdull.mdc.provider.config.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentFontStyle;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.ContentStyle;
import com.alibaba.excel.annotation.write.style.HeadFontStyle;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;

import java.util.Date;

/**
 * excel 导入模板
 *
 * @author dongqin
 * @date 2022/11/20 22:11:23
 */
@ContentStyle // 自动换行
@ContentRowHeight(10) // 内容高
@HeadRowHeight(25) // 行高
@ColumnWidth(15) // 列宽
@HeadFontStyle(fontHeightInPoints = 12) // 字体
@ContentFontStyle(fontHeightInPoints = 16) // 字体
public class TplExport {

	@DropDownSetField(SelectTypeEnum.STATUS)
	@ExcelProperty(value = { "demo模板", "数据状态" }, index = 0)
	private String name;

	@ExcelLength(min = 1, max = 30)
	@ExcelProperty(value = { "demo模板", "编号" }, index = 1)
	private String number;

	@ColumnWidth(27)
	@ExcelProperty(value = { "demo模板", "版本号" }, index = 2)
	private String version;

	@ColumnWidth(27)
	@DateTimeFormat("yyyy-MM-dd")
	@ExcelProperty(value = { "demo模板", "日期" }, index = 3)
	private Date time;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}
}
