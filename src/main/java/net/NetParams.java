package net;

public class NetParams {

	public static final String NET_ERROR = "net error";

	public enum HttpMethod {
		Post,
		Get
	}

	public static final String CHARSET = "utf-8";
	public static final String POST = "POST";
	public static final String KEEPALIVE = "Keep-Alive";
	public static final int TIME_OUT = 30 * 1000; // 超时时间
}
