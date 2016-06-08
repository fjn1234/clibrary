package net;

public class NetParams {

	public static final String NET_ERROR = "net_error";
	public static final String CONNECT_FAIL = "connect_fail";
	public static final String OPERATE_SUCCESS = "operate_success";
	public static final String OPERATE_FAIL = "operate_fail";
	public static final String RESPONSE_ERROR = "response_error";

	public enum HttpMethod {
		Post,
		Get
	}

	public static final String CHARSET = "utf-8";
	public static final String POST = "POST";
	public static final String KEEPALIVE = "Keep-Alive";
	public static final int TIME_OUT = 10 * 1000; // 超时时间
}
