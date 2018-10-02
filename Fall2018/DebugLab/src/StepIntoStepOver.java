/*
  Shows stepping into vs stepping over
  CNA337
  Zachary Rubin zrubin@rtc.edu
*/

import java.util.*;

public class StepIntoStepOver
{
  public static void main(String[] args)
  {
    int n = 10;
    countdown(n);
    countdown2(n);
  }

  private static int countdown(int num)
  {
    while(num >= 0)
    {
      System.out.println(num);
      num--;
    }
    return 0;
  }

  private static int countdown2(int number)
  {
    for(int i = number; i >=0; i--)
    {
      System.out.println(i);
    }
    return 0;
  }
}
