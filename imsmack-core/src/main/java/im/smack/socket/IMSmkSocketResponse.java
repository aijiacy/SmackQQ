package im.smack.socket;

public class IMSmkSocketResponse {
	public static final int S_OK = 200;
	public static final int S_NOT_MODIFIED = 304;
	public static final int S_BAD_REQUEST = 400;
	public static final int S_NOT_AUTHORIZED = 401;
	public static final int S_FORBIDDEN = 403;
	public static final int S_NOT_FOUND = 404;
	public static final int S_NOT_ACCEPTABLE = 406;
	public static final int S_INTERNAL_SERVER_ERROR = 500;
	public static final int S_BAD_GATEWAY = 502;
	public static final int S_SERVICE_UNAVAILABLE = 503;
	public static final int S_SERVICE_PROCESS_FAILED = 402;
	
	/**
	 * 状态码
	 */
	private int responseCode;
	
	/**
	 * 状态消息
	 */
	private String responseMessage;
	
	public IMSmkSocketResponse() {
	}

	public IMSmkSocketResponse(int responseCode, String responseMessage) {
		this.responseCode = responseCode;
		this.responseMessage = responseMessage;
	}

	public int getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(int responseCode) {
		this.responseCode = responseCode;
	}

	public String getResponseMessage() {
		return responseMessage;
	}

	public void setResponseMessage(String responseMessage) {
		this.responseMessage = responseMessage;
	}
}
