package com.example.ebuddiess.vehiclerentalsystem.WelcomeUser;

import android.content.Context;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

public class EndTime {
    int day,month,year;
    Context context;
    String endDate;
    String endtime;
    SimpleDateFormat hourformat;
    SimpleDateFormat dateFormat;

    void store(int day,int month,int year){
        this.day = day;
        this.month = month;
        this.year = year;
    }

    void saveDate(String date){
        this.endDate = date;
    }

    String getDate(){
        return endDate;
    }

    void saveTime(int hour, int minute){
        GregorianCalendar calendar = new GregorianCalendar(year,month,day,hour,minute);
        Date d = calendar.getTime();
        hourformat = new SimpleDateFormat("HH:mm");
        endtime= hourformat.format(d);
    }

    String  getTime(){
      return endtime;
    }


    String getFormattedDate(){
        return endDate+":"+endtime;
    }

    public boolean  validate(String endDate, String endtime, String startDate, String starttime, Context context) {
        this.context = context;
        boolean status = false;
        dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        hourformat = new SimpleDateFormat("HH:mm");
        try {
            Date startDatedata = dateFormat.parse(startDate);
            Date endDatedata   = dateFormat.parse(endDate);
            Date starttimedata = hourformat.parse(starttime);
            Date endtimedata  =  hourformat.parse(endtime);

            if(endDatedata.equals(startDatedata)){
                Toast.makeText(context,"NO BOOKING FOR SAME DATE",Toast.LENGTH_LONG).show();
            }else if(endDatedata.before(startDatedata)){
                Toast.makeText(context,"INVALID DATE",Toast.LENGTH_LONG).show();
            }else if(endDatedata.after(startDatedata) && endtimedata.before(starttimedata)){
                Toast.makeText(context,"BOOK FOR ALTEAST 24 HOURS",Toast.LENGTH_LONG).show();
            }else if(endDatedata.after(startDatedata)&& endtimedata.after(starttimedata)) {
                status = true;
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return  status;
    }
}
