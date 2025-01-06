package util.file;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;

import jakarta.servlet.http.Part;

public class PartFileWriter {

	/**
	 * part 파일을 저장하는 메소드
	 * @param part 파일
	 * @param uploadFilePath 파일 업로드 경로
	 * @return 저장된 파일명 반환
	 * @throws IOException 파일을 디스크에 저장하는 중 에러 발생
	 */
	public static String writeFile(Part part, String uploadFilePath) throws IOException {
		String uploadFileName = null;
		
		String fileName = extractFileName(part.getHeader("Content-Disposition"));
		if (part.getSize() > 0) {
			uploadFileName = fileName.substring(0, fileName.lastIndexOf(".")); // 확장자를 뺀 파일명 알아오기

			uploadFileName += "_" + String.format("%1$tY%1$tm%1$td%1$tH%1$tM%1$tS", Calendar.getInstance());
			uploadFileName += System.nanoTime();
			uploadFileName += fileName.substring(fileName.lastIndexOf(".")); // 확장자 붙이기
			
			part.write(uploadFilePath + File.separator + uploadFileName); // 파일을 지정된 디스크 경로에 저장
		}
		return uploadFileName;
	}
	
	private static String extractFileName(String partHeader) {
		for (String cd : partHeader.split("\\;")) {
			if (cd.trim().startsWith("filename")) {
				String fileName = cd.substring(cd.indexOf("=") + 1).trim().replace("\"", "");
				int index = fileName.lastIndexOf(File.separator); // File.separator : OS가 Windows 이라면 \ 이고, OS가 Mac, Linux, Unix 이라면 / 이다.
				return fileName.substring(index + 1);
			}
		}
		return null;
	}// end of private String extractFileName(String partHeader)-------------------
}
