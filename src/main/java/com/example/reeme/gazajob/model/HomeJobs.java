package com.example.reeme.gazajob.model;

public class HomeJobs {


    public static final String TABLE_NAME = "homeJobs";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_JOBTITLE= "jobTitle";
    public static final String COLUMN_COMPANYNAME = "companyName";
    public static final String COLUMN_POSTDATE = "postDate";
    public static final String COLUMN_SALARY = "salary";
    public static final String COLUMN_FINALDATE = "finalDate";
    public static final String COLUMN_JOBETYPE = "jobType";
    public static final String COLUMN_SPECIFACATION = "specification";
    public static final String COLUMN_REQUIREMENMTS = "requirements";
    public static final String COLUMN_COMPANYADDRESS = "companyAddress";


    // Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_JOBTITLE + " TEXT,"
                    + COLUMN_COMPANYNAME + " TEXT,"
                    +COLUMN_POSTDATE + "TEXT,"
                    +COLUMN_SALARY +"TEXT,"
                    +COLUMN_FINALDATE +"TEXT,"
                    +COLUMN_JOBETYPE +"TEXT,"
                    +COLUMN_SPECIFACATION +"TEXT,"
                    +COLUMN_REQUIREMENMTS +"TEXT,"
                    +COLUMN_COMPANYADDRESS +"TEXT"
                    + ")";


    private int carrerId,id;
    private String jobtitle,companyname,postdate,salary,finaldate,jobtype,specification,requirements,companyaddress,email_company;


    public HomeJobs(){

    }

    public HomeJobs(int id, String jobtitle, String companyname, String postdate,String salary, String finaldate, String jobtype, String specification, String requirements, String companyaddress,int careerId,String email_company) {
        this.id = id;
        this.jobtitle = jobtitle;
        this.companyname = companyname;
        this.postdate=postdate;
        this.salary = salary;
        this.finaldate = finaldate;
        this.jobtype = jobtype;
        this.specification = specification;
        this.requirements = requirements;
        this.companyaddress = companyaddress;
        this.carrerId=careerId;
        this.email_company=email_company;
    }

    public String getEmailCompany() {
        return email_company;
    }

    public void setEmailCompany(String email_company) {
        this.email_company = email_company;
    }

    public int getCarrerId() {
        return carrerId;
    }

    public void setCarrerId(int carrerId) {
        this.carrerId = carrerId;
    }

    public String getPostdate() {
        return postdate;
    }

    public void setPostdate(String postdate) {
        this.postdate = postdate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getJobtitle() {
        return jobtitle;
    }

    public void setJobtitle(String jobtitle) {
        this.jobtitle = jobtitle;
    }

    public String getCompanyname() {
        return companyname;
    }

    public void setCompanyname(String companyname) {
        this.companyname = companyname;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public String getFinaldate() {
        return finaldate;
    }

    public void setFinaldate(String finaldate) {
        this.finaldate = finaldate;
    }

    public String getJobtype() {
        return jobtype;
    }

    public void setJobtype(String jobtype) {
        this.jobtype = jobtype;
    }

    public String getSpecification() {
        return specification;
    }

    public void setSpecification(String specification) {
        this.specification = specification;
    }

    public String getRequirements() {
        return requirements;
    }

    public void setRequirements(String requirements) {
        this.requirements = requirements;
    }

    public String getCompanyaddress() {
        return companyaddress;
    }

    public void setCompanyaddress(String companyaddress) {
        this.companyaddress = companyaddress;
    }
}

