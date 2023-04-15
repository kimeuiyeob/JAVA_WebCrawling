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
		String getLocation = address + " ����";

//		���� ����
		driver.get(url);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			;
		}

//		�˻�â�� �ּ�+���� �Է�
		input = driver.findElement(By.cssSelector("input.gLFyf"));
		input.sendKeys(getLocation);
		input.sendKeys(Keys.RETURN);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			;
		}

//		�µ�,����Ȯ��, ����, ǳ��, �ð�, ��������(���� ��...)
		temperature = driver.findElement(By.id("wob_tm")).getText();// �µ�
		time = driver.findElement(By.id("wob_dts")).getText();// �ð�
		condition = driver.findElement(By.id("wob_dc")).getText();// ��������(���� ��...)

		weatherInfos = driver.findElements(By.cssSelector("div.wtsRwe"));// ���� Ȯ��, ����, ǳ��

//		���� ��� ���� ����� ���
		weatherInfo += getLocation + " �Դϴ�\n" + temperature + "��\n";
		for (WebElement webElement : weatherInfos) {
			weatherInfo += webElement.getText() + "\n";
		}
		weatherInfo += time + "\n" + condition + "\n";

		driver.close();

		System.out.println(weatherInfo);
	}

}