import java.io.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class Task implements Comparable<Task>{

    private String title;
    private String description;
    private Boolean type; //Object used instead of primitive on purpose: True -> Date-based, False -> Priority-Score-based, Null -> Not Fully Constructed
    private LocalDateTime deadline;
    private int priorityScore;
    private static final DateTimeFormatter dtFormat = DateTimeFormatter.ofPattern("M/dd/uuuu h:mm a");

    public Task(String title){
        this.title = title;
        type = null;
    }

    public void dateType(int year, int month, int day, int hour, int minute){
        type = true;
        deadline = LocalDateTime.of(year, month, day, hour, minute);
    }

    public void scoreType(int score){
        type = false;
        priorityScore = score;
    }

    public void describe(String description){
        this.description = description;
    }

    /* Objective-perspective logic for comparison:
    If both objects are null-type, then compare titles alphabetically.
    If one object is complete and the other is null, null always loses.
    If both objects are Date types:
        If dates match, compare titles alphabetically.
        If dates mismatch, earlier dates win.
    If both objects are Score types:
        If scores match, compare titles alphabetically.
        If scores mismatch, lower scores win.
    If one object is a Date type and the other is a Score type:
        If the Date type is overdue, Date type wins.
        If that didn't happen, and the Score type is less than or equal to 0, Score type wins.
        If that didn't happen, and Date type is 48 or fewer hours from now, Date type wins.
        If that didn't happen, and Score type is less than or equal to 5, Score type wins.
        If that didn't happen, Date type wins.
    */
    public int compareTo(Task other){
        if(type == null){
            //If I'm not a fully constructed Task:
            if(other.type == null){
                return title.compareTo(other.title);
            } else {
                return 1;
            }
        }
        //If I am a constructed Task:
        if(other.type == null) return -1;
        LocalDateTime now = LocalDateTime.now();
        if(type){
            if(other.type){
                //If we're both Date types:
                int dateCompare = deadline.compareTo(other.deadline);
                if(dateCompare==0) return title.compareTo(other.title);
                return dateCompare;
            }
            //If I'm a Date type and he's a Score type:
            if(now.until(deadline, ChronoUnit.MINUTES) < 0) return -1;
            if(other.priorityScore<=0) return 1;
            if(now.until(deadline, ChronoUnit.HOURS) <= 48) return -1;
            if(other.priorityScore<=5) return 1;
            return -1;
        }
        if(other.type){
            //If I'm a Score type and he's a Date type:
            if(now.until(other.deadline, ChronoUnit.MINUTES) < 0) return 1;
            if(priorityScore<=0) return -1;
            if(now.until(other.deadline, ChronoUnit.HOURS) <= 48) return 1;
            if(priorityScore<=5) return -1;
            return 1;
        }
        //If we're both Score types:
        int scoreCompare = priorityScore - other.priorityScore;
        if(scoreCompare==0) return title.compareTo(other.title);
        return scoreCompare;
    }

    private StringBuilder prepareString(){
        StringBuilder sb = new StringBuilder(title);
        sb.append(" | ");
        if(type == null){
            //Not Fully Constructed Case
            sb.append("NONE");
        } else {
            if(type){
                //Date-Based Case
                sb.append(deadline.format(dtFormat));
                LocalDateTime now = LocalDateTime.now();
                if(now.until(deadline, ChronoUnit.MINUTES) < 0){
                    sb.append(" --OVERDUE!--");
                }
            } else {
                //Score-Based Case
                sb.append(priorityScore);
            }
        }
        return sb;
    }

    public String toString(){
        StringBuilder sb = prepareString();
        return sb.toString();
    }

    public String toFullString(){
        StringBuilder sb = prepareString();
        if(description != null){
            sb.append('\n');
            sb.append(description);
        }
        return sb.toString();
    }

    public int titleLength(){
        return title.length();
    }

    public String toPaddedString(int pad){
        StringBuilder sb = prepareString();
        int len = title.length();
        for(int i=len; i<pad; i++) sb.insert(len, ' ');
        return sb.toString();
    }

    public void writeToFile(PrintWriter pw){
        pw.println(type);
        pw.println(title);
        if(type == null) return;
        if(type){
            //Date-Based Case
            pw.println(deadline.toEpochSecond(ZoneOffset.UTC));
        } else {
            //Score-Based Case
            pw.println(priorityScore);
        }
    }

    static Task readFromFile(Scanner scan){
        String readType = scan.nextLine();
        String readTitle = scan.nextLine();
        Task newTask = new Task(readTitle);
        if(readType.equals("null")) return newTask;
        if(readType.equals("true")){
            //Date-Based Case
            newTask.type = true;
            long epoch = Long.valueOf(scan.nextLine());
            newTask.deadline = LocalDateTime.ofEpochSecond(epoch, 0, ZoneOffset.UTC);
        } else {
            //Score-Based Case
            newTask.type = false;
            int score = Integer.valueOf(scan.nextLine());
            newTask.priorityScore = score;
        }
        return newTask;
    }

}