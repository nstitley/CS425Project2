package edu.jsu.mcis.cs425.project2;

import java.util.HashMap;

public class BeanApplicant {
    
    private String displayname;
    private String username;
    private int userid;
    private String[] jobs;
    private String[] skills;

    public String getUsername() {
        
        return username;
        
    }
    
    public void setUsername(String username) {
        
        this.username = username;
        
    }
    
    public String getDisplayName() {
        
        return displayname;
        
    }
    
    public void setDisplayName(String displayname) {
        
        this.displayname = displayname;
        
    }
    
    public int getUserId() {
        
        return userid;
        
    }
    
    public void setUserId(int userid) {
        
        this.userid = userid;
        
    }
    
    public String[] getSkills() {
        
        return skills;
        
    }
    
    public void setSkills(String[] skills) {
        
        this.skills = skills;
        
    }
    
    public String[] getJobs() {
        
        return jobs;
        
    }
    
    public void setJobs(String[] jobs) {
        
        this.jobs = jobs;
        
    }
    
    public void setUserInfo() {
        
        Database db = new Database();
        HashMap<String, String> userInfo = db.getUserInfo(username);
        userid = Integer.parseInt(userInfo.get("userid"));
        displayname = userInfo.get("displayname");
        
    }
    
    public String getSkillsList() {
        
        Database db = new Database();
        return ( db.getSkillsListAsHTML(userid) );
        
    }
    
    public void setSkillsList() {
        
        Database db = new Database();
        db.setSkillsList(userid, skills);
        
    }
    
    public String getJobsList() {
        
        Database db = new Database();
        return ( db.getJobsListAsHTML(userid, skills) );
        
    }
    
    public void setJobsList() {
        
        Database db = new Database();
        db.setJobsList(userid, jobs);
        
    }

    int getUserid() {
        
        throw new UnsupportedOperationException("Not supported");
        
    }
    
}
