package com.restful;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


import javax.ws.rs.*;
//import javax.ws.rs.PathParam;
//import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codehaus.jettison.json.JSONArray;

@Path("MeetupMain")
public class MeetupMain {

    @GET
    @Path("version")
    public String version() {
        String version = "1.0.0.5";
        return "The current version is " + version;
    }

    /*Get all Users from the database*/
    @GET
    @Path("/login")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUsers() throws Exception {

        String check = "SELECT * FROM users;";
        Connection con = DBclass.returnConnection();
        PreparedStatement getdata = con.prepareStatement(check);
        ResultSet rs = getdata.executeQuery(check);
        JSONArray array = (new ToJSON()).toJSONArray(rs);

        if (array.length() > 0) {
            return Response.ok("Users :" + array).build();
        } else {
            return Response.serverError().build();
        }
    }

    //insert an user

    @POST
    @Path("/signup")
    public Response addUsers(@FormParam("mail") String mail,
                             @FormParam("lastname") String lastname,
                             @FormParam("firstname") String firstname,
                             @FormParam("bio") String bio,
                             @FormParam("groups") String groups) throws Exception {
        Connection con = DBclass.returnConnection();
        Statement st = con.createStatement();
        int valueUpdate = st.executeUpdate("INSERT INTO users (email, firstname, lastname, bio, groups)" +
                "VALUES ('" + mail + "','" + firstname + "','" + lastname + "','" + bio + "','" + groups + "');");
        if (valueUpdate == 0) {
            return Response.status(400).entity("Error 400 : Can not create a new user '" + mail + "'.").build();
        } else {
            return Response.status(200).entity("Code 200: New user :'" + mail + "' added correctly. <a href=/SocialNetwoork>Kick Back !Meetup</a>").build();
        }
    }

    @POST
    @Path("/joinGroup")
    public Response joinGroup(@FormParam("firstname") String firstname,@FormParam("namegroup") String namegroup) throws Exception {
        Connection con = DBclass.returnConnection();
        Statement st = con.createStatement();
        int valueUpdate = st.executeUpdate("UPDATE groups ADD groupmembers='"+firstname +"' WHERE namegroup LIKE'" + namegroup + "';");
        if (valueUpdate == 0) {
            return Response.status(400).entity("Error 400 : Can not join group '" + namegroup + "'.").build();
        } else {
            return Response.status(200).entity("Code 200: joined group :'" + namegroup + "' <a href=/SocialNetwoork>Kick Back !Meetup</a>").build();
        }
    }

    //updateUser

    @POST
    @Path("/updateUser")
    public Response updateUser(@FormParam("mail") String mail,
                               @FormParam("lastname") String lastname,
                               @FormParam("firstname") String firstname,
                               @FormParam("bio") String bio)
            throws Exception {
        Connection con = DBclass.returnConnection();
        Statement st = con.createStatement();
        int valueUpdate = st.executeUpdate("UPDATE users SET email ='" + mail + "', firstname ='" + firstname + "', lastname ='" + lastname + "', bio ='" + bio + "'"
                + "WHERE email LIKE'" + mail + "';");
        if (valueUpdate == 0) {
            return Response.status(400).entity("Error 400 : Can not update an user.").build();
        } else {
            return Response.status(200).entity("Code 200: Updates correctly the user '" + mail + "'.<a href=/SocialNetwoork>Kick Back !Meetup</a>").build();
        }
    }

    @POST
    @Path("/updateGroup")
    public Response updateGroup(@FormParam("namegroup") String namegroup,@FormParam("namegroupnew") String namegroupnew,@FormParam("description") String description)throws Exception {
        Connection con = DBclass.returnConnection();
        Statement st = con.createStatement();
        int valueUpdate = st.executeUpdate("UPDATE groups SET namegroup ='" + namegroupnew + "', description ='" + description +"'"
                + "WHERE namegroup LIKE'" + namegroup + "';");
        if (valueUpdate == 0) {
            return Response.status(400).entity("Error 400 : Can not update group.").build();
        } else {
            return Response.status(200).entity("Code 200: Updates correctly the group '" + namegroup + "'.<a href=/SocialNetwoork>Kick Back !Meetup</a>").build();
        }
    }

    //deleteUser

