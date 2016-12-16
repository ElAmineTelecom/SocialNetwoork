package com.restful;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codehaus.jettison.json.JSONArray;

@Path("MeetupApi")
public class MeetupApi {

	
	@GET
	@Path("version")
	public String version(@QueryParam ("version") String version)
	{
	//String version="1.0.0.5";
	return "The current version is " +version;
	}
	
	/*Get all Users from the database*/
	@GET
	@Path("login")
	@Produces(MediaType.APPLICATION_JSON) 
	public Response getUsers()throws Exception{
		
		String check = "SELECT * FROM users;";
		Connection con = DBclass.returnConnection();
		PreparedStatement getdata = con.prepareStatement(check);
		ResultSet rs = getdata.executeQuery(check);
		JSONArray array = (new ToJSON()).toJSONArray(rs);
		
		if (array.length() > 0) 
		{
			return Response.ok("Users :" +array).build();
		}
		else
		{
			return Response.serverError().build();
		}
	}
	
	//insert an user
	@GET
	@Path("/signup")
	public Response addUsers(@QueryParam ("mail") String mail, 
			@QueryParam ("lastname") String lastname,
			@QueryParam ("firstname") String firstname,
			@QueryParam ("bio") String bio, 
			@QueryParam ("groups") String groups) throws Exception
	{
		Connection con = DBclass.returnConnection();
		Statement st = con.createStatement(); 
        int valueUpdate = st.executeUpdate("INSERT INTO users (email, firstname, lastname, bio, groups)"+ 
		"VALUES ('"+mail+"','"+firstname+"','"+lastname+"','"+bio+"','"+groups+"');"); 
		if(valueUpdate == 0)
		{
			return Response.status(400).entity("Error 400 : Can not create a new user '"+mail+"'.").build();
		}
		else
		{
			return Response.status(200).entity("Code 200: New user :'"+mail+"' added correctly. <a href=/SocialNetwoork>Kick Back !Meetup</a>").build();
		}
	}
	
	//updateUser
	
	@GET
	@Path("/updateUser")
	public Response updateUser(@QueryParam ("mail") String mail, 
			@QueryParam ("lastname") String lastname,
			@QueryParam ("firstname") String firstname,
			@QueryParam ("bio") String bio) 
			throws Exception
	{
		Connection con = DBclass.returnConnection();
		Statement st = con.createStatement(); 
        int valueUpdate = st.executeUpdate("UPDATE users SET email ='"+mail+"', firstname ='"+firstname+"', lastname ='"+lastname+"', bio ='"+bio+"'"
        		+ "WHERE email LIKE'"+mail+"';");
		if(valueUpdate == 0)
		{
			return Response.status(400).entity("Error 400 : Can not update an user.").build();
		}
		else
		{
			return Response.status(200).entity("Code 200: Updates correctly the user '"+mail+"'.<a href=/SocialNetwoork>Kick Back !Meetup</a>").build();
		}
	}
	
	//deleteUser
	
	@GET
	@Path("deleteUser")
	public Response deleteUser(@QueryParam ("mail") String mail) 
			throws Exception
	{
		Connection con = DBclass.returnConnection();
		Statement st = con.createStatement(); 
        int valueUpdate = st.executeUpdate("DELETE FROM users "
        		+ "WHERE email LIKE'"+mail+"';");
        
        st.executeUpdate("DELETE FROM comments WHERE mail LIKE'"+mail+"';");
        
		if(valueUpdate == 0)
		{
			return Response.status(400).entity("Error 400 : Can not delete the user '"+mail+"'.").build();
		}
		else
		{
			return Response.status(200).entity("Code 200: Delete correctly the user '"+mail+"'.<a href=/SocialNetwoork>Kick Back !Meetup</a>").build();
		}
	}
	// view an user
	@GET
	@Path("/viewUser")
	@Produces(MediaType.APPLICATION_JSON) 
	public Response viewUser(@QueryParam ("mail") String mail) 
			throws Exception
	{
		Connection con = DBclass.returnConnection();
		Statement st = con.createStatement(); 
        ResultSet valueUpdate = st.executeQuery("SELECT * FROM users "
        		+ "WHERE email LIKE'"+mail+"';");
        JSONArray array = (new ToJSON()).toJSONArray(valueUpdate);

		if (array.length() > 0) 
		{
			return Response.ok("User :" +array).build();
		}
		else
		{
			return Response.serverError().build();
		}
	}
	
	
	
	
	// View groups
	@GET
	@Path("/viewGroups")
	@Produces(MediaType.APPLICATION_JSON) 
	public Response viewGroups() throws Exception{
		
		Connection con = DBclass.returnConnection();
		Statement st = con.createStatement(); 
        ResultSet valueUpdate = st.executeQuery("SELECT * FROM groups;");
		
        JSONArray array = (new ToJSON()).toJSONArray(valueUpdate);

		if (array.length() > 0) 
		{
			return Response.ok("Groups :" +array).build();
		}
		else
		{
			return Response.serverError().build();
		}
		
	}
	
	// View comments
	@GET
	@Produces(MediaType.APPLICATION_JSON) 
	public Response viewComments(@QueryParam("query") String groupname) throws Exception{
			
			Connection con = DBclass.returnConnection();
			Statement st = con.createStatement(); 
	        ResultSet valueUpdate = st.executeQuery("SELECT * FROM comments WHERE groupname LIKE '"+groupname+"' ORDER BY date ASC;");
			
	        JSONArray array = (new ToJSON()).toJSONArray(valueUpdate);

			if (array.length() > 0) 
			{
				return Response.ok("Comments :" +array).build();
			}
			else
			{
				return Response.serverError().build();
			}
		}
		
	// add a comments
	@GET
	@Path("/addComment")
	public Response addComment(@QueryParam("group") String group, @QueryParam("mail") String mail,
			@QueryParam("comments") String comments) throws SQLException{
					
		Connection con = DBclass.returnConnection();
		Statement st = con.createStatement(); 
		int valueUpdate = st.executeUpdate("INSERT INTO comments (mail,comment,groupname,date) VALUES ('"+mail+"','"+comments+"','"+group+"',NOW());");
					
		if(valueUpdate == 0)
			{
				return Response.status(400).entity("Error 400 : Can not comment.").build();
			}
		else
			{
				return Response.status(200).entity("Code 200: Comment correctly added user'"+mail+"'.<a href=/SocialNetwoork>Kick Back !Meetup</a>").build();
			}
					
	}
}
