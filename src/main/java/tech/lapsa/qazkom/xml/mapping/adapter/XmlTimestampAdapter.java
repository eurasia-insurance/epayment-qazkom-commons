package tech.lapsa.qazkom.xml.adapter;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class XmlTimestampAdapter extends XmlAdapter<String, Instant> {

    private static final String KKB_TIMESTAMP_PATTERN = "yyyy-MM-dd HH:mm:ss";
    private static final DateTimeFormatter KKB_TIMESTAMP_FORMATTER = DateTimeFormatter.ofPattern(KKB_TIMESTAMP_PATTERN);
    private static final ZoneId KKB_ZONE = ZoneId.of("Asia/Almaty");

    @Override
    public Instant unmarshal(String v) throws Exception {
	if (v == null)
	    return null;
	return LocalDateTime.parse(v, KKB_TIMESTAMP_FORMATTER).atZone(KKB_ZONE).toInstant();
    }

    @Override
    public String marshal(Instant v) throws Exception {
	if (v == null)
	    return null;
	return v.atZone(KKB_ZONE).format(KKB_TIMESTAMP_FORMATTER);
    }
}
