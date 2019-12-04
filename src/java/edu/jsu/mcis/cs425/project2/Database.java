package edu.jsu.mcis.cs425.project2;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class Database {
    
    public Connection getConnection() throws SQLException, NamingException {
        
        Connection conn = null;
        
        try {    
            
            Context envContext = new InitialContext();
            Context initContext  = (Context)envContext.lookup("java:/comp/env");
            DataSource ds = (DataSource)initContext.lookup("jdbc/db_pool");
            conn = ds.getConnection(); 
            
        }
        
        catch (SQLException | NamingException e) {}
        
        return conn;
    }
    
    public HashMap getUserInfo(String username) {
        
         String query = "SELECT * FROM user WHERE username = ?";
         
         HashMap<String, String> results = new HashMap<String, String>();
         
         int id;
        
        try {
            
            Connection conn = getConnection();
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, username);
            boolean hasResults = statement.execute();
            
            if (hasResults) {
                
                ResultSet resultset = statement.getResultSet();
                
                if (resultset.next()) {
                    
                    id = resultset.getInt("id");
                    results.put("userid", String.valueOf(id));
                    results.put("displayname", resultset.getString("displayname"));
                }
                
                resultset.close();
                
            }
            
            statement.close();
            
        }
        
        catch (Exception SQLException) {}
        
        return results;
        
    }

    String getSkillsListAsHTML(int userid) {
        
        String query = "SELECT skills.*,a.userid FROM cs425_p2.skills AS skills LEFT JOIN\n" +
        "(SELECT * FROM cs425_p2.applicants_to_skills WHERE userid = ?) AS a ON skills.id = a.skillsid;";
        
        StringBuilder result = new StringBuilder();        
        String skill;
        
        try {
            
            Connection conn = getConnection();
            
            PreparedStatement statement = conn.prepareStatement(query);
                
            statement.setString(1, Integer.toString(userid));
            boolean hasResults = statement.execute();
                
            ResultSet resultset = statement.getResultSet();
                    
            while (resultset.next()) {
                        
                String description = resultset.getString("description");
                int id = resultset.getInt("id");
                int user = resultset.getInt("userid");
                        
                result.append("<input type=\"checkbox\" name=\"skills\" value=\"");
                result.append(id);
                result.append("\" id=\"skills_").append(id).append("\" ");
                        
                if (user != 0) {
                            
                    result.append("checked");
                            
                }
                        
                result.append(">");
                result.append("<label for=\"skills_").append(id).append("\">").append(description).append("</label><br /><br />");
                        
            }
                    
            resultset.close();
            statement.close();
        }
   
        catch (Exception SQLException) {}
        
        skill = result.toString();
        
        return skill;
    }
    
    public void setSkillsList(int userid, String[] skills) {
        
        String delQuery = "DELETE FROM cs425_p2.applicants_to_skills WHERE userid = ?;";
        
        String insQuery = "INSERT INTO cs425_p2.applicants_to_skills (userid, skillsid) VALUES (?, ?);";
        
        try {
            
            Connection conn = getConnection();
            PreparedStatement statement = conn.prepareStatement(delQuery);
            statement.setString(1, Integer.toString(userid));
            int alter = statement.executeUpdate();
            statement = conn.prepareStatement(insQuery);
                
            for (int i = 0; i < skills.length; i++) {
                    
                statement = conn.prepareStatement(insQuery);
                statement.setString(1, Integer.toString(userid));
                statement.setString(2, skills[i]);
                statement.execute();           
                    
            }
        }
        
        catch (Exception SQLException) {}
    }

    String getJobsListAsHTML(int userid, String[] skills) {
        
        String query = "SELECT jobs.id, jobs.name, a.userid FROM jobs LEFT JOIN\n" +
        "(SELECT * FROM applicants_to_jobs WHERE userid = ?) AS a ON jobs.id = a.jobsid\n" +
        "WHERE jobs.id IN (SELECT jobsid AS id FROM (applicants_to_skills JOIN skills_to_jobs\n" +
        "ON applicants_to_skills.skillsid = skills_to_jobs.skillsid) WHERE applicants_to_skills.userid = ?) ORDER BY jobs.name;";
        
        StringBuilder results = new StringBuilder();
        
        try {
            
            Connection conn = getConnection();
            PreparedStatement statement = conn.prepareStatement(query);
            
            statement.setString(1, Integer.toString(userid));
            statement.setString(2, Integer.toString(userid));
            statement.execute();
            
            ResultSet resultset = statement.getResultSet();
            
            while (resultset.next()) {
                
                int id = resultset.getInt("id");
                String name = resultset.getString("name");
                int userId = resultset.getInt("userid");
                
                results.append("<input type=\"checkbox\" name=\"jobs\" value=\"");
                results.append(id);
                results.append("\" id=\"job_").append(id).append("\" ");
                
                if (userId != 0) {
                    
                    results.append("checked");
                    
                }
                
                results.append(">");
                
                results.append("<label for=\"job_").append(id).append("\">").append(name).append("</label><br /><br />");
            }
        }
        
        catch(Exception SQLException){}
        
        return results.toString();
    }

    void setJobsList(int userid, String[] jobs) {
        
        String delQuery = "DELETE FROM cs425_p2.applicants_to_jobs WHERE userid = ?;";
        String insQuery = "INSERT INTO cs425_p2.applicants_to_jobs (userid, jobsid) VALUES (?, ?);";
        
        try {
            
            Connection conn = getConnection();
            PreparedStatement statement = conn.prepareStatement(delQuery);
            statement.setString(1, Integer.toString(userid));
            int alter = statement.executeUpdate();
            
            statement = conn.prepareStatement(insQuery);
            
            for (int i = 0; i < jobs.length; i++) {
                
                statement = conn.prepareStatement(insQuery);
                statement.setString(1, Integer.toString(userid));
                statement.setString(2, jobs[i]);
                statement.execute();      
                
            }
        }
        catch (Exception SQLException) {}
    }
    
}