package test;

import java.net.SocketException;

import dao.CamfitCrawling;
import dao.DiningSite;
import dao.JdbcDAO;
import dao.WeatherCrawling;
import vo.RecommandVO;

public class Test {
	
	public static void main(String[] args) throws SocketException {
		String printHotCamp = "";
		DiningSite dining = new DiningSite();
		WeatherCrawling weatherCrawling = new WeatherCrawling();

		CamfitCrawling cf = new CamfitCrawling();

		JdbcDAO jdbcDAO = new JdbcDAO();
		
//====================================���� ���� ���� ã�� ķ����===================================
		
		RecommandVO recommandVO = jdbcDAO.printBestCamp();
		if(recommandVO.getCampingName()!=null) {
			printHotCamp = "���� ���� ���� ã�� ķ����\n" + recommandVO.getCampingName() + "\nķ������ : " + recommandVO.getCampType()
			+ "\nȯ�� : " + recommandVO.getEnvironment() + "\n�ּ� : " + recommandVO.getCampAddress();
			System.out.println(printHotCamp);
		}

		String campingAddress = cf.function();
		if(campingAddress!=null) {
			dining.diningmethod(campingAddress);
			weatherCrawling.getWeahter(campingAddress);
		}

	}
}