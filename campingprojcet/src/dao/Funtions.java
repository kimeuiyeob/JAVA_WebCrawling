package dao;

import java.util.ArrayList;
import java.util.Scanner;

public class Funtions {

	private WebDriver webDriver;
	public static final String WEB_DRIVER_PATH = "C:/chromedriver.exe";
	public static final String WEB_DRIVER_ID = "webdriver.chrome.driver";
	Scanner sc = new Scanner(System.in);

	String camfitUrl = "https://camfit.co.kr/search/result";
	int number = 0, choice = 0;
	String bigLocationUrl = "", smallLocationUrl = "";
	String campTypeUrl = "", locationTypeUrl = "";
	String choicedBigLocation = "", temp2 = "";
	String printList;
	String basics = "", additionals = "", leisures = "";
	ArrayList<String> lists = null;

	WebDriver driver = webConnection();

	// 드라이버 연결
	public WebDriver webConnection() {
		
		System.setProperty(WEB_DRIVER_ID, WEB_DRIVER_PATH);
		WebDriver driver = webDriver;
		driver = new ChromeDriver();
		driver.get(camfitUrl);
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			;
		}
		return driver;
	}

	// ================================지역 선택================================
	public void locationSelect() {
		lists = new ArrayList<>();// 큰 지역의 이름을 담음
		String bigLocationName = "";

		// 지역목록 한번에 저장할 변수
		driver.findElement(By.cssSelector("div.right-input input.jRXicD")).click();// 지역을 클릭

		// 큰 지역 목록 뽑고 출력할때 쓸 변수에 저장
		driver.findElements(By.cssSelector("li.jAJMLG p.typography-regular")).forEach(n -> lists.add(n.getText()));

		// 출력할 변수에 담음
		printList = "-----------------------\n";
		lists.forEach(n -> printList += ++number + ". " + n + "\n");

		// 사용자에게 큰지역 입력(선택) 받기
		printList += "원하는 지역의 번호를 입력해주세요";
		System.out.println(printList);
		choice = sc.nextInt();
		System.out.println(lists.get(choice - 1) + " 체크");

		// 사용자에게 받은 번호, 지역의 번호 클릭
		choicedBigLocation = "ul.sc-jGprRt>li:nth-child(" + choice + ")";
		driver.findElement(By.cssSelector(choicedBigLocation)).click();

		// 가져오면 경기 (102), 인천 (9) 이런 식으로 가져오는데 여기서 지역명만 추출
		bigLocationName = lists.get(choice - 1).substring(0, lists.get(choice - 1).indexOf(" "));

		// 주소에 붙일 경로
		bigLocationUrl += "?city=" + bigLocationName;
		// 세종, 제주의 경우 해당 사이트 url에서 검색 조건이 풀네임이므로 해당 경우에 한해 상세정보를 더 붙여서 가져옴
		if (bigLocationName.equals("세종")) {
			bigLocationUrl += "특별자치시";
		}
		if (bigLocationName.equals("제주")) {
			bigLocationUrl += "특별자치도";
		}

		// =======================작은 지역 클릭하기 (단, 0 입력하면선택완료)====================
		// 작은 지역들을 담아둘 Arraylist 생성
		lists = new ArrayList<>();

		driver.findElements(By.cssSelector("ul.sc-hAsxaJ li.jAbgnw")).forEach(n -> lists.add(n.getText()));

		// 상세 지역이 출력 될 때 가독성이 떨어지게 출력이 되곤 했는데, 이 부분을 가독성 좋게 만들어주는 작업.
		number = 0;
		printList = "-----------------------\n";
		lists.forEach(n -> printList += ++number + ". " + n.replace("\n", "") + "\n");
		printList += "0. 선택완료\n원하는 지역의 번호를 입력해주세요";
		System.out.println(printList);

		// 선택
		temp2 = "";
		String smallLocationNames = "";// 지역들 모음
		while (true) {
//			3번 이상 입력했을 때 오류
			choice = sc.nextInt();
			if (choice == 0) {// 0입력 탈출
				break;
			}
			String smallLocationName1 = lists.get(choice - 1).replace("\r", "").replace("\n", "");
			// 지역명의 괄호 분리
			String smallLocationName = smallLocationName1.substring(0, smallLocationName1.indexOf("("));
			// -1이 아니라는 것은 해당 문자가 temp2에 이미 있을 때
			if (temp2.indexOf(choice + "") != -1) {// 체크 해제
				// choice가 temp2에서 몇번 인덱스있는지
				int index = temp2.indexOf(choice + "");
				// 해당 인덱스에 있는 문자를 빈문자열로 바꿈
				temp2 = temp2.replace(temp2.charAt(index) + "", "");
				// 지역모음에서 ,지역명이 있다면 ,지역명을 빈문자열로
				if (smallLocationNames.contains("," + smallLocationName)) {
					smallLocationNames = smallLocationNames.replace("," + smallLocationName, "");
				} else {
					// 지역모음에서 지역명이 있다면 지역명을 빈문자열로
					smallLocationNames = smallLocationNames.replace(smallLocationName, "");
				}
				System.out.println(smallLocationName + " 체크 해제");
			} else {// 체크 되었을 때
				// 값이 하나라도 있다면 ,를 붙여줌
				if (temp2.length() + 1 == 4) {
					System.out.println("최대 3개까지 선택할 수 있습니다");
					continue;
				}
				if (temp2.length() > 0) {
					smallLocationNames += ",";
				}
				// 지역모음에 담음
				smallLocationNames += smallLocationName;
				// 번호모음
				temp2 += choice;
				System.out.println(smallLocationName + " 체크");
			}
			// 지역 클릭
			choicedBigLocation = "ul.fuIZQM>li:nth-child(" + choice + ")";
			driver.findElement(By.cssSelector(choicedBigLocation)).click();
			if (temp2.length() == 0) {// temp2가 0이라면 지역모음도 당연히 0
				smallLocationNames = "";
			}
		}
		if (temp2.length() > 0) {// 소지역을 하나라도 입력했다면 주소 형식에 맞춰 경로 작성
			smallLocationUrl += "&majors=";
		}
		// 주소 형식이랑 선택한 지역 합침
		smallLocationUrl += smallLocationNames;

		driver.findElement(By.cssSelector("ul.gSSvYn")).click();// 닫기 클릭
	}

	// ==========================캠핑 유형 선택==========================================
	public void campTypeSelect() {
		String campTypes = "";
		// 주소타입을 문자열로 받아놓기
		String[] engTypes = { "autoCamping", "glamping", "caravan", "pension", "bungalow", "carCamping" };

		// 캠핑유형 클릭
		driver.findElement(By.cssSelector("div.swiper-slide span.dlthMI")).click();
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			;
		}

		lists = new ArrayList<>();

		// 캠핑유형 목록 가져오기
		driver.findElements(By.cssSelector("div.sc-fIavCj")).forEach(n -> lists.add(n.getText()));

		// 목록출력
		printList = "-----------------------\n";
		number = 0;
		lists.forEach(n -> printList += ++number + ". " + n + "\n");
		printList += "0. 선택완료\n원하는 캠핑 유형을 선택해주세요";
		System.out.println(printList);

		// 클릭
		temp2 = "";
		campTypes = "";
		// 캠핑유형 선택시 버튼클릭 중복해제
		while (true) {
			choice = sc.nextInt();
			if (choice == 0) {
				break;
			}

			// 큰 지역 버튼 클릭 (자식태그에 선택한 번호를 넣어준다.)
			temp2 = "div.MuiPaper-root div:nth-child(" + (choice + 1) + ")";
			driver.findElement(By.cssSelector(temp2)).click();

			// 사용자가 입력한 번호가 중복된 번호라면,
			if (temp2.indexOf(choice + "") != -1) {// 체크 해제
				int index = temp2.indexOf(choice + "");
				// 입력했던 번호가 들어있는 temp2에 입력했던 번호를 지워준다.
				temp2 = temp2.replace(temp2.charAt(index) + "", "");
				// 선택한 캠핑유형중에 해제할 유형을 빈문자열로 바꿔준다.(여러개 입력 되었을 경우 ','가 붙어 나오기 때문에 각각 구분해서 지워준다
				if (campTypes.contains("," + engTypes[choice - 1])) {
					campTypes = campTypes.replace("," + engTypes[choice - 1], "");
				} else {// ','없을 경우
					campTypes = campTypes.replace(engTypes[choice - 1], "");
				}
				System.out.println(lists.get(choice - 1) + " 체크 해제");
			}

			// 받은 사용자 번호가 중복되지 않았고,
			else {
				// 캠핑타입이 여러개라면
				campTypes += temp2.length() > 0 ? "," + engTypes[choice - 1] : engTypes[choice - 1];
//				if (temp2.length() > 0) {
//					// 주소에 "," 추가해준다.
//					campTypes += ",";
//				}
//				// engTypes에 담긴 값을 불러와 영어로 바꿔준다.
//				campTypes += engTypes[choice - 1];
				// (중복확인용 count 해주기)
				temp2 += choice;
				System.out.println(lists.get(choice - 1) + " 체크");
			}
			// temp2의 길이가 0이라면 campTypes 빈문자열로 초기화
			if (temp2.length() == 0) {
				campTypes = "";
			}
		}
		// 캠핑타입의 최종주소
		if (temp2.length() > 0) {
			campTypeUrl += "&types=";
		}
		campTypeUrl += campTypes;

