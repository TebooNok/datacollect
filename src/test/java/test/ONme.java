package test;

import java.util.Scanner;

public class ONme {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String input = sc.nextLine();
        boolean[] isAppear = new boolean[255];
        for (int i = 0; i < 255; i++) {
            isAppear[i] = false;
        }

        for(int i = 0; i < input.length(); i++)
        {
            int index = (int)input.charAt(i);
            isAppear[index] = true;
        }

        for (int i = 0; i < 255; i++) {
            if (isAppear[i])
            {
                System.out.print((char)i);
            }
        }
    }
}