    @POST
    @Path("/deleteUser")
    public Response deleteUser(@FormParam("mail") String mail)
            throws Exception {
        Connection con = DBclass.returnConnection();
        Statement st = con.createStatement();
        int valueUpdate = st.executeUpdate("DELETE FROM users "
                + "WHERE email LIKE'" + mail + "';");
        st.executeUpdate("DELETE FROM comments WHERE mail LIKE'" + mail + "';");
        if (valueUpdate == 0) {
            return Response.status(400).entity("Error 400 : Can not delete the user '" + mail + "'.").build();
        } else {
            return Response.status(200).entity("Code 200: Delete correctly the user '" + mail + "'.<a href=/SocialNetwoork>Kick Back !Meetup</a>").build();
        }
    }

    @POST
    @Path("/deleteGroup")
    public Response deleteGroup(@FormParam("namegroup") String namegroup)
            throws Exception {
        Connection con = DBclass.returnConnection();
        Statement st = con.createStatement();
        int valueUpdate = st.executeUpdate("DELETE FROM groups "
                + "WHERE namegroup LIKE'" + namegroup + "';");
        st.executeUpdate("DELETE FROM comments WHERE groupname LIKE'" + namegroup + "';");
        if (valueUpdate == 0) {
            return Response.status(400).entity("Error 400 : Can not delete the group '" + namegroup + "'.").build();
        } else {
            return Response.status(200).entity("Code 200: Delete correctly the group '" + namegroup + "'.<a href=/SocialNetwoork>Kick Back !Meetup</a>").build();
        }
    }

    // view an user
    @GET
    @Path("/viewUser")
    @Produces(MediaType.TEXT_HTML)
    public String viewUser(@QueryParam("mail") String mail)
            throws Exception {
        String email = null, firstname = null, lastname = null, bio = null, groups = null;
        Connection con = DBclass.returnConnection();
        Statement st = con.createStatement();
        ResultSet valueUpdate = st.executeQuery("SELECT * FROM users "
                + "WHERE email LIKE'" + mail + "';");
        while (valueUpdate.next()) {
            email = valueUpdate.getString("email");
            firstname = valueUpdate.getString("firstname");
            lastname = valueUpdate.getString("lastname");
            bio = valueUpdate.getString("bio");
            groups = valueUpdate.getString("groups");
        }

        return (
                "<html> " + "<title>" + "View User '" + firstname + "'</title>"
                        + "<body>" +
                        "<ul class=list-group>" +
                        "<li class=list-group-item>'" + firstname + "'</li>" +
                        "<li class=list-group-item>'" + lastname + "'</li>" +
                        "<li class=list-group-item>'" + email + "'</li>" +
                        "<li class=list-group-item>'" + bio + "'</li>" +
                        "<li class=list-group-item>'" + groups + "'</li>" +
                        "</ul>"
                        + "<a href=/SocialNetwoork>Kick Back !Meetup</a></body>"
                        + "</html>");
    }


    // View groups
    @GET
    @Path("/viewGroups")
    @Produces(MediaType.TEXT_HTML)
    public String viewGroups() throws SQLException {

        Connection con = DBclass.returnConnection();
        Statement st = con.createStatement();
        ResultSet valueUpdate = st.executeQuery("SELECT * FROM groups;");

        String html = (
                "<html> " + "<title>" + "View All Groups </title>"
                        + "<body><div class=col-lg-12>" +
                        " <table class=table table-hover>" +
                        "<thead>" +
                        "<tr>" +
                        "<th>Name group</th>" +
                        "<th>Descripition</th>" +
                        "<th>Admin</th>" +
                        "<th>Group Members</th>" +
                        "<th>Discussion</th>" +
                        "</tr>" +
                        "</thead>");
        while (valueUpdate.next()) {
            html = html + ("<tbody>" +
                    "<tr>" +
                    "<td><a href=http://http://elamine-pc:8080/SocialNetwoork/app/MeetupMain/comments/" + valueUpdate.getString("namegroup") + ">'" + valueUpdate.getString("namegroup") + "'</a></td>" +
                    "<td>'" + valueUpdate.getString("description") + "'</td>" +
                    "<td>'" + valueUpdate.getString("useradmin") + "'</td>" +
                    "<td>'" + valueUpdate.getString("groupmembers") + "'</td>" +
                    "<td>'" + valueUpdate.getString("discussion") + "'</td>" +
                    "</tr>");
        }

        html += ("</tbody></table></div><br>"
                + "<a href=/SocialNetwoork>Kick Back !Meetup</a></body>"
                + "</html>");


        return html;

    }