//		닫기 클릭
		driver.findElement(By.cssSelector("div.MuiPaper-root div.dqOnwu svg")).click();
	}

	// ==========================환경 유형 선택============================
	public void locationTypeSelect() {
		String choicedBigLocation = "", locationTypes = "";
//		주소창에는 산,계곡...이렇게 나오는게 아니라 영어로 나오기 때문에 영어글자 모음 배열
		String[] engLocationType = { "ocean", "mountain", "forest", "river", "lake", "valley", "island", "flat",
				"etc" };

		// 환경 클릭
		driver.findElement(By.cssSelector("div.swiper-slide-next span.dlthMI")).click();
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			;
		}

		lists = new ArrayList<>(); // 환경요소들 담을 ArrayList
		// 환경 요소들 불러오기
		driver.findElements(By.cssSelector("div.fLWjSa")).forEach(n -> lists.add(n.getText()));

//		뽑아온 환경요소로 사용자에게 보여줄 목록보여주기
		printList = "-----------------------\n";
		number = 0;
		lists.forEach(n -> printList += ++number + ". " + n + "\n");
		printList += "0. 선택완료\n원하는 환경종류의 번호를 선택하세요.";
		System.out.println(printList);

		temp2 = "";
		locationTypes = "";// 빈문자열로 초기화 되었겠지만 일단 초기화
		while (true) {
			choice = sc.nextInt(); // 사용자가 입력할 목록 번호
			if (choice == 0) { // 0누르면 선택완료
				break;
			}
			choicedBigLocation = "div.MuiPaper-root div:nth-child(" + (choice + 1) + ")"; // 환경목록들 예)산,바다,계곡 등을 선택
			driver.findElement(By.cssSelector(choicedBigLocation)).click(); // 선택한 목록 클릭

			if (temp2.indexOf(choice + "") != -1) { // 사용자가 입력한 환경요소가 이미 있다면 아래 코드 실행(체크해제)
				int index = temp2.indexOf(choice + "");// 입력한 번호가 있는 인덱스 찾음
				temp2 = temp2.replace(temp2.charAt(index) + "", "");// 이미 있는 번호를 지워줌
				if (locationTypes.contains("," + engLocationType[choice - 1])) {// 문자열이 ",mountain"이렇게 되어있을 경우
					locationTypes = locationTypes.replace("," + engLocationType[choice - 1], "");// mountain만 지우면 ','이
				} else {// 아니라는 거는 쉼표가 없는거니까
					locationTypes = locationTypes.replace(engLocationType[choice - 1], "");// 그냥 글자만 지워줌
				}
				System.out.println(lists.get(choice - 1) + " 체크 해제");
			} else {// 사용자가 새로운 번호를 입력할 경우
				locationTypes += temp2.length() > 0 ? "," + engLocationType[choice - 1] : engLocationType[choice - 1];
//				if (temp2.length() > 0) {// 길이가 1이상일 경우 선택된 환경이 있다는 거니까
//					locationTypes += ",";// 쉼표 붙히고
//				}
//				locationTypes += engLocationType[choice - 1];// 선택된 환경에 연결
				temp2 += choice;// 어떤게 입력 되었는지 확인하기 위해 temp2에 입력한 번호 저장
				System.out.println(lists.get(choice - 1) + " 체크");
			}
		}
		driver.findElement(By.cssSelector("div.MuiPaper-root button span")).click();
		if (temp2.length() > 0) {// 1이상 있다는 것은 사용자가 뭐라도 선택한 거니까
			locationTypeUrl += "&locationTypes=";// 주소 형식에 맞는 "&locationTypes=" 를 맨앞에 붙혀줌
		}
		locationTypeUrl += locationTypes;// "&locationTypes="이것과 위에서 선택 했던 환경의 영어 이름을 합침
	}

	// ========================위의 정보로 주소 합침=============================
	public String getAddress() {
//		ChromeOptions options = new ChromeOptions();
//		options.addArguments("headless");
		JdbcDAO jdbcDAO = new JdbcDAO();
		List<WebElement> campInfo = null;

//		마지막에 출력 캠핑장의 정보 모음 변수(일괄처리)

		Method method = new Method();

		driver.close();
//		위의 사용자가 입력한 정보를 토대로 주소를 만듦
		String sumUrl = camfitUrl + bigLocationUrl + locationTypeUrl + smallLocationUrl + campTypeUrl;
//		만든 주소로 접속
		driver = new ChromeDriver();
		driver.get(sumUrl);
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			;
		}

