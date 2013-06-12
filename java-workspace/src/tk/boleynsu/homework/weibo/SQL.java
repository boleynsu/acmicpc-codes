package tk.boleynsu.homework.weibo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class SQL
{
	private static Connection connection=null;
	private static Connection getConnection() throws ClassNotFoundException, SQLException
	{
		if (connection==null||connection.isClosed())
		{
			Class.forName("com.mysql.jdbc.Driver");
			connection=DriverManager.getConnection("jdbc:mysql://"+Config.getSqlHost()+"/"+Config.getSqlDatabase(),Config.getSqlUsername(),Config.getSqlPassword());
		}
		return connection;
	}
	public static PreparedStatement prepareStatement(String sql) throws ClassNotFoundException, SQLException
	{
		return getConnection().prepareStatement(sql);
	}
	public static Statement createStatement() throws ClassNotFoundException, SQLException
	{
		return getConnection().createStatement();
	}
	public static void main(String[] args) throws ClassNotFoundException, SQLException
	{
		Class.forName("com.mysql.jdbc.Driver");
		Connection connection=DriverManager.getConnection("jdbc:mysql://"+Config.getSqlHost(),Config.getSqlUsername(),Config.getSqlPassword());
		Statement statement=connection.createStatement();
		statement.execute("create database "+Config.getSqlDatabase()+";");
		statement.execute("use "+Config.getSqlDatabase()+";");
		statement.execute("create table microblog(id bigint auto_increment primary key,parentId bigint,text varchar(140),time timestamp,foreign key(parentId) references microblog(id));");
		statement.execute("create table user(id bigint auto_increment primary key,username varchar(255) unique,password char(128));");
		statement.execute("create table isOwnerOf(uid bigint,mid bigint,foreign key(uid) references user(id),foreign key(mid) references microblog(id));");
		statement.execute("create table isFollowerOf(follower bigint,followee bigint,foreign key(follower) references user(id),foreign key(followee) references user(id));");
		statement.execute("create table hashTag(id bigint auto_increment primary key,text varchar(140) unique);");
		statement.execute("create table hasHashTag(mid bigint,hid bigint,foreign key(mid) references microblog(id),foreign key(hid) references hashTag(id));");
		statement.execute("create table hasAtTag(mid bigint,uid bigint,foreign key(mid) references microblog(id),foreign key(uid) references user(id));");
	}
}
