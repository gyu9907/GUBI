package util.check;

public class Check {

	public static boolean isNullOrBlank(String str) {
		if(str == null || str.isBlank()) {
			return true;
		}
		else {
			return false;
		}
	}
}
