package dao;

public class Method {

	public String removeNumber(String content) {
//		"1. �ֽ���"��� �Ѵٸ� "1. " ����
		return content.substring(content.indexOf(" ") + 1);
	}

	public String removeAddress(String address) {// �ּ��� �ñ��� ������ ���ּ� ����(�˻��� �ȵǴ� ��찡 �ֱ⿡)
		String[] divAddress = address.split(" "); // ����� �и�
		String getLocation = divAddress[0] + " " + divAddress[1] + " " + divAddress[2];// 3��°(index=2)������ ��������
		return getLocation;
	}
}