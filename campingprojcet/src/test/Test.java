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
		
//====================================오늘 가장 많이 찾은 캠핑장===================================
		
		RecommandVO recommandVO = jdbcDAO.printBestCamp();
		if(recommandVO.getCampingName()!=null) {
			printHotCamp = "오늘 가장 많이 찾은 캠핑장\n" + recommandVO.getCampingName() + "\n캠핑유형 : " + recommandVO.getCampType()
			+ "\n환경 : " + recommandVO.getEnvironment() + "\n주소 : " + recommandVO.getCampAddress();
			System.out.println(printHotCamp);
		}

		String campingAddress = cf.function();
		if(campingAddress!=null) {
			dining.diningmethod(campingAddress);
			weatherCrawling.getWeahter(campingAddress);
		}

	}
}