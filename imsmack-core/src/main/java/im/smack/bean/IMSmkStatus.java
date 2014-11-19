package im.smack.bean;
/**
 * 枚举客户端在线状态
 * @author CaoYong
 *
 */
public enum IMSmkStatus {
	/**
	 *  10 : "online",
                20 : "offline",
                30 : "away",
                40 : "hidden",
                50 : "busy",
                60 : "callme",
                70 : "silent"
	 */
	ONLINE("online", 10),
	OFFLINE("offline", 20),
	AWAY("away", 30),
	HIDDEN("hidden", 40),
	BUSY("busy", 50),
	CALLME("callme", 60),
	SLIENT("silent", 70);
	
	private String value;
	private int status;
	IMSmkStatus(String value, int status){
		this.value = value; 
		this.status = status;
	}
	public String getValue(){
		return value;
	}
	public int getStatus(){
		return status;
	}
	
	public static IMSmkStatus valueOfRaw(String txt){
		for(IMSmkStatus s: IMSmkStatus.values()){
			if(s.value.equals(txt)){
				return s;
			}
		}
		throw new IllegalArgumentException("Unknown QQStatus enum: " + txt);
	}
	
	
	public static IMSmkStatus valueOfRaw(int status){
		for(IMSmkStatus s: IMSmkStatus.values()){
			if(s.status == status){
				return s;
			}
		}
		throw new IllegalArgumentException("unknown QQStatus enum: " + status);
	}
	
	public static boolean isGeneralOnline(IMSmkStatus stat){
		return (stat == IMSmkStatus.ONLINE ||
			stat == IMSmkStatus.CALLME ||
			stat == IMSmkStatus.AWAY ||
			stat == IMSmkStatus.SLIENT ||
			stat == IMSmkStatus.BUSY ||
			stat == IMSmkStatus.HIDDEN);
	}
}
