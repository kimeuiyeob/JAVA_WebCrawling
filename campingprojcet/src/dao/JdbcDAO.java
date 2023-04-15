package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import vo.RecommandVO;

public class JdbcDAO {

	public Connection connection; // ���� ��ü
	public PreparedStatement preparedStatement; // ����(SQL��) ��ü
	public ResultSet resultSet; // ��� ���̺� ��ü

//	���� ���� ��ȸ�� ������ ���� VO��ü�� �ϼ��ؼ� �����ϴ� �޼ҵ�
	public RecommandVO printBestCamp() {
		RecommandVO recommandVO = new RecommandVO();

//		tbl_recommand���� �ִ��� ������ �ִ� inputCount�� campAddress, campingName, campType, environment ������ ������ ������ �ʱ�ȭ
		String query = "SELECT campAddress, campingName, campType, environment " + "FROM tbl_recommand "
				+ "where inputCount = (select max(inputCount) " + "FROM tbl_recommand)";
		try {
//		     ���� ��ü ��������
			connection = DBConnecter.getConnection();
//		     �ۼ��� �������� preparedStatement�� ����
			preparedStatement = connection.prepareStatement(query);
//		     ���� ���� (SELECT�� extcuteQuery)
			resultSet = preparedStatement.executeQuery();
//			campAddress, campingName, campType, environment�� �ϳ��� �����Ȱ� ���ÿ� VO��ü�� �����Ѵ�.
			if (resultSet.next()) {
				recommandVO.setCampAddress(resultSet.getString(1));
				recommandVO.setCampingName(resultSet.getString(2));
				recommandVO.setCampType(resultSet.getString(3));
				recommandVO.setEnvironment(resultSet.getString(4));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("checkId()���� ������ ����");
//		   ������ ��ü���� ���� �ǵ�,  �ݴ� ���� ��� finally���� �Ѵ�.
		} finally {
			try {
//		        ���� ��ü(3��) ��� �ݱ�
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
//		VO��ü ����
		return recommandVO;
	}

//	ã�ƿ� �������� ���̺� �Է����ִ� �޼���
	public void inputBestCamp(RecommandVO recommandVO) {
//		�ּ� �Ѱܹ���
		int count = checkAddress(recommandVO);
//		count�� 0�� �ƴ϶�� ������ �̹� �ִ�
		if (count >= 0) {
			updateCount(recommandVO);
//			������ �Ǹ� �ȵǴϱ� Ż��
			return;
		}
//		������ �Է� ������ (�ּ�, ķ���� �̸�, ķ�� ����, ȯ��)
		String query = "insert into tbl_recommand (campAddress, campingName, campType, environment, inputCount) values (?,?,?,?,?)";
		try {
//	         ���� ��ü ��������
			connection = DBConnecter.getConnection();
//	        �ۼ��� �������� preparedStatement�� ����
			preparedStatement = connection.prepareStatement(query);
//	         ? ä���(�¿��� ��� 1���� 1�� ����)
			preparedStatement.setString(1, recommandVO.getCampAddress());
			preparedStatement.setString(2, recommandVO.getCampingName());
			preparedStatement.setString(3, recommandVO.getCampType());
			preparedStatement.setString(4, recommandVO.getEnvironment());
			preparedStatement.setInt(5, recommandVO.getInputCount());
//	         ���� ����
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("checkId()���� ������ ����");
//	       ������ ��ü���� ���� �ǵ�,  �ݴ� ���� ��� finally���� �Ѵ�.
		} finally {
			try {
//	            ���� ��ü ��� �ݱ�
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

//	ķ���ּ� �� �˻�Ƚ�� Ȯ�� �޼ҵ�
	public int checkAddress(RecommandVO recommandVO) {
		int count = -1;

//		ķ�� �ּҿ� ���� �˻��� Ƚ��(inputCount)�� �����´�
		String query = "select inputCount from tbl_recommand where campAddress = ?";
		try {
//	         ���� ��ü ��������
			connection = DBConnecter.getConnection();
//	         �ۼ��� �������� preparedStatement�� ����
			preparedStatement = connection.prepareStatement(query);
//	         ? ä���(�¿��� ��� 1���� 1�� ����)
			preparedStatement.setString(1, recommandVO.getCampAddress());
//	         ���� ���� (SELECT�� extcuteQuery)
			resultSet = preparedStatement.executeQuery();
//	         ��� �� 1�� ��������
			if (resultSet.next()) {
				count = resultSet.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("checkId()���� ������ ����2");
//	       ������ ��ü���� ���� �ǵ�,  �ݴ� ���� ��� finally���� �Ѵ�.
		} finally {
			try {
//	            ���� ��ü(3��) ��� �ݱ�
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
		return count; // �˻��� Ƚ�� ����
	}

//	Ŭ���� ķ���� ���̺� �ִ� ���, ���� �߰����� �ʰ� count�� �÷��ֱ����� �޼ҵ�
	public void updateCount(RecommandVO recommandVO) {
		// inputCount������ ���ڸ� ī����(+1) ���ش�.
		String query = "update tbl_recommand set inputCount = inputCount+1 where campAddress = ?";
		try {
//	         ���� ��ü ��������
			connection = DBConnecter.getConnection();
//	         �ۼ��� �������� preparedStatement�� ����
			preparedStatement = connection.prepareStatement(query);
//	         ? ä���(�¿��� ��� 1���� 1�� ����)
			preparedStatement.setString(1, recommandVO.getCampAddress());
//	         ���� ���� (SELECT�� extcuteQuery)
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("checkId()���� ������ ����2");
//	       ������ ��ü���� ���� �ǵ�,  �ݴ� ���� ��� finally���� �Ѵ�.
		} finally {
			try {
//	            ���� ��ü(2��) ��� �ݱ�
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