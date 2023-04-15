package dao;

public class Method {

	public String removeNumber(String content) {
//		"1. 애슐리"라고 한다면 "1. " 제거
		return content.substring(content.indexOf(" ") + 1);
	}

	public String removeAddress(String address) {// 주소의 시구동 다음의 상세주소 삭제(검색이 안되는 경우가 있기에)
		String[] divAddress = address.split(" "); // 띄어쓰기로 분리
		String getLocation = divAddress[0] + " " + divAddress[1] + " " + divAddress[2];// 3번째(index=2)까지가 동포지션
		return getLocation;
	}
}