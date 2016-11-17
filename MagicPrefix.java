/** 
	 * The class MagicPrefix holds four sub-fields:
	 * prefix: a string of prefix
	 * power: store the type of the power as a string 
	 * minVal: store the lower bound of the magic value as a string 
	 * maxVal: store the upper bound of the magic value as a string 
	 */
public class MagicPrefix {
	public String prefix; 
	public String power; 
	public String minVal; 
	public String maxVal; 

	public MagicPrefix (String ent1, String ent2, String ent3, String ent4) {
		prefix = ent1; 
		power = ent2; 
		minVal = ent3; 
		maxVal = ent4; 
	}
}
