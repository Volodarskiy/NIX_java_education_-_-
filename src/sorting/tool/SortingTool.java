package sorting.tool;

import java.util.*;
import java.io.*;

public class SortingTool {
    private int Mode = 0;
    private int Sort = 0;
    private String outPutFile = "";

    List<String> list = new ArrayList<>();

    public static void main(String[] args) throws Exception {
        sorting.tool.SortingTool st = new sorting.tool.SortingTool();

        st.readArgs(args);
    }

    public void readArgs(String[] args) throws Exception {
        if (args.length == 0) {
            return;
        }
        String inputFile = "";
        for (int i = 1; i < args.length; i+=2) {

            switch (args[i-1])
            {
                case "-dataType":
                    if(!args[i].isEmpty() && !args[i].equals("-")) {
                        this.setMode(args[i]);
                    }
                    else {
                        System.out.println("No data type defined");
                        return;
                    }
                    break;
                case "-sortType":
                    if(!args[i].isEmpty() && !args[i].equals("-")) {
                        this.setMode(args[i]);
                    }
                    else {
                        System.out.println("No sorting type defined");
                        return;
                    }
                    break;
                case "-outputFile":
                    if(!args[i].isEmpty() && !args[i].contains("-")) {
                        this.outPutFile = args[i];
                    }
                    else {
                        System.out.println("No outputFile name defined");
                        return;
                    }
                    break;
                case "-inputFile":
                    if(!args[i].isEmpty() && !args[i].contains("-")) {
                        inputFile = args[i];
                    }
                    else {
                        System.out.println("No inputFile name defined");
                        return;
                    }
                    break;
                default: System.out.printf("%s is not a valid parameter. It will be skipped\n", args[i-1]);
            }
        }
        if(inputFile.isEmpty()) {
            this.createList(new Scanner(System.in));
        }
        else {
            createListFile(inputFile);
        }
    }

    private void setMode(String inp) {
        switch (inp.toLowerCase()) {
            case "long":
                Mode = 0;
                break;
            case "line":
                Mode = 2;
                break;
            case "word":
                Mode = 1;
                break;
            case "natural":
                Sort = 2;
                break;
            case "bycount":
                Sort = 1;
                break;
        }
    }

    private void createListFile(String fileName) throws Exception {
        FileReader fr = new FileReader(fileName);
        Scanner scan = new Scanner(fr);

        createList(scan);
        fr.close();
    }
    private void createList(Scanner scan) throws Exception {

        while (scan.hasNext()) {
            String str = scan.nextLine();

            if (str.isEmpty()) {
                continue;
            }
            if (Mode == 2) {
                list.add(str.trim());
            } else {
                String[] words = str.trim().split(" ");
                for (String w : words) {
                    if (w.isEmpty()) {
                        continue;
                    }
                    if(Mode == 0) {
                        if(isNotLong(w)) {
                            System.out.println(w + " is not a long. It will be skipped.");
                            continue;
                        }
                    }
                    list.add(w);
                }
            }
        }
        scan.close();
        this.checkMode();
    }

    private void checkMode() throws Exception {
        if (list.isEmpty()) {
            return;
        }
        if (Sort == 2) {
            this.sortNat();
        }
        else if (Sort == 1) {
            this.sortCount();
        }
    }

    private void sortNat() throws Exception {
        StringBuilder builder = new StringBuilder();
        if(Mode == 0) {
            builder.append(sortInt());
        }
        else if(Mode == 1) {

            Collections.sort(list);
            for (String s : list) {
                builder.append(s).append(" ");
            }
        }
        else if(Mode == 2) {
            builder.append(sortLine());
        }
        int totalNum = list.size();
        String[] word = getW(Mode);
        String message = String.format("Total %s: %d \nSorted data: %s", word[0], totalNum, builder);

        if(this.outPutFile.isEmpty()) {
            System.out.println(message);
        }
        else {
            this.writeToFile(message);
        }
    }

    private String sortInt() {
        List<Integer> iList = new ArrayList<>();

        for (String s : list) {
            iList.add(Integer.valueOf(s));
        }
        Collections.sort(iList);
        StringBuilder builder = new StringBuilder();
        for (int s : iList) {
            builder.append(s).append(" ");
        }
        return builder.toString();
    }

    private String sortLine() {
        Comparator<String> comp = new sorting.tool.StrComp();
        list.sort(comp);
        Collections.reverse(list);
        StringBuilder builder = new StringBuilder();
        for (String s : list) {
            builder.append("\n").append(s);
        }
        return builder.toString();
    }
    private void sortCount () throws Exception {
        List<sorting.tool.Counter> countedList = new ArrayList<>();

        for (String s : list) {
            countedList.add(new sorting.tool.Counter(s, 1));
        }
        for(int i = 0; i < countedList.size(); i++) {
            for(int j = i+1; j < countedList.size(); j++) {
                if(i == j) {
                    continue;
                }
                sorting.tool.Counter el1 = countedList.get(i);
                sorting.tool.Counter el2 = countedList.get(j);
                if(el1.equals(el2)) {
                    el1.setValue(el1.getValue() + el2.getValue());
                    countedList.remove(j);
                    j--;
                }
            }
        }
        Collections.sort(countedList);

        int totalNum = list.size();
        StringBuilder builder = new StringBuilder();
        String[] word = getW(Mode);

        for(sorting.tool.Counter c: countedList) {
            builder.append(c.getString()).append(" : ").append(c.getValue()).append(" time(s),").append(c.getValue() * 100 / totalNum).append("%\n");
        }
        String message = String.format("Total %s: %d\n%s\n", word[0], totalNum, builder);
        if(this.outPutFile.isEmpty()) {
            System.out.println(message);
        }
        else {
            this.writeToFile(message);
        }
    }
    private void writeToFile(String text) throws Exception {
        FileWriter nFile = new FileWriter(outPutFile);

        nFile.write(text);
        nFile.close();
    }
    private String[] getW(int num) {
        final String[][] dict = {
                {"numbers", "greatest number"},
                {"words", "longest word"},
                {"lines", "longest line"}
        };
        return dict[num];
    }
    private boolean isNotLong(String input)
    {
        for(int i = 0; i < input.length(); i++) {
            if(input.charAt(i) < '0' || input.charAt(i) > '9') {
                return true;
            }
        }
        return false;
    }
}

class StrComp implements Comparator<String>{
    @Override
    public int compare(String o1, String so2) {
        if(o1.length() > so2.length()) {
            return 1;
        } else if(o1.length() < so2.length()) {
            return -1;
        }
        return 0;
    }
}
class Counter implements Comparable<sorting.tool.Counter> {
    private int value;
    private final String text;

    public Counter(String text, int value) {
        this.text = text;
        this.value = value;
    }
    public int compareTo(sorting.tool.Counter anotherInstance) {
        if(this.value != anotherInstance.value) {
            return this.value - anotherInstance.value;
        }
        return anotherInstance.text.length() - this.text.length();
    }
    public void setValue(int value) {
        this.value = value;
    }
    public int getValue() {
        return this.value;
    }
    public String getString() {
        return this.text;
    }
    @Override
    public String toString() {
        return String.format("%s : %d time(s)", text, value);
    }
    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }
        sorting.tool.Counter sec = (sorting.tool.Counter) obj;
        return this.text.equals(sec.text);
    }
}