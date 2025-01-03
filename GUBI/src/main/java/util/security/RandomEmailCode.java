package util.security;

import java.util.Random;

public class RandomEmailCode {

	
	public String makeRandomCode() {
		
		// 인증키를 랜덤하게 생성하도록 한다.
		Random rnd = new Random();
		
		String certification_code = "";
		// 인증키는 영문소문자 5글자 + 숫자 7글자로 만들겠습니다.
		
		char randchar = ' ';
		for (int i = 0; i < 5; i++) {
			/*
			 * min 부터 max 사이의 값으로 랜덤한 정수를 얻으려면 
			 * int rndnum = rnd.nextInt(max - min + 1) + min;
			 * 영문 소문자 'a' 부터 'z' 까지 랜덤하게 1개를 만든다.
			 */
			randchar = (char)(rnd.nextInt('z' - 'a' + 1) + 'a');
			certification_code += randchar; // 쌓아준다.
		}//end of for..
		
		int randnum = 0;
		for (int i = 0; i < 9; i++) {
			/*
			 * min 부터 max 사이의 값으로 랜덤한 정수를 얻으려면 
			 * int rndnum = rnd.nextInt(max - min + 1) + min;
			 * 영문 소문자 'a' 부터 'z' 까지 랜덤하게 1개를 만든다.
			 */
			randnum = (rnd.nextInt(9 - 0 + 1) + 1);
			certification_code += randnum; // 쌓아준다.
		}//end of for..
//		System.out.println("확인용 isUserExist : "+ isUserExist);
//		System.out.println("확인용 certification_code : "+ certification_code);
		
		return certification_code;

	}//end of ...
	
	
	
	
	
	
	
	
}//end of class...
