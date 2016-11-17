import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

public class LootGenerator {
	public static Map<Integer, Pair> monsterMap; 
	public static Map<String, Tuple> baseItemMap;
	public static Map<String, Pair> baseStatMap;
	public static Map<Integer, FourTuple> suffixMap; 
	public static Map<Integer, FourTuple> prefixMap;

	/**
	 * Constructor
	 * @param monstat: source file containing monster name and treasure class
	 * @param treasureClass: source file containing treasure class and base items
	 * @param magicSuffix: source file containing suffix
	 * @param magicPrefix: source file containing prefix
	 * @param armor:source file containing base item statistics
	 * @throws IOException
	 */
	public LootGenerator (File monstat, File treasureClass, File magicSuffix, File magicPrefix, File armor) throws IOException{
		Scanner reader = new Scanner(monstat); 
		monsterMap = generateMonsterMap (reader);

		reader = new Scanner(treasureClass);
		baseItemMap = generateBaseItemMap(reader);

		reader = new Scanner(armor);
		baseStatMap = generateBaseStatMap(reader);

		reader = new Scanner(magicSuffix);
		suffixMap = generateSuffixMap(reader); 

		reader = new Scanner(magicPrefix);
		prefixMap = generatePrefixMap(reader); 

		reader.close();
	}

	/**
	 * @param reader a scanner
	 * @return map with an index attached to each entries of monsters as key, and a pair with monster name and treasure class as value
	 */
	public static Map <Integer, Pair> generateMonsterMap (Scanner reader) {
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
		return map;
	}
	/**
	 * @param reader, a scanner
	 * @return map with string of treasure class as key, and Tuple of three base items as value
	 */
	public static Map <String, Tuple> generateBaseItemMap (Scanner reader) {
		Map <String, Tuple> map = new HashMap<>(); 
		while (reader.hasNextLine()) {
			String line = reader.nextLine(); 
			String[] items = line.split("\t");
			map.put(items[0], new Tuple (items[1], items[2], items[3]));
		}
		return map; 
	}

	/**
	 * @param reader, a scanner
	 * @return map with string of base item as key, and Pair of two base item statistics as value
	 */
	public static Map <String, Pair> generateBaseStatMap (Scanner reader) {
		Map <String, Pair> map = new HashMap<>(); 
		while (reader.hasNextLine()) {
			String line = reader.nextLine(); 
			String[] entries = line.split("\t");
			map.put (entries[0], new Pair (entries[1], entries[2]));
		}
		return map;
	}

	/**
	 * @param reader, a scanner
	 * @return map with an index as key, and FourTuple of four tokens of suffix string as value
	 */
	public static Map <Integer, FourTuple> generateSuffixMap (Scanner reader) {
		Map <Integer, FourTuple> map = new HashMap<>(); 
		int count = 0; 
		while (reader.hasNextLine()) {
			String line = reader.nextLine(); 
			String[] suf = line.split("\t");
			map.put (count, new FourTuple (suf[0], suf[1], suf[2], suf[3]));
			count++; 
		}
		return map;
	}

	/**
	 * @param reader, a scanner
	 * @return map with an index as key, and FourTuple of four tokens of prefix string as value
	 */
	public static Map<Integer, FourTuple> generatePrefixMap (Scanner reader) {
		Map <Integer, FourTuple> map = new HashMap<>(); 
		int count = 0; 
		while (reader.hasNextLine()) {
			String line = reader.nextLine(); 
			String[] pre = line.split("\t");
			map.put (count, new FourTuple (pre[0], pre[1], pre[2], pre[3]));
			count++; 
		}
		return map; 
	}

	/**
	 * Generate a random number between low and high (low exclusive, high inclusive). 
	 * @param low, a lower bound integer(inclusive)
	 * @param high, a higher bound integer(inclusive)
	 * @return a random number between low and high
	 */
	public static int randomBetween (String low, String high) {
		Random randomGenerator = new Random(); 
		int dif = (int) Integer.parseInt (high) - Integer.parseInt(low); 
		int ret; 
		if (dif != 0) {
			int randnum = randomGenerator.nextInt(dif);
			ret = (int) Integer.parseInt (low) + ((randnum > 0) ? randnum: randnum+1);
		} else {
			ret = (int) Integer.parseInt (low) + 1;
		}
		return ret;
	}

	/**
	 * @param size, an integer
	 * @return a random number between 0 (inclusive) and size(exclusive)
	 */
	public static int getRandomNum (int size) {
		Random randomGenerator = new Random(); 
		return randomGenerator.nextInt(size);
	}

