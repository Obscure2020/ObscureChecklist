import java.util.*;
import java.io.*;

class Checklist{

    private static void printQueue(PriorityQueue<Task> queue){
        PriorityQueue<Task> padCount = new PriorityQueue<>(queue);
        int pad = 0;
        while(padCount.size() > 0){
            Task t = padCount.poll();
            int len = t.titleLength();
            if(len > pad) pad = len;
        }
        while(queue.size() > 0){
            Task t = queue.poll();
            System.out.println(t.toPaddedString(pad));
        }
    }

    private static void printTopFive(PriorityQueue<Task> queue){
        printTitle("Current Top Five Tasks:");
        PriorityQueue<Task> padCount = new PriorityQueue<>(queue);
        int pad = 0;
        for(int i=0; i<5; i++){
            if(padCount.size() == 0) break;
            Task t = padCount.poll();
            int len = t.titleLength();
            if(len > pad) pad = len;
        }
        for(int i=0; i<5; i++){
            if(queue.size() == 0) break;
            Task t = queue.poll();
            System.out.println(t.toPaddedString(pad));
        }
    }

    private static int menuSelect(int max, Scanner scan){
        boolean valid = false;
        int input = 0;
        while(!valid){
            System.out.print("Selection: ");
            try{
                input = scan.nextInt();
            } catch(Exception e){
                System.out.println("INVALID INPUT.");
                scan.nextLine();
                continue;
            }
            if(input < 0){
                System.out.println("NEGATIVES DISALLOWED.");
                continue;
            }
            if(input > max){
                System.out.println("TOO LARGE.");
                continue;
            }
            valid = true;
        }
        scan.nextLine();
        return input;
    }

    private static void fakeClear(){
        for(int i=0; i<200; i++) System.out.println();
    }

    private static int getInt(String prompt, Scanner scan){
        boolean valid = false;
        int input = 0;
        while(!valid){
            System.out.print(prompt);
            try{
                input = scan.nextInt();
            } catch(Exception e){
                System.out.println("INVALID INPUT.");
                scan.nextLine();
                continue;
            }
            valid = true;
        }
        scan.nextLine();
        return input;
    }

    private static void initializeTask(Task newTask, Scanner scan){
        System.out.println("0 - NULL / 1 - Date / 2 - Score");
        int mode = menuSelect(2, scan);
        if(mode == 0) return;
        System.out.println();
        if(mode == 1){
            //dateType(int year, int month, int day, int hour, int minute)
            int year = getInt("Year: ", scan);
            int month = getInt("Month: ", scan);
            int day = getInt("Day: ", scan);
            int hour = getInt("Hour: ", scan);
            int minute = getInt("Minute: ", scan);
            newTask.dateType(year, month, day, hour, minute);
        } else {
            int score = getInt("Score: ", scan);
            newTask.scoreType(score);
        }
        return;
    }

    private static Task selectTask(PriorityQueue<Task> tempQueue, Scanner scan){
        ArrayList<Task> tempList = new ArrayList<>(tempQueue.size());
        int pad = 0;
        while(tempQueue.size()>0){
            Task t = tempQueue.poll();
            int len = t.titleLength();
            if(len > pad) pad = len;
            tempList.add(t);
        }
        int numPad = String.valueOf(tempList.size() - 1).length();
        for(int i=0; i<tempList.size(); i++){
            int numLen = String.valueOf(i).length();
            for(int p=numLen; p<numPad; p++) System.out.print(' ');
            System.out.println(i + " -> " + tempList.get(i).toPaddedString(pad));
        }
        System.out.println();
        System.out.println(tempList.size() + " -> Cancel operation.");
        System.out.println();
        int selection = menuSelect(tempList.size(), scan);
        if(selection == tempList.size()) return null;
        return tempList.get(selection);
    }

    public static void printTitle(String input){
        System.out.println("  " + input + "  ");
        int len = input.length() + 4;
        for(int i=0; i<len; i++) System.out.print('=');
        System.out.println();
    }

    public static void main(String[] args) throws Exception{
        PriorityQueue<Task> tasks = new PriorityQueue<>();
        boolean modified = false;

        //Read in tasks from Book.dat
        fakeClear();
        System.out.println("Reading from file...");
        Scanner fileScan = new Scanner(new File("Book.dat"));
        while(fileScan.hasNext()){
            Task t = Task.readFromFile(fileScan);
            tasks.add(t);
        }
        fileScan.close();
        System.out.println("Complete.");
        System.out.println();

        printTopFive(new PriorityQueue<Task>(tasks));
        System.out.println();

        //Menu Loop
        Scanner scan = new Scanner(System.in);
        while(true){
            System.out.println("0 - Exit / 1 - Full List / 2 - New Task / 3 - Remove Task");
            int selection = menuSelect(3, scan);

            //I know you're supposed to use a Switch instead of a bunch of IFs, but I just don't like how variable scope works within a Switch.

            if(selection == 0) break;

            fakeClear();

            if(selection == 1){
                printQueue(new PriorityQueue<Task>(tasks));
                System.out.println();
                continue;
            }

            if(selection == 2){
                printTitle("Creating New Task");
                System.out.println();
                System.out.print("Title: ");
                String title = scan.nextLine();
                System.out.println();
                Task newTask = new Task(title);
                initializeTask(newTask, scan);
                tasks.add(newTask);
                fakeClear();
                System.out.println('[' + title + "] has been added.");
                System.out.println();
                printTopFive(new PriorityQueue<Task>(tasks));
                System.out.println();
                modified = true;
                continue;
            }

            if(selection == 3){
                printTitle("Removing Task");
                System.out.println();
                Task selectedTask = selectTask(new PriorityQueue<Task>(tasks), scan);
                fakeClear();
                if(selectedTask == null){
                    System.out.println("Removal operation cancelled.");
                } else {
                    tasks.remove(selectedTask);
                    System.out.println('[' + selectedTask.toString() + "] has been removed.");
                    modified = true;
                }
                System.out.println();
                printTopFive(new PriorityQueue<Task>(tasks));
                System.out.println();
                continue;
            }
        }
        scan.close();
        System.out.println();

        if(modified){ //Only write new file if it's actually necessary.
            //Save tasks to outBook.dat
            //I'm writing out to a different file than I read in from just in case some sort of error occurs during the write-out process.
            //If I write out to outBook without modifying Book, then data isn't completely lost from disk if this process is interrupted.
            //I let my Launch.bat turn outBook.dat into Book.dat if this process was successful.
            System.out.println("Saving to file...");
            File outfile = new File("outBook.dat");
            PrintWriter pw = new PrintWriter(outfile);
            while(tasks.size() > 0){
                Task t = tasks.poll();
                t.writeToFile(pw);
            }
            pw.close();
            System.out.println("Complete.");
        } else {
            System.out.println("No modifications were performed.");
            System.out.println("Writing skipped.");
        }
    }

}