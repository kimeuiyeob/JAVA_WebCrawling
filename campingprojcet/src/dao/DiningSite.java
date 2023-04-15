package dao;

import java.util.Scanner;

public class DiningSite {
	
	WebDriver webDriver;
	public static final String WEB_DRIVER_PATH = "C:/chromedriver.exe";
	public static final String WEB_DRIVER_ID = "webdriver.chrome.driver";

	public void diningmethod(String address) {
		Scanner sc = new Scanner(System.in);
		DiningSite dining = new DiningSite();
		WebDriver driver = dining.webDriver;
		driver = new ChromeDriver();
		System.setProperty(WEB_DRIVER_ID, WEB_DRIVER_PATH);
//		깔끔하게 하기 위해 상호명 뒤에 번호 제거하는 메소드 넣어놈
		Method key = new Method();
		
//		일괄 처리 부분
		String url = "https://www.diningcode.com/search?";
		List<WebElement> storesList = null, list = null;
		WebElement rist1to5 = null, searchInput = null;
		String location = "", menu = "", name = "", locationAndMenu = "",score = "", diningInfo = "";
		int number = 0;
		
//		사이트 접속
		driver.get(url);// 다이닝 사이트 주소
		try {Thread.sleep(3000);} catch (InterruptedException e) {;}
		
//		검색창에 캠핑장 주소 검색
		searchInput = driver.findElement(By.className("searchInput"));
		searchInput.click();
		searchInput.sendKeys(address);
		searchInput.sendKeys(Keys.RETURN);
		try {Thread.sleep(2000);} catch (InterruptedException e) {;}
		
//		추천 음식점 목록가져오기
		rist1to5 = driver.findElement(By.className("RList"));
		storesList = rist1to5.findElements(By.className("RHeader"));
		
		for (WebElement store : storesList) {
//			식당 이름
			name = store.findElement(By.className("InfoHeader")).getText();
			
//			식당 위치와 메뉴
			list = store.findElements(By.cssSelector("p.Category"));
			locationAndMenu += list.get(0).getText();

//			식당 위치 분리
			location = locationAndMenu.substring(0, locationAndMenu.indexOf("\n"));
			
//			메뉴 분리
			menu = locationAndMenu.substring(locationAndMenu.indexOf("\n")).replace("\n", "").replace("/", "");

//			점수 가져옴
			score = store.findElement(By.className("Score")).getText();
			
//			출력 일괄 처리
			diningInfo = ++number + ". 상호명 : " + key.removeNumber(name) + "\n위치 : " + location + "\n메뉴 : " + menu + "\n점수 : " + score + "\n----------------------------------------";
			
//			식당의 정보가 들어있는 목록 출력
			System.out.println(diningInfo);
		}
		driver.close();

	}

}