//					캠핑장의 정보 모음(주소, 이름, 캠핑종류, 환경)
		ArrayList<String> campAddress = new ArrayList<String>();
		ArrayList<String> campingName = new ArrayList<String>();
		ArrayList<String> campType = new ArrayList<String>();
		ArrayList<String> environment = new ArrayList<String>();

//					가공되지 않은 url
		List<WebElement> campUrls = driver.findElements(By.cssSelector("div a"));
//					각 캠프소개로 이어지는 url모음
		ArrayList<String> campUrl = new ArrayList<String>();
//					가공되지 않은 campUrls에 href로 주소만 뽑아와 저장
		campUrls.forEach(n -> campUrl.add(n.getAttribute("href")));

		driver.close();
//			url모음을 전부 다 들어가봄
		number = 0;
		for (String url : campUrl) {
			if (number == 5) {
				break;
			}
//			드라이버 새로 엶 각 캠핑장정보가 있는 주소로 이동
			driver = new ChromeDriver();
			driver.get(url);
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				;
			}
//		캠핑장 이름
			campingName.add(driver.findElement(By.cssSelector("div.gFlZWK p.margin")).getText());
//			캠핑유형
			campType.add(driver.findElement(By.xpath("//*[@id=\"root\"]/div/section/div/div[3]/div[2]/ul/li[1]/p"))
					.getText());
