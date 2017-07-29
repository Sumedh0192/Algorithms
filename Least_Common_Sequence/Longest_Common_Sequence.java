/*
 * Class: Longest_Common_Sequence 
 * Definition: Find out the Longest Common Sub-sequence between 2 strings
 * Created By: Sumedh Ambokar
*/

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;

public class Longest_Common_Sequence {
	public static String String1;
	public static String String2;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String[] CompareStringArray = readFile();
		getLCS(CompareStringArray[0], CompareStringArray[1]);
	}

	/*	
	 * Method Name: readFile
	 * Description:	Method used to read Graph from the given file
	 * Parameters: void
	 * Return Type: void
	 */
	static String[] readFile(){
		try{
			String[] CompareStringArray = new String[2];
			BufferedReader buffrdr = new BufferedReader(new InputStreamReader
						(new FileInputStream(new File("input.txt")), "UTF8"));
			CompareStringArray[1] = buffrdr.readLine();
			CompareStringArray[0] = buffrdr.readLine();
			buffrdr.close();
			return CompareStringArray;
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	/*	
	 * Method Name: getLCS
	 * Description:	Computes the LCS for the 2 passed Strings
	 * Parameters: String String1, String String2
	 * Return Type: String
	 */
	static void getLCS(String String1, String String2){
		try{
			Integer[][] OptimumLCSArray = new Integer[String1.length() + 1][String2.length() + 1];
			Integer[][]	PathTrackingArray = new Integer[String1.length() + 1][String2.length() + 1]; 
			// PathTrackingArray is used to trace back the LCS and provide the output. It stores 3 values 1, 2 and 3 
			// 1 --> to move across, 2--> to move behind and 3 --> to move up
			for(Integer i = 0; i <= String1.length(); i++){
				for(Integer j = 0; j <= String2.length(); j++){
					if(i == 0 || j == 0){ // Set the initial element of the first row and first column
						OptimumLCSArray[i][j] = 0;
						PathTrackingArray[i][j] = 0;
					}else if(String1.charAt(i - 1) == String2.charAt(j - 1)){ // If a perfect match between chars of string then add to LCS length
						OptimumLCSArray[i][j] = OptimumLCSArray[i-1][j-1] + 1;
						PathTrackingArray[i][j] = 1;
					}else{
						// If not a perfect match then store the value of longest LCS from one step back
						OptimumLCSArray[i][j] = (OptimumLCSArray[i][j-1] > OptimumLCSArray[i-1][j])? OptimumLCSArray[i][j-1] : OptimumLCSArray[i-1][j];
						PathTrackingArray[i][j] = (OptimumLCSArray[i][j-1] > OptimumLCSArray[i-1][j])? 2 : 3;
					}
				}
			}
			Integer i, j;
			i = String1.length();
			j = String2.length();
			String LCS= "";
			while(i > 0 && j > 0){ // form the LCS by backtracking the array
				if(PathTrackingArray[i][j] == 1){
					LCS = String1.charAt(i-1) + LCS;
					i--; j--;
					System.out.println(LCS);
				}else if(PathTrackingArray[i][j] == 2){
					j--;
				}else if(PathTrackingArray[i][j] == 3){
					i--;
				}
			}
			outputResult(LCS);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/*	
	 * Method Name: outputResult
	 * Description:	Prints the LCS of input strings in required format
	 * Parameters: String LCS
	 * Return Type: void
	 */
	static void outputResult(String LCS){
		try{
			Writer Filewtr = new BufferedWriter(new OutputStreamWriter
					(new FileOutputStream(new File("output.txt")), "UTF8"));
			Filewtr.write(LCS.length() + "\n");
			Filewtr.write(LCS);
			Filewtr.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
