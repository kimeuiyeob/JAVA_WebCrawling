package dao;

import java.util.List;

public class WeatherCrawling {
	
	private WebDriver webDriver;
	public static final String WEB_DRIVER_PATH = "C:/chromedriver.exe";
	public static final String WEB_DRIVER_ID = "webdriver.chrome.driver";

	public void getWeahter(String address) {
		
		DiningSite dining = new DiningSite();
		String url = "https://www.google.com/";
		WebDriver driver = dining.webDriver;
		System.setProperty(WEB_DRIVER_ID, WEB_DRIVER_PATH);
		driver = new ChromeDriver();
		List<WebElement> weatherInfos = null;
		WebElement input = null;
		String weatherInfo = "", temperature = "", time = "", condition = "";
		String getLocation = address + " 날씨";

//		구글 접속
		driver.get(url);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			;
		}

//		검색창에 주소+날씨 입력
		input = driver.findElement(By.cssSelector("input.gLFyf"));
		input.sendKeys(getLocation);
		input.sendKeys(Keys.RETURN);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			;
		}

//		온도,강수확률, 습도, 풍속, 시간, 날씨정보(맑음 등...)
		temperature = driver.findElement(By.id("wob_tm")).getText();// 온도
		time = driver.findElement(By.id("wob_dts")).getText();// 시간
		condition = driver.findElement(By.id("wob_dc")).getText();// 날씨정보(맑음 등...)

		weatherInfos = driver.findElements(By.cssSelector("div.wtsRwe"));// 강수 확률, 습도, 풍속

//		날씨 출력 문구 만들고 출력
		weatherInfo += getLocation + " 입니다\n" + temperature + "도\n";
		for (WebElement webElement : weatherInfos) {
			weatherInfo += webElement.getText() + "\n";
		}
		weatherInfo += time + "\n" + condition + "\n";

		driver.close();

		System.out.println(weatherInfo);
	}

}