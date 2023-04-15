package dao;

public class CamfitCrawling {
	// 캠핏 메소드
	public String function() {
		Funtions funtions = new Funtions();
//==========================================큰지역 요소 긁어오기(버튼)=================================================
		funtions.locationSelect();
//===================================캠핑유형 선택==========================================
		funtions.campTypeSelect();
//======================================환경 유형 선택=======================================
		funtions.locationTypeSelect();
//=========================================위의 정보로 주소 합침==============================================
		return funtions.getAddress();
	}
}