//			캠핑환경
			environment.add(driver.findElement(By.xpath("//*[@id=\"root\"]/div/section/div/div[3]/div[2]/ul/li[2]/p"))
					.getText());

//						캠핑장 정보(대표전화, 주소)
			campInfo = driver.findElements(By.cssSelector("li.withBtn p"));
//						대표번호
			String generalDirectoryNumber = campInfo.get(0).getText();
//						주소
			campAddress.add(campInfo.get(1).getText());

//						캠핑장 기본 시설 가져오기
			basics = "";
			List<WebElement> basicService = driver
					.findElements(By.cssSelector("div.sc-bBXxYQ div:nth-child(1) div.sc-grVGCS li"));
			for (WebElement webElement : basicService) {
				basics += (webElement.getText() + " ");
			}
			if (basics.length() == 0) {
				basics = "없음";
			}
//						캠핑장 부가 시설 가져오기
			additionals = "";
			List<WebElement> additionalService = driver
					.findElements(By.cssSelector("div.sc-bBXxYQ div:nth-child(2) div.sc-grVGCS li"));
			for (WebElement webElement : additionalService) {
				additionals += (webElement.getText() + " ");
			}
			if (additionals.length() == 0) {
				additionals = "없음";
			}
//						캠핑장 레저 시설 가져오기
			leisures = "";
			List<WebElement> leisureService = driver
					.findElements(By.cssSelector("div.sc-bBXxYQ div:nth-child(3) div.sc-grVGCS li"));
			for (WebElement webElement : leisureService) {
				leisures += (webElement.getText() + " ");
			}
			if (leisures.length() == 0) {
				leisures = "없음";
			}
//						캠핑장 이름,대표 전화,시설및 레저 출력
//						목록에 순서 부여
			number++;
			printList += number + ". " + driver.findElement(By.className("margin")).getText() + "\n" + "대표전화 : "
					+ generalDirectoryNumber + "\n" + "기본시설 : " + basics + "\n" + "부가시설 : " + additionals + "\n"
					+ "레저시설 : " + leisures + "\n";
			printList += "------------------------------------\n";
			driver.close();
		}
		if (printList.length() == 0) {
			System.out.println("검색 결과가 없습니다");
			driver.close();
			return null;
		}
		System.out.println(printList + "원하는 캠핑장의 번호를 입력하시면 근처 맛집을 찾아 드립니다");
		choice = sc.nextInt();
		System.out.println(campAddress.get(choice - 1) + " 근처 맛집을 검색합니다");

//		데이터베이스에 넘겨줄 객체
		RecommandVO recommandVO = new RecommandVO();
		recommandVO.setCampAddress(campAddress.get(choice - 1));
		recommandVO.setCampingName(campingName.get(choice - 1));
		recommandVO.setCampType(campType.get(choice - 1));
		recommandVO.setEnvironment(environment.get(choice - 1));

//		선택당한 캠핑장 데이터베이스에 저장
		jdbcDAO.inputBestCamp(recommandVO);

		return method.removeAddress(campAddress.get(choice - 1));// 캠핑장 주소 리턴
	}

}