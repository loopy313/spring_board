package com.javalec.board.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.Bidi;
import java.util.ArrayList;

import javax.sql.DataSource;
import javax.naming.Context;
import javax.naming.InitialContext;

import com.javalec.board.dto.BDto;

public class BDao {
	private DataSource dataSource;
	public BDao(){
		try{
			Context context=new InitialContext();
			dataSource=(DataSource)context.lookup("java:comp/env/jdbc/Oracle11g");	
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public ArrayList<BDto> list(){
		ArrayList<BDto> dtos=new ArrayList<BDto>();
		Connection connection=null;
		PreparedStatement preparedStatement=null;
		ResultSet resultSet=null;

		try{
			connection=dataSource.getConnection();
			String query="select bId,bName,bTitle,bContent,bDate,bHit,bGroup,bStep,bIndent from mvc_board order by bGroup desc";
			preparedStatement=connection.prepareStatement(query);
			resultSet=preparedStatement.executeQuery();

			while(resultSet.next()){
				int bId=resultSet.getInt("bId");
				String bName=resultSet.getString("bName");
				String bTitle=resultSet.getString("bTitle");
				String bContent=resultSet.getString("bContent");
				Timestamp bDate=resultSet.getTimestamp("BDate");
				int bHit=resultSet.getInt("bHit");
				int bGroup=resultSet.getInt("bGroup");
				int bStep=resultSet.getInt("bStep");
				int bIndent=resultSet.getInt("bIndent");

				BDto dto=new BDto(bId,bName,bTitle,bContent,bDate,bHit,bGroup,bStep,bIndent);
				dtos.add(dto);
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try{
				if(resultSet!=null)	resultSet.close();
				if(preparedStatement!=null)	preparedStatement.close();
				if(connection!=null) connection.close();
			}catch(Exception e){

			}
		}
		return dtos;
	}

	public BDto contentView(String id){
		upHit(id);
		BDto dto=null;
		Connection connection=null;
		PreparedStatement preparedStatement=null;
		ResultSet resultSet=null;

		try{
			connection=dataSource.getConnection();
			String query="select * from mvc_board where bId=?";
			preparedStatement=connection.prepareStatement(query);
			preparedStatement.setInt(1, Integer.parseInt(id));
			resultSet=preparedStatement.executeQuery();
			if(resultSet.next()){
				int bId=resultSet.getInt("bId");
				String bName=resultSet.getString("bName");
				String bTitle=resultSet.getString("bTitle");
				String bContent=resultSet.getString("bContent");
				Timestamp bDate=resultSet.getTimestamp("bDate");
				int bHit=resultSet.getInt("bHit");
				int bGroup=resultSet.getInt("bGroup");
				int bStep=resultSet.getInt("bStep");
				int bIndent=resultSet.getInt("bIndent");

				dto=new BDto(bId,bName,bTitle,bContent,bDate,bHit
						,bGroup,bStep,bIndent);
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try{
				if(resultSet!=null)	resultSet.close();
				if(preparedStatement!=null)	preparedStatement.close();
				if(connection!=null) connection.close();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return dto;
	}

	private void upHit(String bId) {
		Connection connection=null;
		PreparedStatement preparedStatement=null;

		try{
			connection=dataSource.getConnection();
			String query="update mvc_board set bHit=bHit+1 where bId=?";
			preparedStatement=connection.prepareStatement(query);
			preparedStatement.setString(1, bId);

			int rn=preparedStatement.executeUpdate();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try{
				if(preparedStatement!=null)	preparedStatement.close();
				if(connection!=null)connection.close();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}

	public void write(String bName, String bTitle, String bContent) {
		Connection connection=null;
		PreparedStatement preparedStatement=null;

		try{
			connection=dataSource.getConnection();
			String query="insert into mvc_board(bId,bName,bTitle,bContent,bHit,bGroup,bStep,bIndent) values "
					+ "(mvc_board_seq.nextval,?,?,?,0,mvc_board_seq.currval,0,0)";
			preparedStatement=connection.prepareStatement(query);
			preparedStatement.setString(1, bName);
			preparedStatement.setString(2, bTitle);
			preparedStatement.setString(3, bContent);
			int rn=preparedStatement.executeUpdate();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try{
				if(preparedStatement!=null)	preparedStatement.close();
				if(connection!=null)connection.close();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}

	public void modify(String bId, String bName, String bTitle, String bContent) {
		Connection connection=null;
		PreparedStatement preparedStatement=null;

		try{
			connection=dataSource.getConnection();
			String query="update mvc_board set bName=?,bTitle=?,bContent=? where bId=?";
			preparedStatement=connection.prepareStatement(query);

			preparedStatement.setString(1, bName);
			preparedStatement.setString(2, bTitle);
			preparedStatement.setString(3, bContent);			
			preparedStatement.setInt(4, Integer.parseInt(bId));

			int rn=preparedStatement.executeUpdate();

		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try{
				if(preparedStatement!=null)	preparedStatement.close();
				if(connection!=null)connection.close();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}

	public void delete(String bId) {
		Connection connection=null;
		PreparedStatement preparedStatement=null;
		try{
			connection=dataSource.getConnection();
			String query="delete from mvc_board where bId=?";
			preparedStatement=connection.prepareStatement(query);
			preparedStatement.setInt(1, Integer.parseInt(bId));
			int rn=preparedStatement.executeUpdate();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try{
				if(preparedStatement!=null)	preparedStatement.close();
				if(connection!=null)connection.close();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}

	public void reply(String bId, String bName, String bTitle, String bContent,
			String bGroup, String bStep, String bIndent) {
		replyShape(bGroup,bStep);
		Connection connection=null;
		PreparedStatement preparedStatement=null;

		try{
			connection=dataSource.getConnection();
			String query="insert into mvc_board (bId,bName,bTitle,bContent,bGroup,bStep,bIndent) values"
					+ " (mvc_board_seq.nextval,?,?,?,?,?,?)";
			preparedStatement=connection.prepareStatement(query);
			preparedStatement.setString(1, bName);
			preparedStatement.setString(2, bTitle);
			preparedStatement.setString(3, bContent);
			preparedStatement.setInt(4, Integer.parseInt(bGroup));
			preparedStatement.setInt(5, Integer.parseInt(bStep)+1);
			preparedStatement.setInt(6, Integer.parseInt(bIndent)+1);
			int rn=preparedStatement.executeUpdate();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try{
				if(preparedStatement!=null)	preparedStatement.close();
				if(connection!=null)	connection.close();
			}catch(Exception e){
				e.printStackTrace();
			}
		}

	}

	private void replyShape(String bGroup, String bStep) {
		Connection connection=null;
		PreparedStatement preparedStatement=null;
		
		try{
			connection=dataSource.getConnection();
			String query="update mvc_board set bStep=bstep+1 where bgroup=? and bstep>?";
			preparedStatement=connection.prepareStatement(query);
			preparedStatement.setInt(1, Integer.parseInt(bGroup));
			preparedStatement.setInt(2, Integer.parseInt(bStep));
			
			int rn=preparedStatement.executeUpdate();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try{
				if(preparedStatement!=null)	preparedStatement.close();
				if(connection!=null) connection.close();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		
	}

	public BDto reply_view(String rId) {
		BDto dto=null;

		Connection connection=null;
		PreparedStatement preparedStatement=null;
		ResultSet resultSet=null;
		try{
			connection=dataSource.getConnection();
			String query="select * from mvc_board where bId=?";
			preparedStatement=connection.prepareStatement(query);
			preparedStatement.setInt(1, Integer.parseInt(rId));
			resultSet=preparedStatement.executeQuery();

			if(resultSet.next()){
				int bId=resultSet.getInt("bId");
				String bName=resultSet.getString("bName");
				String bTitle=resultSet.getString("bTitle");
				String bContent=resultSet.getString("bContent");
				Timestamp bDate=resultSet.getTimestamp("bDate");
				int bHit=resultSet.getInt("bHit");
				int bGroup=resultSet.getInt("bGroup");
				int bStep=resultSet.getInt("bStep");
				int bIndent=resultSet.getInt("bIndent");

				dto=new BDto(bId, bName, bTitle, bContent, bDate, bHit, bGroup, bStep, bIndent);
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try{
				if(resultSet!=null)	resultSet.close();
				if(preparedStatement!=null) preparedStatement.close();
				if(connection!=null)	connection.close();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return dto;
	}
}
