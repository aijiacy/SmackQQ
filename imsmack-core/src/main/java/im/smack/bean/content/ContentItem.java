package im.smack.bean.content;

import im.smack.IMSmkException;


/**
 * 内容接口
 * 
 * @author ChenZhiHui
 * @create-time 2013-2-25
 */
public interface ContentItem {
	public Type getType();
	public Object toJson() throws IMSmkException;
	public void fromJson(String text) throws IMSmkException;

	public enum Type {
		/**字体*/
		FONT("font"), 
		/** 文字*/
		TEXT("text"), 
		/**表情*/
		FACE("face"), 
		/**离线图片*/
		OFFPIC("offpic"),
		/**群图片*/
		CFACE("cface");

		private String name;
		Type(String name){
			this.name = name; 
		}
		public String getName(){
			return name;
		}
		public static Type valueOfRaw(String txt){
			if(txt.equals("font")){
				return FONT;
			}else if(txt.equals("face")){
				return FACE;
			}else if(txt.equals("offpic")){
				return OFFPIC;
			}else if(txt.equals("cface")){
				return CFACE;
			}else{
				return TEXT;
			}
		}
	}
}
