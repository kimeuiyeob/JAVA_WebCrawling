package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import vo.RecommandVO;

public class JdbcDAO {

	public Connection connection; // 연결 객체
	public PreparedStatement preparedStatement; // 쿼리(SQL문) 객체
	public ResultSet resultSet; // 결과 테이블 객체

//	가장 많이 조회된 정보에 대한 VO객체를 완성해서 전달하는 메소드
	public RecommandVO printBestCamp() {
		RecommandVO recommandVO = new RecommandVO();

//		tbl_recommand에서 최댓값을 가지고 있는 inputCount의 campAddress, campingName, campType, environment 정보를 가져올 쿼리문 초기화
		String query = "SELECT campAddress, campingName, campType, environment " + "FROM tbl_recommand "
				+ "where inputCount = (select max(inputCount) " + "FROM tbl_recommand)";
		try {
//		     연결 객체 가져오기
			connection = DBConnecter.getConnection();
//		     작성한 쿼리문을 preparedStatement에 전달
			preparedStatement = connection.prepareStatement(query);
//		     쿼리 실행 (SELECT만 extcuteQuery)
			resultSet = preparedStatement.executeQuery();
//			campAddress, campingName, campType, environment을 하나씩 가져옴과 동시에 VO객체에 저장한다.
			if (resultSet.next()) {
				recommandVO.setCampAddress(resultSet.getString(1));
				recommandVO.setCampingName(resultSet.getString(2));
				recommandVO.setCampType(resultSet.getString(3));
				recommandVO.setEnvironment(resultSet.getString(4));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("checkId()에서 쿼리문 오류");
//		   열어준 객체들을 닫을 건데,  닫는 것은 모두 finally에서 한다.
		} finally {
			try {
//		        연결 객체(3개) 모두 닫기
				if (preparedStatement != null) {
					preparedStatement.close();
				}
				if (connection != null) {
					connection.close();
				}
				if (resultSet != null) {
					resultSet.close();
				}
			} catch (SQLException e) {
				throw new RuntimeException(e.getMessage());
			}
		}
//		VO객체 리턴
		return recommandVO;
	}

//	찾아온 정보들을 테이블에 입력해주는 메서드
	public void inputBestCamp(RecommandVO recommandVO) {
//		주소 넘겨받음
		int count = checkAddress(recommandVO);
//		count가 0이 아니라면 정보가 이미 있다
		if (count >= 0) {
			updateCount(recommandVO);
//			저장이 되면 안되니까 탈출
			return;
		}
//		데이터 입력 쿼리문 (주소, 캠핑장 이름, 캠핑 유형, 환경)
		String query = "insert into tbl_recommand (campAddress, campingName, campType, environment, inputCount) values (?,?,?,?,?)";
		try {
//	         연결 객체 가져오기
			connection = DBConnecter.getConnection();
//	        작성한 쿼리문을 preparedStatement에 전달
			preparedStatement = connection.prepareStatement(query);
//	         ? 채우기(좌에서 우로 1부터 1씩 증가)
			preparedStatement.setString(1, recommandVO.getCampAddress());
			preparedStatement.setString(2, recommandVO.getCampingName());
			preparedStatement.setString(3, recommandVO.getCampType());
			preparedStatement.setString(4, recommandVO.getEnvironment());
			preparedStatement.setInt(5, recommandVO.getInputCount());
//	         쿼리 실행
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("checkId()에서 쿼리문 오류");
//	       열어준 객체들을 닫을 건데,  닫는 것은 모두 finally에서 한다.
		} finally {
			try {
//	            연결 객체 모두 닫기
				if (preparedStatement != null) {
					preparedStatement.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				throw new RuntimeException(e.getMessage());
			}
		}
	}

//	캠핑주소 및 검색횟수 확인 메소드
	public int checkAddress(RecommandVO recommandVO) {
		int count = -1;

//		캠핑 주소에 따른 검색된 횟수(inputCount)를 가져온다
		String query = "select inputCount from tbl_recommand where campAddress = ?";
		try {
//	         연결 객체 가져오기
			connection = DBConnecter.getConnection();
//	         작성한 쿼리문을 preparedStatement에 전달
			preparedStatement = connection.prepareStatement(query);
//	         ? 채우기(좌에서 우로 1부터 1씩 증가)
			preparedStatement.setString(1, recommandVO.getCampAddress());
//	         쿼리 실행 (SELECT만 extcuteQuery)
			resultSet = preparedStatement.executeQuery();
//	         결과 행 1개 가져오기
			if (resultSet.next()) {
				count = resultSet.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("checkId()에서 쿼리문 오류2");
//	       열어준 객체들을 닫을 건데,  닫는 것은 모두 finally에서 한다.
		} finally {
			try {
//	            연결 객체(3개) 모두 닫기
				if (resultSet != null) {
					resultSet.close();
				}
				if (preparedStatement != null) {
					preparedStatement.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				throw new RuntimeException(e.getMessage());
			}
		}
		return count; // 검색된 횟수 리턴
	}

//	클릭한 캠핑이 테이블에 있는 경우, 새로 추가하지 않고 count를 올려주기위한 메소드
	public void updateCount(RecommandVO recommandVO) {
		// inputCount쿼리의 숫자를 카운팅(+1) 해준다.
		String query = "update tbl_recommand set inputCount = inputCount+1 where campAddress = ?";
		try {
//	         연결 객체 가져오기
			connection = DBConnecter.getConnection();
//	         작성한 쿼리문을 preparedStatement에 전달
			preparedStatement = connection.prepareStatement(query);
//	         ? 채우기(좌에서 우로 1부터 1씩 증가)
			preparedStatement.setString(1, recommandVO.getCampAddress());
//	         쿼리 실행 (SELECT만 extcuteQuery)
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("checkId()에서 쿼리문 오류2");
//	       열어준 객체들을 닫을 건데,  닫는 것은 모두 finally에서 한다.
		} finally {
			try {
//	            연결 객체(2개) 모두 닫기
				if (preparedStatement != null) {
					preparedStatement.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				throw new RuntimeException(e.getMessage());
			}
		}
	}

}