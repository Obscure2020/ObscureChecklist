import java.util.*;
import java.io.*;

class Checklist{

    private static int countPad(PriorityQueue<Task> queue){
        int pad = 0;
        while(queue.size() > 0){
            Task t = queue.poll();
            int len = t.titleLength();
            if(len > pad) pad = len;
        }
        return pad;
    }

    private static void printQueue(PriorityQueue<Task> queue, int pad){
        while(queue.size() > 0){
            Task t = queue.poll();
            System.out.println(t.toPaddedString(pad));
        }
    }
    public static void main(String[] args) throws Exception{
        PriorityQueue<Task> tasks = new PriorityQueue<>();

        System.out.println("Reading from file...");
        Scanner fileScan = new Scanner(new File("Book.dat"));
        while(fileScan.hasNext()){
            Task t = Task.readFromFile(fileScan);
            tasks.add(t);
        }
        fileScan.close();
        System.out.println("Complete.");
        System.out.println();

        int pad = countPad(new PriorityQueue<Task>(tasks));
        printQueue(new PriorityQueue<Task>(tasks), pad);
        System.out.println();

        System.out.println("Saving to file...");
        File outfile = new File("outBook.dat");
        PrintWriter pw = new PrintWriter(outfile);
        while(tasks.size() > 0){
            Task t = tasks.poll();
            t.writeToFile(pw);
        }
        pw.close();
        System.out.println("Complete.");
    }

}