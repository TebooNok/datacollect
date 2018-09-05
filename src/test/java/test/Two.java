package test;

import java.util.Scanner;

public class Two {
    public static void main(String[] args) {

        String input = new Scanner(System.in).nextLine();
        int maxLength = 0;
        int lastIndex = -1;
        int[] charNum = new int[255];
        int[] maxLenIndex = new int[255];

        for (int i = 0; i < 255; i++) {
            charNum[i] = 0;
            maxLenIndex[i] = 0;
        }
        for(int i = 0; i < input.length(); i++)
        {
            int index = (int)input.charAt(i);
            if (lastIndex == index)
            {
                charNum[index] = charNum[index]+1;
            }
            else
            {
                if (maxLenIndex[index] < charNum[index])
                {
                    maxLenIndex[index] = charNum[index];
                }

                charNum[index] = 1;
            }
            lastIndex = index;

            if (charNum[index] > maxLength)
            {
                maxLength = charNum[index];
            }
        }

        for (int i = 0; i < 255; i++) {
            charNum[i] = Math.max(charNum[i], maxLenIndex[i]);
        }

        for (int i = 0; i < 255; i++) {
            if (charNum[i] == maxLength)
            {
                for(int j = 0; j < maxLength; j++)
                {
                    System.out.print((char)i);
                }
                break;
            }
        }
    }
}
