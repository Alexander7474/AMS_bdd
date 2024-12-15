package data;

/**
 * @author Alexandre LANTERNIER 
 * @brief ensemble d'outils pour manipuler des string
 */
public class StringTool {
	public static String changeStringToSize(String str, int size) {
		String finalStr;
		
		if(str.length() > size) {
			finalStr = str.substring(0, size);
		}else {
			finalStr = str;
			for(int i = 0; i < (size-str.length()); i++) {
				finalStr+= " ";
			}
		}
		
		return finalStr;
	}
}
