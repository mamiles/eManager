public class Tester
{


	public static void main( String[] args ) 
	{
		int current = 857;
		int past = 845;
		int percent = (int)(((float)(current-past)/past)*100);
		int changed = current-past;
		int  decimalchanged= changed/past;
		System.out.println("change = " + changed);
		System.out.println("decimalchange = " + decimalchanged);
		System.out.println("100*change = " + decimalchanged*100);
		System.out.println("percent = " + percent);
		System.exit(0);
	}
}