	/** 
	 * The method randomly chooses one of the three items in the Tuple, if the item chosen is a treasure class, 
	 * the method recursively looks into the treasure class and randomly pick a item out of the three associated
	 * items until the item chosen is a basic item (not a treasure class)
	 * 
	 * @param treasure, string representing treasure class
	 * @return a string representing the base item that is not a treasure class by recursing through the map
	 */
	public static String generateBaseItem (String treasure) {
		Tuple selectedItems = baseItemMap.get(treasure);
		int entry = getRandomNum(3); 
		String baseItem;

		if (entry == 0) {
			baseItem = selectedItems.ent1;
		} else if (entry == 1) {
			baseItem = selectedItems.ent2;
		} else {
			baseItem = selectedItems.ent3;
		}

		while (true) {
			if (!baseItemMap.containsKey(baseItem)) {
				return baseItem;
			} else {
				selectedItems = baseItemMap.get(baseItem);
				entry = getRandomNum(3); 
				if (entry == 0) {
					baseItem = selectedItems.ent1;
				} else if (entry == 1) {
					baseItem = selectedItems.ent2;
				} else {
					baseItem = selectedItems.ent3;
				}
			}
		}
	}

	/**
	 * @param armor a string representing base item
	 * @return a random number between the lower bound and upper bound statistics associated with the
	 * base item (inclusive)
	 */
	public static int generateBaseStats (String armor) {
		Pair p = baseStatMap.get(armor);
		return randomBetween(p.ent1, p.ent2);
	}

	/**
	 * Generate the affixes for our item such that a prefix and suffix each have 
	 * a 1/2 chance of being generated
	 */
	public static void generateAffix(String baseItem) {
		Random randomGenerator = new Random(); 
		int randomNumber = randomGenerator.nextInt(4);
		if (randomNumber == 0) {
			FourTuple suffix = suffixMap.get(getRandomNum(suffixMap.size()));
			String origin = suffix.ent1;
			String power = suffix.ent2;
			int level = randomBetween(suffix.ent3, suffix.ent4); 

			System.out.println(baseItem + " " + origin);
			System.out.println("Defense: " + generateBaseStats(baseItem));
			System.out.println(level + " " + power);
		} else if (randomNumber == 1) {
			FourTuple prefix = prefixMap.get(getRandomNum(prefixMap.size()));
			String origin = prefix.ent1;
			String power = prefix.ent2;
			int level = randomBetween(prefix.ent3, prefix.ent4); 

			System.out.println(origin + " " + baseItem);
			System.out.println("Defense: " + generateBaseStats(baseItem));
			System.out.println(level + " " + power);
		} else if (randomNumber == 2) {
			FourTuple suffix = suffixMap.get(getRandomNum(suffixMap.size()));
			String sufOrigin = suffix.ent1;
			String sufPower = suffix.ent2;
			int sufLevel = randomBetween(suffix.ent3, suffix.ent4); 

			FourTuple prefix = prefixMap.get(getRandomNum(prefixMap.size()));
			String preOrigin = prefix.ent1;
			String prePower = prefix.ent2;
			int preLevel = randomBetween(prefix.ent3, prefix.ent4); 

			System.out.println(preOrigin + " " + baseItem + " " + sufOrigin);
			System.out.println("Defense: " + generateBaseStats(baseItem));
			System.out.println(preLevel + " " + prePower);
			System.out.println(sufLevel + " " + sufPower);
		} else {
			System.out.println(baseItem);
			System.out.println("Defense: " + generateBaseStats(baseItem));
		}
	}

	/**
	 * Generate game by coordinating other helper functions. 
	 */
	public static void playGame() {
		Pair monster = monsterMap.get(getRandomNum(monsterMap.size()));
		String monsterName = monster.ent1; 
		String monsterTreasure = monster.ent2;

		System.out.println("Fighting " + monsterName + "."); 
		System.out.println("You have slain " + monsterName +"!" ); 
		System.out.println(monsterName + " dropped:");
		System.out.println ("");

		String baseItem = generateBaseItem (monsterTreasure);
		generateAffix (baseItem);
	}

	public static void main(String[] args) throws IOException {
		File monstat = new File ("monstats.txt"); 
		File treasureClass = new File ("TreasureClassEx.txt"); 
		File magicSuffix = new File ("MagicSuffix.txt"); 
		File magicPrefix = new File ("MagicPrefix.txt"); 
		File armor = new File ("armor.txt"); 

		LootGenerator l = new LootGenerator (monstat, treasureClass, magicSuffix, magicPrefix, armor);

		boolean cont = true;
		Scanner in = new Scanner(System.in);
		while (cont) {
			playGame();
			System.out.print("Fight again [y/n]?");
			String ans = in.next().toLowerCase();
			if(ans.equals("n")) {
				cont = false;
			}
			while (!ans.equals("y") && !ans.equals("n")) {
				System.out.print("Fight again [y/n]?");
				ans = in.next().toLowerCase();
			}
			System.out.println("");
		}
		in.close();
	}
}