    // View comments


    @GET
    @Path("comments/{query}")
    @Produces(MediaType.TEXT_HTML)
    public String viewComments(@PathParam("query") String groupname) throws SQLException {

        Connection con = DBclass.returnConnection();
        Statement st = con.createStatement();
        ResultSet valueUpdate = st.executeQuery("SELECT * FROM comments WHERE groupname LIKE '" + groupname + "' ORDER BY date ASC;");

        String html = (
                "<html> " + "<title>" + "View All Comments </title>"
                        + "<body><div class=col-lg-12>" +
                        " <table class=table table-hover>" +
                        "<thead>" +
                        "<tr>" +
                        "<th>Name User </th>" +
                        "<th>Comment</th>" +
                        "<th>Date</th>" +
                        "</tr>" +
                        "</thead>" + "<tbody>");
        while (valueUpdate.next()) {
            html = html + "<tr>" + "<td>" + valueUpdate.getString("mail") + "</td>"
                    + "<td>" + valueUpdate.getString("comment") + "</td>" +
                    "<td>" + valueUpdate.getString("date") + "</td></tr>";
        }

        html += ("</tbody></table></div><br>"
                + "<a href=/SocialNetwoork/comments.html class=btn btn-info role=button>Comment</a><br>"
                + "<a href=/SocialNetwoork>Kick Back !</a></body>"
                + "</html>");

        return html;
    }

    // add a comments
    @POST
    @Path("/addComment")
    public Response addComment(@FormParam("group") String group, @FormParam("mail") String mail,
                               @FormParam("comments") String comments) throws SQLException {

        Connection con = DBclass.returnConnection();
        Statement st = con.createStatement();
        int valueUpdate = st.executeUpdate("INSERT INTO comments (mail,comment,groupname,date) VALUES ('" + mail + "','" + comments + "','" + group + "',NOW());");

        if (valueUpdate == 0) {
            return Response.status(400).entity("Error 400 : Can not comment.").build();
        } else {
            return Response.status(200).entity("Code 200: Comment correctly added user'" + mail + "'.<a href=/SocialNetwoork>Kick Back !Meetup</a>").build();
        }

    }

    @GET
    @Path("/connect")
    public String connect(@QueryParam("mail") String mail) throws Exception {
        String firstname = null;
        Connection con = DBclass.returnConnection();
        Statement st = con.createStatement();
        ResultSet valueUpdate = st.executeQuery("SELECT * FROM users "
                + "WHERE email LIKE'" + mail + "';");
        if (valueUpdate.next()) {
            firstname = valueUpdate.getString("firstname");
            return (
                    "<html> " + "<title>" + "Welcome '" + firstname + "'</title>"
                            + "<body>'" + firstname + "'<a href=/SocialNetwoork>Lets go!!!!</a></body></html>");
        }
        else{
            return(
                    "<html> " + "<title>" + "ERROR '" + firstname + "'</title>"
                            + "<body>ERROR-</body></html>");
        }
    }

    @POST
    @Path("/createGroup")
    public Response addGroup(@FormParam("namegroup") String namegroup,
                             @FormParam("description") String description,
                             @FormParam("userAdmin") String userAdmin,
                             @FormParam("groupMembers") String groupMembers,
                             @FormParam("discussion") String discussion) throws Exception {
        Connection con = DBclass.returnConnection();
        Statement st = con.createStatement();
        int valueUpdate = st.executeUpdate("INSERT INTO groups (namegroup, description, userAdmin, groupMembers, discussion)" +
                "VALUES ('" + namegroup + "','" + description + "','" + userAdmin + "','" + groupMembers + "','" + discussion + "');");
        if (valueUpdate == 0) {
            return Response.status(400).entity("Error 400 : Can not create a new Group '" + namegroup + "'.").build();
        } else {
            return Response.status(200).entity("Code 200: New group :'" + namegroup + "' created correctly. <a href=/SocialNetwoork>Kick Back !Meetup</a>").build();
        }
    }
}