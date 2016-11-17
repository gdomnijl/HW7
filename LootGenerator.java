import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

public class LootGenerator {
	public static Map<Integer, Monster> monsterMap; 
	public static Map<String, TreasureClass> baseItemMap;
	public static Map<String, Armor> baseStatMap;
	public static Map<Integer, MagicSuffix> suffixMap; 
	public static Map<Integer, MagicPrefix> prefixMap;

	/**
	 * Constructor
	 * @param monstat: source file containing monster name and treasure class
	 * @param treasureClass: source file containing treasure class and base items
	 * @param magicSuffix: source file containing suffix
	 * @param magicPrefix: source file containing prefix
	 * @param armor:source file containing base item statistics
	 * @throws IOException
	 */
	public LootGenerator (File monstat, File treasureClass, File magicSuffix, File magicPrefix, File armor) 
			throws IOException{
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
	 * @return map with an index attached to each entries of monsters as key, 
	 * and a pair with monster name and treasure class as value.
	 */
	public static Map <Integer, Monster> generateMonsterMap (Scanner reader) {
		Map <Integer, Monster> map = new HashMap<>(); 
		int counter = 0; 
		while (reader.hasNextLine()) {
			String line = reader.nextLine();
			String[] monsterInfo = line.split("\t");
			String monsterName = monsterInfo[0]; 
			String treasure = monsterInfo[3]; 

			map.put (counter, new Monster (monsterName, treasure));
			counter++;
		}
		return map;
	}
	/**
	 * @param reader, a scanner
	 * @return map with string of treasure class as key, and Tuple of three base items as value
	 */
	public static Map <String, TreasureClass> generateBaseItemMap (Scanner reader) {
		Map <String, TreasureClass> map = new HashMap<>(); 
		while (reader.hasNextLine()) {
			String line = reader.nextLine(); 
			String[] items = line.split("\t");
			map.put(items[0], new TreasureClass (items[1], items[2], items[3]));
		}
		return map; 
	}

	/**
	 * @param reader, a scanner
	 * @return map with string of base item as key, and Pair of two base item statistics as value
	 */
	public static Map <String, Armor> generateBaseStatMap (Scanner reader) {
		Map <String, Armor> map = new HashMap<>(); 
		while (reader.hasNextLine()) {
			String line = reader.nextLine(); 
			String[] entries = line.split("\t");
			map.put (entries[0], new Armor (entries[1], entries[2]));
		}
		return map;
	}

	/**
	 * @param reader, a scanner
	 * @return map with an index as key, and FourTuple of four tokens of suffix string as value
	 */
	public static Map <Integer, MagicSuffix> generateSuffixMap (Scanner reader) {
		Map <Integer, MagicSuffix> map = new HashMap<>(); 
		int count = 0; 
		while (reader.hasNextLine()) {
			String line = reader.nextLine(); 
			String[] suf = line.split("\t");
			map.put (count, new MagicSuffix (suf[0], suf[1], suf[2], suf[3]));
			count++; 
		}
		return map;
	}

	/**
	 * @param reader, a scanner
	 * @return map with an index as key, and FourTuple of four tokens of prefix string as value
	 */
	public static Map<Integer, MagicPrefix> generatePrefixMap (Scanner reader) {
		Map <Integer, MagicPrefix> map = new HashMap<>(); 
		int count = 0; 
		while (reader.hasNextLine()) {
			String line = reader.nextLine(); 
			String[] pre = line.split("\t");
			map.put (count, new MagicPrefix (pre[0], pre[1], pre[2], pre[3]));
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
		TreasureClass selectedItems = baseItemMap.get(treasure);
		int entry = getRandomNum(3); 
		String baseItem;

		if (entry == 0) {
			baseItem = selectedItems.item1;
		} else if (entry == 1) {
			baseItem = selectedItems.item2;
		} else {
			baseItem = selectedItems.item3;
		}

		while (true) {
			if (!baseItemMap.containsKey(baseItem)) {
				return baseItem;
			} else {
				selectedItems = baseItemMap.get(baseItem);
				entry = getRandomNum(3); 
				if (entry == 0) {
					baseItem = selectedItems.item1;
				} else if (entry == 1) {
					baseItem = selectedItems.item2;
				} else {
					baseItem = selectedItems.item3;
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
		Armor b = baseStatMap.get(armor);
		return randomBetween(b.minVal, b.maxVal);
	}

	/**
	 * Generate the affixes for our item such that a prefix and suffix each have 
	 * a 1/2 chance of being generated
	 */
	public static void generateAffix(String baseItem) {
		Random randomGenerator = new Random(); 
		int randomNumber = randomGenerator.nextInt(4);
		if (randomNumber == 0) {
			MagicSuffix suffix = suffixMap.get(getRandomNum(suffixMap.size()));
			String origin = suffix.suffix;
			String power = suffix.power;
			int level = randomBetween(suffix.minVal, suffix.maxVal); 

			System.out.println(baseItem + " " + origin);
			System.out.println("Defense: " + generateBaseStats(baseItem));
			System.out.println(level + " " + power);
		} else if (randomNumber == 1) {
			MagicPrefix prefix = prefixMap.get(getRandomNum(prefixMap.size()));
			String origin = prefix.prefix;
			String power = prefix.power;
			int level = randomBetween(prefix.minVal, prefix.maxVal); 

			System.out.println(origin + " " + baseItem);
			System.out.println("Defense: " + generateBaseStats(baseItem));
			System.out.println(level + " " + power);
		} else if (randomNumber == 2) {
			MagicSuffix suffix = suffixMap.get(getRandomNum(suffixMap.size()));
			String sufOrigin = suffix.suffix;
			String sufPower = suffix.power;
			int sufLevel = randomBetween(suffix.minVal, suffix.maxVal); 

			MagicPrefix prefix = prefixMap.get(getRandomNum(prefixMap.size()));
			String preOrigin = prefix.prefix;
			String prePower = prefix.power;
			int preLevel = randomBetween(prefix.minVal, prefix.maxVal); 

			System.out.println(preOrigin + " " + baseItem + " " + sufOrigin);
			System.out.println("Defense: " + generateBaseStats(baseItem));
			System.out.println(preLevel + " " + prePower);
			System.out.println(sufLevel + " " + sufPower);
		} else {
			System.out.println(baseItem);
			System.out.println("Defense: " + generateBaseStats(baseItem));
		}
	}

	public static String pickMonster (Monster monster) {
		return monster.name;
	}

	public static String fetchTreasureClass (Monster monster) {
		return monster.treasure;
	}


	/**
	 * Generate game by coordinating other helper functions. 
	 */
	public static void playGame() {
		Monster monster = monsterMap.get(getRandomNum(monsterMap.size()));
		String monsterName = pickMonster(monster); 
		String monsterTreasure = fetchTreasureClass(monster);

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
