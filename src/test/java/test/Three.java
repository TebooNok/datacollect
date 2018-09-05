package test;

public class Three {
    public static void main(String[] args) {
        String[] testArr = {"Str1","Str2","Str3"};
            for(int i = 0; i < testArr.length; i++)
            {
                testArr[i] = testArr[i].substring(2);
            }

        for(String str:testArr)
        {
            System.out.println(str);
        }
    }
}
