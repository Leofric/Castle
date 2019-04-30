package Castle;

import java.util.Scanner;

public class Tester {

	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		String value;
		String fub[] = new String [2];
		value = in.next();
		if (value.contains(",")){
			fub = value.split(",");
		}
		System.out.println(Integer.parseInt(fub [0]));
		System.out.println(Integer.parseInt(fub [1]));

		
	}

}
