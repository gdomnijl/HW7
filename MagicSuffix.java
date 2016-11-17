/** 
	 * The class MagicSuffix holds four sub-fields:
	 * suffix: a string of suffix
	 * power: store the type of the power as a string 
	 * minVal: store the lower bound of the magic value as a string 
	 * maxVal: store the upper bound of the magic value as a string 
	 */
public class MagicSuffix {
	public String suffix; 
	public String power; 
	public String minVal; 
	public String maxVal; 

	public MagicSuffix (String ent1, String ent2, String ent3, String ent4) {
		suffix = ent1; 
		power = ent2; 
		minVal = ent3; 
		maxVal = ent4; 
	}
}
