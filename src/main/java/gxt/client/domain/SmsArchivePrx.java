package gxt.client.domain;

import java.math.BigDecimal;
import java.util.Date;

import jpa.other.Sms_archive;

import com.google.web.bindery.requestfactory.shared.EntityProxy;
import com.google.web.bindery.requestfactory.shared.ProxyFor;

@ProxyFor(value = Sms_archive.class, locator = gxt.server.domain.SmsArchiveLoc.class)
public interface SmsArchivePrx extends EntityProxy{
	public long getId();
//	public Date getAmndDate();
//	public Date getDeliveryTime();
//	public BigDecimal getDocId();
//	public String getExtId();
//	public String getGroupId();
//	public BigDecimal getIntId();
//	public String getIsFinal();
//	public String getMsgText();
//	public Date getMsgTime();
//	public String getMsgType();
//	public byte[] getMsgUid();
//	public String getMsgUid();
//	public String getPhoneNumber();
//	public BigDecimal getPriority();
//	public String getProvider();
//	public BigDecimal getPushId();
//	public Date getReceiveTime();
	public String getRespClass();
	public String getRespText();
//	public byte[] getRespUid();
	public String getRespUid();
//	public Date getSendTime();
//	public String getSender();
//	public BigDecimal getServerDelay();
//	public BigDecimal getSmsCount();
//	public String getSourceNumber();
//	public String getXml();
	public String getNormalizednumber();
	
	public String getStrReceiveTime();
	public String getStrDeliveryTime();
	public String getStrSendTime();
	public String getSecurMess();
	Integer getVersion();
}
