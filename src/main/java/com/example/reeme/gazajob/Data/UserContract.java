package com.example.reeme.gazajob.Data;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.provider.BaseColumns;
import android.view.inputmethod.InputMethodManager;

public final class UserContract {

    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.

    private UserContract() {

    }



    public static final class userEntry implements BaseColumns {
        /**
         * Name of database table
         */
        public final static String TABLE_NAME = "users";


        //The names of columns.

        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_USER_NAME = "username";
        public final static String COLUMN_USER_EMAIL = "email";
        public final static String COLUMN_USER_PHONENUMBER = "phoneNumber";
        public final static String COLUMN_USER_JOBTITLE = "jobTitle";
        public final static String COLUMN_USER_NOTIFICATION = "notifications";
        public final static String COLUMN_USER_UID = "uid";
        public final static String COLUMN_USER_CEATED_AT = "created_at";




        /**
         * Possible values for the fields .
         */
        public static final int Humanities = 0;
        public static final int sales_and_marketing= 1;
        public static final int Public_Relations_and_Communications = 2;
        public static final int Press_and_media= 3;
        public static final int Various_fields = 4;
        public static final int Operations_and_Logistics_Support= 5;
        public static final int Law_and_law = 6;
        public static final int Information_Technology= 7;
        public static final int Hospitality_and_Tourism= 8;
        public static final int Medicine_Nursing_and_Public_Health = 9;
        public static final int Graphic_Design = 10;
        public static final int Languages_and_translation = 11;
        public static final int Accountants_and_Financial_Sciences= 12;
        public static final int Engineering=13;
        public static final int Education_and_training= 14;
        public static final int Culture_and_Arts = 15;
        public static final int Management_and_Business= 16;


        /**
         * Possible values for the jobTitle of the user.
         */
        public static final int secretary = 0;
        public static final int accountant = 1;
        public static final int Clerk = 2;
        public static final int English_Interpreter = 3;
        public static final int chemist = 4;
        public static final int Dentist = 5;
        public static final int graphic_Designer = 6;
        public static final int UI_UX_designer = 7;
        public static final int Mobile_apps_Programmer = 8;
        public static final int Front_End_Web_Developer = 9;
        public static final int Back_End_Web_Developer = 10;
        public static final int Full_Stack_Developer = 11;
        public static final int Interior_designer_and_architect = 12;
        public static final int ElECTRICAL_ENGINEER = 13;
        public static final int Communications_and_control_engineer = 14;
        public static final int CIVIL_ENGINEER = 15;


        public static boolean isValidJobTitle(int job) {
            if (job ==secretary|| job == accountant || job ==Clerk
                    ||job ==English_Interpreter|| job ==chemist|| job ==Dentist
                    || job ==graphic_Designer|| job ==UI_UX_designer
                    || job ==Mobile_apps_Programmer|| job ==Front_End_Web_Developer|| job ==Back_End_Web_Developer
                    || job ==Full_Stack_Developer || job ==Interior_designer_and_architect|| job ==ElECTRICAL_ENGINEER
                    || job ==Communications_and_control_engineer|| job ==CIVIL_ENGINEER) {
                return true;
            }
            return false;
        }
    }

    public static  final  class SignUpUserEntry implements BaseColumns{

        /**
         * Name of database table
         */
        public final static String TABLE_NAME = "user";


        //The names of columns.

        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_USER_NAME = "name";
        public final static String COLUMN_USER_EMAIL = "email";
        public final static String COLUMN_USER_PHONENUMBER = "jawwal";
        public final static String COLUMN_USER_JOBTITLE = "job_name";
        public final static String COLUMN_USER_NOTIFJAWWAL= "jawwalalert";
        public final static String COLUMN_USER_NOTIFEMAIL = "emailalert";
        public final static String COLUMN_USER_UID = "uid";
        public final static String COLUMN_USER_CEATED_AT = "created_at";

        }






    /**
     *  Hide Keyboard
     */
    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }

    public static final String Full_time = "دوام كامل";
    public static final String Part_time = "دوام جزئي";
    public static final String Part_Full_time ="شغل بالقطعة";




        }




