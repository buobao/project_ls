package com.vocinno.centanet.model;

public class CustomerItem {
	private String custCode = ""; //客户编号
	private String name ="";	 //姓名
	private String reqType = "";	 //需求类型
	public static final String GOU="20037001";
	public static final String ZU="20037002";
	private String districtCode = "";	 //区域
	private String area = "";	 //片区
	private String acreage = "";	 //面积
	private String price = "";	 //价格区间
	private String other = "";	 //其他
	private String relativeDate = "";//相对日期
	private String frame = "";	 //户型
	private String fromToRoom  = "";	 //户型
	private String custReqType = "";

	public String getCustReqType() {
		return custReqType;
	}

	public void setCustReqType(String custReqType) {
		this.custReqType = custReqType;
	}

	public String getFromToRoom() {
		return fromToRoom;
	}

	public String getDistrictCode() {
		return districtCode;
	}

	public void setDistrictCode(String districtCode) {
		this.districtCode = districtCode;
	}

	public void setFromToRoom(String fromToRoom) {
		this.fromToRoom = fromToRoom;
	}

	public String getCustCode() {
		return custCode;
	}
	public void setCustCode(String custCode) {
		this.custCode = custCode;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getReqType() {
		return reqType;
	}
	public void setReqType(String reqType) {
		this.reqType = reqType;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public String getAcreage() {
		return acreage;
	}
	public void setAcreage(String acreage) {
		this.acreage = acreage;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getOther() {
		return other;
	}
	public void setOther(String other) {
		this.other = other;
	}
	public String getRelativeDate() {
		return relativeDate;
	}
	public void setRelativeDate(String relativeDate) {
		this.relativeDate = relativeDate;
	}
	public String getFrame() {
		return frame;
	}
	public void setFrame(String frame) {
		this.frame = frame;
	}
}
