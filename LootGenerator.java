import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

public class LootGenerator {
	public static Pair pickMonster(Scanner reader) {
		Map <Integer, Pair> map = new HashMap<>(); 
		int counter = 0; 
		while (reader.hasNextLine()) {
			String line = reader.nextLine();
			String[] monsterInfo = line.split("\t");
			String monsterName = monsterInfo[0]; 
			String treasure = monsterInfo[3]; 

			map.put (counter, new Pair (monsterName, treasure));
			counter++;
		}

		Random randomGenerator = new Random(); 
		int randomNumber = randomGenerator.nextInt(map.size());

		Pair monster = map.get(randomNumber); 
		return monster;
	}

	public static String generateBaseItem (String treasure, Scanner reader) {
		Map <String, String[]> map = new HashMap<>(); 
		while (reader.hasNextLine()) {
			String line = reader.nextLine(); 
			String[] tokens = line.split("\t");
			String[] item = {tokens[1], tokens[2], tokens[3]}; 
			map.put (tokens[0], item);
		}

		Random randomGenerator = new Random(); 
		int randomNumber = randomGenerator.nextInt(3);

		String[] selectedItems = map.get(treasure);
		String baseItem = selectedItems[randomNumber];

		while (true) {
			if (!map.containsKey(baseItem)) {
				return baseItem;
			} else {
				selectedItems = map.get(baseItem);
				baseItem = selectedItems[randomNumber];
			}
		}
	}

	public static int generateBaseStats(String armor, Scanner reader) {
		Map <String, Integer> map = new HashMap<>(); 
		while (reader.hasNextLine()) {
			String line = reader.nextLine(); 
			String[] tokens = line.split("\t");

			Random randomGenerator = new Random(); 
			int dif = (int) Integer.parseInt(tokens[2]) - Integer.parseInt(tokens[1]); 
			int ret = (int) Integer.parseInt(tokens[1]) + randomGenerator.nextInt(dif);

			map.put (tokens[0], ret);
		}

		return map.get(armor); 
	}

	public static String[] generateSuffix(Scanner reader) {
		Map <Integer, String[]> map = new HashMap<>(); 

		int count = 0; 
		while (reader.hasNextLine()) {
			String line = reader.nextLine(); 
			String[] suf = line.split("\t");
			map.put (count, suf);
			count++; 
		}

		Random randomGenerator = new Random(); 
		int randomNumber = randomGenerator.nextInt(map.size());

		return map.get(randomNumber); 
	}

	public static String[] generatePrefix(Scanner reader) {
		Map <Integer, String[]> map = new HashMap<>(); 
		int count = 0; 
		while (reader.hasNextLine()) {
			String line = reader.nextLine(); 
			String[] suf = line.split("\t");
			map.put (count, suf);
			count++; 
		}

		Random randomGenerator = new Random(); 
		int randomNumber = randomGenerator.nextInt(map.size());

		return map.get(randomNumber); 
	}


	public static int randomBetween(String low, String high) {
		Random randomGenerator = new Random(); 
		int dif = (int) Integer.parseInt (high) - Integer.parseInt(low); 
		int ret; 
		if (dif != 0) {
			ret = (int) Integer.parseInt (low) + randomGenerator.nextInt(dif);
		} else {
			ret = (int) Integer.parseInt (low);
		}
		return ret;
	}

	public static void main(String[] args) throws IOException {
		File monstat = new File ("/home/hejinlin/workspace/LootGenerator/small/monstats.txt"); 
		File monstatLarge = new File ("/home/hejinlin/workspace/LootGenerator/large/monstats.txt"); 
		Scanner reader = new Scanner (monstat); 

		Pair monster = pickMonster (reader);
		String monsterName = monster.name; 
		String monsterTreasure = monster.treasure;

		System.out.println("Fighting " + monsterName + "."); 
		System.out.println("You have slain " + monsterName +"!" ); 
		System.out.println(monsterName + " dropped:");
		System.out.println ("");

		File treasureClass = new File ("/home/hejinlin/workspace/LootGenerator/small/TreasureClassEx.txt"); 
		reader = new Scanner (treasureClass);
		String baseItem = generateBaseItem (monsterTreasure, reader);

		File magicSuffix = new File ("/home/hejinlin/workspace/LootGenerator/small/MagicSuffix.txt"); 
		Scanner suffixReader = new Scanner (magicSuffix);

		File magicPrefix = new File ("/home/hejinlin/workspace/LootGenerator/small/MagicPrefix.txt"); 
		Scanner prefixReader = new Scanner (magicPrefix);

		File armor = new File ("/home/hejinlin/workspace/LootGenerator/small/armor.txt"); 
		reader = new Scanner (armor);

		Random randomGenerator = new Random(); 
		int randomNumber = randomGenerator.nextInt(4);

		if (randomNumber == 0) {
			String[] suffix = generateSuffix(suffixReader);
			String origin = suffix[0];
			String power = suffix[1];
			int level = randomBetween(suffix[2], suffix[3]);
			System.out.println(baseItem + " " + origin);
			System.out.println("Defense: " + generateBaseStats (baseItem, reader));
			System.out.println(level + " " + power);
		} else if (randomNumber == 1) {
			String[] prefix = generatePrefix(prefixReader);
			String origin = prefix[0];
			String power = prefix[1];
			int level = randomBetween(prefix[2], prefix[3]);

			System.out.println(origin + " " + baseItem);
			System.out.println("Defense: " + generateBaseStats (baseItem, reader));
			System.out.println(level + " " + power);
		} else if (randomNumber == 2) {
			String[] suffix = generateSuffix(suffixReader);
			String sufOrigin = suffix[0];
			String sufPower = suffix[1];
			int sufLevel = randomBetween(suffix[2], suffix[3]);

			String[] prefix = generatePrefix(prefixReader);
			String preOrigin = prefix[0];
			String prePower = prefix[1];
			int preLevel = randomBetween(prefix[2], prefix[3]);

			System.out.println(preOrigin + " " + baseItem + " " + sufOrigin);
			System.out.println("Defense: " + generateBaseStats (baseItem, reader));
			System.out.println(preLevel + " " + prePower);
			System.out.println(sufLevel + " " + sufPower);
		} else {
			System.out.println(baseItem);
			System.out.println("Defense: " + generateBaseStats (baseItem, reader));
		}

		reader.close(); 
	}
}
