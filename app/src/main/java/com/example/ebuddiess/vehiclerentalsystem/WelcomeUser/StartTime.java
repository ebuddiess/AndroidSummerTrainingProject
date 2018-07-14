package com.example.ebuddiess.vehiclerentalsystem.WelcomeUser;

import android.content.Context;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class StartTime {
    int hour,minute;
    int day,month,year;
    String selectedDate,currentDate,currentTime,selectedTime;
    Calendar  currentcalendar;
    SimpleDateFormat dateFormat,hourformat;

    void saveDate(String date){
        selectedDate  = date;
    }

    void saveDate(int day,int month,int year){
      this.day = day;
      this.year = year;
      this.month = month;
    }

    String getDate(){
        return selectedDate;
    }

    void saveTime(int hour,int minute){
        this.hour = hour;
        this.minute = minute;
    }

    String getFormattedDate(){
        return selectedDate+":"+selectedTime;
    }

    String getTime(){
        GregorianCalendar calendar = new GregorianCalendar(year,month,day,hour,minute);
        Date d = calendar.getTime();
        hourformat = new SimpleDateFormat("HH:mm");
        selectedTime= hourformat.format(d);
        return selectedTime;
    }

    boolean validate(String selectedDatetxt, String selectedTime, Context context){
        hourformat = new SimpleDateFormat("HH:mm");
        boolean status = false;
        currentTime = hourformat.format(Calendar.getInstance().getTime());
        this.selectedDate = selectedDatetxt;
        this.selectedTime = selectedTime;
        currentcalendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        currentDate =  dateFormat.format(currentcalendar.getTime());
        try {
            Date selectedTimedate = hourformat.parse(selectedTime);
            Date currenTimedate = hourformat.parse(currentTime);
            Calendar now = Calendar.getInstance();
            now.add(Calendar.HOUR,2);
            String futureTime = hourformat.format(now.getTime());
            Date futureTimedate = hourformat.parse(futureTime);
            Date selectedDate = dateFormat.parse(selectedDatetxt);
            Date currentdate = dateFormat.parse(currentDate);
            if(selectedDate.equals(currentdate)) {
                if((selectedTimedate.compareTo(currenTimedate)==0)||selectedTimedate.before(currenTimedate)){
                    Toast.makeText(context,"Minimum Booking is After 2 Hours of Current Time",Toast.LENGTH_SHORT).show();
                }else if(selectedTimedate.after(futureTimedate)){
                     status = true;
                }
            }

            if(selectedDate.before(currentdate)){
                Toast.makeText(context,"Invalid Date",Toast.LENGTH_SHORT).show();
            }else if(selectedDate.after(currentdate)){
                 return  true;
            }

        }catch (Exception e){
            Toast.makeText(context,e.getMessage().toString(),Toast.LENGTH_SHORT).show();
        }
      return  status;
    }



}
