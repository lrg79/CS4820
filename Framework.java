import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.*;

public class Framework
{
	int n; // number of men (women)

	int MenPrefs[][]; // preference list of men (n*n)
	int WomenPrefs[][]; // preference list of women (n*n)

	ArrayList<MatchedPair> MatchedPairsList; // your output should fill this arraylist which is empty at start

	public class MatchedPair
	{
		int man; // man's number
		int woman; // woman's number

		public MatchedPair(int Man,int Woman)
		{
			man=Man;
			woman=Woman;
		}

		public MatchedPair()
		{
		}
	}

	// reading the input
	void input(String input_name)
	{
		File file = new File(input_name);
		BufferedReader reader = null;

		try
		{
			reader = new BufferedReader(new FileReader(file));

			String text = reader.readLine();

			String [] parts = text.split(" ");
			n=Integer.parseInt(parts[0]);

			MenPrefs=new int[n][n];
			WomenPrefs=new int[n][n];

			for (int i=0;i<n;i++)
			{
				text=reader.readLine();
				String [] mList=text.split(" ");
				for (int j=0;j<n;j++)
				{
					MenPrefs[i][j]=Integer.parseInt(mList[j]);
				}
			}

			for (int i=0;i<n;i++)
			{
				text=reader.readLine();
				String [] wList=text.split(" ");
				for(int j=0;j<n;j++)
				{
					WomenPrefs[i][j]=Integer.parseInt(wList[j]);
				}
			}

			reader.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	// writing the output
	void output(String output_name)
	{
		try
		{
			PrintWriter writer = new PrintWriter(output_name, "UTF-8");

			for(int i=0;i<MatchedPairsList.size();i++)
			{
				writer.println(MatchedPairsList.get(i).man+" "+MatchedPairsList.get(i).woman);
			}

			writer.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	public Framework(String []Args)
	{
		input(Args[0]);

		MatchedPairsList=new ArrayList<MatchedPair>(); // you should put the final stable matching in this array list

		/* NOTE
		 * if you want to declare that man x and woman y will get matched in the matching, you can
		 * write a code similar to what follows:
		 * MatchedPair pair=new MatchedPair(x,y);
		 * MatchedPairsList.add(pair);
		*/

		//YOUR CODE STARTS HERE
		//hashmap layout:
		// woman -> (man -> pref number)
		// men -> (woman -> pref number)
		//-1 means not married
		HashMap <Integer, HashMap<Integer, Integer>> wmap = new HashMap<Integer, HashMap<Integer, Integer>>();
		HashMap<Integer, Integer> wstatus = new HashMap<Integer, Integer>(); //keeps track woman married or not
		Queue<Integer> queue = new LinkedList<Integer>();
		
		for(int i = 0; i < n; i++){
			wmap.put(i,  new HashMap<Integer, Integer>());
			wstatus.put(i, -1);
			for(int j = 0; j < n; j++){
				wmap.get(i).put(WomenPrefs[i][j], j);
			}
			queue.add(i);
		}
		
		//while we still have unmarried men
		while(!queue.isEmpty()){
			int man = queue.poll();
			int[] manPref = MenPrefs[man];
			System.out.println(queue);
			for(int i = 0; i < manPref.length; i++){
				int wo = manPref[i];
				if(wstatus.get(wo) == -1){
					System.out.println("case 1:");
					wstatus.put(wo, man);
					System.out.println(man + "," + wo);
					break;
				}
				//she is married
				else{
					System.out.println("case 2:");
					HashMap<Integer, Integer> curWoPref = wmap.get(wo);
					System.out.println("cur pair: " + wstatus.get(wo) + "," + wo);
					System.out.println("man " + wstatus.get(wo) + " ranked " + curWoPref.get(wstatus.get(wo)));
					System.out.println("man " + man + " ranked " + curWoPref.get(man));
					//if she prefers the current man
					if(curWoPref.get(man) < curWoPref.get(wstatus.get(wo))){
						System.out.println("swapping husbands");
						queue.add(wstatus.get(wo)); //man she was married to now unmarried						wstatus.remove(wo);
						wstatus.remove(wo);
						wstatus.put(wo, man);
						System.out.println("queue after swap: " + queue);
						break;
					}
				}
			}
		}
		
		Iterator it = wstatus.entrySet().iterator();
		while(it.hasNext()){
			Map.Entry pair = (Map.Entry)it.next();
			int wo = (int) pair.getKey();
			int man = (int) pair.getValue();
			MatchedPair match = new MatchedPair(man, wo);
			MatchedPairsList.add(match);
		}
		
		//YOUR CODE ENDS HERE

		output(Args[1]);
	}

	public static void main(String [] Args) // Strings in Args are the name of the input file followed by the name of the output file
	{
		new Framework(Args);
	}
}
